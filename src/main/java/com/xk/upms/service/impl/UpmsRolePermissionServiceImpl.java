package com.xk.upms.service.impl;

import com.xk.upms.dao.mapper.UpmsRolePermissionMapper;
import com.xk.upms.dao.repository.UpmsPermissionRepository;
import com.xk.upms.dao.repository.UpmsRolePermissionRepository;
import com.xk.upms.dao.repository.UpmsSystemRepository;
import com.xk.upms.model.bo.UpmsPermissionReq;
import com.xk.upms.model.bo.UpmsRolePermissionReq;
import com.xk.upms.model.bo.UpmsRolePermissionSaveReq;
import com.xk.upms.model.enums.PermissionAction;
import com.xk.upms.model.po.UpmsPermission;
import com.xk.upms.model.po.UpmsRolePermission;
import com.xk.upms.model.po.UpmsSystem;
import com.xk.upms.model.vo.UpmsPermissionResp;
import com.xk.upms.model.vo.UpmsRolePermissionSaveResp;
import com.xk.upms.model.vo.UpmsRolePermissionWithActiveResp;
import com.xk.upms.service.UpmsPermissionService;
import com.xk.upms.service.UpmsRolePermissionService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * UpmsRolePermissionService 實現
 * Created by yuan on 2022/06/24
 * @author yuan
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UpmsRolePermissionServiceImpl implements UpmsRolePermissionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpmsRolePermissionServiceImpl.class);

    @Autowired
    private UpmsRolePermissionRepository upmsRolePermissionRepository;
    @Autowired
    private UpmsPermissionRepository upmsPermissionRepository;
    @Autowired
    private UpmsRolePermissionMapper upmsRolePermissionMapper;
    @Autowired
    private UpmsPermissionService upmsPermissionService;
    @Autowired
    private UpmsSystemRepository upmsSystemRepository;

    @Override
    public void checkCountSameWithPermission(long roleId) {
        long cntAllPermission = upmsPermissionRepository.count();
        int cntPermissionOfRole = upmsRolePermissionRepository.findByRoleId(roleId).size();

        if (cntPermissionOfRole == cntAllPermission * PermissionAction.size()) {
            return;
        }
        UpmsPermissionReq req = new UpmsPermissionReq();
        req.setType((byte) 2);
        List<UpmsPermissionResp> permissions = upmsPermissionService.listBy(req);

        // 将 id 转换为 String[]
        String[] permissionIds = permissions.stream()
                .map(UpmsPermissionResp::getId)
                .map(String::valueOf) // 转换为字符串
                .toArray(String[]::new);
        this.rolePermission(roleId, null, permissionIds);
    }

    @Override
    public UpmsRolePermissionSaveResp create(UpmsRolePermissionSaveReq resources) {
        UpmsRolePermissionSaveResp result = new UpmsRolePermissionSaveResp();

        for (PermissionAction action : PermissionAction.values()) {
            // 创建一个新的角色权限实体，并将其保存到数据库中
            UpmsRolePermission rolePermission = new UpmsRolePermission();
            rolePermission.setRoleId(resources.getRoleId());
            rolePermission.setPermissionId(resources.getPermissionId()); // 确保 permissionId 转换为 long
            rolePermission.setAction(action); // 设置当前的权限操作（READ, WRITE, CREATE 等）
            // admin預設開啟，其他預設關閉
            rolePermission.setActive(resources.getRoleId() == (long) 1 ? true : false); // 激活权限

            // 保存到数据库
            UpmsRolePermission entity = upmsRolePermissionRepository.save(rolePermission);
        }

        return null;
    }

    /**
     * 1. 找到選中系統的功能數量
     * 2. 尋找對應主要資料內容（main）
     * 3. 若數量對不上找出未對上的功能，預設false
     * @param resource
     * @return
     */
    @Override
    public List listBy(UpmsRolePermissionReq resource) {
        List<UpmsRolePermissionWithActiveResp> result;
        UpmsSystem checkSystem = null;
        long cntPermission = 0;

        // 檢查參數
        if (StringUtils.isNotBlank(resource.getSystemCode())) {
            checkSystem = upmsSystemRepository.findOneByName(resource.getSystemCode());
            if (checkSystem == null) {
//                throw new NullPointerException("UpmsRolePermissionService.listBy()中，systemCode參數錯誤");
            } else {
                // 1. 找到選中系統的功能數量
                UpmsPermission entity = new UpmsPermission();
                entity.setType((byte) 2);
                entity.setSystemId(checkSystem.getId());
                Example<UpmsPermission> example = Example.of(entity);
                cntPermission = upmsPermissionRepository.count(example);
            }
        }

        // 2. 尋找對應主要資料內容（main）
        Long roleId = Long.valueOf(resource.getRoleId());
        result = upmsRolePermissionMapper.findBy(roleId, resource.getSystemCode());
        // 3. 若數量對不上找出未對上的功能，預設false
        if (StringUtils.isNotBlank(resource.getSystemCode()) && cntPermission != result.size() / PermissionAction.size()) {
            List<Long> ids = result.stream()
                .collect(Collectors.toMap(
                        UpmsRolePermissionWithActiveResp::getId, // 以 `id` 作為 key
                        permission -> permission, // 以原始對象作為 value
                        (existing, replacement) -> existing // 如果有重複的 key，保留原始對象
                ))
                .values()
                .stream()
                .map(UpmsRolePermissionWithActiveResp::getId) // 直接提取每個去重後對象的 `id`
                .collect(Collectors.toList()); // 收集到一個 List 中


            List<UpmsPermission> permissions = upmsPermissionRepository.findByIdNotInAndTypeAndSystemId(ids, (byte) 2, checkSystem.getId());
            for (UpmsPermission permission : permissions) {
                for (PermissionAction action : PermissionAction.values()) {
                    UpmsRolePermissionWithActiveResp resp = new UpmsRolePermissionWithActiveResp();
                    resp.setId(permission.getId());
                    resp.setName(permission.getName());
                    resp.setActive(false);
                    resp.setAction(action);
                    result.add(resp);
                }
            }
        }
        return result;
    }

    @Override
    public List rolePermission(long roleId, String systemCode, String[] permissionIds) {
        List<UpmsRolePermission> result = new ArrayList<>();

        // 使用 HashSet 去重複
        Set<Long> reqIds = new HashSet<>();

        // 循環每個值，提取數字並添加到 Set 中
        for (String value : permissionIds) {
            // 提取字符串中的數字部分
            String number = value.replaceAll("\\D+", "");
            if (!number.isEmpty()) {
                reqIds.add(Long.parseLong(number));
            }
        }


        // 检查是否是首次创建角色（假设服务层有一个方法可以检查角色是否已存在权限）
        boolean isFirstRoleCreation = upmsRolePermissionRepository.findByRoleId(roleId).isEmpty();

        // 如果是首次创建角色，插入所有权限的 "READ, WRITE, CREATE" 操作
        if (isFirstRoleCreation) {
            LOGGER.info("First time creating role with ID: {}. Inserting all permissions with CREATE action.", roleId);

            for (Long permissionId : reqIds) {
                for (PermissionAction action : PermissionAction.values()) {
                    // 创建一个新的角色权限实体，并将其保存到数据库中
                    UpmsRolePermission rolePermission = new UpmsRolePermission();
                    rolePermission.setRoleId(roleId);
                    rolePermission.setPermissionId(permissionId); // 确保 permissionId 转换为 long
                    rolePermission.setAction(action); // 设置当前的权限操作（READ, WRITE, CREATE 等）
                    // admin預設開啟，其他預設關閉
                    rolePermission.setActive("1".equals(String.valueOf(roleId)) ? true : false); // 激活权限

                    // 保存到数据库
                    upmsRolePermissionRepository.save(rolePermission);
                    result.add(rolePermission);
                }
            }
            LOGGER.info("Inserted all permissions with CREATE action for role ID: {}", roleId);
        }

        /**
         * 若已經有權限存在
         * 0. 仍要判斷是否有新頁面
         * 1. 撈出所有該角色所屬系統權限設定
         * 2. 與前端回傳資料比對
         * 3. 若有更新，協助更新資料
         * 4. 最後反向網parent曾開啟權限（多次）
         */
        if (permissionIds != null) {
            List<UpmsRolePermission> saveEntities = new ArrayList<>();
            // * 0. 仍要判斷是否有新頁面
            List<Long> beenPermissionIds = upmsRolePermissionRepository.findPermissionIdsByRoleId(roleId);
            // 把 beenPermissionIds 转换为 Set 用于快速查找
            Set<Long> permissionIdSet = new HashSet<>(beenPermissionIds);
            List<Long> additionalIds = new ArrayList<>();

            for (String permissionKey : permissionIds) {
                String[] permissionArr = permissionKey.split("_");
                Long lp = Long.parseLong(permissionArr[0]);
                // 如果 permissionId 不在 permissionIdSet 中，添加到 additionalIds 中
                if (!permissionIdSet.contains(lp)) {
                    UpmsRolePermissionSaveReq req = new UpmsRolePermissionSaveReq();
                    req.setRoleId(roleId);
                    req.setPermissionId(lp);
                    try {
                        PermissionAction action = PermissionAction.valueOf(permissionArr[1]);
                        req.setAction(action);
                    } catch (IllegalArgumentException e) {
                        // 如果 permissionArr[1] 不在 PermissionAction 枚舉中，會捕捉到例外
                        System.out.println("無效的 PermissionAction 值: " + permissionArr[1]);
                    }
                    this.create(req);
                }
            }
//         * 1. 撈出所有該角色所屬系統權限設定
            List<UpmsRolePermissionWithActiveResp> allPermissionList = upmsRolePermissionMapper.findBy(roleId, systemCode);

            for (UpmsRolePermissionWithActiveResp urp : allPermissionList) {
                boolean isCheckBoxValuesRtn = false;
                Boolean isCheckActionActive = urp.getActive();

                // 迭代每个复选框的值
                for (String value : permissionIds) {
                    if (value.equals(urp.getId()+"_"+urp.getAction())) {
                        isCheckBoxValuesRtn = true;
                    }
                }
                if (isCheckBoxValuesRtn == isCheckActionActive) {
                    // do Nothing
                } else {
                    UpmsRolePermission entity = new UpmsRolePermission();
                    // 條件
                    entity.setRoleId(roleId);
                    entity.setPermissionId(urp.getId());
                    entity.setAction(urp.getAction());

                    entity = this.safeSaveURP(entity, isCheckBoxValuesRtn);
                    saveEntities.add(entity);
                }
            }



            upmsRolePermissionRepository.saveAll(saveEntities);
        }
        this.checkPermissionLevelOne(roleId);
        this.checkPermissionLevelOne(roleId);
        this.checkPermissionLevelOne(roleId);
        return result;
    }

    @Override
    public List listByAuth(long[] roleId) {
        List<UpmsRolePermissionWithActiveResp> result = new ArrayList<>();

        for (long id : roleId) {
            UpmsRolePermissionReq resource = new UpmsRolePermissionReq();
            resource.setRoleId(String.valueOf(id));

            result.addAll(this.listBy(resource));
        }
        return result;
    }

    private UpmsRolePermission safeSaveURP(UpmsRolePermission entity, boolean setActive) {
        // 使用 Example 对象查找数据库中的匹配实体
        Example<UpmsRolePermission> example = Example.of(entity);
        Optional<UpmsRolePermission> optionalEntity = upmsRolePermissionRepository.findOne(example);

        if (optionalEntity.isPresent()) {
            // 获取数据库中的实体对象
            entity = optionalEntity.get();
            // 更新
//            entity.setActive(isCheckBoxValuesRtn);
            entity.setActive(setActive);
            entity.setUpdateTime(new Date());
        } else {
            // 若没有找到对应记录，可以选择新增一个记录
//            entity.setActive(isCheckBoxValuesRtn);
            entity.setActive(setActive);
            entity.setCreateTime(new Date());
        }
        return entity;
    }

    private void checkPermissionLevelOne(long roleId) {
        List<UpmsRolePermission> entities = upmsRolePermissionRepository.findByRoleIdAndActive(roleId, true);
        // 1. 提取所有的 permissionId
        List<Long> permissionIds = entities.stream()
                .map(UpmsRolePermission::getPermissionId)
                .collect(Collectors.toList());
        // 2. 查询所有权限的详细信息
        List<UpmsPermission> permissions = upmsPermissionRepository.findAllById(permissionIds);
        // 3. 提取所有的 parentId 并去重複
        Set<Long> parentIds = permissions.stream()
                .map(UpmsPermission::getPid)
                .filter(Objects::nonNull) // 过滤掉为 null 的 parentId
                .collect(Collectors.toSet());
        // 4. 遍历父菜单 ID，并开启这些菜单
        List<UpmsRolePermission> saveEntities = new ArrayList<>();
        for (Long parentId : parentIds) {
            UpmsRolePermission entity = new UpmsRolePermission();
            // 條件
            entity.setRoleId(roleId);
            entity.setPermissionId(parentId);
            entity.setAction(PermissionAction.READ); // 假设使用 READ 权限
            // admin預設開啟，其他預設關閉
            entity.setActive("1".equals(String.valueOf(roleId)) ? true : false); // 激活权限

            entity = this.safeSaveURP(entity, true);
            saveEntities.add(entity);
        }
        // 5. 保存更新后的父菜单权限
        upmsRolePermissionRepository.saveAll(saveEntities);
    }

}

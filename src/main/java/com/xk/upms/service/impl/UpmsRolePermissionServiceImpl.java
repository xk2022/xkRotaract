package com.xk.upms.service.impl;

import com.xk.upms.dao.mapper.UpmsRolePermissionMapper;
import com.xk.upms.dao.repository.UpmsPermissionRepository;
import com.xk.upms.dao.repository.UpmsRolePermissionRepository;
import com.xk.upms.model.bo.UpmsRolePermissionReq;
import com.xk.upms.model.enums.PermissionAction;
import com.xk.upms.model.po.UpmsPermission;
import com.xk.upms.model.po.UpmsRolePermission;
import com.xk.upms.model.vo.UpmsPermissionResp;
import com.xk.upms.model.vo.UpmsRolePermissionWithActiveResp;
import com.xk.upms.service.UpmsPermissionService;
import com.xk.upms.service.UpmsRolePermissionService;
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

    @Override
    public void checkCountSameWithPermission(long roleId) {
        long cntAllPermission = upmsPermissionRepository.count();
        int cntPermissionOfRole = upmsRolePermissionRepository.findByRoleId(roleId).size();

        if (cntPermissionOfRole == cntAllPermission) {
            return;
        }
        List<UpmsPermissionResp> permissions = upmsPermissionService.list();

        // 将 id 转换为 String[]
        String[] checkBoxValues = permissions.stream()
                .map(UpmsPermissionResp::getId)
                .map(String::valueOf) // 转换为字符串
                .toArray(String[]::new);
        this.rolePermission(roleId, null, checkBoxValues);
    }

    @Override
    public List listBy(UpmsRolePermissionReq resource) {
        List<UpmsRolePermissionWithActiveResp> result = new ArrayList<>();

        Long roleId = Long.valueOf(resource.getRoleId());
        return upmsRolePermissionMapper.findBy(roleId, resource.getSystemCode());
    }

    @Override
    public List rolePermission(long roleId, String systemCode, String[] checkBoxValues) {
        List<UpmsRolePermission> result = new ArrayList<>();

        // 检查是否是首次创建角色（假设服务层有一个方法可以检查角色是否已存在权限）
        boolean isFirstRoleCreation = upmsRolePermissionRepository.findByRoleId(roleId).isEmpty();

        // 如果是首次创建角色，插入所有权限的 "READ, WRITE, CREATE" 操作
        if (isFirstRoleCreation) {
            LOGGER.info("First time creating role with ID: {}. Inserting all permissions with CREATE action.", roleId);

            for (String permissionId : checkBoxValues) {
                for (PermissionAction action : PermissionAction.values()) {
                    // 创建一个新的角色权限实体，并将其保存到数据库中
                    UpmsRolePermission rolePermission = new UpmsRolePermission();
                    rolePermission.setRoleId(roleId);
                    rolePermission.setPermissionId(Long.parseLong(permissionId)); // 确保 permissionId 转换为 long
                    rolePermission.setAction(action); // 设置当前的权限操作（READ, WRITE, CREATE 等）
                    // admin預設開啟，其他預設關閉
                    rolePermission.setActive(roleId == (long) 1 ? true : false); // 激活权限

                    // 保存到数据库
                    upmsRolePermissionRepository.save(rolePermission);
                    result.add(rolePermission);
                }
            }
            LOGGER.info("Inserted all permissions with CREATE action for role ID: {}", roleId);
        }

        /**
         * 若已經有權限存在
         * 1. 撈出所有該角色所屬系統權限設定
         * 2. 與前端回傳資料比對
         * 3. 若有更新，協助更新資料
         */
        if (checkBoxValues != null) {
            List<UpmsRolePermission> saveEntities = new ArrayList<>();
            List<UpmsRolePermissionWithActiveResp> allPermissionList = upmsRolePermissionMapper.findBy(roleId, systemCode);

            for (UpmsRolePermissionWithActiveResp urp : allPermissionList) {
                boolean isCheckBoxValuesRtn = false;

                String checkActionCode = urp.getId() + "_" + urp.getAction();
                Boolean isCheckActionActive = urp.getActive();

                // 迭代每个复选框的值
                for (String value : checkBoxValues) {
                    if (value.equals(checkActionCode)) {
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

                    entity = this.safeSaveURP(roleId, entity, isCheckBoxValuesRtn);
                    saveEntities.add(entity);
                }
            }
            upmsRolePermissionRepository.saveAll(saveEntities);
        }
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

    private UpmsRolePermission safeSaveURP(long roleId, UpmsRolePermission entity, boolean setActive) {
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

            entity = this.safeSaveURP(roleId, entity, true);
            saveEntities.add(entity);
        }
        // 5. 保存更新后的父菜单权限
        upmsRolePermissionRepository.saveAll(saveEntities);
    }

}

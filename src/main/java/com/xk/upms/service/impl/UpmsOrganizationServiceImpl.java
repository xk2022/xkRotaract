package com.xk.upms.service.impl;

import com.xk.cms.model.bo.CmsClubSaveReq;
import com.xk.cms.model.bo.CmsUserSaveReq;
import com.xk.cms.service.CmsClubService;
import com.xk.cms.service.CmsUserService;
import com.xk.common.util.GenericUpdateService;
import com.xk.common.util.XkBeanUtils;
import com.xk.common.util.XkTypeUtils;
import com.xk.upms.dao.repository.UpmsOrganizationRepository;
import com.xk.upms.dao.repository.UpmsOrganizationUserRepository;
import com.xk.upms.dao.repository.UpmsRoleRepository;
import com.xk.upms.model.bo.UpmsOrganizationReq;
import com.xk.upms.model.bo.UpmsOrganizationSaveReq;
import com.xk.upms.model.bo.UpmsUserSaveReq;
import com.xk.upms.model.po.UpmsOrganization;
import com.xk.upms.model.vo.UpmsOrganizationResp;
import com.xk.upms.model.vo.UpmsOrganizationSaveResp;
import com.xk.upms.model.vo.UpmsOrganizationTreeResp;
import com.xk.upms.model.vo.UpmsUserResp;
import com.xk.upms.service.UpmsOrganizationService;
import com.xk.upms.service.UpmsUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * UpmsOrganizationService 實現
 *
 * @author yuan Created on 2022/06/24.
 * @author yuan Updated on 2024/10/28 with code optimization.
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class UpmsOrganizationServiceImpl implements UpmsOrganizationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpmsOrganizationServiceImpl.class);

    @Autowired
    private UpmsOrganizationRepository upmsOrganizationRepository;
    @Autowired
    private UpmsOrganizationUserRepository upmsOrganizationUserRepository;
    @Autowired
    private UpmsUserService upmsUserService;
    @Autowired
    private UpmsRoleRepository upmsRoleRepository;
    @Autowired
    private CmsUserService cmsUserService;
    @Autowired
    private CmsClubService cmsClubService;

    @Override
    public List<UpmsOrganizationResp> list(UpmsOrganizationReq resources) {
        // parentCode
        if (resources != null && StringUtils.isNotBlank(resources.getParentCode())) {
            UpmsOrganization parentOrg = upmsOrganizationRepository.findByCode(resources.getParentCode());
            resources.setParentId(String.valueOf(parentOrg.getId()));
        }
        // 設置 Example 條件，如果 resources 為 null 則返回 null
        Example<UpmsOrganization> example = resources == null ? null :
                Example.of(XkBeanUtils.copyProperties(resources, UpmsOrganization::new), ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase());
        // 提取排序規則，避免重複定義
        Sort sort = Sort.by(Sort.Order.asc("level"), Sort.Order.asc("orders"));

        // 記錄查詢條件和排序規則
        LOGGER.info("查詢 UpmsOrganization，條件: {}", resources);
        LOGGER.info("排序規則: {}", sort);

        // 查詢數據並轉換為 UpmsOrganizationResp 列表
        List<UpmsOrganization> organizations = (example == null)
                ? upmsOrganizationRepository.findAll(sort)
                : upmsOrganizationRepository.findAll(example, sort);

        // 記錄查詢結果數量（僅在 DEBUG 級別下）
        LOGGER.info("查詢結果數量: {}", organizations.size());

        return XkBeanUtils.copyListProperties(organizations, UpmsOrganizationResp::new);
    }
    // convertToResp

    @Override
    public UpmsOrganizationResp findById(Long id) {
        UpmsOrganization entity = upmsOrganizationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("UpmsOrganization with ID " + id + " not found"));
//        Long cntUser = upmsOrganizationUserRepository.countByOrganizationId(entity.getId());
        return XkBeanUtils.copyProperties(entity, UpmsOrganizationResp::new);
    }

    @Override
    public UpmsOrganizationResp findByCode(String reqCode) {
        UpmsOrganization entity = upmsOrganizationRepository.findByCode(reqCode);
        return XkBeanUtils.copyProperties(entity, UpmsOrganizationResp::new);
    }

    @Override
    public UpmsOrganizationSaveResp create(UpmsOrganizationSaveReq resources) {
        // 創建 Entity 實體並複製屬性
        UpmsOrganization req = XkBeanUtils.copyProperties(resources, UpmsOrganization::new);
        if (StringUtils.isNotBlank(resources.getParentId())) {
            UpmsOrganization pEntity = upmsOrganizationRepository.findById(req.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException("UpmsOrganization with ID " + req.getParentId() + " not found"));
            req.setLevel(pEntity.getLevel()+1);
        } else {
            req.setLevel(1);
        }
        // 保存實體到數據庫
        UpmsOrganization savedEntity = upmsOrganizationRepository.save(req);
        // upmsUserService.create() && cmsUserService.create()
        prepareUserForOrganization(savedEntity);
        // 創建並返回 UpmsOrganizationSaveResp
        return XkBeanUtils.copyProperties(savedEntity, UpmsOrganizationSaveResp::new);
    }

    /**
     * 在建立組織的同時，協助建立對應使用者
     */
    private void prepareUserForOrganization(UpmsOrganization uoEntity) {
        if (uoEntity != null) {
            UpmsUserSaveReq req = new UpmsUserSaveReq();
            req.setUsername(uoEntity.getName());
            if (uoEntity.getCode().startsWith("D")) {
                req.setEmail(uoEntity.getCode().substring(1)+"@District");
                req.setUserRole(upmsRoleRepository.findByCode("district_sys").getId());
                upmsUserService.create(req);
            } else if (uoEntity.getCode().startsWith("C")) {
                req.setEmail(uoEntity.getCode().substring(1)+"@Club");
                req.setUserRole(upmsRoleRepository.findByCode("club_sys").getId());
                upmsUserService.create(req);
            } else {
                return;
            }
        }
    }

    @Override
    public UpmsOrganizationSaveResp update(Long id, UpmsOrganizationSaveReq resources) {
        // 查找指定 ID 的 UpmsOrganization，如果不存在則拋出異常
        UpmsOrganization entity = upmsOrganizationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity<UpmsOrganization> with ID " + id + " not found"));
        // 使用通用更新服務更新實體
        GenericUpdateService<UpmsOrganization> updateService = new GenericUpdateService<>();
        UpmsOrganization updatedEntity = updateService.updateEntity(entity, resources);
        // 保存更新後的實體
        UpmsOrganization savedEntity = upmsOrganizationRepository.save(updatedEntity);
        // cmsUserService.create()
        prepareUserInfoForOrganization(savedEntity);
        // 創建並返回 UpmsOrganizationSaveResp
        return XkBeanUtils.copyProperties(savedEntity, UpmsOrganizationSaveResp::new);
    }

    /**
     * 在開啟組織的同時，協助建立對應使用者資訊
     */
    private void prepareUserInfoForOrganization(UpmsOrganization uoEntity) {
        if (uoEntity != null) {
            UpmsUserResp upmsUser = null;
            if (uoEntity.getCode().startsWith("D")) {
                upmsUser = upmsUserService.findByEmail(uoEntity.getCode().substring(1)+"@District");
            } else if (uoEntity.getCode().startsWith("C")) {
                upmsUser = upmsUserService.findByEmail(uoEntity.getCode().substring(1)+"@Club");

                CmsClubSaveReq ccReq = new CmsClubSaveReq();
                ccReq.setFkUpmsOrganizationId(String.valueOf(uoEntity.getId()));
                ccReq.setName(uoEntity.getName());
                ccReq.setStatus("true");
                cmsClubService.create(ccReq);
            } else {
                return;
            }
            UpmsOrganization parentOrg = upmsOrganizationRepository.findById(uoEntity.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException("Entity<UpmsOrganization> with ID " + uoEntity.getParentId() + " not found"));

            CmsUserSaveReq req = new CmsUserSaveReq();
            req.setFkUpmsUserId(upmsUser.getId());
            req.setDistrict_id(String.valueOf(parentOrg.getId()));
            req.setRotaract_id(String.valueOf(uoEntity.getId()));
            cmsUserService.create(req);
        }
    }

    @Override
    public void deleteByPrimaryKeys(String ids) {
        // 將 IDs 字符串分割並轉換為 Long 列表
        List<Long> idList = Arrays.stream(ids.split("-"))
                .map(XkTypeUtils::parseLongOrNull) // 使用輔助方法進行安全轉換
                .filter(Objects::nonNull)    // 過濾掉無效或空的 ID
                .collect(Collectors.toList());

        if (!idList.isEmpty()) {
            upmsOrganizationRepository.deleteAllByIdInBatch(idList);
        }
    }

    @Override
    public List<UpmsOrganizationResp> findOrganizationsByUserId(long userId) {
        return null;
    }

    @Override
    public List<UpmsOrganizationTreeResp> buildTree(List<UpmsOrganizationResp> organizations) {
        if (organizations == null) {
            return null;
        }

        // 使用 Map 將組織按 parentId 分組
        Map<String, List<UpmsOrganizationTreeResp>> organizationMap = new HashMap<>();
        List<UpmsOrganizationTreeResp> rootNodes = new ArrayList<>();

        for (UpmsOrganizationResp organization : organizations) {
            UpmsOrganizationTreeResp node = new UpmsOrganizationTreeResp();
            BeanUtils.copyProperties(organization, node);

            // 找到根節點
            if (node.getParentId() == null || node.getParentId().isEmpty()) {
                rootNodes.add(node);
            } else {
                // 將組織節點加入 map，按 parentId 分組
                organizationMap.computeIfAbsent(node.getParentId(), k -> new ArrayList<>()).add(node);
            }
        }

        // 建立樹結構
        for (UpmsOrganizationTreeResp rootNode : rootNodes) {
            buildChildren(rootNode, organizationMap);
        }

        return rootNodes;
    }

    @Override
    public List findChildren(String code) {
        UpmsOrganization parentEntity = upmsOrganizationRepository.findByCode(code);

        // 檢查 parentEntity 是否存在
        if (parentEntity == null) {
            throw new IllegalArgumentException("找不到指定 code 的組織：" + code);
        }

        UpmsOrganizationReq req = new UpmsOrganizationReq();
        req.setParentId(String.valueOf(parentEntity.getId()));
        req.setStatus("true");

        // 調用 list 方法查找子組織
        return this.list(req);
    }

    @Override
    public List<UpmsOrganization> getOrganizationsExcludingLevel3() {
        // 查詢不包含 level = 3 的所有組織
        return upmsOrganizationRepository.findByLevelNotIn(Arrays.asList(3));
    }


    /**
     * 遞歸構建子節點，從 Map 中獲取子節點
     */
    private void buildChildren(UpmsOrganizationTreeResp parent, Map<String, List<UpmsOrganizationTreeResp>> organizationMap) {
        List<UpmsOrganizationTreeResp> children = organizationMap.get(parent.getId());
        if (children != null) {
            parent.setChildren(children);
            for (UpmsOrganizationTreeResp child : children) {
                buildChildren(child, organizationMap);  // 遞歸構建子節點
            }
        }
    }

}
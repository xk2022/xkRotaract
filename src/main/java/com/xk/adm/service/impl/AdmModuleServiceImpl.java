package com.xk.adm.service.impl;

import com.xk.adm.dao.repository.AdmModuleRepository;
import com.xk.adm.model.bo.AdmModuleReq;
import com.xk.adm.model.bo.AdmModuleSaveReq;
import com.xk.adm.model.po.AdmModule;
import com.xk.adm.model.vo.AdmModuleResp;
import com.xk.adm.model.vo.AdmModuleSaveResp;
import com.xk.adm.service.AdmModuleService;
import com.xk.common.util.GenericUpdateService;
import com.xk.common.util.XkBeanUtils;
import com.xk.common.util.XkTypeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementation of AdmModuleService for managing AdmModule entities.
 * Handles the business logic for creating, updating, listing, and deleting modules.
 *
 * @author yuan Created on 2024/12/05.
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class AdmModuleServiceImpl implements AdmModuleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdmModuleServiceImpl.class);

    @Autowired
    private AdmModuleRepository admModuleRepository;

    /**
     * Retrieves a list of all modules in the system.
     */
    @Override
    public List<AdmModuleResp> list(AdmModuleReq resources) {
        // 設置 Example 條件，如果 resources 為 null 則返回 null
        Example<AdmModule> example = resources == null ? null :
                Example.of(XkBeanUtils.copyProperties(resources, AdmModule::new), ExampleMatcher.matching()
                        .withIgnoreNullValues()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                        .withIgnoreCase());
        Sort sort = Sort.by(Sort.Order.asc("id"), Sort.Order.asc("code")); // 提取排序規則，避免重複定義
        // 記錄查詢條件和排序規則
        LOGGER.info("查詢 AdmModule，條件: {}", resources);
        LOGGER.info("排序規則: {}", sort);

        // 查詢數據並轉換為 UpmsOrganizationResp 列表
        List<AdmModule> entities = (example == null)
                ? admModuleRepository.findAll(sort)
                : admModuleRepository.findAll(example, sort);
        LOGGER.info("查詢結果數量: {}", entities.size()); // 記錄查詢結果數量（僅在 DEBUG 級別下）
        return XkBeanUtils.copyListProperties(entities, AdmModuleResp::new);
    }

    /**
     * Finds a specific module by its ID.
     */
    @Override
    public AdmModuleResp findById(Long id) {
        AdmModule entity = admModuleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity<AdmModule> with ID: " + id + " not found"));
        return XkBeanUtils.copyProperties(entity, AdmModuleResp::new);
    }

    /**
     * Creates a new module in the system.
     */
    @Override
    public AdmModuleSaveResp create(AdmModuleSaveReq resources) {
        validateRequest(resources);

        AdmModule request = XkBeanUtils.copyProperties(resources, AdmModule::new);
        AdmModule savedEntity = admModuleRepository.save(request);

        return XkBeanUtils.copyProperties(savedEntity, AdmModuleSaveResp::new);
    }

    /**
     * Updates an existing module in the system.
     */
    @Override
    @Transactional
    public AdmModuleSaveResp update(Long id, AdmModuleSaveReq resources) {
        validateRequest(resources);
        // 查找指定 ID 的 UpmsOrganization，如果不存在則拋出異常
        AdmModule existingEntity = admModuleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity<AdmModule> with ID: " + id + " not found"));
        // 使用通用更新服務更新實體
        GenericUpdateService<AdmModule> updateService = new GenericUpdateService<>();
        AdmModule updatedEntity = updateService.updateEntity(existingEntity, resources);
        // 保存更新後的實體
        AdmModule savedEntity = admModuleRepository.save(updatedEntity);
        // 創建並返回 UpmsOrganizationSaveResp
        return XkBeanUtils.copyProperties(savedEntity, AdmModuleSaveResp::new);
    }

    /**
     * Deletes one or more modules by their IDs.
     */
    @Override
    public void deleteByPrimaryKeys(String ids) {
        if (StringUtils.isBlank(ids)) {
            throw new IllegalArgumentException("ID 列表不能為空");
        }
        // 將 IDs 字符串分割並轉換為 Long 列表
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(XkTypeUtils::parseLongOrNull) // 使用輔導方法進行安全轉換
                .filter(Objects::nonNull)    // 過濾掉無效或空的 ID
                .collect(Collectors.toList());

        for (Long id : idList) {
            if (!admModuleRepository.existsById(id)) {
                throw new EntityNotFoundException("Entity<AdmModule> with ID: " + id + " not found");
            }
        }
        // 批量更新模块状态为 false（逻辑删除）
        admModuleRepository.updateStatusToInactive(idList);
    }

    /**
     * Validates the request object for creating or updating a module.
     *
     * @param req the request object to validate
     * @throws IllegalArgumentException if the request is invalid
     */
    private void validateRequest(AdmModuleSaveReq req) {
        if (StringUtils.isBlank(req.getName())) {
            throw new IllegalArgumentException("模块名称不能为空");
        }
        if (StringUtils.isBlank(req.getCode())) {
            throw new IllegalArgumentException("模块标识不能为空");
        }
    }
}

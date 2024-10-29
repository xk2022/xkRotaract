package com.xk.upms.service.impl;

import com.xk.common.util.GenericUpdateService;
import com.xk.common.util.XkBeanUtils;
import com.xk.common.util.XkTypeUtils;
import com.xk.upms.dao.repository.UpmsOrganizationRepository;
import com.xk.upms.dao.repository.UpmsOrganizationUserRepository;
import com.xk.upms.model.bo.UpmsOrganizationReq;
import com.xk.upms.model.bo.UpmsOrganizationSaveReq;
import com.xk.upms.model.po.UpmsOrganization;
import com.xk.upms.model.vo.UpmsOrganizationResp;
import com.xk.upms.model.vo.UpmsOrganizationSaveResp;
import com.xk.upms.service.UpmsOrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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

    @Override
    public List<UpmsOrganizationResp> list(UpmsOrganizationReq resources) {
        List<UpmsOrganization> organizations = upmsOrganizationRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        return organizations.stream()
                .map(org -> {
                    UpmsOrganizationResp resp = new UpmsOrganizationResp();
                    BeanUtils.copyProperties(org, resp);
                    return resp;
                })
                .collect(Collectors.toList());
    }
    // convertToResp

    @Override
    public UpmsOrganizationResp findById(Long id) {
        UpmsOrganizationResp resp = new UpmsOrganizationResp();

        UpmsOrganization entity = upmsOrganizationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("UpmsOrganization with ID " + id + " not found"));
        Long cntUser = upmsOrganizationUserRepository.countByOrganizationId(entity.getId());

        BeanUtils.copyProperties(entity, resp);
        resp.setCountUser(cntUser);

        return resp;
    }

    @Override
    public UpmsOrganizationSaveResp create(UpmsOrganizationSaveReq resources) {
        UpmsOrganization req = XkBeanUtils.copyProperties(resources, UpmsOrganization::new);
        UpmsOrganization savedEntity = upmsOrganizationRepository.save(req);
        return XkBeanUtils.copyProperties(savedEntity, UpmsOrganizationSaveResp::new);
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
        // 創建並返回 UpmsOrganizationSaveResp
        return XkBeanUtils.copyProperties(savedEntity, UpmsOrganizationSaveResp::new);
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

}
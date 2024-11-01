package com.xk.cms.service.impl;

import com.xk.cms.dao.repository.CmsDistrictRepository;
import com.xk.cms.model.bo.CmsDistrictReq;
import com.xk.cms.model.bo.CmsDistrictSaveReq;
import com.xk.cms.model.po.CmsDistrict;
import com.xk.cms.model.vo.CmsDistrictResp;
import com.xk.cms.model.vo.CmsDistrictSaveResp;
import com.xk.cms.service.CmsDistrictService;
import com.xk.common.util.GenericUpdateService;
import com.xk.common.util.XkBeanUtils;
import com.xk.common.util.XkTypeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementation of the CmsDistrict Service.
 * Provides business logic and service methods for managing CmsDistrict entities.
 *
 * @author yuan Created on 2024/10/24.
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class CmsDistrictServiceImpl implements CmsDistrictService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsDistrictServiceImpl.class);

    @Autowired
    private CmsDistrictRepository cmsDistrictRepository;

    @Override
    public List<CmsDistrictResp> list() {
        // 獲取並排序所有 CmsDistrict 實體
        List<CmsDistrict> entities = cmsDistrictRepository.findAll(Sort.by(Sort.Direction.ASC, "code"));

        // 使用工具類複製屬性並生成 CmsDistrictResp 的列表
        return XkBeanUtils.copyListProperties(entities, CmsDistrictResp::new);
    }

    @Override
    public List<CmsDistrictResp> listBy(CmsDistrictReq resources) {
        // 創建 CmsDistrict 實體並複製屬性
        CmsDistrict req = new CmsDistrict();
        BeanUtils.copyProperties(resources, req);

        // 建立 Example 查詢條件
        Example<CmsDistrict> example = Example.of(req);

        // 查詢並排序
        List<CmsDistrict> entities = cmsDistrictRepository.findAll(example, Sort.by(Sort.Direction.ASC, "code"));

        // 使用工具類複製屬性並生成 CmsDistrictResp 的列表
        return XkBeanUtils.copyListProperties(entities, CmsDistrictResp::new);
    }

    @Override
    public CmsDistrictSaveResp create(CmsDistrictSaveReq resources) {
        // 創建 CmsDistrict 實體並複製屬性
        CmsDistrict req = XkBeanUtils.copyProperties(resources, CmsDistrict::new);
        // 保存實體到數據庫
        CmsDistrict savedEntity = cmsDistrictRepository.save(req);
        // 創建並返回 CmsDistrictSaveResp
        return XkBeanUtils.copyProperties(savedEntity, CmsDistrictSaveResp::new);
    }

    @Override
    public CmsDistrictSaveResp update(Long id, CmsDistrictSaveReq resources) {
        // 查找指定 ID 的 CmsDistrict，如果不存在則拋出異常
        CmsDistrict entity = cmsDistrictRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity<CmsDistrict> with ID " + id + " not found"));
        // 使用通用更新服務更新實體
        GenericUpdateService<CmsDistrict> updateService = new GenericUpdateService<>();
        CmsDistrict updatedEntity = updateService.updateEntity(entity, resources);
        // 保存更新後的實體
        CmsDistrict savedEntity = cmsDistrictRepository.save(updatedEntity);
        // 創建並返回 CmsDistrictSaveResp
        return XkBeanUtils.copyProperties(savedEntity, CmsDistrictSaveResp::new);
    }

    @Override
    public void deleteByPrimaryKeys(String ids) {
        // 將 IDs 字符串分割並轉換為 Long 列表
        List<Long> idList = Arrays.stream(ids.split("-"))
                .map(XkTypeUtils::parseLongOrNull) // 使用輔助方法進行安全轉換
                .filter(Objects::nonNull)    // 過濾掉無效或空的 ID
                .collect(Collectors.toList());

        if (!idList.isEmpty()) {
            cmsDistrictRepository.deleteAllByIdInBatch(idList);
        }
    }

}

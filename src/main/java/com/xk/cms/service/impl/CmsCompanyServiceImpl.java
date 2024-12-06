package com.xk.cms.service.impl;

import com.xk.cms.dao.mapper.CmsCompanyMapper;
import com.xk.cms.dao.repository.CmsCompanyPayRepository;
import com.xk.cms.dao.repository.CmsCompanyRepository;
import com.xk.cms.dao.repository.CmsUserCompanyRepository;
import com.xk.cms.dao.repository.CmsUserRepository;
import com.xk.cms.model.bo.CmsCompanyReq;
import com.xk.cms.model.bo.CmsCompanySaveReq;
import com.xk.cms.model.dto.CmsCompanyExample;
import com.xk.cms.model.po.CmsCompany;
import com.xk.cms.model.po.CmsUser;
import com.xk.cms.model.po.CmsUserCompany;
import com.xk.cms.model.vo.CmsCompanyResp;
import com.xk.cms.model.vo.CmsCompanySaveResp;
import com.xk.cms.model.vo.CmsCompanyWithUserResp;
import com.xk.cms.service.CmsCompanyService;
import com.xk.common.json.Industry;
import com.xk.common.util.GoogleApiGeocode;
import com.xk.common.util.XkBeanUtils;
import com.xk.upms.dao.repository.UpmsDictionaryCategoryRepository;
import com.xk.upms.dao.repository.UpmsDictionaryDataRepository;
import com.xk.upms.model.vo.UpmsOrganizationResp;
import com.xk.upms.service.UpmsOrganizationService;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of the CmsCompany Service.
 *
 * @author yuan Created on 2024/05/02.
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class CmsCompanyServiceImpl implements CmsCompanyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsCompanyServiceImpl.class);

    @Autowired
    private CmsCompanyRepository cmsCompanyRepository;
    @Autowired
    private CmsUserCompanyRepository cmsUserCompanyRepository;
    @Autowired
    private GoogleApiGeocode googleApiGeocode;
    @Autowired
    private CmsUserRepository cmsUserRepository;
    @Autowired
    private UpmsDictionaryCategoryRepository upmsDictionaryCategoryRepository;
    @Autowired
    private UpmsDictionaryDataRepository upmsDictionaryDataRepository;
    @Autowired
    private CmsCompanyPayRepository cmsCompanyPayRepository;
    @Autowired
    private CmsCompanyMapper cmsCompanyMapper;
    @Autowired
    private UpmsOrganizationService upmsOrganizationService;

    @Override
    public List list(CmsCompanyReq resources) {
        // 設置 Example 條件，如果 resources 為 null 則返回 null
        Example<CmsCompany> example = resources == null ? null :
                Example.of(XkBeanUtils.copyProperties(resources, CmsCompany::new), ExampleMatcher.matching()
                        .withIgnoreNullValues()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                        .withIgnoreCase());
        // 提取排序規則，避免重複定義
        Sort sort = Sort.by(Sort.Order.asc("id"));
        // 記錄查詢條件和排序規則
        LOGGER.info("查詢 CmsCompany，條件: {}", resources);
        LOGGER.info("排序規則: {}", sort);

        // 查詢數據並轉換為 UpmsOrganizationResp 列表
        List<CmsCompany> companies = (example == null)
                ? cmsCompanyRepository.findAll(sort)
                : cmsCompanyRepository.findAll(example, sort);
        // 記錄查詢結果數量（僅在 DEBUG 級別下）
        LOGGER.info("查詢結果數量: {}", companies.size());

        return XkBeanUtils.copyListProperties(companies, CmsCompanyResp::new);
    }

    @Override
    public List<CmsCompanyResp> listByClub(CmsCompanyReq resources) {
        // 构建 Example 查询条件
        Example<CmsUser> example = resources == null ? null :
                Example.of(XkBeanUtils.copyProperties(resources, CmsUser::new), ExampleMatcher.matching()
                        .withIgnoreNullValues()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                        .withIgnoreCase());
        // 定义排序规则
        Sort sort = Sort.by(Sort.Order.asc("id"));
        // 记录查询条件和排序规则
        LOGGER.info("查询 CmsUser 条件: {}", resources);
        LOGGER.info("排序规则: {}", sort);
        // 查询 CmsUser 数据
        List<CmsUser> cmsUsers = (example == null)
                ? cmsUserRepository.findAll(sort)
                : cmsUserRepository.findAll(example, sort);
        LOGGER.info("查询 CmsUser 结果数量: {}", cmsUsers.size());

        // 提取所有 userId
        Set<Long> userIds = cmsUsers.stream()
                .map(CmsUser::getId)
                .collect(Collectors.toSet());
        if (userIds.isEmpty()) {
            LOGGER.info("未找到任何匹配的 CmsUser");
            return Collections.emptyList();
        }

        // 根据 userId 查询所有 companyId
        List<Long> companyIds = cmsUserCompanyRepository.findAllByFkCmsUserIdIn(userIds).stream()
                .map(CmsUserCompany::getFkCmsCompanyId)
                .distinct()
                .collect(Collectors.toList());
        LOGGER.info("根据 userId 查询到的 companyId 数量: {}", companyIds.size());

        if (companyIds.isEmpty()) {
            LOGGER.info("未找到任何匹配的公司 ID");
            return Collections.emptyList();
        }
        // 根据 companyId 查询 CmsCompany 列表
        List<CmsCompany> companies = cmsCompanyRepository.findAllById(companyIds);
        LOGGER.info("根据 companyId 查询到的 CmsCompany 数量: {}", companies.size());

        // 转换为 CmsCompanyResp 列表
        return XkBeanUtils.copyListProperties(companies, CmsCompanyResp::new);
    }


    @Override
    public List listByUser(long cms_user_id) {
        List<CmsCompanySaveResp> result = new ArrayList<>();

        List<CmsUserCompany> userCompanies = cmsUserCompanyRepository.findByFkCmsUserId(cms_user_id);
        Set<Long> companyIds = userCompanies.stream().map(CmsUserCompany::getFkCmsCompanyId).collect(Collectors.toSet());

        Iterable<Long> ids = companyIds;
        List<CmsCompany> entities = cmsCompanyRepository.findAllById(ids);

        for (CmsCompany entity : entities) {
            CmsCompanySaveResp resp = new CmsCompanySaveResp();
            BeanUtils.copyProperties(entity, resp);

            if (resp.getIndustries() != null) {
                resp = transIndustriesChinese(resp);
            }
            result.add(resp);
        }
        return result;
    }

    @Override
    public List listByUserWithPay(long cms_user_id) {
        List<CmsCompanyExample> result = new ArrayList<>();

        List<CmsCompanyExample> cceList = cmsCompanyMapper.findByFkCmsUserId(cms_user_id);

        for (CmsCompanyExample example : cceList) {
            CmsCompanySaveResp resp = new CmsCompanySaveResp();
            resp.setIndustries(example.getIndustries());

            if (resp.getIndustries() != null) {
                resp = transIndustriesChinese(resp);
                example.setIndustryIds(resp.getIndustryIds());
                example.setIndustriesChinese(resp.getIndustriesChinese());
            }
            result.add(example);
        }
        return result;
    }

    private CmsCompanySaveResp transIndustriesChinese(CmsCompanySaveResp resp) {
        List<Long> industryIds = new ArrayList<>();

        String[] industryArray = resp.getIndustries().split(",");

        StringBuilder industriesBuilder = new StringBuilder();
        for (String id : industryArray) {
            Long industryId = Long.valueOf(id);
            industryIds.add(industryId);

            String industryName = Industry.getNameByKey(Integer.valueOf(id));
            industriesBuilder.append(industryName).append(", ");
        }
        // 移除末尾的逗号和空格
        if (industriesBuilder.length() > 0) {
            industriesBuilder.setLength(industriesBuilder.length() - 2);
        }

        String industries = industriesBuilder.toString();
        resp.setIndustryIds(industryIds);
        resp.setIndustriesChinese(industries);
        return resp;
    }

    @Override
    public CmsCompanySaveResp create(CmsCompanySaveReq resources) {
        CmsCompanySaveResp result = new CmsCompanySaveResp();

        CmsCompany req = new CmsCompany();
        BeanUtils.copyProperties(resources, req);
        if (resources.getIndustryIds() != null) {
            req.setIndustries(remixIndustryIds(resources.getIndustryIds()));
        }
        if (resources.getAddress() != null) {
            req.setLatlng(googleApiGeocode.code(resources.getAddress()));
        }
        CmsCompany entity = cmsCompanyRepository.save(req);

        CmsUserCompany fkEntity = new CmsUserCompany();
        fkEntity.setFkCmsUserId(Long.parseLong(resources.getCmsUserId()));
        fkEntity.setFkCmsCompanyId(entity.getId());
        cmsUserCompanyRepository.save(fkEntity);

        BeanUtils.copyProperties(entity, result);
        return result;
    }

    @Override
    public CmsCompanySaveResp update(Long id, CmsCompanySaveReq resources) {
        CmsCompanySaveResp result = new CmsCompanySaveResp();

        CmsCompany req = new CmsCompany();
        BeanUtils.copyProperties(resources, req);
        if (resources.getIndustryIds() != null) {
            req.setIndustries(remixIndustryIds(resources.getIndustryIds()));
        }
        if (resources.getAddress() != null) {
            req.setLatlng(googleApiGeocode.code(resources.getAddress()));
        }
        CmsCompany entity = cmsCompanyRepository.save(req);
        BeanUtils.copyProperties(entity, result);
//
//        if (entity != null) {
//            CmsCompanyPay entityPay = cmsCompanyPayRepository.findByFkCmsCompanyId(entity.getId());
//            if (entityPay == null) {
//                entityPay = new CmsCompanyPay();
//
//                entityPay.setFkCmsCompanyId(entity.getId());
//            }
//            entityPay.setLocked(resources.isLocked());
//            entityPay.setPaydate(new Date());
//            entityPay = cmsCompanyPayRepository.save(entityPay);
//            BeanUtils.copyProperties(entityPay, result);
//        }
        return result;
    }

    @Override
    public void deleteByPrimaryKeys(String ids) {
        String[] idArray = ids.split("-");
        for (String idStr : idArray) {
            Long id = Long.valueOf(idStr);
            cmsCompanyRepository.deleteById(id);
        }
    }

    @Override
    public CmsCompanyWithUserResp findOneWithPersonalByCompanyId(String reqCompanyId) {
        CmsCompanyWithUserResp result = new CmsCompanyWithUserResp();
        /**
         * CmsCompany
         */
        CmsCompany ccEntity = cmsCompanyRepository.findById(Long.valueOf(reqCompanyId))
                .orElseThrow(() -> new EntityNotFoundException("Entity<CmsCompany> with ID " + reqCompanyId + " not found"));

        CmsCompanySaveResp companyResp = XkBeanUtils.copyProperties(ccEntity, CmsCompanySaveResp::new);
        if (companyResp.getIndustries() != null) {
            companyResp = transIndustriesChinese(companyResp);
        }
        result.setCompanyId(companyResp.getId());
        BeanUtils.copyProperties(companyResp, result);
        /**
         * CmsUser
         */
        List<CmsUserCompany> cucEntities = cmsUserCompanyRepository.findByFkCmsCompanyId(Long.valueOf(reqCompanyId));
        if (cucEntities.size() <= 0) {
            return null;
        }
        CmsUser cuEntity = cmsUserRepository.findById(cucEntities.get(0).getFkCmsUserId())
                .orElseThrow(() -> new EntityNotFoundException("Entity<CmsUser> with ID " + cucEntities.get(0).getFkCmsUserId() + " not found"));
        result.setUserId(cuEntity.getId());
        BeanUtils.copyProperties(cuEntity, result);

        UpmsOrganizationResp districtUOR = upmsOrganizationService.findById(Long.valueOf(result.getDistrict_id()));
        result.setDistrict_name(districtUOR.getName());

        UpmsOrganizationResp clubUOR = upmsOrganizationService.findById(Long.valueOf(result.getRotaract_id()));
        result.setRotaract_name(clubUOR.getName());
        /**
         * CmsCompanyPay
         */
        result.setLocked(false);
//        CmsCompanyPay ccpEntity = cmsCompanyPayRepository.findByFkCmsCompanyId(cucEntity.get(0).getId());
//        if (ccpEntity == null) {
//            result.setLocked(true);
//        } else {
//            BeanUtils.copyProperties(ccpEntity, result);
//        }

        return result;
    }

    private String remixIndustryIds(String ids) {
        return ids.toString().replace("[", "").replace("]", "").replace(" ", "");
    }

}

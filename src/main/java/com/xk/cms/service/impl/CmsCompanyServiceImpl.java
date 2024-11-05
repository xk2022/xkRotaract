package com.xk.cms.service.impl;

import com.xk.cms.dao.mapper.CmsCompanyMapper;
import com.xk.cms.dao.repository.CmsCompanyPayRepository;
import com.xk.cms.dao.repository.CmsCompanyRepository;
import com.xk.cms.dao.repository.CmsUserCompanyRepository;
import com.xk.cms.dao.repository.CmsUserRepository;
import com.xk.cms.model.bo.CmsCompanySaveReq;
import com.xk.cms.model.dto.CmsCompanyExample;
import com.xk.cms.model.po.CmsCompany;
import com.xk.cms.model.po.CmsUser;
import com.xk.cms.model.po.CmsUserCompany;
import com.xk.cms.model.vo.CmsCompanySaveResp;
import com.xk.cms.model.vo.CmsCompanyWithUserResp;
import com.xk.cms.service.CmsCompanyService;
import com.xk.common.json.Industry;
import com.xk.common.util.GoogleApiGeocode;
import com.xk.upms.dao.repository.UpmsDictionaryCategoryRepository;
import com.xk.upms.dao.repository.UpmsDictionaryDataRepository;
import com.xk.upms.model.po.UpmsDictionaryCategory;
import com.xk.upms.model.po.UpmsDictionaryData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * CmsUserService 實現
 * Created by yuan on 2024/05/02
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

    @Override
    public List list() {
        List<CmsCompanySaveResp> result = new ArrayList<>();
        List<CmsCompany> entities = cmsCompanyRepository.findAll();

        for (CmsCompany entity : entities) {
            CmsCompanySaveResp resp = new CmsCompanySaveResp();
            BeanUtils.copyProperties(entity, resp);

            if (resp.getIndustries() != null) {
                resp = transIndustriesChinese(resp);
            }
            resp.setLocked(false);
            result.add(resp);
        }
        return result;
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
    public CmsCompanyWithUserResp findOneWithPersonalByCompanyId(long id) {
        CmsCompanyWithUserResp result = new CmsCompanyWithUserResp();
        /**
         * CmsCompany
         */
        Optional<CmsCompany> OCCEntity = cmsCompanyRepository.findById(id);
        if (!OCCEntity.isPresent()) {
            throw new EntityNotFoundException("CmsCompany not found with id: " + id);
        }
        CmsCompany ccEntity = OCCEntity.get();

        CmsCompanySaveResp resp = new CmsCompanySaveResp();
        BeanUtils.copyProperties(ccEntity, resp);
        if (resp.getIndustries() != null) {
            resp = transIndustriesChinese(resp);
        }
        result.setCompanyId(resp.getId());
        BeanUtils.copyProperties(resp, result);
        /**
         * CmsUser
         */
        List<CmsUserCompany> cucEntity = cmsUserCompanyRepository.findByFkCmsCompanyId(id);
        if (cucEntity.size() <= 0) {
            return null;
        }
        Optional<CmsUser> OCUEntity = cmsUserRepository.findById(cucEntity.get(0).getFkCmsUserId());
        if (!OCUEntity.isPresent()) {
            throw new EntityNotFoundException("CmsUser not found with id: " + id);
        }
        CmsUser cuEntity = OCUEntity.get();
        result.setUserId(cuEntity.getId());
        BeanUtils.copyProperties(cuEntity, result);

        UpmsDictionaryCategory dropdown_DISTRICT_udcEntity =  upmsDictionaryCategoryRepository.findOneByCode("dropdown_DISTRICT");
        UpmsDictionaryData dropdown_DISTRICT_uddEntity = upmsDictionaryDataRepository.findByParentIdAndCode(dropdown_DISTRICT_udcEntity.getId(), result.getDistrict_id());
        result.setDistrict_name(dropdown_DISTRICT_uddEntity.getDescription());

        UpmsDictionaryCategory udcEntity =  upmsDictionaryCategoryRepository.findOneByCode("dropdown_DISTRICT" + result.getDistrict_id());
        UpmsDictionaryData uddEntity = upmsDictionaryDataRepository.findByParentIdAndCode(udcEntity.getId(), String.valueOf(Integer.valueOf(result.getRotaract_id())));
        result.setRotaract_name(uddEntity.getDescription());
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

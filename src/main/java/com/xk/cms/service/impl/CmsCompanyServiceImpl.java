package com.xk.cms.service.impl;

import com.xk.cms.dao.repository.CmsCompanyRepository;
import com.xk.cms.dao.repository.CmsUserCompanyRepository;
import com.xk.cms.model.bo.CmsCompanySaveReq;
import com.xk.cms.model.po.CmsCompany;
import com.xk.cms.model.po.CmsUserCompany;
import com.xk.cms.model.vo.CmsCompanySaveResp;
import com.xk.cms.service.CmsCompanyService;
import com.xk.common.json.Industry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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

    @Override
    public List list() {
        return cmsCompanyRepository.findAll();
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
                List<Long> industryIds = new ArrayList<>();

                String industries = "";
                String[] industry = resp.getIndustries().split(",");
                for(String id : industry) {
                    industryIds.add(Long.valueOf(id));
                    industries += Industry.getNameByKey(Integer.valueOf(id))+", ";
                }
                resp.setIndustryIds(industryIds);
                resp.setIndustriesChinese(industries);
            }
            result.add(resp);
        }
        return result;
    }

    @Override
    public CmsCompanySaveResp create(CmsCompanySaveReq resources) {
        CmsCompanySaveResp result = new CmsCompanySaveResp();

        CmsCompany req = new CmsCompany();
        BeanUtils.copyProperties(resources, req);
        if (resources.getIndustryIds() != null) {
            req.setIndustries(remixIndustryIds(resources.getIndustryIds()));
        }
        CmsCompany entity = cmsCompanyRepository.save(req);

        CmsUserCompany fkEntity = new CmsUserCompany();
        fkEntity.setFkCmsUserId(resources.getCmsUserId());
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
        CmsCompany entity = cmsCompanyRepository.save(req);

        BeanUtils.copyProperties(entity, result);
        return result;
    }

    private String remixIndustryIds(List<Long> ids) {
        return ids.toString().replace("[", "").replace("]", "").replace(" ", "");
    }

}

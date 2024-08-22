package com.xk.cms.service.impl;

import com.xk.cms.dao.repository.CmsCompanyPayRepository;
import com.xk.cms.dao.repository.CmsCompanyRepository;
import com.xk.cms.model.bo.CmsCompanyPaySaveReq;
import com.xk.cms.model.dto.CmsCompanyExample;
import com.xk.cms.model.po.CmsCompany;
import com.xk.cms.model.po.CmsCompanyPay;
import com.xk.cms.model.vo.CmsCompanySaveResp;
import com.xk.cms.service.CmsCompanyPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * CmsCompanyPayService 實現
 * Created by yuan on 2024/08/22
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class CmsCompanyPayServiceImpl implements CmsCompanyPayService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsCompanyPayServiceImpl.class);

    @Autowired
    private CmsCompanyRepository cmsCompanyRepository;
    @Autowired
    private CmsCompanyPayRepository cmsCompanyPayRepository;

    @Override
    public List list() {
        List<CmsCompanyExample> result = new ArrayList<>();

        List<CmsCompany> companies = cmsCompanyRepository.findAll(Sort.by(Sort.Order.desc("id"), Sort.Order.desc("cmsCompanyPayId")));
        for (CmsCompany company : companies) {
            CmsCompanyExample example = new CmsCompanyExample();

            example.setCmsCompanyId(company.getId());
            example.setCompanyName(company.getName());
            if (company.getCompanyPays() != null) {
                CmsCompanyPay lastCompanyPay = company.getCompanyPays().get(0);

                example.setCmsCompanyPayId(lastCompanyPay.getId());
                example.setPaydate(lastCompanyPay.getPayDate());
                example.setLocked(lastCompanyPay.getLocked());
            }
            result.add(example);
        }
        return result;
    }

    @Override
    public CmsCompanySaveResp create(CmsCompanyPaySaveReq resources) {
        CmsCompanySaveResp result = new CmsCompanySaveResp();

        Optional<CmsCompany> ccEntity = cmsCompanyRepository.findById(resources.getCmsCompanyId());
        CmsCompany ccReq = new CmsCompany();
        BeanUtils.copyProperties(ccEntity, ccReq);

        ccReq.setCompanyPays(null);
        CmsCompanyPay ccpReq = new CmsCompanyPay();
        BeanUtils.copyProperties(resources, ccpReq);
        ccReq.addCompanyPay(ccpReq);

        CmsCompany entity = cmsCompanyRepository.save(ccReq);
        BeanUtils.copyProperties(entity, result);
        return result;
    }

    @Override
    public CmsCompanySaveResp update(Long id, CmsCompanyPaySaveReq resources) {
        CmsCompanySaveResp result = new CmsCompanySaveResp();

        Optional<CmsCompany> ccEntity = cmsCompanyRepository.findById(resources.getCmsCompanyId());
        CmsCompany ccReq = new CmsCompany();
        BeanUtils.copyProperties(ccEntity, ccReq);

        ccReq.setCompanyPays(null);
        CmsCompanyPay ccpReq = new CmsCompanyPay();
        BeanUtils.copyProperties(resources, ccpReq);
        ccpReq.setId(id);
        ccReq.addCompanyPay(ccpReq);

        CmsCompany entity = cmsCompanyRepository.save(ccReq);
        BeanUtils.copyProperties(entity, result);
        return result;
    }

}

package com.xk.cms.service.impl;

import com.xk.cms.dao.mapper.CmsCompanyMapper;
import com.xk.cms.dao.repository.CmsCompanyPayRepository;
import com.xk.cms.dao.repository.CmsCompanyRepository;
import com.xk.cms.model.bo.CmsCompanyPaySaveReq;
import com.xk.cms.model.po.CmsCompanyPay;
import com.xk.cms.model.vo.CmsCompanySaveResp;
import com.xk.cms.service.CmsCompanyPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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
    @Autowired
    private CmsCompanyMapper cmsCompanyMapper;

    @Override
    public List list() {
        return cmsCompanyMapper.selectAll();
    }

    @Override
    public CmsCompanySaveResp create(CmsCompanyPaySaveReq resources) {
        CmsCompanySaveResp result = new CmsCompanySaveResp();

        CmsCompanyPay req = new CmsCompanyPay();
        req.setFkCmsCompanyId(resources.getFkCmsCompanyId());
        req.setPayDate(new Date());
        req.setLocked(false);

        CmsCompanyPay entity = cmsCompanyPayRepository.save(req);
        BeanUtils.copyProperties(entity, result);
        return result;
    }

}

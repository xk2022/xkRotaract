package com.xk.upms.service.impl;

import com.xk.upms.dao.repository.UpmsOrganizationRepository;
import com.xk.upms.model.bo.UpmsOrganizationReq;
import com.xk.upms.model.bo.UpmsOrganizationSaveReq;
import com.xk.upms.model.po.UpmsOrganization;
import com.xk.upms.model.vo.UpmsOrganizationSaveResp;
import com.xk.upms.service.UpmsOrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * UpmsOrganizationService 實現
 * Created by yuan on 2022/06/24
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UpmsOrganizationServiceImpl implements UpmsOrganizationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpmsOrganizationServiceImpl.class);

    @Autowired
    private UpmsOrganizationRepository upmsOrganizationRepository;

    @Override
    public List list(UpmsOrganizationReq resources) {
        return upmsOrganizationRepository.findAll();
    }

    @Override
    public UpmsOrganizationSaveResp create(UpmsOrganizationSaveReq resources) {
        UpmsOrganizationSaveResp result = new UpmsOrganizationSaveResp();

        UpmsOrganization req = new UpmsOrganization();
        BeanUtils.copyProperties(resources, req);
        UpmsOrganization entity = upmsOrganizationRepository.save(req);

        BeanUtils.copyProperties(entity, result);
        return result;
    }

    @Override
    public UpmsOrganizationSaveResp update(Long id, UpmsOrganizationSaveReq resources) {
        UpmsOrganizationSaveResp result = new UpmsOrganizationSaveResp();

        UpmsOrganization req = new UpmsOrganization();
        BeanUtils.copyProperties(resources, req);
        UpmsOrganization entity = upmsOrganizationRepository.save(req);

        BeanUtils.copyProperties(entity, result);
        return result;
    }

    @Override
    public void delete(Long id) {
        upmsOrganizationRepository.deleteById(id);
    }

    @Override
    public void deleteByPrimaryKeys(String ids) {
        String[] idArray = ids.split("-");
        for (String idStr : idArray) {
            Long id = Long.valueOf(idStr);
            upmsOrganizationRepository.deleteById(id);
        }
    }

    @Override
    public Object selectByUserId(long id) {
//        return upmsOrganizationRepository.findByUserId(id);
        return null;
    }

}
package com.xk.upms.service.impl;

import com.xk.upms.model.dto.UpmsUserOrganizationExample;
import com.xk.upms.model.po.UpmsUserOrganization;
import com.xk.upms.service.UpmsUserOrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * UpmsUserOrganizationService 實現
 * Created by yuan on 2022/06/24
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UpmsUserOrganizationServiceImpl implements UpmsUserOrganizationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpmsUserOrganizationServiceImpl.class);


    @Override
    public List<UpmsUserOrganization> selectByExample(UpmsUserOrganizationExample upmsUserOrganizationExample) {
        return null;
    }

    @Override
    public void organization(String[] organizationIds, long id) {

    }

    @Override
    public Object selectByUserId(long id) {
        return null;
    }

}
package com.xk.cms.service.impl;

import com.xk.cms.dao.repository.CmsUserRepository;
import com.xk.cms.model.bo.CmsUserReq;
import com.xk.cms.model.bo.CmsUserSaveReq;
import com.xk.cms.model.bo.CmsUserSelfUpdateReq;
import com.xk.cms.model.po.CmsUser;
import com.xk.cms.model.vo.CmsUserResp;
import com.xk.cms.model.vo.CmsUserSaveResp;
import com.xk.cms.service.CmsUserService;
import com.xk.common.util.XkBeanUtils;
import com.xk.upms.dao.repository.UpmsUserRepository;
import com.xk.upms.model.bo.UpmsUserSaveReq;
import com.xk.upms.model.po.UpmsUser;
import com.xk.upms.model.vo.UpmsUserIndexResp;
import com.xk.upms.service.UpmsUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implementation of the CmsUser Service.
 *
 * @author yuan Created on 2024/05/02.
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class CmsUserServiceImpl implements CmsUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsUserServiceImpl.class);

    @Autowired
    private CmsUserRepository cmsUserRepository;
    @Autowired
    private UpmsUserRepository upmsUserRepository;
    @Autowired
    private UpmsUserService upmsUserService;

    @Override
    public List list(CmsUserReq resources) {
        List<UpmsUserIndexResp> resultList = new ArrayList<>();

        CmsUser req = XkBeanUtils.copyProperties(resources, CmsUser::new);
        Example<CmsUser> example = Example.of(req);

        List<CmsUser> entities = cmsUserRepository.findAll(example);
        return XkBeanUtils.copyListProperties(entities, CmsUserResp::new);
    }

    @Override
    public CmsUserSaveResp create(CmsUserSaveReq resources) {
        CmsUserSaveResp result = new CmsUserSaveResp();

        UpmsUser upmsUser = upmsUserRepository.findByUsername(resources.getUsername());
        if (upmsUser == null) {
            return null;
        }
        updateUpmsUser(resources, upmsUser);

        CmsUser entity = new CmsUser();

        CmsUser userinfo = cmsUserRepository.findOneByFkUpmsUserId(upmsUser.getId());
        if (userinfo != null) {
            BeanUtils.copyProperties(resources, userinfo);
        } else {
            BeanUtils.copyProperties(resources, entity);
            entity.setFkUpmsUserId(upmsUser.getId());
        }
        entity.setPhoneNumber(resources.getCellPhone());
        entity = cmsUserRepository.save(entity);

        BeanUtils.copyProperties(entity, result);
        return result;
    }

    @Override
    public CmsUserSaveResp update(Long id, CmsUserSaveReq resources) {
        CmsUserSaveResp result = new CmsUserSaveResp();

        UpmsUser upmsUser = upmsUserRepository.findByUsername(resources.getUsername());
        if (upmsUser == null) {
            return null;
        }
        updateUpmsUser(resources, upmsUser);

        CmsUserSelfUpdateReq req = new CmsUserSelfUpdateReq();
        CmsUser userinfo = cmsUserRepository.findOneByFkUpmsUserId(upmsUser.getId());
        BeanUtils.copyProperties(resources, req);
        BeanUtils.copyProperties(req, userinfo);

        userinfo.setPhoneNumber(resources.getCellPhone());
        CmsUser entity = cmsUserRepository.save(userinfo);
        BeanUtils.copyProperties(entity, result);
        return result;
    }

    private void updateUpmsUser(CmsUserSaveReq resources, UpmsUser upmsUser) {
        UpmsUserSaveReq request = new UpmsUserSaveReq();

        BeanUtils.copyProperties(upmsUser, request);
        request.setCellPhone(resources.getCellPhone());
        request.setUpdateTime(new Date());
        upmsUserService.update(upmsUser.getId(), request);
    }

    @Override
    public CmsUserSaveResp getOneByEmail(String email) {
        CmsUserSaveResp result = new CmsUserSaveResp();

        UpmsUser upmsUser = upmsUserRepository.findByEmail(email);
        if (upmsUser == null) {
            return null;
        }
        BeanUtils.copyProperties(upmsUser, result);
        result.setId(null); // 此處需用 cms_user_id

        CmsUser cmsUser = cmsUserRepository.findOneByFkUpmsUserId(upmsUser.getId());
        if (cmsUser != null) {
            BeanUtils.copyProperties(cmsUser, result);
        }
        return result;
    }

}

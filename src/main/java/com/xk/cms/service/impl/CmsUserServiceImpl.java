package com.xk.cms.service.impl;

import com.xk.cms.dao.repository.CmsUserRepository;
import com.xk.cms.model.bo.CmsUserSaveReq;
import com.xk.cms.model.po.CmsUser;
import com.xk.cms.model.vo.CmsUserSaveResp;
import com.xk.cms.service.CmsUserService;
import com.xk.upms.dao.repository.UpmsUserRepository;
import com.xk.upms.model.po.UpmsUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CmsUserService 實現
 * Created by yuan on 2024/05/02
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class CmsUserServiceImpl implements CmsUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsUserServiceImpl.class);

    @Autowired
    private CmsUserRepository cmsUserRepository;
    @Autowired
    private UpmsUserRepository upmsUserRepository;

    @Override
    public CmsUserSaveResp create(CmsUserSaveReq resources) {
        CmsUserSaveResp result = new CmsUserSaveResp();

        UpmsUser upmsUser = upmsUserRepository.findByUsername(resources.getUsername());
        if (upmsUser == null) {
            return null;
        }
        CmsUser entity = new CmsUser();

        CmsUser userinfo = cmsUserRepository.findOneByFkUpmsUserId(upmsUser.getId());
        if (userinfo != null) {
            BeanUtils.copyProperties(resources, userinfo);
        } else {
            BeanUtils.copyProperties(resources, entity);
            entity.setFkUpmsUserId(upmsUser.getId());
        }
        entity.setPhone(resources.getCellPhone());
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

        CmsUser userinfo = cmsUserRepository.findOneByFkUpmsUserId(upmsUser.getId());
        BeanUtils.copyProperties(resources, userinfo);
        userinfo.setPhone(resources.getCellPhone());

        CmsUser entity = cmsUserRepository.save(userinfo);

        BeanUtils.copyProperties(entity, result);
        return result;
    }

    @Override
    public CmsUserSaveResp getOneByEmail(String email) {
        CmsUserSaveResp result = new CmsUserSaveResp();
        UpmsUser upmsUser = upmsUserRepository.findByEmail(email);
        if (upmsUser == null) {
            return null;
        }
        CmsUser cmsUser = cmsUserRepository.findOneByFkUpmsUserId(upmsUser.getId());
        if (cmsUser == null) {
            return null;
        }
        BeanUtils.copyProperties(cmsUser, result);
        return result;
    }

}

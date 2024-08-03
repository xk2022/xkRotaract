package com.xk.upms.service.impl;

import com.xk.common.util.NotFoundException;
import com.xk.common.util.XkBeanUtils;
import com.xk.upms.dao.mapper.UpmsRoleMapper;
import com.xk.upms.dao.mapper.UpmsUserMapper;
import com.xk.upms.dao.repository.UpmsUserRepository;
import com.xk.upms.model.bo.UpmsUserReq;
import com.xk.upms.model.bo.UpmsUserSaveReq;
import com.xk.upms.model.dto.UpmsUserRoleExample;
import com.xk.upms.model.po.UpmsRole;
import com.xk.upms.model.po.UpmsUser;
import com.xk.upms.model.vo.UpmsUserDetailResp;
import com.xk.upms.model.vo.UpmsUserIndexResp;
import com.xk.upms.model.vo.UpmsUserResp;
import com.xk.upms.model.vo.UpmsUserSaveResp;
import com.xk.upms.service.UpmsUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * UpmsUserService 實現
 * Created by yuan on 2022/06/10
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class UpmsUserServiceImpl implements UpmsUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpmsUserServiceImpl.class);

    @Autowired
    private UpmsUserRepository upmsUserRepository;
    @Autowired
    private UpmsUserMapper upmsUserMapper;
    @Autowired
    private UpmsRoleMapper upmsRoleMapper;

//    @Override
//    public List list(UpmsUserReq resources) {
//        return upmsUserRepository.findAll();
//    }
    
    @Override
    public List list(UpmsUserReq resources) {
        List<UpmsUserIndexResp> resultList = new ArrayList<>();

//        List<UpmsUser> resultEntities = upmsUserRepository.findAll();
        List<UpmsUserRoleExample> resultDtos = upmsUserMapper.selectAllwithRole();
        resultList = XkBeanUtils.copyListProperties(resultDtos, UpmsUserIndexResp::new);

        return resultList;
    }

    @Override
    public UpmsUserResp findByUsername(UpmsUserReq resources) {
        UpmsUserResp result = new UpmsUserResp();

        UpmsUser req = new UpmsUser();
        BeanUtils.copyProperties(resources, req);
        UpmsUser entity = upmsUserRepository.findByUsername(resources.getUsername());

        BeanUtils.copyProperties(entity, result);
        return result;
    }
    
    @Override
    public UpmsUserDetailResp selectDeatilById(long id) {
        UpmsUserDetailResp result = new UpmsUserDetailResp();

        UpmsUser entity = upmsUserRepository.findById(id).get();
        if (entity == null) {
            throw new NotFoundException("查無使用者");
        }
        BeanUtils.copyProperties(entity, result);

        List<UpmsRole> roleList = upmsRoleMapper.selectAllByUserId(id);
        result.setRoleList(roleList);

        return result;
    }

    @Override
    public UpmsUserSaveResp create(UpmsUserSaveReq resources) {
        UpmsUserSaveResp result = new UpmsUserSaveResp();

        UpmsUser req = new UpmsUser();
        BeanUtils.copyProperties(resources, req);

        UpmsUser checkUser = new UpmsUser();
        checkUser = upmsUserRepository.findByUsername(req.getUsername());
        if (null != checkUser) {
            throw new NotFoundException("帳號名已存在！");
        }

//        String salt = UUID.randomUUID().toString().replaceAll("-", "");
//        req.setSalt(salt);
//        req.setPassword(MD5Utils.code(req.getPassword() + req.getSalt()));
        req.setLocked(false);
        req.setCreateTime(new Date());
//        req.setCreateTime(new java.sql.Date(new Date().getTime()));
        UpmsUser entity = upmsUserRepository.save(req);
//        UpmsRole firstRole = new UpmsRole();

        BeanUtils.copyProperties(entity, result);
        return result;
    }

    @Override
    public UpmsUserSaveResp update(Long id, UpmsUserSaveReq resources) {
        UpmsUserSaveResp result = new UpmsUserSaveResp();

        UpmsUser req = new UpmsUser();
        BeanUtils.copyProperties(resources, req);
        UpmsUser entity = upmsUserRepository.save(req);

        BeanUtils.copyProperties(entity, result);
        return result;
    }

    @Override
    public void delete(Long id) {
        upmsUserRepository.deleteById(id);
    }

    @Override
    public void deleteByPrimaryKeys(String ids) {
        String[] idArray = ids.split("-");
        for (String idStr : idArray) {
            Long id = Long.valueOf(idStr);
            upmsUserRepository.deleteById(id);
        }
    }

    @Override
    public Optional<UpmsUser> selectByPrimaryKey(long id) {
        return upmsUserRepository.findById(id);
    }

    @Override
    public boolean checkReferralCode(String referralCode) {
        boolean isReferralCodeActive = false;

        if (StringUtils.isNotBlank(referralCode)) {
            List<UpmsUser> entities = upmsUserRepository.findByCellPhoneLike(referralCode);
            if (entities.size() > 0) {
                isReferralCodeActive = true;
            }
        }
        return isReferralCodeActive;
    }

}

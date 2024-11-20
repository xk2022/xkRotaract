package com.xk.upms.service.impl;

import com.xk.common.util.NotFoundException;
import com.xk.common.util.XkBeanUtils;
import com.xk.upms.dao.mapper.UpmsRoleMapper;
import com.xk.upms.dao.mapper.UpmsUserMapper;
import com.xk.upms.dao.repository.UpmsUserRefRepository;
import com.xk.upms.dao.repository.UpmsUserRepository;
import com.xk.upms.dao.repository.UpmsUserRoleRepository;
import com.xk.upms.model.bo.UpmsUserReq;
import com.xk.upms.model.bo.UpmsUserRoleSaveReq;
import com.xk.upms.model.bo.UpmsUserSaveReq;
import com.xk.upms.model.dto.UpmsUserRoleExample;
import com.xk.upms.model.po.UpmsRole;
import com.xk.upms.model.po.UpmsUser;
import com.xk.upms.model.po.UpmsUserRef;
import com.xk.upms.model.po.UpmsUserRole;
import com.xk.upms.model.vo.*;
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
 *
 * @author yuan Created on 2022/06/10.
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
    @Autowired
    private UpmsUserRoleRepository upmsUserRoleRepository;
    @Autowired
    private UpmsUserRefRepository upmsUserRefRepository;
    
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
    public UpmsUserResp findByEmail(String email) {
        UpmsUser entity = upmsUserRepository.findByEmail(email);
        return XkBeanUtils.copyProperties(entity, UpmsUserResp::new);
    }

    @Override
    public UpmsUserDetailResp selectDetailById(long id) {
        UpmsUserDetailResp result = new UpmsUserDetailResp();

        UpmsUser entity = upmsUserRepository.findById(id).get();
        if (entity == null) {
            throw new NotFoundException("查無使用者");
        }
        BeanUtils.copyProperties(entity, result);

        List<UpmsRole> roleList = upmsRoleMapper.findRolesByUserId(id);
        result.setRoleList(roleList);

        return result;
    }

    /**
     * 創建用戶
     *
     * @param resources
     * @return
     * @throws Exception
     */
    @Override
    public UpmsUserSaveResp create(UpmsUserSaveReq resources) {
        UpmsUserSaveResp result = new UpmsUserSaveResp();

        upmsUserValidator(resources);

        UpmsUser req = new UpmsUser();
        BeanUtils.copyProperties(resources, req);
//        String salt = UUID.randomUUID().toString().replaceAll("-", "");
//        req.setSalt(salt);
//        req.setPassword(MD5Utils.code(req.getPassword() + req.getSalt()));
        req.setLocked(false);
        req.setCreateTime(new Date());
        UpmsUser entity = upmsUserRepository.save(req);

        // 角色寫入
        UpmsUserRole upmsUserRole = new UpmsUserRole();
        upmsUserRole.setUserId(entity.getId());
        upmsUserRole.setRoleId(resources.getUserRole());
        upmsUserRoleRepository.save(upmsUserRole);

        // 推薦人紀錄
        if (StringUtils.isNotBlank(resources.getReferralCode())) {
            List<UpmsUser> referraler = upmsUserRepository.findByCellPhoneLike(resources.getReferralCode());

            UpmsUserRef refEntity = new UpmsUserRef();
            refEntity.setReferrerId(referraler.get(0).getId());
            refEntity.setRefereeId(entity.getId());
            refEntity.setReferralCode(resources.getReferralCode());
            upmsUserRefRepository.save(refEntity);
        }

        BeanUtils.copyProperties(entity, result);
        return result;
    }

    @Override
    public UpmsUserSaveResp update(Long id, UpmsUserSaveReq resources) {
        UpmsUserSaveResp result = new UpmsUserSaveResp();

        UpmsUser entity = upmsUserRepository.findById(id)
                .map(u -> {
                    // 这里可以对 UpmsUser 做一些操作
                    return u;
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (!entity.getId().equals(id)) {
            upmsUserValidator(resources);
        }

        UpmsUser req = new UpmsUser();
        BeanUtils.copyProperties(resources, req);
        entity = upmsUserRepository.save(req);

        BeanUtils.copyProperties(entity, result);
        return result;
    }

    private void upmsUserValidator(UpmsUserSaveReq resources) {

        // 01. check account only first
        boolean isEmailExist = this.checkField("email", resources.getEmail());
        if (isEmailExist) {
            try {
                throw new Exception("電子郵件，先前已註冊使用中。");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (resources.getCellPhone() != null) {
            boolean isCellPhoneExist = this.checkField("cellPhone", resources.getCellPhone());
            if (isCellPhoneExist) {
                try {
                    throw new Exception("行動電話，先前已註冊使用中。");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
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
    public boolean checkField(String columnName, String checkValue) {
        boolean isExist = false;
        UpmsUser entity;

        switch (columnName) {
            case "referralCode":
                List<UpmsUser> entities = upmsUserRepository.findByCellPhoneLike(checkValue);
                if (entities.size() > 0) {
                    isExist = true;
                }
                break;
            case "email":
                entity = upmsUserRepository.findByEmail(checkValue);
                if (entity != null) {
                    isExist = true;
                }
                break;
            case "cellPhone":
                entity = upmsUserRepository.findByCellPhone(checkValue);
                if (entity != null) {
                    isExist = true;
                }
        }
        return isExist;
    }

    @Override
    public void addRoleToUser(UpmsUserRoleSaveReq resources) {
        UpmsUserRole req = new UpmsUserRole();
        BeanUtils.copyProperties(resources, req);
        upmsUserRoleRepository.save(req);
    }

    @Override
    public void removeRoleFromUser(UpmsUserRoleSaveReq resources) {
        UpmsUserRole req = new UpmsUserRole();
        BeanUtils.copyProperties(resources, req);
        upmsUserRoleRepository.delete(req);
    }

    @Override
    public List<UpmsRoleCNDResp> getUserRoles(Long userId) {
        List<UpmsRoleCNDResp> result = new ArrayList<>();

        List<UpmsRole> entities = upmsRoleMapper.findRolesByUserId(userId);
        BeanUtils.copyProperties(entities, result);
        return result;
    }

}

package com.xk.admin.service.impl;


import com.xk.admin.model.dto.UserExample;
import com.xk.admin.service.AuthService;
import com.xk.cms.dao.repository.CmsUserRepository;
import com.xk.cms.model.po.CmsUser;
import com.xk.upms.dao.repository.UpmsUserRepository;
import com.xk.upms.model.po.UpmsUser;
import com.xk.upms.model.vo.UpmsUserSaveResp;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yuan on 2022/05/30
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UpmsUserRepository upmsUserRepository;
    @Autowired
    private CmsUserRepository cmsUserRepository;

    @Override
    public UserExample checkUser(String email, String password) {
        UserExample result = new UserExample();
//        T_User user = userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));
        // 改由 js 前端md5加密
        UpmsUser upmsUser = upmsUserRepository.findByEmailAndPassword(email, password);
        if (upmsUser != null) {
            BeanUtils.copyProperties(upmsUser, result);
        }

        CmsUser cmsUser = cmsUserRepository.findOneByFkUpmsUserId(upmsUser.getId());
        if (cmsUser != null) {
            BeanUtils.copyProperties(cmsUser, result);
        }

        return result;
    }

    @Override
    public Boolean checkUser(String email) {
        UpmsUser user = upmsUserRepository.findByEmail(email);
        return (user != null);
    }

    @Override
    public UpmsUser resetPassword(String email, String password) {
        UpmsUserSaveResp result = new UpmsUserSaveResp();

        UpmsUser user = upmsUserRepository.findByEmail(email);
        user.setPassword(password);
        UpmsUser entity = upmsUserRepository.save(user);

        BeanUtils.copyProperties(entity, result);
        return result;
    }

}

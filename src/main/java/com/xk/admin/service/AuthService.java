package com.xk.admin.service;

import com.xk.admin.model.dto.UserExample;
import com.xk.upms.model.po.UpmsUser;

import java.util.List;

/**
 * Created by yuan on 2024/01/11
 */
public interface AuthService {

    UserExample checkUser(String account, String password);

    Boolean checkUser(String email);

    UpmsUser resetPassword(String email, String password);

    List listSystemByAuth();

    List listPermission(Long systemId, Byte type);

}

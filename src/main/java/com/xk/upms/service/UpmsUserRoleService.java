package com.xk.upms.service;

import com.xk.upms.model.dto.UpmsUserRoleExample;

import java.util.List;

/**
 * UpmsUserRoleService 接口
 * Created by yuan on 2022/06/24
 */
public interface UpmsUserRoleService {

    int role(long userId, String roleId);

    int role(long userId, String[] roleIds);

    List<UpmsUserRoleExample> getUsers(long roleId);

}

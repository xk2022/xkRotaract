package com.xk.upms.service;

import com.xk.upms.model.bo.UpmsRolePermissionReq;

import java.util.List;

/**
 * UpmsRolePermissionService 接口
 * Created by yuan on 2022/06/24
 */
public interface UpmsRolePermissionService {

//    List list();

    void checkCountSameWithPermission(long roleId);

    List listBy(UpmsRolePermissionReq resource);

    List rolePermission(long roleId, String systemCode, String[] checkBoxValues);

    List listByAuth(long[] roleId);

}

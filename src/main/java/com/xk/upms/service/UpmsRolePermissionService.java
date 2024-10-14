package com.xk.upms.service;

import com.xk.upms.model.bo.UpmsRolePermissionReq;
import com.xk.upms.model.bo.UpmsRolePermissionSaveReq;
import com.xk.upms.model.vo.UpmsRolePermissionSaveResp;

import java.util.List;

/**
 * UpmsRolePermission Service 接口
 * @author yuan
 * Created by yuan on 2022/06/24
 */
public interface UpmsRolePermissionService {

//    List list();

    void checkCountSameWithPermission(long roleId);

    UpmsRolePermissionSaveResp create(UpmsRolePermissionSaveReq resources);

    List listBy(UpmsRolePermissionReq resource);

    List rolePermission(long roleId, String systemCode, String[] checkBoxValues);

    List listByAuth(long[] roleId);

}

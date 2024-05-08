package com.xk.upms.service;

import com.xk.upms.model.bo.UpmsPermissionReq;
import com.xk.upms.model.bo.UpmsPermissionSaveReq;
import com.xk.upms.model.po.UpmsPermission;
import com.xk.upms.model.po.UpmsSystem;
import com.xk.upms.model.vo.UpmsPermissionSaveResp;

import java.util.List;

/**
 * UpmsPermissionService 接口
 * Created by yuan on 2022/06/24
 */
public interface UpmsPermissionService {

    List list();

    List listBy(UpmsPermissionReq resources);

    UpmsPermissionSaveResp create(UpmsPermissionSaveReq resources);

    UpmsPermissionSaveResp update(Long id, UpmsPermissionSaveReq resources);

    void delete(Long id);

    void deleteByPrimaryKeys(String ids);

    Object getTreeByRoleId(long id);

    Object getTreeByUserId(long id, byte type);

    List selectBySystemIdAndRole(UpmsSystem system, long roleId);

    List buildTree(List<UpmsPermission> permissions);

    List<UpmsPermission> findAllMenuLevel();

    UpmsPermission findOneByUri(String uri);

    List<UpmsPermission> findBreadcrumbUri(String uri);
}

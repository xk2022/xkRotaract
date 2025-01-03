package com.xk.upms.service;

import com.xk.upms.model.bo.UpmsRoleSaveReq;
import com.xk.upms.model.po.UpmsRole;
import com.xk.upms.model.vo.UpmsRoleResp;
import com.xk.upms.model.vo.UpmsRoleSaveResp;

import java.util.List;
import java.util.Optional;

/**
 * UpmsRoleService 接口
 * Created by yuan on 2022/06/24
 */
public interface UpmsRoleService {

    List list();

    UpmsRoleSaveResp create(UpmsRoleSaveReq resources);

    UpmsRoleSaveResp update(Long id, UpmsRoleSaveReq resources);

    void delete(Long id);

    void deleteByPrimaryKeys(String ids);

    Optional<UpmsRole> selectByPrimaryKey(long id);

    UpmsRoleResp selectByCode(String code);

    UpmsRoleResp findById(Long id);

    void updateByUserIdAndRoleCode(Long upmsUserId, String roleCode);

    List getByCodeLikeWithActive(String codeLike, String rotaract_id);
//    List getByCodeLike(String codeLike);

}

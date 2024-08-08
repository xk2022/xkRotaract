package com.xk.upms.service;

import com.xk.upms.model.bo.UpmsUserReq;
import com.xk.upms.model.bo.UpmsUserSaveReq;
import com.xk.upms.model.po.UpmsUser;
import com.xk.upms.model.vo.UpmsUserDetailResp;
import com.xk.upms.model.vo.UpmsUserResp;
import com.xk.upms.model.vo.UpmsUserSaveResp;

import java.util.List;
import java.util.Optional;

/**
 * UpmsUserService 接口
 * Created by yuan on 2022/06/10
 */
public interface UpmsUserService {

    List list(UpmsUserReq resources);

    UpmsUserResp findByUsername(UpmsUserReq resources);
    
    UpmsUserDetailResp selectDeatilById(long id);

    UpmsUserSaveResp create(UpmsUserSaveReq resources);

    UpmsUserSaveResp update(Long id, UpmsUserSaveReq resources);

    void delete(Long id);

    void deleteByPrimaryKeys(String ids);

    Optional<UpmsUser> selectByPrimaryKey(long id);

    boolean checkField(String columnName, String checkValue);
}

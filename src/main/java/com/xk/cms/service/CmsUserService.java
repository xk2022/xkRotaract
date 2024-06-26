package com.xk.cms.service;

import com.xk.cms.model.bo.CmsUserSaveReq;
import com.xk.cms.model.vo.CmsUserSaveResp;

/**
 * CmsUserService 接口
 * Created by yuan on 2024/05/02
 */
public interface CmsUserService {

    CmsUserSaveResp create(CmsUserSaveReq resources);

    CmsUserSaveResp update(Long id, CmsUserSaveReq resources);

    CmsUserSaveResp getOneByEmail(String email);
}

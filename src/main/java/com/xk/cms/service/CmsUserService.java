package com.xk.cms.service;

import com.xk.cms.model.bo.CmsUserReq;
import com.xk.cms.model.bo.CmsUserSaveReq;
import com.xk.cms.model.vo.CmsUserSaveResp;

import java.util.List;

/**
 * UpmsUserService interface for managing CMS users.
 * Provides methods for CRUD operations on CmsUser entities.
 *
 * @author yuan Created on 2024/05/02.
 */
public interface CmsUserService {

    List list(CmsUserReq resources);

    CmsUserSaveResp create(CmsUserSaveReq resources);

    CmsUserSaveResp update(Long id, CmsUserSaveReq resources);

    CmsUserSaveResp getOneByEmail(String email);
}

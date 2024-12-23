package com.xk.cms.service;

import com.xk.cms.model.bo.CmsUserReq;
import com.xk.cms.model.bo.CmsUserSaveReq;
import com.xk.cms.model.vo.CmsUserResp;
import com.xk.cms.model.vo.CmsUserSaveResp;

import java.util.List;

/**
 * UpmsUserService interface for managing CMS users.
 * Provides methods for CRUD operations on CmsUser entities.
 *
 * @author yuan Created on 2024/05/02.
 */
public interface CmsUserService {

    /**
     * Retrieves a list of all users or filters users based on the specified criteria.
     *
     * @param resources a {@link CmsUserReq} object containing filter criteria.
     * @return a list of {@link CmsUserResp} representing matching users.
     */
    List getList(CmsUserReq resources);

    CmsUserSaveResp create(CmsUserSaveReq resources);

    CmsUserSaveResp update(Long id, CmsUserSaveReq resources);

    CmsUserSaveResp getOneByEmail(String email);
}

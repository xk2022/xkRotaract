package com.xk.cms.service;

import com.xk.cms.model.bo.CmsClubReq;
import com.xk.cms.model.bo.CmsClubSaveReq;
import com.xk.cms.model.vo.CmsClubResp;
import com.xk.cms.model.vo.CmsClubSaveResp;

import java.util.List;

/**
 * CmsClub Service interface
 *
 * @author yuan Created on 2024/09/18.
 */
public interface CmsClubService {

    List list();

    List listBy(CmsClubReq resources);

    CmsClubResp selectByOrganizationId(String organizationId);

    CmsClubSaveResp create(CmsClubSaveReq resources);

    CmsClubSaveResp update(Long id, CmsClubSaveReq resources);

    void deleteByPrimaryKeys(String ids);

    void uploadedImage(String id, byte[] bytes);

}

package com.xk.cms.service;

import com.xk.cms.model.bo.CmsClubInfoSaveReq;
import com.xk.cms.model.dto.CmsClubInfoOverview;
import com.xk.cms.model.vo.CmsClubInfoResp;
import com.xk.cms.model.vo.CmsClubInfoSaveResp;
import com.xk.upms.model.vo.UpmsOrganizationResp;

/**
 * CmsClubInfo Service interface
 *
 * @author yuan Created on 2024/11/20.
 */
public interface CmsClubInfoService {

    CmsClubInfoResp getOne(String rotaractId, UpmsOrganizationResp parentOrg);

    CmsClubInfoSaveResp create(CmsClubInfoSaveReq resources);

    CmsClubInfoSaveResp update(Long id, CmsClubInfoSaveReq resources);

    Boolean saveOverview(CmsClubInfoOverview resources);

}

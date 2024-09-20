package com.xk.cms.service;

import com.xk.cms.model.bo.CmsClubSaveReq;
import com.xk.cms.model.vo.CmsClubSaveResp;

import java.util.List;

/**
 * CmsClub Service interface
 * Created by yuan on 2024/0918
 * @author yuan
 */
public interface CmsClubService {

    List list();

    CmsClubSaveResp create(CmsClubSaveReq resources);

    CmsClubSaveResp update(Long id, CmsClubSaveReq resources);

    void deleteByPrimaryKeys(String ids);

}

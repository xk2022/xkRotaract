package com.xk.upms.service;

import com.xk.upms.model.bo.UpmsOrganizationReq;
import com.xk.upms.model.bo.UpmsOrganizationSaveReq;
import com.xk.upms.model.vo.UpmsOrganizationSaveResp;

import java.util.List;

/**
 * Created by yuan on 2022/06/24
 */
public interface UpmsOrganizationService {

    List list(UpmsOrganizationReq resources);

    UpmsOrganizationSaveResp create(UpmsOrganizationSaveReq resources);

    UpmsOrganizationSaveResp update(Long id, UpmsOrganizationSaveReq resources);

    void delete(Long id);

    void deleteByPrimaryKeys(String ids);

    Object selectByUserId(long id);

}
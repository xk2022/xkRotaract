package com.xk.cms.service;

import com.xk.cms.model.bo.CmsCompanySaveReq;
import com.xk.cms.model.vo.CmsCompanySaveResp;

import java.util.List;

/**
 * CmsCompany Service 接口
 * Created by yuan on 2024/05/02
 */
public interface CmsCompanyService {

    List list();

    List listByUser(long cms_user_id);

    CmsCompanySaveResp create(CmsCompanySaveReq resources);

    CmsCompanySaveResp update(Long id, CmsCompanySaveReq resources);

    void deleteByPrimaryKeys(String ids);

}

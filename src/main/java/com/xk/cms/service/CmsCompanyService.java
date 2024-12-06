package com.xk.cms.service;

import com.xk.cms.model.bo.CmsCompanyReq;
import com.xk.cms.model.bo.CmsCompanySaveReq;
import com.xk.cms.model.vo.CmsCompanySaveResp;
import com.xk.cms.model.vo.CmsCompanyWithUserResp;

import java.util.List;

/**
 * CmsCompany Service interface
 *
 * @author yuan Created on 2024/05/02.
 */
public interface CmsCompanyService {

    /**
     * Find all companys based on the given criteria.
     *
     * @param resources the criteria for filtering companys
     * @return a list of company responses
     */
    List list(CmsCompanyReq resources);

    List listByClub(CmsCompanyReq resources);

    List listByUser(long cms_user_id);

    List listByUserWithPay(long cms_user_id);

    CmsCompanySaveResp create(CmsCompanySaveReq resources);

    CmsCompanySaveResp update(Long id, CmsCompanySaveReq resources);

    void deleteByPrimaryKeys(String ids);

    CmsCompanyWithUserResp findOneWithPersonalByCompanyId(String reqCompanyId);

}

package com.xk.cms.service;

import com.xk.cms.model.bo.CmsCompanyPaySaveReq;
import com.xk.cms.model.vo.CmsCompanySaveResp;

import java.util.List;

/**
 * CmsCompanyPay Service interface
 *
 * @author yuan Created on 2024/08/22.
 */
public interface CmsCompanyPayService {

    List list();

    CmsCompanySaveResp create(CmsCompanyPaySaveReq resources);

}

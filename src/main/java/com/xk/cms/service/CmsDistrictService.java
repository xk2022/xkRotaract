package com.xk.cms.service;

import com.xk.cms.model.bo.CmsDistrictReq;
import com.xk.cms.model.bo.CmsDistrictSaveReq;
import com.xk.cms.model.vo.CmsDistrictSaveResp;

import java.util.List;

/**
 * CmsDistrict Service interface
 *
 * @author yuan Created on 2024/10/24
 */
public interface CmsDistrictService {

    List list();

    List listBy(CmsDistrictReq resources);

    CmsDistrictSaveResp create(CmsDistrictSaveReq resources);

    CmsDistrictSaveResp update(Long id, CmsDistrictSaveReq resources);

    void deleteByPrimaryKeys(String ids);

}

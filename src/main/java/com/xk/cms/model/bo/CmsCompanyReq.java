package com.xk.cms.model.bo;

import com.xk.cms.model.po.CmsCompany;
import lombok.Data;

/**
 * CmsCompany 請求物件
 *
 * @author yuan Created on 2024/11/26.
 */
@Data
public class CmsCompanyReq extends CmsCompany {

    private String rotaract_id;

}
package com.xk.cms.model.bo;

import com.xk.cms.model.po.CmsCompany;
import lombok.Data;

/**
 * CmsCompany 請求變更物件
 *
 * @author yuan Created on 2024/05/02.
 */
@Data
public class CmsCompanySaveReq extends CmsCompany {

    private String cmsUserId;

    private String industryIds;

    private boolean locked;

}
package com.xk.cms.model.bo;

import com.xk.cms.model.po.CmsCompany;
import lombok.Data;

import java.util.List;

/**
 * CmsSelf SaveReq
 * Created by yuan on 2024/05/02
 */
@Data
public class CmsCompanySaveReq extends CmsCompany {

    private long cmsUserId;

    private List<Long> industryIds;

}
package com.xk.cms.model.vo;

import com.xk.cms.model.po.CmsCompany;
import lombok.Data;

import java.util.List;

/**
 * UpmsUser SaveResp
 * Created by yuan on 2024/05/02
 */
@Data
public class CmsCompanySaveResp extends CmsCompany {

    private String industriesChinese;

    private List<Long> industryIds;

}
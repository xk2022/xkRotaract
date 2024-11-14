package com.xk.cms.model.dto;

import com.xk.cms.model.po.CmsUser;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @author yuan Created by yuan on 2024/11/13.
 */
@Getter
@Setter
public class CmsUserExample extends CmsUser {


//    private Long id; // cmsCompanyId
//    private Date createTime;
    private List<Long> industryIds;
    private String industriesChinese;
    /**
     * CmsCompany
     */
    private String companyName;

    /**
     * CmsCompanyPay
     */
    private Long cmsCompanyPayId;
    private Date payDate;
    private Boolean locked;


}

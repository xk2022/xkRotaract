package com.xk.cms.model.dto;

import com.xk.cms.model.po.CmsCompany;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * Created by yuan on 2024/08/22
 */
@Getter
@Setter
public class CmsCompanyExample extends CmsCompany {


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

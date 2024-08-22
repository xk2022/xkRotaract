package com.xk.cms.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by yuan on 2024/08/22
 */
@Getter
@Setter
public class CmsCompanyExample {

    /**
     * CmsCompany
     */
    private Long cmsCompanyId;
    private String companyName;
    private Date createTime;

    /**
     * CmsCompanyPay
     */
    private Long cmsCompanyPayId;
    private Date paydate;
    private Boolean locked;


}

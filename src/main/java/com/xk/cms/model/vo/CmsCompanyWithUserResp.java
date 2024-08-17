package com.xk.cms.model.vo;

import com.xk.upms.model.po.UpmsUser;
import lombok.Data;

import java.util.List;

/**
 * UpmsUser SaveResp
 * Created by yuan on 2022/06/24
 */
@Data
public class CmsCompanyWithUserResp extends UpmsUser {

    /**
     * CmsCompany
     */
    private Long companyId;
    private String name;
    private String phone;
    private String address;
    private String url;
//    private String industries;
    private List<Long> industryIds;
    private String industriesChinese;
    private String latlng;
    /**
     * CmsUser
     */
    private Long userId;
//    private Long fkUpmsUserId;
    private String rname;
    private String realname;
    private String district_id;
    private String district_name;
    private String rotaract_id;
    private String rotaract_name;
//    private String avatar;
//    private Byte sex;

}
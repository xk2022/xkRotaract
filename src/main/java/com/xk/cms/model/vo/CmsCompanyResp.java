package com.xk.cms.model.vo;

import lombok.Data;

import java.util.List;

/**
 * CmsCompany 響應前端物件
 *
 * @author yuan Created on 2024/11/26.
 */
@Data
public class CmsCompanyResp {

    /**
     * CmsCompany(String)
     */
    private String id;
    private String name;
    private String phone;
    private String address;
    private String url;
    private String industries;
    private String latlng;

    private String industriesChinese;

    private List<Long> industryIds;

    private boolean locked;

}
package com.xk.cms.model.vo;

import lombok.Data;

/**
 * Response object for saving CmsDistrict entities.
 * Provides the details of the saved CmsDistrict record.
 *
 * @author yuan Created on 2024/10/24.
 */
@Data
public class CmsDistrictSaveResp {

    private String id;

    private String code;

    private String name;

    private Boolean status;

}
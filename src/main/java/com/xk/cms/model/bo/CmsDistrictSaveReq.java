package com.xk.cms.model.bo;

import lombok.Data;

/**
 * Request object for saving CmsDistrict entities.
 * Represents the data required to create or update a CmsDistrict record.
 *
 * @author yuan Created by yuan on 2024/10/24.
 */
@Data
public class CmsDistrictSaveReq {

    private String id;
    private String code;
    private String name;
    private Boolean status;

}
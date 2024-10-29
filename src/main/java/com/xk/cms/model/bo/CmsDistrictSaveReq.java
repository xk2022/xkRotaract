package com.xk.cms.model.bo;

import lombok.Data;

/**
 * Request object for saving CmsDistrict entities.
 * Represents the data required to create or update a CmsDistrict record.
 *
 * Created on 2024/10/24.
 *
 * @author yuan
 */
@Data
public class CmsDistrictSaveReq {

    private String id;
    private String code;
    private String name;
    private Boolean status;

}
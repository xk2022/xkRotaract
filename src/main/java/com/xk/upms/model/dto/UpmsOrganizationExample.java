package com.xk.upms.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UpmsOrganizationExample implements Serializable {

    private Long organizationId;
    private String name;
    private String code;
    private String description;
    private Long parentId;
    private Integer level;
    private Long orders;
    private Boolean status;
    private Integer eventCount;

}

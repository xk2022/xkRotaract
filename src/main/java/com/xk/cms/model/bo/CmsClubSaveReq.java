package com.xk.cms.model.bo;

import lombok.Data;

/**
 * CmsClub 請求變更物件
 *
 * @author yuan Created on 2024/09/18.
 */
@Data
public class CmsClubSaveReq {

    private String id;

    private String fkUpmsOrganizationId;

    private String name;

    private String status;

}
package com.xk.cms.model.bo;

import lombok.Data;

/**
 * CmsClubInfo 請求變更物件
 *
 * @author yuan Created on 2024/11/20.
 */
@Data
public class CmsClubInfoSaveReq {

    private String id;

    private String clubId;

    private String infoKey;

    private String infoValue;

    private String status;

}
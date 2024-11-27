package com.xk.cms.model.bo;

import lombok.Data;

/**
 * CmsUserSelf 請求變更物件
 *
 * @author yuan Created on 2024/09/12.
 */
@Data
public class CmsUserSelfUpdateReq {

    private String rname;

    private String realname;

    private String district_id;

    private String rotaract_id;

}
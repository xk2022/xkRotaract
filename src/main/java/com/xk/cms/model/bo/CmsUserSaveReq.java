package com.xk.cms.model.bo;

import com.xk.cms.model.po.CmsUser;
import lombok.Data;

/**
 * CmsUser 請求變更物件
 *
 * @author yuan Created on 2024/05/02.
 */
@Data
public class CmsUserSaveReq extends CmsUser {

    private String username;

    private String cellPhone;

}
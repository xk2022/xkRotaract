package com.xk.cms.model.vo;

import com.xk.cms.model.po.CmsUser;
import lombok.Data;

/**
 * UpmsUser 響應變更後物件
 *
 * @author yuan Created on 2024/05/02.
 */
@Data
public class CmsUserSaveResp extends CmsUser {

    private String username;

    private String cellPhone;

}
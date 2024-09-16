package com.xk.cms.model.bo;

import com.xk.cms.model.po.CmsUser;
import lombok.Data;

/**
 * CmsUser SaveReq
 * Created by yuan on 2024/05/02
 * @author yuan
 */
@Data
public class CmsUserSaveReq extends CmsUser {

    private String username;

    private String cellPhone;

}
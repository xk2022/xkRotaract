package com.xk.cms.model.bo;

import com.xk.cms.model.po.CmsUser;
import lombok.Data;

/**
 * CmsSelf SaveReq
 * Created by yuan on 2024/05/02
 */
@Data
public class CmsUserSaveReq extends CmsUser {

    private String username;

}
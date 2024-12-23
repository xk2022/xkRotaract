package com.xk.cms.model.dto;

import com.xk.cms.model.po.CmsUser;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yuan Created by yuan on 2024/11/13.
 */
@Getter
@Setter
public class CmsUserExample extends CmsUser {

    private String email;

    private String roleId;

    private String roleDescription;

}

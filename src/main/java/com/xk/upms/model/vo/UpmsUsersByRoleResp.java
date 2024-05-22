package com.xk.upms.model.vo;

import com.xk.upms.model.po.UpmsUser;

/**
 * UpmsUser SaveResp
 * Created by yuan on 2022/06/24
 */
public class UpmsUsersByRoleResp extends UpmsUser {

    private Long id;
    private String username;
    private String email;
    private Boolean locked;

}

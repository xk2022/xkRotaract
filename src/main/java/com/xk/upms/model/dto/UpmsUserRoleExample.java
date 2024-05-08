package com.xk.upms.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by yuan on 2022/11/30
 */
@Getter
@Setter
public class UpmsUserRoleExample {

    private Long id;

    private String username;

    private String realname;

    private String avatar;

    private String email;

    /**
     * 状态(0:正常,1:锁定)
     */
    private Byte locked;

    private Date createTime;

    private String role_title;

}

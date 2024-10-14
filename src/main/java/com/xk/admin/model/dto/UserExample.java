package com.xk.admin.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserExample {

    private Long id; // upms_user_id

    private String username;

    private String email;

    private String rname;

    private String realname;

    private String district_id;

    private String rotaract_id;

    private String avatar;

    private String phone;

    private Byte sex;

    private long[] roleId;
}

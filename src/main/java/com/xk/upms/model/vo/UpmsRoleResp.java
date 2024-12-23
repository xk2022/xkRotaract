package com.xk.upms.model.vo;

import com.xk.upms.model.po.UpmsRole;
import lombok.Data;

/**
 * UpmsRole SaveResp
 * Created by yuan on 2024/05/06
 */
@Data
public class UpmsRoleResp extends UpmsRole {

    private long countUser;

    private String active = "true";

}

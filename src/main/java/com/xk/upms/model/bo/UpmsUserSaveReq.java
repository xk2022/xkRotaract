package com.xk.upms.model.bo;

import com.xk.upms.model.po.UpmsUser;
import lombok.Data;

/**
 * UpmsUser SaveReq
 * Created by yuan on 2022/06/24
 */
@Data
public class UpmsUserSaveReq extends UpmsUser {

    private Long userRole;

    private String referralCode;

}

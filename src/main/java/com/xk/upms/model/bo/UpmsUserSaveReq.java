package com.xk.upms.model.bo;

import com.xk.upms.model.po.UpmsUser;
import lombok.Data;

/**
 * Request object for saving UpmsUser entities.
 * Represents the data required to create or update a UpmsUser record.
 *
 * Created on 2022/06/24.
 * Updated on 2024/10/25 with code optimization based on GPT recommendations.
 *
 * @author yuan
 */
@Data
public class UpmsUserSaveReq extends UpmsUser {

    private Long userRole;
    private String referralCode;

}

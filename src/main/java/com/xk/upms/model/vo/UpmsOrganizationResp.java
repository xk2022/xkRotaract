package com.xk.upms.model.vo;

import com.xk.upms.model.po.UpmsOrganization;
import lombok.Data;

/**
 * UpmsOrganization Resp
 * Created by yuan on 2022/08/29
 */
@Data
public class UpmsOrganizationResp extends UpmsOrganization {

    private long countUser;

}

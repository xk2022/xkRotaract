package com.xk.upms.model.bo;

import lombok.Data;

/**
 * UpmsPermission Req
 * Created by yuan on 2022/06/29
 */
@Data
public class UpmsPermissionReq {

    /**
     * 所屬系統
     */
    private Long systemId;

    /**
     * 類型(1:目錄,2:菜單,3:按鈕)
     */
    private Byte type;

}

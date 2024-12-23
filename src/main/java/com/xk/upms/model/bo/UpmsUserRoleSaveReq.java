package com.xk.upms.model.bo;

import lombok.Data;

/**
 * UpmsUserRole 請求物件
 *
 * 用於接收前端的請求數據，包含組織的基本屬性，如名稱、編碼、層級等。
 * 該類別與 UpmsUserRole 實體類分離，專門用於前端傳遞參數並避免直接操作實體。
 *
 * @author yuan Created by yuan on 2024/12/16.
 */
@Data
public class UpmsUserRoleSaveReq {

    private String email;

    private String userRole;

    private String access_scope;

}

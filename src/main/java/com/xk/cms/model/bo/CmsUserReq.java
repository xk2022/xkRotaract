package com.xk.cms.model.bo;

import lombok.Data;

/**
 * CmsUser 請求物件
 *
 * 用於接收前端的請求數據，包含 CMS 使用者的基本屬性，如帳號資訊、角色、權限等。
 * 該類別與 CmsUser 實體類分離，專門用於前端傳遞參數並避免直接操作實體。
 *
 * - userId: 使用者唯一標識
 * - username: 使用者名稱
 * - email: 使用者電子郵件
 * - phoneNumber: 使用者電話號碼
 * - role: 使用者角色 (String)，如 "editor", "viewer", "admin" 等
 * - permissions: 權限列表 (List<String>)，包含具體權限如 "CREATE", "READ", "UPDATE", "DELETE"
 * - preferredLanguage: 使用者偏好語言 (String)
 * - notificationsEnabled: 是否啟用通知 (boolean)
 * - rotaractId: 所屬社團 ID (Long)，表示使用者的組織歸屬
 * - districtId: 地區 ID (Long)，表示使用者的區域範疇
 * - isActive: 帳號狀態 (boolean)，表示帳號是否啟用
 *
 * @author yuan Created by yuan on 2024/11/13.
 * Last updated on 2024/11/13 for enhanced functionality with additional user attributes.
 */
@Data
public class CmsUserReq {

//    private String userId;
//    private String username;
//    private String email;
//    private String phoneNumber;
//
//    // 用戶在內容管理系統中的角色或權限
//    private String role; // 例如 "editor", "viewer", "admin" 等
//    private List<String> permissions; // 用戶的具體權限列表，例如 ["CREATE", "READ", "UPDATE", "DELETE"]
//
//    // 可選：用戶的偏好設置
//    private String preferredLanguage;
//    private boolean notificationsEnabled;
//
//    // 預留的內容範疇 ID（例如特定內容組或社團）
    private String district_id;
    private String rotaract_id;
//
//    // 預留的帳號狀態，如是否啟用
//    private boolean isActive;

}
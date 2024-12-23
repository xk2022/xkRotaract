package com.xk.cms.model.vo;

import lombok.Data;

/**
 * CmsUser 響應前端物件
 *
 * 用於返回 CMS 使用者的基本屬性和狀態資訊，如帳號資訊、角色、權限等。
 * 該類別與 CmsUser 實體類分離，專門用於後端傳遞處理結果給前端。
 *
 * 屬性描述：
 * - roleName：用戶在 CMS 中的角色名稱
 * - realName：用戶的真實姓名
 * - districtId：用戶所屬地區 ID
 * - rotaractId：用戶所屬扶青社 ID
 * - avatarUrl：用戶的頭像 URL
 * - phoneNumber：用戶的聯絡電話
 * - gender：用戶的性別（如 "Male", "Female"）
 * - statusMessage：用於返回操作狀態訊息（如 "Active", "Suspended"）
 * - createdAt：帳號創建時間
 * - updatedAt：帳號最近更新時間
 *
 * @author yuan Created by yuan on 2024/11/13.
 * Last updated on 2024/11/13 for enhanced functionality with additional user attributes.
 */
@Data
public class CmsUserResp {

    private String id;
    private String rname;
    private String realname;
    private String district_id;
    private String rotaract_id;
//    private String avatarUrl;
    private String phoneNumber;
    private String gender;

    private String email;
    private String roleId;
    private String roleDescription;

}

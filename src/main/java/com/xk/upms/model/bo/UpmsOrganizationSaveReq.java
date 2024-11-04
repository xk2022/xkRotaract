package com.xk.upms.model.bo;

import lombok.Data;

/**
 * UpmsOrganization 請求物件
 *
 * 用於接收前端的請求數據，包含組織的基本屬性，如名稱、編碼、層級等。
 * 該類別與 UpmsOrganization 實體類分離，專門用於前端傳遞參數並避免直接操作實體。
 *
 * - id: 組織唯一標識 (String)
 * - name: 組織名稱
 * - code: 組織編碼
 * - description: 組織描述
 * - parentId: 父組織 ID (String)
 * - level: 組織層級 (String)
 * - orders: 排序欄位 (String)
 * - status: 組織狀態，表示是否啟用 (String)
 *
 * @author yuan Created by yuan on 2022/06/24.
 * Last updated on 2024/10/30 for enhanced functionality with type conversions.
 */
@Data
public class UpmsOrganizationSaveReq {

    private String id;
    private String name;
    private String code;
    private String description;
    private String parentId;
    private String level;
    private String orders;
    private String status;

    private String parentCode;

}

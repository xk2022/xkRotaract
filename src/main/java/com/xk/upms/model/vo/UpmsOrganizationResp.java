package com.xk.upms.model.vo;

import lombok.Data;

/**
 * UpmsOrganizationResp
 *
 * 用於響應前端的組織資訊 VO（View Object）。
 * 該類別封裝了組織的基本屬性，所有屬性均設置為 String 型別，以便於資料的展示、傳輸及前端的統一處理。
 *
 * 設計目的：
 * 1. **視圖層數據格式統一** - 將所有屬性設定為 String 型別，簡化前端的數據處理，減少型別轉換需求，並確保 JSON 格式的傳輸一致性。
 * 2. **方便展示與格式化** - String 型別便於前端直接進行格式化處理（如日期格式化、數字格式化等），適合在顯示層中直接使用。
 * 3. **專注於視圖需求** - 作為 VO，該類專注於顯示需求，並無業務層邏輯，僅用於數據呈現，適合在 Controller 層與前端進行交互。
 *
 * 屬性說明：
 * - id: 組織的唯一標識符
 * - name: 組織名稱
 * - code: 組織編碼，用於唯一識別
 * - description: 組織描述
 * - parentId: 父組織的唯一標識符，用於確定組織層次結構
 * - level: 組織層級，表示組織的層次
 * - orders: 組織的排序值，用於確定顯示順序
 * - status: 組織狀態，通常為啟用/禁用等狀態標識
 * - countUser: 該組織中的成員數量
 *
 * @author yuan Created on 2022/08/29
 * Last updated on 2024/10/30 to serve as a VO with all fields as Strings, ensuring consistency in data display and transmission.
 */
@Data
public class UpmsOrganizationResp {

    private String id;
    private String name;
    private String code;
    private String description;
    private String parentId;
    private String level;
    private String orders;
    private String status;

}

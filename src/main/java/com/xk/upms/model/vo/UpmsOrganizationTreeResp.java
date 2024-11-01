package com.xk.upms.model.vo;

import lombok.Data;
import java.util.List;

/**
 * UpmsOrganizationTreeResp
 *
 * 用於表示組織的樹形結構的 VO（View Object），適用於前端顯示組織樹。
 * 此類封裝了組織的基本屬性（如名稱、描述等），並包含遞歸的子節點列表，以構建完整的組織層次結構。
 *
 * 設計目的：
 * 1. **組織結構的樹形展示** - 適合於組織層次結構的展示，便於前端構建和顯示樹形結構。
 * 2. **層次遞歸結構** - 包含 `children` 屬性，用於遞歸地存儲子組織節點，使組織樹能夠無限擴展。
 * 3. **便於展示的屬性** - 包含 `open` 和 `checked` 等狀態屬性，以支持前端樹形組件（如展示是否展開、是否選中等）。
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
 * - open: 樹節點是否展開，便於前端樹形結構控制
 * - checked: 樹節點是否選中，便於前端樹形結構控制
 * - children: 子組織節點列表，遞歸形成完整的組織樹
 *
 * Created by yuan on 2022/08/29
 * Last updated on 2024/10/30 to serve as a tree structure with view-specific attributes.
 */
@Data
public class UpmsOrganizationTreeResp {

    private String id;
    private String name;
    private String code;
    private String description;
    private String parentId;
    private String level;
    private String orders;
    private String status;
    private String countUser;

    private boolean open;        // 樹節點是否展開
    private boolean checked;     // 樹節點是否選中

    private List<UpmsOrganizationTreeResp> children; // 子節點列表，遞歸構建樹形結構

}

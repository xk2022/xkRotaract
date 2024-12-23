package com.xk.cms.model.vo;

import lombok.Data;

/**
 * CmsCalendar 響應前端物件
 *
 * @author yuan Created on 2024/10/08.
 *
    {
        id: "d8jai7s",
        name: "Author's Birthday",
        description: "Author's note: Thank you for using EvoCalendar! :)",
        badge: "5-day event",
        date: "October/15/2024",
        type: "birthday", "event", "holiday"
        everyYear: !0
    }
 */
@Data
public class CmsCalendarEvoResp {

    private String id; // 活動 ID
    private String name; // 活動名稱
    private String description; // 活動描述
    private String badge; // 顯示的徽章
    private String date; // 活動日期
    private String type; // 活動類型 (event, holiday, birthday)
    private String everyYear = "!0"; // 是否為每年重複活動
    private String startTime; // 開始時間
    private String endTime; // 結束時間
    private String[] orgCodes; // 所屬地區/社團編碼，例如 ["D3502", "C35020001"]

}

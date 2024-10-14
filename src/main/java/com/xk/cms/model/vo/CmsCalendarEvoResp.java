package com.xk.cms.model.vo;

import lombok.Data;

/**
 * CmsCalendar Resp
 * @author yuan
 * Created by yuan on 2024/10/08
 *
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

    private String id;

    private String name;

    private String description;

    private String badge;

    private String date;

    private String type;

    private String everyYear = "!0";

}

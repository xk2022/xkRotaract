package com.xk.cms.model.vo;

import lombok.Data;

/**
 * CmsCalendar Resp
 * @author yuan
 * Created by yuan on 2024/09/24
 *
 *
//    {
//        id: uid(),
//                title: 'Meeting',
//            start: TODAY + 'T14:30:00',
//            end: TODAY + 'T15:30:00',
//            className: "fc-event-warning",
//            description: 'Lorem ipsum conse ctetur adipi scing',
//            location: 'Meeting Room 11.10'
//    }
 */
@Data
public class CmsCalendarResp {

    private String id;

    private String title;
    // TODAY + 'T14:30:00'
    private String start;
    // TODAY + 'T15:30:00'
    private String end;

    private String className;

    private String description;

    private String location;

}


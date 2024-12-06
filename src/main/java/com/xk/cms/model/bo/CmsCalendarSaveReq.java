package com.xk.cms.model.bo;

import lombok.Data;

/**
 * CmsClub 請求變更物件
 *
 * @author yuan Created on 2024/09/24ㄡ
 */
@Data
public class CmsCalendarSaveReq {

    private String id;

    private String eventName;

    private String eventDescription;

    private String eventLocation;

    private String allDay;

    private String startDate;

    private String startTime;

    private String endDate;

    private String endTime;

    private String district_id;

    private String rotaract_id;

    // event, holiday, birthday
    private String type;

    private String access_scope;

    private String initialDate;

    private String locked;

}
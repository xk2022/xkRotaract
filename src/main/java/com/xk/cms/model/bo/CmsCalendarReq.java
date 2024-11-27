package com.xk.cms.model.bo;

import lombok.Data;

/**
 * CmsClub 請求物件
 *
 * @author yuan Created on 2024/09/24.
 */
@Data
public class CmsCalendarReq {

    private String access_scope;

    private String eventName;

    private String eventDescription;

    private String eventLocation;

    private String startDate;

    private String startTime;

    private String endDate;

    private String endTime;

    private String district_id;

    private String rotaract_id;

}
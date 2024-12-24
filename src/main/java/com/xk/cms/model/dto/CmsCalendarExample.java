package com.xk.cms.model.dto;

import com.xk.cms.model.po.CmsCalendar;
import lombok.Data;

@Data
public class CmsCalendarExample extends CmsCalendar {

    private String orgCode;

    private String districtName;

    private String clubName;

    private String serviceLine;

}

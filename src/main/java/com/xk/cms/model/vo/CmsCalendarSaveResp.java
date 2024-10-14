package com.xk.cms.model.vo;

import com.xk.cms.model.po.CmsCalendar;
import lombok.Data;

/**
 * CmsCalendar SaveResp
 * @author yuan
 * Created by yuan on 2024/09/24
 */
@Data
public class CmsCalendarSaveResp extends CmsCalendar {

    private String startDateStr;

    private String endDateStr;

}
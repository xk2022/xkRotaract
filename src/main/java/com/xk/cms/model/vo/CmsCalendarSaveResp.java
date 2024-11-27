package com.xk.cms.model.vo;

import com.xk.cms.model.po.CmsCalendar;
import lombok.Data;

/**
 * CmsCompany 響應變更後前端物件
 *
 * @author yuan Created on 2024/09/24.
 */
@Data
public class CmsCalendarSaveResp extends CmsCalendar {

    private String startDateStr;

    private String endDateStr;

}
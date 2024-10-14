package com.xk.cms.service;

import com.xk.cms.model.bo.CmsCalendarReq;
import com.xk.cms.model.bo.CmsCalendarSaveReq;
import com.xk.cms.model.vo.CmsCalendarSaveResp;

import java.util.List;

/**
 * CmsCalendar Service interface
 * @author yuan
 * Created by yuan on 2024/09/24
 */
public interface CmsCalendarService {

    List list(CmsCalendarReq resource);

    List listBy(CmsCalendarReq resources);

    CmsCalendarSaveResp findById(Long id);

    CmsCalendarSaveResp create(CmsCalendarSaveReq resources);

    CmsCalendarSaveResp update(CmsCalendarSaveReq resources);

    void deleteByPrimaryKeys(String ids);

    List evoList(CmsCalendarReq req);
}

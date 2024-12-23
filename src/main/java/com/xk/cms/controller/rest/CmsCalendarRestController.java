package com.xk.cms.controller.rest;

import com.xk.cms.model.bo.CmsCalendarReq;
import com.xk.cms.model.bo.CmsCalendarSaveReq;
import com.xk.cms.model.vo.CmsCalendarEvoResp;
import com.xk.cms.service.CmsCalendarService;
import com.xk.common.base.BaseRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 行事曆 RestController
 * 管理行事曆的增刪改查操作。
 *
 * @author yuan Created on 2024/09/25.
 */
@RestController
@Api("行事曆管理api")
@RequestMapping("/api/manage/calendar")
public class CmsCalendarRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsCalendarRestController.class);

    @Autowired
    private BaseRepository baseRepository;
    @Autowired
    private CmsCalendarService cmsCalendarService;

    @ApiOperation(value = "系统列表")
    @PostMapping("/list")
    public Object list(@RequestBody CmsCalendarReq req) {
        return cmsCalendarService.list(req);
    }

    @ApiOperation(value = "系统列表")
    @GetMapping("/page_head")
    public Object page_head() {
        return baseRepository.queryTableComment("cms_company");
    }

    @ApiOperation(value = "系统列表")
    @PostMapping("/showEvo")
    public Object showEvo(@RequestBody CmsCalendarReq req) {
        return cmsCalendarService.evoList(req);
    }

    @PostMapping("/getAllByYear")
    @ApiOperation(value = "獲取所有行事曆數據")
    public ResponseEntity<List<CmsCalendarEvoResp>> getAllByYear(@RequestBody Map<String, Object> requestBody) {
        Integer year = (Integer) requestBody.get("year");

        if (year == null) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        try {
            List<CmsCalendarEvoResp> calendarData = cmsCalendarService.getCalendarDataByYear(year);
            return ResponseEntity.ok(calendarData);
        } catch (Exception e) {
            LOGGER.error("Error fetching calendar data", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @ApiOperation(value = "findById")
    @PostMapping("/findById")
    public Object findById(@RequestBody Map<String, Object> requestBody) {
        String id = (String) requestBody.get("id");
        return cmsCalendarService.findById(Long.valueOf(id));
    }

    /**
     * 删除行事历事件
     */
    @ApiOperation(value = "删除行事历事件")
    @DeleteMapping("/delete/{id}")
    public Object deleteEvent(@PathVariable String id) {
        CmsCalendarSaveReq req = new CmsCalendarSaveReq();
        req.setId(id);
        req.setLocked("true");
        return cmsCalendarService.update(req);
    }

    /**
     * init()
     */
    @GetMapping("/init")
    public String init() {

        return "OK";
    }

}


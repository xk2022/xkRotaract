package com.xk.cms.controller.rest;

import com.xk.cms.model.bo.CmsCalendarReq;
import com.xk.cms.service.CmsCalendarService;
import com.xk.common.base.BaseRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 行事曆 RestController
 * @author yuan
 * Created by yuan on 2024/09/25
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

    @ApiOperation(value = "findById")
    @PostMapping("/findById")
    public Object findById(@RequestBody Map<String, Object> requestBody) {
        String id = (String) requestBody.get("id");
        return cmsCalendarService.findById(Long.valueOf(id));
    }

    /**
     * init()
     */
    @GetMapping("/init")
    public String init() {

        return "OK";
    }

}


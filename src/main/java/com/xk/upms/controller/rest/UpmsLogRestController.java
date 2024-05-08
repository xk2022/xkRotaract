package com.xk.upms.controller.rest;

import com.xk.upms.service.UpmsLogService;
import com.xk.upms.service.UpmsUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 日志 RestController
 * Created by yuan on 2022/06/10
 */
@RestController
@Api("日志管理api")
@RequestMapping("/api/manage/log")
public class UpmsLogRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpmsLogRestController.class);

    @Autowired
    private UpmsLogService upmsLogService;

    @ApiOperation(value = "日志列表")
    @GetMapping("/list")
    public Object list() {
        return upmsLogService.list(null);
    }

}
package com.xk.upms.controller.rest;

import com.xk.common.base.BaseRepostitory;
import com.xk.upms.model.bo.UpmsSystemSaveReq;
import com.xk.upms.service.UpmsSystemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户 RestController
 * Created by yuan on 2022/06/10
 */
@RestController
@Api("系统管理api")
@RequestMapping("/api/manage/system")
public class UpmsSystemRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpmsSystemRestController.class);

    @Autowired
    private BaseRepostitory baseRepostitory;
    @Autowired
    private UpmsSystemService upmsSystemService;

    @ApiOperation(value = "系统列表")
    @GetMapping("/list")
    public Object list() {
        return upmsSystemService.list();
    }

    @ApiOperation(value = "系统列表")
    @GetMapping("/page_head")
    public Object page_head() {
        return baseRepostitory.queryTableComent("upms_system");
    }

    /**
     * init()
     */
    @GetMapping("/init")
    public String init() {
        UpmsSystemSaveReq data = new UpmsSystemSaveReq();
        data.setStatus(true);
        data.setCreateBy("system restAPI");

        data.setTheme("權限管理系統");
        data.setBasepath("/upms");
        data.setName("upms");
        data.setTitle("權限管理系統");
        data.setDescription("User Permissions Management System");
        data.setOrders(Long.valueOf(0));
        upmsSystemService.create(data);

        data.setTheme("內容管理系統");
        data.setBasepath("/cms");
        data.setName("cms");
        data.setTitle("內容管理系統");
        data.setDescription("Content Management System");
        data.setOrders(Long.valueOf(1));
        upmsSystemService.create(data);

        data.setStatus(false);
        data.setTheme("測試用");
        data.setBasepath("/test");
        data.setName("test");
        data.setTitle("測試用管理系統");
        data.setDescription("Test Management System");
        data.setOrders(Long.valueOf(9));
        upmsSystemService.create(data);
        return "OK";
    }

}


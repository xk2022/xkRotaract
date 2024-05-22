package com.xk.admin.controller.rest;

import com.xk.upms.controller.rest.UpmsPermissionRestController;
import com.xk.upms.controller.rest.UpmsRoleRestController;
import com.xk.upms.controller.rest.UpmsSystemRestController;
import com.xk.upms.controller.rest.UpmsUserRestController;
import io.swagger.annotations.Api;
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
@Api("系统啟動資料api")
@RequestMapping("/api/data/init")
public class AdminInitController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminInitController.class);

    @Autowired
    private UpmsSystemRestController upmsSystemRestController;
    @Autowired
    private UpmsPermissionRestController upmsPermissionRestController;
    @Autowired
    private UpmsRoleRestController upmsRoleRestController;
    @Autowired
    private UpmsUserRestController upmsUserRestController;
    /**
     * init()
     */
    @GetMapping("/init")
    public String init() {
        upmsSystemRestController.init();
        upmsPermissionRestController.init();
        upmsRoleRestController.init();
        upmsUserRestController.init();
        upmsUserRestController.role();
        upmsRoleRestController.permission();
        return "OK";
    }

}


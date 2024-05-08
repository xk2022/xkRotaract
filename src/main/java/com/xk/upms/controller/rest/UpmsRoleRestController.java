package com.xk.upms.controller.rest;

import com.xk.common.base.BaseRepostitory;
import com.xk.upms.model.bo.UpmsRoleSaveReq;
import com.xk.upms.service.UpmsRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 角色 RestController
 * Created by yuan on 2022/06/24
 */
@RestController
@Api("角色管理api")
@RequestMapping("/api/manage/role")
public class UpmsRoleRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpmsRoleRestController.class);

    @Autowired
    private BaseRepostitory baseRepostitory;
    @Autowired
    private UpmsRoleService upmsRoleService;

    @ApiOperation(value = "角色列表")
    @GetMapping("/cms_core")
    public Object list() {
        return upmsRoleService.list(null);
    }

    @ApiOperation(value = "角色列表")
    @GetMapping("/page_head")
    public Object page_head() {
        return baseRepostitory.queryTableComent("upms_role");
    }

    /**
     * init()
     */
    @GetMapping("/init")
    public String init() {
        UpmsRoleSaveReq data = new UpmsRoleSaveReq();
        data.setCreateTime(new Date());
        data.setCreateBy("admin");

        data.setCode("admin");
        data.setTitle("系統管理員");
        data.setDescription("系統管理員");
        upmsRoleService.create(data);

        data.setCode("sys");
        data.setTitle("管理員");
        data.setDescription("管理員");
        upmsRoleService.create(data);

        data.setCode("club_sys");
        data.setTitle("社團主權限");
        data.setDescription("社團主權限");
        upmsRoleService.create(data);

        data.setCode("club_P");
        data.setTitle("社長");
        data.setDescription("社長");
        upmsRoleService.create(data);

        data.setCode("club_S");
        data.setTitle("秘書");
        data.setDescription("秘書");
        upmsRoleService.create(data);

        return "OK";
    }

}


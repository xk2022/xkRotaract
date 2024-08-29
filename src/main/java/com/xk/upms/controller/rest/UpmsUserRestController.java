package com.xk.upms.controller.rest;

import com.xk.upms.model.bo.UpmsUserReq;
import com.xk.upms.model.bo.UpmsUserSaveReq;
import com.xk.upms.model.vo.UpmsRoleResp;
import com.xk.upms.model.vo.UpmsUserResp;
import com.xk.upms.service.UpmsRoleService;
import com.xk.upms.service.UpmsUserRoleService;
import com.xk.upms.service.UpmsUserService;
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
 * 用户 RestController
 * Created by yuan on 2022/06/10
 */
@RestController
@Api("用户管理api")
@RequestMapping("/api/manage/user")
public class UpmsUserRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpmsUserRestController.class);

    @Autowired
    private UpmsUserService upmsUserService;
    @Autowired
    private UpmsRoleService upmsRoleService;
    @Autowired
    private UpmsUserRoleService upmsUserRoleService;


    @ApiOperation(value = "用户列表")
    @GetMapping("/list")
    public Object list() {
        return upmsUserService.list(null);
    }


    /**
     * init()
     */
    @GetMapping("/init")
    public String init() throws Exception {
        UpmsUserSaveReq data = new UpmsUserSaveReq();
        data.setCreateBy("system restAPI");
        data.setCreateTime(new Date());

        data.setLocked(false);
        data.setPassword("e9bc0e13a8a16cbb07b175d92a113126"); // 密碼MD5(密碼+鹽)

        data.setEmail("admin@admin"); // 郵箱（帳號一）
        data.setUsername("ADMIN"); // 自訂（帳號二）
        data.setCellPhone("axktest"); // 手機（帳號三）
        upmsUserService.create(data);

        data.setEmail("louis@louis"); // 郵箱（帳號一）
        data.setUsername("louis"); // 自訂（帳號二）
        data.setCellPhone("0978628329"); // 手機（帳號三）
        upmsUserService.create(data);

        data.setEmail("cwei@cwei"); // 郵箱（帳號一）
        data.setUsername("cwei"); // 自訂（帳號二）
        data.setCellPhone("cxktest"); // 手機（帳號三）
        upmsUserService.create(data);

        data.setEmail("bonnie@bonnie"); // 郵箱（帳號一）
        data.setUsername("bonnie"); // 自訂（帳號二）
        data.setCellPhone("bxktest"); // 手機（帳號三）
        upmsUserService.create(data);

        data.setEmail("summer@summer"); // 郵箱（帳號一）
        data.setUsername("summer"); // 自訂（帳號二）
        data.setCellPhone("sxktest"); // 手機（帳號三）
        upmsUserService.create(data);

        data.setEmail("member@member"); // 郵箱（帳號一）
        data.setUsername("member"); // 自訂（帳號二）
        data.setCellPhone("mxktest"); // 手機（帳號三）
        upmsUserService.create(data);

        return "OK";
    }

    /**
     * role()
     */
    @GetMapping("/role")
    public void role() {
        UpmsUserReq resources = new UpmsUserReq();

        /**
         * admin
         */
        UpmsRoleResp respRole = upmsRoleService.selectByCode("admin");

        resources.setUsername("ADMIN");
        UpmsUserResp respUser = upmsUserService.findByUsername(resources);
        upmsUserRoleService.role(respUser.getId(), String.valueOf(respRole.getId()));
        /**
         * sys
         */
        respRole = upmsRoleService.selectByCode("sys");

        resources.setUsername("louis");
        respUser = upmsUserService.findByUsername(resources);
        upmsUserRoleService.role(respUser.getId(), String.valueOf(respRole.getId()));

        resources.setUsername("cwei");
        respUser = upmsUserService.findByUsername(resources);
        upmsUserRoleService.role(respUser.getId(), String.valueOf(respRole.getId()));

        resources.setUsername("bonnie");
        respUser = upmsUserService.findByUsername(resources);
        upmsUserRoleService.role(respUser.getId(), String.valueOf(respRole.getId()));

        resources.setUsername("summer");
        respUser = upmsUserService.findByUsername(resources);
        upmsUserRoleService.role(respUser.getId(), String.valueOf(respRole.getId()));
        /**
         * member
         */
        respRole = upmsRoleService.selectByCode("member");

        resources.setUsername("member");
        respUser = upmsUserService.findByUsername(resources);
        upmsUserRoleService.role(respUser.getId(), String.valueOf(respRole.getId()));
    }
}


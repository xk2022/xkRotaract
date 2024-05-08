package com.xk.upms.controller.rest;

import com.xk.upms.model.bo.UpmsUserSaveReq;
import com.xk.upms.model.po.UpmsRole;
import com.xk.upms.service.UpmsRoleService;
import com.xk.upms.service.UpmsUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

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


    @ApiOperation(value = "用户列表")
    @GetMapping("/list")
    public Object list() {
        return upmsUserService.list(null);
    }


    /**
     * init()
     */
    @GetMapping("/init")
    public String init() {
        UpmsUserSaveReq data = new UpmsUserSaveReq();
        data.setCreateBy("system restAPI");
//        data.setCreateTime(new Date());
        Set<UpmsRole> roles = new HashSet<>(upmsRoleService.list(null));
//        data.setRoles(roles);
        data.setLocked(false);
//        data.setPhone("0987654321"); // 行動電話（帳號二）
        data.setPassword("e9bc0e13a8a16cbb07b175d92a113126"); // 密碼MD5(密碼+鹽)

        data.setUsername("ADMIN"); // 扶輪社友名稱
//        data.setRealname("系統管理員"); // 姓名
        data.setEmail("admin@admin"); // 郵箱（帳號一）
        upmsUserService.create(data);

        data.setUsername("sys"); // 扶輪社友名稱
//        data.setRealname("管理員"); // 姓名
        data.setEmail("sys@sys"); // 郵箱（帳號一）
        upmsUserService.create(data);

        data.setUsername("club_sys"); // 扶輪社友名稱
//        data.setRealname("社團主權限"); // 姓名
        data.setEmail("club@sys"); // 郵箱（帳號一）
        upmsUserService.create(data);

        data.setUsername("club_P"); // 扶輪社友名稱
//        data.setRealname("社長"); // 姓名
        data.setEmail("p@club"); // 郵箱（帳號一）
        upmsUserService.create(data);

        data.setUsername("club_S"); // 扶輪社友名稱
//        data.setRealname("秘書"); // 姓名
        data.setEmail("s@club"); // 郵箱（帳號一）
        upmsUserService.create(data);
        return "OK";
    }
}


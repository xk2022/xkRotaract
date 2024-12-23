package com.xk.upms.controller.rest;

import com.xk.common.base.BaseRepository;
import com.xk.upms.model.bo.UpmsRolePermissionReq;
import com.xk.upms.model.bo.UpmsRoleSaveReq;
import com.xk.upms.model.vo.UpmsPermissionResp;
import com.xk.upms.model.vo.UpmsRoleResp;
import com.xk.upms.service.UpmsPermissionService;
import com.xk.upms.service.UpmsRolePermissionService;
import com.xk.upms.service.UpmsRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

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
    private BaseRepository baseRepository;
    @Autowired
    private UpmsRoleService upmsRoleService;
    @Autowired
    private UpmsPermissionService upmsPermissionService;
    @Autowired
    private UpmsRolePermissionService upmsRolePermissionService;

    @ApiOperation(value = "角色列表")
    @GetMapping("/cms_core")
    public Object list() {
        return upmsRoleService.list();
    }

    @ApiOperation(value = "角色列表")
    @GetMapping("/page_head")
    public Object page_head() {
        return baseRepository.queryTableComment("upms_role");
    }

    @ApiOperation(value = "角色列表")
    @PostMapping("/permissionsBySystem")
    @ResponseBody
    public Object permissionsBySystem(@RequestBody UpmsRolePermissionReq resources) {
        return upmsRolePermissionService.listBy(resources);
    }



    /**
     * init()
     */
    @GetMapping("/init")
    public String init() {
        UpmsRoleSaveReq data = new UpmsRoleSaveReq();
        data.setCreateTime(new Date());
        data.setCreateBy("system restAPI");
        /**
         * organization of admin
         */
        data.setCode("admin");
        data.setTitle("系統管理員");
        data.setDescription("系統管理員");
        data.setOrders((long) 1);
        upmsRoleService.create(data);

        data.setCode("sys");
        data.setTitle("管理員");
        data.setDescription("管理員");
        data.setOrders((long) 2);
        upmsRoleService.create(data);

        data.setCode("webmaster");
        data.setTitle("本站小編");
        data.setDescription("本站小編");
        data.setOrders((long) 3);
        upmsRoleService.create(data);

        /**
         * organization of rit
         */
        data.setCode("rit_sys");
        data.setTitle("RIT主權限");
        data.setDescription("RIT主權限");
        data.setOrders((long) 4);
        upmsRoleService.create(data);

        data.setCode("rit_p");
        data.setTitle("MDIO RIT President");
        data.setDescription("MDIO RIT President");
        data.setOrders((long) 5);
        upmsRoleService.create(data);

        data.setCode("rit_member");
        data.setTitle("MDIO RIT Member");
        data.setDescription("MDIO RIT Member");
        data.setOrders((long) 6);
        upmsRoleService.create(data);
        /**
         * organization of district
         */
        data.setCode("district_sys");
        data.setTitle("地區主權限");
        data.setDescription("地區主權限");
        data.setOrders((long) 7);
        upmsRoleService.create(data);

        data.setCode("district_p");
        data.setTitle("District President");
        data.setDescription("地區代表");
        data.setOrders((long) 8);
        upmsRoleService.create(data);

        data.setCode("district_member");
        data.setTitle("地區幹部");
        data.setDescription("地區幹部");
        data.setOrders((long) 9);
        upmsRoleService.create(data);
        /**
         * organization of club
         */
        data.setCode("club_sys");
        data.setTitle("社團主權限");
        data.setDescription("社團主權限");
        data.setOrders((long) 10);
        upmsRoleService.create(data);

        data.setCode("club_p");
        data.setTitle("Club President");
        data.setDescription("社長");
        data.setOrders((long) 11);
        upmsRoleService.create(data);

        data.setCode("club_s");
        data.setTitle("Club Secretary");
        data.setDescription("秘書");
        data.setOrders((long) 12);
        upmsRoleService.create(data);

        data.setCode("club_chairman");
        data.setTitle("社團幹部");
        data.setDescription("社團幹部");
        data.setOrders((long) 13);
        upmsRoleService.create(data);

        data.setCode("club_member");
        data.setTitle("社友");
        data.setDescription("社友");
        data.setOrders((long) 14);
        upmsRoleService.create(data);

        data.setCode("rookie");
        data.setTitle("菜鳥");
        data.setDescription("首次註冊，尚未被驗證的社友");
        data.setOrders((long) 15);
        upmsRoleService.create(data);

        data.setCode("club_honor");
        data.setTitle("榮譽社友");
        data.setDescription("榮譽社友");
        data.setOrders((long) 16);
        upmsRoleService.create(data);

        data.setCode("club_ob");
        data.setTitle("OB社友");
        data.setDescription("OB社友");
        data.setOrders((long) 17);
        upmsRoleService.create(data);

        data.setCode("club_pass");
        data.setTitle("過往社友");
        data.setDescription("過往社友");
        data.setOrders((long) 18);
        upmsRoleService.create(data);
        return "OK";
    }

    /**
     * permission()
     */
    @GetMapping("/permission")
    public void permission() {
        List<UpmsPermissionResp> permissions = upmsPermissionService.list();

        // 将 id 转换为 String[]
        String[] checkBoxValues = permissions.stream()
                .map(UpmsPermissionResp::getId)
                .map(String::valueOf) // 转换为字符串
                .toArray(String[]::new);
        UpmsRoleResp resp = upmsRoleService.selectByCode("admin");
        upmsRolePermissionService.rolePermission(resp.getId(), null, checkBoxValues);
    }
}


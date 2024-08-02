package com.xk.upms.controller.rest;

import com.xk.common.base.BaseRepostitory;
import com.xk.upms.model.bo.UpmsPermissionSaveReq;
import com.xk.upms.model.po.UpmsPermission;
import com.xk.upms.model.po.UpmsSystem;
import com.xk.upms.service.UpmsPermissionService;
import com.xk.upms.service.UpmsSystemService;
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
 * 權限 RestController
 * Created by yuan on 2022/06/24
 */
@RestController
@Api("權限管理api")
@RequestMapping("/api/manage/permission")
public class UpmsPermissionRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpmsPermissionRestController.class);

    @Autowired
    private BaseRepostitory baseRepostitory;
    @Autowired
    private UpmsPermissionService upmsPermissionService;
    @Autowired
    private UpmsSystemService upmsSystemService;

    @ApiOperation(value = "權限列表")
    @GetMapping("/list")
    public Object list() {
        return upmsPermissionService.list();
    }

    @ApiOperation(value = "權限列表")
    @GetMapping("/page_head")
    public Object page_head() {
        return baseRepostitory.queryTableComent("upms_permission");
    }

    /**
     * init()
     */
    @GetMapping("/init")
    public String init() {
        UpmsPermission findParentEntity = null;
        UpmsSystem findSystemEntity = null;

        UpmsPermissionSaveReq data = new UpmsPermissionSaveReq();
        data.setCreateTime(new Date());
        data.setCreateBy("system restAPI");
        data.setStatus(true);
        /**
         * /admin/index
         */
//        data.setSystemId((long) 1);
        data.setPid(null);
        data.setName("首頁");
        data.setType((byte) 0);
        data.setPermissionValue(":_fragments");
        data.setUri("/admin/index");
        data.setOrders((long) 0);
        upmsPermissionService.create(data);
        /**
         * /admin/upms
         */
        findSystemEntity = (UpmsSystem) upmsSystemService.findOneByName("upms");
        data.setSystemId(findSystemEntity.getId());

        data.setPid(null);
        data.setName("權限管理系統");
        data.setType((byte) 0);
        data.setPermissionValue("upms:manage");
        data.setUri("/admin/upms/manage");
        data.setOrders((long) 0);
        upmsPermissionService.create(data);
        /**
         * /admin/upms/po
         */
        data.setPid(null);
        data.setName("系統組織管理");
        data.setType((byte) 1);
        data.setPermissionValue("upms:po");
        data.setUri("/admin/upms/manage/po");
        data.setOrders((long) 1);
        upmsPermissionService.create(data);

        findParentEntity = upmsPermissionService.findOneByUri("/admin/upms/manage/po");
        data.setPid(findParentEntity.getId());
        data.setType((byte) 2);

        data.setName("系統管理");
        data.setPermissionValue("upms:system");
        data.setUri("/admin/upms/manage/system");
        data.setOrders((long) 11);
        upmsPermissionService.create(data);

        data.setName("組織管理");
        data.setPermissionValue("upms:organization");
        data.setUri("/admin/upms/manage/organization");
        data.setOrders((long) 12);
        upmsPermissionService.create(data);
        /**
         * /admin/upms/ru
         */
        data.setPid(null);
        data.setName("角色用戶管理");
        data.setType((byte) 1);
        data.setPermissionValue("upms:ru");
        data.setUri("/admin/upms/manage/ru");
        data.setOrders((long) 2);
        upmsPermissionService.create(data);

        findParentEntity = upmsPermissionService.findOneByUri("/admin/upms/manage/ru");
        data.setPid(findParentEntity.getId());
        data.setType((byte) 2);

        data.setName("角色管理");
        data.setPermissionValue("upms:role");
        data.setUri("/admin/upms/manage/role");
        data.setOrders((long) 21);
        upmsPermissionService.create(data);

        data.setName("用戶管理");
        data.setPermissionValue("upms:user");
        data.setUri("/admin/upms/manage/user");
        data.setOrders((long) 22);
        upmsPermissionService.create(data);

        findParentEntity = upmsPermissionService.findOneByUri("/admin/upms/manage/role");
        data.setPid(findParentEntity.getId());
        data.setName("角色權限");
        data.setPermissionValue("upms:role/detail");
        data.setUri("/admin/upms/manage/role/detail");
        data.setOrders((long) 21);
        upmsPermissionService.create(data);
        /**
         * /admin/upms/p
         */
        data.setPid(null);
        data.setName("權限資源管理");
        data.setType((byte) 1);
        data.setPermissionValue("upms:p");
        data.setUri("/admin/upms/manage/p");
        data.setOrders((long) 3);
        upmsPermissionService.create(data);

        findParentEntity = upmsPermissionService.findOneByUri("/admin/upms/manage/p");
        data.setPid(findParentEntity.getId());
        data.setType((byte) 2);

        data.setName("權限管理");
        data.setPermissionValue("upms:permission");
        data.setUri("/admin/upms/manage/permission");
        data.setOrders((long) 31);
        upmsPermissionService.create(data);
        /**
         * /admin/upms/o
         */
        data.setPid(null);

        data.setName("其他數據管理");
        data.setType((byte) 1);
        data.setPermissionValue("upms:o");
        data.setUri("/admin/upms/manage/o");
        data.setOrders((long) 4);
        upmsPermissionService.create(data);

        findParentEntity = upmsPermissionService.findOneByUri("/admin/upms/manage/o");
        data.setPid(findParentEntity.getId());
        data.setType((byte) 2);

        data.setName("公用碼表");
        data.setPermissionValue("upms:comm");
        data.setUri("/admin/upms/manage/comm");
        data.setOrders((long) 41);
        upmsPermissionService.create(data);

        data.setName("會話管理");
        data.setPermissionValue("upms:session");
        data.setUri("/admin/upms/manage/session");
        data.setOrders((long) 42);
        upmsPermissionService.create(data);

        data.setName("日誌紀錄");
        data.setPermissionValue("upms:log");
        data.setUri("/admin/upms/manage/log");
        data.setOrders((long) 43);
        upmsPermissionService.create(data);

        data.setName("鍵值管理");
        data.setPermissionValue("upms:key");
        data.setUri("/admin/upms/manage/key");
        data.setOrders((long) 44);
        upmsPermissionService.create(data);

        data.setName("公司行號");
        data.setPermissionValue("cms:company");
        data.setUri("/admin/cms/manage/company");
        data.setOrders((long) 44);
        upmsPermissionService.create(data);
        /**
         * /admin/cms
         */
        findSystemEntity = (UpmsSystem) upmsSystemService.findOneByName("cms");
        data.setSystemId(findSystemEntity.getId());

        data.setPid(null);
        data.setName("內容管理系統");
        data.setType((byte) 0);
        data.setPermissionValue("cms:manage");
        data.setUri("/admin/cms/manage");
        data.setOrders((long) 0);
        upmsPermissionService.create(data);
        /**
         * /admin/cms/u
         */
        data.setPid(null);
        data.setName("個人內容管理");
        data.setType((byte) 1);
        data.setPermissionValue("cms:u");
        data.setUri("/admin/cms/manage/u");
        data.setOrders((long) 1);
        upmsPermissionService.create(data);

        findParentEntity = upmsPermissionService.findOneByUri("/admin/cms/manage/u");
        data.setPid(findParentEntity.getId());
        data.setType((byte) 2);

        data.setName("我的資料");
        data.setPermissionValue("cms:self");
        data.setUri("/admin/cms/manage/self");
        data.setOrders((long) 11);
        upmsPermissionService.create(data);

        return "OK";
    }

}


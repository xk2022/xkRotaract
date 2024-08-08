package com.xk.cms.controller.rest;

import com.xk.cms.service.CmsCompanyService;
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
 * 公司 RestController
 * Created by yuan on 2024/08/05
 */
@RestController
@Api("公司管管理api")
@RequestMapping("/api/manage/xxxxxxxxxx")
public class CmsCompanyRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsCompanyRestController.class);

    @Autowired
    private BaseRepostitory baseRepostitory;
    @Autowired
    private CmsCompanyService cmsCompanyService;

    @ApiOperation(value = "系统列表")
    @GetMapping("/list")
    public Object list() {
        return cmsCompanyService.list();
    }

    @ApiOperation(value = "系统列表")
    @GetMapping("/page_head")
    public Object page_head() {
        return baseRepostitory.queryTableComent("cms_company");
    }

    /**
     * init()
     */
    @GetMapping("/init")
    public String init() {

        return "OK";
    }

}


package com.xk.cms.controller.rest;

import com.xk.cms.model.vo.CmsCompanyWithUserResp;
import com.xk.cms.service.CmsCompanyService;
import com.xk.common.base.BaseRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 公司 RestController
 * Created by yuan on 2024/08/05
 */
@RestController
@Api("公司管管理api")
@RequestMapping("/api/manage/company")
public class CmsCompanyRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsCompanyRestController.class);

    @Autowired
    private BaseRepository baseRepository;
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
        return baseRepository.queryTableComment("cms_company");
    }

    @ApiOperation(value = "公司詳情")
    @PostMapping("/findInfo")
    public CmsCompanyWithUserResp findCompanyAllInfo(@RequestBody Map<String, Object> requestBody) {
        String companyId = (String) requestBody.get("id");
        return cmsCompanyService.findOneWithPersonalByCompanyId(companyId);
    }

    /**
     * init()
     */
    @GetMapping("/init")
    public String init() {

        return "OK";
    }

}


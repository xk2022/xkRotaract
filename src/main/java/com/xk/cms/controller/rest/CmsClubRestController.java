package com.xk.cms.controller.rest;

import com.xk.cms.model.bo.CmsClubReq;
import com.xk.cms.service.CmsClubService;
import com.xk.common.base.BaseRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 社團 RestController
 * @author yuan
 * Created by yuan on 2024/10/08
 */
@RestController
@Api("社團管理api")
@RequestMapping("/api/manage/club")
public class CmsClubRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsClubRestController.class);

    @Autowired
    private BaseRepository baseRepository;
    @Autowired
    private CmsClubService cmsClubService;

    @ApiOperation(value = "社團列表")
    @GetMapping("/list")
    public Object list() {
        return cmsClubService.list();
    }

    @ApiOperation(value = "社團列表")
    @PostMapping("/list")
    public List list(@RequestBody CmsClubReq resources) {
        return cmsClubService.listBy(resources);
    }

    @ApiOperation(value = "系统列表")
    @GetMapping("/page_head")
    public Object page_head() {
        return baseRepository.queryTableComment("cms_club");
    }


    /**
     * init()
     * 替 CmsClubInfo 建立基本資料欄位名稱
     */
    @GetMapping("/init")
    public String init() {


        return "OK";
    }

}


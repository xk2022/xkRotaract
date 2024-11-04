package com.xk.upms.controller.rest;

import com.xk.common.base.BaseRepository;
import com.xk.upms.model.bo.UpmsSystemSaveReq;
import com.xk.upms.service.UpmsDictionaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 字典 RestController
 * Created by yuan on 2024/06/07
 */
@RestController
@Api("系统管理api")
@RequestMapping("/api/manage/dictionary")
public class UpmsDictionaryRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpmsDictionaryRestController.class);

    @Autowired
    private BaseRepository baseRepository;
    @Autowired
    private UpmsDictionaryService upmsDictionaryService;

    @ApiOperation(value = "字典子列表")
    @PostMapping("/listDictionaryData")
    public List listDictionaryData(@RequestBody Map<String, Object> requestBody) {
        String code = (String) requestBody.get("code");
        return upmsDictionaryService.listDDbyDC(code);
    }

    @ApiOperation(value = "系统列表")
    @GetMapping("/page_head")
    public Object page_head() {
        return baseRepository.queryTableComment("upms_dictionary_category");
    }

    /**
     * init()
     */
    @GetMapping("/init")
    public String init() {
        UpmsSystemSaveReq data = new UpmsSystemSaveReq();
        data.setStatus(true);
        data.setCreateBy("system restAPI");

        return "OK";
    }

}


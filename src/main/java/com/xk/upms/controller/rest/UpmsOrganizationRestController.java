package com.xk.upms.controller.rest;

import com.xk.upms.model.bo.UpmsSystemSaveReq;
import com.xk.upms.service.UpmsOrganizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 組織 RestController
 *
 * @author yuan Created on 2024/11/01
 */
@Api("組織管理 API")
@RestController
@RequestMapping("/api/manage/organization")
public class UpmsOrganizationRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpmsOrganizationRestController.class);

    @Autowired
    private UpmsOrganizationService upmsOrganizationService;

    @ApiOperation(value = "查找指定組織的子組織列表")
    @PostMapping("/findChildren")
    public List listDictionaryData(@RequestBody Map<String, Object> requestBody) {
        String code = (String) requestBody.get("code");

        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("Code 參數不可為空");
        }

        LOGGER.info("查詢子組織，父組織代碼: {}", code);
        return upmsOrganizationService.findChildren(code);
    }


    @ApiOperation(value = "初始化設置")
    @GetMapping("/init")
    public String init() {
        UpmsSystemSaveReq data = new UpmsSystemSaveReq();
        data.setStatus(true);
        data.setCreateBy("system restAPI");

        // baseRepository.save(data); // 假設有儲存邏輯

        LOGGER.info("初始化成功，建立人: {}", data.getCreateBy());
        return "Initialization OK";
    }
}



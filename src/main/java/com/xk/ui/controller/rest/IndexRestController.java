package com.xk.ui.controller.rest;

import com.xk.ui.model.vo.BubbleInfo;
import com.xk.ui.model.vo.CompanyLoc;
import com.xk.ui.service.IndexCompanyService;
import com.xk.ui.service.IndexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Index RestController
 *
 * @author yuan Created on 2024/08/05.
 */
@RestController
@Api("首頁相關api")
@RequestMapping("/api/manage/index")
public class IndexRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexRestController.class);

    @Autowired
    private IndexCompanyService indexCompanyService;
    @Autowired
    private IndexService indexService;

    @ApiOperation(value = "公司列表")
    @GetMapping("/locationCompany")
    public CompanyLoc locationCompany() {
        return indexCompanyService.get();
    }

    @ApiOperation(value = "泡泡球列表")
    @GetMapping("/bubbles")
    public List<BubbleInfo> getBubbleList() {
        LOGGER.info("Fetching bubble list");
        return indexService.getBubbleList();
    }

}


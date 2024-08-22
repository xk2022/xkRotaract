package com.xk.ui.controller.web;

import com.xk.cms.service.CmsSelfService;
import com.xk.common.base.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * IndexMapController
 * Created by yuan on 2024/08/01
 */
@Controller
@RequestMapping("/BizMap")
public class IndexMapController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexMapController.class);

    @Autowired
    private CmsSelfService cmsSelfService;

    /**
     * 首頁
     */
    @GetMapping()
    public String index(Model model) {

        model.addAttribute("chunkedIndustries", cmsSelfService.getChunkedIndustries());
        return DIR_MAP_INDEX;
    }
}

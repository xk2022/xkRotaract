package com.xk.ui.controller.web;

import com.xk.cms.service.CmsSelfService;
import com.xk.common.base.BaseController;
import com.xk.common.json.Industry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

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
        this.info(model);

        List<List<Industry>> industries = cmsSelfService.getChunkedIndustries();

        if (industries == null) {
            industries = new ArrayList<>();  // 初始化空列表，避免null
        }
        // 打印获取到的 industries 数据以调试
        industries.forEach(chunk -> chunk.forEach(industry -> {
            LOGGER.info("Industry: " + industry.getName() + ", Key: " + industry.getKey());
        }));
        model.addAttribute("chunkedIndustries", industries);
        return DIR_MAP_INDEX;
    }
}

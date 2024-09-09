package com.xk.ui.controller.web;

import com.xk.common.base.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller
 * Created by yuan on 2024/08/14
 */
@Controller
@RequestMapping("/")
public class IndexController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    /**
     * 查詢 首頁
     */
    @GetMapping()
    public String index(Model model) {
        return DIR_INDEX;
    }

}

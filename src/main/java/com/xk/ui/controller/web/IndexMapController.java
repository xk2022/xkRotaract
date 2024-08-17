package com.xk.ui.controller.web;

import com.xk.common.base.BaseController;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *  Controller
 * Created by yuan on 2024//
 */
@Controller
@RequestMapping("/BizMap")
public class IndexMapController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexMapController.class);

    //    private static final String DIR_INDEX = "truck/apps/user-management/permissions/";
//    private static final String INDEX = DIR_INDEX + "permission";
//    private static final String COMPILER = DIR_INDEX + "permission_add";
    private static final String REDIRECT_ADDR = "redirect:/admin/upms/manage/role";
    private static final int PAGE_SIZE = 8;

    /**
     * 查詢 角色 首頁
     */
//    @RequiresPermissions("upms:role:read")
    @GetMapping()
    public String index(Model model) {
        return DIR_MAP_INDEX;
    }
}

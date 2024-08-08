package com.xk.admin.controller.web;

import com.xk.admin.service.AuthService;
import com.xk.common.base.BaseController;
import com.xk.upms.service.UpmsSystemService;
import com.xk.upms.service.UpmsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 後台管理頁面
 * Created by yuan on 2022/06/10
 */
@Controller
@RequestMapping("/admin/index")
public class AdminIndexController extends BaseController {

    private static final String DIR_INDEX = "admin/";
    private static final String INDEX = DIR_INDEX + "auth/login";
    private static final String REDIRECT_ADDR = "redirect:/admin";

    @Autowired
    private UpmsUserService userService;
    @Autowired
    private UpmsSystemService upmsSystemService;
    @Autowired
    private AuthService authService;

    /**
     * /admin/index
     * @param model
     * @return
     */
    @GetMapping()
    public String index(Model model) {
        this.info(model, this.getClass().getAnnotation(RequestMapping.class).value()[0]);

        /** TODO
         * 20240804 讓角色繞過畫面，直接進入個人資料填寫
         */
        if (model.getAttribute("tree_only") != null) {
            return "redirect:" + model.getAttribute("tree_only");
        }
//        model.addAttribute("page_list", upmsSystemService.listActive());
        model.addAttribute("page_list", authService.listSystem());
        return ADMIN_INDEX;
    }

}

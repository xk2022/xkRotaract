package com.xk.admin.controller.web;

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

//    @GetMapping
//    public String loginPage() {
//        return INDEX;
//    }

    @GetMapping()
    public String index(Model model) {
//        model.addAttribute("fragmentSystem", "");
//        model.addAttribute("fragmentPackage", "_fragments");
        this.info(model, this.getClass().getAnnotation(RequestMapping.class).value()[0]);

        model.addAttribute("page_list", upmsSystemService.listActive());
        return ADMIN_INDEX;
    }

}

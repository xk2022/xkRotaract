package com.xk.admin.controller.web;

import com.xk.admin.model.dto.UserExample;
import com.xk.admin.service.AuthService;
import com.xk.common.base.BaseController;
import com.xk.upms.model.vo.UpmsRoleResp;
import com.xk.upms.service.UpmsRoleService;
import com.xk.upms.service.UpmsSystemService;
import com.xk.upms.service.UpmsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * 後台管理頁面
 * @author yuan
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
    @Autowired
    private UpmsRoleService upmsRoleService;

    /**
     * /admin/index
     * @param model
     * @return
     */
    @GetMapping()
    public String index(Model model, HttpSession session) {
        this.info(model, this.getClass().getAnnotation(RequestMapping.class).value()[0]);

        // 从 HttpSession 中获取用户信息
        UserExample user = (UserExample) session.getAttribute("user");

        UpmsRoleResp roleRookie = upmsRoleService.selectByCode("rookie");
        for (long sessionRoleId : user.getRoleId()) {
            if (sessionRoleId == roleRookie.getId()) {
                return "redirect:/admin/cms/manage/self";
            }
        }

//        model.addAttribute("page_list", upmsSystemService.listActive());
        model.addAttribute("page_list", authService.listSystemByAuth());
        return ADMIN_INDEX;
    }

}

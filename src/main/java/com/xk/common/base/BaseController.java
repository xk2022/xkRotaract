package com.xk.common.base;

import com.xk.admin.service.AuthService;
import com.xk.upms.model.po.UpmsPermission;
import com.xk.upms.model.po.UpmsSystem;
import com.xk.upms.service.UpmsPermissionService;
import com.xk.upms.service.UpmsSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.List;

/**
 * Created by yuan on 2022/05/24
 */
@Controller
public class BaseController {

    public static final String DIR_INDEX = "/index/index";
    public static final String DIR_MAP_INDEX = "/bizmap/index";
    public static final String ADMIN_INDEX = "/admin/index";
    public static final String R_ADMIN_INDEX = "redirect:/admin/index"; // REDIRECT_ADDR
    private static final String R_AUTH_LOGOUT = "redirect:/admin/logout";

    @Autowired
    private UpmsSystemService upmsSystemService;
    @Autowired
    private UpmsPermissionService upmsPermissionService;
    @Autowired
    private AuthService authService;

    public Model info(Model model, String path) {
        /* find this page info private */
        UpmsPermission info = upmsPermissionService.findOneByUri(path);
        model.addAttribute("info", info);

        model.addAttribute("header_breadcrumb", upmsPermissionService.findBreadcrumbUri(path));
        String[] fragment = info.getPermissionValue().split(":");
        model.addAttribute("fragmentSystem", fragment[0]);
        model.addAttribute("fragmentPackage", fragment[1]);

        /* Aside data Setting */
//        model.addAttribute("aside_system", upmsSystemService.listActive());
        model.addAttribute("aside_system", authService.listSystemByAuth());
        UpmsSystem system = (UpmsSystem) upmsSystemService.findOneByName(fragment[0]);
        model.addAttribute("system", system);

//        model.addAttribute("left_tree", upmsPermissionService.buildTree(upmsPermissionService.selectBySystemIdAndRole(system, 1)));
        if (system != null) {
            model.addAttribute("left_tree", upmsPermissionService.buildTree(authService.listPermission(system.getId())));
        }
        /**TODO
         * 單功能快速進入管道
         */
        List<UpmsPermission> permissions = authService.checkPermissionType2();
        if (permissions.size() == 1 ) {
            UpmsPermission onlyPermissionType2 = permissions.get(0);
            model.addAttribute("tree_only", onlyPermissionType2.getUri());
        } else {
            model.addAttribute("tree_only", null);
        }

        model.addAttribute("system_title", null);
        model.addAttribute("system_list", null);
        model.addAttribute("user_menus", null);
        model.addAttribute("user", null);
        model.addAttribute("menus", null);
        return model;
    }

}


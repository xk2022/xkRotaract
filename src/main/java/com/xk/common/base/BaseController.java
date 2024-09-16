package com.xk.common.base;

import com.xk.admin.model.dto.UserExample;
import com.xk.admin.service.AuthService;
import com.xk.upms.model.po.UpmsPermission;
import com.xk.upms.model.po.UpmsSystem;
import com.xk.upms.service.UpmsPermissionService;
import com.xk.upms.service.UpmsRolePermissionService;
import com.xk.upms.service.UpmsSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
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

    @Value("${app.baseUrl:defaultBaseUrl}") // 如果配置没找到，使用 defaultBaseUrl
    private String baseUrl;

    @Autowired
    private UpmsSystemService upmsSystemService;
    @Autowired
    private UpmsPermissionService upmsPermissionService;
    @Autowired
    private AuthService authService;
    @Autowired
    private UpmsRolePermissionService upmsRolePermissionService;

    public Model info(Model model) {
        model.addAttribute("baseUrl", baseUrl);

        return model;
    }

    public Model info(Model model, String path) {
        model.addAttribute("baseUrl", baseUrl);
        /* find this page info private */
        UpmsPermission upInfo = upmsPermissionService.findOneByUri(path);
        model.addAttribute("upInfo", upInfo);

        model.addAttribute("header_breadcrumb", upmsPermissionService.findBreadcrumbUri(path));
        String[] fragment = upInfo.getPermissionValue().split(":");
        model.addAttribute("fragmentSystem", fragment[0]);
        model.addAttribute("fragmentPackage", fragment[1]);

        /* Aside data Setting */
        UpmsSystem system = (UpmsSystem) upmsSystemService.findOneByName(fragment[0]);
        model.addAttribute("system", system);
        model = this.versionBasic(model);

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

//        model.addAttribute("system_title", null);
//        model.addAttribute("system_list", null);
//        model.addAttribute("user_menus", null);
//        model.addAttribute("user", null);
//        model.addAttribute("menus", null);
        return model;
    }

    private Model versionBasic(Model model) {

        /* Aside data Setting */
//        model.addAttribute("aside_system", upmsSystemService.listActive());
//        model.addAttribute("aside_system", authService.listSystemByAuth());

        UpmsSystem system = (UpmsSystem) model.getAttribute("system");
//        model.addAttribute("left_tree", upmsPermissionService.buildTree(upmsPermissionService.selectBySystemIdAndRole(system, 1)));
        if (system != null) {
            model.addAttribute("left_tree", upmsPermissionService.buildTree(authService.listPermission(system.getId())));
        }

        return model;
    }

    public void versionSession(HttpSession session) {
        // 从 HttpSession 中获取用户信息
        UserExample user = (UserExample) session.getAttribute("user");

        // 检查 user 是否为空，防止空指针异常
        if (user != null && user.getRoleId() != null) {
            session.setAttribute("aside_system", authService.listSystemByAuth());
            session.setAttribute("permissionAction", upmsRolePermissionService.listByAuth(user.getRoleId()));

            // 添加更多的上下文信息到日志
//            LOGGER.info("Permissions set for user role ID: {}", user.getRoleId());
        } else {
            // 记录警告日志，指出用户信息不存在
//            LOGGER.warn("User information not found in session or role ID is null.");
        }
    }


}


package com.xk.common.base;

import com.xk.admin.model.dto.UserExample;
import com.xk.admin.service.AuthService;
import com.xk.upms.model.po.UpmsPermission;
import com.xk.upms.model.po.UpmsSystem;
import com.xk.upms.service.UpmsPermissionService;
import com.xk.upms.service.UpmsRolePermissionService;
import com.xk.upms.service.UpmsSystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;

/**
 * @author yuan 
 * Created by yuan on 2022/05/24
 */
@Controller
public class BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    public static final String DIR_INDEX = "/index/index";
    public static final String ADMIN_INDEX = "/admin/index";
    public static final String ERROR_MSG = "/error/msg";
    public static final String REDIRECT_ADMIN_INDEX = "redirect:/admin/index"; // REDIRECT_ADDR
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

        return model;
    }

    public String errorMsg(Model model, String title, String content) {
        model.addAttribute("title", title);
        model.addAttribute("content", content);
        return ERROR_MSG;
    }

    private Model versionBasic(Model model) {

        /* Aside data Setting */
//        model.addAttribute("aside_system", upmsSystemService.listActive());
//        model.addAttribute("aside_system", authService.listSystemByAuth());

        UpmsSystem system = (UpmsSystem) model.getAttribute("system");
//        model.addAttribute("left_tree", upmsPermissionService.buildTree(upmsPermissionService.selectBySystemIdAndRole(system, 1)));
        if (system != null) {
            model.addAttribute("left_tree", upmsPermissionService.buildTree(authService.listPermission(system.getId(), null)));
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


    /**
     * Helper method to add fragment attributes to the model.
     *
     * @param model the model to pass attributes
     * @param system the system name for the fragment
     * @param viewPackage the package name for the fragment
     * @param viewName the specific fragment name
     */
    public void addFragmentAttributes(Model model, String system, String viewPackage, String viewName) {
        model.addAttribute("fragmentSystem", system);
        model.addAttribute("fragmentPackage", viewPackage);
        model.addAttribute("fragmentName", viewName);
    }

}


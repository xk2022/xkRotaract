package com.xk.admin.controller.web;

import com.xk.admin.model.dto.UserExample;
import com.xk.admin.service.AuthService;
import com.xk.common.base.BaseController;
import com.xk.upms.model.bo.UpmsUserSaveReq;
import com.xk.upms.model.po.UpmsRole;
import com.xk.upms.model.po.UpmsUser;
import com.xk.upms.model.vo.UpmsUserSaveResp;
import com.xk.upms.service.UpmsRoleService;
import com.xk.upms.service.UpmsUserRoleService;
import com.xk.upms.service.UpmsUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * 授權、根據token獲取用戶詳細訊息
 * Controller for handling authorization and user details retrieval based on token.
 * Provides endpoints for user login, registration, password reset, and related actions.
 *
 * @author yuan Created on 2024/01/11.
 * @author yuan Updated on 2024/12/05 with code optimization.
 */
@Controller
@RequestMapping("/admin")
public class AuthorizationController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationController.class);

    // Base directory for admin views
    private static final String ADMIN_DIR = "admin";
    // View paths for authentication pages
    private static final String VIEW_AUTH_INDEX = ADMIN_DIR + "/auth/index";
    // Redirect paths for authentication actions
    private static final String REDIRECT_SIGNIN = "redirect:/admin/signIn";
    private static final String REDIRECT_SIGNUP = "redirect:/admin/signUp";
    private static final String REDIRECT_PASSWORD_RESET = "redirect:/admin/passwordReset";
    private static final String REDIRECT_NEW_PASSWORD = "redirect:/admin/newPassword";


    @Autowired
    private AuthService authService;
    @Autowired
    private UpmsUserService upmsUserService;
    @Autowired
    private UpmsRoleService upmsRoleService;
    @Autowired
    private UpmsUserRoleService upmsUserRoleService;

    /**
     * Handles the sign-in page request.
     * Adds required fragments to the model and returns the authentication view.
     */
    @GetMapping("/signIn")
    public String signIn(Model model) {
//        this.addFragmentAttributes(model, "fragmentSystem", "fragmentPackage", "fragmentName");
        this.addFragmentAttributes(model, "auth", "signIn", "view");
        return VIEW_AUTH_INDEX;
    }

    /**
     * Handles user login requests.
     */
    @PostMapping("/signIn")
    public String login(@RequestParam String account, @RequestParam String password
            , HttpSession session, RedirectAttributes attributes) {
        try {
            // 密码解密 Authenticate user credentials
            UserExample user = authService.checkUser(account, password);

            // TODO 20241104 快速通道，讓District & Club主帳號，快速建立密碼（看情況優化代碼刪除）
            // Handle quick password setup for specific accounts (District & Club main accounts)
            if (user == null && passWay(account)) {
                attributes.addFlashAttribute("email", account);
                return REDIRECT_NEW_PASSWORD;
            }

            // Successful login
            if (user != null) {
                session.setAttribute("user", user);
                this.versionSession(session); // Additional session setup
                return REDIRECT_ADMIN_INDEX;
            }
            // Login failed
            attributes.addFlashAttribute("message", "用戶名和密碼錯誤");
            return REDIRECT_SIGNIN;
        } catch (Exception e) {
            LOGGER.error("Login failed for account: {}", account, e);
            attributes.addFlashAttribute("message", "系統錯誤，請稍後再試");
            return REDIRECT_SIGNIN;
        }
    }

    /**
     * Checks if the account is allowed to use the quick password setup.
     * for firstLoginIn.
     */
    private Boolean passWay(String email) {
        // Quick password setup applies only to Club or District accounts with no password set
        if (email.contains("@Club") || email.contains("@District")) {
            // 當密碼為空，result: true
            return authService.checkPassNullable(email);
        }
        return false;
    }

    /**
     * Handles user logout requests.
     * Clears user session and redirects to the sign-in page.\
     */
    @GetMapping("/logout")
    public String logout(Model model, HttpSession session) {
        // Add fragments for the sign-in view
        this.addFragmentAttributes(model, "auth", "signIn", "view");

        // 销毁 session, Clear session data
        session.removeAttribute("user");
        session.invalidate(); // Invalidate the session
        return VIEW_AUTH_INDEX;
    }

    /**
     * Handles the sign-up page request.
     * Sets up the necessary fragments and an empty entity for user input.
     */
    @GetMapping("/signUp")
    public String signUp(Model model) {
        // Add fragments for the sign-up view
        addFragmentAttributes(model, "auth", "signUp", "view");
        // Initialize an empty entity for user input
        model.addAttribute("entity", new UpmsUserSaveReq());
        return VIEW_AUTH_INDEX;
    }

    /**
     * Handles user sign-up requests.
     */
    @PostMapping("/signUp")
    public String signUp(UpmsUserSaveReq resources, RedirectAttributes attributes) {

        // Validate email uniqueness
        if (upmsUserService.checkField("email", resources.getEmail())) {
            attributes.addFlashAttribute("message", "電子郵件，先前已註冊使用中。請返回登入頁面");
            return REDIRECT_SIGNUP;
        }
        // Validate referral code
        if (!upmsUserService.checkField("referralCode", resources.getReferralCode())) {
            attributes.addFlashAttribute("message", "推薦碼有誤，請重新取得");
            return REDIRECT_SIGNUP;
        }
        // Create user account
        resources.setCreateBy("signUp by self");
        UpmsUserSaveResp result = upmsUserService.create(resources);

        if (result == null) {
            attributes.addFlashAttribute("message", "操作失敗，請稍後再試");
            return REDIRECT_SIGNUP;
        }
        LOGGER.info("新增用户，主键：userId={}", result.getId());
        // Assign default role
        UpmsRole role = upmsRoleService.selectByCode("rookie");
        if (role == null) {
            // 如果角色不存在，可以處理相應邏輯
            attributes.addFlashAttribute("message", "操作失敗，系統角色有誤，通知管理員");
            return REDIRECT_SIGNUP;
        }
        String[] roleIds = {String.valueOf(role.getId())};
        upmsUserRoleService.role(result.getId(), roleIds);
        return REDIRECT_SIGNIN;
    }

    @GetMapping("/passwordReset")
    public String passwordReset(Model model) {
        model.addAttribute("fragmentSystem", "auth");
        model.addAttribute("fragmentPackage", "passwordReset");
        model.addAttribute("fragmentName", "view");
        return VIEW_AUTH_INDEX;
    }

    @PostMapping("/passwordReset")
    public String passwordReset(@RequestParam String email, Model model,
                        HttpSession session, RedirectAttributes attributes) {

        Boolean hasUser = authService.checkUser(email);
        if (hasUser) {
            attributes.addFlashAttribute("email", email);
            return REDIRECT_NEW_PASSWORD;
        } else {
            attributes.addFlashAttribute("message", "用戶名錯誤");
            return REDIRECT_PASSWORD_RESET;
        }
    }

    @GetMapping("/newPassword")
    public String newPassword(Model model) {
        model.addAttribute("fragmentSystem", "auth");
        model.addAttribute("fragmentPackage", "newPassword");
        model.addAttribute("fragmentName", "view");
        return VIEW_AUTH_INDEX;
    }

    @PostMapping("/newPassword")
    public String newPassword(@RequestParam String email, @RequestParam String password, Model model,
                                HttpSession session, RedirectAttributes attributes) {
        UpmsUser user = authService.resetPassword(email, password);
        if (user != null) {
            return REDIRECT_SIGNIN;
        } else {
            attributes.addFlashAttribute("message", "系統錯誤");
            return REDIRECT_NEW_PASSWORD;
        }
    }

}

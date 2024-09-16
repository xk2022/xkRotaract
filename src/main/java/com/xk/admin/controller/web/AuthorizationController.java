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
 * Created by yuan on 2024/01/11
 * 授權、根據token獲取用戶詳細訊息
 */
@Controller
@RequestMapping("/admin")
public class AuthorizationController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationController.class);

    private static final String DIR_INDEX = "admin";
    private static final String AUTH_INDEX = DIR_INDEX + "/auth/index";
//    private static final String R_AUTH_INDEX = "redirect:/admin/auth/index"; // REDIRECT_ADDR
    private static final String R_AUTH_SIGNIN = "redirect:/admin/signIn"; // REDIRECT_ADDR
    private static final String R_AUTH_SIGNUP = "redirect:/admin/signUp"; // REDIRECT_ADDR
    private static final String R_AUTH_PASSWORDRESET = "redirect:/admin/passwordReset"; // REDIRECT_ADDR
    private static final String R_AUTH_NEWPASSWORD = "redirect:/admin/newPassword"; // REDIRECT_ADDR

    @Autowired
    private AuthService authService;
    @Autowired
    private UpmsUserService upmsUserService;
    @Autowired
    private UpmsRoleService upmsRoleService;
    @Autowired
    private UpmsUserRoleService upmsUserRoleService;

    @GetMapping("/signIn")
    public String signIn(Model model) {
        model.addAttribute("fragmentSystem", "auth");
        model.addAttribute("fragmentPackage", "signIn");
        model.addAttribute("fragmentName", "view");
        return AUTH_INDEX;
    }

    @PostMapping("/signIn")
    public String login(@RequestParam String account, @RequestParam String password, Model model,
                        HttpSession session, RedirectAttributes attributes) {
        // 密码解密
//        String password = MD5Utils.convertMD5(authUser.getPassword());
        UserExample user = authService.checkUser(account, password);

        if (user != null) {
            session.setAttribute("user", user);
            this.versionSession(session);

            return R_ADMIN_INDEX;
        } else {
            attributes.addFlashAttribute("message", "用戶名和密碼錯誤");
            return R_AUTH_SIGNIN;
        }
    }

    @GetMapping("/logout")
    public String logout(Model model, HttpSession session) {
        model.addAttribute("fragmentSystem", "auth");
        model.addAttribute("fragmentPackage", "signIn");
        model.addAttribute("fragmentName", "view");

        session.removeAttribute("user");
        session.invalidate(); // 销毁 session
        return AUTH_INDEX;
    }

    @GetMapping("/signUp")
    public String signUp(Model model) {
        model.addAttribute("fragmentSystem", "auth");
        model.addAttribute("fragmentPackage", "signUp");
        model.addAttribute("fragmentName", "view");

        model.addAttribute("entity", new UpmsUser());
        return AUTH_INDEX;
    }

    @PostMapping("/signUp")
    public String signUp(UpmsUserSaveReq resources, RedirectAttributes attributes, HttpSession session) throws Exception {
        UpmsUserSaveResp result;

        // 01. check referral Code first
        boolean isReferralCodeExist = upmsUserService.checkField("referralCode", resources.getReferralCode());
        if (!isReferralCodeExist) {
            // 如果角色不存在，可以處理相應邏輯
            attributes.addFlashAttribute("message", "推薦碼有誤，請重新取得");
            return R_AUTH_SIGNUP;
        }
        // 02. check email first
        boolean isEmailExist = upmsUserService.checkField("email", resources.getEmail());
        if (isEmailExist) {
            // 如果email已經存在
            attributes.addFlashAttribute("message", "電子郵件，先前已註冊使用中。請返回登入頁面");
            return R_AUTH_SIGNUP;
        }

        resources.setCreateBy("signUp by self");
        result = upmsUserService.create(resources);
        LOGGER.info("新增用户，主键：userId={}", result.getId());

        UpmsRole role = upmsRoleService.selectByCode("member");
        if (role == null) {
            // 如果角色不存在，可以處理相應邏輯
            attributes.addFlashAttribute("message", "操作失敗，系統角色有誤，通知管理員");
            return R_AUTH_SIGNUP;
        }
        String[] roleIds = {String.valueOf(role.getId())};
        upmsUserRoleService.role(result.getId(), roleIds);



        if (result != null) {
            return R_AUTH_SIGNIN;
        } else {
            attributes.addFlashAttribute("message", "操作失敗");
            return R_AUTH_SIGNUP;
        }
    }

    @GetMapping("/passwordReset")
    public String passwordReset(Model model) {
        model.addAttribute("fragmentSystem", "auth");
        model.addAttribute("fragmentPackage", "passwordReset");
        model.addAttribute("fragmentName", "view");
        return AUTH_INDEX;
    }

    @PostMapping("/passwordReset")
    public String passwordReset(@RequestParam String email, Model model,
                        HttpSession session, RedirectAttributes attributes) {

        Boolean hasUser = authService.checkUser(email);
        if (hasUser) {
            attributes.addFlashAttribute("email", email);
            return R_AUTH_NEWPASSWORD;
        } else {
            attributes.addFlashAttribute("message", "用戶名錯誤");
            return R_AUTH_PASSWORDRESET;
        }
    }

    @GetMapping("/newPassword")
    public String newPassword(Model model) {
        model.addAttribute("fragmentSystem", "auth");
        model.addAttribute("fragmentPackage", "newPassword");
        model.addAttribute("fragmentName", "view");
        return AUTH_INDEX;
    }

    @PostMapping("/newPassword")
    public String newPassword(@RequestParam String email, @RequestParam String password, Model model,
                                HttpSession session, RedirectAttributes attributes) {
        UpmsUser user = authService.resetPassword(email, password);
        if (user != null) {
            return R_AUTH_SIGNIN;
        } else {
            attributes.addFlashAttribute("message", "系統錯誤");
            return R_AUTH_NEWPASSWORD;
        }
    }

}

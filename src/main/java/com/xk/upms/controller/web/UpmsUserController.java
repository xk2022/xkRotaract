package com.xk.upms.controller.web;

import com.xk.common.base.BaseController;
import com.xk.upms.model.bo.UpmsUserSaveReq;
import com.xk.upms.model.po.UpmsUser;
import com.xk.upms.model.vo.UpmsUserSaveResp;
import com.xk.upms.service.*;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller for managing UpmsUser (account) in the UPMS system.
 * Provides endpoints for creating, updating, listing, and deleting UpmsUser entities,
 * as well as managing user roles, permissions, and organizations.
 *
 * @author yuan Created on 2022/06/10.
 * @author yuan Updated on 2024/10/25 with code optimization based on GPT recommendations.
 */
@Api(value = "用户管理")
@Controller
@RequestMapping("/admin/upms/manage/user")
public class UpmsUserController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpmsUserController.class);
    private static final String REDIRECT_URL = "redirect:/admin/upms/manage/user/";
    private static final int PAGE_SIZE = 8;

    @Autowired
    private UpmsUserService upmsUserService;
    @Autowired
    private UpmsOrganizationService upmsOrganizationService;
    @Autowired
    private UpmsUserOrganizationService upmsUserOrganizationService;
    @Autowired
    private UpmsRoleService upmsRoleService;
    @Autowired
    private UpmsUserRoleService upmsUserRoleService;
    @Autowired
    private UpmsUserPermissionService upmsUserPermissionService;

    /**
     * 查詢 用户 首頁
     *
     * Displays the user management homepage.
     * Retrieves a list of users and sets up the model attributes for rendering the user list view.
     *
     * @param model the model to which attributes are added for rendering the view
     * @return the path to the admin index page
     */
    @GetMapping
    public String index(Model model) {
        // 記錄當前頁面的基本資訊
        this.info(model, this.getClass().getAnnotation(RequestMapping.class).value()[0]);
        // 設置分片名稱為 "list" 以在頁面中顯示對應的模板
        model.addAttribute("fragmentName", "list");

        // 加入用戶列表和一個新的 UpmsUser 實體到模型中
        model.addAttribute("page_list", upmsUserService.list(null));
        model.addAttribute("entity", new UpmsUser());
        model.addAttribute("select_roles", upmsRoleService.list());
        // 返回管理員首頁模板路徑
        return ADMIN_INDEX;
    }

    /**
     * 查詢 用戶 明細頁
     *
     * Displays the detail page of a specific user.
     * Retrieves user information, roles, and user details by the provided user ID,
     * and sets up the model attributes for rendering the user detail view.
     *
     * @param id the ID of the user whose details are being retrieved
     * @param model the model to which attributes are added for rendering the view
     * @return the path to the admin index page
     */
    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        this.info(model, this.getClass().getAnnotation(RequestMapping.class).value()[0] + "/detail");
        model.addAttribute("fragmentName", "detail");

        // 將所有角色列表加入模型，以便在視圖中使用
        model.addAttribute("role_list", upmsRoleService.list());
        // 根據用戶 ID 獲取詳細的用戶信息並加入模型
        model.addAttribute("info", upmsUserService.selectDeatilById(id));
        // 根據用戶 ID 獲取主要的用戶實體並加入模型
        model.addAttribute("entity", upmsUserService.selectByPrimaryKey(id));
        // 返回管理員首頁模板路徑
        return ADMIN_INDEX;
    }

    /**
     * 新增/修改 用户 Create/Update
     *
     * Handles the creation or update of a user.
     * If the ID is null, a new user is created; otherwise, the existing user is updated.
     * The password field is not updated directly through this method;
     * for password changes, use AuthorizationController.passwordReset().
     *
     * @param resources the user information (UpmsUserSaveReq) to create or update
     * @param attributes used to pass flash attributes (messages) between redirects
     * @return the redirect address after the operation
     * @throws Exception if any error occurs during the create or update operation
     */
    @PostMapping("/save")
    public String saveOrUpdate(UpmsUserSaveReq resources, RedirectAttributes attributes) {
        UpmsUserSaveResp result;

        if (resources.getId() == null) {
            result = upmsUserService.create(resources);
            LOGGER.info("Created new account, ID: {}", result.getId());
        } else {
            // 不允许直接修改密码，需使用其他特定方法進行
            resources.setPassword(null);

            // 更新用户
            result = upmsUserService.update(resources.getId(), resources);
            LOGGER.info("Updated account, ID: {}", resources.getId());
        }

        // 根據操作結果設置提示訊息
        attributes.addFlashAttribute("message", (result == null) ? "操作失敗" : "操作成功");
        return REDIRECT_URL;
    }


    /**
     * 刪除 用户 Delete
     */
    @GetMapping("/delete/{ids}")
    public String delete(@PathVariable("ids") String ids) {
        upmsUserService.deleteByPrimaryKeys(ids);
        return REDIRECT_URL;
    }

    /**
     * 用戶組織
     */
//    @RequiresPermissions("upms:user:organization")
    @GetMapping("/organization/{ids}")
    public String organization(@PathVariable("id") long id, Model model) {
        // 所有組織
        model.addAttribute("upmsOrganizations", upmsOrganizationService.list(null));
        // 用戶擁有組織
        model.addAttribute("upmsUserOrganizations", upmsUserOrganizationService.selectByUserId(id));
        return "/manage/user/organization.jsp";
    }

    //    @RequiresPermissions("upms:user:organization")
    @PostMapping("/organization/{id}")
    public Object organization(@PathVariable("id") long id, HttpServletRequest request) {
        String[] organizationIds = request.getParameterValues("organizationId");
        upmsUserOrganizationService.organization(organizationIds, id);
//        return new UpmsResult(UpmsResultConstant.SUCCESS, "");
        return REDIRECT_URL;
    }

    /**
     * 用户角色
     */
//    @RequiresPermissions("upms:user:role")
    @GetMapping("/role/{name}")
    public String role(@PathVariable("code") String code, Model model) {
        // 所有角色
        model.addAttribute("upmsRoles", upmsRoleService.list());
        // 用戶擁有角色
        model.addAttribute("upmsUserRoles", upmsRoleService.selectByCode(code));
        return "/manage/user/role.jsp";
    }

    //    @RequiresPermissions("upms:user:role")
    @PostMapping("/role/{id}")
    public Object role(@PathVariable("id") int id, HttpServletRequest request) {
        String[] roleIds = request.getParameterValues("user_role");
        upmsUserRoleService.role((long) id, roleIds);
//        return new UpmsResult(UpmsResultConstant.SUCCESS, "");
        return REDIRECT_URL + id;
    }

    /**
     * 用户權限
     */
//    @RequiresPermissions("upms:user:permission")
    @GetMapping("/permission/{id}")
    public String permission(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", upmsUserService.selectByPrimaryKey(id));
        return "/manage/user/permission.jsp";
    }

    //    @RequiresPermissions("upms:user:permission")
    @PostMapping("/permission/{id}")
    public Object permission(@PathVariable("id") long id, HttpServletRequest request) {
//        JSONArray datas = JSONArray.parseArray(request.getParameter("datas"));
        upmsUserPermissionService.permission(null, id);
//        return new UpmsResult(UpmsResultConstant.SUCCESS, datas.size());
        return REDIRECT_URL;
    }

}




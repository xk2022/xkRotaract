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
import javax.servlet.http.HttpSession;

/**
 * 用户 Controller
 * Created by yuan on 2022/06/10
 */
@Api(value = "用户管理")
@Controller
@RequestMapping("/admin/upms/manage/user")
public class UpmsUserController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpmsUserController.class);

    //    private static final String DIR_INDEX = "truck/apps/user-management/permissions/";
//    private static final String INDEX = DIR_INDEX + "permission";
//    private static final String COMPILER = DIR_INDEX + "permission_add";
    private static final String REDIRECT_ADDR = "redirect:/admin/upms/manage/user";
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
     */
    @GetMapping()
    public String index(Model model) {
        this.info(model, this.getClass().getAnnotation(RequestMapping.class).value()[0]);
        model.addAttribute("fragmentName", "list");

        model.addAttribute("page_list", upmsUserService.list(null));
        model.addAttribute("entity", new UpmsUser());
        return ADMIN_INDEX;
    }

    /**
     * 查詢 用戶 明細頁
     */
    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        this.info(model, this.getClass().getAnnotation(RequestMapping.class).value()[0]);
        model.addAttribute("fragmentSystem", "upms");
        model.addAttribute("fragmentPackage", "user/detail");
        model.addAttribute("fragmentName", "detail");

//        model.addAttribute("page_list", upmsUserService.list(null));
        model.addAttribute("info", upmsUserService.selectDeatilById(id));
        model.addAttribute("entity", upmsUserService.selectByPrimaryKey(id));
        return ADMIN_INDEX;
    }

    /**
     * 新增/修改 用户 Create/Update
     */
//    @RequiresPermissions("upms:user:create")
//    @RequiresPermissions("upms:user:update")
    @PostMapping("/save")
    public String post(UpmsUserSaveReq resources, RedirectAttributes attributes, HttpSession session) {
        UpmsUserSaveResp result;
        if (resources.getId() == null) {
            result = upmsUserService.create(resources);
            LOGGER.info("新增用户，主键：userId={}", result.getId());
        } else {
            // 不允许直接改密码
            resources.setPassword(null);

            result = upmsUserService.update(resources.getId(), resources);
        }

        if (result == null) {
            attributes.addFlashAttribute("message", "操作失敗");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return REDIRECT_ADDR;
    }

    /**
     * 刪除 用户 Delete
     */
//    @RequiresPermissions("upms:user:delete")
    @GetMapping("/delete/{ids}")
    public String delete(@PathVariable("ids") String ids, RedirectAttributes attributes) {
        upmsUserService.deleteByPrimaryKeys(ids);
        return REDIRECT_ADDR;
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
        return REDIRECT_ADDR;
    }

    /**
     * 用户角色
     */
//    @RequiresPermissions("upms:user:role")
    @GetMapping("/role/{name}")
    public String role(@PathVariable("code") String code, Model model) {
        // 所有角色
        model.addAttribute("upmsRoles", upmsRoleService.list(null));
        // 用戶擁有角色
        model.addAttribute("upmsUserRoles", upmsRoleService.selectByCode(code));
        return "/manage/user/role.jsp";
    }

    //    @RequiresPermissions("upms:user:role")
    @PostMapping("/role/{id}")
    public Object role(@PathVariable("id") int id, HttpServletRequest request) {
        String[] roleIds = request.getParameterValues("roleId");
        upmsUserRoleService.role((long) id, roleIds);
//        return new UpmsResult(UpmsResultConstant.SUCCESS, "");
        return REDIRECT_ADDR;
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
        return REDIRECT_ADDR;
    }


}




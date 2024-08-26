package com.xk.upms.controller.web;

import com.xk.common.base.BaseController;
import com.xk.upms.model.bo.UpmsPermissionReq;
import com.xk.upms.model.bo.UpmsPermissionSaveReq;
import com.xk.upms.model.po.UpmsPermission;
import com.xk.upms.model.vo.UpmsPermissionSaveResp;
import com.xk.upms.service.UpmsPermissionService;
import com.xk.upms.service.UpmsSystemService;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 權限 Controller
 * Created by yuan on 2022/06/24
 */
@Api(value = "權限管理")
@Controller
@RequestMapping("/admin/upms/manage/permission")
public class UpmsPermissionController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpmsPermissionController.class);

    private static final String REDIRECT_ADDR = "redirect:/admin/upms/manage/permission";
    private static final int PAGE_SIZE = 8;

    @Autowired
    private UpmsPermissionService upmsPermissionService;
    @Autowired
    private UpmsSystemService upmsSystemService;

    /**
     * 查詢 權限 首頁
     */
    @GetMapping()
    public String index(UpmsPermissionReq resources, Model model) {
        this.info(model, this.getClass().getAnnotation(RequestMapping.class).value()[0]);
        model.addAttribute("fragmentName", "list");

        model.addAttribute("page_list"
                , upmsPermissionService.buildTree(upmsPermissionService.listBy(resources)));
        model.addAttribute("entity", new UpmsPermission());

        model.addAttribute("select_system", upmsSystemService.list());
        model.addAttribute("selected_system", resources.getSystemId());
        model.addAttribute("select_permission", upmsPermissionService.list());
        return ADMIN_INDEX;
    }
    public String index() {
        return "/manage/permission/index.jsp";
    }

    /**
     * 新增/修改 權限 Create/Update
     */
//    @RequiresPermissions("upms:permission:create")
//    @RequiresPermissions("upms:role:update")
    @PostMapping("/save")
    public String post(UpmsPermissionSaveReq resources, RedirectAttributes attributes, HttpSession session) {
        UpmsPermissionSaveResp result;
        if (resources.getId() == null) {
            result = upmsPermissionService.create(resources);
        } else {
            result = upmsPermissionService.update(resources.getId(), resources);
        }

        if (result == null) {

            attributes.addFlashAttribute("message", "操作失敗");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return REDIRECT_ADDR;
    }

    /**
     * 刪除 權限 Delete
     */
//    @RequiresPermissions("upms:role:delete")
    @GetMapping("/delete/{ids}")
    public String delete(@PathVariable("ids") String ids, RedirectAttributes attributes) {
        upmsPermissionService.deleteByPrimaryKeys(ids);
        attributes.addFlashAttribute("message", "刪除成功");
        return REDIRECT_ADDR;
    }

    /**
     * 角色權限列表
     */
//    @RequiresPermissions("upms:permission:read")
    @GetMapping("/role/{id}")
    public Object role(@PathVariable("id") long id, Model model) {
        model.addAttribute("role", upmsPermissionService.getTreeByRoleId(id));
        return "/manage/role/permission.jsp";
    }

    /**
     * 用戶權限列表
     */
//    @RequiresPermissions("upms:permission:read")
    @PostMapping("/user/{id}")
    public Object user(@PathVariable("id") long id, HttpServletRequest request) {
        return upmsPermissionService.getTreeByUserId(id, NumberUtils.toByte(request.getParameter("type")));
    }

}
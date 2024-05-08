package com.xk.upms.controller.web;

import com.xk.common.base.BaseController;
import com.xk.upms.model.bo.UpmsRoleSaveReq;
import com.xk.upms.model.vo.UpmsRoleSaveResp;
import com.xk.upms.service.UpmsPermissionService;
import com.xk.upms.service.UpmsRolePermissionService;
import com.xk.upms.service.UpmsRoleService;
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
 * 角色 Controller
 * Created by yuan on 2022/06/24
 */
@Api(value = "UPMS管理")
@Controller
@RequestMapping("/admin/upms/manage")
public class UpmsController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpmsController.class);

    //    private static final String DIR_INDEX = "truck/apps/user-management/permissions/";
//    private static final String INDEX = DIR_INDEX + "permission";
//    private static final String COMPILER = DIR_INDEX + "permission_add";
    private static final String REDIRECT_ADDR = "redirect:/admin/upms/manage/role";
    private static final int PAGE_SIZE = 8;

    @Autowired
    private UpmsRoleService upmsRoleService;
    @Autowired
    private UpmsPermissionService upmsPermissionService;
    @Autowired
    private UpmsRolePermissionService upmsRolePermissionService;

    /**
     * 查詢 角色 首頁
     */
//    @RequiresPermissions("upms:role:read")
    @GetMapping()
    public String index(Model model) {
//        model.addAttribute("fragmentSystem", "upms");
//        model.addAttribute("fragmentPackage", "/manage");
        this.info(model, this.getClass().getAnnotation(RequestMapping.class).value()[0]);

//        model.addAttribute("page_list", upmsRoleService.list(null));
//        model.addAttribute("entity", new UpmsRole());
//        model.addAttribute("checkbox_menus", upmsPermissionService.buildTree(upmsPermissionService.findAllMenuLevel()));
        return ADMIN_INDEX;
    }

    /**
     * 新增/修改 角色 Create/Update
     */
//    @RequiresPermissions("upms:role:create")
//    @RequiresPermissions("upms:role:update")
    @PostMapping("/save")
    public String post(UpmsRoleSaveReq resources, RedirectAttributes attributes, HttpSession session) {
        UpmsRoleSaveResp result;
        if (resources.getId() == null) {
            result = upmsRoleService.create(resources);
        } else {
            result = upmsRoleService.update(resources.getId(), resources);
        }

        if (result == null) {
            attributes.addFlashAttribute("message", "操作失敗");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return REDIRECT_ADDR;
    }

    /**
     * 刪除 角色 Delete
     */
//    @RequiresPermissions("upms:role:delete")
    @GetMapping("/delete/{ids}")
    public String delete(@PathVariable("ids") String ids, RedirectAttributes attributes) {
        upmsRoleService.deleteByPrimaryKeys(ids);
        attributes.addFlashAttribute("message", "刪除成功");
        return REDIRECT_ADDR;
    }

    /**
     * 角色權限
     */
//    @RequiresPermissions("upms:role:permission")
    @GetMapping("/permission/{id}")
    public String permission(@PathVariable("id") long id, Model model) {
        model.addAttribute("role", upmsRoleService.selectByPrimaryKey(id));
        return "/manage/role/permission.jsp";
    }

//    @RequiresPermissions("upms:role:permission")
    @PostMapping("/permission/{id}")
    public Object permission(@PathVariable("id") long id, HttpServletRequest request) {
//        JSONArray datas = JSONArray.parseArray(request.getParameter("datas"));
        int result = upmsRolePermissionService.rolePermission(null, id);
//        return new UpmsResult(UpmsResultConstant.SUCCESS, result);
        return REDIRECT_ADDR;
    }

}
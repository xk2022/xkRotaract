package com.xk.upms.controller.web;

import com.xk.common.base.BaseController;
import com.xk.upms.model.bo.UpmsOrganizationSaveReq;
import com.xk.upms.model.po.UpmsRole;
import com.xk.upms.model.vo.UpmsOrganizationSaveResp;
import com.xk.upms.service.UpmsOrganizationService;
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

import javax.servlet.http.HttpSession;

/**
 * 組織 Controller
 * Created by yuan on 2022/06/24
 */
@Api(value = "組織管理")
@Controller
@RequestMapping("/admin/upms/manage/organization")
public class UpmsOrganizationController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpmsOrganizationController.class);

    private static final String REDIRECT_ADDR = "redirect:/admin/upms/manage/organization";
    private static final int PAGE_SIZE = 8;

    @Autowired
    private UpmsOrganizationService upmsOrganizationService;

    /**
     * 查詢 組織 首頁
     */
    @GetMapping()
    public String index(Model model) {
        this.info(model, this.getClass().getAnnotation(RequestMapping.class).value()[0]);
        model.addAttribute("fragmentName", "list");

        model.addAttribute("page_list", upmsOrganizationService.list(null));
        model.addAttribute("entity", new UpmsRole());
        return ADMIN_INDEX;
    }

    /**
     * 新增/修改 組織 Create/Update
     */
//    @RequiresPermissions("upms:organization:create")
//    @RequiresPermissions("upms:organization:update")
    @PostMapping("/save")
    public String post(UpmsOrganizationSaveReq resources, RedirectAttributes attributes, HttpSession session) {
        UpmsOrganizationSaveResp result;
        if (resources.getId() == null) {
            result = upmsOrganizationService.create(resources);
        } else {
            result = upmsOrganizationService.update(resources.getId(), resources);
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
        upmsOrganizationService.deleteByPrimaryKeys(ids);
        attributes.addFlashAttribute("message", "刪除成功");
        return REDIRECT_ADDR;
    }

}
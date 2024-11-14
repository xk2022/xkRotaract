package com.xk.cms.controller.web;

import com.xk.admin.model.dto.UserExample;
import com.xk.cms.model.bo.CmsUserReq;
import com.xk.cms.model.bo.CmsUserSaveReq;
import com.xk.cms.model.vo.CmsUserSaveResp;
import com.xk.cms.service.CmsUserService;
import com.xk.common.base.BaseController;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * 用户 Controller for managing CmsUser (user_info) in the CMS system.
 * Provides endpoints for creating, updating, listing, and deleting UpmsUser entities,
 * as well as managing user roles, permissions, and organizations.
 *
 * @author yuan Created on 2024/02/21.
 * @author yuan Updated on 2024/11/13.
 */
@Api(value = "用户管理")
@Controller
@RequestMapping("/admin/cms/manage/user")
public class CmsUserController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsUserController.class);
    private static final String REDIRECT_URL = "redirect:/admin/cms/manage/user";

    @Autowired
    private CmsUserService cmsUserService;

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
        model.addAttribute("page_list", cmsUserService.list(new CmsUserReq()));
        model.addAttribute("entity", new CmsUserSaveReq());
        // 返回管理員首頁模板路徑
        return ADMIN_INDEX;
    }

    @GetMapping("/district")
    public String indexDistrict(Model model, HttpSession session) {
        // 設置當前頁面信息
        this.info(model, this.getClass().getAnnotation(RequestMapping.class).value()[0] + "/club");
        // 設定頁面片段信息
        model.addAttribute("fragmentPackage", "user");
        model.addAttribute("fragmentName", "list");
        model.addAttribute("access_scope", "district");

        // 獲取當前使用者
        UserExample user = (UserExample) session.getAttribute("user");
        // 檢查使用者所屬社團ID是否有效
        if (StringUtils.isBlank(user.getDistrict_id()) || "0".equals(user.getDistrict_id())) {
            return this.errorMsg(model, "查無所屬地區", "請先至“我的資料”，選擇您的所屬地區！");
        }
        // 將使用者的地區ID和所屬社團ID添加到模型中
        model.addAttribute("district_id", user.getDistrict_id());
        model.addAttribute("rotaract_id", user.getRotaract_id());

        // 添加用戶列表和新建的 CmsUserSaveReq 實體到模型中
        CmsUserReq req = new CmsUserReq();
        req.setDistrict_id(user.getDistrict_id());
        model.addAttribute("page_list", cmsUserService.list(req));
        model.addAttribute("entity", new CmsUserSaveReq());
        // 返回頁面模板路徑
        return ADMIN_INDEX;
    }

    @GetMapping("/club")
    public String indexClub(Model model, HttpSession session) {
        // 設置當前頁面信息
        this.info(model, this.getClass().getAnnotation(RequestMapping.class).value()[0] + "/club");
        // 設定頁面片段信息
        model.addAttribute("fragmentPackage", "user");
        model.addAttribute("fragmentName", "list");
        model.addAttribute("access_scope", "club");

        // 獲取當前使用者
        UserExample user = (UserExample) session.getAttribute("user");
        // 檢查使用者所屬社團ID是否有效
        if (StringUtils.isBlank(user.getRotaract_id()) || "0".equals(user.getRotaract_id())) {
            return this.errorMsg(model, "查無所屬社", "請先至“我的資料”，選擇您的所屬社！");
        }
        // 將使用者的地區ID和所屬社團ID添加到模型中
        model.addAttribute("district_id", user.getDistrict_id());
        model.addAttribute("rotaract_id", user.getRotaract_id());

        // 添加用戶列表和新建的 CmsUserSaveReq 實體到模型中
        CmsUserReq req = new CmsUserReq();
        req.setRotaract_id(user.getRotaract_id());
        model.addAttribute("page_list", cmsUserService.list(req));
        model.addAttribute("entity", new CmsUserSaveReq());
        // 返回頁面模板路徑
        return ADMIN_INDEX;
    }

    /**
     * 新增/修改 用户 Create/Update
     */
    @PostMapping("/save")
    public String saveOrUpdate(CmsUserSaveReq resources, RedirectAttributes attributes) {
        CmsUserSaveResp result;

        if (resources.getId() == null) {
            result = cmsUserService.create(resources);
            LOGGER.info("新增用户，主键：userId={}", result.getId());
        } else {
            result = cmsUserService.update(resources.getId(), resources);
        }
        // 根據操作結果設置提示訊息
        attributes.addFlashAttribute("message", (result == null) ? "操作失敗" : "操作成功");
        return REDIRECT_URL;
    }


    /**
     * 新增/修改 用户 Create/Update
     */
    @PostMapping("/save/self")
    public String self(CmsUserSaveReq resources, RedirectAttributes attributes) {
        CmsUserSaveResp result;

        if (resources.getId() == null) {
            result = cmsUserService.create(resources);
            LOGGER.info("新增用户，主键：userId={}", result.getId());
        } else {
            result = cmsUserService.update(resources.getId(), resources);
        }

        // 根據操作結果設置提示訊息
        attributes.addFlashAttribute("message", (result == null) ? "操作失敗" : "操作成功");
        return "redirect:/admin/cms/manage/self";
    }

}




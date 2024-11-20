package com.xk.cms.controller.web;

import com.xk.admin.model.dto.UserExample;
import com.xk.cms.model.bo.CmsClubSaveReq;
import com.xk.cms.model.vo.CmsClubSaveResp;
import com.xk.cms.service.CmsClubService;
import com.xk.common.base.BaseController;
import com.xk.common.util.XkTypeUtils;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
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
 * Controller for managing clubs in the CMS system.
 * Provides endpoints for creating, updating, listing, and deleting club entities.
 *
 * @author yuan Created on 2024/09/18.
 */
@Api(value = "Club Management")
@Controller
@RequestMapping("/admin/cms/manage/club")
public class CmsClubController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsClubController.class);
    private static final String REDIRECT_URL = "redirect:/admin/cms/manage/club";

    @Autowired
    private CmsClubService cmsClubService;

    /**
     * Displays the homepage with a list of clubs.
     *
     * @param model the model object to pass attributes to the view
     * @return the view name of the admin index page
     */
    @GetMapping()
    public String index(Model model, HttpSession session) {
        this.info(model, this.getClass().getAnnotation(RequestMapping.class).value()[0]);
        model.addAttribute("fragmentName", "dashboard");

        UserExample user = (UserExample) session.getAttribute("user");
        if (StringUtils.isBlank(user.getDistrict_id()) || "0".equals(user.getDistrict_id())) {
            return this.errorMsg(model, "查無所屬地區", "請先至“我的資料”，選擇您的所屬地區！");
        }
        model.addAttribute("page_list", cmsClubService.getOne(user.getRotaract_id()));
        model.addAttribute("entity", new CmsClubSaveReq());
        return ADMIN_INDEX;
    }

    /**
     * 新增/修改 社團 Create/Update
     */
    @PostMapping("/save")
    public String post(CmsClubSaveReq resources, RedirectAttributes attributes) {
        CmsClubSaveResp result;

        if (StringUtils.isBlank(resources.getId())) {
            result = cmsClubService.create(resources);
            LOGGER.info("Created new Club, ID: {}", result.getId());
        } else {
            Long id = XkTypeUtils.parseLongOrNull(resources.getId());
            result = cmsClubService.update(id, resources);
            LOGGER.info("Updated Club, ID: {}", resources.getId());
        }

        attributes.addFlashAttribute("message", (result == null) ? "操作失敗" : "操作成功");
        return REDIRECT_URL;
    }

    /**
     * 刪除 系統 Delete
     */
    @GetMapping("/delete/{ids}")
    public String delete(@PathVariable("ids") String ids, RedirectAttributes attributes) {
        cmsClubService.deleteByPrimaryKeys(ids);
        attributes.addFlashAttribute("message", "刪除成功");
        return REDIRECT_URL;
    }

}




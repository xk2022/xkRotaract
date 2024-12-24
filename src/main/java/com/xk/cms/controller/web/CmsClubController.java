package com.xk.cms.controller.web;

import com.xk.admin.model.dto.UserExample;
import com.xk.cms.model.bo.CmsClubSaveReq;
import com.xk.cms.model.dto.CmsClubInfoOverview;
import com.xk.cms.model.vo.CmsClubSaveResp;
import com.xk.cms.service.CmsClubInfoService;
import com.xk.cms.service.CmsClubService;
import com.xk.common.base.BaseController;
import com.xk.common.util.XkTypeUtils;
import com.xk.upms.model.vo.UpmsOrganizationResp;
import com.xk.upms.service.UpmsOrganizationService;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;

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
    @Autowired
    private UpmsOrganizationService upmsOrganizationService;
    @Autowired
    private CmsClubInfoService cmsClubInfoService;

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
        // 檢查使用者所屬社團ID是否有效
        if (StringUtils.isBlank(user.getRotaract_id()) || "0".equals(user.getRotaract_id())) {
            return this.errorMsg(model, "查無所屬社", "請先至“我的資料”，選擇您的所屬社！");
        }


        UpmsOrganizationResp parentOrg = upmsOrganizationService.getParentOrganization(Long.valueOf(user.getRotaract_id()));
        model.addAttribute("cmsClubInfo", cmsClubInfoService.getOne(user.getRotaract_id(), parentOrg));
        model.addAttribute("infoOverviewReq", new CmsClubInfoOverview());
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
     * 新增/修改 社團 Create/Update
     */
    @PostMapping("/saveOverview")
    public String saveOverview(CmsClubInfoOverview resources, RedirectAttributes attributes) {
        Boolean result;
        result = cmsClubInfoService.saveOverview(resources);

        attributes.addFlashAttribute("message", result ? "操作失敗" : "操作成功");
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

    @PostMapping("/upload-image/{id}")
    public String uploadImageToDatabase(@PathVariable("id") String id, @RequestParam("file") MultipartFile file, RedirectAttributes attributes) throws IOException {
        if (file.isEmpty()) {
            attributes.addFlashAttribute("message", "文件为空，请重新上传！");
            return REDIRECT_URL; // 返回上传页面
        }
        cmsClubService.uploadedImage(id, file.getBytes());
        attributes.addFlashAttribute("message", "图片上传成功！");
        return REDIRECT_URL;
    }

}




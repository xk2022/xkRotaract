package com.xk.cms.controller.web;

import com.xk.admin.model.dto.UserExample;
import com.xk.common.base.BaseController;
import com.xk.common.util.XkTypeUtils;
import com.xk.upms.model.bo.UpmsOrganizationReq;
import com.xk.upms.model.bo.UpmsOrganizationSaveReq;
import com.xk.upms.model.vo.UpmsOrganizationSaveResp;
import com.xk.upms.service.UpmsOrganizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
 * Controller for managing organization in the CMS system.
 * Provides endpoints for creating, updating, listing, and deleting organization entities.
 *
 * @author yuan Created on 2024/11/04.
 */
@Api(value = "District Management")
@Controller
@RequestMapping("/admin/cms/manage/organization")
public class CmsOrganizationController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsOrganizationController.class);
    private static final String REDIRECT_URL = "redirect:/admin/cms/manage/organization";

    @Autowired
    private UpmsOrganizationService upmsOrganizationService;

    @ApiOperation(value = "組織管理首頁District", notes = "顯示組織列表District")
    @GetMapping("/district")
    public String indexDistrict(Model model, HttpSession session) {
        this.info(model, this.getClass().getAnnotation(RequestMapping.class).value()[0]+"/district");
        model.addAttribute("fragmentPackage", "organization");
        model.addAttribute("fragmentName", "list");

        UserExample user = (UserExample) session.getAttribute("user");
        if (StringUtils.isBlank(user.getDistrict_id()) || "0".equals(user.getDistrict_id())) {
            return this.errorMsg(model, "查無所屬地區", "請先至“我的資料”，選擇您的所屬地區！");
        }
        UpmsOrganizationReq req = new UpmsOrganizationReq();
        req.setParentId(user.getDistrict_id());
        model.addAttribute("page_list", upmsOrganizationService.list(req));
        model.addAttribute("entity", new UpmsOrganizationSaveReq());

        UpmsOrganizationReq reqSelect = new UpmsOrganizationReq();
        reqSelect.setParentCode("RIT");
        reqSelect.setLevel("2");
        model.addAttribute("select_organization", upmsOrganizationService.list(reqSelect));
        return ADMIN_INDEX;
    }

    /**
     * 新增/修改 組織 Create/Update
     *
     * Handles the creation or update of a organization.
     *
     * @param resources the information (UpmsOrganizationSaveReq) to create or update
     * @param attributes used to pass flash attributes (messages) between redirects
     * @return the redirect address after the operation
     * @throws Exception if any error occurs during the create or update operation
     */
    @ApiOperation(value = "新增或修改組織", notes = "處理組織的創建或更新")
    @PostMapping("/save")
    public String saveOrUpdate(UpmsOrganizationSaveReq resources, RedirectAttributes attributes) {
        UpmsOrganizationSaveResp result;

        if (StringUtils.isBlank(resources.getId())) {
            result = upmsOrganizationService.create(resources);
            LOGGER.info("Created new Organization, ID: {}", result.getId());
        } else {
            Long id = XkTypeUtils.parseLongOrNull(resources.getId());
            result = upmsOrganizationService.update(id, resources);
            LOGGER.info("Updated Organization, ID: {}", resources.getId());
        }

        attributes.addFlashAttribute("message", (result == null) ? "操作失敗" : "操作成功");
        return REDIRECT_URL+"/district";
    }

}

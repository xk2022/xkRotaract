package com.xk.upms.controller.web;

import com.xk.common.base.BaseController;
import com.xk.common.util.XkTypeUtils;
import com.xk.upms.model.bo.UpmsOrganizationSaveReq;
import com.xk.upms.model.po.UpmsOrganization;
import com.xk.upms.model.vo.UpmsOrganizationSaveResp;
import com.xk.upms.service.UpmsOrganizationService;
import com.xk.upms.service.UpmsUserOrganizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

import javax.validation.constraints.NotNull;

/**
 * Controller for managing UpmsOrganization in the UPMS system.
 * Provides endpoints for creating, updating, listing, and deleting UpmsOrganization entities,
 * as well as managing users, roles, permissions.
 *
 * @author yuan Created on 2022/06/24.
 * @author yuan Updated on 2022/08/28 init logic of organization.
 * @author yuan Updated on 2024/10/28 with code optimization based on GPT recommendations.
 */
@Api(value = "District Management")
@Controller
@RequestMapping("/admin/upms/manage/organization")
public class UpmsOrganizationController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpmsOrganizationController.class);
    private static final String REDIRECT_URL = "redirect:/admin/upms/manage/organization";

    @Autowired
    private UpmsOrganizationService upmsOrganizationService;
    @Autowired
    private UpmsUserOrganizationService upmsUserOrganizationService;

    /**
     * 查詢 組織 首頁
     *
     * Displays the organization management homepage.
     * Retrieves a list of organizations and sets up the model attributes for rendering the user list view.
     *
     * @param model the model to which attributes are added for rendering the view
     * @return the path to the admin index page
     */
    @ApiOperation(value = "組織管理首頁", notes = "顯示組織列表")
    @GetMapping()
    public String index(Model model) {
        this.info(model, this.getClass().getAnnotation(RequestMapping.class).value()[0]);
        model.addAttribute("fragmentName", "list");

//        model.addAttribute("page_list", upmsOrganizationService.list(null));
        model.addAttribute("page_list", upmsOrganizationService.buildTree(upmsOrganizationService.list(null)));
        model.addAttribute("entity", new UpmsOrganizationSaveReq());
        model.addAttribute("select_organization", upmsOrganizationService.getOrganizationsExcludingLevel3());
        return ADMIN_INDEX;
    }

    /**
     * 查詢 組織 明細頁
     *
     * Displays the detail page of a specific organization.
     *
     * @param id the ID of the organization whose details are being retrieved
     * @param model the model to which attributes are added for rendering the view
     * @return the path to the admin index page
     */
    @ApiOperation(value = "組織詳細頁", notes = "顯示特定組織的詳細信息")
    @GetMapping("/{id}")
    public String detail(@ApiParam(value = "組織ID", required = true) @PathVariable @NotNull Long id, Model model) {
        this.info(model, this.getClass().getAnnotation(RequestMapping.class).value()[0]+"/detail");
        model.addAttribute("fragmentName", "detail");

        model.addAttribute("organization", upmsOrganizationService.findById(id));
        model.addAttribute("page_list", upmsUserOrganizationService.getUsers(id));

        model.addAttribute("entity", new UpmsOrganization());
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
        return REDIRECT_URL;
    }

    /**
     * 刪除 組織 Delete
     *
     * @param ids the IDs of the organizations to be deleted
     * @param attributes used to pass flash attributes (messages) between redirects
     * @return the redirect address after deletion
     */
    @ApiOperation(value = "刪除組織", notes = "根據ID刪除組織")
    @GetMapping("/delete/{ids}")
    public String delete(@ApiParam(value = "組織ID", required = true) @PathVariable("ids") String ids, RedirectAttributes attributes) {
        try {
            upmsOrganizationService.deleteByPrimaryKeys(ids);
            attributes.addFlashAttribute("message", "刪除成功");
        } catch (Exception ex) {
            LOGGER.error("Error deleting organization with IDs: {}", ids, ex);
            attributes.addFlashAttribute("message", "刪除失敗");
        }
        return REDIRECT_URL;
    }

}
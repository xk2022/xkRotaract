package com.xk.adm.controller.web;

import com.xk.adm.model.bo.AdmModuleSaveReq;
import com.xk.adm.model.vo.AdmModuleSaveResp;
import com.xk.adm.service.AdmModuleService;
import com.xk.common.base.BaseController;
import com.xk.common.util.XkTypeUtils;
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
 * Controller for managing AdmModule in the ADM system.
 * Provides endpoints for creating, updating, listing, and deleting AdmModule entities.
 *
 * @author yuan Created on 2024/12/05.
 */
@Api(value = "Module Management")
@Controller
@RequestMapping("/admin/adm/manage/module")
public class AdmModuleController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdmModuleController.class);
    private static final String REDIRECT_URL = "redirect:/admin/adm/manage/module";

    @Autowired
    private AdmModuleService admModuleService;

    /**
     * 模块管理首页
     * Displays the module management homepage with a list of all modules.
     */
    @ApiOperation(value = "模塊管理首頁", notes = "顯示模塊列表")
    @GetMapping
    public String index(Model model) {
        this.info(model, this.getClass().getAnnotation(RequestMapping.class).value()[0]);
        model.addAttribute("fragmentName", "list");

        model.addAttribute("page_list", admModuleService.list(null));
        model.addAttribute("entity", new AdmModuleSaveReq());
        return ADMIN_INDEX;
    }

    /**
     * 模块详情页
     * Displays the detail page of a specific module.
     */
    @ApiOperation(value = "模块详情页", notes = "显示特定模块的详细信息")
    @GetMapping("/{id}")
    public String detail(@ApiParam(value = "模块ID", required = true) @PathVariable @NotNull Long id, Model model) {
        this.info(model, this.getClass().getAnnotation(RequestMapping.class).value()[0] + "/detail");
        model.addAttribute("fragmentName", "detail");

        model.addAttribute("module", admModuleService.findById(id));
        model.addAttribute("entity", new AdmModuleSaveReq());
        return ADMIN_INDEX;
    }

    /**
     * 新增/修改模块 Create/Update
     * Handles the creation or update of a module.
     */
    @ApiOperation(value = "新增或修改模块", notes = "处理模块的创建或更新")
    @PostMapping("/save")
    public String saveOrUpdate(AdmModuleSaveReq resources, RedirectAttributes attributes) {
        AdmModuleSaveResp result;

        if (StringUtils.isBlank(resources.getId())) {
            result = admModuleService.create(resources);
            LOGGER.info("Created new Module, ID: {}", result.getId());
        } else {
            Long id = XkTypeUtils.parseLongOrNull(resources.getId());
            result = admModuleService.update(id, resources);
            LOGGER.info("Updated Module, ID: {}", resources.getId());
        }
        attributes.addFlashAttribute("message", (result == null) ? "操作失败" : "操作成功");
        return REDIRECT_URL;
    }

    /**
     * 删除模块 Delete
     */
    @ApiOperation(value = "删除模块", notes = "根据ID删除模块")
    @GetMapping("/delete/{ids}")
    public String delete(@ApiParam(value = "模块ID", required = true) @PathVariable("ids") String ids, RedirectAttributes attributes) {
        try {
            admModuleService.deleteByPrimaryKeys(ids);
            attributes.addFlashAttribute("message", "删除成功");
        } catch (Exception ex) {
            LOGGER.error("Error deleting module with IDs: {}", ids, ex);
            attributes.addFlashAttribute("message", "删除失败");
        }
        return REDIRECT_URL;
    }

}

package com.xk.cms.controller.web;

import com.xk.cms.model.bo.CmsDistrictSaveReq;
import com.xk.cms.model.po.CmsDistrict;
import com.xk.cms.model.vo.CmsDistrictSaveResp;
import com.xk.cms.service.CmsDistrictService;
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

/**
 * Controller for managing districts in the CMS system.
 * Provides endpoints for creating, updating, listing, and deleting district entities.
 *
 * Created on 2024/10/24.
 *
 * @author yuan
 */
@Api(value = "District Management")
@Controller
@RequestMapping("/admin/cms/manage/district")
public class CmsDistrictController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsDistrictController.class);
    private static final String REDIRECT_URL = "redirect:/admin/cms/manage/district";

    @Autowired
    private CmsDistrictService cmsDistrictService;

    /**
     * Displays the homepage with a list of districts.
     *
     * @param model the model object to pass attributes to the view
     * @return the view name of the admin index page
     */
    @GetMapping
    public String index(Model model) {
        this.info(model, this.getClass().getAnnotation(RequestMapping.class).value()[0]);

        model.addAttribute("fragmentName", "list");
        model.addAttribute("page_list", cmsDistrictService.list());
        model.addAttribute("entity", new CmsDistrict());

        return ADMIN_INDEX;
    }

    /**
     * Creates or updates a club entity based on the given request data.
     *
     * @param resources the request object containing club information
     * @param attributes the attributes to pass flash messages to the redirect view
     * @return the redirect URL to the district management page
     */
    @PostMapping("/save")
    public String saveOrUpdate(CmsDistrictSaveReq resources, RedirectAttributes attributes) {
        CmsDistrictSaveResp result;

        if (StringUtils.isBlank(resources.getId())) {
            result = cmsDistrictService.create(resources);
            LOGGER.info("Created new district, ID: {}", result.getId());
        } else {
            Long id = XkTypeUtils.parseLongOrNull(resources.getId());
            result = cmsDistrictService.update(id, resources);
            LOGGER.info("Updated district, ID: {}", resources.getId());
        }

        // 根據操作結果設置提示訊息
        attributes.addFlashAttribute("message", (result == null) ? "操作失敗" : "操作成功");
        return REDIRECT_URL;
    }

    /**
     * Deletes the specified districts based on their IDs.
     *
     * @param ids the IDs of the districts to be deleted
     * @param attributes the attributes to pass flash messages to the redirect view
     * @return the redirect URL to the district management page
     */
    @GetMapping("/delete/{ids}")
    public String delete(@PathVariable("ids") String ids, RedirectAttributes attributes) {
        cmsDistrictService.deleteByPrimaryKeys(ids);
        attributes.addFlashAttribute("message", "刪除成功");
        return REDIRECT_URL;
    }

}

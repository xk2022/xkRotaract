package com.xk.cms.controller.web;

import com.xk.cms.model.bo.CmsCompanySaveReq;
import com.xk.cms.model.vo.CmsCompanySaveResp;
import com.xk.cms.service.CmsCompanyService;
import com.xk.common.base.BaseController;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * 公司管理 Controller
 * Created by yuan on 2024/04/24
 */
@Api(value = "公司管理")
@Controller
@RequestMapping("/admin/cms/manage/company")
public class CmsCompanyController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsCompanyController.class);

    private static final String REDIRECT_ADDR = "redirect:/admin/cms/manage/company";
    private static final int PAGE_SIZE = 8;

    @Autowired
    private CmsCompanyService cmsCompanyService;

    /**
     * 新增/修改 用户 Create/Update
     */
//    @RequiresPermissions("upms:user:create")
//    @RequiresPermissions("upms:user:update")
    @PostMapping("/save")
    public String post(CmsCompanySaveReq resources, RedirectAttributes attributes, HttpSession session) {
        CmsCompanySaveResp result;

        if (resources.getId() == null) {
            result = cmsCompanyService.create(resources);
            LOGGER.info("新增用户，主键：userId={}", result.getId());
        } else {
            result = cmsCompanyService.update(resources.getId(), resources);
        }

        if (result == null) {
            attributes.addFlashAttribute("message", "操作失敗");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return "redirect:/admin/cms/manage/self";
    }

}




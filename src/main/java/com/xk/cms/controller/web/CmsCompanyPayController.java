package com.xk.cms.controller.web;

import com.xk.cms.model.bo.CmsCompanyPaySaveReq;
import com.xk.cms.model.po.CmsCompanyPay;
import com.xk.cms.model.vo.CmsCompanySaveResp;
import com.xk.cms.service.CmsCompanyPayService;
import com.xk.common.base.BaseController;
import com.xk.common.util.GoogleApiGeocode;
import io.swagger.annotations.Api;
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
 * 公司管理-覆核 Controller
 * Created by yuan on 2024/08/22
 */
@Api(value = "公司覆核")
@Controller
@RequestMapping("/admin/cms/manage/applyCompany")
public class CmsCompanyPayController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsCompanyPayController.class);

    private static final String REDIRECT_ADDR = "redirect:/admin/cms/manage/applyCompany";
    private static final int PAGE_SIZE = 8;

    @Autowired
    private CmsCompanyPayService cmsCompanyPayService;

    /**
     * 查詢 系統 首頁
     */
    @GetMapping()
    public String index(Model model) {
        this.info(model, this.getClass().getAnnotation(RequestMapping.class).value()[0]);
        model.addAttribute("fragmentName", "list");

        model.addAttribute("page_list", cmsCompanyPayService.list());
        model.addAttribute("entity", new CmsCompanyPay());
        return ADMIN_INDEX;
    }

    /**
     * 新增/修改 公司 Create/Update
     */
    @PostMapping("/save")
    public String post(CmsCompanyPaySaveReq resources, RedirectAttributes attributes, HttpSession session) {
        CmsCompanySaveResp result;

        if (resources.getCmsCompanyPayId() == null) {
            result = cmsCompanyPayService.create(resources);
            LOGGER.info("新增用户，主键：userId={}", result.getId());
        } else {
            result = cmsCompanyPayService.update(resources.getCmsCompanyPayId(), resources);
        }

        if (result == null) {
            attributes.addFlashAttribute("message", "操作失敗");
        } else {
            // 打入
            GoogleApiGeocode.main("123");
            attributes.addFlashAttribute("message", "操作成功");
        }
        return REDIRECT_ADDR;
    }

}




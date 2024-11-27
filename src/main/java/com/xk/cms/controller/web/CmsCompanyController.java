package com.xk.cms.controller.web;

import com.xk.admin.model.dto.UserExample;
import com.xk.cms.model.bo.CmsCompanyReq;
import com.xk.cms.model.bo.CmsCompanySaveReq;
import com.xk.cms.model.vo.CmsCompanySaveResp;
import com.xk.cms.service.CmsCompanyService;
import com.xk.cms.service.CmsSelfService;
import com.xk.common.base.BaseController;
import com.xk.common.util.GoogleApiGeocode;
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
    @Autowired
    private CmsSelfService cmsSelfService;

    /**
     * 查詢 系統 首頁
     */
    @GetMapping()
    public String index(Model model) {
        this.info(model, this.getClass().getAnnotation(RequestMapping.class).value()[0]);
        model.addAttribute("fragmentName", "list");

        model.addAttribute("page_list", cmsCompanyService.list(null));
        model.addAttribute("entity", new CmsCompanySaveReq());
        model.addAttribute("chunkedIndustries", cmsSelfService.getChunkedIndustries());
        return ADMIN_INDEX;
    }

    @GetMapping("/club")
    public String indexClub(Model model, HttpSession session) {
        this.info(model, this.getClass().getAnnotation(RequestMapping.class).value()[0] + "/club");
        model.addAttribute("fragmentPackage", "company");
        model.addAttribute("fragmentName", "list");

        model.addAttribute("access_scope", "club");
        UserExample user = (UserExample) session.getAttribute("user");
        if (StringUtils.isBlank(user.getRotaract_id()) || "0".equals(user.getRotaract_id())) {
            return this.errorMsg(model, "查無所屬社", "請先至“我的資料”，選擇您的所屬社！");
        }
        model.addAttribute("district_id", user.getDistrict_id());
        model.addAttribute("rotaract_id", user.getRotaract_id());
        CmsCompanyReq req = new CmsCompanyReq();
        req.setRotaract_id(user.getRotaract_id());
        model.addAttribute("page_list", cmsCompanyService.list(req));
        model.addAttribute("entity", new CmsCompanySaveReq());
        model.addAttribute("chunkedIndustries", cmsSelfService.getChunkedIndustries());
        return ADMIN_INDEX;
    }

    /**
     * 新增/修改 公司 Create/Update
     */
    @PostMapping("/save")
    public String post(CmsCompanySaveReq resources, RedirectAttributes attributes) {
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
            // 打入
            GoogleApiGeocode.main("123");
            attributes.addFlashAttribute("message", "操作成功");
        }
        return "redirect:/admin/cms/manage/self";
    }


    /**
     * 刪除 系統 Delete
     */
    @GetMapping("/delete/{ids}")
    public String delete(@PathVariable("ids") String ids, RedirectAttributes attributes) {
        cmsCompanyService.deleteByPrimaryKeys(ids);
        attributes.addFlashAttribute("message", "刪除成功");
        return REDIRECT_ADDR;
    }

}




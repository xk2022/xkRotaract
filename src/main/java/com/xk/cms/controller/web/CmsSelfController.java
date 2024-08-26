package com.xk.cms.controller.web;

import com.xk.admin.model.dto.UserExample;
import com.xk.cms.model.dto.CmsCompanyExample;
import com.xk.cms.model.vo.CmsUserSaveResp;
import com.xk.cms.service.CmsCompanyService;
import com.xk.cms.service.CmsSelfService;
import com.xk.cms.service.CmsUserService;
import com.xk.common.base.BaseController;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * 個人管理 Controller
 * Created by yuan on 2024/04/24
 */
@Api(value = "個人管理")
@Controller
@RequestMapping("/admin/cms/manage/self")
public class CmsSelfController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsSelfController.class);

    private static final String REDIRECT_ADDR = "redirect:/admin/cms/manage/self";
    private static final int PAGE_SIZE = 8;

    @Autowired
    private CmsSelfService cmsSelfService;
    @Autowired
    private CmsUserService cmsUserService;
    @Autowired
    private CmsCompanyService cmsCompanyService;

    /**
     * 查詢 用户 資訊
     */
    @GetMapping()
    public String index(Model model, HttpSession session) {
        this.info(model, this.getClass().getAnnotation(RequestMapping.class).value()[0]);
        model.addAttribute("fragmentName", "view");

        UserExample user = (UserExample) session.getAttribute("user");
        CmsUserSaveResp entity = new CmsUserSaveResp();
        if (user != null) {
            entity = cmsUserService.getOneByEmail(user.getEmail());
        }
        if (entity == null) {
            entity = new CmsUserSaveResp();
        }
        model.addAttribute("entity", entity);

        List<CmsCompanyExample> companies = new ArrayList<>();
        if (entity != null && entity.getId() != null) {
            companies = cmsCompanyService.listByUserWithPay(entity.getId());
        }
        model.addAttribute("companies", companies);
        model.addAttribute("chunkedIndustries", cmsSelfService.getChunkedIndustries());

        return ADMIN_INDEX;
    }

}




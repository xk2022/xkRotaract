package com.xk.cms.controller.web;

import com.xk.cms.model.bo.CmsUserSaveReq;
import com.xk.cms.model.vo.CmsUserSaveResp;
import com.xk.cms.service.CmsUserService;
import com.xk.common.base.BaseController;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 用户 Controller
 * Created by yuan on 2024/02/21
 */
@Api(value = "用户管理")
@Controller
@RequestMapping("/admin/cms/manage/user")
public class CmsUserController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsUserController.class);

    private static final String REDIRECT_ADDR = "redirect:/admin/upms/manage/user";
    private static final int PAGE_SIZE = 8;

    @Autowired
    private CmsUserService cmsUserService;

    /**
     * 新增/修改 用户 Create/Update
     */
    @PostMapping("/save")
    public String post(CmsUserSaveReq resources, RedirectAttributes attributes) throws Exception {
        CmsUserSaveResp result;

        if (resources.getId() == null) {
            result = cmsUserService.create(resources);
            LOGGER.info("新增用户，主键：userId={}", result.getId());
        } else {
            result = cmsUserService.update(resources.getId(), resources);
        }

        if (result == null) {
            attributes.addFlashAttribute("message", "操作失敗");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return "redirect:/admin/cms/manage/self";
    }

}




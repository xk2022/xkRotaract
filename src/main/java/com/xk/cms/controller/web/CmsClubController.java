package com.xk.cms.controller.web;

import com.xk.cms.model.bo.CmsClubSaveReq;
import com.xk.cms.model.po.CmsClub;
import com.xk.cms.model.vo.CmsClubSaveResp;
import com.xk.cms.service.CmsClubService;
import com.xk.common.base.BaseController;
import io.swagger.annotations.Api;
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
 * 社團管理 Controller
 * @author yuan
 * Created by yuan on 2024/09/18
 */
@Api(value = "Club管理")
@Controller
@RequestMapping("/admin/cms/manage/club")
public class CmsClubController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsClubController.class);

    private static final String REDIRECT_ADDR = "redirect:/admin/cms/manage/club";
    @Autowired
    private CmsClubService cmsClubService;

    /**
     * 查詢 社團 首頁
     */
    @GetMapping()
    public String index(Model model) {
        this.info(model, this.getClass().getAnnotation(RequestMapping.class).value()[0]);
        model.addAttribute("fragmentName", "list");

        model.addAttribute("page_list", cmsClubService.list());
        model.addAttribute("entity", new CmsClub());
        return ADMIN_INDEX;
    }

    /**
     * 新增/修改 社團 Create/Update
     */
    @PostMapping("/save")
    public String post(CmsClubSaveReq resources, RedirectAttributes attributes) {
        CmsClubSaveResp result;

        if (resources.getId() == null) {
            result = cmsClubService.create(resources);
            LOGGER.info("新增用户，主键：userId={}", result.getId());
        } else {
            result = cmsClubService.update(resources.getId(), resources);
        }

        if (result == null) {
            attributes.addFlashAttribute("message", "操作失敗");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return REDIRECT_ADDR;
    }

    /**
     * 刪除 系統 Delete
     */
    @GetMapping("/delete/{ids}")
    public String delete(@PathVariable("ids") String ids, RedirectAttributes attributes) {
        cmsClubService.deleteByPrimaryKeys(ids);
        attributes.addFlashAttribute("message", "刪除成功");
        return REDIRECT_ADDR;
    }

}




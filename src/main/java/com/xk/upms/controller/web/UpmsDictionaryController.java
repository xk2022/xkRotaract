package com.xk.upms.controller.web;

import com.xk.common.base.BaseController;
import com.xk.upms.model.bo.UpmsDictionaryCategoryReq;
import com.xk.upms.model.po.UpmsDictionaryCategory;
import com.xk.upms.model.vo.UpmsDictionaryCategoryResp;
import com.xk.upms.service.UpmsDictionaryService;
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

import javax.servlet.http.HttpSession;

/**
 * 字典管理 Controller
 * Created by yuan on 2024/05/22
 */
@Api(value = "字典管理")
@Controller
@RequestMapping("/admin/upms/manage/dictionary")
public class UpmsDictionaryController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpmsDictionaryController.class);

    private static final String REDIRECT_ADDR = "redirect:/admin/upms/manage/dictionary";
    private static final int PAGE_SIZE = 8;

    @Autowired
    private UpmsDictionaryService upmsDictionaryService;

    /**
     * 查詢 首頁
     */
    @GetMapping()
    public String index(Model model) {
        this.info(model, this.getClass().getAnnotation(RequestMapping.class).value()[0]);
        model.addAttribute("fragmentName", "list");

        model.addAttribute("page_list", upmsDictionaryService.list());
        model.addAttribute("entity", new UpmsDictionaryCategory());
        return ADMIN_INDEX;
    }

    /**
     * 新增/修改 系統 Create/Update
     */
    @PostMapping("/save")
    public String post(UpmsDictionaryCategoryReq resources, RedirectAttributes attributes, HttpSession session) {
        UpmsDictionaryCategoryResp result;
        if (resources.getId() == null) {
            result = upmsDictionaryService.createCategory(resources);
        } else {
            result = upmsDictionaryService.updateCategory(resources.getId(), resources);
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
        upmsDictionaryService.deleteCategoryByPrimaryKeys(ids);
        attributes.addFlashAttribute("message", "刪除成功");
        return REDIRECT_ADDR;
    }

}

package com.xk.upms.controller.web;

import com.xk.admin.service.AuthService;
import com.xk.common.base.BaseController;
import com.xk.common.base.BaseRepostitory;
import com.xk.upms.model.bo.UpmsSystemSaveReq;
import com.xk.upms.model.dto.UpmsSystemExample;
import com.xk.upms.model.vo.UpmsSystemSaveResp;
import com.xk.upms.service.UpmsSystemService;
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
 * 系統 Controller
 * Created by yuan on 2022/06/10
 */
@Api(value = "系统管理")
@Controller
@RequestMapping("/admin/upms/manage/system")
public class UpmsSystemController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpmsSystemController.class);

//    private static final String DIR_INDEX = "truck/apps/user-management/permissions/";
//    private static final String INDEX = DIR_INDEX + "permission";
//    private static final String COMPILER = DIR_INDEX + "permission_add";
    private static final String REDIRECT_ADDR = "redirect:/admin/upms/manage/system";
    private static final int PAGE_SIZE = 8;

    @Autowired
    private BaseRepostitory baseRepostitory;
    @Autowired
    private UpmsSystemService upmsSystemService;
    @Autowired
    private AuthService authService;

    /**
     * 查詢 系統 首頁
     */
    @GetMapping()
    public String index(Model model) {
        this.info(model, this.getClass().getAnnotation(RequestMapping.class).value()[0]);
        model.addAttribute("fragmentName", "list");

        model.addAttribute("page_list", upmsSystemService.list());
        model.addAttribute("entity", new UpmsSystemExample());
//        model.addAttribute("page_thead", baseRepostitory.queryTableComent("upms_system"));
        return ADMIN_INDEX;
    }

    /**
     * 新增/修改 系統 Create/Update
     */
    @PostMapping("/save")
    public String post(UpmsSystemSaveReq resources, RedirectAttributes attributes, HttpSession session) {
        UpmsSystemSaveResp result;
        if (resources.getId() == null) {
            result = upmsSystemService.create(resources);
        } else {
            result = upmsSystemService.update(resources.getId(), resources);
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
        upmsSystemService.deleteByPrimaryKeys(ids);
        attributes.addFlashAttribute("message", "刪除成功");
        return REDIRECT_ADDR;
    }

}
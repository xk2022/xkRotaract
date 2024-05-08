package com.xk.upms.controller.web;

import com.xk.common.base.BaseController;
import com.xk.common.base.BaseRepostitory;
import com.xk.upms.model.dto.UpmsSystemExample;
import com.xk.upms.service.UpmsLogService;
import com.xk.upms.service.UpmsSystemService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 日誌 Controller
 * Created by yuan on 2022/06/24
 */
@Api(value = "日誌管理")
@Controller
@RequestMapping("/admin/upms/manage/log")
public class UpmsLogController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpmsLogController.class);

    //    private static final String DIR_INDEX = "truck/apps/user-management/permissions/";
//    private static final String INDEX = DIR_INDEX + "permission";
//    private static final String COMPILER = DIR_INDEX + "permission_add";
    private static final String REDIRECT_ADDR = "redirect:/admin/upms/manage/log";
    private static final int PAGE_SIZE = 8;

    @Autowired
    private UpmsLogService upmsLogService;

    /**
     * 查詢 日誌 首頁
     */
//    @RequiresPermissions("upms:log:read")
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("fragmentSystem", "upms");
        model.addAttribute("fragmentPackage", "log");
        this.info(model, this.getClass().getAnnotation(RequestMapping.class).value()[0]);

        model.addAttribute("page_list", upmsLogService.list(null));
        model.addAttribute("entity", new UpmsSystemExample());
//        model.addAttribute("page_thead", baseRepostitory.queryTableComent("upms_system"));
//        return "admin/upms/system/list";
        return ADMIN_INDEX;
    }

    /**
     * 刪除 日誌 Delete
     */
//    @RequiresPermissions("upms:log:delete")
    @GetMapping("/delete/{ids}")
    public String delete(@PathVariable("ids") String ids, RedirectAttributes attributes) {
        upmsLogService.deleteByPrimaryKeys(ids);
        attributes.addFlashAttribute("message", "刪除成功");
        return REDIRECT_ADDR;
    }

}
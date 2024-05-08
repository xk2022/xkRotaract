package com.xk.demo.controller.web;

import com.xk.common.base.BaseController;
import com.xk.demo.model.dto.DemoExampleExample;
import com.xk.demo.model.po.DemoExample;
import com.xk.demo.service.DemoExampleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
 * 範例 Controller
 * Created by yuan on 2022/06/10
 */
@Api(tags = "範例頁面")
@Controller
@RequestMapping("/demo/example")
public class DemoExampleController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoExampleController.class);

    private static final String DIR_INDEX = "truck/apps/user-management/permissions/";
    private static final String INDEX = DIR_INDEX + "permission";
    private static final String COMPILER = DIR_INDEX + "permission_add";
    private static final String REDIRECT_ADDR = "redirect:/truck/menu";
    private static final int PAGE_SIZE = 8;

    @Autowired
    private DemoExampleService demoExampleService;

    /**
     * 查詢菜單
     */
    @ApiOperation(value = "範例index")
    @GetMapping()
    public String index(Model model) {
        this.info(model, this.getClass().getAnnotation(RequestMapping.class).value()[0]);
        model.addAttribute("page_list", demoExampleService.list(null));
        model.addAttribute("entity", new DemoExampleExample());
//        model.addAttribute("dropdown_menus", menuService.findAdllByHidden(false));
        return "index";
    }

    /**
     * 新增/修改 菜單
     */
    @ApiOperation(value = "範例save")
    @PostMapping("/save")
    public String post(DemoExampleExample resources, RedirectAttributes attributes, HttpSession session) {
        DemoExample result;
        if (resources.getId() == null) {
            result = demoExampleService.create(resources);
        } else {
            result = demoExampleService.update(resources.getId(), resources);
        }

        if (result == null) {
            attributes.addFlashAttribute("message", "操作失敗");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return REDIRECT_ADDR;
    }

    /**
     * 刪除
     */
    @ApiOperation(value = "範例delete")
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        demoExampleService.deleteById(id);
        attributes.addFlashAttribute("message", "刪除成功");
        return REDIRECT_ADDR;
    }

}

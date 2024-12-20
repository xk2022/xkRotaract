package com.xk.demo.controller.web;

import com.xk.common.base.BaseController;
import com.xk.demo.model.bo.DemoExampleSaveReq;
import com.xk.demo.model.dto.DemoExampleExample;
import com.xk.demo.model.vo.DemoExampleSaveResp;
import com.xk.demo.service.DemoExampleService;
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
 * 範例 Controller
 * @author yuan
 * Created by yuan on 2022/06/10
 * Update by yuan at 2024/09/18
 */
@Api(tags = "範例頁面")
@Controller
@RequestMapping("/demo/example")
public class DemoExampleController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoExampleController.class);

    private static final String REDIRECT_ADDR = "redirect:/admin/cms/manage/club";
    @Autowired
    private DemoExampleService demoExampleService;

    /**
     * 查詢範例
     */
    @GetMapping()
    public String index(Model model) {
        this.info(model, this.getClass().getAnnotation(RequestMapping.class).value()[0]);
        model.addAttribute("fragmentName", "list");

        model.addAttribute("page_list", demoExampleService.list());
        model.addAttribute("entity", new DemoExampleExample());
        return ADMIN_INDEX;
    }

    /**
     * 新增/修改 範例 Create/Update
     */
    @PostMapping("/save")
    public String post(DemoExampleSaveReq resources, RedirectAttributes attributes) {
        DemoExampleSaveResp result;

        if (resources.getId() == null) {
            result = demoExampleService.create(resources);
            LOGGER.info("新增{}，主键：userId={}", "範例DemoExample", result.getId());
        } else {
            result = demoExampleService.update(resources.getId(), resources);
            LOGGER.info("更新{}，主键：userId={}", "範例DemoExample", result.getId());
        }

        if (result == null) {
            attributes.addFlashAttribute("message", "操作失敗");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return REDIRECT_ADDR;
    }

    /**
     * 刪除 範例 Delete
     */
    @GetMapping("/delete/{ids}")
    public String delete(@PathVariable("ids") String ids, RedirectAttributes attributes) {
        demoExampleService.deleteByPrimaryKeys(ids);
        attributes.addFlashAttribute("message", "刪除成功");
        return REDIRECT_ADDR;
    }
//    @GetMapping("/{id}/delete")
//    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
//        demoExampleService.deleteById(id);
//        attributes.addFlashAttribute("message", "刪除成功");
//        return REDIRECT_ADDR;
//    }

}

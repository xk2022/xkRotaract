package com.xk.upms.controller.web;

import com.xk.common.base.BaseController;
import com.xk.upms.model.bo.UpmsDictionaryCategoryReq;
import com.xk.upms.model.bo.UpmsDictionaryDataReq;
import com.xk.upms.model.po.UpmsDictionaryCategory;
import com.xk.upms.model.vo.UpmsDictionaryCategoryResp;
import com.xk.upms.model.vo.UpmsDictionaryDataResp;
import com.xk.upms.service.UpmsDictionaryService;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public String post(UpmsDictionaryCategoryReq resources, RedirectAttributes attributes) {
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


    /**
     * 新增/修改 系統 Create/Update
     */
    @PostMapping("/data/save")
    public String postCateData(@RequestParam Map<String, String> allParams, RedirectAttributes attributes) {
        List<UpmsDictionaryDataResp> resultList;

        // 獲取 category_id
        Long categoryId = Long.valueOf(allParams.get("category_id")) ;
        // 解析數據行
        List<UpmsDictionaryDataReq> resources = new ArrayList<>();

        int index = 0;
        while (allParams.containsKey("rows[" + index + "].id")) {
            UpmsDictionaryDataReq resource = new UpmsDictionaryDataReq();

            String dataId = allParams.get("rows[" + index + "].id");
            if (StringUtils.isNotBlank(dataId)) {
                resource.setId(Long.valueOf(dataId));
            }
            resource.setCode(allParams.get("rows[" + index + "].code"));
            resource.setDescription(allParams.get("rows[" + index + "].description"));
            resources.add(resource);
            index++;
        }
        // 現在 rows 包含了所有的行數據，你可以進行進一步處理或儲存到數據庫
        resultList = upmsDictionaryService.updateData(categoryId, resources);

        if (resultList == null) {
            attributes.addFlashAttribute("message", "操作失敗");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return REDIRECT_ADDR;
    }

}

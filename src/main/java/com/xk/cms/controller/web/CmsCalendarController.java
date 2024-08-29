package com.xk.cms.controller.web;

import com.xk.common.base.BaseController;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 行事曆 Controller
 * Created by yuan on 2024/08/26
 */
@Api(value = "行事曆管理")
@Controller
@RequestMapping("/admin/cms/manage/calendar")
public class CmsCalendarController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CmsCalendarController.class);

	private static final String REDIRECT_ADDR = "redirect:/admin/cms/manage/calendar";

	/**
	 * 查詢 行事曆 首頁
	 */
	@GetMapping()
	public String index(Model model) {
		this.info(model, this.getClass().getAnnotation(RequestMapping.class).value()[0]);
		model.addAttribute("fragmentName", "calendar");

//		model.addAttribute("page_list", cmsCompanyService.list());
//		model.addAttribute("entity", new UpmsSystemExample());
//		model.addAttribute("chunkedIndustries", cmsSelfService.getChunkedIndustries());
		return ADMIN_INDEX;
	}

}
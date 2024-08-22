package com.xk.cms.controller.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xk.common.base.BaseController;

import io.swagger.annotations.Api;

/**
 * 資本資料管理系統 Controller Created by yuan on 2023/12/27
 */
@Api(value = "CMS管理")
@Controller
@RequestMapping("/admin/cms/manage")
public class CmsController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CmsController.class);

	private static final String REDIRECT_ADDR = "redirect:/admin/cms/manage";
	private static final int PAGE_SIZE = 8;

	/**
	 * 查詢 角色 首頁
	 */
	@GetMapping()
	public String index(Model model) {
//		this.info(model, this.getClass().getAnnotation(RequestMapping.class).value()[0]);
		return "redirect:/admin/cms/manage/self";
	}

}
package com.xk.cms.controller.web;

import com.xk.admin.model.dto.UserExample;
import com.xk.cms.model.bo.CmsCalendarSaveReq;
import com.xk.cms.model.vo.CmsCalendarSaveResp;
import com.xk.cms.service.CmsCalendarService;
import com.xk.common.base.BaseController;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * 行事曆 Controller
 * @author yuan
 * Created by yuan on 2024/08/26
 */
@Api(value = "行事曆管理")
@Controller
@RequestMapping("/admin/cms/manage/calendar")
public class CmsCalendarController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CmsCalendarController.class);
	private static final String REDIRECT_URL = "redirect:/admin/cms/manage/calendar";

	@Autowired
	private CmsCalendarService cmsCalendarService;

	/**
	 * 查詢 行事曆 首頁
	 */
	@GetMapping()
	public String index(Model model) {
		this.info(model, this.getClass().getAnnotation(RequestMapping.class).value()[0]);
//		model.addAttribute("fragmentPackage", "calendar");
		model.addAttribute("fragmentName", "list");

		model.addAttribute("access_scope", "all");
		model.addAttribute("entity", new CmsCalendarSaveReq());
		return ADMIN_INDEX;
	}

	@GetMapping("/club")
	public String indexClub(Model model, HttpSession session) {
		this.info(model, this.getClass().getAnnotation(RequestMapping.class).value()[0] + "/club");
		model.addAttribute("fragmentPackage", "calendar");
		model.addAttribute("fragmentName", "list");

		model.addAttribute("access_scope", "club");
		UserExample user = (UserExample) session.getAttribute("user");
		if (StringUtils.isBlank(user.getRotaract_id()) || "0".equals(user.getRotaract_id())) {
			return this.errorMsg(model, "查無所屬社", "請先至“我的資料”，選擇您的所屬社！");
		}
		model.addAttribute("district_id", user.getDistrict_id());
		model.addAttribute("rotaract_id", user.getRotaract_id());
		model.addAttribute("entity", new CmsCalendarSaveReq());
		return ADMIN_INDEX;
	}

	@GetMapping("/district")
	public String indexDistrict(Model model, HttpSession session) {
		this.info(model, this.getClass().getAnnotation(RequestMapping.class).value()[0] + "/district");
		model.addAttribute("fragmentPackage", "calendar");
		model.addAttribute("fragmentName", "list");

		model.addAttribute("access_scope", "district");
		UserExample user = (UserExample) session.getAttribute("user");
		if (StringUtils.isBlank(user.getDistrict_id()) || "0".equals(user.getDistrict_id())) {
			return this.errorMsg(model, "查無所屬地區", "請先至“我的資料”，選擇您的所屬地區！");
		}
		model.addAttribute("district_id", user.getDistrict_id());
		model.addAttribute("entity", new CmsCalendarSaveReq());
		return ADMIN_INDEX;
	}


	/**
	 * 新增/修改 Create/Update
	 */
	@PostMapping("/save")
	public String post(CmsCalendarSaveReq resources, RedirectAttributes attributes) {
		CmsCalendarSaveResp result;

		if (StringUtils.isBlank(resources.getId())) {
			result = cmsCalendarService.create(resources);
			LOGGER.info("新增用户，主键：userId={}", result.getId());
		} else {
			result = cmsCalendarService.update(resources);
		}

		attributes.addFlashAttribute("initialDate", resources.getInitialDate());
		// 根據操作結果設置提示訊息
		attributes.addFlashAttribute("message", (result == null) ? "操作失敗" : "操作成功");
		return REDIRECT_URL + "/" + resources.getAccess_scope();
	}

}
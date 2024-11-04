package com.xk.cms.controller.web;

import com.xk.admin.model.dto.UserExample;
import com.xk.cms.model.dto.CmsCompanyExample;
import com.xk.cms.model.vo.CmsUserSaveResp;
import com.xk.cms.service.CmsCompanyService;
import com.xk.cms.service.CmsSelfService;
import com.xk.cms.service.CmsUserService;
import com.xk.common.base.BaseController;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 個人管理 Controller
 * <p>
 * 本控制器負責處理與個人管理相關的請求，包括查詢個人信息、個人公司信息以及行業分類等內容。
 * 用戶通過此控制器可查詢和管理與自己相關的數據，如個人資料、公司信息等。
 * <p>
 * 主要方法:
 * - index: 加載個人信息頁面，展示用戶的個人信息和公司信息，並記錄相關日誌。
 * <p>
 * 設計背景:
 * 該控制器旨在為個人用戶提供管理界面，提供信息的讀取和展示功能。通過 HttpSession 進行用戶身份驗證，
 * 以及與各服務層的交互來獲取數據，包括用戶和公司服務。
 *
 * @author yuan Created on 2024/04/24.
 * @author yuan Updated on 2024/11/01.
 */
@Api(value = "個人管理")
@Controller
@RequestMapping("/admin/cms/manage/self")
public class CmsSelfController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsSelfController.class);
    private static final String REDIRECT_URL = "redirect:/admin/cms/manage/self";

    @Autowired
    private CmsSelfService cmsSelfService;
    @Autowired
    private CmsUserService cmsUserService;
    @Autowired
    private CmsCompanyService cmsCompanyService;

    /**
     * 查詢 用戶 資訊
     *
     * 處理主頁面查詢，從 session 中獲取當前用戶，根據用戶信息加載個人詳細資料和相關公司信息。
     * 該方法記錄了每個主要步驟的日誌，包括用戶是否存在、公司信息數量等，最後將數據傳遞給視圖。
     *
     * @param model
     * @param session
     * @return the path to the admin index page
     */
    @GetMapping()
    public String index(Model model, HttpSession session) {
        LOGGER.info("進入用戶信息查詢方法：CmsSelfController().index()");
        this.info(model, this.getClass().getAnnotation(RequestMapping.class).value()[0]);
        model.addAttribute("fragmentName", "view");

        CmsUserSaveResp entity = getUserFromSession(session);
        model.addAttribute("entity", entity);
        model.addAttribute("companies", loadUserCompanies(entity));

        model.addAttribute("chunkedIndustries", cmsSelfService.getChunkedIndustries());
        LOGGER.info("成功加載用戶數據，返回視圖頁面");
        return ADMIN_INDEX;
    }

    /**
     * getUserFromSession(HttpSession session): 私有方法，用於從 session 中獲取當前用戶信息。
     * 如果用戶未登錄，則記錄警告日誌並返回一個空的 CmsUserSaveResp 對象，以避免 NullPointerException。
     *
     * @param session
     * @return
     */
    private CmsUserSaveResp getUserFromSession(HttpSession session) {
        UserExample user = (UserExample) session.getAttribute("user");
        if (user == null) {
            LOGGER.warn("用戶未登錄，session中未找到用戶信息。");
            return new CmsUserSaveResp(); // 返回空的 CmsUserSaveResp 對象以避免 NullPointerException
        }

        LOGGER.info("查詢用戶信息，用戶郵箱: {}", user.getEmail());
        return cmsUserService.getOneByEmail(user.getEmail());
    }

    /**
     * loadUserCompanies(CmsUserSaveResp entity): 私有方法，用於根據用戶的 ID 查詢公司信息。
     * 若用戶 ID 為 null，則返回空列表並記錄警告。
     *
     * @param entity
     * @return
     */
    private List<CmsCompanyExample> loadUserCompanies(CmsUserSaveResp entity) {
        if (entity == null || entity.getId() == null) {
            LOGGER.warn("無法查詢公司信息，用戶信息為空或ID為空");
            return null; // 返回空列表
        }

        List<CmsCompanyExample> companies = cmsCompanyService.listByUserWithPay(entity.getId());
        LOGGER.info("用戶ID: {}，查詢到的公司數量: {}", entity.getId(), companies.size());
        return companies;
    }

}




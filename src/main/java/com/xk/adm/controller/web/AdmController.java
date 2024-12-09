package com.xk.adm.controller.web;

import com.xk.adm.service.AdmModuleService;
import com.xk.common.base.BaseController;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ADM 管理系统 Controller
 * 提供全局管理功能，包括系统配置、日志管理、任务调度、子系统管理等。
 *
 * @author yuan Created on 2024/12/05.
 */
@Api(value = "ADM管理")
@Controller
@RequestMapping("/admin/adm/manage")
public class AdmController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdmController.class);

    @Autowired
    private AdmModuleService admModuleService;

    /**
     * 查詢 ADM 管理系統首頁
     */
    @GetMapping
    public String index(Model model) {
        // 設置當前模塊信息
        this.info(model, this.getClass().getAnnotation(RequestMapping.class).value()[0]);

        // 查詢子系統狀態
//        List<AdmSubsystem> subsystems = admSubsystemService.listAllSubsystems();
//        model.addAttribute("subsystem_list", subsystems);

        return ADMIN_INDEX;
    }

}

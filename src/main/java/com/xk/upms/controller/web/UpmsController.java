package com.xk.upms.controller.web;

import com.xk.admin.service.AuthService;
import com.xk.common.base.BaseController;
import com.xk.upms.model.po.UpmsPermission;
import com.xk.upms.model.po.UpmsSystem;
import com.xk.upms.service.UpmsRolePermissionService;
import com.xk.upms.service.UpmsSystemService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 權限管理系統 Controller
 * @author yuan
 * Created by yuan on 2023/12/27
 */
@Api(value = "UPMS管理")
@Controller
@RequestMapping("/admin/upms/manage")
public class UpmsController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpmsController.class);

    @Autowired
    private AuthService authService;
    @Autowired
    private UpmsSystemService upmsSystemService;
    @Autowired
    private UpmsRolePermissionService upmsRolePermissionService;

    /**
     * 查詢 權限管理系統 首頁
     */
    @GetMapping()
    public String index(Model model) {
        this.info(model, this.getClass().getAnnotation(RequestMapping.class).value()[0]);

        UpmsSystem system = (UpmsSystem) upmsSystemService.findOneByName("upms");
        List<UpmsPermission> permissions = authService.listPermission(system.getId(), (byte) 2);
        model.addAttribute("page_list", permissions);
        return ADMIN_INDEX;
    }

}
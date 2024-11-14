package com.xk.admin.controller.rest;

import com.xk.admin.controller.web.AuthorizationController;
import com.xk.admin.model.dto.UserExample;
import com.xk.admin.service.AuthService;
import com.xk.common.base.BaseController;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 *
 * @author yuan Created on 2024/11/13.
 */
@RestController
@Api(value = "AuthorizationRestController")
@RequestMapping("/api/auth")
public class AuthorizationRestController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationRestController.class);

    @Autowired
    private AuthorizationController authorizationController;
    @Autowired
    private AuthService authService;

    @PostMapping("/checkLogin")
    public UserExample login(@RequestParam String account, @RequestParam String password) {
        // 密码解密
//        String password = MD5Utils.convertMD5(authUser.getPassword());
        UserExample user = authService.checkUser(account, password);
        return user;
    }

}

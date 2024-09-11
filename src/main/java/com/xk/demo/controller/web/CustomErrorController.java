package com.xk.demo.controller.web;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 範例 Controller
 * Created by yuan on 2024/09/10
 */
@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        // 获取错误状态码
        Object status = request.getAttribute("javax.servlet.error.status_code");

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == 404) {
                return "error/404";  // 返回自定义404页面
            } else if (statusCode == 500) {
                return "error/500";  // 返回自定义500页面
            }
        }
        return "error/error";  // 返回默认错误页面
    }

}

package com.xk.ui.controller.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Index RedirectController
 * Created by yuan on 2024/09/09
 */
@RestController
public class RedirectController {

    @PostMapping("/redirect")
    public String handleRedirect(@RequestBody Map<String, String> requestData) {
        String url = requestData.get("url");

        // 确保 url 是安全的，避免 XSS 攻击
        if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
            // 返回重定向的 URL
            return url;
        } else {
            // 如果 URL 不安全或无效，返回默认主页
            return "/";
        }
    }

}

package com.xk.config;

import com.xk.admin.model.dto.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    @Autowired
    private HttpServletRequest request;

    @Override
    public Optional<String> getCurrentAuditor() {
        // 這裡從 session 或安全上下文中獲取當前用戶的 user_id
        // 假設你有一個方法來獲取當前用戶，返回的字符串就是 user_id
        String currentUserId = getCurrentUserIdFromSession();
        return Optional.ofNullable(currentUserId);
    }

    // 這裡是自定義的方法，從 session 中獲取當前用戶的 user_id
    private String getCurrentUserIdFromSession() {
        // 假設你已經將 user_id 放到 session 中
        // 从 HttpSession 中获取用户信息
        HttpSession session = request.getSession();
        UserExample user = (UserExample) session.getAttribute("user");

        if (user == null) {
            return null;
        }
        // 你可以根據具體項目修改這段邏輯
        return String.valueOf(user.getId());
    }
}

package com.xk.upms.controller.web;

import com.xk.common.base.BaseController;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 會話 Controller
 * Created by yuan on 2022/06/24
 */
@Api(value = "會話管理")
@Controller
@RequestMapping("/admin/upms/manage/session")
public class UpmsSessionController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpmsSessionController.class);

    private static final String REDIRECT_ADDR = "redirect:/admin/upms/manage/session";
    private static final int PAGE_SIZE = 8;

//    @Autowired
//    private UpmsSessionDao sessionDAO;

    /**
     * 查詢 會話 首頁
     */
//    @RequiresPermissions("upms:session:read")
    @GetMapping()
    public String index(Model model) {
        return ADMIN_INDEX;
//        return sessionDAO.getActiveSessions(offset, limit);
    }

    /**
     * 強制退出
     */
//    @RequiresPermissions("upms:session:forceout")
    @GetMapping("/forceout/{ids}")
    public Object forceout(@PathVariable("ids") String ids) {
//        int count = sessionDAO.forceout(ids);
//        return new UpmsResult(UpmsResultConstant.SUCCESS, count);
        return null;
    }
}
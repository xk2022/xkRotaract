package com.xk.ui.controller.web;

import com.xk.common.base.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * IndexCalendar Controller
 * @author yuan
 * Created by yuan on 2024/09/25
 */
@Controller
@RequestMapping("/Calendar")
public class IndexCalendarController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexCalendarController.class);

    public static final String DIR_CALENDAR_INDEX = "/calendar/index";

    /**
     * 首頁
     */
    @GetMapping()
    public String index(Model model) {
        this.info(model);

        return DIR_CALENDAR_INDEX;
    }

    @GetMapping("/p")
    public String indexP(Model model) {
        this.info(model);

        return DIR_CALENDAR_INDEX+"P";
    }
}

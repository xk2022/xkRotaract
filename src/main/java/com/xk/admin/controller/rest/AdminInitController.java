package com.xk.admin.controller.rest;

import com.xk.upms.controller.rest.*;
import com.xk.upms.dao.repository.UpmsDictionaryCategoryRepository;
import com.xk.upms.dao.repository.UpmsDictionaryDataRepository;
import com.xk.upms.model.po.UpmsDictionaryCategory;
import com.xk.upms.model.po.UpmsDictionaryData;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户 RestController
 * Created by yuan on 2022/06/10
 */
@RestController
@Api("系统啟動資料api")
@RequestMapping("/api/data/init")
public class AdminInitController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminInitController.class);

    @Autowired
    private UpmsSystemRestController upmsSystemRestController;
    @Autowired
    private UpmsPermissionRestController upmsPermissionRestController;
    @Autowired
    private UpmsRoleRestController upmsRoleRestController;
    @Autowired
    private UpmsUserRestController upmsUserRestController;
    @Autowired
    private UpmsOrganizationRestController upmsOrganizationRestController;
    @Autowired
    private UpmsDictionaryCategoryRepository categoryRepository;
    @Autowired
    private UpmsDictionaryDataRepository dataRepository;
//    @Autowired
//    private CmsClubRepository clubRepository;

    /**
     * init()
     */
    @GetMapping("/init")
    public String init() throws Exception {
        upmsSystemRestController.init();
        upmsPermissionRestController.init();
        upmsRoleRestController.init();
        upmsUserRestController.init();
        upmsUserRestController.role();
        upmsRoleRestController.permission();
        upmsOrganizationRestController.init();
        this.initDDD();
//        this.insertClub(); // club


        return "OK";
    }

    private void initDDD() {
//        打印所有地区的键和值

        List<UpmsDictionaryCategory> categories = new ArrayList<>();
        // 001
        UpmsDictionaryCategory sexCat = new UpmsDictionaryCategory();
        sexCat.setCode("dropdown_SEX");
        sexCat.setDescription("性別選單");
        UpmsDictionaryCategory sexEntity = categoryRepository.save(sexCat);

        UpmsDictionaryData sexData = new UpmsDictionaryData();
        sexData.setParentId(sexEntity.getId());

        sexData.setCode("0");
        sexData.setDescription("男性");
        dataRepository.save(sexData);

        sexData.setCode("1");
        sexData.setDescription("女性");
        dataRepository.save(sexData);

        // 002
        UpmsDictionaryCategory eventCat = new UpmsDictionaryCategory();
        sexCat.setCode("dropdown_EVENTS_TYPE");
        sexCat.setDescription("行事曆活動類型");
        UpmsDictionaryCategory eventEntity = categoryRepository.save(eventCat);

        UpmsDictionaryData eventData = new UpmsDictionaryData();
        eventData.setParentId(eventEntity.getId());

        eventData.setCode("0");
        eventData.setDescription("國定假日");
        dataRepository.save(eventData);

        eventData.setCode("1");
        eventData.setDescription("例會");
        dataRepository.save(eventData);

        eventData.setCode("2");
        eventData.setDescription("活動");
        dataRepository.save(eventData);
    }



}


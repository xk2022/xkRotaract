package com.xk.admin.controller.rest;

import com.xk.common.json.*;
import com.xk.upms.controller.rest.UpmsPermissionRestController;
import com.xk.upms.controller.rest.UpmsRoleRestController;
import com.xk.upms.controller.rest.UpmsSystemRestController;
import com.xk.upms.controller.rest.UpmsUserRestController;
import com.xk.upms.dao.repository.UpmsDictionaryCategaryRepository;
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
    private UpmsDictionaryCategaryRepository categaryRepository;
    @Autowired
    private UpmsDictionaryDataRepository dataRepository;

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
        return "OK";
    }

    @GetMapping("/initDDD")
    public void initDDD() {
        categaryRepository.deleteAll();
        // 打印所有地区的键和值

        List<UpmsDictionaryCategory> categories = new ArrayList<>();
        // 001
        UpmsDictionaryCategory sexCat = new UpmsDictionaryCategory();
        sexCat.setCode("dropdown_SEX");
        sexCat.setDescription("性別選單");
        categories.add(sexCat);

        // 002
        UpmsDictionaryCategory districtCat = new UpmsDictionaryCategory();
        sexCat.setCode("dropdown_DISTRICT");
        sexCat.setDescription("所有地區選單");
        categories.add(districtCat);

        System.out.println("所有地区：");
        for (District district : District.values()) {
            System.out.printf("键: %d, 名称: %s%n", district.getKey(), district.getName());
            UpmsDictionaryCategory req = new UpmsDictionaryCategory();
            req.setCode("dropdown_DISTRICT" + String.valueOf(district.getKey()));
            req.setDescription(district.getName() + "所有社名");
            categories.add(req);
        }

        categaryRepository.saveAll(categories);



        dataRepository.deleteAll();

        List<UpmsDictionaryData> datas = new ArrayList<>();

        System.out.println("所有地区：");
        for (District district : District.values()) {
            System.out.printf("键: %d, 名称: %s%n", district.getKey(), district.getName());
            UpmsDictionaryData req = new UpmsDictionaryData();
            req.setParentId(categaryRepository.findOneByCode("dropdown_DISTRICT").getId());
            req.setCode(String.valueOf(district.getKey()));
            req.setDescription(district.getName());
            datas.add(req);
        }
        // 打印所有地区的键和值
        for (District_3461 district : District_3461.values()) {
            System.out.printf("键: %d, 名称: %s%n", district.getKey(), district.getName());
            UpmsDictionaryData req = new UpmsDictionaryData();
            req.setParentId(categaryRepository.findOneByCode("dropdown_DISTRICT3461").getId());
            req.setCode(String.valueOf(district.getKey()));
            req.setDescription(district.getName());
            datas.add(req);
        }
        for (District_3462 district : District_3462.values()) {
            System.out.printf("键: %d, 名称: %s%n", district.getKey(), district.getName());
            UpmsDictionaryData req = new UpmsDictionaryData();
            req.setParentId(categaryRepository.findOneByCode("dropdown_DISTRICT3462").getId());
            req.setCode(String.valueOf(district.getKey()));
            req.setDescription(district.getName());
            datas.add(req);
        }
        for (District_3481 district : District_3481.values()) {
            System.out.printf("键: %d, 名称: %s%n", district.getKey(), district.getName());
            UpmsDictionaryData req = new UpmsDictionaryData();
            req.setParentId(categaryRepository.findOneByCode("dropdown_DISTRICT3481").getId());
            req.setCode(String.valueOf(district.getKey()));
            req.setDescription(district.getName());
            datas.add(req);
        }
        for (District_3482 district : District_3482.values()) {
            System.out.printf("键: %d, 名称: %s%n", district.getKey(), district.getName());
            UpmsDictionaryData req = new UpmsDictionaryData();
            req.setParentId(categaryRepository.findOneByCode("dropdown_DISTRICT3482").getId());
            req.setCode(String.valueOf(district.getKey()));
            req.setDescription(district.getName());
            datas.add(req);
        }
        for (District_3490 district : District_3490.values()) {
            System.out.printf("键: %d, 名称: %s%n", district.getKey(), district.getName());
            UpmsDictionaryData req = new UpmsDictionaryData();
            req.setParentId(categaryRepository.findOneByCode("dropdown_DISTRICT3490").getId());
            req.setCode(String.valueOf(district.getKey()));
            req.setDescription(district.getName());
            datas.add(req);
        }
        for (District_3501 district : District_3501.values()) {
            System.out.printf("键: %d, 名称: %s%n", district.getKey(), district.getName());
            UpmsDictionaryData req = new UpmsDictionaryData();
            req.setParentId(categaryRepository.findOneByCode("dropdown_DISTRICT3501").getId());
            req.setCode(String.valueOf(district.getKey()));
            req.setDescription(district.getName());
            datas.add(req);
        }
        for (District_3502 district : District_3502.values()) {
            System.out.printf("键: %d, 名称: %s%n", district.getKey(), district.getName());
            UpmsDictionaryData req = new UpmsDictionaryData();
            req.setParentId(categaryRepository.findOneByCode("dropdown_DISTRICT3502").getId());
            req.setCode(String.valueOf(district.getKey()));
            req.setDescription(district.getName());
            datas.add(req);
        }
        for (District_3510 district : District_3510.values()) {
            System.out.printf("键: %d, 名称: %s%n", district.getKey(), district.getName());
            UpmsDictionaryData req = new UpmsDictionaryData();
            req.setParentId(categaryRepository.findOneByCode("dropdown_DISTRICT3510").getId());
            req.setCode(String.valueOf(district.getKey()));
            req.setDescription(district.getName());
            datas.add(req);
        }
        for (District_3521 district : District_3521.values()) {
            System.out.printf("键: %d, 名称: %s%n", district.getKey(), district.getName());
            UpmsDictionaryData req = new UpmsDictionaryData();
            req.setParentId(categaryRepository.findOneByCode("dropdown_DISTRICT3521").getId());
            req.setCode(String.valueOf(district.getKey()));
            req.setDescription(district.getName());
            datas.add(req);
        }
        for (District_3522 district : District_3522.values()) {
            System.out.printf("键: %d, 名称: %s%n", district.getKey(), district.getName());
            UpmsDictionaryData req = new UpmsDictionaryData();
            req.setParentId(categaryRepository.findOneByCode("dropdown_DISTRICT3522").getId());
            req.setCode(String.valueOf(district.getKey()));
            req.setDescription(district.getName());
            datas.add(req);
        }

        dataRepository.saveAll(datas);
    }

}


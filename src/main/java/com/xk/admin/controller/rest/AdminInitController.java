package com.xk.admin.controller.rest;

import com.xk.cms.dao.repository.CmsClubRepository;
import com.xk.cms.model.po.CmsClub;
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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    @Autowired
    private CmsClubRepository clubRepository;

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
        this.initDDD();
        this.insertClub();

        return "OK";
    }

    private void initDDD() {
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
//        // 打印所有地区的键和值
//        for (District_3461 district : District_3461.values()) {
//            System.out.printf("键: %d, 名称: %s%n", district.getKey(), district.getName());
//            UpmsDictionaryData req = new UpmsDictionaryData();
//            req.setParentId(categaryRepository.findOneByCode("dropdown_DISTRICT3461").getId());
//            req.setCode(String.valueOf(district.getKey()));
//            req.setDescription(district.getName());
//            datas.add(req);
//        }
//        for (District_3462 district : District_3462.values()) {
//            System.out.printf("键: %d, 名称: %s%n", district.getKey(), district.getName());
//            UpmsDictionaryData req = new UpmsDictionaryData();
//            req.setParentId(categaryRepository.findOneByCode("dropdown_DISTRICT3462").getId());
//            req.setCode(String.valueOf(district.getKey()));
//            req.setDescription(district.getName());
//            datas.add(req);
//        }
//        for (District_3481 district : District_3481.values()) {
//            System.out.printf("键: %d, 名称: %s%n", district.getKey(), district.getName());
//            UpmsDictionaryData req = new UpmsDictionaryData();
//            req.setParentId(categaryRepository.findOneByCode("dropdown_DISTRICT3481").getId());
//            req.setCode(String.valueOf(district.getKey()));
//            req.setDescription(district.getName());
//            datas.add(req);
//        }
//        for (District_3482 district : District_3482.values()) {
//            System.out.printf("键: %d, 名称: %s%n", district.getKey(), district.getName());
//            UpmsDictionaryData req = new UpmsDictionaryData();
//            req.setParentId(categaryRepository.findOneByCode("dropdown_DISTRICT3482").getId());
//            req.setCode(String.valueOf(district.getKey()));
//            req.setDescription(district.getName());
//            datas.add(req);
//        }
//        for (District_3490 district : District_3490.values()) {
//            System.out.printf("键: %d, 名称: %s%n", district.getKey(), district.getName());
//            UpmsDictionaryData req = new UpmsDictionaryData();
//            req.setParentId(categaryRepository.findOneByCode("dropdown_DISTRICT3490").getId());
//            req.setCode(String.valueOf(district.getKey()));
//            req.setDescription(district.getName());
//            datas.add(req);
//        }
//        for (District_3501 district : District_3501.values()) {
//            System.out.printf("键: %d, 名称: %s%n", district.getKey(), district.getName());
//            UpmsDictionaryData req = new UpmsDictionaryData();
//            req.setParentId(categaryRepository.findOneByCode("dropdown_DISTRICT3501").getId());
//            req.setCode(String.valueOf(district.getKey()));
//            req.setDescription(district.getName());
//            datas.add(req);
//        }
//        for (District_3502 district : District_3502.values()) {
//            System.out.printf("键: %d, 名称: %s%n", district.getKey(), district.getName());
//            UpmsDictionaryData req = new UpmsDictionaryData();
//            req.setParentId(categaryRepository.findOneByCode("dropdown_DISTRICT3502").getId());
//            req.setCode(String.valueOf(district.getKey()));
//            req.setDescription(district.getName());
//            datas.add(req);
//        }
//        for (District_3510 district : District_3510.values()) {
//            System.out.printf("键: %d, 名称: %s%n", district.getKey(), district.getName());
//            UpmsDictionaryData req = new UpmsDictionaryData();
//            req.setParentId(categaryRepository.findOneByCode("dropdown_DISTRICT3510").getId());
//            req.setCode(String.valueOf(district.getKey()));
//            req.setDescription(district.getName());
//            datas.add(req);
//        }
//        for (District_3521 district : District_3521.values()) {
//            System.out.printf("键: %d, 名称: %s%n", district.getKey(), district.getName());
//            UpmsDictionaryData req = new UpmsDictionaryData();
//            req.setParentId(categaryRepository.findOneByCode("dropdown_DISTRICT3521").getId());
//            req.setCode(String.valueOf(district.getKey()));
//            req.setDescription(district.getName());
//            datas.add(req);
//        }
//        for (District_3522 district : District_3522.values()) {
//            System.out.printf("键: %d, 名称: %s%n", district.getKey(), district.getName());
//            UpmsDictionaryData req = new UpmsDictionaryData();
//            req.setParentId(categaryRepository.findOneByCode("dropdown_DISTRICT3522").getId());
//            req.setCode(String.valueOf(district.getKey()));
//            req.setDescription(district.getName());
//            datas.add(req);
//        }

        dataRepository.saveAll(datas);
    }

    @GetMapping("insertClub")
    public void insertClub() {
        List<CmsClub> datas = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

        // 打印所有地区的键和值
        for (District_3461 club : District_3461.values()) {
            System.out.printf("键: %d, 名称: %s%n", club.getKey(), club.getName());
            CmsClub req = new CmsClub();
            req.setDistrict("3461");
            String[] splitStr = club.getName().split("_");
            req.setName(splitStr[0]);
            if (StringUtils.isNotBlank(splitStr[1])) {
                try {
                    Date date = formatter.parse(splitStr[1]);
                    req.setRegistrationDate(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            req.setStatus(true);
            req.setCreateBy("system restAPI");
            datas.add(req);
        }
        for (District_3462 club : District_3462.values()) {
            System.out.printf("键: %d, 名称: %s%n", club.getKey(), club.getName());
            CmsClub req = new CmsClub();
            req.setDistrict("3462");
            String[] splitStr = club.getName().split("_");
            req.setName(splitStr[0]);
            if (StringUtils.isNotBlank(splitStr[1])) {
                try {
                    Date date = formatter.parse(splitStr[1]);
                    req.setRegistrationDate(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            req.setStatus(true);
            req.setCreateBy("system restAPI");
            datas.add(req);
        }
        for (District_3481 club : District_3481.values()) {
            System.out.printf("键: %d, 名称: %s%n", club.getKey(), club.getName());
            CmsClub req = new CmsClub();
            req.setDistrict("3481");
            String[] splitStr = club.getName().split("_");
            req.setName(splitStr[0]);
            if (StringUtils.isNotBlank(splitStr[1])) {
                try {
                    Date date = formatter.parse(splitStr[1]);
                    req.setRegistrationDate(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            req.setStatus(true);
            req.setCreateBy("system restAPI");
            datas.add(req);
        }
        for (District_3482 club : District_3482.values()) {
            System.out.printf("键: %d, 名称: %s%n", club.getKey(), club.getName());
            CmsClub req = new CmsClub();
            req.setDistrict("3482");
            String[] splitStr = club.getName().split("_");
            req.setName(splitStr[0]);
            if (StringUtils.isNotBlank(splitStr[1])) {
                try {
                    Date date = formatter.parse(splitStr[1]);
                    req.setRegistrationDate(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            req.setStatus(true);
            req.setCreateBy("system restAPI");
            datas.add(req);
        }
        for (District_3490 club : District_3490.values()) {
            System.out.printf("键: %d, 名称: %s%n", club.getKey(), club.getName());
            CmsClub req = new CmsClub();
            req.setDistrict("3490");
            String[] splitStr = club.getName().split("_");
            req.setName(splitStr[0]);
            if (StringUtils.isNotBlank(splitStr[1])) {
                try {
                    Date date = formatter.parse(splitStr[1]);
                    req.setRegistrationDate(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            req.setStatus(true);
            req.setCreateBy("system restAPI");
            datas.add(req);
        }
        for (District_3501 club : District_3501.values()) {
            System.out.printf("键: %d, 名称: %s%n", club.getKey(), club.getName());
            CmsClub req = new CmsClub();
            req.setDistrict("3501");
            String[] splitStr = club.getName().split("_");
            req.setName(splitStr[0]);
            if (StringUtils.isNotBlank(splitStr[1])) {
                try {
                    Date date = formatter.parse(splitStr[1]);
                    req.setRegistrationDate(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            req.setStatus(true);
            req.setCreateBy("system restAPI");
            datas.add(req);
        }
        for (District_3502 club : District_3502.values()) {
            System.out.printf("键: %d, 名称: %s%n", club.getKey(), club.getName());
            CmsClub req = new CmsClub();
            req.setDistrict("3502");
            String[] splitStr = club.getName().split("_");
            req.setName(splitStr[0]);
            if (StringUtils.isNotBlank(splitStr[1])) {
                try {
                    Date date = formatter.parse(splitStr[1]);
                    req.setRegistrationDate(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            req.setStatus(true);
            req.setCreateBy("system restAPI");
            datas.add(req);
        }
        for (District_3510 club : District_3510.values()) {
            System.out.printf("键: %d, 名称: %s%n", club.getKey(), club.getName());
            CmsClub req = new CmsClub();
            req.setDistrict("3510");
            String[] splitStr = club.getName().split("_");
            req.setName(splitStr[0]);
            if (StringUtils.isNotBlank(splitStr[1])) {
                try {
                    Date date = formatter.parse(splitStr[1]);
                    req.setRegistrationDate(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            req.setStatus(true);
            req.setCreateBy("system restAPI");
            datas.add(req);
        }
        for (District_3521 club : District_3521.values()) {
            System.out.printf("键: %d, 名称: %s%n", club.getKey(), club.getName());
            CmsClub req = new CmsClub();
            req.setDistrict("3521");
            String[] splitStr = club.getName().split("_");
            req.setName(splitStr[0]);
            if (StringUtils.isNotBlank(splitStr[1])) {
                try {
                    Date date = formatter.parse(splitStr[1]);
                    req.setRegistrationDate(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            req.setStatus(true);
            req.setCreateBy("system restAPI");
            datas.add(req);
        }
        for (District_3522 club : District_3522.values()) {
            System.out.printf("键: %d, 名称: %s%n", club.getKey(), club.getName());
            CmsClub req = new CmsClub();
            req.setDistrict("3522");
            String[] splitStr = club.getName().split("_");
            req.setName(splitStr[0]);
            if (StringUtils.isNotBlank(splitStr[1])) {
                try {
                    Date date = formatter.parse(splitStr[1]);
                    req.setRegistrationDate(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            req.setStatus(true);
            req.setCreateBy("system restAPI");
            datas.add(req);
        }

        clubRepository.saveAll(datas);
    }

}


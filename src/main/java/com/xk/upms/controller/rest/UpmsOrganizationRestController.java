package com.xk.upms.controller.rest;

import com.xk.common.json.*;
import com.xk.upms.model.bo.UpmsOrganizationSaveReq;
import com.xk.upms.model.vo.UpmsOrganizationResp;
import com.xk.upms.service.UpmsOrganizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 組織 RestController
 *
 * @author yuan Created on 2024/11/01
 */
@Api("組織管理 API")
@RestController
@RequestMapping("/api/manage/organization")
public class UpmsOrganizationRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpmsOrganizationRestController.class);

    @Autowired
    private UpmsOrganizationService upmsOrganizationService;

    @ApiOperation(value = "查找指定組織的子組織列表")
    @PostMapping("/findChildren")
    public List listDictionaryData(@RequestBody Map<String, Object> requestBody) {
        String code = (String) requestBody.get("code");
        String id = (String) requestBody.get("id");

        if (StringUtils.isBlank(code)) {
            if (StringUtils.isBlank(id)) {
                throw new IllegalArgumentException("Code 參數不可為空");
            } else {
                UpmsOrganizationResp resp = upmsOrganizationService.findById(Long.valueOf(id));
                code = resp.getCode();
            }
        }
        LOGGER.info("查詢子組織，父組織代碼: {}", code);
        return upmsOrganizationService.findChildren(code);
    }

    @ApiOperation(value = "获取所有 District 数据")
    @PostMapping("/getAllDistricts")
    public Map<String, List<UpmsOrganizationResp>> getAllDistricts() {
        LOGGER.info("获取所有 District 数据");
        return upmsOrganizationService.getAllDistricts();
    }

    @ApiOperation(value = "初始化設置")
    @GetMapping("/init")
    public String init() {

        // RIT
        UpmsOrganizationSaveReq uoSaveReq = new UpmsOrganizationSaveReq();
        uoSaveReq.setStatus("1");
        /**
         *
         */
        uoSaveReq.setLevel("1");

        uoSaveReq.setName("測試是不是第一筆消失");
        uoSaveReq.setCode("N/A");
        uoSaveReq.setDescription("測試是不是第一筆消失");
        uoSaveReq.setOrders("0");
        upmsOrganizationService.create(uoSaveReq);

        uoSaveReq.setName("系統管理員");
        uoSaveReq.setCode("Administrator");
        uoSaveReq.setDescription("系統（網站）管理團隊");
        uoSaveReq.setOrders("1");
        upmsOrganizationService.create(uoSaveReq);

        uoSaveReq.setName("台灣扶青多地區資訊組織 MDIO Rotaract In Taiwan");
        uoSaveReq.setCode("RIT");
        uoSaveReq.setDescription("台灣扶青多地區資訊組織 MDIO Rotaract In Taiwan");
        uoSaveReq.setOrders("2");
        upmsOrganizationService.create(uoSaveReq);
        /**
         *
         */
        uoSaveReq.setLevel("2");

        uoSaveReq.setParentId(upmsOrganizationService.findByCode("Administrator").getId());
        uoSaveReq.setName("系統管理員");
        uoSaveReq.setCode("Admin");
        uoSaveReq.setDescription("Admin fo pro permission and organization");
        uoSaveReq.setOrders("2");
        upmsOrganizationService.create(uoSaveReq);

        System.out.println("所有地区：");
        uoSaveReq.setParentId(upmsOrganizationService.findByCode("RIT").getId());

        for (District district : District.values()) {
            System.out.printf("键: %d, 名称: %s%n", district.getKey(), district.getName());

            uoSaveReq.setName("國際扶輪" + district.getName());
            uoSaveReq.setCode("D" + district.getKey());
            uoSaveReq.setDescription("國際扶輪" + district.getName() + "-扶青團隊");
            uoSaveReq.setOrders(String.valueOf(district.getKey()));
            upmsOrganizationService.create(uoSaveReq);
        }
        /**
         *
         */
        uoSaveReq.setLevel("3");
        String districtCode, clubName, clubCode;

        // 打印所有地区的键和值
        districtCode = "3461";
        uoSaveReq.setParentId(upmsOrganizationService.findByCode("D" + districtCode).getId());
        for (District_3461 club : District_3461.values()) {
            System.out.printf("键: %d, 名称: %s%n", club.getKey(), club.getName());

            clubName = club.getName().split("_")[0];
            clubCode = districtCode + String.format("%04d", club.getKey());
            uoSaveReq.setName(clubName);
            uoSaveReq.setCode("C" + clubCode);
            uoSaveReq.setDescription("D" + districtCode + "-C" + clubCode + "-" + club.getName().split("_")[1]);
            uoSaveReq.setOrders(clubCode);
            upmsOrganizationService.create(uoSaveReq);
        }

        districtCode = "3462";
        uoSaveReq.setParentId(upmsOrganizationService.findByCode("D" + districtCode).getId());
        for (District_3462 club : District_3462.values()) {
            System.out.printf("键: %d, 名称: %s%n", club.getKey(), club.getName());

            clubName = club.getName().split("_")[0];
            clubCode = districtCode + String.format("%04d", club.getKey());
            uoSaveReq.setName(clubName);
            uoSaveReq.setCode("C" + clubCode);
            uoSaveReq.setDescription("D" + districtCode + "-C" + clubCode + "-" + club.getName().split("_")[1]);
            uoSaveReq.setOrders(clubCode);
            upmsOrganizationService.create(uoSaveReq);
        }

        districtCode = "3481";
        uoSaveReq.setParentId(upmsOrganizationService.findByCode("D" + districtCode).getId());
        for (District_3481 club : District_3481.values()) {
            System.out.printf("键: %d, 名称: %s%n", club.getKey(), club.getName());

            clubName = club.getName().split("_")[0];
            clubCode = districtCode + String.format("%04d", club.getKey());
            uoSaveReq.setName(clubName);
            uoSaveReq.setCode("C" + clubCode);
            uoSaveReq.setDescription("D" + districtCode + "-C" + clubCode + "-" + club.getName().split("_")[1]);
            uoSaveReq.setOrders(clubCode);
            upmsOrganizationService.create(uoSaveReq);
        }

        districtCode = "3482";
        uoSaveReq.setParentId(upmsOrganizationService.findByCode("D" + districtCode).getId());
        for (District_3482 club : District_3482.values()) {
            System.out.printf("键: %d, 名称: %s%n", club.getKey(), club.getName());

            clubName = club.getName().split("_")[0];
            clubCode = districtCode + String.format("%04d", club.getKey());
            uoSaveReq.setName(clubName);
            uoSaveReq.setCode("C" + clubCode);
            uoSaveReq.setDescription("D" + districtCode + "-C" + clubCode + "-" + club.getName().split("_")[1]);
            uoSaveReq.setOrders(clubCode);
            upmsOrganizationService.create(uoSaveReq);
        }

        districtCode = "3490";
        uoSaveReq.setParentId(upmsOrganizationService.findByCode("D" + districtCode).getId());
        for (District_3490 club : District_3490.values()) {
            System.out.printf("键: %d, 名称: %s%n", club.getKey(), club.getName());

            clubName = club.getName().split("_")[0];
            clubCode = districtCode + String.format("%04d", club.getKey());
            uoSaveReq.setName(clubName);
            uoSaveReq.setCode("C" + clubCode);
            uoSaveReq.setDescription("D" + districtCode + "-C" + clubCode + "-" + club.getName().split("_")[1]);
            uoSaveReq.setOrders(clubCode);
            upmsOrganizationService.create(uoSaveReq);
        }

        districtCode = "3501";
        uoSaveReq.setParentId(upmsOrganizationService.findByCode("D" + districtCode).getId());
        for (District_3501 club : District_3501.values()) {
            System.out.printf("键: %d, 名称: %s%n", club.getKey(), club.getName());

            clubName = club.getName().split("_")[0];
            clubCode = districtCode + String.format("%04d", club.getKey());
            uoSaveReq.setName(clubName);
            uoSaveReq.setCode("C" + clubCode);
            uoSaveReq.setDescription("D" + districtCode + "-C" + clubCode + "-" + club.getName().split("_")[1]);
            uoSaveReq.setOrders(clubCode);
            upmsOrganizationService.create(uoSaveReq);
        }

        districtCode = "3502";
        uoSaveReq.setParentId(upmsOrganizationService.findByCode("D" + districtCode).getId());
        for (District_3502 club : District_3502.values()) {
            System.out.printf("键: %d, 名称: %s%n", club.getKey(), club.getName());

            clubName = club.getName().split("_")[0];
            clubCode = districtCode + String.format("%04d", club.getKey());
            uoSaveReq.setName(clubName);
            uoSaveReq.setCode("C" + clubCode);
            uoSaveReq.setDescription("D" + districtCode + "-C" + clubCode + "-" + club.getName().split("_")[1]);
            uoSaveReq.setOrders(clubCode);
            upmsOrganizationService.create(uoSaveReq);
        }

        districtCode = "3521";
        uoSaveReq.setParentId(upmsOrganizationService.findByCode("D" + districtCode).getId());
        for (District_3521 club : District_3521.values()) {
            System.out.printf("键: %d, 名称: %s%n", club.getKey(), club.getName());

            clubName = club.getName().split("_")[0];
            clubCode = districtCode + String.format("%04d", club.getKey());
            uoSaveReq.setName(clubName);
            uoSaveReq.setCode("C" + clubCode);
            uoSaveReq.setDescription("D" + districtCode + "-C" + clubCode + "-" + club.getName().split("_")[1]);
            uoSaveReq.setOrders(clubCode);
            upmsOrganizationService.create(uoSaveReq);
        }

        districtCode = "3522";
        uoSaveReq.setParentId(upmsOrganizationService.findByCode("D" + districtCode).getId());
        for (District_3522 club : District_3522.values()) {
            System.out.printf("键: %d, 名称: %s%n", club.getKey(), club.getName());

            clubName = club.getName().split("_")[0];
            clubCode = districtCode + String.format("%04d", club.getKey());
            uoSaveReq.setName(clubName);
            uoSaveReq.setCode("C" + clubCode);
            uoSaveReq.setDescription("D" + districtCode + "-C" + clubCode + "-" + club.getName().split("_")[1]);
            uoSaveReq.setOrders(clubCode);
            upmsOrganizationService.create(uoSaveReq);
        }
        return "Initialization OK";
    }




//    private void initDDD() {
        // 打印所有地区的键和值
//
//        List<UpmsDictionaryCategory> categories = new ArrayList<>();
//        // 001
//        UpmsDictionaryCategory sexCat = new UpmsDictionaryCategory();
//        sexCat.setCode("dropdown_SEX");
//        sexCat.setDescription("性別選單");
//        categories.add(sexCat);






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

//        dataRepository.saveAll(datas);
//    }
//
//    @GetMapping("insertClub")
//    public void insertClub() {
//        List<CmsClub> datas = new ArrayList<>();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
//
//        // 打印所有地区的键和值
//        for (District_3461 club : District_3461.values()) {
//            System.out.printf("键: %d, 名称: %s%n", club.getKey(), club.getName());
//            CmsClub req = new CmsClub();
//            req.setDistrict("3461");
//            String[] splitStr = club.getName().split("_");
//            req.setName(splitStr[0]);
//            if (StringUtils.isNotBlank(splitStr[1])) {
//                try {
//                    Date date = formatter.parse(splitStr[1]);
//                    req.setRegistrationDate(date);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//            req.setStatus(true);
//            req.setCreateBy("system restAPI");
//            datas.add(req);
//        }
//        for (District_3462 club : District_3462.values()) {
//            System.out.printf("键: %d, 名称: %s%n", club.getKey(), club.getName());
//            CmsClub req = new CmsClub();
//            req.setDistrict("3462");
//            String[] splitStr = club.getName().split("_");
//            req.setName(splitStr[0]);
//            if (StringUtils.isNotBlank(splitStr[1])) {
//                try {
//                    Date date = formatter.parse(splitStr[1]);
//                    req.setRegistrationDate(date);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//            req.setStatus(true);
//            req.setCreateBy("system restAPI");
//            datas.add(req);
//        }
//        for (District_3481 club : District_3481.values()) {
//            System.out.printf("键: %d, 名称: %s%n", club.getKey(), club.getName());
//            CmsClub req = new CmsClub();
//            req.setDistrict("3481");
//            String[] splitStr = club.getName().split("_");
//            req.setName(splitStr[0]);
//            if (StringUtils.isNotBlank(splitStr[1])) {
//                try {
//                    Date date = formatter.parse(splitStr[1]);
//                    req.setRegistrationDate(date);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//            req.setStatus(true);
//            req.setCreateBy("system restAPI");
//            datas.add(req);
//        }
//        for (District_3482 club : District_3482.values()) {
//            System.out.printf("键: %d, 名称: %s%n", club.getKey(), club.getName());
//            CmsClub req = new CmsClub();
//            req.setDistrict("3482");
//            String[] splitStr = club.getName().split("_");
//            req.setName(splitStr[0]);
//            if (StringUtils.isNotBlank(splitStr[1])) {
//                try {
//                    Date date = formatter.parse(splitStr[1]);
//                    req.setRegistrationDate(date);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//            req.setStatus(true);
//            req.setCreateBy("system restAPI");
//            datas.add(req);
//        }
//        for (District_3490 club : District_3490.values()) {
//            System.out.printf("键: %d, 名称: %s%n", club.getKey(), club.getName());
//            CmsClub req = new CmsClub();
//            req.setDistrict("3490");
//            String[] splitStr = club.getName().split("_");
//            req.setName(splitStr[0]);
//            if (StringUtils.isNotBlank(splitStr[1])) {
//                try {
//                    Date date = formatter.parse(splitStr[1]);
//                    req.setRegistrationDate(date);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//            req.setStatus(true);
//            req.setCreateBy("system restAPI");
//            datas.add(req);
//        }
//        for (District_3501 club : District_3501.values()) {
//            System.out.printf("键: %d, 名称: %s%n", club.getKey(), club.getName());
//            CmsClub req = new CmsClub();
//            req.setDistrict("3501");
//            String[] splitStr = club.getName().split("_");
//            req.setName(splitStr[0]);
//            if (StringUtils.isNotBlank(splitStr[1])) {
//                try {
//                    Date date = formatter.parse(splitStr[1]);
//                    req.setRegistrationDate(date);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//            req.setStatus(true);
//            req.setCreateBy("system restAPI");
//            datas.add(req);
//        }
//        for (District_3502 club : District_3502.values()) {
//            System.out.printf("键: %d, 名称: %s%n", club.getKey(), club.getName());
//            CmsClub req = new CmsClub();
//            req.setDistrict("3502");
//            String[] splitStr = club.getName().split("_");
//            req.setName(splitStr[0]);
//            if (StringUtils.isNotBlank(splitStr[1])) {
//                try {
//                    Date date = formatter.parse(splitStr[1]);
//                    req.setRegistrationDate(date);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//            req.setStatus(true);
//            req.setCreateBy("system restAPI");
//            datas.add(req);
//        }
//        for (District_3510 club : District_3510.values()) {
//            System.out.printf("键: %d, 名称: %s%n", club.getKey(), club.getName());
//            CmsClub req = new CmsClub();
//            req.setDistrict("3510");
//            String[] splitStr = club.getName().split("_");
//            req.setName(splitStr[0]);
//            if (StringUtils.isNotBlank(splitStr[1])) {
//                try {
//                    Date date = formatter.parse(splitStr[1]);
//                    req.setRegistrationDate(date);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//            req.setStatus(true);
//            req.setCreateBy("system restAPI");
//            datas.add(req);
//        }
//        for (District_3521 club : District_3521.values()) {
//            System.out.printf("键: %d, 名称: %s%n", club.getKey(), club.getName());
//            CmsClub req = new CmsClub();
//            req.setDistrict("3521");
//            String[] splitStr = club.getName().split("_");
//            req.setName(splitStr[0]);
//            if (StringUtils.isNotBlank(splitStr[1])) {
//                try {
//                    Date date = formatter.parse(splitStr[1]);
//                    req.setRegistrationDate(date);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//            req.setStatus(true);
//            req.setCreateBy("system restAPI");
//            datas.add(req);
//        }
//        for (District_3522 club : District_3522.values()) {
//            System.out.printf("键: %d, 名称: %s%n", club.getKey(), club.getName());
//            CmsClub req = new CmsClub();
//            req.setDistrict("3522");
//            String[] splitStr = club.getName().split("_");
//            req.setName(splitStr[0]);
//            if (StringUtils.isNotBlank(splitStr[1])) {
//                try {
//                    Date date = formatter.parse(splitStr[1]);
//                    req.setRegistrationDate(date);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//            req.setStatus(true);
//            req.setCreateBy("system restAPI");
//            datas.add(req);
//        }
//
//        clubRepository.saveAll(datas);
//    }

}



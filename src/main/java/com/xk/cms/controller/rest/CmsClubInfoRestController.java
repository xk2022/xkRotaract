package com.xk.cms.controller.rest;

import com.xk.cms.dao.repository.CmsClubInfoRepository;
import com.xk.cms.model.bo.CmsClubReq;
import com.xk.cms.model.po.CmsClubInfo;
import com.xk.cms.service.CmsClubService;
import com.xk.common.base.BaseRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 社團信息 RestController
 * @author yuan
 * Created by yuan on 2024/11/20
 */
@RestController
@Api("社團管理api")
@RequestMapping("/api/manage/clubInfo")
public class CmsClubInfoRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsClubInfoRestController.class);

    @Autowired
    private BaseRepository baseRepository;
    @Autowired
    private CmsClubService cmsClubService;
    @Autowired
    private CmsClubInfoRepository cmsClubInfoRepository;

    @ApiOperation(value = "社團列表")
    @GetMapping("/list")
    public Object list() {
        return cmsClubService.list();
    }

    @ApiOperation(value = "社團列表")
    @PostMapping("/list")
    public List list(@RequestBody CmsClubReq resources) {
        return cmsClubService.listBy(resources);
    }

    @ApiOperation(value = "系统列表")
    @GetMapping("/page_head")
    public Object page_head() {
        return baseRepository.queryTableComment("cms_club");
    }


    /**
     * init()
     * 替 CmsClubInfo 建立基本資料欄位名稱
     */
    @GetMapping("/init")
    public String init() {
        List<CmsClubInfo> saveEntities = new ArrayList<>();

        String[] infoKeys = {
                // CmsClubInfoHeader
                "club_logo", "club_name", "organization_district", "service_area", "service_email",
                // CmsClubInfoOverview
                "registration_date", "sponsoring_club", "member_count", "meeting_venue", "contact_number",
                "meeting_schedule", "fax_number", "correspondence_address"
        };

        for (String infoKey : infoKeys) {
            CmsClubInfo saveEntity = new CmsClubInfo();
            saveEntity.setClubId(Long.valueOf("0"));
            saveEntity.setStatus(true);

            saveEntity.setInfoKey(infoKey);
            saveEntities.add(saveEntity);
        }
        cmsClubInfoRepository.saveAll(saveEntities);
        return "OK";
    }

}


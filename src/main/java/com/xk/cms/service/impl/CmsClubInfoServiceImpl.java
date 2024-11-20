package com.xk.cms.service.impl;

import com.xk.cms.dao.repository.CmsClubInfoRepository;
import com.xk.cms.model.bo.CmsClubInfoSaveReq;
import com.xk.cms.model.po.CmsClubInfo;
import com.xk.cms.model.vo.CmsClubInfoResp;
import com.xk.cms.model.vo.CmsClubInfoSaveResp;
import com.xk.cms.service.CmsClubInfoService;
import com.xk.common.util.GenericUpdateService;
import com.xk.common.util.XkBeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

/**
 * Implementation of the CmsClubInfo Service.
 *
 * @author yuan Created on 2024/11/20.
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class CmsClubInfoServiceImpl implements CmsClubInfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsClubInfoServiceImpl.class);

    @Autowired
    private CmsClubInfoRepository cmsClubInfoRepository;

    @Override
    public CmsClubInfoResp getOne(String rotaractId) {
        return null;
    }

    @Override
    public CmsClubInfoSaveResp create(CmsClubInfoSaveReq resources) {
        CmsClubInfo req = XkBeanUtils.copyProperties(resources, CmsClubInfo::new);
        CmsClubInfo savedEntity = cmsClubInfoRepository.save(req);
        return XkBeanUtils.copyProperties(savedEntity, CmsClubInfoSaveResp::new);
    }

    @Override
    public CmsClubInfoSaveResp update(Long id, CmsClubInfoSaveReq resources) {
        // 查找指定 ID 的 CmsDistrict，如果不存在則拋出異常
        CmsClubInfo entity = cmsClubInfoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity<CmsClubInfo> with ID " + id + " not found"));
        // 使用通用更新服務更新實體
        GenericUpdateService<CmsClubInfo> updateService = new GenericUpdateService<>();
        CmsClubInfo updatedEntity = updateService.updateEntity(entity, resources);
        // 保存更新後的實體
        CmsClubInfo savedEntity = cmsClubInfoRepository.save(updatedEntity);
        // 創建並返回 CmsDistrictSaveResp
        return XkBeanUtils.copyProperties(savedEntity, CmsClubInfoSaveResp::new);
    }

//    private List<CmsClubInfo> mapInsert(Map<String, String> dataMap) {
//        List<CmsClubInfo> reslultList = new ArrayList<>();
//
//        CmsClubInfo saveEntitiy = new CmsClubInfo();
//        saveEntitiy.setClubId(null);
//        saveEntitiy.setInfoKey();
//
//
//        for (dataMap) {
//
//        }
//        dataMap.forEach();
//
//        CmsClubInfoHeader header = new CmsClubInfoHeader();
//        header.setClubLogo(dataMap.get("club_logo"));
//        header.setClubName(dataMap.get("club_name"));
//        header.setOrganizationDistrict(dataMap.get("organization_district"));
//        header.setServiceArea(dataMap.get("service_area"));
//        header.setServiceEmail(dataMap.get("service_email"));
//        header.setInfoCompletionScore(Double.valueOf(dataList.size()/initKey.size()));
//
//        CmsClubInfoOverview overview = new CmsClubInfoOverview();
//        overview.setClubName("桃園扶青社");
//        overview.setRegistrationDate("1984-09-21");
//        overview.setSponsoringClub("桃園扶輪社");
//        overview.setMemberCount("35");
//        overview.setMeetingVenue("桃園住都大飯店");
//        overview.setContactNumber("(03)376-5077");
//        overview.setMeetingSchedule("每月第二週、第四週 星期五 晚上");
//        overview.setFaxNumber("(03)335-6346");
//        overview.setCorrespondenceAddress("桃園市桃園區某某路123號");
//    }

}

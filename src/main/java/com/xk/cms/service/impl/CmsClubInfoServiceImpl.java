package com.xk.cms.service.impl;

import com.xk.cms.dao.repository.CmsClubInfoRepository;
import com.xk.cms.dao.repository.CmsClubRepository;
import com.xk.cms.model.bo.CmsClubInfoSaveReq;
import com.xk.cms.model.dto.CmsClubInfoHeader;
import com.xk.cms.model.dto.CmsClubInfoOverview;
import com.xk.cms.model.po.CmsClub;
import com.xk.cms.model.po.CmsClubInfo;
import com.xk.cms.model.vo.CmsClubInfoResp;
import com.xk.cms.model.vo.CmsClubInfoSaveResp;
import com.xk.cms.service.CmsClubInfoService;
import com.xk.common.util.GenericUpdateService;
import com.xk.common.util.XkBeanUtils;
import com.xk.upms.model.vo.UpmsOrganizationResp;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private CmsClubRepository cmsClubRepository;
    @Autowired
    private CmsClubInfoRepository cmsClubInfoRepository;

    @Override
    public CmsClubInfoResp getOne(String rotaractId, UpmsOrganizationResp parentOrg) {
        if (StringUtils.isBlank(rotaractId)) {
            return null;
        }
        List<CmsClubInfo> initKey = cmsClubInfoRepository.findByClubId(Long.valueOf("0"));
        // 使用 Stream API 提取所有的 infoKey
        List<String> keys = initKey.stream().map(CmsClubInfo::getInfoKey) // 提取 infoKey
                .collect(Collectors.toList()); // 收集到 List

        CmsClub ccEntity = cmsClubRepository.findByFkUpmsOrganizationId(Long.valueOf(rotaractId));
        if (ccEntity == null) {
            return null;
        }
        List<CmsClubInfo> dataList = cmsClubInfoRepository.findByClubId(ccEntity.getId());
        Map<String, String> dataMap = dataList.stream()
                .filter(info -> info.getInfoValue() != null) // 过滤掉值为 null 的条目
                .collect(Collectors.toMap(CmsClubInfo::getInfoKey, CmsClubInfo::getInfoValue));

        CmsClubInfoResp result = new CmsClubInfoResp();

        CmsClubInfoHeader header = new CmsClubInfoHeader();
        // 转换 club_logo 为 Base64
        if (StringUtils.isNotBlank(dataMap.get("club_logo"))) {
            // 後續搬移至本地端路徑存放在club_logo
            header.setClubLogo(dataMap.get("club_logo"));
        } else {
            if (ccEntity.getClubLogo() != null) {
                // TODO 快速上傳Blob byte圖片暫存在DB，後續搬移到本地端，加速效能
                String base64Logo = Base64.getEncoder().encodeToString(ccEntity.getClubLogo()); // 将二进制数据编码为 Base64
//                byte[] logoBlob = Base64.getDecoder().decode(ccEntity.getClubLogo()); // 假设数据库中存储的是 Base64 编码的字符串
//                String base64Logo = Base64.getEncoder().encodeToString(logoBlob);
                header.setClubLogoBase64(base64Logo); // 设置为 Base64 字符串
            }
        }
//        header.setClubName(dataMap.get("club_name"));
        header.setClubName(ccEntity.getName());
        header.setOrganizationDistrict(parentOrg.getName());
        header.setServiceArea(dataMap.get("service_area"));
        header.setServiceEmail(dataMap.get("service_email"));
//        header.setInfoCompletionScore(Double.valueOf(dataList.size()/initKey.size()));
//        header.setInfoCompletionScore(((double) dataList.size() / initKey.size())*100);
        header.setInfoCompletionScore(Math.round(((double) dataList.size() / initKey.size()) * 10000) / 100.0);


        CmsClubInfoOverview overview = new CmsClubInfoOverview();
//        overview.setClubName(dataMap.get("club_name"));
        overview.setClubName(ccEntity.getName());
        overview.setRegistrationDate(dataMap.get("registration_date"));
        overview.setSponsoringClub(dataMap.get("sponsoring_club"));
        overview.setMemberCount(dataMap.get("member_count"));
        overview.setMeetingVenue(dataMap.get("meeting_venue"));
        overview.setMeetingSchedule(dataMap.get("meeting_schedule"));
        overview.setContactNumber(dataMap.get("contact_number"));
        overview.setFaxNumber(dataMap.get("fax_number"));
        overview.setCorrespondenceAddress(dataMap.get("correspondence_address"));
        overview.setServiceInstagram(dataMap.get("service_instagram"));
        overview.setServiceLine(dataMap.get("service_line"));

        result.setCmsClubId(String.valueOf(ccEntity.getId()));
        result.setInfoHeader(header);
        result.setInfoOverview(overview);

        System.out.println(result);

        return result;
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

    @Override
    public Boolean saveOverview(CmsClubInfoOverview resources) {
        if (StringUtils.isBlank(resources.getId())) {
            return false;
        }
        Long clubId = Long.valueOf(resources.getId());

        // 准备保存的 key-value 信息
        Map<String, String> overviewData = new HashMap<>();
        overviewData.put("club_name", resources.getClubName());
        overviewData.put("registration_date", resources.getRegistrationDate());
        overviewData.put("sponsoring_club", resources.getSponsoringClub());
        overviewData.put("member_count", resources.getMemberCount());
        overviewData.put("meeting_venue", resources.getMeetingVenue());
        overviewData.put("contact_number", resources.getContactNumber());
        overviewData.put("meeting_schedule", resources.getMeetingSchedule());
        overviewData.put("fax_number", resources.getFaxNumber());
        overviewData.put("correspondence_address", resources.getCorrespondenceAddress());
        overviewData.put("service_area", resources.getServiceArea());
        overviewData.put("service_email", resources.getServiceEmail());
        overviewData.put("service_instagram", resources.getServiceInstagram());
        overviewData.put("service_line", resources.getServiceLine());

        // 遍历并保存/更新每个 key-value
        for (Map.Entry<String, String> entry : overviewData.entrySet()) {
            CmsClubInfo info = cmsClubInfoRepository.findByClubIdAndInfoKey(clubId, entry.getKey())
                    .orElse(new CmsClubInfo());

            info.setClubId(clubId);
            info.setInfoKey(entry.getKey());
            info.setInfoValue(entry.getValue());
            info.setStatus(true);

            cmsClubInfoRepository.save(info);
        }
        return true;
    }
}

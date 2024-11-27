package com.xk.cms.service.impl;

import com.xk.cms.dao.repository.CmsClubInfoRepository;
import com.xk.cms.dao.repository.CmsClubRepository;
import com.xk.cms.model.bo.CmsClubReq;
import com.xk.cms.model.bo.CmsClubSaveReq;
import com.xk.cms.model.dto.CmsClubInfoHeader;
import com.xk.cms.model.dto.CmsClubInfoOverview;
import com.xk.cms.model.po.CmsClub;
import com.xk.cms.model.po.CmsClubInfo;
import com.xk.cms.model.vo.CmsClubResp;
import com.xk.cms.model.vo.CmsClubSaveResp;
import com.xk.cms.service.CmsClubService;
import com.xk.common.util.GenericUpdateService;
import com.xk.common.util.XkBeanUtils;
import com.xk.upms.model.vo.UpmsOrganizationResp;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of the CmsClub Service.
 *
 * @author yuan Created on 2024/09/18.
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class CmsClubServiceImpl implements CmsClubService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsClubServiceImpl.class);

    @Autowired
    private CmsClubRepository cmsClubRepository;
    @Autowired
    private CmsClubInfoRepository cmsClubInfoRepository;

    @Override
    public List list() {
        List<CmsClubResp> resultList = new ArrayList<>();

        List<CmsClub> entities = cmsClubRepository.findAll(Sort.by(Sort.Direction.ASC, "registrationDate"));
        resultList = XkBeanUtils.copyListProperties(entities, CmsClubResp::new);
        return resultList;
    }

    @Override
    public List listBy(CmsClubReq resources) {
        List<CmsClubResp> resultList = new ArrayList<>();

        CmsClub req = new CmsClub();
        BeanUtils.copyProperties(resources, req);
        Example<CmsClub> example = Example.of(req);

        List<CmsClub> entities = cmsClubRepository.findAll(example);
        resultList = XkBeanUtils.copyListProperties(entities, CmsClubResp::new);
        return resultList;
    }

    @Override
    public CmsClubResp getOne(String rotaractId, UpmsOrganizationResp parentOrg) {
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

        CmsClubResp result = new CmsClubResp();

        CmsClubInfoHeader header = new CmsClubInfoHeader();
        header.setClubLogo(dataMap.get("club_logo"));
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

        result.setCmsClubId(String.valueOf(ccEntity.getId()));
        result.setInfoHeader(header);
        result.setInfoOverview(overview);

        System.out.println(result);

        return result;
    }

    @Override
    public CmsClubSaveResp create(CmsClubSaveReq resources) {
        CmsClub req = XkBeanUtils.copyProperties(resources, CmsClub::new);
        CmsClub entity = cmsClubRepository.save(req);
        return XkBeanUtils.copyProperties(entity, CmsClubSaveResp::new);
    }

    @Override
    public CmsClubSaveResp update(Long id, CmsClubSaveReq resources) {
        CmsClubSaveResp result = new CmsClubSaveResp();

        Optional<CmsClub> optional = cmsClubRepository.findById(id);
        if (!optional.isPresent()) {
            throw new EntityNotFoundException("Entity<CmsClub> with ID " + id + " not found");
        }
        CmsClub req = optional.get();

        GenericUpdateService<CmsClub> updateService = new GenericUpdateService<>();
        req = updateService.updateEntity(req, resources);
        CmsClub entity = cmsClubRepository.save(req);

        BeanUtils.copyProperties(entity, result);
        return result;
    }

    @Override
    public void deleteByPrimaryKeys(String ids) {
        String[] idArray = ids.split("-");
        for (String idStr : idArray) {
            Long id = Long.valueOf(idStr);
            cmsClubRepository.deleteById(id);
        }
    }

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

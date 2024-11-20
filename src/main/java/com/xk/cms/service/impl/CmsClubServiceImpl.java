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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    public CmsClubResp getOne(String rotaractId) {
        if (StringUtils.isBlank(rotaractId)) {
            return null;
        }
        List<CmsClubInfo> initKey = cmsClubInfoRepository.findByClubId(Long.valueOf("0"));
        // 使用 Stream API 提取所有的 infoKey
        List<String> keys = initKey.stream().map(CmsClubInfo::getInfoKey) // 提取 infoKey
                .collect(Collectors.toList()); // 收集到 List

        CmsClub entity = cmsClubRepository.findByFkUpmsOrganizationId(Long.valueOf(rotaractId));
        if (entity == null) {
            return null;
        }
        List<CmsClubInfo> dataList = cmsClubInfoRepository.findByClubId(entity.getId());
        Map<String, String> dataMap = dataList.stream()
                .collect(Collectors.toMap(CmsClubInfo::getInfoKey, CmsClubInfo::getInfoValue));

        CmsClubResp result = new CmsClubResp();

        CmsClubInfoHeader header = new CmsClubInfoHeader();
        header.setClubLogo(dataMap.get("club_logo"));
        header.setClubName(dataMap.get("club_name"));
        header.setOrganizationDistrict(dataMap.get("organization_district"));
        header.setServiceArea(dataMap.get("service_area"));
        header.setServiceEmail(dataMap.get("service_email"));
        header.setInfoCompletionScore(Double.valueOf(dataList.size()/initKey.size()));

        CmsClubInfoOverview overview = new CmsClubInfoOverview();
        overview.setClubName("桃園扶青社");
        overview.setRegistrationDate("1984-09-21");
        overview.setSponsoringClub("桃園扶輪社");
        overview.setMemberCount("35");
        overview.setMeetingVenue("桃園住都大飯店");
        overview.setContactNumber("(03)376-5077");
        overview.setMeetingSchedule("每月第二週、第四週 星期五 晚上");
        overview.setFaxNumber("(03)335-6346");
        overview.setCorrespondenceAddress("桃園市桃園區某某路123號");

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

}

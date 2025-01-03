package com.xk.cms.service.impl;

import com.xk.cms.dao.mapper.CmsCalendarMapper;
import com.xk.cms.dao.repository.CmsCalendarRepository;
import com.xk.cms.dao.repository.CmsClubRepository;
import com.xk.cms.model.bo.CmsCalendarReq;
import com.xk.cms.model.bo.CmsCalendarSaveReq;
import com.xk.cms.model.dto.CmsCalendarExample;
import com.xk.cms.model.po.CmsCalendar;
import com.xk.cms.model.vo.CmsCalendarEvoResp;
import com.xk.cms.model.vo.CmsCalendarResp;
import com.xk.cms.model.vo.CmsCalendarSaveResp;
import com.xk.cms.service.CmsCalendarService;
import com.xk.common.util.GenericUpdateService;
import com.xk.common.util.XkBeanUtils;
import com.xk.upms.dao.repository.UpmsOrganizationRepository;
import com.xk.upms.model.vo.UpmsOrganizationResp;
import com.xk.upms.service.UpmsOrganizationService;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the CmsCalendar Service.
 *
 * @author yuan Created on 2024/09/24.
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class CmsCalendarServiceImpl implements CmsCalendarService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsCalendarServiceImpl.class);

    @Autowired
    private CmsCalendarRepository cmsCalendarRepository;
    @Autowired
    private CmsClubRepository cmsClubRepository;
    @Autowired
    private UpmsOrganizationService upmsOrganizationService;
    @Autowired
    private UpmsOrganizationRepository upmsOrganizationRepository;
    @Autowired
    private CmsCalendarMapper cmsCalendarMapper;

    @Override
    public List list(CmsCalendarReq resource) {
        List<CmsCalendar> entities = null;

        CmsCalendar req = new CmsCalendar();
        req.setLocked(false);
        if (StringUtils.isNotBlank(resource.getDistrict_id())) {
            req.setDistrict_id(resource.getDistrict_id());
        }
        if (StringUtils.isNotBlank(resource.getRotaract_id())) {
            req.setDistrict_id(null);
            req.setRotaract_id(resource.getRotaract_id());
        }
        Example<CmsCalendar> example = Example.of(req);

        switch (resource.getAccess_scope()) {
            case "all":
                entities = cmsCalendarRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
                break;
            case "club":
            case "district":
                entities = cmsCalendarRepository.findAll(example, Sort.by(Sort.Direction.ASC, "id"));
                break;
            default:
        }
        return this.modifyContent(entities);
    }

    private List modifyContent(List<CmsCalendar> entities) {
        List<CmsCalendarResp> resultList = new ArrayList<>();

        // 目標格式
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (CmsCalendar entity : entities) {
            CmsCalendarResp temp = new CmsCalendarResp();

            temp.setId(String.valueOf(entity.getId()));
            if (StringUtils.isBlank(entity.getRotaract_id()) || "0".equals(entity.getRotaract_id())) {
                // 地區活動
                UpmsOrganizationResp district = upmsOrganizationService.findById(Long.valueOf(entity.getDistrict_id()));
                if (district != null) {
                    temp.setTitle("[" + district.getName() + "]" + entity.getEventName());
                } else {
                    temp.setTitle(entity.getEventName());
                }
            } else {
                // 社內活動
//                Optional<CmsClub> club = cmsClubRepository.findById(Long.valueOf(entity.getRotaract_id()));
                UpmsOrganizationResp club = upmsOrganizationService.findById(Long.valueOf(entity.getRotaract_id()));
                if (club != null) {
                    temp.setTitle("[" + club.getName() + "]" + entity.getEventName());
                } else {
                    temp.setTitle(entity.getEventName());
                }
            }
            if (StringUtils.isBlank(entity.getStartTime())) {
                temp.setStart(outputFormat.format(entity.getStartDate()));
                temp.setEnd(outputFormat.format(entity.getEndDate()));
            } else {
                temp.setStart(outputFormat.format(entity.getStartDate()) + 'T' + entity.getStartTime());
                temp.setEnd(outputFormat.format(entity.getEndDate()) + 'T' + entity.getEndTime());
            }
            temp.setClassName("fc-event-primary");
            temp.setDescription(entity.getEventDescription());
            temp.setLocation(entity.getEventLocation());

            resultList.add(temp);
        }

        return resultList;
    }

    @Override
    public List listBy(CmsCalendarReq resources) {
        List<CmsCalendarResp> resultList = new ArrayList<>();

        CmsCalendar req = new CmsCalendar();
        BeanUtils.copyProperties(resources, req);
        Example<CmsCalendar> example = Example.of(req);

        List<CmsCalendar> entities = cmsCalendarRepository.findAll(example);
        resultList = XkBeanUtils.copyListProperties(entities, CmsCalendarResp::new);
        return resultList;
    }

    @Override
    public CmsCalendarSaveResp findById(Long id) {
        CmsCalendarSaveResp resp = new CmsCalendarSaveResp();

        Optional<CmsCalendar> optional = cmsCalendarRepository.findById(id);
        if (!optional.isPresent()) {
            throw new EntityNotFoundException("Entity<CmsCalendar> with ID " + id + " not found");
        }
        CmsCalendar entity = optional.get();

        BeanUtils.copyProperties(entity, resp);
        // 定義日期格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // 將 Date 轉換為格式化後的字串
        resp.setStartDateStr(dateFormat.format(resp.getStartDate()));
        resp.setEndDateStr(dateFormat.format(resp.getEndDate()));
        return resp;
    }

    @Override
    public CmsCalendarSaveResp create(CmsCalendarSaveReq resources) {
        CmsCalendarSaveResp result = new CmsCalendarSaveResp();

        CmsCalendar req = new CmsCalendar();
        BeanUtils.copyProperties(resources, req);
        // 定義日期格式
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            req.setStartDate(formatter.parse(resources.getStartDate()));
            req.setEndDate(formatter.parse(resources.getEndDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        req.setLocked(false);
        CmsCalendar entity = cmsCalendarRepository.save(req);

        BeanUtils.copyProperties(entity, result);
        return result;
    }

    @Override
    public CmsCalendarSaveResp update(CmsCalendarSaveReq resources) {
        CmsCalendarSaveResp result = new CmsCalendarSaveResp();

        Optional<CmsCalendar> optional = cmsCalendarRepository.findById(Long.valueOf(resources.getId()));
        if (!optional.isPresent()) {
            throw new EntityNotFoundException("Entity<CmsCalendar> with ID " + resources.getId() + " not found");
        }
        CmsCalendar req = optional.get();

        GenericUpdateService<CmsCalendar> updateService = new GenericUpdateService<>();
        req = updateService.updateEntity(req, resources);
        CmsCalendar entity = cmsCalendarRepository.save(req);

        BeanUtils.copyProperties(entity, result);
        return result;
    }

    @Override
    public void deleteByPrimaryKeys(String ids) {
        String[] idArray = ids.split("-");
        for (String idStr : idArray) {
            Long id = Long.valueOf(idStr);
            cmsCalendarRepository.deleteById(id);
        }
    }

    @Override
    public List<CmsCalendarEvoResp> evoList(CmsCalendarReq req) {
        List<CmsCalendar> entities = cmsCalendarRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        List<CmsCalendarExample> events = XkBeanUtils.copyListProperties(entities, CmsCalendarExample::new);
        return events.stream()
                .map(this::mapToCmsCalendarEvoResp)
                .collect(Collectors.toList());
    }

    @Override
    public List<CmsCalendarEvoResp> getCalendarDataByYear(Integer year) {
        // 設定活動範圍，例如 7/1 當年到 6/30 次年
        LocalDate startDate = LocalDate.of(year, 7, 1);
        LocalDate endDate = LocalDate.of(year + 1, 6, 30);
        // 從資料庫中查詢數據
        List<CmsCalendarExample> events = cmsCalendarMapper.findByDateRange(startDate, endDate);
        // 使用 mapToCmsCalendarEvoResp 方法轉換數據
        return events.stream()
                .map(this::mapToCmsCalendarEvoResp)
                .collect(Collectors.toList());
    }


    private CmsCalendarEvoResp mapToCmsCalendarEvoResp(CmsCalendarExample entity) {
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM/dd/yyyy");
        CmsCalendarEvoResp temp = new CmsCalendarEvoResp();

        temp.setId(String.valueOf(entity.getId()));

        temp.setName(entity.getEventName());
        if (StringUtils.isBlank(entity.getRotaract_id()) || "0".equals(entity.getRotaract_id())) {
            // 地區活動
//            UpmsOrganizationResp district = upmsOrganizationService.findById(Long.valueOf(entity.getDistrict_id()));
            if (StringUtils.isNotBlank(entity.getDistrictName())) {
                temp.setName("[" + entity.getDistrictName() + "]" + entity.getEventName());
            }
        } else {
            // 社內活動
//            UpmsOrganizationResp club = upmsOrganizationService.findById(Long.valueOf(entity.getRotaract_id()));
            if (StringUtils.isNotBlank(entity.getClubName())) {
                temp.setName("[" + entity.getClubName() + "]" + entity.getEventName());
            }
        }

        temp.setDescription(entity.getEventDescription());
        if (StringUtils.isNotBlank(entity.getServiceLine())) {
            temp.setBadge("報名請洽Line ID: " + entity.getServiceLine());
        } else {
            temp.setBadge("歡迎詢問官方IG @rotaractkaleido");
        }
        temp.setDate(outputFormat.format(entity.getStartDate()));

        if (StringUtils.isBlank(entity.getStartTime())) {
            temp.setStartTime("");
        } else {
            temp.setStartTime(entity.getStartTime());
        }

        String type = "0".equals(entity.getType()) ? "birthday" :
                ("1".equals(entity.getType()) ? "event" : "holiday");
        temp.setType(type);
        temp.setEveryYear("!0");

        if (StringUtils.isNotBlank(entity.getOrgCode())) {
            temp.setOrgCodes(entity.getOrgCode().split(","));
        }

        return temp;
    }

}

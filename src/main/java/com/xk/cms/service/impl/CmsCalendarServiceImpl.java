package com.xk.cms.service.impl;

import com.xk.cms.dao.repository.CmsCalendarRepository;
import com.xk.cms.dao.repository.CmsClubRepository;
import com.xk.cms.model.bo.CmsCalendarReq;
import com.xk.cms.model.bo.CmsCalendarSaveReq;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * CmsCalendar ServiceImpl 實現
 * @author yuan
 * Created by yuan on 2024/09/24
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

    @Override
    public List list(CmsCalendarReq resource) {
        List<CmsCalendar> entities = null;

        CmsCalendar req = new CmsCalendar();
        if (StringUtils.isNotBlank(resource.getDistrict_id())) {
            req.setDistrict_id(resource.getDistrict_id());
        }
        if (StringUtils.isNotBlank(resource.getRotaract_id())) {
            req.setDistrict_id(null);
            req.setRotaract_id(resource.getRotaract_id());
        }
        Example<CmsCalendar> example = Example.of(req);

        switch (resource.getCalendar_range()) {
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
            CmsCalendarResp temp =  new CmsCalendarResp();

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
    public List evoList(CmsCalendarReq req) {
        List<CmsCalendarEvoResp> resultList = new ArrayList<>();

        // 目標格式
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM/dd/yyyy");

        List<CmsCalendar> entities = cmsCalendarRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        for (CmsCalendar entity : entities) {
            CmsCalendarEvoResp temp =  new CmsCalendarEvoResp();

            temp.setId(String.valueOf(entity.getId()));
            if (StringUtils.isBlank(entity.getRotaract_id()) || "0".equals(entity.getRotaract_id())) {
                // 地區活動
                UpmsOrganizationResp district = upmsOrganizationService.findById(Long.valueOf(entity.getDistrict_id()));
                if (district != null) {
                    temp.setName("[" + district.getName() + "]" + entity.getEventName());
                } else {
                    temp.setName(entity.getEventName());
                }
            } else {
                // 社內活動
//                Optional<CmsClub> club = cmsClubRepository.findById(Long.valueOf(entity.getRotaract_id()));
                UpmsOrganizationResp club = upmsOrganizationService.findById(Long.valueOf(entity.getRotaract_id()));
                if (club != null) {
                    temp.setName("[" + club.getName() + "]" + entity.getEventName());
                } else {
                    temp.setName(entity.getEventName());
                }
            }
            temp.setDescription(entity.getEventDescription());
            temp.setBadge("測試顯示");
            temp.setDate(outputFormat.format(entity.getStartDate()));
            if (StringUtils.isBlank(entity.getStartTime())) {
                temp.setStartTime("");
//                temp.setEnd(outputFormat.format(entity.getEndDate()));
            } else {
                temp.setStartTime(entity.getStartTime());
//                temp.setEnd(outputFormat.format(entity.getEndDate()) + 'T' + entity.getEndTime());
            }
            String type = "0".equals(entity.getType()) ? "birthday" :
                    ("1".equals(entity.getType()) ? "event" : "holiday");
            temp.setType(type);
            temp.setEveryYear("!0");

            resultList.add(temp);
        }
        return resultList;
    }

}

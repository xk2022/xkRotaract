package com.xk.cms.service.impl;

import com.xk.cms.dao.repository.CmsClubInfoRepository;
import com.xk.cms.dao.repository.CmsClubRepository;
import com.xk.cms.model.bo.CmsClubReq;
import com.xk.cms.model.bo.CmsClubSaveReq;
import com.xk.cms.model.po.CmsClub;
import com.xk.cms.model.vo.CmsClubResp;
import com.xk.cms.model.vo.CmsClubSaveResp;
import com.xk.cms.service.CmsClubService;
import com.xk.common.util.GenericUpdateService;
import com.xk.common.util.XkBeanUtils;
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
import java.util.Optional;

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
    public CmsClubResp selectByOrganizationId(String organizationId) {
        CmsClub ccEntity = cmsClubRepository.findByFkUpmsOrganizationId(Long.valueOf(organizationId));
        return XkBeanUtils.copyProperties(ccEntity, CmsClubResp::new);
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

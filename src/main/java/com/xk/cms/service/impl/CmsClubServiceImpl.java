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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the CmsClub Service.
 *
 * @author yuan Created 2024/09/18
 * @author yuan Updated 2024/12/24 - Optimized for better performance and maintainability
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
    public List<CmsClubResp> list() {
        LOGGER.info("Fetching all clubs sorted by registration date.");
        List<CmsClub> entities = cmsClubRepository.findAll(Sort.by(Sort.Direction.ASC, "registrationDate"));
        LOGGER.info("Fetched {} clubs.", entities.size());
        return XkBeanUtils.copyListProperties(entities, CmsClubResp::new);
    }

    @Override
    public List<CmsClubResp> listBy(CmsClubReq resources) {
        LOGGER.info("Fetching clubs based on criteria: {}", resources);
        CmsClub exampleEntity = XkBeanUtils.copyProperties(resources, CmsClub::new);
        Example<CmsClub> example = Example.of(exampleEntity);
        List<CmsClub> entities = cmsClubRepository.findAll(example);
        LOGGER.info("Found {} clubs matching criteria.", entities.size());
        return XkBeanUtils.copyListProperties(entities, CmsClubResp::new);
    }

    @Override
    public CmsClubResp selectByOrganizationId(String organizationId) {
        LOGGER.info("Fetching club by organization ID: {}", organizationId);
        CmsClub entity = cmsClubRepository.findByFkUpmsOrganizationId(Long.valueOf(organizationId));
        return Optional.ofNullable(entity)
                .map(e -> XkBeanUtils.copyProperties(e, CmsClubResp::new))
                .orElseThrow(() -> new EntityNotFoundException("Club with organization ID " + organizationId + " not found."));
    }

    @Override
    public CmsClubSaveResp create(CmsClubSaveReq resources) {
        LOGGER.info("Creating new club with data: {}", resources);
        CmsClub entity = cmsClubRepository.save(XkBeanUtils.copyProperties(resources, CmsClub::new));
        LOGGER.info("Created club with ID: {}", entity.getId());
        return XkBeanUtils.copyProperties(entity, CmsClubSaveResp::new);
    }

    @Override
    public CmsClubSaveResp update(Long id, CmsClubSaveReq resources) {
        LOGGER.info("Updating club with ID: {}", id);
        CmsClub existingEntity = cmsClubRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Club with ID " + id + " not found."));
        GenericUpdateService<CmsClub> updateService = new GenericUpdateService<>();
        CmsClub updatedEntity = updateService.updateEntity(existingEntity, resources);
        CmsClub savedEntity = cmsClubRepository.save(updatedEntity);
        LOGGER.info("Updated club with ID: {}", savedEntity.getId());
        return XkBeanUtils.copyProperties(savedEntity, CmsClubSaveResp::new);
    }

    @Override
    public void deleteByPrimaryKeys(String ids) {
        LOGGER.info("Deleting clubs with IDs: {}", ids);
        List<Long> idList = parseIds(ids);
        cmsClubRepository.deleteAllByIdInBatch(idList);
        LOGGER.info("Deleted {} clubs.", idList.size());
    }

    @Override
    public void uploadedImage(String id, byte[] bytes) {
        LOGGER.info("Uploading image for club with ID: {}", id);
        CmsClub entity = cmsClubRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new EntityNotFoundException("Club with ID " + id + " not found."));
        entity.setClubLogo(bytes);
        cmsClubRepository.save(entity);
        LOGGER.info("Uploaded image for club with ID: {}", id);
    }

    /**
     * Helper method to parse ID string into a list of Longs.
     */
    private List<Long> parseIds(String ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("Invalid ID string: " + ids);
        }
        return Arrays.stream(ids.split("-"))
                .map(Long::parseLong) // 使用 parseLong 而不是 valueOf
                .collect(Collectors.toList());
    }

}

package com.xk.upms.service.impl;

import com.xk.upms.dao.repository.UpmsLogRepository;
import com.xk.upms.model.dto.UpmsLogExample;
import com.xk.upms.model.po.UpmsLog;
import com.xk.upms.service.UpmsLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * UpmsLogService 實現
 * Created by yuan on 2022/06/10
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UpmsLogServiceImpl implements UpmsLogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpmsLogServiceImpl.class);

    @Autowired
    private UpmsLogRepository upmsLogRepository;

    @Override
    public List list(UpmsLogExample resources) {
        return upmsLogRepository.findAll();
    }

    @Override
    public UpmsLog create(UpmsLogExample resources) {
        UpmsLog entity = upmsLogRepository.save(resources);
        return entity;
    }

    @Override
    public UpmsLog update(Long id, UpmsLogExample resources) {
        UpmsLog entity = upmsLogRepository.save(resources);
        return entity;
    }

    @Override
    public void delete(Long id) {
        upmsLogRepository.deleteById(id);
    }

    @Override
    public void deleteByPrimaryKeys(String ids) {
        String[] idArray = ids.split("-");
        for (String idStr : idArray) {
            Long id = Long.valueOf(idStr);
            upmsLogRepository.deleteById(id);
        }
    }

}

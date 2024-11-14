package com.xk.demo.service.impl;

import com.xk.common.util.GenericUpdateService;
import com.xk.common.util.XkBeanUtils;
import com.xk.demo.dao.mapper.DemoExampleMapper;
import com.xk.demo.dao.repository.DemoExampleRepository;
import com.xk.demo.model.bo.DemoExampleReq;
import com.xk.demo.model.bo.DemoExampleSaveReq;
import com.xk.demo.model.po.DemoExample;
import com.xk.demo.model.vo.DemoExampleResp;
import com.xk.demo.model.vo.DemoExampleSaveResp;
import com.xk.demo.service.DemoExampleService;
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
 * DemoExample ServiceImpl 實現
 * @author yuan
 * Created by yuan on 2022/06/10
 * Update by yuan at 2024/09/18
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class DemoExampleServiceImpl implements DemoExampleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoExampleServiceImpl.class);

    @Autowired
    private DemoExampleRepository demoExampleRepository;
    @Autowired
    private DemoExampleMapper demoExampleMapper;

    @Override
    public List list() {
        List<DemoExampleResp> resultList = new ArrayList<>();

        List<DemoExample> entities = demoExampleRepository.findAll(Sort.by(Sort.Direction.ASC, "orders"));
        resultList = XkBeanUtils.copyListProperties(entities, DemoExampleResp::new);
        return resultList;
    }

    @Override
    public List listBy(DemoExampleReq resources) {
        DemoExample req = XkBeanUtils.copyProperties(resources, DemoExample::new);
        Example<DemoExample> example = Example.of(req);

        List<DemoExample> entities = demoExampleRepository.findAll(example);
        return XkBeanUtils.copyListProperties(entities, DemoExampleResp::new);
    }

    @Override
    public DemoExampleSaveResp create(DemoExampleSaveReq resources) {
        DemoExampleSaveResp result = new DemoExampleSaveResp();

        DemoExample req = new DemoExample();
        BeanUtils.copyProperties(resources, req);
        DemoExample entity = demoExampleRepository.save(req);

        BeanUtils.copyProperties(entity, result);
        return result;
    }

    @Override
    public DemoExampleSaveResp update(Long id, DemoExampleSaveReq resources) {
        DemoExampleSaveResp result = new DemoExampleSaveResp();

        Optional<DemoExample> optional = demoExampleRepository.findById(id);
        if (!optional.isPresent()) {
            throw new EntityNotFoundException("Entity<DemoExample> with ID " + id + " not found");
        }
        DemoExample req = optional.get();

        GenericUpdateService<DemoExample> updateService = new GenericUpdateService<>();
        req = updateService.updateEntity(req, resources);
        DemoExample entity = demoExampleRepository.save(req);

        BeanUtils.copyProperties(entity, result);
        return result;
    }

    @Override
    public void deleteByPrimaryKeys(String ids) {
        String[] idArray = ids.split("-");
        for (String idStr : idArray) {
            Long id = Long.valueOf(idStr);
            demoExampleRepository.deleteById(id);
        }
    }

//    @Override
//    public void deleteById(Long id) {
//        demoExampleRepository.deleteById(id);
//    }

}
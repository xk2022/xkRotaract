package com.xk.demo.service.impl;

import com.xk.demo.dao.repository.DemoExampleRepository;
import com.xk.demo.dao.mapper.DemoExampleMapper;
import com.xk.demo.model.dto.DemoExampleExample;
import com.xk.demo.model.po.DemoExample;
import com.xk.demo.service.DemoExampleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * DemoExampleService 實現
 * Created by yuan on 2022/06/10
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
    public List list(DemoExampleExample resources) {
        return demoExampleRepository.findAll();
    }

    @Override
    public DemoExample create(DemoExampleExample resources) {
        DemoExample entity = demoExampleRepository.save(resources);
        return entity;
    }

    @Override
    public DemoExample update(Long id, DemoExampleExample resources) {
        DemoExample entity = demoExampleRepository.save(resources);
        return entity;
    }

    @Override
    public void deleteById(Long id) {
        demoExampleRepository.deleteById(id);
    }

    @Override
    public DemoExample queryById(Long id) {
        DemoExample entity = demoExampleMapper.queryById(id);
        return entity;
    }
}
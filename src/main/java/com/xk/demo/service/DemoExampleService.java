package com.xk.demo.service;

import com.xk.demo.model.dto.DemoExampleExample;
import com.xk.demo.model.po.DemoExample;

import java.util.List;

/**
 * DemoExampleService 接口
 * Created by yuan on 2022/06/10
 */
public interface DemoExampleService {

    List list(DemoExampleExample resources);

    DemoExample create(DemoExampleExample resources);

    DemoExample update(Long id, DemoExampleExample resources);

    void deleteById(Long id);

    DemoExample queryById(Long id);

}
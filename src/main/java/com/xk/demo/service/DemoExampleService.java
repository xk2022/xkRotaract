package com.xk.demo.service;

import com.xk.demo.model.bo.DemoExampleReq;
import com.xk.demo.model.bo.DemoExampleSaveReq;
import com.xk.demo.model.vo.DemoExampleSaveResp;

import java.util.List;

/**
 * DemoExample Service interface
 * @author yuan
 * Created by yuan on 2022/06/10
 * Update by yuan at 2024/09/18
 */
public interface DemoExampleService {

    List list();

    List listBy(DemoExampleReq resources);

    DemoExampleSaveResp create(DemoExampleSaveReq resources);

    DemoExampleSaveResp update(Long id, DemoExampleSaveReq resources);

    void deleteByPrimaryKeys(String ids);
//    void deleteById(Long id);

}
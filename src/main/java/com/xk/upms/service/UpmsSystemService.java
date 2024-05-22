package com.xk.upms.service;

import com.xk.upms.model.bo.UpmsSystemSaveReq;
import com.xk.upms.model.vo.UpmsSystemSaveResp;

import java.util.List;

/**
 * UpmsSystemService 接口
 * Created by yuan on 2022/06/10
 */
public interface UpmsSystemService {

    List list();

    List listActive();

    UpmsSystemSaveResp create(UpmsSystemSaveReq resources);

    UpmsSystemSaveResp update(Long id, UpmsSystemSaveReq resources);

    void delete(Long id);

    void deleteByPrimaryKeys(String ids);

    Object findOneByName(String name);

}

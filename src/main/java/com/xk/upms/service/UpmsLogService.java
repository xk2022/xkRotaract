package com.xk.upms.service;

import com.xk.upms.model.dto.UpmsLogExample;
import com.xk.upms.model.po.UpmsLog;

import java.util.List;

/**
 * UpmsLogService 接口
 * Created by yuan on 2022/06/10
 */
public interface UpmsLogService {

    List list(UpmsLogExample resources);

    UpmsLog create(UpmsLogExample resources);

    UpmsLog update(Long id, UpmsLogExample resources);

    void delete(Long id);

    void deleteByPrimaryKeys(String ids);

}

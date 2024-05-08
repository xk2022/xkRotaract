package com.xk.upms.service.impl;

import com.xk.upms.service.UpmsUserPermissionService;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UpmsUserPermissionService 實現
 * Created by yuan on 2022/06/24
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UpmsUserPermissionServiceImpl implements UpmsUserPermissionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpmsUserPermissionServiceImpl.class);

    @Override
    public void permission(JSONArray datas, long id) {

    }

}

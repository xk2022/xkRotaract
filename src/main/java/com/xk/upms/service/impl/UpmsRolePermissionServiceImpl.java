package com.xk.upms.service.impl;

import com.xk.upms.service.UpmsRolePermissionService;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UpmsRolePermissionService 實現
 * Created by yuan on 2022/06/24
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UpmsRolePermissionServiceImpl implements UpmsRolePermissionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpmsRolePermissionServiceImpl.class);

    @Override
    public int rolePermission(JSONArray datas, long id) {
        return 0;
    }

}

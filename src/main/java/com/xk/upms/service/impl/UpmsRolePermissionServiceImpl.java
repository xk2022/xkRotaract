package com.xk.upms.service.impl;

import com.xk.upms.dao.repository.UpmsPermissionRepository;
import com.xk.upms.dao.repository.UpmsRolePermissionRepository;
import com.xk.upms.model.po.UpmsPermission;
import com.xk.upms.model.po.UpmsRolePermission;
import com.xk.upms.service.UpmsRolePermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * UpmsRolePermissionService 實現
 * Created by yuan on 2022/06/24
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UpmsRolePermissionServiceImpl implements UpmsRolePermissionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpmsRolePermissionServiceImpl.class);

    @Autowired
    private UpmsRolePermissionRepository upmsRolePermissionRepository;
    @Autowired
    private UpmsPermissionRepository upmsPermissionRepository;

    @Override
    public List rolePermission(String[] datas, long id) {
        List<UpmsRolePermission> result = new ArrayList<>();
        // 將此角色權限歸零
//        upmsRolePermissionRepository.updateActiveByRoleId(id);
        upmsRolePermissionRepository.deleteByRoleId(id);

        List<UpmsRolePermission> entities = new ArrayList<>();
        for(String permission : datas) {
            UpmsRolePermission addEntity = new UpmsRolePermission();
            addEntity.setRoleId(id);
            addEntity.setActive(true);
            String[] param = permission.split("_");
            addEntity.setPermissionId(Long.valueOf(param[0]));
            addEntity.setAction("write");
            entities.add(addEntity);

            /** TODO
             *  暫時排除例外
              */

            List<UpmsPermission> permissions = upmsPermissionRepository.findByPid(Long.valueOf(param[0]));
            if (permissions.size() > 0 ) {
                for (UpmsPermission childP : permissions) {
                    UpmsRolePermission addSubEntity = new UpmsRolePermission();
                    BeanUtils.copyProperties(addEntity, addSubEntity);
                    addSubEntity.setPermissionId(childP.getId());
                    entities.add(addSubEntity);
                }
            }
        }
        result = upmsRolePermissionRepository.saveAll(entities);

        return result;
    }

    private void childRolePermission(Long pid) {

    }

}

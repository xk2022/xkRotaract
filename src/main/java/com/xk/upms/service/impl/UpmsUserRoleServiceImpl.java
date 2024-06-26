package com.xk.upms.service.impl;

import com.xk.upms.dao.mapper.UpmsUserMapper;
import com.xk.upms.dao.repository.UpmsUserRoleRepository;
import com.xk.upms.model.dto.UpmsUserRoleExample;
import com.xk.upms.model.po.UpmsUserRole;
import com.xk.upms.service.UpmsUserRoleService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * UpmsUserRoleService 實現
 * Created by yuan on 2024/05/06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UpmsUserRoleServiceImpl implements UpmsUserRoleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpmsUserRoleServiceImpl.class);

    @Autowired
    private UpmsUserRoleRepository upmsUserRoleRepository;
    @Autowired
    private UpmsUserMapper upmsUserMapper;

    @Override
    public int role(long userId, String roleId) {
        int result = 0;
        // 删除旧记录
        upmsUserRoleRepository.deleteByUserId(userId);
        // 增加新记录

        if (StringUtils.isBlank(roleId)) {
            return 0;
        }
        UpmsUserRole entity = new UpmsUserRole();
        entity.setUserId(userId);
        entity.setRoleId(NumberUtils.toLong(roleId));
        upmsUserRoleRepository.save(entity);
        return 1;
    }

    @Override
    public int role(long userId, String[] roleIds) {
        int result = 0;
        // 删除旧记录
        upmsUserRoleRepository.deleteByUserId(userId);
        // 增加新记录
        if (null != roleIds) {
            for (String roleId : roleIds) {
                if (StringUtils.isBlank(roleId)) {
                    continue;
                }
                UpmsUserRole entity = new UpmsUserRole();
                entity.setUserId(userId);
                entity.setRoleId(NumberUtils.toLong(roleId));
                upmsUserRoleRepository.save(entity);
                result++;
            }
        }
        return result;
    }

    @Override
    public List<UpmsUserRoleExample> getUsers(long roleId) {
        return upmsUserMapper.selectUsersByRole(roleId);
    }

}
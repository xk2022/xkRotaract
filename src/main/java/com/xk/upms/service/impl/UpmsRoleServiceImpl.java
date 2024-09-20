package com.xk.upms.service.impl;

import com.xk.common.util.GenericUpdateService;
import com.xk.upms.dao.repository.UpmsRolePermissionRepository;
import com.xk.upms.dao.repository.UpmsRoleRepository;
import com.xk.upms.dao.repository.UpmsUserRoleRepository;
import com.xk.upms.model.bo.UpmsRoleSaveReq;
import com.xk.upms.model.po.UpmsRole;
import com.xk.upms.model.vo.UpmsRoleResp;
import com.xk.upms.model.vo.UpmsRoleSaveResp;
import com.xk.upms.service.UpmsRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * UpmsRoleService 實現
 * @author yuan
 * Created by yuan on 2022/06/24
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class UpmsRoleServiceImpl implements UpmsRoleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpmsRoleServiceImpl.class);

    @Autowired
    private UpmsRoleRepository upmsRoleRepository;
    @Autowired
    private UpmsUserRoleRepository upmsUserRoleRepository;
    @Autowired
    private UpmsRolePermissionRepository upmsRolePermissionRepository;

    @Override
    public List list() {
        List<UpmsRoleResp> resultList = new ArrayList<>();

        List<UpmsRole> entities = upmsRoleRepository.findAll();
        if (entities != null) {
            for (UpmsRole entity : entities) {
                UpmsRoleResp resp = new UpmsRoleResp();
                Long cntUser = upmsUserRoleRepository.countByRoleId(entity.getId());

                BeanUtils.copyProperties(entity, resp);
                resp.setCountUser(cntUser);
                resultList.add(resp);
            }
        }
        return resultList;
    }

    @Override
    public UpmsRoleSaveResp create(UpmsRoleSaveReq resources) {
        UpmsRoleSaveResp result = new UpmsRoleSaveResp();

        UpmsRole req = new UpmsRole();
        BeanUtils.copyProperties(resources, req);
        UpmsRole entity = upmsRoleRepository.save(req);

        BeanUtils.copyProperties(entity, result);
        return result;
    }

    @Override
    public UpmsRoleSaveResp update(Long id, UpmsRoleSaveReq resources) {
        UpmsRoleSaveResp result = new UpmsRoleSaveResp();

        Optional<UpmsRole> optional = upmsRoleRepository.findById(id);
        if (!optional.isPresent()) {
            // 根据需求处理找不到实体的情况，例如抛出异常
            throw new EntityNotFoundException("Role with ID " + id + " not found");
        }
        UpmsRole req = optional.get();

        // 通用更新实体方法，仅更新 resources 中非 null 的字段
        GenericUpdateService<UpmsRole> updateService = new GenericUpdateService<>();
        req = updateService.updateEntity(req, resources);
        // 保存实体
        UpmsRole entity = upmsRoleRepository.save(req);
        // 将更新后的实体转换为响应对象
        BeanUtils.copyProperties(entity, result);
        return result;
    }

    @Override
    public void delete(Long id) {
        upmsRoleRepository.deleteById(id);
    }

    @Override
    public void deleteByPrimaryKeys(String ids) {
        String[] idArray = ids.split("-");
        for (String idStr : idArray) {
            Long id = Long.valueOf(idStr);
            upmsRoleRepository.deleteById(id);
        }
    }

    @Override
    public Optional<UpmsRole> selectByPrimaryKey(long id) {
        return upmsRoleRepository.findById(id);
    }

    @Override
    public UpmsRoleResp selectByCode(String code) {
        UpmsRoleResp resp = new UpmsRoleResp();

        UpmsRole entity = upmsRoleRepository.findByCode(code);
        BeanUtils.copyProperties(entity, resp);
        return resp;
    }

    @Override
    public UpmsRoleResp findById(Long id) {
        UpmsRoleResp resp = new UpmsRoleResp();

        UpmsRole entity = upmsRoleRepository.getOne(id);

        Long cntUser = upmsUserRoleRepository.countByRoleId(entity.getId());
        BeanUtils.copyProperties(entity, resp);
        resp.setCountUser(cntUser);

        return resp;
    }

}

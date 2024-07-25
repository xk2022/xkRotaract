package com.xk.upms.service.impl;

import com.xk.common.util.NotFoundException;
import com.xk.upms.dao.repository.UpmsPermissionRepository;
import com.xk.upms.dao.repository.UpmsRolePermissionRepository;
import com.xk.upms.model.bo.UpmsPermissionReq;
import com.xk.upms.model.bo.UpmsPermissionSaveReq;
import com.xk.upms.model.po.UpmsPermission;
import com.xk.upms.model.po.UpmsSystem;
import com.xk.upms.model.vo.UpmsPermissionResp;
import com.xk.upms.model.vo.UpmsPermissionSaveResp;
import com.xk.upms.model.vo.UpmsPermissionTreeResp;
import com.xk.upms.service.UpmsPermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * UpmsPermissionService 實現
 * Created by yuan on 2022/06/24
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UpmsPermissionServiceImpl implements UpmsPermissionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpmsPermissionServiceImpl.class);

    @Autowired
    private UpmsPermissionRepository upmsPermissionRepository;

    @Autowired
    private UpmsRolePermissionRepository upmsRolePermissionRepository;

    @Override
    public List list() {
        List<UpmsPermissionResp> result = new ArrayList<>();

        List<UpmsPermission> entities = upmsPermissionRepository.findAll();

        for (UpmsPermission entity : entities) {
            UpmsPermissionResp temp  = new UpmsPermissionResp();
            BeanUtils.copyProperties(entity, temp);
            result.add(temp);
        }
        return result;
    }

    @Override
    public List listBy(UpmsPermissionReq resources) {
        List<UpmsPermissionResp> result = new ArrayList<>();

        UpmsPermission req = new UpmsPermission();
        BeanUtils.copyProperties(resources, req);
        Example<UpmsPermission> example = Example.of(req);

        List<UpmsPermission> entities = upmsPermissionRepository.findAll(example);

        for (UpmsPermission entity : entities) {
            UpmsPermissionResp temp  = new UpmsPermissionResp();
            BeanUtils.copyProperties(entity, temp);
            result.add(temp);
        }
        return result;
    }

    @Override
    public UpmsPermissionSaveResp create(UpmsPermissionSaveReq resources) {
        UpmsPermissionSaveResp result = new UpmsPermissionSaveResp();

        UpmsPermission req = new UpmsPermission();
        BeanUtils.copyProperties(resources, req);

        UpmsPermission checkPermission = new UpmsPermission();
        checkPermission = upmsPermissionRepository.findByPermissionValue(req.getPermissionValue());
        if (null != checkPermission) {
            throw new NotFoundException("權限已存在！");
        }

        req.setType((req.getPid() == null) ? (byte) 1 : (byte) 2);
        long time = System.currentTimeMillis();
        req.setOrders(time);
        req.setCreateTime(new Date());
        UpmsPermission entity = upmsPermissionRepository.save(req);

        BeanUtils.copyProperties(entity, result);
        return result;
    }

    @Override
    public UpmsPermissionSaveResp update(Long id, UpmsPermissionSaveReq resources) {
        UpmsPermissionSaveResp result = new UpmsPermissionSaveResp();

        UpmsPermission req = new UpmsPermission();
        BeanUtils.copyProperties(resources, req);
        UpmsPermission entity = upmsPermissionRepository.save(req);

        BeanUtils.copyProperties(entity, result);
        return result;
    }

    @Override
    public void delete(Long id) {
        upmsPermissionRepository.deleteById(id);
    }

    @Override
    public void deleteByPrimaryKeys(String ids) {
        String[] idArray = ids.split("-");
        for (String idStr : idArray) {
            Long id = Long.valueOf(idStr);
            upmsPermissionRepository.deleteById(id);
        }
    }

    @Override
    public Object getTreeByRoleId(long id) {
        return null;
    }

    @Override
    public Object getTreeByUserId(long id, byte type) {
        return null;
    }

    @Override
    public List selectBySystemIdAndRole(UpmsSystem system, long roleId) {
        if (system == null) {
            return null;
        }
        Long systemId = system.getId();

//        // 角色已有權限
//        List<UpmsRolePermission> rolePermissions = upmsRolePermissionRepository.findByRoleId(roleId);
//        // 抽出所有 permissionId
//        List<Long> permissionIds = new ArrayList<>();
//        // 查詢所有
//        return upmsPermissionRepository.findBySystemIdAndIdIn(systemId, permissionIds);
        return upmsPermissionRepository.findBySystemIdAndStatus(systemId, true);
    }

    @Override
    public List buildTree(List<UpmsPermission> permissions) {
        if (permissions == null) {
            return null;
        }
        List<UpmsPermissionTreeResp> trees = new ArrayList<>();
        for (UpmsPermission permission : permissions) {
            if (!Byte.valueOf("1").equals(permission.getType())) {
                continue;
            }
            // catalog 目錄
            UpmsPermissionTreeResp catalog = new UpmsPermissionTreeResp();
            BeanUtils.copyProperties(permission, catalog);
            catalog.setOpen(true);
            if (true) {
                catalog.setChecked(true);
            }

            catalog = recursively(catalog, permissions);
            trees.add(catalog);
        }
        if (trees.isEmpty()) {
            for (UpmsPermission permission : permissions) {
                if (!Byte.valueOf("2").equals(permission.getType())) {
                    continue;
                }
                // menu 菜單
                UpmsPermissionTreeResp menu = new UpmsPermissionTreeResp();
                BeanUtils.copyProperties(permission, menu);
                menu.setOpen(true);
                if (true) {
                    menu.setChecked(true);
                }

                menu = recursively(menu, permissions);
                trees.add(menu);
            }
        }
        return trees;
    }

    @Override
    public List<UpmsPermission> findAllMenuLevel() {
        return upmsPermissionRepository.findByType((byte) 2);
    }

    @Override
    public UpmsPermission findOneByUri(String uri) {
        return upmsPermissionRepository.findByUri(uri);
    }

    @Override
    public List<UpmsPermission> findBreadcrumbUri(String uri) {
        List<UpmsPermission> breadcrumb = new ArrayList<>();
        UpmsPermission lastBreadcrumb = upmsPermissionRepository.findByUri(uri);
        if (lastBreadcrumb != null && lastBreadcrumb.getPid() != null) {
            Optional<UpmsPermission> secBreadcrumb = upmsPermissionRepository.findById(lastBreadcrumb.getPid());
            breadcrumb.add(secBreadcrumb.get());
        }
        breadcrumb.add(lastBreadcrumb);
        return breadcrumb;
    }

    /**
     * 遞迴迭代、剝洋蔥
     */
    private UpmsPermissionTreeResp recursively(UpmsPermissionTreeResp parent, List<UpmsPermission> menus) {
        for (UpmsPermission subMenu : menus) {
            if (parent.getId().equals(subMenu.getPid())) {
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<>());
                }

                UpmsPermissionTreeResp self = new UpmsPermissionTreeResp();
                BeanUtils.copyProperties(subMenu, self);
                self.setOpen(true);
                if (true) {
                    self.setChecked(true);
                }

                self = recursively(self, menus);
                parent.getChildren().add(self);
            }
        }
        return parent;
    }

}

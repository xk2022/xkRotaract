package com.xk.admin.service.impl;


import com.xk.admin.model.dto.UserExample;
import com.xk.admin.service.AuthService;
import com.xk.cms.dao.repository.CmsUserRepository;
import com.xk.cms.model.po.CmsUser;
import com.xk.common.util.NotFoundException;
import com.xk.upms.dao.repository.*;
import com.xk.upms.model.enums.PermissionAction;
import com.xk.upms.model.po.*;
import com.xk.upms.model.vo.UpmsUserSaveResp;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by yuan on 2022/05/30
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private HttpServletRequest request;
    /**
     * User
     */
    @Autowired
    private UpmsUserRepository upmsUserRepository;
    @Autowired
    private CmsUserRepository cmsUserRepository;
    @Autowired
    private UpmsUserRoleRepository upmsUserRoleRepository;
    /**
     * Permission
     */
    @Autowired
    private UpmsRolePermissionRepository upmsRolePermissionRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private UpmsSystemRepository upmsSystemRepository;
    @Autowired
    private UpmsPermissionRepository upmsPermissionRepository;

    @Override
    public UserExample checkUser(String account, String password) {
        UserExample result = new UserExample();
        // 改由 js 前端md5加密
//        T_User user = userRepository.findByUsernameAndPassword(username, MD5Utils.code(password));
        // account as email

        UpmsUser uuEntity = upmsUserRepository.findByUsernameOrEmailOrCellPhone(account, account, account);
        if (uuEntity == null) {
//            throw new NotFoundException("查無使用者");
            return null;
        }
        UpmsUser chkPass = upmsUserRepository.findByUsernameAndPassword(uuEntity.getUsername(), password);
        if (chkPass == null) {
//            throw new NotFoundException("使用者密碼錯誤");
            return null;
        }

        // user info update by user
        CmsUser cuEntity = cmsUserRepository.findOneByFkUpmsUserId(uuEntity.getId());
        if (cuEntity != null) {
            BeanUtils.copyProperties(cuEntity, result);
        }
        // user role
        List<UpmsUserRole> uurList = upmsUserRoleRepository.findByUserId(uuEntity.getId());
        if (uurList == null) {
            throw new NotFoundException("查無使用者對應角色");
        }
        List<Long> list = new ArrayList<>();
        for(UpmsUserRole temp : uurList) {
            list.add(temp.getRoleId());
        }
        long[] roleIds = list.stream().mapToLong(Long::longValue).toArray();
        result.setRoleId(roleIds);

        // 確認完登入資訊，回填最後登入時間
        uuEntity.setLastLogin(new Date());
        uuEntity = upmsUserRepository.save(uuEntity);
        BeanUtils.copyProperties(uuEntity, result);
        return result;
    }

    @Override
    public Boolean checkUser(String email) {
        UpmsUser user = upmsUserRepository.findByEmail(email);
        return (user != null);
    }

    @Override
    public UpmsUser resetPassword(String email, String password) {
        UpmsUserSaveResp result = new UpmsUserSaveResp();

        UpmsUser user = upmsUserRepository.findByEmail(email);
        user.setPassword(password);
        UpmsUser entity = upmsUserRepository.save(user);

        BeanUtils.copyProperties(entity, result);
        return result;
    }

    /**
     * aside_system
     */
    @Override
    public List listSystemByAuth() {
        Set<Long> permissionIds = (Set<Long>) userPermissions();
        // 查询所有權限
        List<UpmsPermission> permissions = upmsPermissionRepository.findByStatusAndIdInOrPidIn(true, new ArrayList<>(permissionIds), new ArrayList<>(permissionIds));

        // 抽出所有 systemId
        List<Long> list = new ArrayList<>();
        for(UpmsPermission temp : permissions) {
            list.add(temp.getSystemId());
        }
        // 去重複
        List<Long> systemIds = list.stream().distinct().collect(Collectors.toList());

        // 查詢所有
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UpmsSystem> query = criteriaBuilder.createQuery(UpmsSystem.class);
        Root<UpmsSystem> root = query.from(UpmsSystem.class);
        // 构建查询条件，限制返回结果的 id 在给定的 id 列表中
        Predicate idPredicate = root.get("id").in(systemIds);
        Predicate statusPredicate = criteriaBuilder.isTrue(root.get("status")); // status 为 true 的条件
        Predicate finalPredicate = criteriaBuilder.and(idPredicate, statusPredicate);
        // 添加排序
        query.orderBy(criteriaBuilder.asc(root.get("orders")));

        query.select(root).where(finalPredicate);
        // 根据权限获取系统列表
//        List<UpmsSystem> systems = upmsSystemRepository.findByIdInAndStatusOrderByOrders(permissionIds, true);

        return entityManager.createQuery(query).getResultList();
    }

    /**
     * left_tree
     */
    @Override
    public List listPermission(Long systemId, Byte type) {
        // 获取用户权限 ID 集合
        Set<Long> permissionIds = (Set<Long>) userPermissions(); // 确保 userPermissions() 返回的是 Set<Long>

        // 查找所有活动的权限
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<UpmsPermission> query = cb.createQuery(UpmsPermission.class);
        Root<UpmsPermission> root = query.from(UpmsPermission.class);

        List<Predicate> predicates = new ArrayList<>();
        if (systemId != null) {
            predicates.add(cb.equal(root.get("systemId"), systemId));
        }
        predicates.add(cb.equal(root.get("status"), true));

        if (type != null) {
            predicates.add(cb.equal(root.get("type"), type));
        }

        query.where(predicates.toArray(new Predicate[0]));
        List<UpmsPermission> allActivePermissions = entityManager.createQuery(query).getResultList();

        // 从所有活动权限中筛选出用户拥有的权限
        List<UpmsPermission> activePermissions = allActivePermissions.stream()
                .filter(permission -> permissionIds.contains(permission.getId()))
                .collect(Collectors.toList());

        return activePermissions;
    }

    private Collection<? extends Serializable> userPermissions() {
        // 从 HttpSession 中获取用户信息
        HttpSession session = request.getSession();
        UserExample user = (UserExample) session.getAttribute("user");

        // 从用户角色中获取权限
        Set<Long> permissionIds = new HashSet<>();
        for (long roleId : user.getRoleId()) {
            List<UpmsRolePermission> rps = upmsRolePermissionRepository.findByRoleIdAndActionAndActive(roleId, PermissionAction.READ, true);
            for (UpmsRolePermission rp : rps) {
                permissionIds.add(rp.getPermissionId());
            }
        }
        return permissionIds;
//        List<Long> permissionIds = list.stream().distinct().collect(Collectors.toList());
        // 查询所有权限
//        List<UpmsPermission> permissions = upmsPermissionRepository.findByStatusAndIdInOrPidIn(true, new ArrayList<>(permissionIds), new ArrayList<>(permissionIds));
//
//        return permissions;
    }




}

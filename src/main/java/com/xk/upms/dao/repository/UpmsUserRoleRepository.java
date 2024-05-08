package com.xk.upms.dao.repository;

import com.xk.upms.model.po.UpmsUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface UpmsUserRoleRepository extends JpaRepository<UpmsUserRole, Long>, Serializable {

    void deleteByUserId(long userId);

    // 統計具有特定 roleId 的用戶數量
    Long countByRoleId(Long roleId);
}

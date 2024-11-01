package com.xk.upms.dao.repository;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xk.upms.model.po.UpmsUserRole;

public interface UpmsUserRoleRepository extends JpaRepository<UpmsUserRole, Long>, Serializable {

    void deleteByUserId(Long userId);

    // 統計具有特定 roleId 的用戶數量
    Long countByRoleId(Long roleId);

//    List<UpmsUserRole> findByUserId(Long userId);
    
    Optional<UpmsUserRole>findByUserId(Long userIde);
}

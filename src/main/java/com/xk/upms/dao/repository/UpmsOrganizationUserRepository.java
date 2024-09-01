package com.xk.upms.dao.repository;

import com.xk.upms.model.po.UpmsUserOrganization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface UpmsOrganizationUserRepository extends JpaRepository<UpmsUserOrganization, Long>, Serializable {

    // 統計具有特定 roleId 的用戶數量
    Long countByOrganizationId(Long organizationId);

}
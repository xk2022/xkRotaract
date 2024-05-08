package com.xk.upms.dao.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xk.upms.model.po.UpmsRolePermission;

public interface UpmsRolePermissionRepository extends JpaRepository<UpmsRolePermission, Long>, Serializable {

	List findByRoleId(long roleId);

}

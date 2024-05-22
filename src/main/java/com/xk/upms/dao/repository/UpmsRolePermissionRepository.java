package com.xk.upms.dao.repository;

import com.xk.upms.model.po.UpmsRolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

public interface UpmsRolePermissionRepository extends JpaRepository<UpmsRolePermission, Long>, Serializable {

	List findByRoleId(long roleId);

	List findByRoleIdAndAction(long roleId, String action);

	@Transactional
	@Modifying
	@Query("UPDATE UpmsRolePermission SET active = false WHERE roleId = :roleId")
	void updateActiveByRoleId(Long roleId);

	@Transactional
	void deleteByRoleId(Long roleId);

}

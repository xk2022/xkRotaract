package com.xk.upms.dao.repository;

import com.xk.upms.model.enums.PermissionAction;
import com.xk.upms.model.po.UpmsRolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

public interface UpmsRolePermissionRepository extends JpaRepository<UpmsRolePermission, Long>, Serializable {

	List findByRoleId(long roleId);

	List findByRoleIdAndActive(long roleId, boolean active);

	UpmsRolePermission findByRoleIdAndPermissionId(long roleId, long permissionId);

	List findByRoleIdAndActionAndActive(long roleId, PermissionAction action, boolean active);

	@Transactional
	@Modifying
	@Query("UPDATE UpmsRolePermission SET active = false WHERE roleId = :roleId")
	void updateActiveByRoleId(Long roleId);

	@Transactional
	void deleteByRoleId(Long roleId);

	@Query(value = "SELECT permission_id FROM upms_role_permission WHERE role_id = :roleId GROUP BY permission_id", nativeQuery = true)
	List<Long> findPermissionIdsByRoleId(@Param("roleId") Long roleId);

}

package com.xk.upms.dao.repository;

import com.xk.upms.model.po.UpmsPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

public interface UpmsPermissionRepository extends JpaRepository<UpmsPermission, Long>, Serializable {

    UpmsPermission findByPermissionValue(String permissionValue);

    List findBySystemId(Long systemId);

    List findBySystemIdAndIdIn(Long systemId, List<Long> ids);

    List findByType(Byte type);

    UpmsPermission findByUri(String uri);
}

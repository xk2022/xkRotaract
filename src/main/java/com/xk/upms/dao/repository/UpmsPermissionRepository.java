package com.xk.upms.dao.repository;

import com.xk.upms.model.po.UpmsPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

public interface UpmsPermissionRepository extends JpaRepository<UpmsPermission, Long>, Serializable {

    UpmsPermission findByPermissionValue(String permissionValue);

    List findBySystemId(Long systemId);

    List findBySystemIdAndStatus(Long systemId, Boolean status);
    List findBySystemIdAndStatusAndType(Long systemId, Boolean status, Byte type);
    List findByStatusAndType(Boolean status, Byte type);

    List findBySystemIdAndStatusTrueAndIdInOrPidIn(Long systemId, List<Long> ids, List<Long> pids);
    List findByStatusAndIdInOrPidIn(Boolean status, List<Long> ids, List<Long> pids);

    List findByType(Byte type);

    UpmsPermission findByUri(String uri);

    List findByPid(Long pid);

    List findByIdNotInAndTypeAndSystemId(List<Long> ids, Byte type, Long systemId);
}

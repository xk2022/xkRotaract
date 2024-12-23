package com.xk.upms.dao.repository;

import com.xk.upms.model.po.UpmsRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

public interface UpmsRoleRepository extends JpaRepository<UpmsRole, Long>, Serializable {

    UpmsRole findByCode(String code);

    List<UpmsRole> findByCodeStartingWithOrderByOrdersAsc(String prefix);

}

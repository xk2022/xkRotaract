package com.xk.upms.dao.repository;

import com.xk.upms.model.po.UpmsOrganization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

public interface UpmsOrganizationRepository extends JpaRepository<UpmsOrganization, Long>, Serializable {

    UpmsOrganization findByCode(String code);
    // 使用命名規範的方法
    List<UpmsOrganization> findByLevelNotIn(List<Integer> levels);

}
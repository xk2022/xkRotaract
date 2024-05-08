package com.xk.upms.dao.repository;

import com.xk.upms.model.po.UpmsOrganization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface UpmsOrganizationRepository extends JpaRepository<UpmsOrganization, Long>, Serializable {

}
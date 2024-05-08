package com.xk.upms.dao.repository;

import com.xk.upms.model.po.UpmsLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface UpmsLogRepository extends JpaRepository<UpmsLog, Long>, Serializable {

}

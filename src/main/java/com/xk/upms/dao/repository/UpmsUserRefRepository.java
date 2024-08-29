package com.xk.upms.dao.repository;

import com.xk.upms.model.po.UpmsUserRef;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface UpmsUserRefRepository extends JpaRepository<UpmsUserRef, Long>, Serializable {

}

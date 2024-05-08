package com.xk.upms.dao.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xk.upms.model.po.UpmsSystem;

public interface UpmsSystemRepository extends JpaRepository<UpmsSystem, Long>, Serializable {

	UpmsSystem findOneByName(String name);

}

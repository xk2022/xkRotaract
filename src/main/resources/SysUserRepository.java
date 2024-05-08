package com.xk.demo.dao.repository;

import com.xk.demo.model.po.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface SysUserRepository extends JpaRepository<SysUser, Long>, Serializable {

}


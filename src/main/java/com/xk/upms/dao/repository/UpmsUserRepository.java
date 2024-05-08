package com.xk.upms.dao.repository;

import com.xk.upms.model.po.UpmsUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface UpmsUserRepository extends JpaRepository<UpmsUser, Long>, Serializable {

    UpmsUser findByUsername(String name);

    UpmsUser findByEmailAndPassword(String email, String code);

    UpmsUser findByEmail(String email);
}

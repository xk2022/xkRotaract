package com.xk.upms.dao.repository;

import com.xk.upms.model.po.UpmsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.util.List;

public interface UpmsUserRepository extends JpaRepository<UpmsUser, Long>, Serializable {

    UpmsUser findByUsername(String name);

    UpmsUser findByEmailAndPassword(String email, String code);
    UpmsUser findByCellPhoneAndPassword(String cellPhone, String code);

    UpmsUser findByEmail(String email);

    @Query("SELECT u FROM UpmsUser u WHERE u.cellPhone LIKE %:referralCode%")
    List<UpmsUser> findByCellPhoneLike(@Param("referralCode") String referralCode);
}

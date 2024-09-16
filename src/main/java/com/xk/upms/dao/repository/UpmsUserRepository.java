package com.xk.upms.dao.repository;

import com.xk.upms.model.po.UpmsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.util.List;

public interface UpmsUserRepository extends JpaRepository<UpmsUser, Long>, Serializable {

    UpmsUser findByUsername(String name);

    // 查找用户根据用户名、邮箱或手机号
    UpmsUser findByUsernameOrEmailOrCellPhone(String username, String email, String cellPhone);
    UpmsUser findByUsernameAndPassword(String name, String code);

    UpmsUser findByEmail(String email);
    UpmsUser findByCellPhone(String cellPhone);

    @Query("SELECT u FROM UpmsUser u WHERE u.cellPhone LIKE %:referralCode%")
    List<UpmsUser> findByCellPhoneLike(@Param("referralCode") String referralCode);
}

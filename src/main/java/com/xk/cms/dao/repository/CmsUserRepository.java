package com.xk.cms.dao.repository;

import com.xk.cms.model.po.CmsUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

/**
 * CmsUser Repository
 * Created by yuan on 2024/05/02
 */
public interface CmsUserRepository extends JpaRepository<CmsUser, Long>, Serializable {

    CmsUser findOneByFkUpmsUserId(Long id);

}

package com.xk.cms.dao.repository;

import com.xk.cms.model.po.CmsClub;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

/**
 * CmsClub Repository
 * Created by yuan on 2024/09/18
 * @author yuan
 */
public interface CmsClubRepository extends JpaRepository<CmsClub, Long>, Serializable {

}

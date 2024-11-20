package com.xk.cms.dao.repository;

import com.xk.cms.model.po.CmsClub;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

/**
 * CmsClub Repository
 *
 * @author yuan Created on 2024/09/18.
 */
public interface CmsClubRepository extends JpaRepository<CmsClub, Long>, Serializable {

    CmsClub findByFkUpmsOrganizationId(Long organization_id);

}

package com.xk.cms.dao.repository;

import com.xk.cms.model.po.CmsClub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;
import java.util.List;

/**
 * CmsClub Repository
 *
 * @author yuan Created on 2024/09/18.
 */
public interface CmsClubRepository extends JpaRepository<CmsClub, Long>, Serializable {

    CmsClub findByFkUpmsOrganizationId(Long organization_id);

    @Query("SELECT c FROM CmsClub c WHERE c.clubLogo IS NOT NULL AND c.status = true")
    List<CmsClub> findActiveClubsWithLogo();

}

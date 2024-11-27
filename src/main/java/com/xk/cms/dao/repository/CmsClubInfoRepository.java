package com.xk.cms.dao.repository;

import com.xk.cms.model.po.CmsClubInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * CmsClubInfo Repository
 *
 * @author yuan Created on 2024/11/15.
 */
public interface CmsClubInfoRepository extends JpaRepository<CmsClubInfo, Long>, Serializable {

    List<CmsClubInfo> findByClubId(Long clubId);

    Optional<CmsClubInfo> findByClubIdAndInfoKey(Long clubId, String infoKey);

}

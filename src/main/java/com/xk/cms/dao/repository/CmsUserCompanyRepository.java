package com.xk.cms.dao.repository;

import com.xk.cms.model.po.CmsUserCompany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * CmsUserCompany Repository
 * Created by yuan on 2024/05/02
 */
public interface CmsUserCompanyRepository extends JpaRepository<CmsUserCompany, Long>, Serializable {

    List findByFkCmsUserId(long cms_user_id);

    List findByFkCmsCompanyId(long cms_company_id);

    List<CmsUserCompany> findAllByFkCmsUserIdIn(Set<Long> userIds);

}

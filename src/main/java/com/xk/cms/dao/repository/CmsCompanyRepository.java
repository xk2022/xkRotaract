package com.xk.cms.dao.repository;

import com.xk.cms.model.po.CmsCompany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

/**
 * CmsCompany Repository
 * Created by yuan on 2024/05/02
 */
public interface CmsCompanyRepository extends JpaRepository<CmsCompany, Long>, Serializable {

}

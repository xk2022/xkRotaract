package com.xk.cms.dao.repository;

import com.xk.cms.model.po.CmsCompanyPay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

/**
 * CmsCompanyPay Repository
 * Created by yuan on 2024/08/17
 */
public interface CmsCompanyPayRepository extends JpaRepository<CmsCompanyPay, Long>, Serializable {

    CmsCompanyPay findByFkCmsCompanyId(Long id);

}

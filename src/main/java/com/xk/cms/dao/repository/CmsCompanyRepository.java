package com.xk.cms.dao.repository;

import com.xk.cms.model.po.CmsCompany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

/**
 * CmsCompany Repository
 * Created by yuan on 2024/05/02
 */
public interface CmsCompanyRepository extends JpaRepository<CmsCompany, Long>, Serializable {

    // 使用方法名称约定查询 latlng 不为空的所有记录
    List<CmsCompany> findByLatlngIsNotNull();

    List<CmsCompany> findAllById(Iterable<Long> companyIds);

}

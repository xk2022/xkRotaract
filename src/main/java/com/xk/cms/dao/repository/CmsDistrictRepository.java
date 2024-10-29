package com.xk.cms.dao.repository;

import com.xk.cms.model.po.CmsDistrict;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

/**
 * CmsDistrict Repository
 * Created by yuan on 2024/10/24
 * @author yuan
 */
public interface CmsDistrictRepository extends JpaRepository<CmsDistrict, Long>, Serializable {

}

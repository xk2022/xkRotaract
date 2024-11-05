package com.xk.upms.dao.repository;

import com.xk.upms.model.po.UpmsDictionaryCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface UpmsDictionaryCategoryRepository extends JpaRepository<UpmsDictionaryCategory, Long>, Serializable {

    UpmsDictionaryCategory findOneByCode(String code);

}

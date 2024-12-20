package com.xk.upms.dao.repository;

import com.xk.upms.model.po.UpmsDictionaryData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

public interface UpmsDictionaryDataRepository extends JpaRepository<UpmsDictionaryData, Long>, Serializable {

    List findByParentIdOrderByCode(Long parentId);

    UpmsDictionaryData findByParentIdAndCode(Long parentId, String Code);

}

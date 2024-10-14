package com.xk.cms.dao.repository;

import com.xk.cms.model.po.CmsCalendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

/**
 * CmsCalendar Repository
 * @author yuan
 * Created by yuan on 2024/09/24
 */
public interface CmsCalendarRepository extends JpaRepository<CmsCalendar, Long>, Serializable {

}

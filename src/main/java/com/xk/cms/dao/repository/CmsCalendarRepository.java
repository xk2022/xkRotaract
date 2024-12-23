package com.xk.cms.dao.repository;

import com.xk.cms.model.po.CmsCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * CmsCalendar Repository
 * @author yuan
 * Created by yuan on 2024/09/24
 */
public interface CmsCalendarRepository extends JpaRepository<CmsCalendar, Long>, Serializable {

    @Query("SELECT c FROM CmsCalendar c WHERE c.district_id = :districtId AND c.startDate BETWEEN :startDate AND :endDate")
    List<CmsCalendar> findByDistrictIdAndDateRange(@Param("districtId") String districtId,
                                                   @Param("startDate") Date startDate,
                                                   @Param("endDate") Date endDate);


}

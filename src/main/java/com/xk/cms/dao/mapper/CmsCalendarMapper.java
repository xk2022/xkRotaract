package com.xk.cms.dao.mapper;

import com.xk.cms.model.dto.CmsCalendarExample;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface CmsCalendarMapper {

    List<CmsCalendarExample> findByDateRange(LocalDate startDate, LocalDate endDate);

}
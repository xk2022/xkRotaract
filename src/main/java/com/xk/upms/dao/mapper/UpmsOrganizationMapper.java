package com.xk.upms.dao.mapper;

import com.xk.upms.model.dto.UpmsOrganizationExample;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UpmsOrganizationMapper {

    List<UpmsOrganizationExample> top20EventsClub();

}
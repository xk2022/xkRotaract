package com.xk.cms.dao.mapper;

import com.xk.cms.model.dto.CmsUserExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CmsUserMapper {

    // 使用 CmsUser 進行查詢
    List<CmsUserExample> getClubUsers(@Param("rotaract_id") String rotaractId);

}
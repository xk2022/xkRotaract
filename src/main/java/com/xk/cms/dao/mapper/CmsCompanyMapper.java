package com.xk.cms.dao.mapper;

import com.xk.cms.model.dto.CmsCompanyExample;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CmsCompanyMapper {

    List<CmsCompanyExample> selectAll();

    List<CmsCompanyExample> findByFkCmsUserId(long id);

}
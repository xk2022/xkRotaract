package com.xk.upms.dao.mapper;

import com.xk.upms.model.dto.UpmsRoleCountUserExample;
import com.xk.upms.model.po.UpmsRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UpmsRoleMapper {

    List<UpmsRole> findRolesByUserId(long id);

    List<UpmsRoleCountUserExample> countByClub(long club_id);

}
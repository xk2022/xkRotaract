package com.xk.upms.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.xk.upms.model.dto.UpmsUserRoleExample;

@Mapper
public interface UpmsUserMapper {

	public List<UpmsUserRoleExample> selectAllwithRole();

}
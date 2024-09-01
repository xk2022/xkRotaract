package com.xk.upms.dao.mapper;

import com.xk.upms.model.dto.UpmsUserOrganizationExample;
import com.xk.upms.model.dto.UpmsUserRoleExample;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UpmsUserMapper {

	List<UpmsUserRoleExample> selectAllwithRole();

	List<UpmsUserRoleExample> selectUsersByRole(long roleId);

	List<UpmsUserOrganizationExample> selectUsersByOrganization(long organizationId);

}
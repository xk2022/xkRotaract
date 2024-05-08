package com.xk.upms.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.xk.upms.model.po.UpmsRole;

@Mapper
public interface UpmsRoleMapper {

    public List<UpmsRole> selectAllByUserId(long id);

}
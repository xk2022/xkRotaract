package com.xk.demo.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.xk.demo.model.po.DemoExample;

@Mapper
public interface DemoExampleMapper {

	// int deleteByPrimaryKey(Long id);
//
//    int insert(Demo_User record);
//
//    int insertSelective(Demo_User record);
//
//    SysUser selectByPrimaryKey(Long id);
//
//    int updateByPrimaryKeySelective(Demo_User record);
//
//    int updateByPrimaryKey(Demo_User record);

	/**
	 * 查询全部用户
	 *
	 * @return
	 */
//    List<SysUser> selectAll();

//    @Select("select * from Demo_Example where exapmle_id = #{id}")
	DemoExample queryById(@Param(value = "id") long id);

}
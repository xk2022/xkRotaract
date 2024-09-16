package com.xk.upms.dao.mapper;

import com.xk.upms.model.vo.UpmsRolePermissionWithActiveResp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * UpmsRolePermission Mapper
 * Created by yuan on 2024/09/11
 * @author yuan
 */
@Mapper
public interface UpmsRolePermissionMapper {

    public List<UpmsRolePermissionWithActiveResp> findBy(long role_id, String system_name);

}
package com.xk.upms.model.vo;

import com.xk.upms.model.enums.PermissionAction;
import lombok.Data;

/**
 * UpmsPermission SaveResp
 * Created by yuan on 2024/09/13
 * @author yuan
 *
 *
SELECT urp.`permission_id`, urp.`action`, urp.`active`, up.`name`
FROM `upms_role_permission` urp
LEFT JOIN `upms_permission` up ON up.`permission_id` = urp.`permission_id` AND up.`type` = 2
INNER JOIN `upms_system` us ON up.`system_id` = us.`system_id` AND us.`name` = #{system_name}
WHERE urp.`role_id` = #{role_id}
 */
@Data
public class UpmsRolePermissionWithActiveResp {

    private Long id; // upms_role_permission.permission_id

    private PermissionAction action; // upms_role_permission.action

    private Boolean active; // upms_role_permission.active

    private String name; // upms_permission.name

}

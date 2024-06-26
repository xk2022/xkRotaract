package com.xk.upms.model.po;

import com.xk.common.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by yuan on 2022/06/10
 */
@Entity
@Getter
@Setter
@Table(name = "upms_role_permission")
public class UpmsRolePermission extends BaseEntity implements Serializable {
    /**
     * 流水號
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "faceset_generator", strategy = "guid")
    @Column(name = "role_permission_id")
    @NotNull(groups = Update.class)
    private Long id;
    /**
     * 角色编号
     */
    private Long roleId;
    /**
     * 权限编号
     */
    private Long permissionId;
    /**
     * 權限種類
     */
    private String action;
    /**
     * 权限開關
     */
    private Boolean active;

}
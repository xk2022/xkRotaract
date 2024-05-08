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
@Table(name = "upms_user_permission")
public class UpmsUserPermission extends BaseEntity implements Serializable {
    /**
     * 流水號
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "faceset_generator", strategy = "guid")
    @Column(name = "user_permission_id")
    @NotNull(groups = Update.class)
    private Long id;
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * 权限编号
     */
    private Long permissionId;
    /**
     * 权限类型(-1:减权限,1:增权限)
     */
    private Byte type;
}
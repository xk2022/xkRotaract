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
@Table(name = "upms_organization")
public class UpmsOrganization extends BaseEntity implements Serializable {
    /**
     * 流水號
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "faceset_generator", strategy = "guid")
    @Column(name = "organization_id")
    @NotNull(groups = Update.class)
    private Long id;
    /**
     * 所属上级
     */
    private Integer pid;
    /**
     * 组织名称
     */
    private String name;
    /**
     * 组织描述
     */
    private String description;

}
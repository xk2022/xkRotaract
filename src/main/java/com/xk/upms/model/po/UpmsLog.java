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
@Table(name = "upms_log")
public class UpmsLog extends BaseEntity implements Serializable {
    /**
     * 流水號
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "faceset_generator", strategy = "guid")
    @Column(name = "log_id")
    @NotNull(groups = Update.class)
    private Long id;
    /**
     * 操作描述
     */
    private String description;
    /**
     * 操作用户
     */
    private String username;
    /**
     * 操作时间
     */
    private Long startTime;
    /**
     * 消耗时间
     */
    private Integer spendTime;
    /**
     * 根路径
     */
    private String basePath;
    /**
     * URI
     */
    private String uri;
    /**
     * URL
     */
    private String url;
    /**
     * 请求类型
     */
    private String method;
    /**
     * 用户标识
     */
    private String userAgent;
    /**
     * IP地址
     */
    private String ip;
    /**
     * 权限值
     */
    private String permissions;
}
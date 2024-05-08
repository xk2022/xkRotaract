package com.xk.demo.model.po;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Entity: 实体类, 必须
 * @Table: 对应数据库中的表, 必须, name=表名, Indexes是声明表里的索引, columnList是索引的列, 同时声明此索引列是否唯一, 默认false
 */
@Entity
@Data
@Table(name = "sys_user", indexes = {@Index(name = "id", columnList = "id", unique = true), @Index(name = "name", columnList = "name", unique = true)})
public class SysUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @GeneratedValue： 表明是否自动生成, 必须, strategy也是必写, 指明主键生成策略, 默认是Oracle
    private Long id;

    @Column(name = "name", nullable = false) // @Column： 对应数据库列名,可选, nullable 是否可以为空, 默认true
    private String name;

    private String password;

    private String email;

    private String nickName;

    private String avatar;

    private String salt;

    private String mobile;

    private Byte status;

    private Long deptId;

    private String createBy;

    private Date createTime;

    private String lastUpdateBy;

    private Date lastUpdateTime;

    private Byte delFlag;

}

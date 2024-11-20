package com.xk.upms.model.po;

import com.xk.common.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.io.Serializable;

/**
 * UpmsRole 實體類 - 角色管理
 *
 * @author yuan Created on 2022/06/10.
 */
@Entity
@Getter
@Setter
@Table(name = "upms_role",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"code"})}) // 確保代碼唯一
public class UpmsRole extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", updatable = false, nullable = false)
    @Comment("00_流水號") // 描述
    private Long id;

    @Column(name = "code", unique = true, nullable = false, length = 100)
    @Comment("01_角色名稱")
    private String code;

    @Column(name = "title", nullable = false, length = 100)
    @Comment("02_角色標題")
    private String title;

    @Column(name = "description", length = 500)
    @Comment("03_角色描述")
    private String description;

    @Column(name = "orders", nullable = false)
    @Comment("04_排序")
    private Long orders = 0L; // 預設值

}

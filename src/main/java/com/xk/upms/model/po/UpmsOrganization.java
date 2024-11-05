package com.xk.upms.model.po;

import com.xk.common.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 組織實體類，代表UPMS系統中的組織。
 * 提供組織的基本信息，包括組織名、、、、等。
 * ID: 每個組織的唯一標識。
 * Name: 組織名稱（必填）。
 * Code: 組織編碼，通常是唯一的，用來快速識別組織。
 * Description: 組織的描述，選填。
 * Parent ID: 父組織 ID，用於建立層次關係。如果為 null，表示它是根組織。
 * Level: 組織層級，用於簡化查詢（例如，1 表示根層級，2 表示其子層級）。
 * Status: 組織狀態，通常為布林值，用於標記該組織是否啟用或有效。
 *
 * @author yuan Created on 2022/06/10.
 * @author yuan Updated on 2024/10/28 with code optimization based on GPT recommendations.
 */
@Entity
@Getter
@Setter
@Table(name = "upms_organization")
public class UpmsOrganization extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "faceset_generator", strategy = "guid")
    @Column(name = "organization_id")
    @NotNull(groups = Update.class)
    @Comment("00_流水號")
    private Long id;

    @NotBlank(message = "組織名稱不能為空")
    @Column(name = "name", nullable = false)
    @Comment("01_組織名稱")
    private String name;

    @Column(name = "code", unique = true)
    @Comment("02_組織編碼（唯一，用於標識）")
    private String code;

    @Comment("03_組織描述")
    private String description;

    @Comment("04_父組織的ID，用於構建樹狀結構")
    private Long parentId;

    @Comment("05_組織層級，用於指示組織所在層次")
    private Integer level;

    @Column(name = "orders")
    @Comment("90_資料排序")
    private Long orders;

    @Column(name = "status", columnDefinition = "boolean default true")
    @ColumnDefault(value = "true")
    @Comment("91_組織狀態，表示是否有效或啟用")
    private Boolean status;

}
package com.xk.adm.model.po;

import com.xk.common.base.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 模块实体类，代表 ADM 系统中的模块。
 * 提供模块的基本信息，包括模块名称、标识、描述等。
 *
 * 字段说明：
 * - ID: 每个模块的唯一标识。
 * - Name: 模块名称（必填）。
 * - Code: 模块标识，通常是唯一的，用来快速识别模块（如 UPMS、CMS）。
 * - Description: 模块的描述信息（选填）。
 * - Status: 模块状态，布尔值，标识模块是否启用或禁用（默认启用）。
 * - Version: 模块的版本号（如 1.0.0）。
 * - Base URL: 模块的根路径，用于访问模块的入口。
 * - Create Time: 模块的创建时间（自动生成）。
 * - Update Time: 模块的更新时间（自动更新）。
 *
 * @author yuan Created on 2024/12/05.
 */
@Data
@Entity
@Table(name = "adm_module", uniqueConstraints = {
        @UniqueConstraint(name = "UK_module_code", columnNames = "code")
})
public class AdmModule extends BaseEntity implements Serializable {

    /**
     * 模块唯一标识，使用自增主键。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "module_id", nullable = false, updatable = false)
    @Comment("00_模块ID")
    private Long id;

    /**
     * 模块名称（必填）。
     */
    @NotBlank(message = "模块名称不能为空")
    @Column(name = "name", nullable = false, length = 100)
    @Comment("01_模块名称")
    private String name;

    /**
     * 模块标识，通常是唯一的（如 UPMS、CMS）。
     */
    @NotBlank(message = "模块标识不能为空")
    @Column(name = "code", nullable = false, unique = true, length = 50)
    @Comment("02_模块标识（唯一，用于标识模块）")
    private String code;

    /**
     * 模块描述（选填）。
     */
    @Column(name = "description", columnDefinition = "TEXT")
    @Comment("03_模块描述")
    private String description;

    /**
     * 模块版本号（如 1.0.0）。
     */
    @Column(name = "version", length = 20)
    @Comment("04_模块版本号")
    private String version;

    /**
     * 模块根路径，用于访问模块的入口。
     */
    @Column(name = "base_url", length = 255)
    @Comment("05_模块根路径")
    private String baseUrl;

    /**
     * 模块状态（默认启用）。
     */
    @Column(name = "status", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    @ColumnDefault("true")
    @Comment("91_狀態（啟用/禁用）")
    private Boolean status = true;

}

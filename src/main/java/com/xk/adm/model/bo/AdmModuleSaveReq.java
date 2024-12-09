package com.xk.adm.model.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Request object for saving or updating AdmModule entities.
 *
 * Fields:
 * - id: Optional; required for updates, null for creation.
 * - name: Required; represents the name of the module.
 * - code: Required; unique identifier for the module (e.g., "UPMS").
 * - description: Optional; provides additional information about the module.
 * - version: Optional; indicates the version of the module (e.g., "1.0.0").
 * - baseUrl: Optional; specifies the base URL of the module.
 * - status: Optional; indicates the module's active status ("true" or "false").
 *
 * Validation:
 * - Name and code fields are required.
 * - Code must be alphanumeric and may include underscores.
 * - Version must follow a semantic versioning format (e.g., "1.0.0").
 *
 * @author yuan Created on 2024/12/05.
 */
@Data
public class AdmModuleSaveReq {

    /**
     * 模块 ID，更新时必需，创建时应为 null。
     */
    private String id;

    /**
     * 模块名称（必填）。
     */
    @NotBlank(message = "模块名称不能为空")
    @Size(max = 100, message = "模块名称不能超过100个字符")
    private String name;

    /**
     * 模块标识（必填）。
     */
    @NotBlank(message = "模块标识不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "模块标识只能包含字母、数字和下划线")
    @Size(max = 50, message = "模块标识不能超过50个字符")
    private String code;

    /**
     * 模块描述（选填）。
     */
    @Size(max = 255, message = "模块描述不能超过255个字符")
    private String description;

    /**
     * 模块版本号（选填）。
     */
    @Pattern(regexp = "^(\\d+\\.\\d+\\.\\d+)$", message = "版本号必须符合语义化版本规则，例如 '1.0.0'")
    private String version;

    /**
     * 模块根路径（选填）。
     */
    @Size(max = 255, message = "模块根路径不能超过255个字符")
    private String baseUrl;

    /**
     * 模块状态（选填）。
     * 值为 "true" 或 "false"。
     */
    @Pattern(regexp = "^(true|false)$", message = "状态必须为 'true' 或 'false'")
    private String status;

}

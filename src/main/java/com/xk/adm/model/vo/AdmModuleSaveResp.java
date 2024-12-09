package com.xk.adm.model.vo;

import lombok.Data;

/**
 * Response object for creating or updating AdmModule entities.
 *
 * Fields:
 * - id: The unique identifier of the module.
 * - name: The name of the module.
 * - code: The unique code of the module (e.g., "UPMS").
 * - description: A description of the module.
 * - version: The version of the module (e.g., "1.0.0").
 * - baseUrl: The base URL of the module.
 * - status: The current status of the module ("true" for active, "false" for inactive).
 *
 * This class is used to provide a structured response for module creation or updates.
 *
 * @author yuan Created on 2024/12/05.
 */
@Data
public class AdmModuleSaveResp {

    /**
     * 模块 ID
     */
    private String id;

    /**
     * 模块名称
     */
    private String name;

    /**
     * 模块标识
     */
    private String code;

    /**
     * 模块描述
     */
    private String description;

    /**
     * 模块版本号
     */
    private String version;

    /**
     * 模块根路径
     */
    private String baseUrl;

    /**
     * 模块状态
     * "true" 表示启用，"false" 表示禁用
     */
    private String status;

}

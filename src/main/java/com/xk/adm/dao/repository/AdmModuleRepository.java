package com.xk.adm.dao.repository;

import com.xk.adm.model.po.AdmModule;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for AdmModule entities.
 * Provides methods for CRUD operations and custom queries for managing modules.
 *
 * 功能：
 * - 提供基础的增删改查功能
 * - 支持通过模块名称和标识查询模块
 * - 提供启用状态模块的查询
 * - 自定义原生 SQL 查询模块统计信息
 *
 * 注：该类由 Spring Data JPA 自动实现，无需手动编写实现代码。
 *
 * @author yuan Created on 2024/12/05.
 */
@Repository
public interface AdmModuleRepository extends JpaRepository<AdmModule, Long>, Serializable {

    /**
     * 根据模块标识查询模块（唯一）。
     *
     * @param code 模块标识（如 "UPMS"）
     * @return 包含模块实体的 Optional 对象
     */
    Optional<AdmModule> findByCode(String code);

    /**
     * 查询所有启用状态的模块。
     *
     * @param status 模块状态（true 为启用，false 为禁用）
     * @return 启用状态的模块列表
     */
    List<AdmModule> findByStatus(Boolean status);

    /**
     * 模糊查询模块名称。
     *
     * @param name 部分或完整模块名称
     * @return 模块列表
     */
    List<AdmModule> findByNameContainingIgnoreCase(String name);

    /**
     * 查询模块总数。
     *
     * @return 模块总数量
     */
    @Query("SELECT COUNT(m) FROM AdmModule m")
    long countModules();

    /**
     * 使用原生 SQL 查询所有模块的基本信息（ID、名称和状态）。
     *
     * @return 模块基本信息的列表
     */
    @Query(value = "SELECT module_id, name, status FROM adm_module", nativeQuery = true)
    List<Object[]> findModuleBasicInfo();

    /**
     * 批量更新模块状态为 inactive（逻辑删除）。
     *
     * @param ids 模块 ID 列表
     */
    @Modifying
    @Query("UPDATE AdmModule m SET m.status = false WHERE m.id IN :ids")
    void updateStatusToInactive(@Param("ids") List<Long> ids);

}

package com.xk.adm.service;

import com.xk.adm.model.bo.AdmModuleReq;
import com.xk.adm.model.bo.AdmModuleSaveReq;
import com.xk.adm.model.vo.AdmModuleResp;
import com.xk.adm.model.vo.AdmModuleSaveResp;

import java.util.List;

/**
 * Service interface for managing AdmModule in the ADM system.
 *
 * The service ensures encapsulated business logic and validations
 * for managing modules effectively.
 *
 * @author yuan Created on 2024/12/05.
 */
public interface AdmModuleService {

    /**
     * Retrieves a list of all modules in the system.
     */
    List list(AdmModuleReq resources);

    /**
     * Finds a specific module by its ID.
     *
     * @param id the unique ID of the module to find
     * @return the AdmModule entity representing the module
     * @throws IllegalArgumentException if the module with the given ID does not exist
     */
    AdmModuleResp findById(Long id);

    /**
     * Creates a new module in the system.
     *
     * @param req the request object containing the module details
     * @return a response object containing the created module's details
     * @throws IllegalArgumentException if the module details are invalid
     */
    AdmModuleSaveResp create(AdmModuleSaveReq req);

    /**
     * Updates an existing module in the system.
     *
     * @param id the unique ID of the module to update
     * @param req the request object containing the updated module details
     * @return a response object containing the updated module's details
     * @throws IllegalArgumentException if the module with the given ID does not exist
     */
    AdmModuleSaveResp update(Long id, AdmModuleSaveReq req);

    /**
     * Deletes one or more modules by their IDs.
     *
     * @param ids a comma-separated string of module IDs to delete
     * @throws IllegalArgumentException if any of the IDs are invalid or do not exist
     */
    void deleteByPrimaryKeys(String ids);
}

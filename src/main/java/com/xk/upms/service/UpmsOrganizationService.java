package com.xk.upms.service;

import com.xk.upms.model.bo.UpmsOrganizationReq;
import com.xk.upms.model.bo.UpmsOrganizationSaveReq;
import com.xk.upms.model.vo.UpmsOrganizationResp;
import com.xk.upms.model.vo.UpmsOrganizationSaveResp;

import java.util.List;

/**
 * UpmsOrganizationService interface for managing UPMS organizations.
 * Provides methods for CRUD operations on UpmsOrganization entities.
 *
 * @author yuan Created on 2022/06/24.
 */
public interface UpmsOrganizationService {

    /**
     * Find all organizations based on the given criteria.
     *
     * @param resources the criteria for filtering organizations
     * @return a list of organization responses
     */
    List<UpmsOrganizationResp> list(UpmsOrganizationReq resources);

    /**
     * Find an organization by its ID.
     *
     * @param id the ID of the organization
     * @return the organization response
     */
    UpmsOrganizationResp findById(Long id);

    /**
     * Create a new organization.
     *
     * @param resources the information required to create the organization
     * @return the response after creating the organization
     */
    UpmsOrganizationSaveResp create(UpmsOrganizationSaveReq resources);

    /**
     * Update an existing organization.
     *
     * @param id the ID of the organization to update
     * @param resources the information required to update the organization
     * @return the response after updating the organization
     */
    UpmsOrganizationSaveResp update(Long id, UpmsOrganizationSaveReq resources);

    /**
     * Delete multiple organizations by their IDs.
     *
     * @param ids the IDs of the organizations to delete, separated by commas
     */
    void deleteByPrimaryKeys(String ids);

    /**
     * Find organizations associated with a specific user ID.
     *
     * @param userId the ID of the user
     * @return a list of organization responses associated with the user
     */
    List<UpmsOrganizationResp> findOrganizationsByUserId(long userId);
}
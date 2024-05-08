package com.xk.upms.service;

import com.xk.upms.model.dto.UpmsUserOrganizationExample;
import com.xk.upms.model.po.UpmsUserOrganization;

import java.util.List;

/**
 * UpmsUserOrganizationService 接口
 * Created by yuan on 2022/06/24
 */
public interface UpmsUserOrganizationService {

    List<UpmsUserOrganization> selectByExample(UpmsUserOrganizationExample upmsUserOrganizationExample);

    void organization(String[] organizationIds, long id);

    Object selectByUserId(long id);
}

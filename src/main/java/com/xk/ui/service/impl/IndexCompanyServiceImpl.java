package com.xk.ui.service.impl;

import com.xk.cms.dao.repository.CmsCompanyRepository;
import com.xk.cms.model.po.CmsCompany;
import com.xk.ui.model.vo.CompanyLoc;
import com.xk.ui.service.IndexCompanyService;
import com.xk.common.json.Industry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * IndexCompanyService 实现
 * Created by yuan on 2024/08/05
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class IndexCompanyServiceImpl implements IndexCompanyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexCompanyServiceImpl.class);

    @Autowired
    private CmsCompanyRepository cmsCompanyRepository;

    @Override
    public CompanyLoc get() {
        LOGGER.info("Fetching all companies.");
        CompanyLoc result = new CompanyLoc();
        try {
            List<CmsCompany> companies = cmsCompanyRepository.findByLatlngIsNotNull();

            // Initialize the map to store locations by type
            Map<String, List<CompanyLoc.Location>> typeMap = new HashMap<>();

            // Iterate through each industry type
            for (Industry industry : Industry.values()) {
                String typeKey = String.valueOf(industry.getKey());
                List<CompanyLoc.Location> locations = companies.stream()
                        .filter(company -> {
                            String industries = company.getIndustries();
                            if (industries == null || industries.isEmpty()) {
                                return false;
                            }
                            // Check if the company's industries contains the current industry key
                            String[] industryKeys = industries.split(",");
                            return Arrays.stream(industryKeys)
                                    .map(String::trim) // Remove any leading/trailing whitespace
                                    .map(Integer::parseInt) // Convert to integer
                                    .anyMatch(industryKey -> industryKey == industry.getKey());
                        })
                        .map(company -> {
                            CompanyLoc.Location location = new CompanyLoc.Location();
                            location.setLocId(String.valueOf( company.getId()));
                            location.setName(company.getName());
                            location.setDescription(company.getAddress());
                            String[] latLng = company.getLatlng().split(",");
                            if (latLng.length >= 2) {
                                location.setLat(latLng[0]); // Convert to double
                                location.setLng(latLng[1]); // Convert to double
                            } else {
                                return null;
                            }
                            return location;
                        })
                        .collect(Collectors.toList());
                typeMap.put(typeKey, locations);
            }

            result.setLocations(typeMap);
            LOGGER.info("Fetched {} companies.", companies.size());
        } catch (Exception e) {
            LOGGER.error("Error fetching companies", e);
            // Handle exception as needed
        }
        return result;
    }
}

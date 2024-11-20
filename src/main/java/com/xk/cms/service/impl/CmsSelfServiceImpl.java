package com.xk.cms.service.impl;

import com.xk.cms.service.CmsSelfService;
import com.xk.common.json.Industry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation of the CmsSelf Service.
 *
 * @author yuan Created on 2024/05/02.
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class CmsSelfServiceImpl implements CmsSelfService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsSelfServiceImpl.class);

    @Override
    public List getAllIndustries() {
        return Arrays.asList(Industry.values());
    }

    @Override
    public List<List<Industry>> getChunkedIndustries() {
        List<Industry> allIndustries = Arrays.asList(Industry.values());
        List<List<Industry>> chunkedIndustries = new ArrayList<>();
        int chunkSize = 4;
        for (int i = 0; i < allIndustries.size(); i += chunkSize) {
            int end = Math.min(i + chunkSize, allIndustries.size());
            chunkedIndustries.add(allIndustries.subList(i, end));
        }
        return chunkedIndustries;
    }

}

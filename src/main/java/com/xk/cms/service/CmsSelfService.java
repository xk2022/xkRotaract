package com.xk.cms.service;

import com.xk.common.json.Industry;

import java.util.List;

/**
 * CmsUserService 接口
 * Created by yuan on 2024/05/02
 * @author yuan
 */
public interface CmsSelfService {

    List getAllIndustries();

    List<List<Industry>> getChunkedIndustries();

}

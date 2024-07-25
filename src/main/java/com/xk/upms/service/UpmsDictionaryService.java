package com.xk.upms.service;

import com.xk.upms.model.bo.UpmsDictionaryCategoryReq;
import com.xk.upms.model.bo.UpmsDictionaryDataReq;
import com.xk.upms.model.vo.UpmsDictionaryCategoryResp;
import com.xk.upms.model.vo.UpmsDictionaryDataResp;

import java.util.List;

/**
 * UpmsSystemService 接口
 * Created by yuan on 2022/06/10
 */
public interface UpmsDictionaryService {

    List list();

    List listDDbyDC(String categaryCode);

    UpmsDictionaryCategoryResp createCategory(UpmsDictionaryCategoryReq resources);

    UpmsDictionaryCategoryResp updateCategory(Long id, UpmsDictionaryCategoryReq resources);

    void deleteCategory(Long id);

    void deleteCategoryByPrimaryKeys(String ids);

    UpmsDictionaryDataResp createData(UpmsDictionaryDataReq resources);

    UpmsDictionaryDataResp updateData(Long id, UpmsDictionaryDataReq resources);

    void deleteData(Long id);

    void deleteDataByPrimaryKeys(String ids);

}

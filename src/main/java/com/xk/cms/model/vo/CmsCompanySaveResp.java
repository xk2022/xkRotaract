package com.xk.cms.model.vo;

import com.xk.cms.model.po.CmsCompany;
import lombok.Data;

import java.util.List;

/**
 * CmsCompany 響應變更後物件
 *
 * @author yuan Created on 2024/05/02.
 */
@Data
public class CmsCompanySaveResp extends CmsCompany {

    private String industriesChinese;

    private List<Long> industryIds;

    private boolean locked;

}
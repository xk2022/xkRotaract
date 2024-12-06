package com.xk.cms.model.vo;

import com.xk.cms.model.dto.CmsClubInfoHeader;
import com.xk.cms.model.dto.CmsClubInfoOverview;
import lombok.Data;

/**
 * CmsClubInfo 響應前端物件
 *
 * @author yuan Created on 2024/11/20.
 */
@Data
public class CmsClubInfoResp {

    private String cmsClubId;

    private CmsClubInfoHeader infoHeader; // The Top

    private CmsClubInfoOverview infoOverview; // first page

}
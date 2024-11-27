package com.xk.cms.model.vo;

import com.xk.cms.model.dto.CmsClubInfoHeader;
import com.xk.cms.model.dto.CmsClubInfoOverview;
import lombok.Data;

/**
 * CmsClub 響應前端物件
 *
 * @author yuan Created on 2024/09/18.
 * @author yuan Last Updated on 2024/11/15.
 */
@Data
public class CmsClubResp {

    private String cmsClubId;

    private CmsClubInfoHeader infoHeader; // The Top

    private CmsClubInfoOverview infoOverview; // first page

}
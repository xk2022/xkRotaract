package com.xk.cms.model.vo;

import com.xk.cms.model.dto.CmsClubInfoHeader;
import com.xk.cms.model.dto.CmsClubInfoOverview;
import lombok.Data;

/**
 * CmsClub Resp
 *
 * @author yuan Created on 2024/09/18.
 * @author yuan Updated on 2024/11/15.
 */
@Data
public class CmsClubResp {

    private CmsClubInfoHeader infoHeader; // The Top

    private CmsClubInfoOverview infoOverview; // first page

}
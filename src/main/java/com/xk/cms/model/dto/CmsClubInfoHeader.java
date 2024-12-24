package com.xk.cms.model.dto;

import lombok.Data;

/**
 * CmsClub InfoHeader
 *
 * @author yuan Created on 2024/11/15.
 */
@Data
public class CmsClubInfoHeader {

    private String id;

    private String clubLogo; // 社徽章 (Club Emblem)

    private String clubName; // 社名稱 (Club Name)

    private String organizationDistrict; // 地區名稱（中文） (District Name in Chinese)

    private String serviceArea; // 服務區域 (Service Area)

    private String serviceEmail; // 服務電子信箱 (Service Email)

    private Double infoCompletionScore; // 資料完成度 (Information Completion Score)

    private String clubLogoBase64; // 用于 Base64 编码的 Logo

}
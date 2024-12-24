package com.xk.cms.model.dto;

import lombok.Data;

/**
 * CmsClub InfoOverview
 *
 * @author yuan Created on 2024/11/15.
 */
@Data
public class CmsClubInfoOverview {

    private String id;

    private String clubName; // 扶青社名稱 (Club Name)

    private String registrationDate; // 授證日期 (Charter Date)

    private String sponsoringClub; // 輔導社 (Sponsoring Club)

    private String memberCount; // 社員人數 (Number of Members)

    private String meetingVenue; // 例會地點 (Regular Meeting Venue) : 強調「例行會議」的場所

    private String contactNumber; // 聯絡電話 (Contact Number)

    private String meetingSchedule; // 例會時間 (Regular Meeting Schedule) : 更正式且強調例行性。

    private String faxNumber; // 傳真電話 (Fax Number)

    private String correspondenceAddress; // 通訊處 (Correspondence Address) : 適用於正式場合，特別是指用於通信的地址。

    private String serviceArea;

    private String serviceEmail;

    private String serviceInstagram;

    private String serviceLine;
    
}
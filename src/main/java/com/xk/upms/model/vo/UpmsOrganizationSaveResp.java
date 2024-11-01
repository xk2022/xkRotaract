package com.xk.upms.model.vo;

import lombok.Data;

/**
 * UpmsOrganization Save Response Object
 * 用於返回組織信息的響應物件，所有屬性均為 String 型別，避免直接使用實體類的屬性。
 *
 * Created by yuan on 2022/06/24
 * Updated on 2024/10/30 to remove inheritance and use String types.
 */
@Data
public class UpmsOrganizationSaveResp {

    private String id;
    private String name;
    private String code;
    private String description;
    private String parentId;
    private String level;
    private String orders;
    private String status;

}
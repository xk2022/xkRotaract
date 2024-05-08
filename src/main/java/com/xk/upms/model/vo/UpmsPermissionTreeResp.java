package com.xk.upms.model.vo;

import com.xk.upms.model.po.UpmsPermission;
import lombok.Data;

import java.util.List;

/**
 * UpmsPermission TreeResp
 * Created by yuan on 2022/06/27
 */
@Data
public class UpmsPermissionTreeResp extends UpmsPermission {

    /**
     * 流水號
     */
    private Long id;
    /**
     * 名稱
     */
    private String name;
    /**
     * 路徑
     */
    private String uri;
    /**
     *
     */
    private boolean open;
    /**
     * 是否選定
     */
    private boolean checked = false;

    private List<UpmsPermissionTreeResp> children;

}

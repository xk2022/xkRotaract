package com.xk.upms.model.vo;

import java.util.List;

import com.xk.upms.model.po.UpmsRole;
import com.xk.upms.model.po.UpmsUser;

import lombok.Getter;
import lombok.Setter;

/**
 * UpmsUser Resp for Page: Index
 * Created by yuan on 2022/11/30
 */
@Getter
@Setter
public class UpmsUserDetailResp extends UpmsUser {

    public List<UpmsRole> roleList;

}
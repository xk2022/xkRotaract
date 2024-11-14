package com.xk.cms.model.po;

import com.xk.common.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by yuan on 2024/05/02
 */
@Entity
@Getter
@Setter
//@EqualsAndHashCode //不能用@EqualsAndHashCode和@ToString，否则死循环内存溢出
@Table(name = "cms_user")
public class CmsUser extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "faceset_generator", strategy = "guid")
    @Column(name = "user_id")
    @NotNull(groups = Update.class)
    @Comment("00_流水號")
    private Long id;

    // user account
    @Comment("fk_upms_user_id")
    @Column(name = "fk_upms_user_id")
    private Long fkUpmsUserId;

    @Comment("01_扶輪名")
    private String rname;

    @Comment("02_姓名")
    private String realname;

    @Comment("03_扶輪地區")
    private String district_id;

    @Comment("04_扶青所屬社")
    private String rotaract_id;

    @Comment("05_頭像")
    private String avatarUrl;
//    @Lob
//    private Blob avatar;

    @Comment("06_電話")
    private String phoneNumber;

    @Comment("07_性别 用戶性別（例如 \"Male\", \"Female\"）")
    private Byte gender;

}
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
@Table(name = "cms_company")
public class CmsCompany extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "faceset_generator", strategy = "guid")
    @Column(name = "user_id")
    @NotNull(groups = Update.class)
    @Comment("00_流水號")
    private Long id;

    @Comment("01_公司名稱")
    private String name;

    @Comment("02_公司電話")
    private String phone;

    @Comment("03_公司地址")
    private String address;

    @Comment("04_公司網址")
    private String url;

    @Comment("05_產業別")
    private String industries;

}
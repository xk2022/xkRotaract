package com.xk.cms.model.po;

import com.xk.common.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by yuan on 2024/08/17
 */
@Entity
@Getter
@Setter
//@EqualsAndHashCode //不能用@EqualsAndHashCode和@ToString，否则死循环内存溢出
@Table(name = "cms_company_pay")
public class CmsCompanyPay extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_pay_id")
    @NotNull(groups = Update.class)
    @Comment("00_流水號")
    private Long id;

    private Long fkCmsCompanyId;

    @Comment("01_繳費日期")
    private Date payDate;

    /**
     * 状态(0:正常,1:锁定)
     */
    @Comment("02_状态")
    @Column(columnDefinition = "boolean default false")
    private Boolean locked;

}
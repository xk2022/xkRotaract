package com.xk.cms.model.po;

import com.xk.common.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by yuan on 2024/09/18
 * @author yuan
 */
@Entity
@Getter
@Setter
@Table(name = "cms_club")
public class CmsClub extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "faceset_generator", strategy = "guid")
    @Column(name = "club_id")
    @NotNull(groups = Update.class)
    @Comment("00_流水號")
    private Long id;

    @Comment("01_所属地區")
    private String district;

    @Comment("02_社團名稱（地區or社名）")
    private String name;

    @Column(name = "registration_date", nullable = true) // Make the column nullable
    @Temporal(TemporalType.TIMESTAMP)
    @Comment("03_註冊日期RI")
    private Date registrationDate;

    @Comment("04_狀態(0:禁止,1:正常)")
    private Boolean status;

}
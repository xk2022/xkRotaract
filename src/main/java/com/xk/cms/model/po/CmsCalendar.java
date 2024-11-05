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
 * @author yuan
 * Created by yuan on 2024/08/26
 */
@Entity
@Getter
@Setter
//@EqualsAndHashCode //不能用@EqualsAndHashCode和@ToString，否则死循环内存溢出
@Table(name = "cms_calendar")
public class CmsCalendar extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calendar_id")
    @NotNull(groups = Update.class)
    @Comment("00_流水號")
    private Long id;

    @Comment("01_活動名稱")
    private String eventName;

    @Comment("02_活動說明")
    private String eventDescription;

    @Comment("03_活動地點")
    private String eventLocation;

    @Comment("04_全天")
    private Boolean allDay = null;

    @Temporal(TemporalType.TIMESTAMP)
    @Comment("05_活動開始日期")
    private Date startDate;

    @Comment("06_活動開始時間")
    private String startTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Comment("07_活動結束日期")
    private Date endDate;

    @Comment("08_活動結束時間")
    private String endTime;

    @Comment("09_活動類型")
    private String type;

    @Comment("10_所屬地區")
    private String district_id;

    @Comment("11_所屬扶青社")
    private String rotaract_id;

}
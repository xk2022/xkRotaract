package com.xk.upms.model.po;

import com.xk.common.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by yuan on 2022/06/10
 */
@Entity
@Getter
@Setter
@Table(name = "upms_system")
public class UpmsSystem extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "faceset_generator", strategy = "guid")
    @Column(name = "system_id")
    @NotNull(groups = Update.class)
    @Comment("00_流水號")
    private Long id;

    @Comment("01_系統圖標")
    private String icon;

    @Comment("02_系統背景")
    private String banner;

    @Comment("03_系統主题")
    private String theme;

    @Comment("04_系統根目錄")
    private String basepath;

    @NotNull
    @Comment("05_系統名稱")
    private String name;

    @Comment("06_系統標題")
    private String title;

    @Comment("07_系統描述")
    private String description;
    /**
     * 状态(-1:黑名单,1:正常)
     */
    @ColumnDefault(value = "true")
    @Comment("08_狀態")
    private Boolean status;

    @Comment("09_排序")
    private Long orders = Long.valueOf(999);

}
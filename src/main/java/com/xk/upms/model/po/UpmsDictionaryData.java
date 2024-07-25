package com.xk.upms.model.po;

import com.xk.common.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 字典分類
 * Created by yuan on 2024/05/22
 */
@Entity
@Getter
@Setter
@Table(name = "upms_dictionary_data")
public class UpmsDictionaryData extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "faceset_generator", strategy = "guid")
    @Column(name = "dictionary_data_id")
    @NotNull(groups = Update.class)
    @Comment("00_流水號")
    private Long id;

    @Comment("01_父輩類型")
    private Long parentId;

    @Comment("02_類型代碼")
    private String code;

    @Comment("03_類型說明")
    private String description;
}
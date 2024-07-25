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
@Table(name = "upms_dictionary_category")
public class UpmsDictionaryCategory extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "faceset_generator", strategy = "guid")
    @Column(name = "dictionary_category_id")
    @NotNull(groups = Update.class)
    @Comment("00_流水號")
    private Long id;

    @Comment("01_類型代碼")
    private String code;

    @Comment("02_類型說明")
    private String description;
}
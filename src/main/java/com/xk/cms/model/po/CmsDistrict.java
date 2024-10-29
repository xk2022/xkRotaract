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
 * Created by yuan on 2024/10/24
 * @author yuan
 */
@Entity
@Getter
@Setter
@Table(name = "cms_district")
public class CmsDistrict extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "faceset_generator", strategy = "guid")
    @Column(name = "district_id")
    @NotNull(groups = Update.class)
    @Comment("00_流水號")
    private Long id;

    @Comment("01_所属地區")
    private String code;

    @Comment("02_社團名稱（地區or社名）")
    private String name;

    @Comment("91_狀態(0:禁止,1:正常)")
    private Boolean status;

}
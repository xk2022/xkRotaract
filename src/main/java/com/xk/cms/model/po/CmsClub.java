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
 *
 * @author yuan Created on 2024/09/18.
 * @author yuan Updated on 2024/11/15.
 * @author yuan Updated on 2024/12/24. Add @Blob clubLogo
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

    @Column(name = "organization_id", nullable = false, length = 255)
    @Comment("01_所属組織 ID")
    private Long fkUpmsOrganizationId;

    @Comment("02_社團名稱（地區or社名）")
    private String name;

    @Lob
    @Column(name = "club_logo")
    @Comment("03_社團LOGO")
    private byte[] clubLogo;

    @Column(name = "status", nullable = false, columnDefinition = "boolean default true")
    @Comment("91_狀態，表示是否有效或啟用")
    private Boolean status;

}
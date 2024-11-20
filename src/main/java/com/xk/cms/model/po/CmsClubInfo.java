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
 * CmsClubInfo - 社團資訊表
 * 用於儲存 key-value 格式的社團資訊
 *
 * @author yuan Created on 2024/11/15.
 */
@Entity
@Getter
@Setter
@Table(name = "cms_club_info")
public class CmsClubInfo extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "faceset_generator", strategy = "guid")
    @Column(name = "id")
    @NotNull(groups = Update.class)
    @Comment("00_流水號")
    private Long id;

    @Column(name = "club_id", nullable = false)
    @Comment("01_關聯的社團 ID")
    @NotNull
    private Long clubId;

    @Column(name = "info_key", nullable = false, length = 255)
    @Comment("02_資訊的名稱（key）")
    @NotNull
    private String infoKey;

    @Column(name = "info_value", columnDefinition = "TEXT")
    @Comment("03_資訊的內容（value）")
    private String infoValue;

    @Column(name = "status", nullable = false, columnDefinition = "boolean default true")
    @Comment("91_狀態，表示是否有效或啟用")
    private Boolean status;

}

package com.xk.upms.model.po;

import com.xk.common.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by yuan on 2024/08/29
 * 推薦碼
 */
@Entity
@Getter
@Setter
@Table(name = "upms_user_referral")
public class UpmsUserRef extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "referral_id")
    @NotNull(groups = Update.class)
    @Comment("00_流水號")
    private Long id;

    @Comment("01_推薦人")
    @Column(nullable = false)
    private Long referrerId;

    @Comment("02_被推薦人")
    @Column(nullable = false)
    private Long refereeId;

    // 系統當下手機後六位
    @Comment("03_推薦人碼")
    @Column(nullable = false, length = 50)
    private String referralCode;

}
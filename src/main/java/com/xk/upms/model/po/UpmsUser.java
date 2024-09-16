package com.xk.upms.model.po;

import com.xk.common.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by yuan on 2022/06/10
 * 基本信息
 *
 * 用戶ID
 * 姓名
 * 工號
 * 所在部門
 * 職位
 */
@Entity
@Getter
@Setter
//@EqualsAndHashCode //不能用@EqualsAndHashCode和@ToString，否则死循环内存溢出
@Table(name = "upms_user")
public class UpmsUser extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "faceset_generator", strategy = "guid")
    @Column(name = "user_id")
    @NotNull(groups = Update.class)
    @Comment("00_流水號")
    private Long id;

    @NotBlank(message = "用戶名稱不能為空")
    @Column(unique = true)
    @Comment("01_用戶名稱")
    private String username;

    @NotBlank(message = "郵箱不能為空")
    @Column(unique = true)
    @Comment("02_郵箱")
    @Pattern(regexp = ".+@.+\\..+", message = "請輸入有效的郵箱地址")
    private String email;

    @Comment("03_電話")
    @Pattern(regexp = "^[0-9]{10,15}$", message = "請輸入有效的電話號碼")
    private String cellPhone;

//    @Comment("04_鹽")
//    private String salt;

    @NotBlank(message = "密碼不能為空")
    @Comment("05_密碼MD5(密碼+鹽)")
    private String password;

    // last_login: 記錄上次登錄時間，可以是NULL。
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Comment("06_最後登入時間")
    private Date lastLogin;

    // failed_attempts: 記錄登錄失敗的次數，用於實施登錄嘗試限制。
//    @Comment("07_登入失敗次數")
//    private String failedAttempts;

    /**
     * account_locked: 布林值，標記帳戶是否被鎖定。
     * 状态(0:正常,1:锁定)
     */
    @Comment("09_状态")
    @Column(columnDefinition = "boolean default false")
    private Boolean locked;

}
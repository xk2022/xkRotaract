package com.xk.upms.model.po;

import com.xk.common.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

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

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "t_users_roles",
//            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
//            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "role_id")})
//    private Set<UpmsRole> roles;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "userInfo_id", referencedColumnName = "userInfo_id")
//    private T_UserInfo userInfo;

    @NotBlank(message = "用戶名稱NotBlank")
    @Column(unique = true)
    @Comment("01_用戶名稱")
    private String username;

    @Comment("02_密碼MD5(密碼+鹽)")
    private String password;

    @Comment("03_鹽")
    private String salt;

//    @Comment("04_姓名")
//    private String realname;

//    @Comment("05_頭像")
//    private String avatar;
//    @Lob
//    private Blob avatar;

//    @Comment("06_電話")
//    private String phone;

    @Comment("04_郵箱")
    private String email;

//    @Comment("08_性别")
//    private Byte sex;
    /**
     * 状态(0:正常,1:锁定)
     */
    @Comment("05_状态")
    @Column(columnDefinition = "boolean default false")
    private Boolean locked;

}
package com.xk.demo.model.po;

import com.xk.common.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author yuan
 * Created by yuan on 2022/06/10
 * Update by yuan at 2024/09/18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("範例實體類")
@Entity
@Table(name = "demo_example")
public class DemoExample extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "faceset_generator", strategy = "guid")
    @Column(name = "exapmle_id")
    @NotNull(groups = Update.class)
    @Comment("00_流水號")
    private Long id;

    @ApiModelProperty(value = "名稱", required = true, example = "王小明")
    @NotBlank
    @Comment("01_名稱")
    private String name;

    @Comment("09_排序")
    private Long orders = Long.valueOf(999);
}
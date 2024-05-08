package com.xk.demo.model.po;

import com.xk.common.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by yuan on 2022/06/10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("範例實體類")
@Entity
@Table(name = "demo_example")
public class DemoExample extends BaseEntity implements Serializable {
    /**
     * 流水號
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "faceset_generator", strategy = "guid")
    @Column(name = "exapmle_id")
    @NotNull(groups = Update.class)
    private Long id;

    /**
     * 名稱
     */
    @ApiModelProperty(value = "名稱", required = true, example = "王小明")
    @NotBlank
    private String name;
}
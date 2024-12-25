package com.xk.ui.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "泡泡球信息")
public class BubbleInfo {

    @ApiModelProperty(value = "泡泡的标题")
    private String title;

    @ApiModelProperty(value = "泡泡的链接")
    private String link;

    @ApiModelProperty(value = "泡泡的图片地址")
    private String imageUrl;

    @ApiModelProperty(value = "用于 Base64 编码的 Logo")
    private String clubLogoBase64;

}

package com.itl.iap.notice.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 查询条件
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "MsgTargetPersonTempDto",description = "消息中心用户模板配置查询条件")
public class MsgTargetPersonTempDto {

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "账户")
    private String userName;

    @ApiModelProperty(value = "本人姓名")
    private String realName;

}

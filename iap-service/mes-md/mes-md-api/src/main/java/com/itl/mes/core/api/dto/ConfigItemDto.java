package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "ConfigItemDto",description = "配置项分页查询")
public class ConfigItemDto implements Serializable {

    @ApiModelProperty(value = "分页信息")
    private Page page;

    @ApiModelProperty(value="UUID【PK】")
    private String bo;

    @ApiModelProperty(value="工厂【UK】")
    private String site;

    @ApiModelProperty(value="编号【UK】")
    private String itemCode;

    @ApiModelProperty(value="名称")
    private String itemName;

    @ApiModelProperty(value="描述")
    private String itemDesc;

    @ApiModelProperty(value="建档日期")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss.SSS",
            timezone = "GMT+8"
    )
    private Date createDate;

    @ApiModelProperty(value="建档人")
    private String createUser;

    @ApiModelProperty(value="修改日期")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss.SSS",
            timezone = "GMT+8"
    )
    private Date modifyDate;

    @ApiModelProperty(value="修改人")
    private String modifyUser;

    @ApiModelProperty(value="状态（0：未启用，1：已启用）")
    private Integer state;

    @ApiModelProperty(value="配置项KEY")
    private String configItemKey;

    @ApiModelProperty(value="配置项VALUE")
    private String configItemValue;

    @ApiModelProperty(value="配置项TYPE")
    private String configItemType;

}

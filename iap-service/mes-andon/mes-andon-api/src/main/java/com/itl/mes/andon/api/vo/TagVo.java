package com.itl.mes.andon.api.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "TagVo", description = "保存安灯类型标识数据")
public class TagVo {

    @ApiModelProperty(value = "分页对象")
    private Page page;


    @ApiModelProperty(value = "ANDON_TYPE_TAG:SITE,ANDON_TYPE_TAG【PK】")
    private String bo;

    @ApiModelProperty(value = "工厂")
    private String site;

    @ApiModelProperty(value = "安灯类型标识")
    private String andonTypeTag;

    @ApiModelProperty(value = "安灯类型标识名称")
    private String andonTypeTagName;

    @ApiModelProperty(value="建档日期")
    private Date createDate;

    @ApiModelProperty(value="建档人")
    private String createUser;

    @ApiModelProperty(value="修改日期")
    private Date modifyDate;

    @ApiModelProperty(value="修改人")
    private String modifyUser;


}

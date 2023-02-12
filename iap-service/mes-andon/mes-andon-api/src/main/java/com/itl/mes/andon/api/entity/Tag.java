package com.itl.mes.andon.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("andon_type_tag")
@ApiModel(value = "tag", description = "安灯类型标识")
public class Tag implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "BO", type = IdType.INPUT)
    @Length(max = 100)
    @ApiModelProperty(value = "ANDON_TYPE_TAG:SITE,ANDON_TYPE_TAG【PK】")
    private String bo;

    /** 工厂 */
    @TableField("SITE")
    @Length(max = 100)
    @ApiModelProperty(value = "工厂")
    private String site;

    @TableField("ANDON_TYPE_TAG")
    @Length(max = 64)
    @ApiModelProperty(value = "安灯类型标识")
    private String andonTypeTag;

    @TableField("ANDON_TYPE_TAG_NAME")
    @Length(max = 64)
    @ApiModelProperty(value = "安灯类型标识名称")
    private String andonTypeTagName;

    @ApiModelProperty(value="建档日期")
    @TableField("CREATE_DATE")
    private Date createDate;

    @ApiModelProperty(value="建档人")
    @TableField("CREATE_USER")
    private String createUser;

    @ApiModelProperty(value="修改日期")
    @TableField("MODIFY_DATE")
    private Date modifyDate;

    @ApiModelProperty(value="修改人")
    @TableField("MODIFY_USER")
    private String modifyUser;
}

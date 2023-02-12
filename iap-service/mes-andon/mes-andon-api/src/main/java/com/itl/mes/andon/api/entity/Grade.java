package com.itl.mes.andon.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.mes.andon.api.constant.CustomCommonConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

/**
 * 安灯等级
 *
 * @author cuichonghe
 * @date 2020-12-14 14:56:55
 */
@Data
@TableName("andon_grade")
@ApiModel(value = "Grade", description = "安灯等级表")
public class Grade implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ANDON_GRADE:SITE,ANDON_GRADE【PK】")
    @Length(max = 100)
    @TableId(value = "BO", type = IdType.INPUT)
    private String bo;

    @ApiModelProperty(value = "工厂")
    @Length(max = 100)
    @TableField("SITE")
    private String site;

    @ApiModelProperty(value = "安灯等级编号")
    @Length(max = 64)
    @TableField("ANDON_GRADE")
    private String andonGrade;

    @ApiModelProperty(value = "名称")
    @Length(max = 64)
    @TableField("ANDON_GRADE_NAME")
    private String andonGradeName;

    @ApiModelProperty(value = "描述")
    @Length(max = 256)
    @TableField("ANDON_GRADE_DESC")
    private String andonGradeDesc;

    @ApiModelProperty(value = "开始")
    @TableField("START_TIME")
    private Integer startTime;

    @ApiModelProperty(value = "结束")
    @TableField("END_TIME")
    private Integer endTime;

    @ApiModelProperty(value = "状态（已启用0，已禁用1）")
    @Length(max = 1)
    @TableField("STATE")
    private String state;

    @ApiModelProperty(value = "建档日期")
    @TableField("CREATE_DATE")
    @JsonFormat(pattern = CustomCommonConstants.DATE_FORMAT_CONSTANTS, timezone = "GMT+8")
    private Date createDate;

    @ApiModelProperty(value = "建档人")
    @TableField("CREATE_USER")
    private String createUser;

    @ApiModelProperty(value = "修改日期")
    @TableField("MODIFY_DATE")
    @JsonFormat(pattern = CustomCommonConstants.DATE_FORMAT_CONSTANTS, timezone = "GMT+8")
    private Date modifyDate;

    @ApiModelProperty(value = "修改人")
    @TableField("MODIFY_USER")
    private String modifyUser;

}

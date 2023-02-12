package com.itl.mom.label.api.entity.label;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.mom.label.api.enums.LabelPrintLogStateEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @auth liuchenghao
 * @date 2021/4/2
 */
@Data
@ApiModel(value = "LabelPrintLog",description = "标签打印日志实体")
@TableName("label_label_print_log")
public class LabelPrintLog {

    /**
     * 标签打印日志BO
     */
    @TableId(value = "BO", type = IdType.UUID)
    private String bo;

    /**
     * 标签打印BO
     */
    @TableField("LABEL_PRINT_BO")
    private String labelPrintBo;

    /**
     * 标签打印详情BO
     */
    @TableField("LABEL_PRINT_DETAIL_BO")
    private String labelPrintDetailBo;

    /**
     * 打印日期
     */
    @ApiModelProperty(value = "打印日期")
    @TableField("PRINT_DATE")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private Date printDate;

    /**
     * 打印用户
     */
    @ApiModelProperty(value = "用户名")
    @TableField("PRINT_USER")
    private String printUser;

    /**
     * 打印类型
     * {@link LabelPrintLogStateEnum#getCode()}
     * */
    @ApiModelProperty(value = "打印类型")
    @TableField("state")
    private String state;
    /**
     * 打印数量
     * */
    @ApiModelProperty(value = "打印数量")
    @TableField("PRINT_COUNT")
    private Integer printCount;
    /**
     * 打印机名称
     * */
    @ApiModelProperty(value = "打印机名称")
    @TableField("PRINTER")
    private String printer;

    /**
     * 工位
     * */
    @TableField("station")
    private String station;

}

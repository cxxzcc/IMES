package com.itl.mes.me.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

/**
 * @auth liuchenghao
 * @date 2021/4/2
 */
@Data
@ApiModel(value = "LabelPrintLog",description = "标签打印日志实体")
@TableName("me_label_print_log")
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
    @TableField("PRINT_DATE")
    private Date printDate;

    /**
     * 打印用户
     */
    @TableField("PRINT_USER")
    private String printUser;

}

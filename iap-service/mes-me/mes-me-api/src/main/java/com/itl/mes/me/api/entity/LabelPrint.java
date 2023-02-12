package com.itl.mes.me.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

/**
 * @auth liuchenghao
 * @date 2021/3/23
 */
@Data
@ApiModel(value = "LabelPrint",description = "标签打印实体")
@TableName("me_label_print")
public class LabelPrint {


    @TableId(value = "BO", type = IdType.UUID)
    private String bo;



    /**
     * 条码数量
     */
    @TableField("BAR_CODE_AMOUNT")
    private Integer barCodeAmount;

    /**
     * 开始编码
     */
    @TableField("START_CODE")
    private String startCode;

    /**
     * 截止编码
     */
    @TableField("END_CODE")
    private String endCode;


    /**
     * 创建账号
     */
    @TableField("CREATE_USER")
    private String createUser;

    /**
     * 创建时间
     */
    @TableField("CREATE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;


    @TableField("PRINT_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date printDate;

    /**
     * 工厂
     */
    @TableField("SITE")
    private String site;


    /**
     * 是否补码
     */
    @TableField("IS_COMPLEMENT")
    private Integer isComplement;


    /**
     * 规则模板
     */
    @TableField("RULE_LABEL_BO")
    private String ruleLabelBo;


    @TableField("TEMPLATE_TYPE")
    private String templateType;

    @TableField("LODOP_TEXT")
    private String lodopText;

    @TableField("ELEMENT_TYPE")
    private String elementType;

    @TableField("ELEMENT_BO")
    private String elementBo;

    /**
     * 编码规则参数
     */
    @TableField("RULE_LABEL_PARAMS")
    private String ruleLabelParams;
}

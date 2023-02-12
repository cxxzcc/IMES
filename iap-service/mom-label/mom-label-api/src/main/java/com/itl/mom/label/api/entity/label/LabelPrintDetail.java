package com.itl.mom.label.api.entity.label;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @auth liuchenghao
 * @date 2021/3/23
 */
@Data
@ApiModel(value = "LabelPrintDetail",description = "标签打印详情实体")
@TableName("label_label_print_detail")
public class LabelPrintDetail {


    /**
     * 主键
     */
    @TableId(value = "BO", type = IdType.UUID)
    private String bo;

    /**
     * 打印范围ID
     */
    @TableField("LABEL_PRINT_BO")
    private String labelPrintBo;

    /**
     * 编号
     */
    @TableField("DETAIL_CODE")
    private String detailCode;

    /**
     * 打印次数
     */
    @TableField("PRINT_COUNT")
    private Integer printCount;

    /**
     * 最后打印账号
     */
    @TableField("LAST_PRINT_USER")
    private String lastPrintUser;

    /**
     * 最后打印时间
     */
    @TableField("LAST_PRINT_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date lastPrintDate;


    @TableField("LABEL_PARAMS")
    private String labelParams;

    @TableField("PDF_URL")
    private String pdfUrl;

    /**
     * 状态，用于包装
     */
    @TableField("STATE")
    private String state = "CREATE";

    /**
     * 包装最大数量，用于包装
     */
    @TableField("PACKING_MAX_QUANTITY")
    private BigDecimal packingMaxQuantity;

    /**
     * 包装数量，用于包装
     */
    @TableField("PACKING_QUANTITY")
    private BigDecimal packingQuantity;


}

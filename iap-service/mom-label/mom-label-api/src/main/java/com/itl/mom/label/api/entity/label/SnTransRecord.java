package com.itl.mom.label.api.entity.label;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 条码转移记录
 * @author dengou
 * @date 2021/11/1
 */
@Data
@TableName("z_sn_trans_record")
public class SnTransRecord {

    /**
     * 主键
     * */
    @TableId(value = "id", type = IdType.UUID)
    @ApiModelProperty(value = "主键")
    private String id;


    /**
     * 条码号
     * */
    @TableField("sn")
    @ApiModelProperty(value = "条码号")
    private String sn;
    /**
     * 条码id
     * */
    @TableField("sn_bo")
    @ApiModelProperty(value = "条码id")
    private String snBo;

    /**
     * 转移前工单号
     * */
    @TableField("old_order_no")
    @ApiModelProperty(value = "转移前工单号")
    private String oldOrderNo;

    /**
     * 转移后工单号
     * */
    @TableField("new_order_no")
    @ApiModelProperty(value = "转移后工单号")
    private String newOrderNo;

    /**
     * 转移日期
     * */
    @TableField("trans_date")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    @ApiModelProperty(value = "转移日期")
    private Date transDate;

    /**
     * 用户账号
     * */
    @TableField("user_name")
    @ApiModelProperty(value = "用户账号")
    private String userName;

    /**
     * 用户姓名
     * */
    @TableField("real_name")
    @ApiModelProperty(value = "用户姓名")
    private String realName;

    /**
     * 当前工单
     * */
    @TableField(exist = false)
    @ApiModelProperty(value = "当前工单")
    private String currentOrderNo;

    /**
     * 工厂id
     * */
    @TableField("site")
    @ApiModelProperty(value = "工厂id")
    private String site;

    public SnTransRecord() {
    }

    @Builder
    public SnTransRecord(String id, String sn, String snBo, String oldOrderNo, String newOrderNo, Date transDate, String userName, String realName, String site, String oldOrderId, String newOrderId) {
        this.id = id;
        this.sn = sn;
        this.snBo = snBo;
        this.oldOrderNo = oldOrderNo;
        this.newOrderNo = newOrderNo;
        this.transDate = transDate;
        this.userName = userName;
        this.realName = realName;
        this.site = site;
    }
}

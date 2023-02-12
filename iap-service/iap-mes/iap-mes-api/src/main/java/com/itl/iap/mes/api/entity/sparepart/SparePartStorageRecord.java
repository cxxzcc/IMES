package com.itl.iap.mes.api.entity.sparepart;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 备件出入库记录
 * @author dengou
 * @date 2021/9/17
 */
@TableName("m_spare_part_storage_record")
@Data
public class SparePartStorageRecord {

    @TableId(value = "ID", type = IdType.UUID)
    @ApiModelProperty(value="id")
    private String id;
    /**
     * 工单类型
     * */
    @TableField("TYPE")
    @ApiModelProperty(value="工单类型")
    private String type;
    /**
     * 工单编号
     * */
    @TableField("ORDER_NO")
    @ApiModelProperty(hidden = true)
    private String orderNo;
    /**
     * 经办人,用户username
     * */
    @TableField("AGENT")
    @ApiModelProperty(value="经办人")
    private String agent;
    /**
     * 关联单号, 比如出库关联维修单
     * */
    @TableField("REFERENCE_ORDER_NO")
    @ApiModelProperty(value="关联单号")
    private String referenceOrderNo;
    /**
     * 部门id
     * */
    @TableField("ORGANIZATION_ID")
    @ApiModelProperty(value="部门id")
    private String organizationId;
    /**
     * 客户
     * */
    @TableField("CUSTOMER")
    @ApiModelProperty(value="客户id")
    private String customer;
    /**
     * 创建人
     * */
    @TableField("CREATE_USER")
    @ApiModelProperty(hidden = true)
    private String createUser;
    /**
     * 出库/入库时间
     * */
    @TableField("RECORD_TIME")
    @ApiModelProperty(value="出库/入库时间，不填默认当前时间")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    private Date recordTime;
    /**
     * 创建时间
     * */
    @TableField("CREATE_TIME")
    @ApiModelProperty(hidden = true)
    private Date createTime;

    /**
     * 出库/入库标识, true=入库，false=出库
     * */
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private Boolean isIn;

    /**
     * 出入库详情列表
     * */
    @TableField(exist = false)
    @ApiModelProperty(value="出入库详情列表")
    private List<SparePartStorageRecordDetail> detailList;

    /**
     * 备注
     * */
    @TableField("REMARK")
    @ApiModelProperty(value="备注")
    private String remark;



    /**
     * 仓库id， 入库用
     * */
    @TableField(exist = false)
    @ApiModelProperty(value="仓库id, 入库用")
    private String wareHouseId;
    /**
     * 仓库名称
     * */
    @TableField(exist = false)
    @ApiModelProperty(value="仓库名称, 入库用")
    private String wareHouseName;
}

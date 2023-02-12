package com.itl.mes.me.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * (MeSnDataCollection)表实体类
 *
 * @author cch
 * @since 2021-06-02 14:23:09
 */
@Data
@TableName("me_sn_data_collection_temp")
@ApiModel(value = "me_sn_data_collection_temp")
@Accessors(chain = true)
public class MeSnDataCollectionTemp extends Model<MeSnDataCollectionTemp> {
    @TableId(type = IdType.AUTO)
    private String id;
    @TableField("operation_id")
    @ApiModelProperty(value = "工序id")
    private String operationId;

    @TableField("operation_type")
    @ApiModelProperty(value = "操作类型(数据判定/数据收集)")
    private String operationType;
    @TableField("sn")
    @ApiModelProperty(value = "条码")
    private String sn;
    @TableField("remark")
    @ApiModelProperty(value = "备注")
    private String remarks;
    @TableField("text")
    @ApiModelProperty(value = "文本输入")
    private String text;
    @TableField("num")
    @ApiModelProperty(value = "测量值")
    private Integer num;
    @TableField("[option]")
    @ApiModelProperty(value = "选项")
    private String option;
    @TableField("result")
    @ApiModelProperty(value = "判定结果")
    private String result;
    @TableField("item_id")
    @ApiModelProperty(value = "项目id")
    private String itemId;

    @TableField(exist = false)
    @ApiModelProperty("ncLogs")
    private MeSfcNcLog ncLogs;

}

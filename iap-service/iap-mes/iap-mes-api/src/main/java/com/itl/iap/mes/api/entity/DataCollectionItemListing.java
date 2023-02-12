package com.itl.iap.mes.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("m_data_collection_item_listing")
@ApiModel("collectionItem")
public class DataCollectionItemListing {

    @TableId(type = IdType.UUID)
    private String id;

    @ApiModelProperty("序号")
    @TableField("serial")
    private Integer serial;

    @ApiModelProperty("键")
    @TableField("keyValue")
    private String keyValue;

    @ApiModelProperty("文本")
    @TableField("textValue")
    private String textValue;

    @ApiModelProperty("主表id")
    @TableField("dataCollectionItemId")
    private String dataCollectionItemId;

    @ApiModelProperty("是否默认 1 是 0否")
    @TableField("isDefault")
    private String isDefault;

}

package com.itl.iap.mes.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itl.iap.common.base.dto.MesFilesVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("m_repair_upkeep_execute_item")
public class UpkeepExecuteItem {
    private static final long serialVersionUID = -30729856515700265L;

    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 保养执行主键
     */
    @TableField("upkeepExecuteId")
    private String upkeepExecuteId;


    /**
     * 项目序号
     */
    @TableField("itemCode")
    private Integer itemCode;


    @ApiModelProperty("项目描述")
    @TableField("remark")
    private String remark;

    /**
     * 项目名称
     */
    @TableField("itemName")
    private String itemName;

    /**
     * 项目类型
     */
    @TableField("type")
    private Integer type;

    /**
     * 最大值
     */
    @TableField("maxNum")
    private Integer maxNum;

    /**
     * 最小值
     */
    @TableField("minNum")
    private Integer minNum;

    @TableField("itemValue")
    private String itemValue;

    @ApiModelProperty("数据收集组项目id")
    @TableField("dataCollectionItemId")
    private String dataCollectionItemId;

    @TableField(exist = false)
    @ApiModelProperty( value = "附件列表, pics=图片列表， docs=文档列表" )
    private List<MesFilesVO> mesFiles;

    @ApiModelProperty("图片地址")
    @TableField("imgSrc")
    private String imgSrc;

    @TableField(exist = false)
    private List<DataCollectionItemListing> dataCollectionItemListingList;


}

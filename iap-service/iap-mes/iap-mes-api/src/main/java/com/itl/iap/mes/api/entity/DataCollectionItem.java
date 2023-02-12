package com.itl.iap.mes.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itl.iap.common.base.dto.MesFilesVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("m_data_collection_item")
@ApiModel("DataCollectionItem")
public class DataCollectionItem {
    private static final long serialVersionUID = -30729856515700265L;

    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 项目序号
     */
    @TableField("itemNo")
    private Integer itemNo;

    @ApiModelProperty("图片地址")
    @TableField("imgSrc")
    private String imgSrc;

    @TableField(exist = false)
    @ApiModelProperty( value = "附件列表, pics=图片列表， docs=文档列表" )
    private List<MesFilesVO> mesFiles;
    /**
     * 项目名称
     */
    @TableField("itemName")
    private String itemName;

    /**
     * 项目描述
     */
    @TableField("remark")
    private String remark;

    /**
     * 项目类型
     */
    @TableField("itemType")
    private Integer itemType;

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

    /**
     * 主表id
     */
    @TableField("dataCollectionId")
    private String dataCollectionId;

    @TableField(exist = false)
    private List<DataCollectionItemListing> dataCollectionItemListingList;

}

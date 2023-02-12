package com.itl.mes.core.api.entity;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author houfan
 * @since 2021-11-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("m_production_defect_record")
@ApiModel(value="ProductionDefectRecord对象", description="")
public class ProductionDefectRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "工序 编号")
    private String process;

    @ApiModelProperty(value = "工位 编号")
    private String station;

    @ApiModelProperty(value = "缺陷记录")
    private String defectRecords;

    @ApiModelProperty(value = "缺陷代码")
    private String defectCode;

    @ApiModelProperty(value = "缺陷描述")
    private String defectDescription;

    @ApiModelProperty(value = "检验项目描述")
    private String descriptionOfInspectionItems;

    @ApiModelProperty(value = "外键 采集记录查询主键")
    private String collectionRecordId;

    /**
     * 是否处理标识， Y/N
     * */
    private String isHandleFlag;


    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty(value = "创建人用户名")
    private String createUser;

}

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
 *  产品检验记录
 * </p>
 *
 * @author houfan
 * @since 2021-11-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("m_record_of_product_test")
@ApiModel(value="RecordOfProductTest对象", description="")
public class RecordOfProductTest implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "检验项目Id")
    private String projectId;

    @ApiModelProperty(value = "检验项目名称")
    private String projectName;

    @ApiModelProperty(value = "规范上限")
    private String uppperLimit;

    @ApiModelProperty(value = "规范下限")
    private String lowerLimit;

    @ApiModelProperty(value = "测试值")
    private String test;

    @ApiModelProperty(value = "工序 编号")
    private String process;

    @ApiModelProperty(value = "工位 编号")
    private String station;

    @ApiModelProperty(value = "检验结果")
    private String result;

    @ApiModelProperty(value = "检验人")
    private String surveyor;

    /**
     * 生产采集记录主键
     * {@link ProductionCollectionRecord#id}
     * */
    @ApiModelProperty(value = "生产采集记录主键")
    private String productionCollectionRecordId;

    /**
     * 采集记录主键
     * {@link CollectionRecord#id}
     * */
    @ApiModelProperty(value = "外键 采集记录查询主键")
    private String collectionRecordId;


    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty(value = "创建人用户名")
    private String createUser;

}

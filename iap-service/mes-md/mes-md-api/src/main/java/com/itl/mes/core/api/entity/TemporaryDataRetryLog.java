package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 过站暂存数据重传记录
 * </p>
 *
 * @author dengou
 * @since 2021-12-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="MTemporaryDataRetryLog对象", description="")
@TableName("m_temporary_data_retry_log")
public class TemporaryDataRetryLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "暂存数据id")
    private String temporaryDataId;

    @ApiModelProperty(value = "重传次数")
    private Integer retryCount;

    @ApiModelProperty(value = "最后返回结果")
    private String lastResultMsg;

    @ApiModelProperty(value = "更新人用户名")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}

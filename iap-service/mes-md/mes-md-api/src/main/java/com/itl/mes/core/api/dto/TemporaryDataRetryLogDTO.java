package com.itl.mes.core.api.dto;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.mes.core.api.constant.TemporaryDataTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 过站重传记录dto
 * @author dengou
 * @date 2021/12/8
 */
@Data
public class TemporaryDataRetryLogDTO {


    @ApiModelProperty(value = "sn")
    private String sn;
    @ApiModelProperty(value = "暂存数据id")
    private String temporaryDataId;

    @ApiModelProperty(value = "暂存数据")
    private String content;

    /**
     * 暂存数据类型
     * {@link TemporaryDataTypeEnum#getCode()}
     * */
    @ApiModelProperty(value = "类型")
    private String type;

    /**
     * 暂存数据类型
     * {@link TemporaryDataTypeEnum#getDesc()}
     * */
    @ApiModelProperty(value = "类型说明")
    private String typeDesc;

    @ApiModelProperty(value = "保存时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private Date saveTime;


    @ApiModelProperty(value = "是否重传 Y/N")
    private String isRetryFlag;

    @ApiModelProperty(value = "重传次数")
    private Integer retryCount;


    @ApiModelProperty(value = "最后返回结果")
    private String lastResultMsg;

    @ApiModelProperty(value = "更新人用户名")
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private Date updateTime;

}

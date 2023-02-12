package com.itl.iap.mes.api.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.iap.mes.api.entity.CheckExecuteItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author cjq
 * @Date 2021/12/2 10:26 上午
 * @Description TODO
 */
@Data
@ApiModel("点检工单包含计划")
public class CheckExecuteAndPlanQueryDTO {

    @ApiModelProperty("生效开始时间")
    private String operaStartTime;

    @ApiModelProperty("生效结束时间")
    private String operaEndTime;


    private String site;

}

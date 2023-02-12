package com.itl.mes.core.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @auth liuchenghao
 * @date 2021/4/6
 */
@Data
@ApiModel(value = "MeProductStatusVo", description = "产品状态查询返回实体")
public class MeProductStatusVo {
    @ApiModelProperty("sn")
    private String sn;
    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("elementCode")
    private String elementCode;
    @ApiModelProperty("state")
    private String state;
    @ApiModelProperty("currentOperation")
    private String currentOperation;
    @ApiModelProperty("nextOperation")
    private String nextOperation;
    @ApiModelProperty("下工序id")
    private String nextOperationId;
    @ApiModelProperty("currentPlStation")
    private String currentPlStation;
    @ApiModelProperty("productName")
    private String productName;
    @ApiModelProperty("currentPerson")
    private String currentPerson;
    @ApiModelProperty("currentD")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date currentD;
    @ApiModelProperty("done")
    private String done;
    @ApiModelProperty("onLine")
    private String onLine;
    @ApiModelProperty("done")
    private String hold;
    @ApiModelProperty("item")
    private String item;
    @ApiModelProperty("snState")
    private String snState;

    @ApiModelProperty("productStateBo")
    private String productStateBo;

    @ApiModelProperty(value = "工单bo")
    private String shopOrderBo;

    @ApiModelProperty(value = "标签打印Bo")
    private String labelPrintBo;

    @ApiModelProperty(value = "条码bo")
    private String snBo;



}

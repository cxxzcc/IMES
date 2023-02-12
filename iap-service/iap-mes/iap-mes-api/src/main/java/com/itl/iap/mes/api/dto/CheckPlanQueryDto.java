package com.itl.iap.mes.api.dto;

import com.itl.iap.common.base.mapper.QueryWapper;
import com.itl.iap.common.base.mapper.QueryWapperEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
@ApiModel("点检计划列表查询对象")
public class CheckPlanQueryDto {

    @QueryWapper(queryWapperEnum = QueryWapperEnum.LIKE)
    @ApiModelProperty("点检计划名称")
    private String checkPlanName;

    @QueryWapper
    @ApiModelProperty("数据收集组id")
    private String dataCollectionId;

    @ApiModelProperty("设备编码")
    private String diviceCode;

    @ApiModelProperty("设备名称")
    private String diviceName;

    @ApiModelProperty("设备编码或者名称-模糊查询")
    private String diviceCodeOrName;

    @QueryWapper(value = "t1.DEVICE_ID", queryWapperEnum = QueryWapperEnum.IN)
    @ApiModelProperty("设备条件-勿传")
    private List<String> diviceCondition;

    @QueryWapper(value = "t2.CHECK_USER_ID", queryWapperEnum = QueryWapperEnum.EQ)
    @ApiModelProperty("点检人编码")
    private String checkUserCode;

    @QueryWapper
    @ApiModelProperty("状态 0:未启用 1:启用")
    private Integer state;

    @QueryWapper
    private String siteId;

    @ApiModelProperty("时间类型 0-今日 1-昨日 2-本周 3-本月")
    private String timeType;

}

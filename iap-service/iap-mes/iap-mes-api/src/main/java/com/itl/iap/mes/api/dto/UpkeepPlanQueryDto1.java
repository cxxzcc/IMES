package com.itl.iap.mes.api.dto;

import com.itl.iap.common.base.mapper.QueryWapper;
import com.itl.iap.common.base.mapper.QueryWapperEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
@ApiModel("保养计划查询对象")
public class UpkeepPlanQueryDto1 {

    @QueryWapper(queryWapperEnum = QueryWapperEnum.LIKE)
    @ApiModelProperty("保养计划名称")
    private String upkeepPlanName;

    @QueryWapper
    @ApiModelProperty("数据收集组id")
    private String dataCollectionId;

    @ApiModelProperty("设备编号")
    private String diviceCode;

    @ApiModelProperty("设备名称")
    private String diviceName;

    @ApiModelProperty("设备编号或者名称")
    private String diviceCodeOrName;

    @QueryWapper(value = "t1.DEVICE_ID", queryWapperEnum = QueryWapperEnum.IN)
    @ApiModelProperty("设备条件-勿传")
    private List<String> diviceCondition;

    @QueryWapper(value = "t2.UPKEEP_USER_ID", queryWapperEnum = QueryWapperEnum.EQ)
    @ApiModelProperty("保养人编码")
    private String upkeepUserCode;

    @QueryWapper
    @ApiModelProperty("状态-0未启用 1启用")
    private Integer state;

    @QueryWapper
    private String site;

    @ApiModelProperty("时间类型 0-今日 1-昨日 2-本周 3-本月")
    private String timeType;

    @ApiModelProperty("排序字段")
    private String sortName;

    @ApiModelProperty("排序类型 desc asc")
    private String sortType;


}

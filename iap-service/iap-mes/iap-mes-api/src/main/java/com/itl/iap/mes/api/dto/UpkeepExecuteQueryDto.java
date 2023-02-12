package com.itl.iap.mes.api.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.itl.iap.common.base.mapper.QueryWapper;
import com.itl.iap.common.base.mapper.QueryWapperEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
@ApiModel("保养计划执行列表查询对象")
public class UpkeepExecuteQueryDto {

    @QueryWapper(value = "t1.upkeepPlanName", queryWapperEnum = QueryWapperEnum.LIKE)
    @ApiModelProperty("保养计划名称")
    private String upkeepPlanName;

    @QueryWapper(value = "t.upkeepNo", queryWapperEnum = QueryWapperEnum.LIKE)
    @ApiModelProperty("保养单号")
    private String upkeepNo;

    @ApiModelProperty("设备编号")
    private String diviceCode;

    @ApiModelProperty("设备名称")
    private String diviceName;

    @ApiModelProperty("设备编号或者名称")
    private String diviceCodeOrName;

    @QueryWapper(value = "t.deviceId", queryWapperEnum = QueryWapperEnum.IN)
    @ApiModelProperty("设备条件-勿传")
    private List<String> diviceCondition;

    @QueryWapper(value = "t.state")
    @ApiModelProperty("状态  0 待保养  1完成 2 保养中")
    private Integer state;

    @QueryWapper(value = "t.dataCollectionId")
    @ApiModelProperty("数据收集组id")
    private String dataCollectionId;

    @QueryWapper(value = "t2.UPKEEP_USER_ID")
    @ApiModelProperty("保养人id")
    private String upkeepUserId;

    @ApiModelProperty("时间类型 -今日 -昨日 -本周 -本月")
    private String timeType;

    @QueryWapper(value = "t.site")
    private String site;

    @ApiModelProperty("排序字段")
    private String sortName;

    @ApiModelProperty("排序类型 desc asc")
    private String sortType;

}

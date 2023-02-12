package com.itl.iap.mes.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.iap.mes.api.entity.*;
import com.itl.iap.mes.api.entity.UpkeepPlan;
import com.itl.iap.mes.api.entity.UpkeepUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Data
@ApiModel("设备保养计划保存对象")
public class UpkeepPlanDto {

    @ApiModelProperty("保养计划id")
    private String id;

    @ApiModelProperty("保养计划编码")
    private String planNo;

    @ApiModelProperty("保养计划名称")
    private String upkeepPlanName;

    @ApiModelProperty("设备列表")
    private List<UpkeepDevice> devices;

    @ApiModelProperty("保养人列表")
    private List<UpkeepUser> upkeepUsers;

    @ApiModelProperty("数据收集组id")
    private String dataCollectionId;

    @ApiModelProperty("数据收集组名称")
    private String dataCollectionName;

    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty("结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty("状态")
    private Integer state;

    @ApiModelProperty("间隔  每多长时间执行")
    private Integer cycle;

    @ApiModelProperty("月 天 时  的常量  0月1天2时")
    private Integer ytd;

    @ApiModelProperty("状态名称")
    private String stateName;

    @ApiModelProperty("描述")
    private String remark;

    @ApiModelProperty("定时任务id集合")
    private String jobIds;

    @ApiModelProperty("执行状态   0 未执行   1执行中")
    private Integer runState;

    private String site;


}

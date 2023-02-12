package com.itl.iap.mes.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.iap.common.base.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("m_repair_upkeep_execute")
@ApiModel("保养计划执行实体")
public class UpkeepExecute extends BaseEntity {

    @TableId(type = IdType.UUID)
    private String id;

    @ApiModelProperty("保养单号")
    @TableField("upkeepNo")
    private String upkeepNo;

    @ApiModelProperty("保养计划id")
    @TableField("upkeepPlanId")
    private String upkeepPlanId;

    @ApiModelProperty("设备id")
    @TableField("deviceId")
    private String deviceId;

    @ApiModelProperty("状态  0 保存  1完成..")
    @TableField("state")
    private Integer state;

    @ApiModelProperty("数据收集组id")
    @TableField("dataCollectionId")
    private String dataCollectionId;

    @ApiModelProperty("受理开始时间")
    @TableField("startTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty("受理结束时间")
    @TableField("endTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty("生效开始时间")
    @TableField("operaStartTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date operaStartTime;

    @ApiModelProperty("生效结束时间")
    @TableField("operaEndTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date operaEndTime;

    @ApiModelProperty("任务id")
    @TableField("jobId")
    private String jobId;

    @ApiModelProperty("描述")
    @TableField("remark")
    private String remark;

    @TableField("site")
    private String site;

    /**/

    @ApiModelProperty("点检计划名称")
    @TableField(exist = false)
    private String upkeepPlanName;

    @ApiModelProperty("附件列表")
    @TableField(exist = false)
    private List<MesFiles> mesFiles;

    @ApiModelProperty("项目")
    @TableField(exist = false)
    private List<UpkeepExecuteItem> upkeepExecuteItemList;

    @ApiModelProperty("操作人id")
    @TableField(exist = false)
    private String operaUser;

    @ApiModelProperty("操作人姓名")
    @TableField(exist = false)
    private String operaUserName;

    @ApiModelProperty("保养人列表")
    @TableField(exist = false)
    private List<UpkeepUser> upkeepUserList;

    @ApiModelProperty("设备编号")
    @TableField(exist = false)
    private String code;

    @ApiModelProperty("设备名称")
    @TableField(exist = false)
    private String name;

    @ApiModelProperty("设备类型")
    @TableField(exist = false)
    private String type;

    @ApiModelProperty("设备状态")
    @TableField(exist = false)
    private String deviceState;

    @ApiModelProperty("设备状态名称")
    @TableField(exist = false)
    private String deviceStateName;

    @ApiModelProperty("规格型号")
    @TableField(exist = false)
    private String deviceModel;

    @ApiModelProperty("工位")
    @TableField(exist = false)
    private String station;

    @ApiModelProperty("产线")
    @TableField(exist = false)
    private String productionLine;

    @ApiModelProperty("数据收集组")
    @TableField(exist = false)
    private String dataCollectionName;
}

package com.itl.iap.mes.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.iap.common.base.base.BaseEntity;
import com.itl.iap.mes.api.vo.PlanTimeVo;
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
@TableName("m_repair_upkeep_plan")
@ApiModel("设备保养计划实体")
public class UpkeepPlan extends BaseEntity {

    @TableId(type = IdType.UUID)
    private String id;

    @ApiModelProperty("保养计划编码")
    @TableField("planNo")
    private String planNo;

    @ApiModelProperty("保养计划名称")
    @TableField("upkeepPlanName")
    private String upkeepPlanName;

    @ApiModelProperty("数据收集组id")
    @TableField("dataCollectionId")
    private String dataCollectionId;

    @ApiModelProperty("开始时间")
    @TableField("startTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty("结束时间")
    @TableField("endTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty("状态 1启用 0未启用")
    @TableField("state")
    private Integer state;

    @ApiModelProperty("间隔  每多长时间执行")
    @TableField("cycle")
    private Integer cycle;

    @ApiModelProperty("月 天 时  的常量  0月1天2时")
    @TableField("ytd")
    private Integer ytd;

    @ApiModelProperty("描述")
    @TableField("remark")
    private String remark;

    @ApiModelProperty("定时任务id集合")
    @TableField("jobIds")
    private String jobIds;

    @ApiModelProperty("执行状态   0 未执行   1执行中")
    @TableField("runState")
    private Integer runState;

    @TableField("site")
    private String site;

    /**/
    @ApiModelProperty("保养人列表")
    @TableField(exist = false)
    private List<UpkeepUser> upkeepUsers;

    @ApiModelProperty("保养设备列表")
    @TableField(exist = false)
    private List<UpkeepDevice> upkeepDivices;

    @ApiModelProperty("状态名称")
    @TableField(exist = false)
    private String stateName;

    @ApiModelProperty("数据收集组名称")
    @TableField(exist = false)
    private String dataCollectionName;


    @ApiModelProperty("生成的计划时间工单数据")
    @TableField(exist = false)
    private List<PlanTimeVo> planTimeData;



    @TableField(exist = false)
    @ApiModelProperty("设备编号")
    private String code;

    @TableField(exist = false)
    @ApiModelProperty("设备名称")
    private String name;

    @TableField(exist = false)
    @ApiModelProperty("周期")
    private String cycleName;

    @ApiModelProperty(" 保养人姓名")
    @TableField(exist = false)
    private String upkeepUserName;

}

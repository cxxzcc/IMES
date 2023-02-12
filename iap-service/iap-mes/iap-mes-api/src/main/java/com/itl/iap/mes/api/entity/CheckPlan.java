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
@TableName("m_repair_check_plan")
@ApiModel("设备点检计划实体")
public class CheckPlan extends BaseEntity {

    @TableId(type = IdType.UUID)
    private String id;

    @ApiModelProperty("点检计划编码")
    @TableField("planNo")
    private String planNo;

    @ApiModelProperty("点检计划名称")
    @TableField("checkPlanName")
    private String checkPlanName;

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

    @ApiModelProperty("工厂id")
    @TableField("siteId")
    private String siteId;

    @ApiModelProperty("点检人列表")
    @TableField(exist = false)
    private List<CheckPlanUser> checkUsers;

    @ApiModelProperty("设备列表")
    @TableField(exist = false)
    private List<CheckDevice> checkDevice;

    @ApiModelProperty("timeType 0-今日 1-昨日 2-本周 3-本月")
    @TableField(exist = false)
    private String timeType;

    @ApiModelProperty("状态名称")
    @TableField(exist = false)
    private String stateName;

    @ApiModelProperty("数据收集组名称")
    @TableField(exist = false)
    private String dataCollectionName;


    @ApiModelProperty("生成的计划时间工单数据")
    @TableField(exist = false)
    private List<PlanTimeVo> planTimeData;
}

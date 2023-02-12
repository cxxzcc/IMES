package com.itl.iap.mes.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.iap.mes.api.dto.sparepart.SparePartStorageDetailRequestDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("m_repair_corrective_maintenance")
@ApiModel("设备维修列表查询对象")
public class CorrectiveMaintenance {
    private static final long serialVersionUID = -30729856515700265L;
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 设备类型
     */
    @ApiModelProperty("设备类型")
    @TableField("type")
    private Integer type;
    @TableField("ANDON_BO")
    private String andonBo;

    @TableField(exist = false)
    private String deviceBo;

    /**
     * 设备编号
     */
    @ApiModelProperty("设备编号")
    @TableField("code")
    private String code;

    @ApiModelProperty("设备名称")
    @TableField(exist = false)
    private String deviceName;

    @ApiModelProperty("终端设备型号")
    @TableField(exist = false)
    private String deviceModel;

    @ApiModelProperty("设备类型")
    @TableField("deviceType")
    private String deviceType;

    @ApiModelProperty("故障编码")
    @TableField("faultCode")
    private String faultCode;

    @ApiModelProperty("维修方法")
    @TableField("repairMethod")
    private String repairMethod;

    @ApiModelProperty("异常描述")
    @TableField("faultDesc")
    private String faultDesc;


    @ApiModelProperty("维修类型")
    @TableField("repairType")
    private String repairType;

    @ApiModelProperty("维修状态 0待维修 1维修中 2维修完成 3撤销维修")
    @TableField("state")
    private Integer state;

    @ApiModelProperty("产线")
    @TableField("productionLine")
    private String productionLine;

    @ApiModelProperty("产线名称")
    @TableField(exist = false)
    private String productionLineName;

    @ApiModelProperty("工位")
    @TableField("station")
    private String station;

    @ApiModelProperty("工位名称")
    @TableField(exist = false)
    private String stationName;

    @ApiModelProperty("发生时间")
    @TableField("happenTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date happenTime;

    @ApiModelProperty("时间类型 0-今日 1-昨日 2-本周 3-本月")
    @TableField(exist = false)
    private String timeType;

    @ApiModelProperty("开始时间")
    @TableField(exist = false)
    private String startTime;

    @ApiModelProperty("结束时间")
    @TableField(exist = false)
    private String endTime;

    /**
     * 维修人员id
     */
    @ApiModelProperty("实际维修人员id")
    @TableField("repairUserId")
    private String repairUserId;

    /**
     * 维修人员姓名
     */
    @ApiModelProperty("实际维修人员姓名")
    @TableField("repairUserName")
    private String repairUserName;


    /**
     * 维修开始时间
     */
    @ApiModelProperty("维修执行开始时间")
    @TableField("repairStartTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date repairStartTime;

    /**
     * 维修结束时间
     */
    @ApiModelProperty("维修执行结束时间")
    @TableField("repairEndTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date repairEndTime;

    @ApiModelProperty("附件信息")
    @TableField(exist = false)
    private Map<String, List<MesFiles>> mesFiles;


    @TableField("siteId")
    private String siteId;


    @ApiModelProperty("该条数据创建时间")
    @TableField("createTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;


    /**
     * 故障等级
     */
    @ApiModelProperty("故障等级  oneLevel一级 twoLevel二级 threeLevel三级")
    @TableField("faultLevel")
    private String faultLevel;


    /**
     * 是否停机
     */
    @ApiModelProperty("紧急程度  normal一般 urgent紧急 other其它")
    @TableField("urgencyLevel")
    private String urgencyLevel;

    /**
     * 报修单号
     */
    @ApiModelProperty(value = "报修单号", required = true)
    @TableField("repairNo")
    private String repairNo;

    @ApiModelProperty("维修人员列表")
    @TableField(exist = false)
    private List<CorrectiveUser> repairUsers;

    /**
     * 备件数量详情
     */
    @ApiModelProperty("备件详情")
    @TableField(exist = false)
    private List<SparePartStorageDetailRequestDTO> details;


    /**
     * 报修人员id
     */
    @ApiModelProperty("报修人员id")
    @TableField("repairApplicantId")
    private String repairApplicantId;

    /**
     * 报修人员姓名
     */
    @ApiModelProperty("报修人员姓名")
    @TableField("repairApplicant")
    private String repairApplicant;
    /**
     * 是否停机
     */
    @ApiModelProperty("是否停机 0停机 1不停机")
    @TableField("halt")
    private Integer halt;


    /**
     * 维修费用
     */
    @ApiModelProperty("维修费用")
    @TableField("maintenanceCost")
    private BigDecimal maintenanceCost;

    /**
     * 设备封面图
     */
    @ApiModelProperty("设备封面图")
    @TableField(exist = false)
    private String coverImg;


    /**
     * 当前用户id, 传值则查当前用户， 不传查所有   （工单用户过滤查询）
     */
    @ApiModelProperty(value = "当前用户id", hidden = true)
    @TableField(exist = false)
    private String rlatedToMe;


    /**
     * 异常类型
     */
    @ApiModelProperty("异常类型")
    @TableField(exist = false)
    private String errorTypeName;

    /**
     * 保养Id
     */
    @ApiModelProperty("保养Id")
    @TableField(exist = false)
    private String upkeepId;

    /**
     * 点检Id
     */
    @ApiModelProperty("点检Id")
    @TableField(exist = false)
    private String checkId;

}

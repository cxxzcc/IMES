package com.itl.mes.core.api.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.itl.iap.common.base.dto.MesFilesVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author sky,
 * @date 2019/6/17
 * @time 13:36
 */
@Data
@ApiModel(value = "DeviceVo", description = "保存设备数据使用")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeviceVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="bo")
    private String bo;

    @ApiModelProperty(value="设备编号【UK】")
    @Excel(name = "设备编码")
    private String device;

    @ApiModelProperty(value="设备名称")
    @Excel(name = "设备名称")
    private String deviceName;

    @ApiModelProperty(value="设备描述")
    @Excel(name = "设备描述")
    private String deviceDesc;

    @ApiModelProperty(value="设备型号")
    @Excel(name = "规格型号")
    private String deviceModel;

    @ApiModelProperty(value="状态")
    private String state;

    @ApiModelProperty(value="是否为加工设备")
    @Excel(name = "是否为加工设备", replace = {"是_Y", "否_N"})
    private String isProcessDevice;

    @ApiModelProperty(value="产线")
    @Excel(name = "产线")
    private String productLine;

    @ApiModelProperty(value="工位")
    @Excel(name = "工位")
    private String station;

    @ApiModelProperty(value="安装地点")
    private String location;

    @ApiModelProperty(value="资产编号")
    private String assetsCode;

    @ApiModelProperty(value="生产厂家")
    @Excel(name = "生产厂家")
    private String manufacturer;

    @ApiModelProperty(value="投产日期")
    @JsonFormat(
            pattern = "yyyy-MM-dd",
            timezone = "GMT+8"
    )
    private Date validStartDate;

    @Excel(name = "投产日期")
    @ApiModelProperty(hidden = true)
    private String validStartDateStr;

    @ApiModelProperty(value="结束日期")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date validEndDate;

    @ApiModelProperty(value="备注")
    private String memo;

    @ApiModelProperty(value="修改日期")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss.SSS",
            timezone = "GMT+8"
    )
    private Date modifyDate;

    @ApiModelProperty( value="可分配设备类型" )
    private List<DeviceTypeSimplifyVo> availableDeviceTypeList;

    @ApiModelProperty( value="已分配设备类型" )
    private List<DeviceTypeSimplifyVo> assignedDeviceTypeList;

    @ApiModelProperty( value = "自定义数据属性和值" )
    private List<CustomDataAndValVo> customDataAndValVoList;

    @ApiModelProperty( value = "自定义数据属性和值，保存时传入" )
    private List<CustomDataValVo> customDataValVoList;

    @ApiModelProperty( value = "螺杆组合" )
    private String screwCombination;
    @ApiModelProperty( value = "使用寿命" )
    @Excel(name = "使用年限")
    private Integer usefulLife;
    @ApiModelProperty( value = "封面图" )
    private String coverImg;

    @ApiModelProperty( value = "附件列表, pics=图片列表， docs=文档列表" )
    private Map<String, List<MesFilesVO>> mesFiles;

    @ApiModelProperty(value = "负责人")
    @Excel(name = "负责人")
    private String incharge;

    @ApiModelProperty(value = "联系方式")
    @Excel(name = "联系电话")
    private String tel;


    @ApiModelProperty( value = "购买日期" )
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    private Date buyDate;

    @ApiModelProperty( value = "购买日期", hidden = true)
    @Excel(name = "购买日期")
    private String buyDateStr;

    /**
     * 状态说明
     * */
    @ApiModelProperty( value = "状态说明" )
    private String stateDesc;

    /**
     * 供应商
     * */
    @TableField("SUPPLIER")
    @ApiModelProperty(value="供应商id")
    @Excel(name = "供应商编码")
    private String supplier;
    /**
     * 供应商
     * */
    @TableField(exist = false)
    @ApiModelProperty(value="供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "site")
    private String site;

    /**
     * 设备类型， 导入用
     * */
    @Excel(name = "设备类型")
    private String deviceType;

    //基地

    @ApiModelProperty(value = "仪器位置")
    private String baseLocation;

    @ApiModelProperty(value = "基地id")
    private String baseId;

    @ApiModelProperty(value = "基地code")
    private String baseCode;

    @ApiModelProperty(value = "基地名称")
    private String baseName;

    @ApiModelProperty(value = "是否基准仪器 1是 0否")
    private String isStandard;

    @ApiModelProperty(value = "仪器类型id")
    private String instrumentTypeId;

    @ApiModelProperty(value = "仪器类型名称")
    private String instrumentTypeName;
}

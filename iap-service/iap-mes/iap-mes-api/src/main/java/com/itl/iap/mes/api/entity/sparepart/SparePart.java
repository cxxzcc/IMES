package com.itl.iap.mes.api.entity.sparepart;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.iap.mes.api.entity.MesFiles;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import com.itl.mes.core.api.vo.DeviceVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 备件
 * @author dengou
 * @date 2021/9/17
 */
@TableName("m_spare_part")
@Data
public class SparePart {


    @TableId(value = "ID", type = IdType.UUID)
    @ApiModelProperty(value="主键")
    private String id;

    /**
     * 工厂
     * */
    @TableField("SITE")
    @ApiModelProperty(value="工厂")
    private String site;
    /**
     * 备件编号
     * */
    @TableField("SPARE_PART_NO")
    @ApiModelProperty(value="备件编号")
    @NotNull(message = "备件编号不能为空")
    @Excel(name = "备件编号")
    private String sparePartNo;
    /**
     * 备件名称
     * */
    @TableField("NAME")
    @ApiModelProperty(value="备件名称")
    @NotEmpty(message = "备件名称不能为空")
    @Excel(name = "备件名称")
    private String name;

    /**
     * 关联设备编码
     * */
    @TableField(exist = false)
    @ApiModelProperty(value="关联设备编码")
    @Excel(name = "关联设备编码")
    private String deviceCode;
    /**
     * 规格型号
     * */
    @TableField("SPEC")
    @ApiModelProperty(value="规格型号")
    @Excel(name = "规格型号")
    private String spec;
    /**
     * 类型
     * */
    @TableField("TYPE")
    @ApiModelProperty(value="类型")
    @Excel(name = "备件类型")
    private String type;
    /**
     * 类型说明
     * */
    @TableField(exist = false)
    @ApiModelProperty(value="类型说明")
    private String typeDesc;
    /**
     * 库存上限
     * */
    @TableField("INVENTORY_MAX")
    @ApiModelProperty(value="库存上限")
    @Min(value = 0, message = "库存上限不能小于0")
    @Excel(name = "库存上限")
    private Integer inventoryMax;
    /**
     * 库存下限
     * */
    @TableField("INVENTORY_MIN")
    @ApiModelProperty(value="库存下限")
    @Min(value = 0, message = "库存下限不能小于0")
    @Excel(name = "库存下限")
    private Integer inventoryMin;
    /**
     * 单价
     * */
    @TableField("PRICE")
    @ApiModelProperty(value="单价")
    @Min(value = 0, message = "单价不能小于0")
    private BigDecimal price;
    /**
     * 计量单位
     * */
    @TableField("UNIT")
    @ApiModelProperty(value="计量单位")
    @Excel(name = "计量单位")
    private String unit;
    /**
     * 供应商
     * */
    @TableField("SUPPLIER")
    @ApiModelProperty(value="供应商id")
    @Excel(name = "供应商")
    private String supplier;
    /**
     * 供应商
     * */
    @TableField(exist = false)
    @ApiModelProperty(value="供应商名称")
    private String supplierName;
    /**
     * 生产厂家
     * */
    @TableField("PRODUCER")
    @ApiModelProperty(value="生产厂家")
    @Excel(name = "生产厂家")
    private String producer;
    /**
     * 负责人
     * */
    @TableField("PRINCIPAL")
    @ApiModelProperty(value="负责人")
    @Excel(name = "负责人")
    private String principal;
    /**
     * 电话
     * */
    @TableField("PRINCIPAL_PHONE")
    @ApiModelProperty(value="电话")
    @Excel(name = "联系电话")
    private String principalPhone;
    /**
     * 备注
     * */
    @TableField("REMARK")
    @ApiModelProperty(value="备注")
    @Excel(name = "备注")
    private String remark;

    /**
     * 封面图。取第一张
     * */
    @TableField(value = "COVER_IMG", strategy = FieldStrategy.NOT_NULL)
    @ApiModelProperty(value="封面图")
    private String coverImg;
    /**
     * 购买时间
     * */
    @TableField("BUY_DATE")
    @ApiModelProperty(value="购买时间")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    private Date buyDate;

    @ApiModelProperty( value = "购买日期", hidden = true)
    @Excel(name = "购买日期")
    @TableField(exist = false)
    private String buyDateStr;
    /**
     * 创建时间
     * */
    @TableField("CREATE_TIME")
    @ApiModelProperty(value="创建时间", hidden = true)
    private Date createTime;
    /**
     * 创建人
     * */
    @TableField("CREATE_USER")
    @ApiModelProperty(value="创建人", hidden = true)
    private String createUser;
    /**
     * 更新时间
     * */
    @TableField("UPDATE_TIME")
    @ApiModelProperty(value="更新时间", hidden = true)
    private Date updateTime;
    /**
     * 更新人
     * */
    @TableField("UPDATE_USER")
    @ApiModelProperty(value="更新人", hidden = true)
    private String updateUser;

    /**
     * 当前库存（总库存）
     * */
    @TableField(exist = false)
    @ApiModelProperty(value="当前库存（总库存）")
    private Integer currentInventory;
    /**
     * 各仓库库存信息
     * */
    @TableField(exist = false)
    @ApiModelProperty(value="各仓库库存信息，详情接口返回")
    private List<SparePartInventory> sparePartInventories;
    /**
     * 图片
     * */
    @TableField(exist = false)
    @ApiModelProperty(value="图片")
    private Map<String, List<MesFiles>> mesFiles;

    /**
     * 设备ids
     * */
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private List<String> deviceIdList;



    /**
     * 设备信息（app展示设备名称）
     * */
    @TableField(exist = false)
    @ApiModelProperty(hidden = true)
    private List<DeviceVo> deviceVoList;

    /**
     * 设备ids,逗号分隔
     * */
    @TableField(exist = false)
    @ApiModelProperty(value="设备ids")
    private String deviceIds;


    /**
     * 自定义列和数据
     * */
    @TableField(exist = false)
    @ApiModelProperty(value="自定义列和数据")
    private List<CustomDataAndValVo> customDataAndValVos;

}

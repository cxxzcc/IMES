package com.itl.mes.pp.api.entity.schedule;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liuchenghao
 * @date 2020/11/11 16:20
 */

/**
 * 手动排程：排程产线关联表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("p_schedule_production_line")
public class ScheduleProductionLineEntity {


    @TableId(type = IdType.UUID)
    private String bo;

    @TableField("PRODUCTION_LINE_BO")
    private String productionLineBo;

    @TableField("SHIFT_BO")
    private String shiftBo;

    @TableField("STATION_BO")
    private String stationBo;

    @TableField("WORK_SHOP_BO")
    private String workShopBo;

    @TableField("WORK_SHOP")
    private String workShop;

    @TableField("QUANTITY")
    private BigDecimal quantity;

    @TableField("START_DATE")
    private Date startDate;

    @TableField("END_DATE")
    private Date endDate;

    @TableField("SCHEDULE_BO")
    private String scheduleBo;

    @TableField("SHOP_ORDER")
    private String shopOrder;

    @TableField("STATE")
    private String state;

    @TableField("CREATE_DATE")
    private Date createDate;

    @TableField("CREATE_USER")
    private String createUser;

    @TableField("MODIFY_DATE")
    private Date modifyDate;

    @TableField("MODIFY_USER")
    private String modifyUser;

}

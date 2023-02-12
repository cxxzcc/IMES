package com.itl.iap.system.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.itl.iap.common.base.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @auth liuchenghao
 * @date 2020/12/18
 */
@Data
@Accessors(chain = true)
@TableName("iap_sys_user_station_t")
public class IapSysUserStationT extends BaseModel {

    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 用户ID
     */
    @TableField("USER_ID")
    private String userId;


    /**
     * 工位Id
     */
    @TableField("STATION_BO")
    private String stationBo;

    /**
     * 默认工位
     */
    @TableField("IS_DEFAULT")
    //@EnumValue(intValues = {1, 0}, message = "默认工位参数只能是0或1")
    private int isDefault;

    /**
     * 工位序号
     */
    @TableField("SERIAL_NUM")
    private int serialNum;

    /**
     * 工厂
     */
    @TableField("SITE")
    private String site;
}

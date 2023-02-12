package com.itl.mes.me.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 条码过站记录
 * </p>
 *
 * @author dengou
 * @since 2021-12-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="MeSnCrossStationLog对象", description="条码过站记录")
@TableName("me_sn_cross_station_log")
public class MeSnCrossStationLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "条码")
    private String sn;

    @ApiModelProperty(value = "工厂id")
    private String site;

    @ApiModelProperty(value = "工序bo")
    private String operationBo;

    @ApiModelProperty(value = "工位")
    private String station;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人用户名")
    private String createUser;


    public MeSnCrossStationLog() {
    }

    @Builder
    public MeSnCrossStationLog(String id, String sn, String site, String operationBo, String station, Date createTime, String createUser) {
        this.id = id;
        this.sn = sn;
        this.site = site;
        this.operationBo = operationBo;
        this.station = station;
        this.createTime = createTime;
        this.createUser = createUser;
    }
}

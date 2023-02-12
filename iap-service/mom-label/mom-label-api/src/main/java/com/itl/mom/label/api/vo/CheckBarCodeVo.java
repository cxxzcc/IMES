package com.itl.mom.label.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @auth liuchenghao
 * @date 2021/1/26
 */
@Data
@ApiModel(value = "CheckBarCodeVo",description = "检查条码返回的实体")
public class CheckBarCodeVo implements Serializable {

    @ApiModelProperty(value = "标签打印详细BO，主键")
    private String bo;

    @ApiModelProperty(value = "包含的物料数量")
    private Integer amount;


    @ApiModelProperty(value = "物料BO")
    private String itemBo;

    @ApiModelProperty(value = "物料编码")
    private String item;

    @ApiModelProperty(value = "物料名称")
    private String itemName;

    @ApiModelProperty(value = "物料描述")
    private String itemDesc;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "可使用的物料数量")
    private Integer useAmount;

    /**
     * NEW新建；标签条码刚生成
     * LOADED装载；标签已装箱或已有实物
     * EMPTY空置；标签未装箱或无实物
     * END结束；标签已发出或失效
     * WIP在制；标签生产中
     * RESERVED保留；标签预留，占不可用
     * SCRAPPED报废，标签不可以已报废
     * UP  上架
     * DOWN  下架
     */
    @ApiModelProperty(value= "状态（NEW新建;LOADED装载;EMPTY空置;END结束;WIP在制;RESERVED保留;SCRAPPED报废;UP上架;DOWN下架）")
    private String state;

    /**
     * OK合格，标签对应物料合格
     * NG不合格，
     * FROZEN冻结，用于盘点或物料判定不可用
     * CONCESSION让步放行
     * SCRAPPED报废
     * PENDING待定
     */
    @ApiModelProperty("OK合格;NG不合格;FROZEN冻结;CONCESSION让步放行;SCRAPPED报废;PENDING待定")
    private String qualityStatus;

}

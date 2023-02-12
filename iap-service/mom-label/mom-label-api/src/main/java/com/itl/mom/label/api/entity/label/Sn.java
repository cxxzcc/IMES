package com.itl.mom.label.api.entity.label;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 条码信息表
 * </p>
 *
 * @author space
 * @since 2019-10-25
 */
@TableName("z_sn")
@ApiModel(value="Sn",description="条码信息表")
@Data
@Accessors(chain = true)
public class Sn extends Model<Sn> {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value="SN:SITE,SN【PK】")
    @Length( max = 100 )
    @TableId(value = "BO", type = IdType.INPUT)
    private String bo;

    @ApiModelProperty(value="站点【UK】")
    @Length( max = 32 )
    @TableField("SITE")
    private String site;

    @ApiModelProperty(value="条码【UK】")
    @Length( max = 64 )
    @TableField("SN")
    private String sn;

    @ApiModelProperty(value="原条码【UK】")
    @Length( max = 64 )
    @TableField("OLD_SN")
    private String oldSn;

    @ApiModelProperty(value="补码状态(Y:补码;N:正常码)")
    @Length( max = 1 )
    @TableField("COMPLEMENT_CODE_STATE")
    private String complementCodeState;

    @ApiModelProperty(value="物料【UK】")
    @Length( max = 100 )
    @TableField("ITEM_BO")
    private String itemBo;

    @ApiModelProperty(value="原始物料BO")
    @Length( max = 100 )
    @TableField("ORIGINAL_ITEM_BO")
    private String originalItemBo;

    @ApiModelProperty(value="数量")
    @TableField("QTY")
    private BigDecimal qty;

    @ApiModelProperty(value="工单号")
    @Length( max = 64 )
    @TableField("SHOP_ORDER")
    private String shopOrder;

    @ApiModelProperty(value="任务号")
    @Length( max = 64 )
    @TableField("TASK_NO")
    private String taskNo;

    @ApiModelProperty(value="物料清单")
    @Length( max = 100 )
    @TableField("BOM_BO")
    private String bomBo;

    @ApiModelProperty(value="成型工单")
    @Length( max = 64 )
    @TableField("SHAP_ORDER_BO")
    private String shapOrderBo;

    @ApiModelProperty(value="喷釉工单")
    @Length( max = 64 )
    @TableField("PAINT_ORDER_BO")
    private String paintOrderBo;

    @ApiModelProperty(value="烧成工单")
    @Length( max = 64 )
    @TableField("FIRE_ORDER_BO")
    private String fireOrderBo;

    @ApiModelProperty(value="包装工单")
    @Length( max = 64 )
    @TableField("PACK_ORDER_BO")
    private String packOrderBo;

    @ApiModelProperty(value="生产线")
    @Length( max = 100 )
    @TableField("PRODUCT_LINE_BO")
    private String productLineBo;

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
    @ApiModelProperty(value= "状态（401新建402出烘干室绑车403青坯入库 404青坯出库 405修检扫描 406擦绺扫描 407 喷釉扫码408白坯入库409白坯出库410装窑扫码411烧成入窑412烧成开窑413成检扫描414回烧接收415回烧装窑416回烧出窑417装箱扫描 421报废422保留423青坯返坯 424白坯返坯 425破损 426 冻结418回烧补瓷）")
    @Length( max = 10 )
    @TableField("STATE")
    private String state;

    /**
     * OK合格，标签对应物料合格
     * NG不合格，
     * FROZEN冻结，用于盘点或物料判定不可用
     * CONCESSION让步放行
     * SCRAPPED报废
     * PENDING待定
     */
    @TableField("QUALITY_STATUS")
    private String qualityStatus;

    @ApiModelProperty(value="Y 精坯 N毛坯")
    @Length( max = 1 )
    @TableField("IS_BOUTIQUE")
    private String isBoutique;

    @ApiModelProperty(value="投入数量")
    @TableField("INPUT_QTY")
    private BigDecimal inputQty;

    @ApiModelProperty(value="产出数量")
    @TableField("OUT_QTY")
    private BigDecimal outQty;

    @ApiModelProperty(value="报废数量")
    @TableField("SCRAP_QTY")
    private BigDecimal scrapQty;

    @ApiModelProperty(value="最近过站时间")
    @TableField("LATELY_PASS_DATE")
    private Date latelyPassDate;

    @ApiModelProperty(value="最近过站工序")
    @Length( max = 64 )
    @TableField("LATELY_PASS_OPERATION")
    private String latelyPassOperation;

    @ApiModelProperty(value="最近过站工位")
    @Length( max = 64 )
    @TableField("LATELY_PASS_STATION")
    private String latelyPassStation;

    @ApiModelProperty(value="完成时间")
    @Length( max = 14 )
    @TableField("COMPLETE_DATE")
    private String completeDate;

    @ApiModelProperty(value="物料类型")
    @Length( max = 10 )
    @TableField("ITEM_TYPE")
    private String itemType;

    @ApiModelProperty(value="当前最大流水号")
    @TableField("MAX_SERIAL_NUMBER")
    private Integer maxSerialNumber;

    @ApiModelProperty(value="建档人")
    @Length( max = 32 )
    @TableField("CREATE_USER")
    private String createUser;

    @ApiModelProperty(value="建档日期")
    @TableField("CREATE_DATE")
    private Date createDate;

    @ApiModelProperty(value="修改人")
    @Length( max = 32 )
    @TableField("MODIFY_USER")
    private String modifyUser;

    @ApiModelProperty(value="修改时间")
    @TableField("MODIFY_DATE")
    private Date modifyDate;


    /**
     * 打印范围ID
     */
    @TableField("LABEL_PRINT_BO")
    private String labelPrintBo;

    /**
     * 标签模板的参数集合，用于打印标签
     */
    @TableField("LABEL_PARAMS")
    private String labelParams;


    /**
     * 打印次数
     */
    @TableField("PRINT_COUNT")
    private Integer printCount;

    /**
     * 最后打印账号
     */
    @TableField("LAST_PRINT_USER")
    private String lastPrintUser;

    /**
     * 最后打印时间
     */
    @TableField("LAST_PRINT_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date lastPrintDate;

    /**
     * 包装最大数量，用于包装
     */
    @TableField("PACKING_MAX_QUANTITY")
    private BigDecimal packingMaxQuantity;

    /**
     * 包装数量，用于包装
     */
    @TableField("PACKING_QUANTITY")
    private BigDecimal packingQuantity;

    @TableField("GET_USER")
    private String getUser;
    @TableField("GET_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date getTime;
    @TableField("WORK_SHOP_BO")
    private String workShopBo;
    @TableField("DEADLINE")
    private Date deadline;
    @ApiModelProperty(value="routerId")
    @TableField("ROUTER_ID")
    private String routerId;


    public static final String BO = "BO";

    public static final String SITE = "SITE";

    public static final String SN = "SN";

    public static final String OLD_SN = "OLD_SN";

    public static final String COMPLEMENT_CODE_STATE = "COMPLEMENT_CODE_STATE";

    public static final String ITEM_BO = "ITEM_BO";

    public static final String ORIGINAL_ITEM_BO = "ORIGINAL_ITEM_BO";

    public static final String QTY = "QTY";

    public static final String SHOP_ORDER = "SHOP_ORDER";

    public static final String TASK_NO = "TASK_NO";

    public static final String BOM_BO = "BOM_BO";

    public static final String SHAP_ORDER_BO = "SHAP_ORDER_BO";

    public static final String PAINT_ORDER_BO = "PAINT_ORDER_BO";

    public static final String FIRE_ORDER_BO = "FIRE_ORDER_BO";

    public static final String PACK_ORDER_BO = "PACK_ORDER_BO";

    public static final String PRODUCT_LINE_BO = "PRODUCT_LINE_BO";

    public static final String STATE = "STATE";

    public static final String IS_BOUTIQUE = "IS_BOUTIQUE";

    public static final String INPUT_QTY = "INPUT_QTY";

    public static final String OUT_QTY = "OUT_QTY";

    public static final String SCRAP_QTY = "SCRAP_QTY";

    public static final String LATELY_PASS_DATE = "LATELY_PASS_DATE";

    public static final String LATELY_PASS_OPERATION = "LATELY_PASS_OPERATION";

    public static final String LATELY_PASS_STATION = "LATELY_PASS_STATION";

    public static final String COMPLETE_DATE = "COMPLETE_DATE";

    public static final String ITEM_TYPE = "ITEM_TYPE";

    public static final String MAX_SERIAL_NUMBER = "MAX_SERIAL_NUMBER";

    public static final String CREATE_USER = "CREATE_USER";

    public static final String CREATE_DATE = "CREATE_DATE";

    public static final String MODIFY_USER = "MODIFY_USER";

    public static final String MODIFY_DATE = "MODIFY_DATE";

    @Override
    protected Serializable pkVal() {
        return this.bo;
    }


    public void setObjectSetBasicAttribute( String userId,Date date ){
        this.createUser=userId;
        this.createDate=date;
        this.modifyUser=userId;
        this.modifyDate=date;
   }

    @Override
    public String toString() {
        return "Sn{" +
            ", bo = " + bo +
            ", site = " + site +
            ", sn = " + sn +
            ", oldSn = " + oldSn +
            ", complementCodeState = " + complementCodeState +
            ", itemBo = " + itemBo +
            ", originalItemBo = " + originalItemBo +
            ", qty = " + qty +
            ", shopOrder = " + shopOrder +
            ", taskNo = " + taskNo +
            ", bomBo = " + bomBo +
            ", shapOrderBo = " + shapOrderBo +
            ", paintOrderBo = " + paintOrderBo +
            ", fireOrderBo = " + fireOrderBo +
            ", packOrderBo = " + packOrderBo +
            ", productLineBo = " + productLineBo +
            ", state = " + state +
            ", isBoutique = " + isBoutique +
            ", inputQty = " + inputQty +
            ", outQty = " + outQty +
            ", scrapQty = " + scrapQty +
            ", latelyPassDate = " + latelyPassDate +
            ", latelyPassOperation = " + latelyPassOperation +
            ", latelyPassStation = " + latelyPassStation +
            ", completeDate = " + completeDate +
            ", itemType = " + itemType +
            ", maxSerialNumber = " + maxSerialNumber +
            ", createUser = " + createUser +
            ", createDate = " + createDate +
            ", modifyUser = " + modifyUser +
            ", modifyDate = " + modifyDate +
        "}";
    }
}

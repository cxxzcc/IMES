package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 工序表
 * </p>
 *
 * @author space
 * @since 2019-06-06
 */
@TableName("m_operation")
@ApiModel(value="Operation",description="工序表")
@Data
public class Operation extends Model<Operation> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="OP:SITE,OPERATION,VERSION【PK】")
   @Length( max = 200 )
   @TableId(value = "BO", type = IdType.INPUT)
   private String bo;

   @ApiModelProperty(value="工厂【UK】")
   @Length( max = 32 )
   @TableField("SITE")
   private String site;

   @ApiModelProperty(value="工序编号【UK】")
   @Length( max = 64 )
   @TableField("OPERATION")
   private String operation;

   @ApiModelProperty(value="版本号【UK】")
   @Length( max = 3 )
   @TableField("VERSION")
   private String version;

   @ApiModelProperty(value="工序名称")
   @Length( max = 32 )
   @TableField("OPERATION_NAME")
   private String operationName;

   @ApiModelProperty(value="所属产线 BO【UK】")
   @Length( max = 100 )
   @TableField("PRODUCTION_LINE_BO")
   private String productionLineBo;

   @ApiModelProperty(value="描述")
   @Length( max = 200 )
   @TableField("OPERATION_DESC")
   private String operationDesc;

   @ApiModelProperty(value="状态")
   @Length( max = 32 )
   @TableField("STATE")
   private String state;

   @ApiModelProperty(value="是否当前版本 Y是 N否")
   @Length( max = 1 )
   @TableField("IS_CURRENT_VERSION")
   private String isCurrentVersion;

   @ApiModelProperty(value="工序类型 N：普通S：特殊T：测试")
   @Length( max = 1 )
   @TableField("OPERATION_TYPE")
   private String operationType;

   @ApiModelProperty(value="最大经过次数")
   @TableField("MAX_TIMES")
   private Integer maxTimes;

   @ApiModelProperty(value="重测次数")
   @TableField("REPEAT_TEST_TIMES")
   private Integer repeatTestTimes;

   @ApiModelProperty(value="缺省不良代码（BO）")
   @Length( max = 64 )
   @TableField(value = "DEFAULT_NC_CODE_BO", strategy = FieldStrategy.IGNORED)
   private String defaultNcCodeBo;

   @ApiModelProperty(value="不良代码组（BO）")
   @Length( max = 64 )
   @TableField(value = "NC_GROUP_BO", strategy = FieldStrategy.IGNORED)
   private String ncGroupBo;

   @ApiModelProperty(value="建档日期")
   @TableField("CREATE_DATE")
   private Date createDate;

   @ApiModelProperty(value="建档人")
   @Length( max = 32 )
   @TableField("CREATE_USER")
   private String createUser;

   @ApiModelProperty(value="修改日期")
   @TableField("MODIFY_DATE")
   private Date modifyDate;

   @ApiModelProperty(value="修改人")
   @Length( max = 32 )
   @TableField("MODIFY_USER")
   private String modifyUser;

   @ApiModelProperty(value="缺省工位")
   @Length( max = 200 )
   @TableField(value = "DEFAULT_STATION_BO", strategy = FieldStrategy.IGNORED)
   private String defaultStationBo;

   @ApiModelProperty(value="工位类型")
   @Length( max = 200 )
   @TableField("STATION_TYPE_BO")
   private String stationTypeBo;

   public String getActionId() {
      return actionId;
   }

   public void setActionId(String actionId) {
      this.actionId = actionId;
   }

   @ApiModelProperty(value = "过站动作Id")
   @TableField("ACTION_ID")
   private String actionId;


   @ApiModelProperty(value = "工艺参数")
   @TableField(value = "OPERATION_PARAM_STR", strategy = FieldStrategy.NOT_NULL)
   private String operationParamStr;


   /**
    * 是否校验资质标识， Y/N
    * */
   @TableField("IS_CHECK_PROVE_FLAG")
   private String isCheckProveFlag;



   public static final String BO = "BO";

   public static final String SITE = "SITE";

   public static final String OPERATION = "OPERATION";

   public static final String VERSION = "VERSION";

   public static final String OPERATION_NAME = "OPERATION_NAME";

   public static final String PRODUCTION_LINE_BO = "PRODUCTION_LINE_BO";

   public static final String OPERATION_DESC = "OPERATION_DESC";

   public static final String STATE = "STATE";

   public static final String IS_CURRENT_VERSION = "IS_CURRENT_VERSION";

   public static final String OPERATION_TYPE = "OPERATION_TYPE";

   public static final String MAX_TIMES = "MAX_TIMES";

   public static final String REPEAT_TEST_TIMES = "REPEAT_TEST_TIMES";

   public static final String DEFAULT_NC_CODE_BO = "DEFAULT_NC_CODE_BO";

   public static final String NC_GROUP_BO = "NC_GROUP_BO";

   public static final String CREATE_DATE = "CREATE_DATE";

   public static final String CREATE_USER = "CREATE_USER";

   public static final String MODIFY_DATE = "MODIFY_DATE";

   public static final String MODIFY_USER = "MODIFY_USER";

   public static final String DEFAULT_STATION_BO = "DEFAULT_STATION_BO";

   public static final String STATION_TYPE_BO = "STATION_TYPE_BO";

   public static final String ACTION_ID = "ACTION_ID";

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
}

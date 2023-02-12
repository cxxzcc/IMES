package com.itl.mes.core.api.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author space
 * @since 2019-08-30
 */
@TableName("m_quality_plan_parameter")
@ApiModel(value = "QualityPlanParameter", description = "")
public class QualityPlanParameter extends Model<QualityPlanParameter> {

    private static final long serialVersionUID = 1L;


    @Excel(name = "表BO", orderNum = "17")
    @ApiModelProperty(value = "QPP:SITE,QUALITY_PLAN,VERSION[PK],PARAMETER_NAME")
    @Length(max = 100)
    @TableId(value = "BO", type = IdType.INPUT)
    private String bo;

    @Excel(name = "工厂", orderNum = "16")
    @ApiModelProperty(value = "工厂")
    @Length(max = 32)
    @TableField("SITE")
    private String site;

    @Excel(name = "质量控制计划BO", orderNum = "0")
    @ApiModelProperty(value = "质量控制计划BO")
    @Length(max = 100)
    @TableField("QUALITY_PLAN_BO")
    private String qualityPlanBo;

    @Excel(name = "序号", orderNum = "1", type = 10)
    @ApiModelProperty(value = "序号(10,20,30......)")
    @TableField("SEQ")
    private Integer seq;

    @Excel(name = "检查项目编号", orderNum = "2")
    @ApiModelProperty(value = "检查项目编号")
    @Length(max = 64)
    @TableField("PARAMETER_NAME")
    private String parameterName;

    @Excel(name = "检查项目描述", orderNum = "3")
    @ApiModelProperty(value = "检查项目描述")
    @Length(max = 200)
    @TableField("PARAMETER_DESC")
    private String parameterDesc;

    @Excel(name = "目标值", orderNum = "4")
    @ApiModelProperty(value = "目标值")
    @Length(max = 32)
    @TableField("AIM_VAL")
    private String aimVal;

    @Excel(name = "上限", orderNum = "5")
    @ApiModelProperty(value = "上限")
    @TableField("UPPER_LIMIT")
    private BigDecimal upperLimit;

    @Excel(name = "下限", orderNum = "6")
    @ApiModelProperty(value = "下限")
    @TableField("LOWER_LIMIT")
    private BigDecimal lowerLimit;

    @Excel(name = "检验方法", orderNum = "7")
    @ApiModelProperty(value = "检验方法")
    @Length(max = 64)
    @TableField("INSPECT_METHOD")
    private String inspectMethod;

    @Excel(name = "检验类型", orderNum = "8")
    @ApiModelProperty(value = "检验类型")
    @Length(max = 64)
    @TableField("INSPECT_TYPE")
    private String inspectType;

    @Excel(name = "参数类型", orderNum = "9")
    @ApiModelProperty(value = "参数类型（0：数值，1：布尔，2：文本）")
    @Length(max = 10)
    @TableField("PARAMETER_TYPE")
    private String parameterType;

    @Excel(name = "检验数量", orderNum = "10", type = 10)
    @ApiModelProperty(value = "检验数量")
    @TableField("INSPECT_QTY")
    private Integer inspectQty;

    @Excel(name = "是否启用", orderNum = "11")
    @ApiModelProperty(value = "是否启用（0：未启用，1：启用）")
    @Length(max = 1)
    @TableField("ENABLED")
    private String enabled;

    @Excel(name = "创建人", orderNum = "12")
    @ApiModelProperty(value = "创建人")
    @Length(max = 32)
    @TableField("CREATE_USER")
    private String createUser;

    @Excel(name = "创建时间", orderNum = "13", exportFormat = "yyyy-MM-dd", importFormat = "yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    @TableField("CREATE_DATE")
    private Date createDate;

    @Excel(name = "修改人", orderNum = "14")
    @ApiModelProperty(value = "修改人")
    @Length(max = 32)
    @TableField("MODIFY_USER")
    private String modifyUser;

    @Excel(name = "修改时间", orderNum = "15", exportFormat = "yyyy-MM-dd hh:mm:ss")
    @ApiModelProperty(value = "修改时间")
    @TableField("MODIFY_DATE")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss.SSS",
            timezone = "GMT+8"
    )
    private Date modifyDate;


    public String getBo() {
        return bo;
    }

    public void setBo(String bo) {
        this.bo = bo;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getQualityPlanBo() {
        return qualityPlanBo;
    }

    public void setQualityPlanBo(String qualityPlanBo) {
        this.qualityPlanBo = qualityPlanBo;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterDesc() {
        return parameterDesc;
    }

    public void setParameterDesc(String parameterDesc) {
        this.parameterDesc = parameterDesc;
    }

    public String getAimVal() {
        return aimVal;
    }

    public void setAimVal(String aimVal) {
        this.aimVal = aimVal;
    }

    public BigDecimal getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(BigDecimal upperLimit) {
        this.upperLimit = upperLimit;
    }

    public BigDecimal getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(BigDecimal lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public String getInspectMethod() {
        return inspectMethod;
    }

    public void setInspectMethod(String inspectMethod) {
        this.inspectMethod = inspectMethod;
    }

    public String getInspectType() {
        return inspectType;
    }

    public void setInspectType(String inspectType) {
        this.inspectType = inspectType;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public Integer getInspectQty() {
        return inspectQty;
    }

    public void setInspectQty(Integer inspectQty) {
        this.inspectQty = inspectQty;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public static final String BO = "BO";

    public static final String SITE = "SITE";

    public static final String QUALITY_PLAN_BO = "QUALITY_PLAN_BO";

    public static final String SEQ = "SEQ";

    public static final String PARAMETER_NAME = "PARAMETER_NAME";

    public static final String PARAMETER_DESC = "PARAMETER_DESC";

    public static final String AIM_VAL = "AIM_VAL";

    public static final String UPPER_LIMIT = "UPPER_LIMIT";

    public static final String LOWER_LIMIT = "LOWER_LIMIT";

    public static final String INSPECT_METHOD = "INSPECT_METHOD";

    public static final String INSPECT_TYPE = "INSPECT_TYPE";

    public static final String PARAMETER_TYPE = "PARAMETER_TYPE";

    public static final String INSPECT_QTY = "INSPECT_QTY";

    public static final String ENABLED = "ENABLED";

    public static final String CREATE_USER = "CREATE_USER";

    public static final String CREATE_DATE = "CREATE_DATE";

    public static final String MODIFY_USER = "MODIFY_USER";

    public static final String MODIFY_DATE = "MODIFY_DATE";

    @Override
    protected Serializable pkVal() {
        return this.bo;
    }


    public void setObjectSetBasicAttribute(String userId, Date date) {
        this.createUser = userId;
        this.createDate = date;
        this.modifyUser = userId;
        this.modifyDate = date;
    }

    @Override
    public String toString() {
        return "QualityPlanParameter{" +
                ", bo = " + bo +
                ", site = " + site +
                ", qualityPlanBo = " + qualityPlanBo +
                ", seq = " + seq +
                ", parameterName = " + parameterName +
                ", parameterDesc = " + parameterDesc +
                ", aimVal = " + aimVal +
                ", upperLimit = " + upperLimit +
                ", lowerLimit = " + lowerLimit +
                ", inspectMethod = " + inspectMethod +
                ", inspectType = " + inspectType +
                ", parameterType = " + parameterType +
                ", inspectQty = " + inspectQty +
                ", enabled = " + enabled +
                ", createUser = " + createUser +
                ", createDate = " + createDate +
                ", modifyUser = " + modifyUser +
                ", modifyDate = " + modifyDate +
                "}";
    }
}
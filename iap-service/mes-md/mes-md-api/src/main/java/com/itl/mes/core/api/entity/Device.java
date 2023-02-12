package com.itl.mes.core.api.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.itl.mes.core.api.vo.DeviceTypeSimplifyVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.experimental.FieldNameConstants;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 设备表
 * </p>
 *
 * @author space
 * @since 2019-06-17
 */
@TableName("m_device")
@ApiModel(value="Device",description="设备表")
@FieldNameConstants
public class Device extends Model<Device> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="RES:SITE,DEVICE【PK】")
   @Length( max = 100 )
   @TableId(value = "BO", type = IdType.INPUT)
   private String bo;

   @ApiModelProperty(value = "仪器位置")
   @Length(max = 255)
   @TableField("BASE_LOCATION")
   private String baseLocation;

   @ApiModelProperty(value = "基地id")
   @Length(max = 64)
   @TableField("BASE_ID")
   private String baseId;

   @ApiModelProperty(value = "仪器类型id")
   @Length(max = 64)
   @TableField("INSTRUMENT_TYPE_ID")
   private String instrumentTypeId;

   @ApiModelProperty(value = "是否基准仪器 1是 0否")
   @Length(max = 64)
   @TableField("IS_STANDARD")
   private String isStandard;

   @ApiModelProperty(value="工厂【UK】")
   @Length( max = 32 )
   @TableField("SITE")
   private String site;

   @ApiModelProperty(value="设备编号【UK】")
   @Excel(name = "设备编号", orderNum ="1")
   @Length( max = 64 )
   @TableField("DEVICE")
   private String device;

   @ApiModelProperty(value="设备名称")
   @Excel(name = "设备名称", orderNum ="2")
   @Length( max = 100 )
   @TableField("DEVICE_NAME")
   private String deviceName;

   @ApiModelProperty(value="设备描述")
   @Excel(name = "设备描述", orderNum ="5")
   @Length( max = 200 )
   @TableField("DEVICE_DESC")
   private String deviceDesc;

   @ApiModelProperty(value="设备型号")
   @Excel(name = "设备型号", orderNum ="3")
   @Length( max = 30 )
   @TableField("DEVICE_MODEL")
   private String deviceModel;

   @ApiModelProperty(value="状态")
   @Length( max = 100 )
   @TableField("STATE")
   private String state;

   public String getStatedesc() {
      return statedesc;
   }

   public void setStatedesc(String statedesc) {
      this.statedesc = statedesc;
   }

   @ApiModelProperty(value="状态描述")
   @Excel(name = "状态", orderNum ="16")
   @Length( max = 100 )
   @TableField(exist = false)
   private String statedesc;

   @ApiModelProperty(value="是否为加工设备")
   @Excel(name = "是否为加工设备",replace = {"是_Y","否_N"},orderNum ="14")
   @Length( max = 1 )
   @TableField("IS_PROCESS_DEVICE")
   private String isProcessDevice;

   @ApiModelProperty(value="产线【M_PRODUCT_LINE的BO】")
   @Length( max = 100 )
   @TableField("PRODUCT_LINE_BO")
   private String productLineBo;

   @ApiModelProperty(value="产线")
   @Excel(name = "产线", orderNum ="6")
   @Length( max = 100 )
   @TableField(exist = false)
   private String productLine;

   @ApiModelProperty(value="工位【M_STATION的BO】")
   @Length( max = 100 )
   @TableField("STATION_BO")
   private String stationBo;

   @ApiModelProperty(value="工位")
   @Excel(name = "工位", orderNum ="7")
   @Length( max = 100 )
   @TableField(exist = false)
   private String station;

   @ApiModelProperty(value="设备类型名称")
   @Excel(name = "设备类型编码", orderNum ="4")
   @TableField(exist = false)
   private String deviceTypeName;

   @ApiModelProperty(value="安装地点")
   @Excel(name = "安装地点", orderNum = "17")
   @Length( max = 100 )
   @TableField("LOCATION")
   private String location;

   @ApiModelProperty(value="资产编号")
   @Length( max = 64 )
   @TableField("ASSETS_CODE")
   private String assetsCode;

   @ApiModelProperty(value="生产厂家")
   @Excel(name = "生产厂家", orderNum ="9")
   @Length( max = 100 )
   @TableField("MANUFACTURER")
   private String manufacturer;

   @ApiModelProperty(value="投产日期")
   @Excel(name = "投产日期", orderNum ="10")
   @TableField("VALID_START_DATE")
   private Date validStartDate;

   @ApiModelProperty(value="结束日期")
   @TableField("VALID_END_DATE")
   private Date validEndDate;

   @ApiModelProperty(value="备注")
   @Excel(name = "备注", orderNum = "18")
   @Length( max = 200 )
   @TableField("MEMO")
   private String memo;

   @ApiModelProperty(value="建档日期")
   @TableField("CREATE_DATE")
   private Date createDate;

   @ApiModelProperty(value="建档人")
   @Length( max = 32 )
   @TableField("CREATE_USER")
   private String createUser;

   @ApiModelProperty(value="修改人")
   @Length( max = 32 )
   @TableField("MODIFY_USER")
   private String modifyUser;

   @ApiModelProperty(value="修改日期")
   @TableField("MODIFY_DATE")
   private Date modifyDate;
   @ApiModelProperty(value="车间")
   @TableField(exist = false)
   private String workShop;

   @ApiModelProperty(value="螺杆组合")
   @TableField("SCREW_COMBINATION")
   private String screwCombination;

   @ApiModelProperty(value = "负责人")
   @Excel(name = "负责人", orderNum ="12")
   @TableField("INCHARGE")
   private String incharge;

   @ApiModelProperty(value = "联系方式")
   @Excel(name = "联系方式", orderNum ="13")
   @TableField("TEL")
   private String tel;

   public Integer getUsefulLife() {
      return usefulLife;
   }

   public void setUsefulLife(Integer usefulLife) {
      this.usefulLife = usefulLife;
   }

   @ApiModelProperty( value = "使用寿命" )
   @Excel(name = "使用寿命", orderNum ="15")
   @TableField("USEFUL_LIFE")
   private Integer usefulLife;

   @ApiModelProperty( value = "封面图" )
   @TableField(value = "COVER_IMG", strategy = FieldStrategy.NOT_NULL)
   private String coverImg;

   @ApiModelProperty( value = "购买日期" )
   @Excel(name = "投产日期", orderNum ="11")
   @TableField("BUY_DATE")
   private Date buyDate;

   @ApiModelProperty( value = "供应商id" )
   @TableField("SUPPLIER")
   private String supplier;

   public static long getSerialVersionUID() {
      return serialVersionUID;
   }

   public String getProductLine() {
      return productLine;
   }

   public void setProductLine(String productLine) {
      this.productLine = productLine;
   }

   public String getStation() {
      return station;
   }

   public void setStation(String station) {
      this.station = station;
   }

   public String getDeviceTypeName() {
      return deviceTypeName;
   }

   public void setDeviceTypeName(String deviceTypeName) {
      this.deviceTypeName = deviceTypeName;
   }

   public String getSupplier() {
      return supplier;
   }

   public void setSupplier(String supplier) {
      this.supplier = supplier;
   }

   public String getSupplierName() {
      return supplierName;
   }

   public void setSupplierName(String supplierName) {
      this.supplierName = supplierName;
   }

   public static String getBO() {
      return BO;
   }

   public static String getSITE() {
      return SITE;
   }

   public static String getDEVICE() {
      return DEVICE;
   }

   public static String getSTATE() {
      return STATE;
   }

   public static String getLOCATION() {
      return LOCATION;
   }

   public static String getMANUFACTURER() {
      return MANUFACTURER;
   }

   public static String getMEMO() {
      return MEMO;
   }

   @ApiModelProperty( value = "供应商" )
   @Excel(name = "供应商", orderNum = "8")
   @TableField(exist = false)
   private String supplierName;

   public String getBaseLocation() {
      return baseLocation;
   }

   public void setBaseLocation(String baseLocation) {
      this.baseLocation = baseLocation;
   }

   public String getBaseId() {
      return baseId;
   }

   public String getInstrumentTypeId() {
      return instrumentTypeId;
   }

   public void setInstrumentTypeId(String instrumentTypeId) {
      this.instrumentTypeId = instrumentTypeId;
   }

   public void setBaseId(String baseId) {
      this.baseId = baseId;
   }

   public String getIsStandard() {
      return isStandard;
   }

   public void setIsStandard(String isStandard) {
      this.isStandard = isStandard;
   }

   public Date getBuyDate() {
      return buyDate;
   }

   public void setBuyDate(Date buyDate) {
      this.buyDate = buyDate;
   }

   public String getIncharge() {
      return incharge;
   }

   public void setIncharge(String incharge) {
      this.incharge = incharge;
   }

   public String getTel() {
      return tel;
   }

   public void setTel(String tel) {
      this.tel = tel;
   }


   public String getScrewCombination() {
      return screwCombination;
   }

   public void setScrewCombination(String screwCombination) {
      this.screwCombination = screwCombination;
   }

   public String getWorkShop() {
      return workShop;
   }

   public void setWorkShop(String workShop) {
      this.workShop = workShop;
   }

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

   public String getDevice() {
      return device;
   }

   public void setDevice(String device) {
      this.device = device;
   }

   public String getDeviceName() {
      return deviceName;
   }

   public void setDeviceName(String deviceName) {
      this.deviceName = deviceName;
   }

   public String getDeviceDesc() {
      return deviceDesc;
   }

   public void setDeviceDesc(String deviceDesc) {
      this.deviceDesc = deviceDesc;
   }

   public String getDeviceModel() {
      return deviceModel;
   }

   public void setDeviceModel(String deviceModel) {
      this.deviceModel = deviceModel;
   }

   public String getState() {
      return state;
   }

   public void setState(String state) {
      this.state = state;
   }

   public String getIsProcessDevice() {
      return isProcessDevice;
   }

   public void setIsProcessDevice(String isProcessDevice) {
      this.isProcessDevice = isProcessDevice;
   }

   public String getProductLineBo() {
      return productLineBo;
   }

   public void setProductLineBo(String productLineBo) {
      this.productLineBo = productLineBo;
   }

   public String getStationBo() {
      return stationBo;
   }

   public void setStationBo(String stationBo) {
      this.stationBo = stationBo;
   }

   public String getLocation() {
      return location;
   }

   public void setLocation(String location) {
      this.location = location;
   }

   public String getAssetsCode() {
      return assetsCode;
   }

   public void setAssetsCode(String assetsCode) {
      this.assetsCode = assetsCode;
   }

   public String getManufacturer() {
      return manufacturer;
   }

   public void setManufacturer(String manufacturer) {
      this.manufacturer = manufacturer;
   }

   public Date getValidStartDate() {
      return validStartDate;
   }

   public void setValidStartDate(Date validStartDate) {
      this.validStartDate = validStartDate;
   }

   public Date getValidEndDate() {
      return validEndDate;
   }

   public void setValidEndDate(Date validEndDate) {
      this.validEndDate = validEndDate;
   }

   public String getMemo() {
      return memo;
   }

   public void setMemo(String memo) {
      this.memo = memo;
   }

   public Date getCreateDate() {
      return createDate;
   }

   public void setCreateDate(Date createDate) {
      this.createDate = createDate;
   }

   public String getCreateUser() {
      return createUser;
   }

   public void setCreateUser(String createUser) {
      this.createUser = createUser;
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


   public String getCoverImg() {
      return coverImg;
   }

   public void setCoverImg(String coverImg) {
      this.coverImg = coverImg;
   }

   public static final String BO = "BO";

   public static final String SITE = "SITE";

   public static final String DEVICE = "DEVICE";

   public static final String DEVICE_NAME = "DEVICE_NAME";

   public static final String DEVICE_DESC = "DEVICE_DESC";

   public static final String DEVICE_MODEL = "DEVICE_MODEL";

   public static final String STATE = "STATE";

   public static final String IS_PROCESS_DEVICE = "IS_PROCESS_DEVICE";

   public static final String PRODUCT_LINE_BO = "PRODUCT_LINE_BO";

   public static final String STATION_BO = "STATION_BO";

   public static final String LOCATION = "LOCATION";

   public static final String ASSETS_CODE = "ASSETS_CODE";

   public static final String MANUFACTURER = "MANUFACTURER";

   public static final String VALID_START_DATE = "VALID_START_DATE";

   public static final String VALID_END_DATE = "VALID_END_DATE";

   public static final String MEMO = "MEMO";

   public static final String CREATE_DATE = "CREATE_DATE";

   public static final String CREATE_USER = "CREATE_USER";

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
      return "Device{" +
         ", bo = " + bo +
         ", site = " + site +
         ", device = " + device +
         ", deviceName = " + deviceName +
         ", deviceDesc = " + deviceDesc +
         ", deviceModel = " + deviceModel +
         ", state = " + state +
         ", isProcessDevice = " + isProcessDevice +
         ", productLineBo = " + productLineBo +
         ", stationBo = " + stationBo +
         ", location = " + location +
         ", assetsCode = " + assetsCode +
         ", manufacturer = " + manufacturer +
         ", validStartDate = " + validStartDate +
         ", validEndDate = " + validEndDate +
         ", memo = " + memo +
         ", createDate = " + createDate +
         ", createUser = " + createUser +
         ", modifyUser = " + modifyUser +
         ", modifyDate = " + modifyDate +
         "}";
   }
}

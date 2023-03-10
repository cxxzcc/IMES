package com.itl.mes.core.api.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 班组信息主表
 * </p>
 *
 * @author space
 * @since 2019-06-25
 */
@TableName("m_team")
@ApiModel(value="Team",description="班组信息主表")
public class Team extends Model<Team> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="GROUP:SITE,GROUP【PK-】")
   @Length( max = 100 )
   @TableId(value = "BO", type = IdType.INPUT)
   @Excel(name = "bo" ,orderNum = "1")
   private String bo;

   @ApiModelProperty(value="工厂【UK-】")
   @Length( max = 32 )
   @TableField("SITE")
   @Excel(name = "工厂" ,orderNum = "2")
   private String site;

   @ApiModelProperty(value="班组【UK-】")
   @Length( max = 32 )
   @TableField("TEAM")
   @Excel(name = "班组编码" ,orderNum = "3")
   private String team;

   @ApiModelProperty(value="班次描述")
   @Length( max = 412 )
   @TableField("TEAM_DESC")
   @Excel(name = "班次描述" ,orderNum = "4")
   private String teamDesc;

   @ApiModelProperty(value="班组长")
   @Length( max = 32 )
   @TableField("LEADER")
   @Excel(name = "班组长" ,orderNum = "5")
   private String leader;

   @ApiModelProperty(value="产线")
   @Length( max = 100 )
   @TableField("PRODUCT_LINE_BO")
   private String productLineBo;

   public String getProductLine() {
      return productLine;
   }

   public void setProductLine(String productLine) {
      this.productLine = productLine;
   }

   @ApiModelProperty(value="产线")
   @Excel(name = "产线", orderNum ="6")
   @Length( max = 100 )
   @TableField(exist = false)
   private String productLine;

   @ApiModelProperty(value="创建人")
   @Length( max = 32 )
   @TableField("CREATE_USER")
   @Excel(name = "创建人" ,orderNum = "7")
   private String createUser;

   @ApiModelProperty(value="创建时间")
   @TableField("CREATE_DATE")
   @Excel(name = "创建时间" ,orderNum = "8")
   private Date createDate;

   @ApiModelProperty(value="修改时间")
   @TableField("MODIFY_DATE")
   @Excel(name = "修改时间" ,orderNum = "9")
   private Date modifyDate;

   @ApiModelProperty(value="修改人")
   @Length( max = 32 )
   @TableField("MODIFY_USER")
   @Excel(name = "修改人" ,orderNum = "10")
   private String modifyUser;


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

   public String getTeam() {
      return team;
   }

   public void setTeam(String team) {
      this.team = team;
   }

   public String getTeamDesc() {
      return teamDesc;
   }

   public void setTeamDesc(String teamDesc) {
      this.teamDesc = teamDesc;
   }

   public String getLeader() {
      return leader;
   }

   public void setLeader(String leader) {
      this.leader = leader;
   }

   public String getProductLineBo() {
      return productLineBo;
   }

   public void setProductLineBo(String productLineBo) {
      this.productLineBo = productLineBo;
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

   public Date getModifyDate() {
      return modifyDate;
   }

   public void setModifyDate(Date modifyDate) {
      this.modifyDate = modifyDate;
   }

   public String getModifyUser() {
      return modifyUser;
   }

   public void setModifyUser(String modifyUser) {
      this.modifyUser = modifyUser;
   }

   public static final String BO = "BO";

   public static final String SITE = "SITE";

   public static final String TEAM = "TEAM";

   public static final String TEAM_DESC = "TEAM_DESC";

   public static final String LEADER = "LEADER";

   public static final String PRODUCT_LINE_BO = "PRODUCT_LINE_BO";

   public static final String CREATE_USER = "CREATE_USER";

   public static final String CREATE_DATE = "CREATE_DATE";

   public static final String MODIFY_DATE = "MODIFY_DATE";

   public static final String MODIFY_USER = "MODIFY_USER";

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
      return "Team{" +
         ", bo = " + bo +
         ", site = " + site +
         ", team = " + team +
         ", teamDesc = " + teamDesc +
         ", leader = " + leader +
         ", productLineBo = " + productLineBo +
         ", createUser = " + createUser +
         ", createDate = " + createDate +
         ", modifyDate = " + modifyDate +
         ", modifyUser = " + modifyUser +
         "}";
   }
}
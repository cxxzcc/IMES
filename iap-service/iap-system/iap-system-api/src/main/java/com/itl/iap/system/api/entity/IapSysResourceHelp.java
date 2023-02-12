package com.itl.iap.system.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 操作变动明细日志(IapChangeDetailLogT)实体类
 *
 * @author linjs
 * @since 2020-10-30 11:06:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("iap_sys_resource_help")
@ApiModel("帮助文档列表")
public class IapSysResourceHelp implements Serializable {
    private static final long serialVersionUID = -88855609200243723L;

    @TableId(type = IdType.UUID)
    private String id;
    /**
    * 数据表
    */
    @TableField("TABLE_NAME")
    private String tableName;
    /**
    * 备注
    */
    @TableField("REMARK")
    private String remark;
    /**
    * 文档内容
    */
    @TableField("HELP_DOC")
    private String helpDoc;
    /**
    * 菜单资源Id
    */
    @TableField("RESOURCE_ID")
    private String resourceId;
    /**
     * 页面路径
     */
    @TableField("ROUTER_PATH")
    private String routerPath;

    /**
     * 菜单资源名称
     */
    @TableField(exist = false)
    private String resourcesName;

    /**
     * 父id
     */
    @TableField(exist = false)
    private String parentId;











}
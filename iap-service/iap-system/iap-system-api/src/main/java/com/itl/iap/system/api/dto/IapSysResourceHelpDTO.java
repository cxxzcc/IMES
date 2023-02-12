package com.itl.iap.system.api.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 帮助文档保存参数
 */
@Data
@ApiModel("帮助文档保存参数")
public class IapSysResourceHelpDTO {

    private String id;

    @NotBlank
    @ApiModelProperty("数据表名称")
    private String tableName;

    @ApiModelProperty("备注")
    private String remark;

    @NotBlank
    @ApiModelProperty("文档内容")
    private String helpDoc;

    @NotBlank
    @ApiModelProperty("菜单资源Id")
    private String resourceId;

    @NotBlank
    @ApiModelProperty("页面路径")
    private String routerPath;




}
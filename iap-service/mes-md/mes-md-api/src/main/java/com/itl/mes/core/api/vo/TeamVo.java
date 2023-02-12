package com.itl.mes.core.api.vo;


import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "TeamVo",description = "保存班组用")
public class TeamVo implements Serializable {

    @ApiModelProperty(value="GROUP:SITE,GROUP【PK-】")
    private String bo;

    @ApiModelProperty(value="工厂【UK-】")
    private String site;

    @ApiModelProperty(value="班组【UK-】")
    @Excel(name = "班组编码")
    private String team;

    @ApiModelProperty(value="班次描述")
    @Excel(name = "班组描述")
    private String teamDesc;

    @ApiModelProperty(value="班组长")
    @Excel(name = "班组长")
    private String leader;

    @ApiModelProperty(value="产线")
    @Excel(name = "产线")
    private String productLine;

    @ApiModelProperty(value="修改时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss.SSS",
            timezone = "GMT+8"
    )
    private Date modifyDate;

    @ApiModelProperty(value = "未分配班组成员")
    private List<EmployeeVo> availbleEmployeeVoList;

    @ApiModelProperty(value = "已分配班组成员")
    private List<EmployeeVo> assignedEmployeeVoList;

    @ApiModelProperty( value = "自定义数据属性和值" )
    private List<CustomDataAndValVo> customDataAndValVoList;

    @ApiModelProperty( value = "自定义数据属性和值，保存时传入" )
    private List<CustomDataValVo> customDataValVoList;
}

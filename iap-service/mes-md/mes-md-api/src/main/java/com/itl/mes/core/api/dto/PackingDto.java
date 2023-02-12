package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import com.itl.mes.core.api.vo.CustomDataValVo;
import com.itl.mes.core.api.vo.PackLevelVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author FengRR
 * @date 2021/5/27
 * @since JDK1.8
 */
@Data
@ApiModel(value = "PackingDto",description = "包装Lov工具")
public class PackingDto {

    @ApiModelProperty(value = "分页信息")
    private Page page;

    @ApiModelProperty(value="PK:SITE,PACK_NAME【PK】")
    private String bo;

    @ApiModelProperty(value="工厂")
    private String site;

    @ApiModelProperty(value="包装名称")
    private String packName;

    @ApiModelProperty(value="包装等级 1最大 6最小 共6级")
    private String packGrade;


    @ApiModelProperty(value="描述")
    private String packDesc;

    @ApiModelProperty(value="最大包装数")
    private BigDecimal maxQty;

    @ApiModelProperty(value="最小包装数")
    private BigDecimal minQty;

    @ApiModelProperty(value="高度")
    private BigDecimal heights;

    @ApiModelProperty(value="长度")
    private BigDecimal lengths;

    @ApiModelProperty(value="宽度")
    private BigDecimal widths;

    @ApiModelProperty(value="包装重量")
    private BigDecimal weights;

    @ApiModelProperty(value="填充重量")
    private BigDecimal fillWeight;

    @ApiModelProperty(value="状态")
    private String state;

    @ApiModelProperty(value="修改时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss.SSS",
            timezone = "GMT+8"
    )
    private Date modifyDate;

    @ApiModelProperty(value="包装对象集合")
    private List<PackLevelVo> packLevelVos;

    @ApiModelProperty( value = "自定义数据属性和值" )
    private List<CustomDataAndValVo> customDataAndValVoList;

    @ApiModelProperty( value = "自定义数据属性和值，保存时传入" )
    private List<CustomDataValVo> customDataValVoList;
}


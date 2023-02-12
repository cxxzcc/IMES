package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@FieldNameConstants
@ApiModel(value = "项目保存对象")
public class TProjectDTO {

    private String id;

    @NotBlank
    @Length(max = 64)
    @ApiModelProperty("项目编码")
    private String code;

    @NotBlank
    @Length(max = 255)
    @ApiModelProperty("项目名称")
    private String name;

    @ApiModelProperty("仪器类型id")
    private String instrumentTypeId;

    @ApiModelProperty("单位")
    private String unit;

    @ApiModelProperty("小数位")
    private Integer digital;

    @ApiModelProperty("标准偏差")
    private String standardDeviation;


}

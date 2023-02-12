package com.itl.mom.label.api.dto.label;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 标签打印响应dto
 * @author dengou
 * @date 2021/12/6
 */
@Data
public class LabelPrintResponseDTO {

    /**
     * 模板类型
     * */
    @ApiModelProperty(value = "模板类型, LODOP/JASPER")
    private String templateType;

    /**
     * lodoptext
     * */
    @ApiModelProperty(value = "lodoptext")
    private String lodopText;


    @ApiModelProperty(value = "打印参数")
    private List<String> printParam;
}

package com.itl.mom.label.api.vo;

import com.itl.mes.core.api.vo.CustomDataAndValVo;
import com.itl.mom.label.api.entity.file.MesFiles;
import com.itl.mom.label.api.entity.label.LabelEntityParams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @auth liuchenghao
 * @date 2021/3/15
 */
@Data
@ApiModel(value = "LabelVo",description = "标签返回实体")
public class LabelVo {

    private String id;

    @ApiModelProperty(value = "标签类型ID")
    private String labelTypeId;

    @ApiModelProperty(value = "工厂")
    private String site;

    @ApiModelProperty(value = "是否使用数据")
    private Integer useDataSource;

    @ApiModelProperty(value = "标签")
    private String label;

    @ApiModelProperty(value = "标签描述")
    private String labelDescription;

    @ApiModelProperty(value = "状态，0未启动，1启动")
    private String state;

    @ApiModelProperty(value = "模板类型，有Lodop和Jasper")
    private String templateType;

    @ApiModelProperty(value = "lodop的模板代码，用于前端回显预览")
    private String lodopText;

    @ApiModelProperty(value = "jasper的文件信息")
    private MesFiles jasperFile;

    @ApiModelProperty(value = "标签的参数集合")
    private List<LabelEntityParams> labelEntityParamsList;

    @ApiModelProperty(value = "标签的自定义字段信息和值")
    private List<CustomDataAndValVo> customDataAndValVoList;

}

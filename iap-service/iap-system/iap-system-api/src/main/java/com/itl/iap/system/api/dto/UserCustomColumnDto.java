package com.itl.iap.system.api.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "用户自定义列设置")
public class UserCustomColumnDto implements Serializable {

    /**
     * 页面标识
     */
    @ApiModelProperty(value = "页面标识", required = true)
    private String pageId;

/*
    @ApiModelProperty(value = "拼接sql字段,前端暂时不用传")
    private List<String> spliceColumnList;
*/

    @ApiModelProperty(value = "要显示的字段集合")
    private List<String> showColumnList;

    @ApiModelProperty(value = "是否排序")
    private Boolean isSort;
}

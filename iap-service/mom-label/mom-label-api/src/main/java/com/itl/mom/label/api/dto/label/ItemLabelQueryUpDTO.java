package com.itl.mom.label.api.dto.label;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "物料标签挂起")
public class ItemLabelQueryUpDTO {

    @NonNull
    @Size(min = 1, max = 10000)
    @ApiModelProperty(value = "id列表")
    private List<String> idList;

    @NotBlank
    @ApiModelProperty(value = "是否挂起 Y是 N否")
    private String sfgq;
}

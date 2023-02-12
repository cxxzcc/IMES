package com.itl.mes.me.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author yx
 * @date 2021/5/31
 * @since 1.8
 */
@Data
@ApiModel("me操作查询Dto")
public class OperationQueryDto {
    private Page page;
    private String operation;
    private String desc;
}

package com.itl.mes.me.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author yaoxiang
 * @date 2020/12/25
 * @since JDK1.8
 */
@Data
@ApiModel("ActionDto")
public class ActionDto {


    private String desc;
    private String action;
    private Page page;
}

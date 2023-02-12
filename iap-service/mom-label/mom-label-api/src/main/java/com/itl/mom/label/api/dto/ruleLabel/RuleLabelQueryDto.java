package com.itl.mom.label.api.dto.ruleLabel;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * @author yaoxiang
 * @date 2021/1/21
 * @since JDK1.8
 */
@Data
public class RuleLabelQueryDto {
    private Page page;
    private String site;
    private String bo;
    private String ruleLabel;
    private String ruleLabelName;
    private String elementType;
}

package com.itl.mes.core.provider.controller;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.mes.core.api.dto.ItemForParamQueryDto;
import com.itl.mes.core.provider.handle.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author yx
 * @date 2021/3/19
 * @since JDK1.8
 */
@RestController
@RequestMapping("/ruleLabel")
public class LabelRuleQueryController {

    @Autowired
    private ApplicationContext context;

    @PostMapping("/getParams")
    @ApiOperation("根据字段和物料id获取各个字段的值")
    public Map<String, Object> getParams(@RequestBody ItemForParamQueryDto queryDto) throws CommonException {
        LabelRuleHandle handle;

        switch (queryDto.getElementType()) {
            case "ITEM": {
                handle = context.getBean(ItemLabelRuleHandle.class);
                break;
            }
            case "SO": {
                handle = context.getBean(ShopOrderLabelRuleHandle.class);
                break;
            }
            case "RES": {
                handle = context.getBean(DeviceLabelRuleHandle.class);
                break;
            }
            case "CARRIER": {
                handle = context.getBean(CarrierLabelRuleHandle.class);
                break;
            }
            case "PK": {
                handle = context.getBean(PackLabelRuleHandle.class);
                break;
            }
            default:
                throw new CommonException("规则模板异常!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }

        return "Y".equals(queryDto.getIsCustom()) ? handle.handleCustom(queryDto) : handle.handle(queryDto);
    }
}

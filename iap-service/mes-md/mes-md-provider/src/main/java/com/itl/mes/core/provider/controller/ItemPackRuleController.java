package com.itl.mes.core.provider.controller;


import com.itl.iap.common.base.exception.CommonException;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.ItemPackRuleDetailDto;

import com.itl.mes.core.api.service.ItemPackRuleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FengRR
 * @date 2021/5/31
 * @since JDK1.8
 */
@RestController
@RequestMapping("/itemPackRuleController")
@Api(tags = " 物料和包装规则维护" )
public class ItemPackRuleController {

    private final Logger logger = LoggerFactory.getLogger(ItemPackRuleController.class);

    @Autowired
    private ItemPackRuleService itemPackRuleService;


    @PostMapping("/save")
    @ApiOperation(value="保存物料和包装规则")
    public ResponseData saveItemGroup(String bo , @RequestBody List<ItemPackRuleDetailDto> itemPackRuleDto ) throws CommonException {
        itemPackRuleService.saveItemAndPackRule(bo ,itemPackRuleDto);
        List<ItemPackRuleDetailDto> itemPackRuleDetailDtoList = itemPackRuleService.getItemAndPackRule(itemPackRuleDto.get(0).getItem() , itemPackRuleDto.get(0).getVersion());
        return ResponseData.success(itemPackRuleDetailDtoList);
    }

}


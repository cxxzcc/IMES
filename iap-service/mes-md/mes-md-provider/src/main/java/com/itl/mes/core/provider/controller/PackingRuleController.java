package com.itl.mes.core.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.PackingRuleDto;
import com.itl.mes.core.api.service.PackingRuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author FengRR
 * @date 2021/5/26
 * @since JDK1.8
 */
@RestController
@RequestMapping("/packingRule")
@Api(tags = " 包装规则" )
public class PackingRuleController {

    @Resource
    private PackingRuleService packingRuleService;

    @PostMapping("/query")
    @ApiOperation(value = "查询包装规则")
    public ResponseData<IPage<PackingRuleDto>> getPackingRule(@RequestBody PackingRuleDto packingRuleDto) throws CommonException {
        try {
            return ResponseData.success(packingRuleService.findList(packingRuleDto));
        }catch (Exception e){
            return ResponseData.error(e.getMessage());
        }
    }

    @PostMapping("/queryRuleDel")
    @ApiOperation(value = "根据规则bo查询包装规则明细")
    public ResponseData<PackingRuleDto> getRuleDel(@RequestBody PackingRuleDto packingRuleDto) throws CommonException {
        try {
            return ResponseData.success(packingRuleService.findPackRuleDelList(packingRuleDto));
        }catch (Exception e){
            return ResponseData.error(e.getMessage());
        }
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新包装规则")
    public ResponseData updatePackingRule(@RequestBody PackingRuleDto packingRuleDto) {
        packingRuleService.updatePackingRule(packingRuleDto);
        return ResponseData.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除包装信息")
    public ResponseData<String> delete(@RequestBody List<String> bos) throws CommonException {
        packingRuleService.deletePackingRule(bos);
        return ResponseData.success();
    }

    @PostMapping("/deleteDel")
    @ApiOperation(value = "删除包装规则明细")
    public ResponseData<String> deleteDel(@RequestBody List<String> bos) throws CommonException {
        packingRuleService.deletePackingRuleDel(bos);
        return ResponseData.success();
    }

    @PostMapping("/add")
    @ApiOperation(value="新增包装规则信息")
    public ResponseData<PackingRuleDto> add(@RequestBody PackingRuleDto packingRuleDto) throws CommonException {
        packingRuleService.addPackingRule(packingRuleDto);
        PackingRuleDto dto = packingRuleService.findRule(packingRuleDto.getPackRule());
        return ResponseData.success(dto);
    }

    @PostMapping("/save")
    @ApiOperation(value="保存包装规则信息")
    public ResponseData<PackingRuleDto> save(@RequestBody PackingRuleDto packingRuleDto) throws CommonException {
        packingRuleService.savePackingRule(packingRuleDto);
        PackingRuleDto dto = packingRuleService.findRule(packingRuleDto.getPackRule());
        return ResponseData.success(dto);
    }



}


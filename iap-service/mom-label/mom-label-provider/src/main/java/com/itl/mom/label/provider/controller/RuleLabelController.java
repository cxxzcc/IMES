package com.itl.mom.label.provider.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.constant.BOPrefixEnum;
import com.itl.mom.label.api.dto.ruleLabel.ItemColumns;
import com.itl.mom.label.api.dto.ruleLabel.RuleLabelQueryDto;
import com.itl.mom.label.api.dto.ruleLabel.RuleLabelSaveDto;
import com.itl.mom.label.api.dto.ruleLabel.RuleLabelShowDto;
import com.itl.mom.label.api.entity.label.LabelEntityParams;
import com.itl.mom.label.api.entity.ruleLabel.RuleLabel;
import com.itl.mom.label.api.entity.ruleLabel.RuleLabelDetail;
import com.itl.mom.label.api.enums.FLAG;
import com.itl.mom.label.api.service.ruleLabel.RuleLabelDetailService;
import com.itl.mom.label.api.service.ruleLabel.RuleLabelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author yaoxiang
 * @date 2021/1/21
 * @since JDK1.8
 */
@Api(tags = "元素(物料/工单/设备/容器/载具/单据)规则模板")
@RestController
@RequestMapping("/ruleLabel")
public class RuleLabelController {
    @Autowired
    private RuleLabelService ruleLabelService;

    @Autowired
    private RuleLabelDetailService ruleLabelDetailService;

    @PostMapping("/save")
    @ApiOperation("保存")
    public ResponseData<String> save(@RequestBody RuleLabelSaveDto saveDto) throws CommonException {
        if (ObjectUtil.isNull(saveDto)) {
            throw new CommonException("参数不能为空", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        ruleLabelService.saveAndUpdate(saveDto);
        return ResponseData.success("success");
    }

    @PostMapping("/page")
    @ApiOperation("[物料]分页查询")
    public ResponseData<IPage<RuleLabelShowDto>> page(@RequestBody RuleLabelQueryDto queryDto) throws CommonException {
        queryDto.setElementType(BOPrefixEnum.ITEM.getPrefix());
        return ResponseData.success(ruleLabelService.queryPage(queryDto));
    }

    @PostMapping("/pageBySO")
    @ApiOperation("[工单]分页查询")
    public ResponseData<IPage<RuleLabelShowDto>> pageBySO(@RequestBody RuleLabelQueryDto queryDto) throws CommonException {
        queryDto.setElementType(BOPrefixEnum.SO.getPrefix());
        return ResponseData.success(ruleLabelService.queryPage(queryDto));
    }

    @PostMapping("/pageByCar")
    @ApiOperation("[载具]分页查询")
    public ResponseData<IPage<RuleLabelShowDto>> pageByCar(@RequestBody RuleLabelQueryDto queryDto) throws CommonException {
        queryDto.setElementType(BOPrefixEnum.CARRIER.getPrefix());
        return ResponseData.success(ruleLabelService.queryPage(queryDto));
    }

    @PostMapping("/pageDeviceRule")
    @ApiOperation("[设备]分页查询")
    public ResponseData<IPage<RuleLabelShowDto>> pageDeviceRule(@RequestBody RuleLabelQueryDto queryDto) throws CommonException {
        queryDto.setElementType(BOPrefixEnum.RES.getPrefix());
        return ResponseData.success(ruleLabelService.queryPage(queryDto));
    }

    @PostMapping("/pagePackRule")
    @ApiOperation("[包装]分页查询")
    public ResponseData<IPage<RuleLabelShowDto>> pagePackRule(@RequestBody RuleLabelQueryDto queryDto) throws CommonException {
        queryDto.setElementType(BOPrefixEnum.PK.getPrefix());
        return ResponseData.success(ruleLabelService.queryPage(queryDto));
    }

    @PostMapping("/pageDocumentRule")
    @ApiOperation("[单据]分页查询")
    public ResponseData<IPage<RuleLabelShowDto>> pageDocumentRule(@RequestBody RuleLabelQueryDto queryDto) throws CommonException {
        queryDto.setElementType("DOCUMENT");
        return ResponseData.success(ruleLabelService.queryPage(queryDto));
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除")
    public ResponseData<String> delete(@RequestBody String[] bos) throws CommonException {
        if (ArrayUtil.isEmpty(bos)) {
            throw new CommonException("参数为空!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        ruleLabelService.delete(bos);
        return ResponseData.success("success");
    }

    @GetMapping("/getItemColumns")
    @ApiOperation("获取[物料]表字段及自定义字段")
    public ResponseData<List<ItemColumns>> getItemColumns() {
        return ResponseData.success(ruleLabelService.getColumns(FLAG.ITEM));
    }

    @GetMapping("/getShopOrderColumns")
    @ApiOperation("获取[工单]表字段及自定义字段")
    public ResponseData<List<ItemColumns>> getShopOrderColumns() {
        return ResponseData.success(ruleLabelService.getColumns(FLAG.SHOP_ORDER));
    }

    @GetMapping("/getCarColumns")
    @ApiOperation("获取[载具]表字段及自定义字段")
    public ResponseData<List<ItemColumns>> getCarColumns() {
        return ResponseData.success(ruleLabelService.getColumns(FLAG.CARRIER));
    }

    @GetMapping("/getDeviceColumns")
    @ApiOperation("获取[设备]表字段及自定义字段")
    public ResponseData<List<ItemColumns>> getDeviceColumns() {
        return ResponseData.success(ruleLabelService.getColumns(FLAG.DEVICE));
    }

    @GetMapping("/getDeviceTypeColumns")
    @ApiOperation("获取[设备类型]表字段")
    public ResponseData<List<ItemColumns>> getDeviceTypeColumns() {
        return ResponseData.success(ruleLabelService.getColumns(FLAG.DEVICE_TYPE));
    }

    @GetMapping("/getPackColumns")
    @ApiOperation("获取[包装相关]表字段")
    public ResponseData<List<ItemColumns>> getPackColumns() {
        return ResponseData.success(ruleLabelService.getColumns(FLAG.PACK, FLAG.PACK_LEVEL));
    }

    @GetMapping("/getLabelEntityParams/{id}")
    @ApiOperation("获取标签模板变量")
    public ResponseData<List<LabelEntityParams>> getLabelEntityParams(@PathVariable("id")String id) {
        return ResponseData.success(ruleLabelService.getLabelEntityParams(id));
    }

    @GetMapping("/generatorCode/{bo}/{elementBo}/{number}")
    @ApiOperation("生成该规则对应编码number条")
    public ResponseData<List<String>> generatorCode(@PathVariable("bo") String bo,
                                                    @PathVariable("elementBo") String elementBo,
                                                    @PathVariable("number") Integer number,
                                                    @PathVariable("paramsMap") Map<String,Object> paramsMap) throws CommonException {
        // 查询itemRuleLabel和itemRuleLabelDetail
        RuleLabel ruleLabel = ruleLabelService.getById(bo);
        List<RuleLabelDetail> list = ruleLabelDetailService.list(
                new QueryWrapper<RuleLabelDetail>()
                        .lambda()
                        .eq(RuleLabelDetail::getIrlBo, bo));
        return ResponseData.success(ruleLabelService.generatorCode(ruleLabel, list, elementBo, number,paramsMap));
    }

    @GetMapping("/get/{bo}")
    @ApiOperation("查询明细(for [物料][工单][设备][载具][包装][单据]编辑)")
    public ResponseData<RuleLabelShowDto> getByBo(@PathVariable("bo") String bo) {
        return ResponseData.success(ruleLabelService.getByBo(bo));
    }

    @GetMapping("/get/{ruleLabel}/{elementType}")
    @ApiOperation("查询明细(for [物料][工单][设备][载具][包装]编辑)")
    public ResponseData<RuleLabelShowDto> getByCode(@PathVariable("ruleLabel") String ruleLabel, @PathVariable("elementType") String elementType) {
        return ResponseData.success(ruleLabelService.getByCode(ruleLabel,elementType));
    }

}

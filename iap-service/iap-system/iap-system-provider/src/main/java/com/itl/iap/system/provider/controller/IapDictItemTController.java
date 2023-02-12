package com.itl.iap.system.provider.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.system.api.dto.IapDictItemTDto;
import com.itl.iap.system.api.entity.IapDictItemT;
import com.itl.iap.system.api.entity.IapDictT;
import com.itl.iap.system.api.service.IapDictItemTService;
import com.itl.iap.system.api.service.IapDictTService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 字典表详情表Controller
 *
 * @author 李骐光
 * @date 2020-06-16
 * @since jdk1.8
 */
@Api("System-数据字典详情表控制层")
@RestController
@RequestMapping("/iapDictItemT")
public class IapDictItemTController {
    @Autowired
    private IapDictItemTService iapDictItemService;
    @Autowired
    private IapDictTService iapDictTService;

    @PostMapping("/queryDictCodeOrName")
    @ApiOperation(value = "通过字典编号和名称查询", notes = "通过字典编号和名称查询")
    public ResponseData<List<IapDictItemTDto>> queryDictCodeOrName(@RequestBody IapDictItemTDto iapDictItemDto) {
        return ResponseData.success(iapDictItemService.queryDictCodeOrName(iapDictItemDto));
    }


    @GetMapping("/queryDictCode")
    @ApiOperation(value = "通过字典编号和名称查询", notes = "通过字典编号和名称查询")
    public ResponseData<List<IapDictItemTDto>> queryDictCode(@RequestParam("dictCode") String dictCode) {
        return ResponseData.success(iapDictItemService.queryDictCode(dictCode));
    }

    @PostMapping("/insertIapDictItemT")
    @ApiOperation(value = "新增字典", notes = "新增字典")
    public ResponseData<Integer> insertIapDictItemT(@RequestBody IapDictItemTDto iapDictItemDto) throws CommonException {
        IapDictT byId = iapDictTService.getById(iapDictItemDto.getIapDictId());
        if(byId == null) {
            throw new CommonException("未找到字典!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        iapDictItemDto.setParentCode(byId.getCode());
        return ResponseData.success(iapDictItemService.insertIapDictItemT(iapDictItemDto));
    }

    @PostMapping("/updateIapDictItemT")
    @ApiOperation(value = "修改字典", notes = "修改字典")
    public ResponseData<Integer> updateIapDictItemT(@RequestBody  IapDictItemTDto iapDictItemDto) throws CommonException {
        List<IapDictItemTDto> itemDtoByIds = iapDictItemService.getItemDtoByIds(Arrays.asList(iapDictItemDto.getId()));
        if(CollUtil.isEmpty(itemDtoByIds)) {
            throw new CommonException("未找到字典项!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        IapDictItemTDto item = itemDtoByIds.get(0);
        if(item == null) {
            throw new CommonException("未找到字典项!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        iapDictItemDto.setParentCode(item.getParentCode());
        return ResponseData.success(iapDictItemService.updateIapDictItemT(iapDictItemDto));
    }

    @PostMapping("/deleteByIds")
    @ApiOperation(value = "通过id批量删除数据", notes = "通过id批量删除数据")
    public ResponseData<Boolean> deleteByIds(@RequestBody List<String> ids) {
        List<IapDictItemTDto> itemDtoByIds = iapDictItemService.getItemDtoByIds(ids);
        if(CollUtil.isEmpty(itemDtoByIds)) {
            throw new CommonException("未找到字典项!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        List<String> parentCodes = itemDtoByIds.stream().map(IapDictItemTDto::getParentCode).distinct().collect(Collectors.toList());
        if(parentCodes.size() > 1) {
            throw new CommonException("非法操作!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return ResponseData.success(iapDictItemService.deleteByIds(ids, parentCodes.get(0)));
    }

    @GetMapping("/getCode/{code}")
    @ApiOperation(value = "根据数据字典编码获取取值", notes = "根据数据字典编码获取取值")
    public ResponseData<List<IapDictItemT>> getCode(@PathVariable("code") String code) {
        return ResponseData.success(iapDictItemService.getCode(code));
    }

    /**
     * 根据字典code查询字典项map。 Key=字典项keyvalue;value=字典项名称
     * @param code 字典编码code
     * @return Map.
     * */
    @GetMapping("/itemMapByParentCode")
    public ResponseData<Map<String, String>> getDictItemMapByParentCode(String code) {
        if(StrUtil.isBlank(code)) {
            return ResponseData.success(Collections.emptyMap());
        }
        List<IapDictItemTDto> list = iapDictItemService.queryDictCode(code);
        if(CollUtil.isEmpty(list)) {
            return ResponseData.success(Collections.emptyMap());
        }
        Map<String, String> collect = list.stream().collect(Collectors.toMap(IapDictItemTDto::getKeyValue, IapDictItemTDto::getName));
        return ResponseData.success(collect);
    }
}

package com.itl.mes.core.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.ConfigItemDto;
import com.itl.mes.core.api.entity.ConfigItem;
import com.itl.mes.core.api.service.ConfigItemService;
import com.itl.mes.core.api.vo.ConfigItemVo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author chenjx1
 * @since 2021-11-9
 */
@RestController
@RequestMapping("/configItem")
@Api(tags = "配置项")
public class ConfigItemController {
    private final Logger logger = LoggerFactory.getLogger(ConfigItemController.class);

    @Autowired
    public ConfigItemService configItemService;


    @PostMapping("/save")
    @ApiOperation(value = "保存配置项信息")
    public ResponseData save(@RequestBody List<ConfigItemVo> configItemVoList) throws CommonException{
        if (configItemVoList == null) {
            throw new CommonException("参数不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        configItemService.save(configItemVoList);
        return ResponseData.success();
    }

    @PostMapping("/queryPage")
    @ApiOperation(value = "分页查询配置项信息")
    public ResponseData<IPage<ConfigItemDto>> queryPage(@RequestBody ConfigItemDto configItemDto) throws CommonException {
        if (configItemDto == null) throw new CommonException("参数不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        IPage<ConfigItemDto> configItemList = configItemService.selectPageList(configItemDto);
        return ResponseData.success(configItemList);
    }

    @PostMapping("/query")
    @ApiOperation(value = "查询配置项信息")
    public ResponseData<List<ConfigItem>> query(@RequestBody ConfigItem configItem) throws CommonException {
        if (configItem == null) throw new CommonException("参数不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        List<ConfigItem> configItemList = configItemService.selectList(configItem);
        return ResponseData.success(configItemList);
    }

    @GetMapping("/delete")
    @ApiOperation(value = "删除配置项信息")
    public ResponseData<String> delete(String bo) throws CommonException {
        if (bo == null || "".equals(bo)){
            throw new CommonException("BO不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        int delete = configItemService.delete(bo);
        if (delete == 0) throw new CommonException("此配置项不存在", CommonExceptionDefinition.BASIC_EXCEPTION);
        return ResponseData.success("success");
    }

}

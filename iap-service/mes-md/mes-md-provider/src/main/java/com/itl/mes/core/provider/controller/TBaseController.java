package com.itl.mes.core.provider.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.mapper.QueryWrapperUtil;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.TBaseDTO;
import com.itl.mes.core.api.dto.TBaseQueryDTO;
import com.itl.mes.core.api.entity.TBase;
import com.itl.mes.core.api.service.TBaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import java.util.List;


@Validated
@RequestMapping("/base")
@Api(tags = "基地-维护")
@RestController
@RequiredArgsConstructor
public class TBaseController {

    private final TBaseService tBaseService;

    @ApiOperation(value = "基地信息分页列表")
    @PostMapping(value = "/page")
    public ResponseData<IPage<TBase>> page(@RequestBody TBaseQueryDTO tBaseQueryDTO) {
        QueryWrapper queryWrapper = QueryWrapperUtil.convertQuery(tBaseQueryDTO);
        IPage<TBase> page = tBaseService.page(tBaseQueryDTO.getPage(), queryWrapper);
        return ResponseData.success(page);
    }

    @ApiOperation(value = "基地信息列表")
    @PostMapping(value = "/list")
    ResponseData<List<TBase>> list(@RequestBody TBaseQueryDTO tBaseQueryDTO) {
        QueryWrapper<TBase> queryWrapper = QueryWrapperUtil.convertQuery(tBaseQueryDTO);
        queryWrapper.lambda().eq(TBase::getIsUse, "1");
        List<TBase> list = tBaseService.list(queryWrapper);
        return ResponseData.success(list);
    }

    @ApiOperation(value = "基地信息保存")
    @PostMapping(value = "/save")
    ResponseData save(@RequestBody @Validated TBaseDTO tBaseDTO) {
        String id = tBaseDTO.getId();
        String code = tBaseDTO.getCode();
        QueryWrapper<TBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TBase::getCode, code);
        TBase tBase = tBaseService.getOne(queryWrapper);
        if (tBase != null && !tBase.getId().equals(id)) {
            throw new CommonException("编码不能重复", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if (tBase == null) {
            tBase = new TBase();
        }
        BeanUtil.copyProperties(tBaseDTO, tBase);
        tBaseService.saveOrUpdate(tBase);
        return ResponseData.success();
    }

    @ApiOperation(value = "基地信息删除")
    @PostMapping(value = "/delete")
    ResponseData delete(@RequestBody @NotEmpty List<String> list) {
        //TODO 验证不能删除
        tBaseService.removeByIds(list);
        return ResponseData.success();
    }

}

package com.itl.iap.system.provider.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.mapper.QueryWrapperUtil;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.system.api.dto.IapSysParameterQueryDto;
import com.itl.iap.system.api.dto.IapSysParameterSaveDTO;
import com.itl.iap.system.api.entity.IapSysParameter;
import com.itl.iap.system.api.service.IapSysParameterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "系统全局参数")
@RestController
@RequestMapping("/iapSysParameter")
@RequiredArgsConstructor
@Validated
public class IapSysParameterController {

    private final IapSysParameterService iapSysParameterService;

    @PostMapping("/page")
    @ApiOperation(value = "系统参数分页查询")
    public ResponseData<IPage<IapSysParameter>> page(@RequestBody IapSysParameterQueryDto iapSysParameterQueryDto) {
        QueryWrapper queryWrapper = QueryWrapperUtil.convertQuery(iapSysParameterQueryDto);
        IPage<IapSysParameter> page = iapSysParameterService.pageList(iapSysParameterQueryDto.getPage(), queryWrapper);
        return ResponseData.success(page);
    }

    @PostMapping("/save")
    @ApiOperation(value = "系统参数保存")
    public ResponseData save(@RequestBody @Validated IapSysParameterSaveDTO iapSysParameterSaveDTO) {
        String id = iapSysParameterSaveDTO.getId();
        String code = iapSysParameterSaveDTO.getCode();
        QueryWrapper<IapSysParameter> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(IapSysParameter::getCode, code);
        IapSysParameter one = iapSysParameterService.getOne(queryWrapper);
        if (one != null && !one.getId().equals(id)) {
            throw new CommonException("编码不能重复", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if (one == null) {
            one = new IapSysParameter();
        }
        BeanUtils.copyProperties(iapSysParameterSaveDTO, one);
        iapSysParameterService.saveOrUpdate(one);
        return ResponseData.success();
    }

    @PostMapping("/initData")
    @ApiOperation(value = "初始化")
    public ResponseData initData(@RequestBody List<String> ids) {
        IapSysParameter iapSysParameter = new IapSysParameter();
        iapSysParameter.setShowValue("");
        UpdateWrapper<IapSysParameter> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().in(CollectionUtil.isNotEmpty(ids), IapSysParameter::getId, ids);
        iapSysParameterService.update(iapSysParameter, updateWrapper);
        return ResponseData.success();
    }

}

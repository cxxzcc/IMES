package com.itl.mes.core.provider.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.mapper.QueryWrapperUtil;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.TProjectDTO;
import com.itl.mes.core.api.dto.TProjectQueryDTO;
import com.itl.mes.core.api.entity.TProject;
import com.itl.mes.core.api.service.TProjectService;
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
@RequestMapping("/project")
@Api(tags = "项目-维护")
@RestController
@RequiredArgsConstructor
public class TProjectController {

    private final TProjectService tProjectService;

    @ApiOperation(value = "项目信息分页列表")
    @PostMapping(value = "/page")
    public ResponseData<IPage<TProject>> page(@RequestBody TProjectQueryDTO tProjectQueryDTO) {
        IPage<TProject> page = tProjectService.pageList(tProjectQueryDTO);
        return ResponseData.success(page);
    }

    @ApiOperation(value = "项目信息列表")
    @PostMapping(value = "/list")
    ResponseData<List<TProject>> list(@RequestBody TProjectQueryDTO tProjectQueryDTO) {
        List<TProject> list = tProjectService.allList(tProjectQueryDTO);
        return ResponseData.success(list);
    }

    @ApiOperation(value = "项目信息保存")
    @PostMapping(value = "/save")
    ResponseData save(@RequestBody @Validated TProjectDTO tProjectDTO) {
        String id = tProjectDTO.getId();
        String code = tProjectDTO.getCode();
        QueryWrapper<TProject> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TProject::getCode, code);
        TProject tProject = tProjectService.getOne(queryWrapper);
        if (tProject != null && !tProject.getId().equals(id)) {
            throw new CommonException("编码不能重复", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if (tProject == null) {
            tProject = new TProject();
        }
        BeanUtil.copyProperties(tProjectDTO, tProject);
        tProjectService.saveOrUpdate(tProject);
        return ResponseData.success();
    }

    @ApiOperation(value = "项目信息删除")
    @PostMapping(value = "/delete")
    ResponseData delete(@RequestBody @NotEmpty List<String> list) {
        //TODO 验证不能删除
        tProjectService.removeByIds(list);
        return ResponseData.success();
    }

}

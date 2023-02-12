package com.itl.plugins.report.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.mapper.QueryWrapperUtil;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.plugins.report.common.ReportConfig;
import com.itl.plugins.report.dto.ReportContentDTO;
import com.itl.plugins.report.dto.ReportContentQueryDTO;
import com.itl.plugins.report.entity.ReportContent;
import com.itl.plugins.report.service.ReportContentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author cjq
 * @Date 2021/11/29 10:46 上午
 * @Description 报表管理
 */
@RestController
@RequestMapping("/report")
@Api(tags = "报表管理")
@RequiredArgsConstructor
@Validated
public class ReportContentController {

    private final ReportContentService reportContentService;


    @PostMapping("/delete")
    @ApiOperation(value = "逻辑删除")
    public ResponseData delete(@RequestBody @NotEmpty List<String> ids) {
        ReportContent reportContent = new ReportContent();
        reportContent.setIsDelete("1");
        UpdateWrapper<ReportContent> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().in(ReportContent::getId, ids);
        reportContentService.update(reportContent, updateWrapper);
        return ResponseData.success();
    }

    @PostMapping("/page")
    @ApiOperation(value = "分页列表")
    public ResponseData<IPage<ReportContent>> page(@RequestBody ReportContentQueryDTO queryDTO) {
        QueryWrapper<ReportContent> queryWrapper = QueryWrapperUtil.convertQuery(queryDTO);
        queryWrapper.lambda().ne(ReportContent::getIsDelete, "1");
        IPage<ReportContent> page = reportContentService.page(queryDTO.getPage(), queryWrapper);
        return ResponseData.success(page);
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存")
    public ResponseData save(@RequestBody @Validated ReportContentDTO reportContentDTO) {
        String id = reportContentDTO.getId();
        String code = reportContentDTO.getCode();
        QueryWrapper<ReportContent> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(ReportContent::getCode, code);
        ReportContent reportContent = reportContentService.getOne(queryWrapper);
        if (reportContent != null && !reportContent.getId().equals(id)) {
            throw new CommonException("编码不能重复", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if (reportContent == null) {
            reportContent = new ReportContent();
        }
        BeanUtils.copyProperties(reportContentDTO, reportContent);
        String fileName = reportContent.getCode() + ReportConfig.SUFFIX;
        reportContent.setFileName(fileName);
        reportContent.setDesignUrl(ReportConfig.DESIGN_BASIC_URL + "&code=" + code);
        reportContent.setIsDelete("0");
        reportContentService.saveOrUpdate(reportContent);
        return ResponseData.success();
    }


}

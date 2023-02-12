package com.itl.mes.core.provider.controller;


import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.base.BaseEntity;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.mapper.QueryWrapperUtil;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.*;
import com.itl.mes.core.api.entity.TInstrumentType;
import com.itl.mes.core.api.service.TInstrumentTypeService;
import com.itl.mes.core.provider.listener.InstrumentTypeDataListener;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Validated
@RequestMapping("/instrument/type")
@Api(tags = "仪器类型-维护")
@RestController
@RequiredArgsConstructor
public class TInstrumentTypeController {

    private final TInstrumentTypeService tInstrumentTypeService;

    @ApiOperation(value = "仪器类型分页列表")
    @PostMapping(value = "/page")
    public ResponseData<IPage<TInstrumentType>> page(@RequestBody @Validated TInstrumentTypeQueryDTO tInstrumentTypeQueryDTO) {
        QueryWrapper queryWrapper = QueryWrapperUtil.convertQuery(tInstrumentTypeQueryDTO);
        queryWrapper.orderByDesc(BaseEntity.Fields.updateTime);
        IPage<TInstrumentType> page = tInstrumentTypeService.page(tInstrumentTypeQueryDTO.getPage(), queryWrapper);
        return ResponseData.success(page);
    }

    @ApiOperation(value = "仪器类型列表")
    @PostMapping(value = "/list")
    ResponseData<List<TInstrumentType>> list(@RequestBody @Validated TInstrumentTypeQueryDTO tInstrumentTypeQueryDTO) {
        QueryWrapper queryWrapper = QueryWrapperUtil.convertQuery(tInstrumentTypeQueryDTO);
        queryWrapper.orderByDesc(BaseEntity.Fields.updateTime);
        List<TInstrumentType> list = tInstrumentTypeService.list(queryWrapper);
        return ResponseData.success(list);
    }

    @ApiOperation(value = "仪器类型保存")
    @PostMapping(value = "/save")
    ResponseData save(@RequestBody @Validated TInstrumentTypeDTO tInstrumentTypeDTO) {
        String id = tInstrumentTypeDTO.getId();
        String name = tInstrumentTypeDTO.getName();
        QueryWrapper<TInstrumentType> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TInstrumentType::getName, name);
        TInstrumentType tInstrumentType = tInstrumentTypeService.getOne(queryWrapper);
        if (tInstrumentType != null && !tInstrumentType.getId().equals(id)) {
            throw new CommonException("名称不能重复", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if (tInstrumentType == null) {
            tInstrumentType = new TInstrumentType();
        }
        BeanUtil.copyProperties(tInstrumentTypeDTO, tInstrumentType);
        tInstrumentTypeService.saveOrUpdate(tInstrumentType);
        return ResponseData.success();
    }

    @ApiOperation(value = "仪器类型删除")
    @PostMapping(value = "/delete")
    ResponseData delete(@RequestBody @NotEmpty List<String> list) {
        //TODO 验证不能删除
        tInstrumentTypeService.removeByIds(list);
        return ResponseData.success();
    }

    @ApiOperation(value = "下载模板")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response) {
        try (
                InputStream in = (new ClassPathResource("templates/instrumentType.xls")).getInputStream();
                ServletOutputStream out = response.getOutputStream();
        ) {
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.ms-excel");
            String fileName = URLEncoder.encode("仪器名称导入", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
//            response.setContentLength(in.available());
            IOUtils.copy(in, out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @ApiOperation(value = "仪器类型导入 ")
    @PostMapping("/importFile")
    public ResponseData importFile(@RequestParam("file") MultipartFile file) throws Exception {
        InstrumentTypeDataListener instrumentTypeDataListener = new InstrumentTypeDataListener();
        //获取项目信息
        List<TInstrumentType> list = tInstrumentTypeService.list();
        Map<String, TInstrumentType> collect = list.stream().collect(Collectors.toMap(TInstrumentType::getName, Function.identity()));
        instrumentTypeDataListener.setProjectMap(collect);
        EasyExcel.read(file.getInputStream(), InstrumentTypeDataDTO.class, instrumentTypeDataListener).sheet().doRead();
        List errorList = instrumentTypeDataListener.getErrorList();
        List<InstrumentTypeDataDTO> cachedDataList = instrumentTypeDataListener.getCachedDataList();
        if (errorList.size() > 0) {
            return ResponseData.success(errorList);
        } else {
            List<TInstrumentType> insertDto = new ArrayList<>();
            for (InstrumentTypeDataDTO instrumentTypeDataDTO : cachedDataList) {
                TInstrumentType tInstrumentType = BeanUtil.copyProperties(instrumentTypeDataDTO, TInstrumentType.class);
                insertDto.add(tInstrumentType);
            }
            tInstrumentTypeService.saveBatch(insertDto);
        }
        return ResponseData.success();
    }


}

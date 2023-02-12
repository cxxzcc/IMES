package com.itl.mes.core.provider.controller;


import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.itl.iap.common.base.base.ValidList;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.*;
import com.itl.mes.core.api.entity.TBase;
import com.itl.mes.core.api.entity.TProject;
import com.itl.mes.core.api.entity.TProjectBaseActual;
import com.itl.mes.core.api.service.TBaseService;
import com.itl.mes.core.api.service.TProjectBaseActualService;
import com.itl.mes.core.api.service.TProjectService;
import com.itl.mes.core.api.vo.TProjectBaseActualVO;
import com.itl.mes.core.provider.listener.ProjectBaseActualDataListener;
import com.itl.mes.core.provider.mapper.TProjectBaseActualMapper;
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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Validated
@RequestMapping("/project/base/actual")
@Api(tags = "各基地对标-维护")
@RestController
@RequiredArgsConstructor
public class TProjectBaseActualController {

    private final TProjectBaseActualService tProjectBaseActualService;
    private final TProjectBaseActualMapper tProjectBaseActualMapper;
    private final TProjectService tProjectService;
    private final TBaseService tBaseService;


    @ApiOperation(value = "各基地对标页面")
    @PostMapping(value = "/list")
    public ResponseData<IPage<TProjectBaseActualVO>> page(@RequestBody TProjectBaseActualQueryDTO tProjectActualQueryDTO) {
        IPage<TProjectBaseActualVO> page = tProjectBaseActualService.pageList(tProjectActualQueryDTO);
        return ResponseData.success(page);
    }


    @ApiOperation(value = "基地对标导入 ", notes = "导入")
    @PostMapping("/importFile")
    public ResponseData importFile(@RequestParam("file") MultipartFile file) throws Exception {
        ProjectBaseActualDataListener projectBaseActualDataListener = new ProjectBaseActualDataListener();
        //获取基地信息
        List<TBase> baseList = tBaseService.list();
        Map<String, TBase> baseMap = baseList.stream().collect(Collectors.toMap(TBase::getCode, Function.identity()));
        //获取项目信息
        List<TProject> list = tProjectService.list();
        Map<String, TProject> collect = list.stream().collect(Collectors.toMap(TProject::getCode, Function.identity()));
        projectBaseActualDataListener.setProjectMap(collect);
        projectBaseActualDataListener.setBaseMap(baseMap);
        EasyExcel.read(file.getInputStream(), ProjectBaseActualDataDTO.class, projectBaseActualDataListener).sheet().doRead();
        List errorList = projectBaseActualDataListener.getErrorList();
        List<ProjectBaseActualDataDTO> cachedDataList = projectBaseActualDataListener.getCachedDataList();
        if (errorList.size() > 0) {
            return ResponseData.success(errorList);
        } else {
            List<TProjectBaseActual> insertDto = new ArrayList<>();
            for (ProjectBaseActualDataDTO projectActualDataDTO : cachedDataList) {
                TProjectBaseActual tProjectBaseActual = new TProjectBaseActual();
                tProjectBaseActual.setProject_code(projectActualDataDTO.getProjectCode());
                tProjectBaseActual.setUse_date(projectActualDataDTO.getUseDate());
                tProjectBaseActual.setBase_code(projectActualDataDTO.getBaseCode());
                tProjectBaseActual.setStandard(new BigDecimal(projectActualDataDTO.getStandard()));
                insertDto.add(tProjectBaseActual);
            }
            tProjectBaseActualMapper.insertOrUpdateBatch(insertDto);
        }
        return ResponseData.success();
    }

    @ApiOperation(value = "下载模板")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response) {
        try (
                InputStream in = (new ClassPathResource("templates/projectBaseActual.xls")).getInputStream();
                ServletOutputStream out = response.getOutputStream();
        ) {
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=projectBaseActual.xls");
//            response.setContentLength(in.available());
            IOUtils.copy(in, out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @ApiOperation(value = "基地对标保存")
    @PostMapping(value = "/save")
    public ResponseData save(@RequestBody @Validated ValidList<TProjectBaseActualDTO> list) {
        List<TProjectBaseActual> data = new ArrayList<>();
        for (TProjectBaseActualDTO dto : list) {
            TProjectBaseActual tProjectBaseActual = new TProjectBaseActual();
            tProjectBaseActual.setProject_code(dto.getProjectCode());
            tProjectBaseActual.setUse_date(dto.getUseDate());
            tProjectBaseActual.setBase_code(dto.getBaseCode());
            tProjectBaseActual.setActual(dto.getActual());
            tProjectBaseActual.setRange(dto.getRange());
            data.add(tProjectBaseActual);
        }
        tProjectBaseActualMapper.updateBatchById(data);
        return ResponseData.success();
    }

    @ApiOperation(value = "基地对标导出")
    @PostMapping(value = "/export")
    public void export(@RequestBody TProjectBaseActualQueryDTO tProjectActualQueryDTO, HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码
            InputStream in = (new ClassPathResource("templates/projectBaseActualExport.xlsx")).getInputStream();
//            String templateFileName = this.getClass().getResource("/templates").getPath() + File.separator + "projectBaseActualExport.xlsx";
            String fileName = URLEncoder.encode("各基地对标页面", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            //获取数据
            List<TProjectBaseActualVO> data = tProjectBaseActualService.allList(tProjectActualQueryDTO);
            List<ProjectBaseActualDataVO> list = new ArrayList<>();
            for (TProjectBaseActualVO datum : data) {
                ProjectBaseActualDataVO vo = BeanUtil.copyProperties(datum, ProjectBaseActualDataVO.class);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                vo.setUseDate(sdf.format(datum.getUseDate()));
                vo.setCreateTime(sdf1.format(datum.getCreateTime()));
                list.add(vo);
            }
            EasyExcel.write(response.getOutputStream())
                    .withTemplate(in)
                    .autoCloseStream(Boolean.FALSE)
                    .sheet()
                    .doWrite(list);
        } catch (Exception e) {
            // 重置response
            e.printStackTrace();
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().println("导出失败");
        }

    }


}

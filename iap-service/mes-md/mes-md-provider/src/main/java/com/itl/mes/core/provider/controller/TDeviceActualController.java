package com.itl.mes.core.provider.controller;


import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.itl.iap.common.base.base.ValidList;
import com.itl.iap.common.base.mapper.QueryWrapperUtil;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.constant.BOPrefixEnum;
import com.itl.mes.core.api.dto.*;
import com.itl.mes.core.api.entity.Device;
import com.itl.mes.core.api.entity.TProject;
import com.itl.mes.core.api.entity.TProjectActual;
import com.itl.mes.core.api.service.TProjectActualService;
import com.itl.mes.core.api.service.TProjectService;
import com.itl.mes.core.api.vo.TProjectActualVO;
import com.itl.mes.core.provider.listener.ProjectActualDataListener;
import com.itl.mes.core.provider.mapper.DeviceMapper;
import com.itl.mes.core.provider.mapper.TProjectActualMapper;
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
import javax.validation.constraints.NotBlank;
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
@RequestMapping("/device/actual")
@Api(tags = "基地内对标验证-维护")
@RestController
@RequiredArgsConstructor
public class TDeviceActualController {

    private final TProjectActualMapper tProjectActualMapper;
    private final DeviceMapper deviceMapper;
    private final TProjectService tProjectService;
    private final TProjectActualService tProjectActualService;


    @ApiOperation(value = "根据基地和仪器名称(类型)获取基准仪器")
    @PostMapping(value = "/getDeviceByBase")
    public ResponseData<Device> getDeviceByBase(@RequestBody BaseAndInstrumentDTO baseAndInstrumentDTO) {
        QueryWrapper<Device> queryWrapper = QueryWrapperUtil.convertQuery(baseAndInstrumentDTO);
        queryWrapper.lambda().eq(Device::getIsStandard, "1");
        Device device = deviceMapper.selectOne(queryWrapper);
        return ResponseData.success(device);
    }

    @ApiOperation(value = "根据仪器名称(类型)获取项目")
    @GetMapping(value = "/getProjectByDevice")
    public ResponseData getProjectByDevice(@RequestParam @NotBlank String instrumentTypeId) {
        QueryWrapper<TProject> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TProject::getInstrumentTypeId, instrumentTypeId);
        List<TProject> list = tProjectService.list(queryWrapper);
        return ResponseData.success(list);
    }

    @ApiOperation(value = "下载模板")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response) {
        try (
                InputStream in = (new ClassPathResource("templates/projectActual.xls")).getInputStream();
                ServletOutputStream out = response.getOutputStream();
        ) {
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=projectActual.xls");
//            response.setContentLength(in.available());
            IOUtils.copy(in, out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "基地对标-日常验证-导入 ", notes = "导入")
    @PostMapping("/importFile")
    public ResponseData importFile(@RequestParam("file") MultipartFile file) throws Exception {
        ProjectActualDataListener projectActualDataListener = new ProjectActualDataListener();
        //获取仪器信息
        String site = UserUtils.getSite();
        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().isNotNull(Device::getBaseId).isNotNull(Device::getInstrumentTypeId).eq(Device::getSite, site);
        List<Device> devices = deviceMapper.selectList(queryWrapper);
        Map<String, Device> deviceMap = devices.stream().collect(Collectors.toMap(Device::getDevice, Function.identity()));
        projectActualDataListener.setDeviceMap(deviceMap);
        //获取项目信息
        List<TProject> list = tProjectService.list();
        Map<String, TProject> collect = list.stream().collect(Collectors.toMap(TProject::getCode, Function.identity()));
        projectActualDataListener.setProjectMap(collect);
        EasyExcel.read(file.getInputStream(), ProjectActualDataDTO.class, projectActualDataListener).sheet().doRead();
        List errorList = projectActualDataListener.getErrorList();
        List<ProjectActualDataDTO> cachedDataList = projectActualDataListener.getCachedDataList();
        if (errorList.size() > 0) {
            return ResponseData.success(errorList);
        } else {
            List<TProjectActual> insertDto = new ArrayList<>();
            for (ProjectActualDataDTO projectActualDataDTO : cachedDataList) {
                TProjectActual tProjectActual = new TProjectActual();
                tProjectActual.setDevice_bo(BOPrefixEnum.RES.getPrefix() + ":" + site + "," + projectActualDataDTO.getDevice());
                tProjectActual.setProject_code(projectActualDataDTO.getProjectCode());
                tProjectActual.setUse_date(projectActualDataDTO.getUseDate());
                tProjectActual.setStandard(new BigDecimal(projectActualDataDTO.getStandard()));
                insertDto.add(tProjectActual);
            }
            tProjectActualMapper.insertOrUpdateBatch(insertDto);
        }
        return ResponseData.success();
    }

    @ApiOperation(value = "基地对标-日常验证-保存")
    @PostMapping(value = "/daily/save")
    public ResponseData save(@RequestBody @NotEmpty List<TProjectActualDTO> list) {
        String site = UserUtils.getSite();
        List<TProjectActual> data = new ArrayList<>();
        for (TProjectActualDTO dto : list) {
            TProjectActual tProjectActual = new TProjectActual();
            tProjectActual.setDevice_bo(BOPrefixEnum.RES.getPrefix() + ":" + site + "," + dto.getDevice());
            tProjectActual.setProject_code(dto.getProjectCode());
            tProjectActual.setUse_date(dto.getUseDate());
            tProjectActual.setActual(dto.getActual());
            tProjectActual.setRange(dto.getRange());
            tProjectActual.setResult(dto.getResult());
            data.add(tProjectActual);
        }
        tProjectActualMapper.updateBatchById(data);
        return ResponseData.success();
    }

    @ApiOperation(value = "基地对标-日常验证-页面")
    @PostMapping(value = "/daily/list")
    public ResponseData<List<TProjectActualVO>> page(@RequestBody TProjectActualQueryDTO tProjectActualQueryDTO) {
        List<TProjectActualVO> list = tProjectActualService.allList(tProjectActualQueryDTO);
        return ResponseData.success(list);
    }

    @ApiOperation(value = "基地对标-日常验证-导出")
    @PostMapping(value = "/daily/export")
    public void export(@RequestBody TProjectActualQueryDTO tProjectActualQueryDTO, HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码
            InputStream in = (new ClassPathResource("templates/projectActualExport.xlsx")).getInputStream();
//            String templateFileName = this.getClass().getResource("/templates").getPath() + File.separator + "projectActualExport.xlsx";
            String fileName = URLEncoder.encode("基地内对标日常验证", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            //获取数据
            List<TProjectActualVO> data = tProjectActualService.allList(tProjectActualQueryDTO);
            List<ProjectActualDataVO> list = new ArrayList<>();
            for (TProjectActualVO datum : data) {
                ProjectActualDataVO vo = BeanUtil.copyProperties(datum, ProjectActualDataVO.class);
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



    @ApiOperation(value = "基地对标-交叉验证-页面")
    @PostMapping(value = "/all/List")
    public ResponseData<List<TProjectActualVO>> list(@RequestBody TProjectActualQueryDTO tProjectActualQueryDTO) {
        List<TProjectActualVO> list = tProjectActualService.allList(tProjectActualQueryDTO);
        return ResponseData.success(list);
    }


    @ApiOperation(value = "基地对标-交叉验证-保存")
    @PostMapping(value = "/all/save")
    public ResponseData allSave(@RequestBody @Validated ValidList<TProjectActualDTO> list) {
        String site = UserUtils.getSite();
        List<TProjectActual> data = new ArrayList<>();
        for (TProjectActualDTO dto : list) {
            TProjectActual tProjectActual = BeanUtil.copyProperties(dto, TProjectActual.class);
            tProjectActual.setDevice_bo(BOPrefixEnum.RES.getPrefix() + ":" + site + "," + dto.getDevice());
            tProjectActual.setProject_code(dto.getProjectCode());
            tProjectActual.setUse_date(dto.getUseDate());
            data.add(tProjectActual);
        }
        tProjectActualMapper.updateBatchById(data);
        return ResponseData.success();
    }



}

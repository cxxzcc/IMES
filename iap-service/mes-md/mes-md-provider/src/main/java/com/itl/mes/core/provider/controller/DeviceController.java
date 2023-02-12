package com.itl.mes.core.provider.controller;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.ExcelUtils;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.base.utils.ValidationUtil;
import com.itl.iap.mes.client.service.FileUploadService;
import com.itl.mes.core.api.bo.DeviceHandleBO;
import com.itl.mes.core.api.constant.CustomCommonConstants;
import com.itl.mes.core.api.dto.DeviceChangeStateRequestVO;
import com.itl.mes.core.api.dto.DeviceConditionDto;
import com.itl.mes.core.api.dto.DeviceCountStatisticsDTO;
import com.itl.mes.core.api.dto.DeviceDto;
import com.itl.mes.core.api.entity.Device;
import com.itl.mes.core.api.entity.DeviceType;
import com.itl.mes.core.api.service.DeviceService;
import com.itl.mes.core.api.vo.DevicePlusVo;
import com.itl.mes.core.api.vo.DeviceVo;
import com.itl.mes.core.provider.utils.excel.DeviceVoVerifyHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author space
 * @since 2019-06-17
 */
@RestController
@RequestMapping("/devices")
@Api(tags = " 设备表")
public class DeviceController {
    private final Logger logger = LoggerFactory.getLogger(DeviceController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public DeviceService deviceService;

    @Autowired
    private DeviceVoVerifyHandler deviceVoVerifyHandler;

    @Value("${file.path}")
    private String filePath;

    @Autowired
    private FileUploadService fileUploadService;


    /**
     * 自定义条件查询设备
     *
     * @return
     */
    @PostMapping("/getDataByCondition")
    @ApiOperation(value = "自定义条件*查询设备")
    public ResponseData<List<DevicePlusVo>> getDataByCondition(@RequestBody DeviceConditionDto deviceLikeDto) {
        List<DevicePlusVo> list = deviceService.getDataByCondition(deviceLikeDto);
        return ResponseData.success(list);
    }

    /**
     * 根据id查询
     *
     * @param id 主键
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "通过主键查询数据")
    public ResponseData<Device> getDeviceById(@PathVariable String id) {
        return ResponseData.success(deviceService.getById(id));
    }


    @PostMapping("/selectDeviceWorkshop")
    @ApiOperation(value = "分页查询机台车间信息", notes = "分页查询机台车间信息")
    public ResponseData<IPage<Device>> findList(@RequestBody DeviceDto deviceDto) {
        return ResponseData.success(deviceService.selectDeviceWorkshop(deviceDto));
    }

    /**
     * 保存设备数据
     *
     * @param deviceVo
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value = "保存设备数据")
    public ResponseData<DeviceVo> saveDevice(@RequestBody DeviceVo deviceVo) throws CommonException {
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(deviceVo);
        if (validResult.hasErrors()) {
            throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        deviceService.saveDevice(deviceVo);
        deviceVo = deviceService.getDeviceVoByDevice(deviceVo.getDevice());
        return ResponseData.success(deviceVo);
    }

    /**
     * 修改设备状态
     *
     * @param code,state
     * @return
     */
    @GetMapping("/upateState")
    @ApiOperation(value = "修改设备状态")
    public ResponseData upateState(String code, String site, String state) throws CommonException {
        DeviceHandleBO deviceHandleBO = new DeviceHandleBO(site, code);
        deviceService.updateState(deviceHandleBO.getBo(), state);
        return ResponseData.success();
    }

    /**
     * 后台管理修改设备状态
     *
     * @param deviceChangeStateRequestVO
     * @return
     */
    @PutMapping("/changeState")
    @ApiOperation(value = "修改设备状态")
    public ResponseData changeState(@RequestBody DeviceChangeStateRequestVO deviceChangeStateRequestVO) throws CommonException {
        return ResponseData.success(deviceService.changeState(deviceChangeStateRequestVO));
    }


    /**
     * 根据device查询
     *
     * @param device 设备编号
     * @return
     */
    @GetMapping("/query")
    @ApiOperation(value = "通过设备编号查询数据")
    @ApiImplicitParam(name = "device", value = "设备编号", dataType = "string", paramType = "query")
    public ResponseData<DeviceVo> getDeviceVoByDevice(@RequestParam String device) throws CommonException {
        DeviceVo result = null;
        if (StrUtil.isBlank(device)) {
            throw new CommonException("设备编号不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        result = deviceService.getDeviceVoByDevice(device);
        if (result == null) {
            throw new CommonException("设备编号" + device + "数据未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return ResponseData.success(result);
    }


    /**
     * 根据device查询
     *
     * @param devices 设备编号列表
     * @return
     */
    @PostMapping("/query/list")
    @ApiOperation(value = "通过设备编号列表查询数据")
    public ResponseData<List<Device>> getDeviceVoByDevices(@RequestBody List<String> devices) throws CommonException {
        if (CollectionUtil.isEmpty(devices)) {
            throw new CommonException("设备编号不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        List<Device> result = deviceService.selectByDevices(devices);
        return ResponseData.success(result);
    }

    /**
     * 删除设备数据
     *
     * @param device
     * @param modifyDate
     * @return
     */
    @GetMapping("/delete")
    @ApiOperation(value = "删除设备数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "device", value = "设备编号", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "modifyDate", value = "修改时间", dataType = "string", paramType = "query")
    })
    public ResponseData<String> deleteDevice(String device, String modifyDate) throws CommonException, ParseException {
        if (StrUtil.isBlank(device)) {
            throw new CommonException("设备参数不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if (StrUtil.isBlank(modifyDate)) {
            throw new CommonException("修改日期不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        deviceService.deleteDevice(device.trim(), DateUtil.parse(modifyDate, CustomCommonConstants.DATE_FORMAT_CONSTANTS));
        return ResponseData.success("success");
    }

    @PostMapping("/getDeviceTypeVoList")
    @ApiOperation(value = "查询前500条设备类型数据页面初始化用")
    public ResponseData<IPage<DeviceType>> getDeviceTypeVoList(@RequestBody Page params) {
        IPage<DeviceType> deviceTypeVoList = deviceService.getDeviceTypeVoList(params);
        return ResponseData.success(deviceTypeVoList);
    }


    @PostMapping("/getScrewByLine")
    @ApiOperation(value = "查询前500条设备类型数据页面初始化用")
    public ResponseData getScrewByLine(@RequestBody Map<String, Object> params) {
        return ResponseData.success(deviceService.getScrewByLine(params));
    }


    /**
     * 获取所有设备数量
     */
    @GetMapping("/count/{site}")
    public ResponseData<Integer> getDeviceCountBySite(@PathVariable("site") String site) {
        return ResponseData.success(deviceService.getDeviceCountBySite(site));
    }

    /**
     * 根据设备bos查询设备名称列表
     */
    @PostMapping("/queryNames")
    public ResponseData<List<DeviceVo>> queryNameByBos(@RequestBody List<String> bos) {
        return ResponseData.success(deviceService.queryNameByBos(bos));
    }

    /**
     * 统计各个状态设备的数量
     */
    @GetMapping("/statistics/countGroupByState")
    @ApiOperation(value = "统计各个状态设备的数量")
    public ResponseData<List<DeviceCountStatisticsDTO>> queryDeviceCountStatisticsByState() {
        return ResponseData.success(deviceService.queryDeviceCountStatisticsByState(UserUtils.getSite()));
    }


    @ApiOperation(value = "export", notes = "导出模板", httpMethod = "GET")
    @GetMapping(value = "/export")
    public void download(HttpServletRequest request, HttpServletResponse response) {
        deviceService.export(request, response);
    }




   /* @ApiOperation(value = "importFile", notes = "导入")
    @PostMapping("/importFile")
    public ResponseData importFile(@RequestParam("file") MultipartFile file) {
        deviceService.importFile(file);
        return ResponseData.success(true);
    }*/


    @ApiOperation(value = "importFile", notes = "导入")
    @PostMapping("/importFile")
    public ResponseData importFile(@RequestParam("file") MultipartFile file) throws IOException {
        ImportParams params = new ImportParams();
        params.setNeedVerify(true);
        params.setTitleRows(0);
        params.setVerifyHandler(deviceVoVerifyHandler);
        Map<String, Object> map = ExcelUtils.importExcel(
                file.getInputStream(),
                DeviceVo.class,
                params,
                filePath,
                deviceService::saveByImport,
                fileUploadService::uploadSingle
        );
        return ResponseData.success(map);
    }



    @ApiOperation(value = "download", notes = "下载模板", httpMethod = "GET")
    @GetMapping(value = "/download")
    public String download(HttpServletResponse response) {
        InputStream in = null;
        ServletOutputStream out = null;
        try {
            in = (new ClassPathResource("templates/device.xls")).getInputStream();
            out = response.getOutputStream();
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment;filename=设备维护导入模板.xls");
            response.setContentLength(in.available());
            IOUtils.copy(in, out);
            out.flush();
        } catch (Exception var16) {
            var16.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException var15) {
                    ;
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException var14) {
                }
            }
        }


        return "";
    }

}
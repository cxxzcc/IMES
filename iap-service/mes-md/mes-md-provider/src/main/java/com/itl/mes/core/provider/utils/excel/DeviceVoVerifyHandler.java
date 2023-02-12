package com.itl.mes.core.provider.utils.excel;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.ProductLineHandleBO;
import com.itl.mes.core.api.bo.StationHandleBO;
import com.itl.mes.core.api.entity.Device;
import com.itl.mes.core.api.entity.DeviceType;
import com.itl.mes.core.api.entity.ProductLine;
import com.itl.mes.core.api.entity.Station;
import com.itl.mes.core.api.service.ProductLineService;
import com.itl.mes.core.api.service.StationService;
import com.itl.mes.core.api.vo.DeviceVo;
import com.itl.mes.core.provider.feign.SupplierFeignService;
import com.itl.mes.core.provider.service.impl.DeviceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.itl.mes.core.provider.mapper.DeviceTypeMapper;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 设备导入自定义校验
 * @author dengou
 * @date 2021/10/28
 */
@Component
public class DeviceVoVerifyHandler implements IExcelVerifyHandler<DeviceVo> {

    @Autowired
    private DeviceServiceImpl deviceService;
    @Autowired
    private SupplierFeignService supplierFeignService;
    @Autowired
    private ProductLineService productLineService;

    @Autowired
    private StationService stationServices;
    @Autowired
    private DeviceTypeMapper deviceTypeMapper;


    @Override
    public ExcelVerifyHandlerResult verifyHandler(DeviceVo deviceVo) {

        //设置默认验证为true
        ExcelVerifyHandlerResult excelVerifyHandlerResult = new ExcelVerifyHandlerResult(true);
        List<String> errorMsg = new ArrayList<>();

        if(StrUtil.isBlank(deviceVo.getDeviceName())) {
            errorMsg.add("设备名称不能为空");
        }
        if(StrUtil.isBlank(deviceVo.getDeviceModel())) {
            errorMsg.add("规格型号不能为空");
        }
        if(StrUtil.isBlank(deviceVo.getDeviceType())) {
            errorMsg.add("设备类型不能为空");
        }

        //投产日期校验
        if(StrUtil.isNotBlank(deviceVo.getValidStartDateStr())) {
            try {
                DateTime validStartDate = DateUtil.parse(deviceVo.getValidStartDateStr());
                deviceVo.setValidStartDate(validStartDate);
            } catch (Exception e) {
                errorMsg.add("投产日期格式错误");
            }
        }

        //购买日期校验
        if(StrUtil.isNotBlank(deviceVo.getBuyDateStr())) {
            try {
                DateTime buyDate = DateUtil.parse(deviceVo.getBuyDateStr());
                deviceVo.setBuyDate(buyDate);
            } catch (Exception e) {
                errorMsg.add("购买日期格式错误");
            }
        }


        //设备编码校验
        if(StrUtil.isBlank(deviceVo.getDevice())) {
            errorMsg.add("设备编码不能为空");
        }
        DeviceType deviceType = deviceTypeMapper.getByDeviceType(deviceVo.getDeviceType());
        if(deviceType == null){
            errorMsg.add("设备类型不存在");
        }


        Integer deviceCodeCount = deviceService.lambdaQuery().eq(Device::getDevice, deviceVo.getDevice()).count();
        if (deviceCodeCount > 0) {
            errorMsg.add("设备编码不能重复");
        }

        // 校验产线
        if(StrUtil.isNotBlank(deviceVo.getProductLine())) {

            Integer productLineCount = productLineService.lambdaQuery().eq(ProductLine::getBo, new ProductLineHandleBO(UserUtils.getSite(),deviceVo.getProductLine()).getBo()).count();
            if (productLineCount <= 0) {
                errorMsg.add("产线不存在");
            }
        }

        // 校验工位
        if(StrUtil.isNotBlank(deviceVo.getStation())) {
            Integer stationCount = stationServices.lambdaQuery().eq(Station::getBo, new StationHandleBO(UserUtils.getSite(),deviceVo.getStation()).getBo()).count();
            if (stationCount <= 0) {
                errorMsg.add("工位不存在");
            }
        }

        // 校验供应商
       if(StrUtil.isNotBlank(deviceVo.getSupplier())) {
           ResponseData<Map<String, Object>> result = supplierFeignService.getByCode(deviceVo.getSupplier());
           if (result.isSuccess()) {
               Map<String, Object> data = result.getData();
               String supplier1 = (String) data.getOrDefault("bo", "");
               if (supplier1 == null | supplier1 == "") {
                   errorMsg.add("供应商不存在");
               }
           }
       }

        //拼接错误提示
        if(CollUtil.isNotEmpty(errorMsg)) {
            excelVerifyHandlerResult.setSuccess(false);
            excelVerifyHandlerResult.setMsg(CollUtil.join(errorMsg, ";"));
        }
        return excelVerifyHandlerResult;
    }

}

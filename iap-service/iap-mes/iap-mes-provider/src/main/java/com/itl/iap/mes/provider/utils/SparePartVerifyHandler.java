package com.itl.iap.mes.provider.utils;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.Assert;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.mes.api.entity.sparepart.SparePart;
import com.itl.iap.mes.api.service.SparePartService;
import com.itl.iap.mes.provider.feign.SupplierFeignService;
import com.itl.iap.mes.provider.mapper.SparePartMapper;
import com.itl.mes.core.api.vo.DeviceTypeVo;
import com.itl.mes.core.api.vo.DeviceVo;
import com.itl.mes.core.client.service.DeviceService;
import com.itl.mes.core.client.service.DeviceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 设备导入自定义校验
 * @author dengou
 * @date 2021/10/28
 */
@Component
public class SparePartVerifyHandler implements IExcelVerifyHandler<SparePart> {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DeviceTypeService deviceTypeService;

    @Autowired
    private SupplierFeignService supplierFeignService;

    @Autowired
    private SparePartService sparePartService;


    @Override
    public ExcelVerifyHandlerResult verifyHandler(SparePart sparePart) {

        //设置默认验证为true
        ExcelVerifyHandlerResult excelVerifyHandlerResult = new ExcelVerifyHandlerResult(true);
        List<String> errorMsg = new ArrayList<>();
        if(StrUtil.isBlank(sparePart.getSparePartNo())) {
            errorMsg.add("备件编号不能为空");
        }else {

          boolean s = sparePartService.checkExistsBySparePartNo(sparePart, UserUtils.getSite());
          if(s == true){
              errorMsg.add("备件编号重复");
          }

        }
        if(StrUtil.isBlank(sparePart.getName())) {
            errorMsg.add("备件名称不能为空");
        }
        if(StrUtil.isBlank(sparePart.getDeviceCode())) {
            errorMsg.add("关联设备编码不能为空");
        }else {
            ResponseData<DeviceVo> deviceVo = deviceService.getDeviceVoByDevice(sparePart.getDeviceCode());
            if(deviceVo == null){
                errorMsg.add("关联设备编码不存在");
            }
        }

        if(StrUtil.isNotBlank(sparePart.getType())){
            ResponseData<DeviceTypeVo> deviceTypeVo = deviceTypeService.getDeviceTypeVoByDeviceType(sparePart.getType());
            if(deviceTypeVo == null){
                errorMsg.add("设备类型不存在");
            }
        }

        //购买日期校验
        if(StrUtil.isNotBlank(sparePart.getBuyDateStr())) {
            try {
                DateTime buyDate = DateUtil.parse(sparePart.getBuyDateStr());
                sparePart.setBuyDate(buyDate);
            } catch (Exception e) {
                errorMsg.add("购买日期格式错误");
            }
        }

        //校验供应商
       if(StrUtil.isNotBlank(sparePart.getSupplier())) {

           ResponseData<Map<String, Object>> result = supplierFeignService.getByCode(sparePart.getSupplier());
           if(result.isSuccess()) {
               Map<String, Object> data = result.getData();
               String supplier1 = (String)data.getOrDefault("bo", "");
               if(supplier1 == null | supplier1 == ""){
                   errorMsg.add("供应商不存在");
               }else {
                   sparePart.setSupplier(supplier1);
               }
           }
       }

        if(CollUtil.isNotEmpty(errorMsg)) {
            excelVerifyHandlerResult.setSuccess(false);
            excelVerifyHandlerResult.setMsg(CollUtil.join(errorMsg, ";"));
        }
        return excelVerifyHandlerResult;
    }


}

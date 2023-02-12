package com.itl.mes.core.provider.handle;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.dto.ItemForParamQueryDto;
import com.itl.mes.core.api.entity.Device;
import com.itl.mes.core.api.service.CustomDataValService;
import com.itl.mes.core.api.service.DeviceService;
import com.itl.mes.core.api.service.DeviceTypeItemService;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author yx
 * @date 2021/3/21
 * @since JDK1.8
 */
@Component
public class DeviceLabelRuleHandle implements LabelRuleHandle {

    @Autowired
    private CustomDataValService customDataValService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DeviceTypeItemService deviceTypeItemService;

    @Override
    public Map<String, Object> handle(ItemForParamQueryDto queryDto) {
        Map<String, Object> ret = new HashMap<>();
        List<String> columns = queryDto.getColumns();
        String elementBo = queryDto.getElementBo();
        //主字段
        AtomicReference<Map<String, Object>> map = new AtomicReference<>();

        Device device = deviceService.getById(elementBo);

        Optional.ofNullable(device)
                .ifPresent(d -> {
                    map.set(BeanUtil.beanToMap(d));
                    StringJoiner type = new StringJoiner(",");
                    StringJoiner typeName = new StringJoiner(",");
                    StringJoiner typeDesc = new StringJoiner(",");

                    deviceTypeItemService.getByDevice(d.getBo()).forEach(x -> {
                        type.add(x.getDeviceType());
                        typeName.add(x.getDeviceTypeName());
                        typeDesc.add(x.getDeviceTypeDesc());
                    });
                    map.get().put("type", type.toString());
                    map.get().put("typeName", typeName.toString());
                    map.get().put("typeDesc", typeDesc.toString());
                });

        Map<String, Object> finalMap = map.get();
        columns.forEach(column -> {
            Object o = finalMap.get(lineToHump(column.toLowerCase()));
            if (ObjectUtil.isNotNull(o)) {
                ret.put(column, o);
            }
        });
        return ret;
    }

    @Override
    public Map<String, Object> handleCustom(ItemForParamQueryDto queryDto) throws CommonException {
        String site = queryDto.getSite();
        Map<String, Object> ret = new HashMap<>();
        List<String> columns = queryDto.getColumns();
        String elementBo = queryDto.getElementBo();
        //自定义字段
        List<CustomDataAndValVo> customs = customDataValService.selectCustomDataAndValByBoAndDataType(site, elementBo, CustomDataTypeEnum.DEVICE.getDataType());
        customs.forEach(custom -> {
            if (columns.contains(custom.getAttribute())) {
                ret.put(custom.getAttribute(), custom.getVals());
            }
        });
        return ret;
    }
}

package com.itl.iap.mes.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.mes.api.common.CheckStateEnum;
import com.itl.iap.mes.api.common.UpkeepStateEnum;
import com.itl.iap.mes.api.dto.CheckPlanDto;
import com.itl.iap.mes.api.entity.CheckDevice;
import com.itl.iap.mes.api.entity.CheckExecute;
import com.itl.iap.mes.api.entity.UpkeepExecute;
import com.itl.iap.mes.provider.mapper.CheckDeviceMapper;
import com.itl.mes.core.api.entity.Device;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yx
 * @date 2021/8/26
 * @since 1.8
 */
@Service
public class CheckDeviceServiceImpl extends ServiceImpl<CheckDeviceMapper, CheckDevice> {

    public void saveCheckPlanDevices(CheckPlanDto checkPlanDto) {
        if (checkPlanDto.getDevices() != null && checkPlanDto.getDevices().size() > 0) {
            List<CheckDevice> list = new ArrayList<>();
            checkPlanDto.getDevices().stream().distinct().forEach(device->{
                CheckDevice checkDevice = new CheckDevice();
                checkDevice.setCheckId(checkPlanDto.getId());
                checkDevice.setDeviceId(device.getDeviceId());
                list.add(checkDevice);
            });
            this.saveBatch(list);
        }
    }

    public List<CheckDevice> getDeviceInfos(String checkId) {
        return this.list(Wrappers.<CheckDevice>lambdaQuery().eq(CheckDevice::getCheckId, checkId));
    }


}

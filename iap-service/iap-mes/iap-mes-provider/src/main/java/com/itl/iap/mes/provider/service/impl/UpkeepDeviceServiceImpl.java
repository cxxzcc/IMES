package com.itl.iap.mes.provider.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.mes.api.dto.UpkeepPlanDto;
import com.itl.iap.mes.api.entity.UpkeepDevice;
import com.itl.iap.mes.provider.mapper.UpkeepDeviceMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yx
 * @date 2021/8/26
 * @since 1.8
 */
@Service
public class UpkeepDeviceServiceImpl extends ServiceImpl<UpkeepDeviceMapper, UpkeepDevice> {

    public void saveCheckPlanDevices(UpkeepPlanDto upkeepPlanDto) {
        if (CollectionUtil.isNotEmpty(upkeepPlanDto.getDevices())) {
            List<UpkeepDevice> list = upkeepPlanDto.getDevices().stream().distinct()
                    .map(x -> {
                        UpkeepDevice upkeepDevice = new UpkeepDevice();
                        upkeepDevice.setUpkeepId(upkeepPlanDto.getId());
                        upkeepDevice.setDeviceId(x.getDeviceId());
                        return upkeepDevice;
                    }).collect(Collectors.toList());
            this.saveBatch(list);
        }
    }

    public List<UpkeepDevice> getDeviceInfos(String upkeepId) {
        return this.lambdaQuery().eq(UpkeepDevice::getUpkeepId, upkeepId).list();
    }
}

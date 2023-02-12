package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.mes.core.api.entity.DeviceTypeFault;
import com.itl.mes.core.api.service.DeviceTypeFaultService;
import com.itl.mes.core.provider.mapper.DeviceTypeFaultMapper;
import org.springframework.stereotype.Service;

/**
 * @author yx
 * @date 2021/8/24
 * @since 1.8
 */
@Service
public class DeviceTypeFaultServiceImpl extends ServiceImpl<DeviceTypeFaultMapper, DeviceTypeFault> implements DeviceTypeFaultService {
}

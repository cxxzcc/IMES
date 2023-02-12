package com.itl.mes.core.client.service.impl;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.client.service.ClassFrequencyService;
import com.itl.mes.pp.api.entity.scheduleplan.ClassFrequencyEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;


/**
 * @author chenjx1
 * @date 2021/11/15
 */
@Component
@Slf4j
public class ClassFrequencyServiceImpl implements ClassFrequencyService {

    @Override
    public ResponseData<ClassFrequencyEntity> getById(String id) {
        return null;
    }
}

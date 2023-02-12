package com.itl.mes.core.client.service;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.client.config.FallBackConfig;
import com.itl.mes.core.client.service.impl.ClassFrequencyServiceImpl;
import com.itl.mes.pp.api.entity.scheduleplan.ClassFrequencyEntity;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


/**
 * 班次
 *
 * @author chenjx1
 * @date 2021-11-15
 */
@FeignClient(value = "mes-pp-provider",contextId = "ClassFrequency",
        fallback = ClassFrequencyServiceImpl.class,
        configuration = FallBackConfig.class)
@Api(tags = "班次")
public interface ClassFrequencyService {

    @GetMapping("/p/classFrequency/getById/{id}")
    ResponseData<ClassFrequencyEntity> getById(@PathVariable String id);

}

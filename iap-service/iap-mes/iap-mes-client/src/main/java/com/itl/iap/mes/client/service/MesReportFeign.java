package com.itl.iap.mes.client.service;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.mes.api.dto.CheckExecuteAndPlanDTO;
import com.itl.iap.mes.api.dto.CheckExecuteAndPlanQueryDTO;
import com.itl.iap.mes.client.config.FallBackConfig;
import com.itl.iap.mes.client.service.impl.LabelServiceImpl;
import com.itl.iap.mes.client.service.impl.MesReportFeignImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author cjq
 * @Date 2021/12/2 2:58 下午
 * @Description TODO
 */
@FeignClient(value = "iap-mes-provider", contextId = "mesReportFeign", fallbackFactory = MesReportFeignImpl.class, configuration = FallBackConfig.class)
public interface MesReportFeign {

    @ApiOperation(value = "点检工单报表")
    @PostMapping(value = "/report/mes/getCheckExecuteList")
    ResponseData<List<CheckExecuteAndPlanDTO>> getCheckExecuteList(@RequestBody CheckExecuteAndPlanQueryDTO checkExecuteAndPlanQueryDTO);
}

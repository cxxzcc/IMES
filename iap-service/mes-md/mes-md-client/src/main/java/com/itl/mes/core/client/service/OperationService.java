package com.itl.mes.core.client.service;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.ProveDto;
import com.itl.mes.core.api.entity.Operation;
import com.itl.mes.core.client.config.FallBackConfig;
import com.itl.mes.core.client.service.impl.OperationServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 工序 服务 feign接口
 * @author dengou
 * @date 2021/12/14
 */
@FeignClient(value = "mes-md-provider", fallback = OperationServiceImpl.class, configuration = FallBackConfig.class)
public interface OperationService {

    @GetMapping("/operations/{id}")
    @ApiOperation(value="通过主键查询数据")
    ResponseData<Operation> getOperationById(@PathVariable String id);

    /**
     * feign接口  根据工序Id查询资质列表
     * */
    @GetMapping("/operations/prove/{bo}")
    @ApiOperation(value = "根据工序Id查询资质列表")
    ResponseData<List<ProveDto>> getProveDtoListByOperationBo(@PathVariable("bo") String operationBo);


}

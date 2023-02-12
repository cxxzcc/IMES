package com.itl.mes.core.client.service;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.CustomerDTO;
import com.itl.mes.core.client.config.FallBackConfig;
import com.itl.mes.core.client.service.impl.CallSapApiServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author chenjx1
 * @date 2021/10/20
 * @since JDK1.8
 */
@FeignClient(value = "mes-md-provider", contextId = "callSapApi",
        fallback = CallSapApiServiceImpl.class,
        configuration = FallBackConfig.class)
public interface CallSapApiService {

    /**
     * 客户信息交互接口
     *
     * @param customerDTO
     * @return
     */
    @PostMapping("/sap/callSapApi/customerSet")
    ResponseData customerSet(@RequestBody CustomerDTO customerDTO);

//    @ApiOperation("sap接口推送")
//    @PostMapping("/sap/call")
//    ResponseData call(@RequestBody List<String> ids);

}

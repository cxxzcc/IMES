package com.itl.mes.core.client.service.impl;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.CustomerDTO;
import com.itl.mes.core.client.service.CallSapApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author chenjx1
 * @date 2021/10/20
 * @since JDK1.8
 */
@Slf4j
@Component
public class CallSapApiServiceImpl implements CallSapApiService {

    public static final String PRODUCTION_STORAGE_ROUTING = "/customer/add";

    @Override
    public ResponseData customerSet(CustomerDTO customerDTO) {
        log.error("sorry CallSapApiService customerSet feign fallback customerDTO:{}" + customerDTO.toString());
        return ResponseData.error("调用customerSet失败");
    }

//    @Override
//    public ResponseData call(List<String> ids) {
//        log.error("sorry CallSapApiService call feign fallback ids:{}" + ids.toString());
//        return ResponseData.error("调用call失败");
//    }
}

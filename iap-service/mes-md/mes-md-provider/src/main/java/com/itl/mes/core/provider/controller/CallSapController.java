package com.itl.mes.core.provider.controller;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.CustomerDTO;
import com.itl.mes.core.api.service.sap.CallSapApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Lxz
 * @create 2021/11/25
 * @description
 **/
@Api(tags = "接口日志")
@RequestMapping("/sap")
@RestController
public class CallSapController {

    @Autowired
    private CallSapApiService callSapApiService;

    /**
     * 客户信息交互接口
     *
     * @param customerDTO
     * @return
     */
    @PostMapping("/callSapApi/customerSet")
    @ApiOperation("customerSet")
    public ResponseData customerSet(@RequestBody CustomerDTO customerDTO) {
        return callSapApiService.customerSet(customerDTO);
    }

//    @ApiOperation("sap接口推送")
//    @PostMapping("/call")
//    public ResponseData call(@RequestBody List<String> ids) {
//        return callSapApiService.call(ids);
//    }

}

package com.itl.mes.core.client.service.impl;


import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.RouterFitDto;
import com.itl.mes.core.api.entity.Router;
import com.itl.mes.core.client.service.RouterFitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * @author chenjx1
 * @date 2021/10/20
 */
@Slf4j
@Component
public class RouterFitServiceImpl implements RouterFitService {

    @Override
    public ResponseData<RouterFitDto> getRouterAndBom(String orderType, String itemBo, String productBo) {
        return null;
    }
}

package com.itl.mes.core.client.service.impl;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.ProveDto;
import com.itl.mes.core.api.entity.Operation;
import com.itl.mes.core.client.service.OperationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author dengou
 * @date 2021/12/14
 */
@Slf4j
@Component
public class OperationServiceImpl implements OperationService {

    @Override
    public ResponseData<Operation> getOperationById(String id) {
        log.error("sorry OperationService getOperationById feign fallback id:{}" + id);
        return ResponseData.error("OperationService服务getOperationById调用失败");
    }

    @Override
    public ResponseData<List<ProveDto>> getProveDtoListByOperationBo(String operationBo) {
        log.error("sorry OperationService getProveDtoListByOperationBo feign fallback operationBo:{}" + operationBo);
        return ResponseData.error("OperationService服务getProveDtoListByOperationBo调用失败");
    }
}

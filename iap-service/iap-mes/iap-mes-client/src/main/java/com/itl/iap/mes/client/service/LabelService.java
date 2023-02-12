package com.itl.iap.mes.client.service;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.mes.client.config.FallBackConfig;
import com.itl.iap.mes.client.service.impl.LabelServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author liuchenghao
 * @date 2021/01/24
 * @since JDK1.8
 */
@FeignClient(value = "iap-mes-provider",contextId = "labelUseless", fallback = LabelServiceImpl.class, configuration = FallBackConfig.class)
public interface LabelService {


    /**
     * 批量生成PDF
     * @param list
     * @param labelId
     * @return
     */
    @ApiOperation(value = "batchCreatePdf", notes = "批量生成PDF", httpMethod = "Post")
    @PostMapping(value = "/sys/label/batchCreatePdf")
    ResponseData batchCreatePdf(@RequestBody List<Map<String, Object>> list, @RequestParam("labelId") String labelId);
}

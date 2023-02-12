package com.itl.andon.core.client.service;

import com.itl.andon.core.client.config.FallBackConfig;
import com.itl.andon.core.client.service.fallback.AndonServiceFallBack;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.andon.api.dto.AndonSaveRepairCallBackDTO;
import com.itl.mes.andon.api.entity.Record;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "mes-andon-provider", configuration = FallBackConfig.class, fallback = AndonServiceFallBack.class)
public interface AndonService {


    @GetMapping("/andon/findByLine")
    @ApiOperation(value = "根据产线查询所有的异常")
    List<Record> findByLine(@RequestParam("line") String line);



    @PostMapping("/trigger/saveRepair/callback")
    @ApiOperation(value = "保存维修工单回调")
    ResponseData<Boolean> saveRepairCallBack(@RequestBody AndonSaveRepairCallBackDTO andonSaveRepairCallBackDTO);

}

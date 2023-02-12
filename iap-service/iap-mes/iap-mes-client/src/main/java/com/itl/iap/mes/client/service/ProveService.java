package com.itl.iap.mes.client.service;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.mes.api.entity.prove.ProveEntity;
import com.itl.iap.mes.client.config.FallBackConfig;
import com.itl.iap.mes.client.service.impl.ProveServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author dengou
 * @date 2021/12/13
 */
@FeignClient(value = "iap-mes-provider",contextId = "proveFeignService", fallback = ProveServiceImpl.class, configuration = FallBackConfig.class)
public interface ProveService {


    /**
     * 根据id列表查询
     * */
    @PostMapping("/sys/prove/getByIds")
    ResponseData<List<ProveEntity>> getByIds(@RequestBody List<String> ids);


    @GetMapping("/sys/prove/userId/{userId}")
    ResponseData<List<ProveEntity>> getByUserId(@PathVariable("userId") String userId, @RequestParam("site") String site);
}

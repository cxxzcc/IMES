package com.itl.mes.core.client.service;

import com.itl.mes.core.api.vo.BomVo;
import com.itl.mes.core.client.config.FallBackConfig;
import com.itl.mes.core.client.service.impl.BomServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author yx
 * @date 2021/4/7
 * @since JDK1.8
 */
@FeignClient(value = "mes-md-provider",
        contextId = "BomService",
        fallback = BomServiceImpl.class,
        configuration = FallBackConfig.class
)
public interface BomService {
    @GetMapping("/bom/queryByBo")
    @ApiOperation(value = "查询物料清单ByBo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bo", value = "bo", dataType = "string", paramType = "query")
    })
    BomVo selectByBo(@RequestParam("bo") String bo);
}

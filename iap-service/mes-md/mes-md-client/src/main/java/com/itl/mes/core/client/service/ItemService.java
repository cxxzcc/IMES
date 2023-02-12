package com.itl.mes.core.client.service;

import cn.hutool.core.util.StrUtil;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.ItemQueryDto;
import com.itl.mes.core.api.dto.MboMitemDTO;
import com.itl.mes.core.api.entity.Item;
import com.itl.mes.core.api.vo.ItemAndCustomDataValVo;
import com.itl.mes.core.api.vo.ItemFullVo;
import com.itl.mes.core.client.config.FallBackConfig;
import com.itl.mes.core.client.service.impl.ItemFeignServiceImpl;
import com.itl.mes.core.client.service.impl.ItemServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author chenjx1
 * @date 2021/10/20
 * @since JDK1.8
 */
@FeignClient(value = "mes-md-provider", contextId = "item",
        fallback = ItemServiceImpl.class,
        configuration = FallBackConfig.class)
public interface ItemService {

    /**
     * 根据item查询
     *
     * @param item 物料
     * @return
     */
    @GetMapping("/items/query")
    @ApiOperation(value = "根据物料和版本查询物料数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "item", value = "物料", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "version", value = "版本", dataType = "string", paramType = "query")
    })
    ResponseData<ItemFullVo> getItemByItemAndVersion(@RequestParam String item, @RequestParam String version);
}

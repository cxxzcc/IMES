package com.itl.mes.me.client.service;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.me.api.vo.CheckBarCodeVo;
import com.itl.mes.me.client.config.FallBackConfig;
import com.itl.mes.me.client.service.impl.LabelPrintServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhongfei
 * @date 2021/4/15
 * @since JDK1.8
 */
@FeignClient(value = "mes-me-provider",contextId = "labelPrint", fallback = LabelPrintServiceImpl.class, configuration = FallBackConfig.class)
public interface LabelPrintService {

    /**
     * 检查条码是否合法，返回对应的BO和物料BO，和数量
     * @param barCode
     * @param elementType
     * @return
     */
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "barCode",value = "条码", paramType = "query"),
            @ApiImplicitParam(name = "elementType",value = "条码类型：单体条码：ITEM，包装条码：PACKING", paramType = "query"),
    })
    @GetMapping("/app/labelPrintRange/checkBarCode")
    @ApiOperation(value = "检查条码是否合法，返回对应的BO和物料BO，和数量")
    ResponseData<CheckBarCodeVo> checkBarCode(@RequestParam("barCode") String barCode, @RequestParam("elementType") String elementType);

}

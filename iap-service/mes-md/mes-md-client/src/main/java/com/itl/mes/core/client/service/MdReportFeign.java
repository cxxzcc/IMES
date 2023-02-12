package com.itl.mes.core.client.service;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.ProductCheckoutDTO;
import com.itl.mes.core.api.dto.ProductCheckoutDetailDTO;
import com.itl.mes.core.api.dto.TProjectActualQueryDTO;
import com.itl.mes.core.api.vo.ProductCheckoutDetailVO;
import com.itl.mes.core.api.vo.ProductCheckoutVO;
import com.itl.mes.core.api.vo.TProjectActualVO;
import com.itl.mes.core.client.config.FallBackConfig;
import com.itl.mes.core.client.service.impl.MdReportFeignImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author cjq
 * @Date 2021/12/2 2:58 下午
 * @Description TODO
 */
@FeignClient(value = "mes-md-provider", contextId = "mdReportFeign", fallbackFactory = MdReportFeignImpl.class, configuration = FallBackConfig.class)
public interface MdReportFeign {

    @ApiOperation(value = "产品检验统计报表")
    @PostMapping(value = "/report/md/getProductCheckoutList")
    ResponseData<List<ProductCheckoutVO>> getProductCheckoutList(@RequestBody ProductCheckoutDTO productCheckoutDTO);

    @ApiOperation(value = "产品检验详情报表")
    @PostMapping(value = "/report/md/getProductCheckoutDetailList")
    ResponseData<List<ProductCheckoutDetailVO>> getProductCheckoutDetailList(@RequestBody ProductCheckoutDetailDTO productCheckoutDetailDTO);

    @ApiOperation(value = "仪器交叉数据")
    @PostMapping(value = "/device/actual/all/List")
    ResponseData<List<TProjectActualVO>> getDeviceActual(@RequestBody TProjectActualQueryDTO projectActualQueryDTO);
}

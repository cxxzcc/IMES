package com.itl.mes.core.client.service;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.ProductStateUpdateDto;
import com.itl.mes.core.api.dto.UpdateDoneDto;
import com.itl.mes.core.api.entity.MeProductStatus;
import com.itl.mes.core.api.entity.SnItem;
import com.itl.mes.core.api.vo.MeProductStatusVo;
import com.itl.mes.core.client.config.FallBackConfig;
import com.itl.mes.me.api.entity.LabelPrint;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author GKL
 * @date 2021/11/10 - 15:33
 * @since 2021/11/10 - 15:33 星期三 by GKL
 */
@FeignClient(value = "mom-label-provider", contextId = "MomLabel",

        configuration = FallBackConfig.class)
@Api(tags = "物料标签管理")
public interface MomLabelService {


    @GetMapping("/labelPrintRange/getLabelPrintBo")
    ResponseData<LabelPrint> getLabelPrintBo(@RequestParam("labelPrintBo") String labelPrintBo);

    @GetMapping("/momSnItem/querySnItemByItemBoAndSnBo")
    ResponseData<SnItem> querySnItemByItemBoAndSnBo(@RequestParam("itemBo") String itemBo, @RequestParam("snBo") String snBo);

    @GetMapping("/meProductStatus/findProductStatus")
    ResponseData<List<MeProductStatusVo>> findProductStatusBySnAndStatus(@RequestParam("sn") String sn, @RequestParam("status") int status);

    @GetMapping("/meProductStatus/{id}")
    ResponseData<MeProductStatus> selectProductStatusOne(@PathVariable Serializable id);

    @PostMapping("/meProductStatus/productStateUpdate")
    ResponseData<Boolean> productStateUpdate(List<ProductStateUpdateDto> productStateUpdateDto);

    @PostMapping("/meProductStatus/updateProductStatusDoneByBo")
    ResponseData updateProductStatusDoneByBo(List<UpdateDoneDto> updateDoneDtos);

    @GetMapping("/labelPrintRange/queryCodeByItem")
    ResponseData<List<String>> queryCodeByItem(@RequestParam("itemCode") List<String > itemCode);

    @GetMapping("/labelPrintRange/queryItemBySn")
    ResponseData<String> queryItemBySn(@RequestParam("sn") String sn);

    @GetMapping("/momSnItem/updateSysl")
    ResponseData<Boolean> updateSysl(@RequestParam("id")String id, @RequestParam("sysl") BigDecimal sysl);
    @PostMapping("/momSnItem/updateSyslTpZero")
    ResponseData<Boolean> updateSyslByBoList(List<String> boList);
}

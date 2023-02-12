package com.itl.mom.label.client.service;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mom.label.api.dto.label.LabelTransferRequestDTO;
import com.itl.mom.label.api.entity.label.Sn;
import com.itl.mom.label.client.config.FallBackConfig;
import com.itl.mom.label.client.service.impl.SnServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author cch
 * @date 2021/4/27$
 * @since JDK1.8
 */
@FeignClient(value = "mom-label-provider", contextId = "momLabelSn", fallback = SnServiceImpl.class, configuration = FallBackConfig.class)
public interface SnService {


    @PostMapping("/monopy/sns/update/{type}")
    @ApiOperation(value = "变更条码状态type-UP/DOWN")
    ResponseData save(@RequestBody List<String> sns, @PathVariable("type") String type);

    @PostMapping("/monopy/sns/check/{sn}/{type}")
    @ApiOperation(value = "校验条码type-UP/DOWN")
    ResponseData check(@PathVariable("sn") String sn, @PathVariable("type") String type);

    @GetMapping("/monopy/sns/getItemInfoAndSnStateBySn/{sn}")
    @ApiOperation(value = "getItemInfoAndSnStateBySn for feign")
    Map<String, String> getItemInfoAndSnStateBySn(@PathVariable("sn") String sn);

    @GetMapping("/monopy/sns/getSnInfo/{sn}")
    @ApiOperation(value = "getSnInfo for feign")
    Sn getSnInfo(@PathVariable("sn") String sn);

    @PostMapping("/monopy/sns/collarSn/{sn}/{workShopBo}/{productLineBo}")
    @ApiOperation(value = "领用Sn")
    void collarSn(@PathVariable("sn") String sn,
                         @PathVariable("workShopBo") String workShopBo,
                         @PathVariable("productLineBo") String productLineBo);

    @PostMapping("/monopy/sns/changeSnStateByMap")
    @ApiOperation(value = "根据map<sn,state>信息改变对应条码状态")
    Boolean changeSnStateByMap(Map<String, String> map);

    @PostMapping("/monopy/sns/updateOrderBo")
    @ApiOperation(value = "工单拆单-变更条码工单")
    ResponseData updateOrderBo(@RequestParam List<String> snBoList, @RequestParam String newOrderBo);

    @PostMapping("/monopy/sns/queryOrderBoList")
    @ApiOperation(value = "拆单使用：查询拆当前工单产品SN，未上线状态的条码BO")
    ResponseData<List<String>> queryOrderBoList(@RequestParam String orderBo, @RequestParam int onLine);

    /**
     * 条码转移
     *
     * @param labelTransferRequestDTO 条码转移参数
     */
    @PostMapping("/monopy/sns/transfer")
    @ApiOperation(value = "条码转移")
    ResponseData<Boolean> transferLabels(@RequestBody @Valid LabelTransferRequestDTO labelTransferRequestDTO);

    /**
     * 工单拆单时，条码转移
     *
     * @param labelTransferRequestDTO 工单拆单时，条码转移参数
     */
    @PostMapping("/monopy/sns/transferAsOrder")
    @ApiOperation(value = "工单拆单时，条码转移")
    ResponseData<Boolean> transferLabelsAsOrder(@RequestBody @Valid LabelTransferRequestDTO labelTransferRequestDTO);

}

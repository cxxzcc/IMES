package com.itl.mom.label.client.service;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.ProductStateUpdateDto;
import com.itl.mom.label.api.dto.label.UpdateNextProcessDTO;
import com.itl.mom.label.api.dto.label.UpdateSnHoldDTO;
import com.itl.mom.label.api.entity.MeProductStatus;
import com.itl.mom.label.client.config.FallBackConfig;
import com.itl.mom.label.client.service.impl.MeProductStatusServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author dengou
 * @date 2021/11/12
 */
@FeignClient(value = "mom-label-provider", contextId = "meProductStatus", fallback = MeProductStatusServiceImpl.class, configuration = FallBackConfig.class)
public interface MeProductStatusService {

    /**
     * 根据sn查询
     * */
    @GetMapping("/meProductStatus/getBySn")
    ResponseData<MeProductStatus> getBySn(@RequestParam("sn") String sn, @RequestParam("site")String site);
    /**
     * 根据snbo列表查询
     * */
    @PostMapping("/meProductStatus/getBySnBoList")
    ResponseData<List<MeProductStatus>> getBySnBoList(@RequestBody List<String> snBoList);

    /**
     * 根据snbo列表查询，sn和工单
     * */
    @PostMapping("/meProductStatus/getShopOrderBySnBoList")
    ResponseData<List<MeProductStatus>> getShopOrderBySnBoList(@RequestBody List<String> snBoList);


    /**
     * 插入产品状态记录
     * */
    @PostMapping("/meProductStatus")
    ResponseData insert(@RequestBody MeProductStatus meProductStatus);


    /**
     * 更新产品状态-下工序
     * */
    @PostMapping("/meProductStatus/updateNextProcess")
    ResponseData<Boolean> updateNextProcess(ProductStateUpdateDto productStateUpdateDto);

    /**
     * 更新下一工序
     * */
    @PostMapping("/meProductStatus/updateNextProcessBatch")
    ResponseData<Boolean> updateNextProcessBatch(@RequestBody UpdateNextProcessDTO param);

    /**
     * 根据ids更新是否挂起
     * @param ids 产品状态id集合
     * @param isHold 是否挂起
     * @return 是否成功
     * */
    @PostMapping("/meProductStatus/updateIsHoldByIds")
    ResponseData<Boolean> updateIsHold(@RequestBody UpdateSnHoldDTO updateSnHoldDTO);

}

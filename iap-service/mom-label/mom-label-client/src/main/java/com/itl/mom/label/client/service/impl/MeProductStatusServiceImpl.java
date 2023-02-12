package com.itl.mom.label.client.service.impl;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.ProductStateUpdateDto;
import com.itl.mom.label.api.dto.label.UpdateNextProcessDTO;
import com.itl.mom.label.api.dto.label.UpdateSnHoldDTO;
import com.itl.mom.label.api.entity.MeProductStatus;
import com.itl.mom.label.client.service.MeProductStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author dengou
 * @date 2021/11/12
 */
@Slf4j
@Component
public class MeProductStatusServiceImpl implements MeProductStatusService {
    @Override
    public ResponseData<MeProductStatus> getBySn(String sn, String site) {
        log.error("sorry MeProductStatusService getBySn feign fallback sn:{} site:{}", sn,site);
        return ResponseData.error("调用根据sn查询失败");
    }

    @Override
    public ResponseData<List<MeProductStatus>> getBySnBoList(List<String> snBoList) {
        log.error("sorry MeProductStatusService getBySnBoList feign fallback snBoList:{}", snBoList);
        return ResponseData.error("调用根据snBo列表查询失败");
    }

    @Override
    public ResponseData insert(MeProductStatus meProductStatus) {
        log.error("sorry MeProductStatusService insert feign fallback meProductStatus:{}", meProductStatus.toString());
        return ResponseData.error("调用根据sn插入失败");
    }

    @Override
    public ResponseData<Boolean> updateNextProcess(ProductStateUpdateDto productStateUpdateDto) {
        log.error("sorry productStateUpdate updateNextProcess feign fallback productStateUpdateDto:{}", productStateUpdateDto.toString());
        return ResponseData.error("updateNextProcess调用失败");
    }

    @Override
    public ResponseData<Boolean> updateNextProcessBatch(UpdateNextProcessDTO param) {
        log.error("sorry productStateUpdate updateNextProcessBatch feign fallback param:{}", param);
        return ResponseData.error("updateNextProcessBatch调用失败");
    }

    @Override
    public ResponseData<Boolean> updateIsHold(UpdateSnHoldDTO updateSnHoldDTO) {
        log.error("sorry productStateUpdate insert feign fallback updateIsHoldByIds， updateSnHoldDTO:{}", updateSnHoldDTO);
        return ResponseData.error("updateIsHoldByIds调用失败");
    }

    @Override
    public ResponseData<List<MeProductStatus>> getShopOrderBySnBoList(List<String> snBoList) {
        log.error("sorry MeProductStatusService getShopOrderBySnBoList feign fallback snBoList:{}", snBoList);
        return ResponseData.error("调用根据snBo列表查询工单失败");
    }
}

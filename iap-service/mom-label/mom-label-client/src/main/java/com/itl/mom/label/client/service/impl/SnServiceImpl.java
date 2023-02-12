package com.itl.mom.label.client.service.impl;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mom.label.api.dto.label.LabelTransferRequestDTO;
import com.itl.mom.label.api.entity.label.Sn;
import com.itl.mom.label.client.service.SnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author cch
 * @date 2021/4/27$
 * @since JDK1.8
 */
@Slf4j
@Component
public class SnServiceImpl implements SnService {
    @Override
    public ResponseData save(List<String> sns, String type) {
        log.error("sorry SnService save feign fallback station:{}" + sns + type);
        return null;
    }

    @Override
    public ResponseData check(String sn, String type) {
        log.error("sorry SnService check feign fallback station:{}" + sn + type);
        return null;
    }

    @Override
    public Map<String, String> getItemInfoAndSnStateBySn(String sn) {
        log.error("sorry SnService getItemInfoAndSnStateBySn feign fallback sn:{}", sn);
        return null;
    }

    @Override
    public Sn getSnInfo(String sn) {
        log.error("sorry SnService getSnInfo feign fallback sn:{}", sn);
        return null;
    }

    @Override
    public void collarSn(String sn, String workShopBo, String productLineBo) {
        log.error("sorry SnService collarSn feign fallback sn:{}, workShopBo:{}, productLineBo:{}", sn, workShopBo, productLineBo);
    }

    @Override
    public Boolean changeSnStateByMap(Map<String, String> map) {
        log.error("sorry SnService changeSnStateByMap feign fallback map: {}", map);
        return null;
    }

    @Override
    public ResponseData updateOrderBo(List<String> snBoList, String newOrderBo) {
        return null;
    }

    @Override
    public ResponseData<List<String>> queryOrderBoList(String orderBo, int onLine) {
        return ResponseData.error("调用queryOrderBoList失败！");
    }

    @Override
    public ResponseData<Boolean> transferLabels(LabelTransferRequestDTO labelTransferRequestDTO) {
        return null;
    }

    @Override
    public ResponseData<Boolean> transferLabelsAsOrder(LabelTransferRequestDTO labelTransferRequestDTO) {
        return ResponseData.error("调用transferLabelsAsOrder失败！");
    }
}

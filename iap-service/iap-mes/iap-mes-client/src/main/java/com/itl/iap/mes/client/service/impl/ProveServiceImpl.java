package com.itl.iap.mes.client.service.impl;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.mes.api.entity.prove.ProveEntity;
import com.itl.iap.mes.client.service.ProveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author dengou
 * @date 2021/12/13
 */
@Slf4j
@Component
public class ProveServiceImpl implements ProveService {

    @Override
    public ResponseData<List<ProveEntity>> getByIds(List<String> ids) {
        log.error("sorry ProveService getByIds feign fallback ids:{} ", ids);
        return ResponseData.error("getByIds调用失败");
    }

    @Override
    public ResponseData<List<ProveEntity>> getByUserId(String userId, String site) {
        log.error("sorry ProveService getByUserId feign fallback userId:{}，site:{} ", userId, site);
        return ResponseData.error("getByUserId调用失败");
    }
}

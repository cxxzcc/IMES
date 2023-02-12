package com.itl.andon.core.client.service.fallback;

import com.itl.andon.core.client.service.AndonService;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.andon.api.dto.AndonSaveRepairCallBackDTO;
import com.itl.mes.andon.api.entity.Record;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

/**
 * @author dengou
 * @date 2021/11/19
 */
@Slf4j
public class AndonServiceFallBack implements AndonService {

    @Override
    public List<Record> findByLine(String line) {
        log.error("sorry AndonService findByLine feign fallback line:{}", line);
        return Collections.emptyList();
    }

    @Override
    public ResponseData<Boolean> saveRepairCallBack(AndonSaveRepairCallBackDTO andonSaveRepairCallBackDTO) {
        log.error("sorry AndonService saveRepairCallBack feign fallback andonSaveRepairCallBackDTO:{}", andonSaveRepairCallBackDTO.toString());
        return ResponseData.error("saveRepairCallBack feign调用失败");
    }
}

package com.itl.mes.core.client.service.impl;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.ProductStateUpdateDto;
import com.itl.mes.core.api.dto.UpdateDoneDto;
import com.itl.mes.core.api.entity.MeProductStatus;
import com.itl.mes.core.api.entity.SnItem;
import com.itl.mes.core.api.vo.MeProductStatusVo;
import com.itl.mes.core.client.service.MomLabelService;
import com.itl.mes.me.api.entity.LabelPrint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author GKL
 * @date 2021/11/10 - 15:34
 * @since 2021/11/10 - 15:34 星期三 by GKL
 */
@Service
@Slf4j
public class MomLabelServiceImpl implements MomLabelService {
    @Override
    public ResponseData<LabelPrint> getLabelPrintBo(String labelPrintBo) {
        return ResponseData.error("查询失败");
    }

    @Override
    public ResponseData<SnItem> querySnItemByItemBoAndSnBo(String itemBo, String snBo) {
        log.error("sorry  querySnItemByItemBoAndSnBo feign fallback, itemBo:{} ,snBo:{}", itemBo, snBo);
        return ResponseData.error("查询失败");
    }

    @Override
    public ResponseData<List<MeProductStatusVo>> findProductStatusBySnAndStatus(String sn, int status) {
        return ResponseData.error("查询失败");
    }


    @Override
    public ResponseData<MeProductStatus> selectProductStatusOne(Serializable id) {
        return ResponseData.error("查询失败");
    }

    @Override
    public ResponseData<Boolean> productStateUpdate(List<ProductStateUpdateDto> productStateUpdateDto) {
        return ResponseData.error("修改失败");
    }

    @Override
    public ResponseData updateProductStatusDoneByBo(List<UpdateDoneDto> updateDoneDtos) {
        return ResponseData.error("修改失败");
    }

    @Override
    public ResponseData<List<String>> queryCodeByItem(List<String> itemCode) {
        return ResponseData.error("查询失败");
    }



    @Override
    public ResponseData<String> queryItemBySn(String sn) {
        return ResponseData.error("查询失败");
    }

    @Override
    public ResponseData<Boolean> updateSysl(String id, BigDecimal sysl) {
       return ResponseData.error("修改剩余数量失败");
    }

    @Override
    public ResponseData<Boolean> updateSyslByBoList(List<String> boList) {
        return ResponseData.error("修改剩余数量为0失败");
    }


}

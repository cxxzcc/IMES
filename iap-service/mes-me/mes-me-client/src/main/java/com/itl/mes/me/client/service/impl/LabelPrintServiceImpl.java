package com.itl.mes.me.client.service.impl;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.me.api.vo.CheckBarCodeVo;
import com.itl.mes.me.client.service.LabelPrintService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author zhongfei
 * @date 2021/04/15
 * @since JDK1.8
 */
@Component
@Slf4j
public class LabelPrintServiceImpl implements LabelPrintService {
    /**
     * 检查条码是否合法，返回对应的BO和物料BO，和数量
     *
     * @param barCode
     * @param elementType
     * @return
     */
    @Override
    public ResponseData<CheckBarCodeVo> checkBarCode(String barCode, String elementType) {
        log.error("sorry LabelPrintService checkBarCode feign fallback barCode:{} elementType:{}",barCode,elementType);
        return ResponseData.error("查询失败");
    }
}

package com.itl.iap.mes.client.service.impl;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.mes.client.service.LabelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author yaoxiang
 * @date 2020/12/14
 * @since JDK1.8
 */
@Slf4j
@Component
public class LabelServiceImpl implements LabelService {


    /**
     * 批量生成PDF
     *
     * @param list
     * @param labelId
     * @return
     */
    @Override
    public ResponseData batchCreatePdf(List<Map<String, Object>> list, String labelId) {
        log.error("sorry LabelService batchCreatePdf feign fallback list:{} labelId:{}", list,labelId);
        return ResponseData.error("调用打印服务失败");
    }
}

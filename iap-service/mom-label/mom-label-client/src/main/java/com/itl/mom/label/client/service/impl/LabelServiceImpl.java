package com.itl.mom.label.client.service.impl;


import com.itl.iap.common.base.response.ResponseData;
import com.itl.mom.label.api.dto.label.LabelPrintSaveDto;
import com.itl.mom.label.api.entity.label.LabelTypeEntity;
import com.itl.mom.label.api.entity.label.Sn;
import com.itl.mom.label.api.vo.CheckBarCodeVo;
import com.itl.mom.label.api.vo.ScanReturnVo;
import com.itl.mom.label.client.service.LabelService;
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

    @Override
    public ResponseData<List<Sn>> addLabelPrint(LabelPrintSaveDto labelPrintSaveDto) {
        log.error("sorry LabelPrintService addLabelPrint feign fallback ");
        return null;
    }

    @Override
    public ResponseData<ScanReturnVo> scanReturn(String barCode, String elementType) {
        log.error("sorry LabelPrintService scanReturn feign fallback barCode:{} elementType:{}",barCode,elementType);
        return null;
    }

    @Override
    public ResponseData updateLabelPrintDetail(Sn sn) {
        log.error("sorry LabelPrintService updateLabelPrintDetail feign fallback ");
        return null;
    }

    @Override
    public ResponseData getSn(String bo) {
        log.error("sorry LabelPrintService getSn feign fallback ");
        return null;
    }

    @Override
    public ResponseData<List<LabelTypeEntity>> getLabelTypeByIdList(List<String> idList) {
        log.error("sorry LabelPrintService getLabelTypeByIdList feign fallback ");
        return null;
    }
}

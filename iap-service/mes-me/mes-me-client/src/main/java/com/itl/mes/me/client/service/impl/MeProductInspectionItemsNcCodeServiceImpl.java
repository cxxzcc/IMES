package com.itl.mes.me.client.service.impl;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.me.api.entity.MeProductInspectionItemsNcCode;
import com.itl.mes.me.api.vo.MeProductInspectionItemsNcCodeVo;
import com.itl.mes.me.client.service.MeProductInspectionItemsNcCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author dengou
 * @since JDK1.8
 */
@Component
@Slf4j
public class MeProductInspectionItemsNcCodeServiceImpl implements MeProductInspectionItemsNcCodeService {

    @Override
    public ResponseData<List<MeProductInspectionItemsNcCode>> listItemNcCodes(MeProductInspectionItemsNcCode meProductInspectionItemsNcCode) {
        log.error("查询产品检验项目不良代码集合失败");
        return ResponseData.error("查询产品检验项目不良代码集合失败");
    }

    @Override
    public ResponseData<List<MeProductInspectionItemsNcCodeVo>> listItemNcCodesTwo(MeProductInspectionItemsNcCodeVo meProductInspectionItemsNcCodeVo) {
        log.error("查询产品检验项目不良代码集合Two失败");
        return ResponseData.error("查询产品检验项目不良代码集合Two失败");
    }

    @Override
    public ResponseData saveList(List<MeProductInspectionItemsNcCode> meProductInspectionItemsNcCodeList) throws CommonException {
        log.error("批量保存产品检验项目不良代码失败");
        return ResponseData.error("批量保存产品检验项目不良代码失败");
    }
}

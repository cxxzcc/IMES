package com.itl.mes.me.client.service;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.me.api.entity.MeProductInspectionItemsNcCode;
import com.itl.mes.me.api.vo.MeProductInspectionItemsNcCodeVo;
import com.itl.mes.me.client.config.FallBackConfig;
import com.itl.mes.me.client.service.impl.MeProductInspectionItemsNcCodeServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author chenjx1
 * @date 2021/12/09
 */
@FeignClient(value = "mes-me-provider",contextId = "meProductInspectionItemsNcCode", fallback = MeProductInspectionItemsNcCodeServiceImpl.class, configuration = FallBackConfig.class)
public interface MeProductInspectionItemsNcCodeService {

    /**
     * 列表-产品检验项不良代码
     */
    @PostMapping("/productNcCodes/listItemNcCodes")
    ResponseData<List<MeProductInspectionItemsNcCode>> listItemNcCodes(@RequestBody MeProductInspectionItemsNcCode meProductInspectionItemsNcCode);

    /**
     * 列表-产品检验项不良代码Two
     */
    @PostMapping("/productNcCodes/listItemNcCodesTwo")
    ResponseData<List<MeProductInspectionItemsNcCodeVo>> listItemNcCodesTwo(@RequestBody MeProductInspectionItemsNcCodeVo meProductInspectionItemsNcCodeVo);

    /**
     * 保存集合
     */
    @PostMapping("/productNcCodes/saveList")
    ResponseData saveList(@RequestBody List<MeProductInspectionItemsNcCode> meProductInspectionItemsNcCodeList) throws CommonException;

}

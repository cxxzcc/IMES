package com.itl.mes.core.api.service;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.ProductFinishUpdateDto;
import com.itl.mes.core.api.dto.ProductStatusUpdateDto;
import com.itl.mes.core.api.vo.ProductStatusUpdateVo;

/**
 * 产品状态更新
 *
 * @author GKL
 * @date 2021/11/15 - 17:16
 * @since 2021/11/15 - 17:16 星期一 by GKL
 */
public interface ProductUpdateService  {
    /**
     * 产品状态更新
     * @param dto dto
     * @return ResponseData.class
     */
    ResponseData<ProductStatusUpdateVo> productStatusUpdate(ProductStatusUpdateDto dto);

    /**
     * 产品完工更新
     * @param dto dto
     * @return ResponseData.class
     */
    ResponseData<Boolean> productFinishUpdate(ProductFinishUpdateDto dto);
}

package com.itl.mes.core.client.service.impl;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.ProductCheckoutDTO;
import com.itl.mes.core.api.dto.ProductCheckoutDetailDTO;
import com.itl.mes.core.api.dto.TProjectActualQueryDTO;
import com.itl.mes.core.api.vo.BomVo;
import com.itl.mes.core.api.vo.ProductCheckoutDetailVO;
import com.itl.mes.core.api.vo.ProductCheckoutVO;
import com.itl.mes.core.api.vo.TProjectActualVO;
import com.itl.mes.core.client.service.BomService;
import com.itl.mes.core.client.service.MdReportFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yx
 * @date 2021/4/7
 * @since JDK1.8
 */
@Slf4j
@Service
public class MdReportFeignImpl implements MdReportFeign {

    @Override
    public ResponseData<List<ProductCheckoutVO>> getProductCheckoutList(ProductCheckoutDTO productCheckoutDTO) {
        return null;
    }

    @Override
    public ResponseData<List<ProductCheckoutDetailVO>> getProductCheckoutDetailList(ProductCheckoutDetailDTO productCheckoutDetailDTO) {
        return null;
    }

    @Override
    public ResponseData<List<TProjectActualVO>> getDeviceActual(TProjectActualQueryDTO projectActualQueryDTO) {
        return null;
    }
}

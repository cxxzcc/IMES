package com.itl.mes.core.provider.controller;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.ProductFinishUpdateDto;
import com.itl.mes.core.api.dto.ProductStatusUpdateDto;
import com.itl.mes.core.api.service.ProductUpdateService;
import com.itl.mes.core.api.vo.ProductStatusUpdateVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author GKL
 * @date 2021/11/15 - 17:10
 * @since 2021/11/15 - 17:10 星期一 by GKL
 */
@RestController
@RequestMapping("/productUpdate")
@Api(tags = "产品更新")
@RequiredArgsConstructor
@Slf4j
public class ProductUpdateController {

    private final ProductUpdateService productUpdateService;

    /**
     * @param dto 条码,工位
     * @return ResponseData.class
     */
    @PostMapping("productStatusUpdate")
    @ApiOperation(value = "产品状态更新")
    public ResponseData<ProductStatusUpdateVo> productStatusUpdate(@RequestBody ProductStatusUpdateDto dto) {
        try {
            return productUpdateService.productStatusUpdate(dto);
        } catch (Exception e) {
            log.info("exception is ->{}", e.getMessage());
            return ResponseData.error(e.getMessage());
        }
    }


    /**
     * @param dto 条码,工位
     * @return ResponseData.class
     */
    @PostMapping("productFinishUpdate")
    @ApiOperation(value = "产品完工更新")
    public ResponseData<Boolean> productFinishUpdate(@RequestBody ProductFinishUpdateDto dto) {
        try {
            return productUpdateService.productFinishUpdate(dto);
        } catch (Exception e) {
            log.info("exception is ->{}", e.getMessage());
            return ResponseData.error(e.getMessage());
        }
    }

}

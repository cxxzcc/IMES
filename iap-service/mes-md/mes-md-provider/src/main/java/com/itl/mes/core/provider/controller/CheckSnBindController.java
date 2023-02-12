package com.itl.mes.core.provider.controller;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.CheckItemCodeDto;
import com.itl.mes.core.api.dto.CheckSnBindDto;
import com.itl.mes.core.api.service.CheckSnBindService;
import com.itl.mes.core.api.vo.CheckSnBindVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验证单体条码绑定controller
 *
 * @author GKL
 * @date 2021/11/4 - 14:20
 * @since 2021/11/4 - 14:20 星期四 by GKL
 */
@RestController
@RequestMapping("/checkSnBind")
@Api(tags = "验证单体条码绑定")
@RequiredArgsConstructor
@Slf4j
public class CheckSnBindController {

    private final CheckSnBindService checkSnBindService;

    /**
     * @param dto 条码,工位
     * @return ResponseData.class
     */
    @PostMapping("checkSn")
    @ApiOperation(value = "扫描产品条码校验条码")
    public ResponseData<CheckSnBindVo> checkSnBind(@RequestBody CheckSnBindDto dto) {
        try {
            return checkSnBindService.checkSn(dto);
        } catch (Exception e) {
            log.info("exception is ->{}", e.getMessage());
            return ResponseData.error(e.getMessage());
        }
    }

    @PostMapping("checkItemCode")
    @ApiOperation(value = "校验物料编码")
    public ResponseData<Boolean> checkItemCode(@RequestBody CheckItemCodeDto dto) {
        try {
            return checkSnBindService.checkItemCode(dto);
        } catch (Exception e) {
            log.info("exception is ->{}", e.getMessage());
            return ResponseData.error(e.getMessage());
        }
    }

    @PostMapping("queryItemBomBySn")
    @ApiOperation(value = "通过sn获取物料bom")
    public ResponseData<String> queryItemBomBySn(@RequestBody CheckSnBindDto dto){
        try{
            return checkSnBindService.queryItemBomBySn(dto);
        }catch (Exception e){
            log.info(e.getMessage());
            return ResponseData.error(e.getMessage());
        }
    }

}

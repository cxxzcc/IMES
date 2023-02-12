package com.itl.mes.core.provider.controller;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.BomItemSnByStation;
import com.itl.mes.core.api.dto.CheckBatchSnDto;
import com.itl.mes.core.api.dto.CheckBomItemAndStationSn;
import com.itl.mes.core.api.dto.CheckBomItemSn;
import com.itl.mes.core.api.service.CheckBatchSnService;
import com.itl.mes.core.api.vo.CheckBatchSnVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 校验批次产品
 *
 * @author GKL
 * @date 2021/11/26 - 16:47
 * @since 2021/11/26 - 16:47 星期五 by GKL
 */
@RestController
@RequestMapping("/checkBatchSnBind")
@Api(tags = "验证批次条码绑定")
@RequiredArgsConstructor
@Slf4j
public class CheckBatchSnController {
    private final CheckBatchSnService checkBatchSnService;


    /**
     * @param checkBatchSnDto 条码,工位 ,工厂
     * @return ResponseData.class
     */
    @PostMapping("checkBatchSn")
    @ApiOperation(value = "扫描产品条码校验批次条码")
    public ResponseData<CheckBatchSnVo> checkBatchSn(@RequestBody CheckBatchSnDto checkBatchSnDto) {
        try {
            return checkBatchSnService.checkBatchSn(checkBatchSnDto);
        } catch (Exception e) {
            return ResponseData.error(e.getMessage());
        }
    }


    @PostMapping("checkSnBomAndStation")
    @ApiOperation(value = "产品bom对应当前工位上料数量校验")
    public ResponseData<Boolean> checkSnBomAndStation(@RequestBody CheckBomItemAndStationSn checkBomItemAndStationSn) {
        try {
            return checkBatchSnService.checkSnBomAndStation(checkBomItemAndStationSn);

        } catch (Exception e) {
            return ResponseData.error(e.getMessage());
        }
    }

    @PostMapping("checkBomItemSn")
    @ApiOperation(value = "上料下料")
    public ResponseData<Boolean> checkBomItemSn(@RequestBody CheckBomItemSn checkBomItemSn) {
        try {
            return checkBatchSnService.checkBomItemSn(checkBomItemSn);

        } catch (Exception e) {
            return ResponseData.error(e.getMessage());
        }
    }

    @GetMapping("getUsedListByStation/{station}")
    @ApiOperation(value = "获取工位对应的数据")
    public ResponseData<BomItemSnByStation> getUsedListByStation(@PathVariable("station") String station){
        try{
           return checkBatchSnService.getUsedListByStation(station);
        }catch (Exception e){
            return ResponseData.error(e.getMessage());
        }
    }
 /*   @PostMapping("checkBatchSnItemCode")
    @ApiOperation(value = "校验物料编码")
    public ResponseData<Boolean> checkBatchSnItemCode(@RequestBody CheckBatchSnItemCodeDto dto) {
        try {
            return checkBatchSnService.checkBatchSnItemCode(dto);
        } catch (Exception e) {
            log.info("exception is ->{}", e.getMessage());
            return ResponseData.error(e.getMessage());
        }
    }*/

}

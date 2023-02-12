package com.itl.mes.me.provider.controller;

import com.itl.mes.me.api.dto.ShopOrderPackDTO;
import com.itl.mes.me.api.vo.ShopOrderPackRealTreeVO;
import com.itl.mes.me.api.vo.ShopOrderPackTemTreeVO;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.me.api.entity.PackTemp;
import com.itl.mes.me.api.service.PackTempService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author yx
 * @date 2021/6/16
 * @since 1.8
 */
@RestController("/mePack")
@Api(tags = "生产执行-包装")
@Validated
public class PackController {
    private PackTempService packTempService;

    @Autowired
    public void setPackTempService(PackTempService packTempService) {
        this.packTempService = packTempService;
    }


    @PostMapping("/getPackReal")
    @ApiOperation(value = "获取包装列表")
    public ResponseData<List<ShopOrderPackRealTreeVO>> getPackReal(@RequestBody ShopOrderPackDTO shopOrderPackDTO ) throws CommonException {
        List<ShopOrderPackRealTreeVO> o = packTempService.getPackReal(shopOrderPackDTO);
        return ResponseData.success(o);
    }

    @GetMapping("/getPackTempSn")
    @ApiOperation(value = "获取工位下的扫描记录")
    public ResponseData<List<ShopOrderPackTemTreeVO>> getPackTempSn() throws CommonException {
        List<ShopOrderPackTemTreeVO> o = packTempService.getPackTempSn();
        return ResponseData.success(o);
    }

    @GetMapping("/scanPackSn")
    @ApiOperation(value = "扫描包装")
    public ResponseData scanSnPack(@RequestParam("sn") @NotBlank(message = "条码不能为空") String sn) throws CommonException {
        packTempService.scanSnPack(sn);
        return ResponseData.success();
    }

    @PostMapping("/saveScanPackSn")
    @ApiOperation(value = "包装保存")
    public ResponseData saveScanPackSn(@RequestBody List<String> shopOrderList) throws CommonException {
        packTempService.saveScanPackSn(shopOrderList);
        return ResponseData.success();
    }


    @GetMapping("/scanSn")
    @ApiOperation(value = "scanSn")
    public ResponseData scanSn(@RequestParam("sn") String sn, @RequestParam("operationBo") String operationBo) throws CommonException {
        return ResponseData.success(packTempService.scanSn(sn, operationBo));
    }

    @PostMapping("/executePack")
    @ApiOperation(value = "executePack")
    public ResponseData executePack(@RequestBody List<PackTemp> list) throws CommonException {
        return ResponseData.success(packTempService.executePack(list));
    }
}

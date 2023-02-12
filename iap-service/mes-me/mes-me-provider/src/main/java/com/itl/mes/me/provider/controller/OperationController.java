package com.itl.mes.me.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.vo.ShopOrderBomComponnetVo;
import com.itl.mes.me.api.dto.OperationQueryDto;
import com.itl.mes.me.api.entity.Operation;
import com.itl.mes.me.api.service.OperationService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @author yx
 * @date 2021/5/31
 * @since 1.8
 */
@RestController
@RequestMapping("/operation")
@Api(tags = "me工位操作项")
public class OperationController {

    private OperationService operationService;

    @Autowired
    public void setOperationService(OperationService operationService) {
        this.operationService = operationService;
    }

    @PostMapping("/page")
    @ApiOperation(value = "分页查询")
    public ResponseData<IPage<Operation>> page(@RequestBody OperationQueryDto queryDto) throws CommonException {
        return ResponseData.success(operationService.queryPage(queryDto));
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存")
    public ResponseData<String> save(@RequestBody Operation operation) throws CommonException {
        Objects.requireNonNull(operation, "参数不能为空!");
        operationService.saveAndUpdate(operation);
        return ResponseData.success("success");
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "删除")
    public ResponseData<String> delete(@RequestBody List<String> bos) {
        operationService.delete(bos);
        return ResponseData.success("success");
    }

    @GetMapping("/checkSnAndOperation")
    @ApiOperation(value = "依据工单SN-校验工序")
    public ResponseData checkSnAndOperation(@RequestParam(value = "sn") String sn,
                                            @RequestParam(value = "operationBo") String operationBo) throws CommonException {
        return ResponseData.success(operationService.checkSnAndOperation(sn, operationBo));
    }

    @GetMapping("/getSingleAssyList")
    @ApiOperation(value = "单体条码绑定-获取装配清单")
    public ResponseData getSingleAssyList(@RequestParam(value = "productSn") String productSn)
            throws CommonException {
        return ResponseData.success(operationService.getAssyList(productSn, "L"));
    }

    @GetMapping("/getBatchAssyList")
    @ApiOperation(value = "批次条码绑定-获取装配清单&上料清单")
    public ResponseData getBatchAssyList(@RequestParam(value = "productSn") String productSn, @RequestParam(value = "operationBo") String operationBo)
            throws CommonException {
        return ResponseData.success(operationService.getBatchAssyList(productSn, operationBo));
    }

    @GetMapping("/bindSingleSn")
    @ApiOperation(value = "单体条码绑定")
    public ResponseData bindSingleSn(@RequestParam(value = "productSn") String productSn,
                                     @RequestParam(value = "childSn") String childSn)
            throws CommonException {
        return ResponseData.success(operationService.bindSingleSn(productSn, childSn));
    }

    @PostMapping("/saveAssyTempInfo/{productSn}")
    @ApiOperation(value = "单体条码绑定-保存临时装配信息")
    public ResponseData saveAssyTempInfo(@PathVariable("productSn") String productSn, @RequestBody List<ShopOrderBomComponnetVo> list) {
        operationService.saveAssyTempInfo(productSn, list);
        return ResponseData.success();
    }

    @GetMapping("/bindBatchSn")
    @ApiOperation(value = "批次条码绑定")
    public ResponseData bindBatchSn(@RequestParam(value = "productSn") String productSn,
                                    @RequestParam(value = "childSn") String childSn,
                                    @RequestParam(value = "operationBo") String operationBo)
            throws CommonException {
        return ResponseData.success(operationService.bindBatchSn(productSn, childSn, operationBo));
    }

    @PostMapping("/unload")
    @ApiOperation(value = "批次条码绑定-下料")
    public ResponseData unload(@RequestBody List<String> ids) {
        return ResponseData.success(operationService.unload(ids));
    }
}

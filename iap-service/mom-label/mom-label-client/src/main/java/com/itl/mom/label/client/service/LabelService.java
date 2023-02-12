package com.itl.mom.label.client.service;


import com.itl.iap.common.base.response.ResponseData;
import com.itl.mom.label.api.dto.label.LabelPrintSaveDto;
import com.itl.mom.label.api.entity.label.LabelTypeEntity;
import com.itl.mom.label.api.entity.label.Sn;
import com.itl.mom.label.api.vo.CheckBarCodeVo;
import com.itl.mom.label.api.vo.ScanReturnVo;
import com.itl.mom.label.client.config.FallBackConfig;
import com.itl.mom.label.client.service.impl.LabelServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author liuchenghao
 * @date 2021/01/24
 * @since JDK1.8
 */
@FeignClient(value = "mom-label-provider", contextId = "label",
        fallback = LabelServiceImpl.class,
        configuration = FallBackConfig.class)
public interface LabelService {


    /**
     * 批量生成PDF
     *
     * @param list
     * @param labelId
     * @return
     */
    @ApiOperation(value = "batchCreatePdf", notes = "批量生成PDF", httpMethod = "Post")
    @PostMapping(value = "/sys/label/batchCreatePdf")
    ResponseData batchCreatePdf(@RequestBody List<Map<String, Object>> list, @RequestParam("labelId") String labelId);

    /**
     * 检查条码是否合法，返回对应的BO和物料信息和数量
     *
     * @param barCode
     * @param elementType
     * @return
     */
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "barCode", value = "条码", paramType = "query"),
            @ApiImplicitParam(name = "elementType", value = "条码类型：单体条码：ITEM，包装条码：PACKING", paramType = "query"),
    })
    @GetMapping("/app/labelPrintRange/checkBarCode")
    @ApiOperation(value = "检查条码是否合法，返回对应的BO和物料信息和数量")
    ResponseData<CheckBarCodeVo> checkBarCode(@RequestParam("barCode") String barCode, @RequestParam("elementType") String elementType);

    @PostMapping("/labelPrintRange/addLabelPrint")
    @ApiOperation(value = "添加标签打印任务")
    ResponseData<List<Sn>> addLabelPrint(@RequestBody LabelPrintSaveDto labelPrintSaveDto);


    @GetMapping("/labelPrintRange/scanReturn")
    @ApiOperation(value = "扫描条码带出信息，返回对应的BO和物料BO，和数量")
    ResponseData<ScanReturnVo> scanReturn(@RequestParam("barCode") String barCode, @RequestParam("elementType") String elementType);

    @PostMapping("/labelPrintRange/updateLabelPrintDetail")
    @ApiOperation(value = "更新标签数量")
    ResponseData updateLabelPrintDetail(@RequestBody Sn sn);

    @GetMapping("/labelPrintRange/getSn")
    @ApiOperation(value = "通过snBo查询sn条码")
    ResponseData getSn(@RequestParam("bo") String bo);


    @PostMapping("/sys/labelType/getByIdList")
    @ApiOperation(value = "通过snBo查询sn条码")
    ResponseData<List<LabelTypeEntity>> getLabelTypeByIdList(@RequestBody List<String> idList);
}

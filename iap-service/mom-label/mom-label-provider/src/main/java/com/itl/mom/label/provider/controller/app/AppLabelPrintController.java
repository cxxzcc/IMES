package com.itl.mom.label.provider.controller.app;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mom.label.api.service.label.SnService;
import com.itl.mom.label.api.vo.CheckBarCodeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhongfei
 * @date 2021/1/26 15:34
 */
@Api(tags = "APP标签打印控制层")
@RestController
@RequestMapping("/app/labelPrintRange")
public class AppLabelPrintController {

    @Autowired
    private SnService snService;


    /**
     * 检查条码是否合法，返回对应的BO和物料信息和数量
     * @param barCode
     * @param elementType
     * @return
     */
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "barCode",value = "条码", paramType = "query"),
            @ApiImplicitParam(name = "elementType",value = "条码类型：单体条码：ITEM，包装条码：PACKING", paramType = "query"),
    })
    @GetMapping("/checkBarCode")
    @ApiOperation(value = "检查条码是否合法，返回对应的BO和物料信息和数量")
    public ResponseData<CheckBarCodeVo> checkBarCode(@RequestParam("barCode") String barCode, @RequestParam("elementType") String elementType){
        return snService.checkBarCode(barCode,elementType);
    }
}

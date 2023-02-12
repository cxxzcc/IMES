package com.itl.mes.core.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.RouterFitDto;
import com.itl.mes.core.api.entity.RouterFit;
import com.itl.mes.core.api.service.RouterFitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;



/**
 *
 * @author xtz
 * @date 2021-05-25
 */
@RestController
@Api(tags = "工艺路线设置")
@RequestMapping("/routerFit")
public class RouterFitController {

    @Resource
    private RouterFitService routerFitService;


    @PostMapping("/queryByItem")
    @ApiOperation(value = "查询工艺路线物料设置")
    public ResponseData<IPage<RouterFitDto>> queryByItem(@RequestBody RouterFitDto routerFitDto){

        return ResponseData.success(routerFitService.queryByItem(routerFitDto));
    }

    @PostMapping("/queryByItemGroup")
    @ApiOperation(value = "查询工艺路线物料组设置")
    public ResponseData<IPage<RouterFitDto>> queryByItemGroup(@RequestBody RouterFitDto routerFitDto){

        return ResponseData.success(routerFitService.queryByItemGroup(routerFitDto));
    }

    @PostMapping("/queryByProductLine")
    @ApiOperation(value = "查询工艺路线产线设置")
    public ResponseData<IPage<RouterFitDto>> queryByProductLine(@RequestBody RouterFitDto routerFitDto){

        return ResponseData.success(routerFitService.queryByProductLine(routerFitDto));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新建")
    public ResponseData add(@RequestBody RouterFit routerFit) throws CommonException {

        return ResponseData.success(routerFitService.add(routerFit));
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改")
    public ResponseData update(@RequestBody RouterFit routerFit) throws CommonException {

        return ResponseData.success(routerFitService.update(routerFit));
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除")
    public ResponseData delete(@RequestBody RouterFit routerFits){
        routerFitService.delete(routerFits);
        return ResponseData.success();
    }

    @PostMapping("/getRouterAndBom")
    @ApiOperation(value = "根据物料、物料组和产线带出工艺路线和物料清单")
    public ResponseData<RouterFitDto> getRouterAndBom(@RequestParam(value = "orderType", required = false) String orderType,
                                        @RequestParam(value = "itemBo",required = false) String itemBo,
                                        @RequestParam(value = "productBo",required = false) String productBo){

        return ResponseData.success( routerFitService.getRouterAndBom(orderType,itemBo,productBo));
    }

}

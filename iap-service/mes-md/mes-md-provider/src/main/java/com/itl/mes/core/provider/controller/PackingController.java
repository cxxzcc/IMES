package com.itl.mes.core.provider.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.mes.core.api.dto.PackingDto;
import com.itl.mes.core.api.entity.PackLevel;
import com.itl.mes.core.api.service.PackLevelService;
import com.itl.mes.core.api.service.PackingService;
import com.itl.mes.core.api.vo.PackingVo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 *
 * @author space
 * @since 2019-07-12
 */
@RestController
@RequestMapping("/packings")
@Api(tags = " 包装定义表" )
public class PackingController {

    private final Logger logger = LoggerFactory.getLogger(PackingController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public PackingService packingService;

    @Autowired
    private PackLevelService packLevelService;



    @PostMapping("/save")
    @ApiOperation(value="保存包装信息")
    public ResponseData<PackingVo> save(@RequestBody PackingVo packingVo) throws CommonException {
        packingService.save(packingVo);
        packingVo = packingService.getPackingVoByPackName(packingVo.getPackName());
        return ResponseData.success(packingVo);
    }

    @PostMapping("/savePacking")
    @ApiOperation(value="保存包装信息基础")
    public ResponseData<PackingVo> savePacking(@RequestBody PackingVo packingVo) throws CommonException {
        packingService.savePacking(packingVo);
        packingVo = packingService.getPackVoByPackName(packingVo.getPackName());
        return ResponseData.success(packingVo);
    }

    @PostMapping("/addPacking")
    @ApiOperation(value="新增包装信息基础")
    public ResponseData<PackingVo> addPacking(@RequestBody PackingVo packingVo) throws CommonException {
        packingService.addPacking(packingVo);
        packingVo = packingService.getPackVoByPackName(packingVo.getPackName());
        return ResponseData.success(packingVo);
    }


    @GetMapping("/query")
    @ApiOperation(value = "查询包装信息")
    public ResponseData<PackingVo> getPackingVo(String packName) throws CommonException {
        PackingVo packingVo= packingService.getPackingVoByPackName(packName);
        return ResponseData.success(packingVo);
    }

    @PostMapping("/queryLov")
    @ApiOperation(value = "查询包装信息Lov")
    public ResponseData<IPage<PackingDto>> getPackingVo(@RequestBody PackingDto packingDto) throws CommonException {
        return ResponseData.success(packingService.findLabelPrintPackingLov(packingDto));
    }



    @GetMapping("/delete")
    @ApiOperation(value = "删除包装信息")
    public ResponseData<String> delete(String packName,String modifyDate) throws CommonException {
        packingService.delete(packName, DateUtil.parse(modifyDate));
        return ResponseData.success("success");
    }


    @PostMapping("/findLabelPrintPackingList")
    @ApiOperation(value = "模糊查询包装信息弹出框")
    @ApiOperationSupport(params = @DynamicParameters(name = "CommonPackingRequestModel", properties = {
            @DynamicParameter(name = "page", value = "页面，默认为1"),
            @DynamicParameter(name = "limit", value = "分页大小，默认20，可不填"),
            @DynamicParameter(name = "orderByField", value = "排序属性，可不填"),
            @DynamicParameter(name = "isAsc", value = "排序方式，true/false，可不填"),
            @DynamicParameter(name = "packName", value = "包装，可不填"),
            @DynamicParameter(name = "packDesc", value = "包装描述，可不填"),
            @DynamicParameter(name = "state", value = "状态，可不填"),
            @DynamicParameter(name = "packGrade", value = "状态，可不填"),
            @DynamicParameter(name = "packName", value = "返回字段：包装名称，不需要传入"),
            @DynamicParameter(name = "packDesc", value = "返回字段：描述，不需要传入"),
            @DynamicParameter(name = "state", value = "返回字段：状态，不需要传入"),
            @DynamicParameter(name = "packGrade", value = "返回字段：包装等级，不需要传入"),
            @DynamicParameter(name = "maxQty", value = "返回字段：最大数量，不需要传入"),
            @DynamicParameter(name = "minQty", value = "返回字段：最小数量，不需要传入"),
            @DynamicParameter(name = "packLevelBo", value = "返回字段：包装级别BO，不需要传入"),
            @DynamicParameter(name = "item", value = "返回字段：物料编码，不需要传入"),
            @DynamicParameter(name = "shopOrder", value = "返回字段：工单，不需要传入")
    }))
    public ResponseData<IPage<Map<String, Object>>> findLabelPrintPackingList(@RequestBody Map<String, Object> params) {

        return ResponseData.success(packingService.findLabelPrintPackingList(new QueryPage<>(params), params));

    }

    @GetMapping("/packLevel")
    @ApiOperation(value = "查询包装明细BO获取包装对应明细记录")
    public ResponseData<PackLevel> getPackLevelByBo(@RequestParam("bo") String bo){
        PackLevel packLevel = packLevelService.getById(bo);
        return ResponseData.success(packLevel);
    }
}
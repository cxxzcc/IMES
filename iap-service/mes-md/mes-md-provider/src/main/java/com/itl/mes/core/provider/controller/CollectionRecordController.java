package com.itl.mes.core.provider.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.util.StringUtils;
import com.itl.mes.core.api.dto.CollectionRecordCommonTempDTO;
import com.itl.mes.core.api.dto.QualitativeInspectionSaveDTO;
import com.itl.mes.core.api.dto.RepairTempDataDTO;
import com.itl.mes.core.api.dto.UpdateNextOperationDto;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.*;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  采集记录- 前端控制器
 * </p>
 *
 * @author houfan
 * @since 2021-11-02
 */
@RestController
@RequestMapping("/collectionRecord")
@Api(tags = "采集记录" )
public class CollectionRecordController {


    @Autowired
    private ICollectionRecordService collectionRecordService;
    @Autowired
    private IProductionDefectRecordService productionDefectRecordService;
    @Autowired
    private IRecordOfProductTestService recordOfProductTestService;
    @Autowired
    private ProductMaintenanceRecordService productMaintenanceRecordService;
    @Autowired
    private ProductMaintenanceRecordKeyService productMaintenanceRecordKeyService;
    @Autowired
    private ProductionCollectionRecordService productionCollectionRecordService;
    @Autowired
    private ProductionCollectionRecordKeyService productionCollectionRecordKeyService;

    /**
     * 分页查询采集记录
     * @param params 分页查询参数
     * @return 分页列表
     * */
    @GetMapping("/page")
    @ApiOperation(value = "采集记录分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam( name="page", value = "页面，默认为1" ),
            @ApiImplicitParam( name="limit", value = "分页大小，默认20，可不填" ),
            @ApiImplicitParam( name="barCode", value = "条码, 支持模糊搜索" ),
            @ApiImplicitParam( name="workOrderNumber", value = "工单编号, 支持模糊搜索" ),
            @ApiImplicitParam( name="hold", value = "是否Hold 1=是 0=否" ),
            @ApiImplicitParam( name="workshop", value = "工单类型，取字典 ORDER_TYPE_STATE" ),
            @ApiImplicitParam( name="productionLine", value = "产线 编码" ),
            @ApiImplicitParam( name="productCode", value = "产品编码" ),
            @ApiImplicitParam( name="productName", value = "产品名称， 支持模糊搜索" ),
            @ApiImplicitParam( name="currentProcess", value = "当前工序编码" ),
            @ApiImplicitParam( name="nextProcess", value = "下一工序编码" ),
            @ApiImplicitParam( name="complete", value = "是否已完工 1=是 0=否" )
    })
    public ResponseData<Page<CollectionRecord>> getPage(@RequestParam Map<String, Object> params) {
        StringUtils.replaceMatchTextByMap(params);
        params.put("site", UserUtils.getSite());
        return ResponseData.success(collectionRecordService.getPage(params));
    }

    /**
     * 生产采集记录
     * @param id 采集记录id
     * @return 采集记录列表
     * */
    @GetMapping("/productionCollectionRecord/{id}")
    @ApiOperation(value = "生产采集记录")
    public ResponseData<List<ProductionCollectionRecord>> getProductionCollectionRecordList(
            @ApiParam(name = "id", value = "采集记录id", required = true)
            @PathVariable("id") String id) {
        return ResponseData.success(productionCollectionRecordService.getListByCollectionRecordId(id));
    }


    /**
     * 产品检验记录
     * @param id 采集记录id
     * @return 产品检验记录列表
     * */
    @GetMapping("/productTestRecord/{id}")
    @ApiOperation(value = "产品检验记录")
    public ResponseData<List<RecordOfProductTest>> getProductTestRecordList(
            @ApiParam(name = "id", value = "采集记录id", required = true)
            @PathVariable("id") String id) {
        return ResponseData.success(recordOfProductTestService.getListByCollectionRecordId(id));
    }


    /**
     * 产品维修记录
     * @param id 采集记录id
     * @return 产品维修记录
     * */
    @GetMapping("/productMaintenanceRecord/{id}")
    @ApiOperation(value = "产品维修记录")
    public ResponseData<List<ProductMaintenanceRecord>> getProductMaintenanceRecordList(
            @ApiParam(name = "id", value = "采集记录id", required = true)
            @PathVariable("id") String id) {
        return ResponseData.success(productMaintenanceRecordService.getListByCollectionRecordId(id));
    }


    /**
     * 产品缺陷记录
     * @param id 采集记录id
     * @return 缺陷记录列表
     * */
    @GetMapping("/productionDefectRecord/{id}")
    @ApiOperation(value = "产品缺陷记录")
    public ResponseData<List<ProductionDefectRecord>> getProductionDefectRecordList(
            @ApiParam(name = "id", value = "采集记录id", required = true)
            @PathVariable("id") String id) {
        return ResponseData.success(productionDefectRecordService.getListByCollectionRecordId(id));
    }


    /**
     * 关键件列表
     * @param id 采集记录id
     * @return 关键件列表
     * */
    @GetMapping("/productionCollectionRecordKey/{id}")
    @ApiOperation(value = "生产采集记录-关键件列表")
    public ResponseData<List<ProductionCollectionRecordKey>> getProductionCollectionRecordKeyList(
            @ApiParam(name = "id", value = "生产采集记录id", required = true)
            @PathVariable("id") String id) {
        return ResponseData.success(productionCollectionRecordKeyService.getListByCollectionRecordId(id));
    }


    /**
     * 根据维修记录id查询维修措施列表
     * @param id 维修记录id
     * @return 维修措施列表
     * */
    @GetMapping("/maintenanceMethod/{id}")
    @ApiOperation(value = "产品维修记录-维修措施列表")
    public ResponseData<List<ProductMaintenanceMethodRecord>> getMaintenanceMethodListByProductionMaintenanceRecordId(
            @ApiParam(name = "id", value = "产品维修记录id", required = true)
            @PathVariable("id") String id) {
        return ResponseData.success(productMaintenanceRecordKeyService.getListByCollectionRecordId(id));
    }

    /**
     * 保存采集记录-定性，定量检验
     * @param qualitativeInspectionSaveDTO 采集记录
     * @param shopOrder 工单号
     * @param site 工厂id
     * */
    @PostMapping("/saveTempQualitativeInspection")
    public ResponseData<String> saveTempQualitativeInspection(@RequestBody List<QualitativeInspectionSaveDTO> qualitativeInspectionSaveDTO, @RequestParam("shopOrder") String shopOrder, @RequestParam("site") String site) {
        return ResponseData.success(collectionRecordService.saveTempQualitativeInspection(qualitativeInspectionSaveDTO, shopOrder, site));
    }
    /**
     * 保存采集记录-维修
     * @param repairTempDataDTOS 维修记录
     * */
    @PostMapping("/saveProductMaintenanceRecord")
    public ResponseData<Boolean> saveProductMaintenanceRecord(@RequestBody List<RepairTempDataDTO> repairTempDataDTOS) {
        return ResponseData.success(collectionRecordService.saveProductMaintenanceRecord(repairTempDataDTOS));
    }
    /**
     * 保存采集记录-通用方法
     * @param collectionRecordCommonTempDTOS 通用采集记录
     * */
    @PostMapping("/saveByCommons")
    public ResponseData<Boolean> saveByCommon(@RequestBody List<CollectionRecordCommonTempDTO> collectionRecordCommonTempDTOS) {
        return ResponseData.success(collectionRecordService.saveByCommons(collectionRecordCommonTempDTOS));
    }

    /**
     * 根据sn查询列表
     * @param sn sn
     * @return 缺陷记录列表
     * */
    @PostMapping("/defectRecord/getListBySn")
    public ResponseData<List<ProductionDefectRecord>> getListBySn(@RequestParam("sn") String sn) {
        return ResponseData.success(productionDefectRecordService.getListBySn(sn));
    }


    /**
     * 根据id列表查询不合格代码列表
     * @param ids id列表
     * @return 缺陷记录列表
     * */
    @PostMapping("/defectRecord/getListByIds")
    public ResponseData<List<ProductionDefectRecord>> getDefectRecordListByIds(@RequestBody List<String> ids) {
        return ResponseData.success(productionDefectRecordService.getListByIds(ids));
    }

    /**
     * 更新下工序
     * @param snBoListStr 条码bo
     * @param operationBo 工序bo
     * @param operationName 工序名称
     * @return 是否成功
     * */
    @PostMapping("/updateNextOperation")
    public ResponseData<Boolean> updateNextOperation(@RequestBody UpdateNextOperationDto body) {
        return ResponseData.success(collectionRecordService.updateNextOperation(body.getSnBoList(), body.getOperationBo(), body.getOperationName()));
    }

}

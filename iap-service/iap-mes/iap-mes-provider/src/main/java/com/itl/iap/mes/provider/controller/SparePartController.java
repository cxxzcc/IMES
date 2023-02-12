package com.itl.iap.mes.provider.controller;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.ExcelUtils;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.mes.api.dto.sparepart.*;
import com.itl.iap.mes.api.entity.sparepart.SparePart;
import com.itl.iap.mes.api.entity.sparepart.SparePartStorageRecord;
import com.itl.iap.mes.api.entity.sparepart.SparePartStorageRecordDetail;
import com.itl.iap.mes.api.service.SparePartDeviceMappingService;
import com.itl.iap.mes.api.service.SparePartService;
import com.itl.iap.mes.api.service.SparePartStorageRecordService;
import com.itl.iap.mes.provider.service.impl.FileUploadServiceImpl;
import com.itl.iap.mes.provider.utils.SparePartVerifyHandler;
import com.itl.mes.core.api.entity.ProductLine;
import io.swagger.annotations.*;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 备件controller
 * @author dengou
 * @date 2021/9/17
 */
@RestController
@RequestMapping("/m/sparePart")
@Api(tags = " 备件" )
public class SparePartController {

    @Autowired
    private SparePartService sparePartService;
    @Autowired
    private SparePartStorageRecordService sparePartStorageRecordService;
    @Autowired
    private SparePartDeviceMappingService sparePartDeviceMappingService;
    @Autowired
    private SparePartVerifyHandler sparePartVerifyHandler;
    @Autowired
    private FileUploadServiceImpl fileUploadService;

    /**
     * 备件列表查询
     * @param params 查询参数
     * @return 分页列表
     * */
    @GetMapping("/list")
    @ApiOperation(value="分页查询备件列表")
    @ApiImplicitParams({
            @ApiImplicitParam( name="page", value = "页面，默认为1" ),
            @ApiImplicitParam( name="limit", value = "分页大小，默认20，可不填" ),
            @ApiImplicitParam( name="sparePartNo", value = "备件编号，可不填" ),
            @ApiImplicitParam( name="name", value = "备件名称，可不填" ),
            @ApiImplicitParam( name="type", value = "备件类型，可不填" ),
            @ApiImplicitParam( name="storageType", value = "库存预警类型， 0=库存不足，1=库存过量,不传=全部" ),
            @ApiImplicitParam( name="searchText", value = "搜索关键字，根据备件编号和名称搜索" ),
            @ApiImplicitParam( name="warehouseId", value = "仓库id" ),
    })
    public ResponseData<Page<SparePartDTO>> list(@RequestParam Map<String, Object> params) {
        return ResponseData.success(sparePartService.page(params));
    }

    /**
     * 备件列表查询
     * @param params 查询参数
     * @return 分页列表
     * */
    @PostMapping("/list")
    @ApiOperation(value="分页查询备件列表")
    @ApiImplicitParams({
            @ApiImplicitParam( name="page", value = "页面，默认为1" ),
            @ApiImplicitParam( name="limit", value = "分页大小，默认20，可不填" ),
            @ApiImplicitParam( name="sparePartNo", value = "备件编号，可不填" ),
            @ApiImplicitParam( name="name", value = "备件名称，可不填" ),
            @ApiImplicitParam( name="type", value = "备件类型，可不填" ),
            @ApiImplicitParam( name="storageType", value = "库存预警类型， 0=库存不足，1=库存过量,不传=全部" ),
            @ApiImplicitParam( name="searchText", value = "搜索关键字，根据备件编号和名称搜索" ),
            @ApiImplicitParam( name="warehouseId", value = "仓库id" ),
    })
    public ResponseData<Page<SparePartDTO>> postList(@RequestBody(required = false) Map<String, Object> params) {
        return ResponseData.success(sparePartService.page(params));
    }

    /**
     * 备件列表导出
     * @param params 查询参数
     * @return 分页列表
     * */
    @PostMapping("/export")
    @ApiOperation(value="导出备件列表")
    @ApiImplicitParams({
            @ApiImplicitParam( name="page", value = "页面，默认为1" ),
            @ApiImplicitParam( name="limit", value = "分页大小，默认20，可不填" ),
            @ApiImplicitParam( name="site", value = "工厂，可不填" ),
            @ApiImplicitParam( name="sparePartNo", value = "备件编号，可不填" ),
            @ApiImplicitParam( name="name", value = "备件名称，可不填" ),
            @ApiImplicitParam( name="type", value = "备件类型，可不填" ),
            @ApiImplicitParam( name="storageType", value = "库存预警类型， 0=库存不足，1=库存过量,不传=全部" ),
            @ApiImplicitParam( name="searchText", value = "搜索关键字，根据备件编号和名称搜索" )
    })
    public void export(@RequestBody Map<String, Object> params, HttpServletResponse response) throws CommonException {
        params.put("page", "1");
        params.put("limit", Integer.MAX_VALUE);
        Page<SparePartDTO> page = sparePartService.page(params);
        ExcelUtils.exportExcel(page.getRecords(), "备件列表", "备件列表", SparePartDTO.class, "备件列表.xls", true, response);

    }

    @Value("${file.path}")
    private String filePath;
    @ApiOperation(value = "importFile", notes = "导入")
    @PostMapping("/importFile")
    public ResponseData importFile(@RequestParam("file") MultipartFile file) throws IOException {
        ImportParams params = new ImportParams();
        params.setNeedVerify(true);
        params.setTitleRows(0);
        params.setVerifyHandler(sparePartVerifyHandler);
        Map<String, Object> map = ExcelUtils.importExcel(
                file.getInputStream(),
                SparePart.class,
                params,
                filePath,
                sparePartService::saveByImport,
                null
        );
        return ResponseData.success(map);
    }

    @ApiOperation(value = "download", notes = "下载模板", httpMethod = "GET")
    @GetMapping(value = "/download")
    public String download(HttpServletResponse response) {
        InputStream in = null;
        ServletOutputStream out = null;
        try {
            in = (new ClassPathResource("templates/sparePart.xls")).getInputStream();
            out = response.getOutputStream();
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment;filename=备件导入模板.xls");
            response.setContentLength(in.available());
            IOUtils.copy(in, out);
            out.flush();
        } catch (Exception var16) {
            var16.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException var15) {
                    ;
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException var14) {
                }
            }
        }


        return "";
    }

    /**
     * 备件库存列表查询-lov
     * @param params 查询参数
     * @return 分页列表
     * */
    @PostMapping("/lov/list")
    @ApiOperation(value="分页查询备件库存列表")
    @ApiImplicitParams({
            @ApiImplicitParam( name="site", value = "工厂，可不填" ),
            @ApiImplicitParam( name="sparePartNo", value = "备件编号，可不填" ),
            @ApiImplicitParam( name="name", value = "备件名称，可不填" ),
            @ApiImplicitParam( name="type", value = "备件类型，可不填" ),
            @ApiImplicitParam( name="storageType", value = "库存预警类型， 0=库存不足，1=库存过量,不传=全部" ),
            @ApiImplicitParam( name="searchText", value = "搜索关键字，根据备件编号,备件名称,仓库名称搜索" )
    })
    public ResponseData<Page<SparePartDTO>> lovList(@RequestBody Map<String, Object> params) {
        return ResponseData.success(sparePartService.pageByInventory(params));
    }


    /**
     * 备件新增
     * */
    @PostMapping
    @ApiOperation(value="备件新增")
    public ResponseData<Boolean> add(@RequestBody @Valid SparePart sparePart) throws CommonException {
        if(CollUtil.isEmpty(sparePart.getDeviceIdList())) {
            String deviceIds = sparePart.getDeviceIds();
            sparePart.setDeviceIdList(StrUtil.splitTrim(deviceIds, ";"));
        }
        return ResponseData.success(sparePartService.addSparePart(sparePart));
    }

    /**
     * 备件修改
     * */
    @PutMapping
    @ApiOperation(value="备件修改")
    public ResponseData<Boolean> update(@RequestBody @Valid SparePart sparePart) {
        if(CollUtil.isEmpty(sparePart.getDeviceIdList())) {
            String deviceIds = sparePart.getDeviceIds();
            sparePart.setDeviceIdList(StrUtil.splitTrim(deviceIds, ";"));
        }
        return ResponseData.success(sparePartService.updateSparePart(sparePart));
    }

    /**
     * 备件详情
     * */
    @GetMapping("/{id}")
    @ApiOperation(value="备件详情")
    public ResponseData<SparePart> detail(@ApiParam(name = "id", value = "备件id") @PathVariable("id") String id) {
        return ResponseData.success(sparePartService.detail(id));
    }

    /**
     * 删除备件
     * */
    @DeleteMapping("/delete")
    @ApiOperation(value="删除备件")
    public ResponseData<Boolean> delete(@RequestBody List<String> ids) {
        return ResponseData.success(sparePartService.deleteSparePartBatch(ids));
    }

    /**
     * 出入库记录
     * @param params  sparePartId=备件id
     * */
    @GetMapping("/storageRecord")
    @ApiOperation(value="出入库记录")
    @ApiImplicitParams({
            @ApiImplicitParam( name="page", value = "页面，默认为1" ),
            @ApiImplicitParam( name="limit", value = "分页大小，默认20，可不填" ),
            @ApiImplicitParam( name="sparePartId", value = "备件id" ),
            @ApiImplicitParam( name="storageType", value = "出入库类型，0=出库，1=入库" )

    })
    public ResponseData<Page<SparePartStorageRecordDTO>> storageRecord(@RequestParam Map<String, Object> params) {
        return ResponseData.success(sparePartStorageRecordService.getStorageRecordBySparePart(params));
    }


    /**
     * 出入库详情
     * */
    @GetMapping("/storageRecord/detail/{id}")
    @ApiOperation(value="出入库详情")
    public ResponseData<SparePartStorageRecordDTO> storageRecordDetail(@ApiParam(name = "id", value = "出入库记录id") @PathVariable("id") String id) {
        return ResponseData.success(sparePartStorageRecordService.detail(id));
    }

    /**
     * 出入库备件列表
     * @param params recordId 出入库记录id
     * */
    @GetMapping("/storageRecord/sparepartList")
    @ApiOperation(value="出入库备件列表")
    @ApiImplicitParams({
            @ApiImplicitParam( name="page", value = "页面，默认为1" ),
            @ApiImplicitParam( name="limit", value = "分页大小，默认20，可不填" ),
            @ApiImplicitParam( name="recordId", value = "出入库记录id" )
    })
    public ResponseData<Page<SparePartStorageRecordDetail>> recordSparepartList(@RequestParam Map<String, Object> params) {
        return ResponseData.success(sparePartStorageRecordService.listByRecordId(params));
    }

    /**
     * 出库
     * */
    @PostMapping("/delivery")
    @ApiOperation(value="出库")
    public ResponseData<Boolean> delivery(@RequestBody @Valid SparePartStorageRecord sparePartStorageRecord) throws CommonException {
        sparePartStorageRecord.setIsIn(false);
        return ResponseData.success(sparePartStorageRecordService.addStorageRecord(sparePartStorageRecord));
    }
    /**
     * 入库
     * */
    @PostMapping("/putInStorage")
    @ApiOperation(value="入库")
    public ResponseData<Boolean> purInStorage(@RequestBody @Valid SparePartStorageRecord sparePartStorageRecord) throws CommonException {
        sparePartStorageRecord.setIsIn(true);
        List<SparePartStorageRecordDetail> detailList = sparePartStorageRecord.getDetailList();
        //处理入库仓库选择
        if(CollUtil.isNotEmpty(detailList)) {
            String wareHouseId = sparePartStorageRecord.getWareHouseId();
            String wareHouseName = sparePartStorageRecord.getWareHouseName();
            detailList.forEach(e -> {
                if(StrUtil.isBlank(e.getWareHouseId())) {
                    e.setWareHouseId(wareHouseId);
                }
                if(StrUtil.isBlank(e.getWareHouseName())) {
                    e.setWareHouseName(wareHouseName);
                }
            });
        }
        return ResponseData.success(sparePartStorageRecordService.addStorageRecord(sparePartStorageRecord));
    }

    /**
     * 关联设备列表
     * */
    @GetMapping("/deviceList")
    @ApiOperation(value="关联设备列表")
    @ApiImplicitParams({
            @ApiImplicitParam( name="page", value = "页面，默认为1" ),
            @ApiImplicitParam( name="limit", value = "分页大小，默认20，可不填" ),
            @ApiImplicitParam( name="sparePartId", value = "备件id" )
    })
    public ResponseData<Page<DeviceInfoDTO>> getDeviceList(@RequestParam Map<String, Object> params) {
        return ResponseData.success(sparePartDeviceMappingService.getDeviceList(params));
    }

    /**
     * 更换记录
     * */
    @GetMapping("/repair/page")
    @ApiOperation(value="更换记录")
    @ApiImplicitParams({
            @ApiImplicitParam( name="page", value = "页面，默认为1" ),
            @ApiImplicitParam( name="limit", value = "分页大小，默认20，可不填" ),
            @ApiImplicitParam( name="sparePartId", value = "备件id" )
    })
    public ResponseData<Page<DeviceChangeRecordDTO>> getRepairBySparePart(@RequestParam Map<String, Object> params) {
        return ResponseData.success(sparePartDeviceMappingService.changeRecord(params));
    }


    /**
     * 备件统计信息
     * */
    @GetMapping("/record/statistics")
    @ApiOperation(value="备件库存统计信息")
    public ResponseData<SparepartStorageCountStatisticsDTO> recordStatistics() {
        return ResponseData.success(sparePartStorageRecordService.storageCountStatistics(UserUtils.getSite()));
    }

}

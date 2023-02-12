package com.itl.mes.core.client.service;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.CollectionRecordCommonTempDTO;
import com.itl.mes.core.api.dto.QualitativeInspectionSaveDTO;
import com.itl.mes.core.api.dto.RepairTempDataDTO;
import com.itl.mes.core.api.entity.TemporaryData;
import com.itl.mes.core.client.config.FallBackConfig;
import com.itl.mes.core.client.service.impl.TemporaryDataServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 采集记录暂存表
 * @author dengou
 * @date 2021/11/12
 */
@FeignClient(value = "mes-md-provider",contextId = "temporaryData", fallback = TemporaryDataServiceImpl.class, configuration = FallBackConfig.class)
public interface TemporaryDataService {

    /**
     * 获取定性检验暂存信息
     * @param station 工位编号
     * @param sn 条码
     * @return 性检验暂存信息
     * */
    @GetMapping("/qualitativeInspection/getBySn/{sn}")
    ResponseData<List<QualitativeInspectionSaveDTO>> getQualitativeInspectionListBySn(@PathVariable("sn") String sn, @RequestParam("station") String station);

    /**
     * 获取定量检验暂存信息
     * @param station 工位编号
     * @param sn 条码
     * @return 性检验暂存信息
     * */
    @GetMapping("/quantifyInspection/getBySn/{sn}")
    ResponseData<List<QualitativeInspectionSaveDTO>> getQuantifyInspectionListBySn(@PathVariable("sn") String sn, @RequestParam("station") String station);

    /**
     * 删除临时数据
     * @param sn 条码
     * @param station 工位
     * @param type 类型
     * @return 是否成功
     * */
    @DeleteMapping("/temporaryData/remove")
    ResponseData<Boolean> remove(@RequestParam("sn") String sn, @RequestParam("station") String station,  @RequestParam("type") String type);
    /**
     * 删除临时数据
     * @param sn 条码
     * @param station 工位
     * @param types 类型
     * @return 是否成功
     * */
    @DeleteMapping("/temporaryData/removeList")
    ResponseData<Boolean> removeList(@RequestParam("sn") String sn, @RequestParam("station") String station, @RequestParam("type") String types);

    /**
     * 保存临时数据， 根据sn和station判断是否存在， 存在则更新，不存在则新增
     * */
    @PostMapping("/temporaryData/repair/saveTemp")
    ResponseData<Boolean> addOrUpdate(@RequestBody TemporaryData temporaryData);


    /**
     * 获取定量检验暂存信息
     * @param station 工位编号
     * @param sn 条码
     * @return 性检验暂存信息
     * */
    @GetMapping("/repair/getBySn/{sn}")
    ResponseData<List<RepairTempDataDTO>> getRepairTempDataDtoList(@RequestParam("sn") String sn, @RequestParam("station") String station);


    /**
     * 获取通用临时数据
     * */
    @PostMapping("/temporaryData/common/getCommonData")
    ResponseData<List<CollectionRecordCommonTempDTO>> getCommons(@RequestParam("sn") String sn, @RequestParam("station")String station, @RequestParam("types") String types);

    /**
     * 根据id列表查询
     * */
    @PostMapping("/temporaryData/getByIds")
    ResponseData<List<TemporaryData>> getByIds(@RequestBody List<String> ids);
}

package com.itl.mes.me.provider.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.common.base.dto.UserTDto;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.Assert;
import com.itl.iap.common.base.utils.CommonUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.MeSfcHandleBO;
import com.itl.mes.core.api.bo.ProductLineHandleBO;
import com.itl.mes.core.api.bo.StationHandleBO;
import com.itl.mes.core.api.constant.CheckResultEnum;
import com.itl.mes.core.api.constant.TemporaryDataTypeEnum;
import com.itl.mes.core.api.dto.CollectionRecordCommonTempDTO;
import com.itl.mes.core.api.dto.QualitativeInspectionSaveDTO;
import com.itl.mes.core.api.dto.RepairTempDataDTO;
import com.itl.mes.core.api.entity.Station;
import com.itl.mes.core.api.entity.TemporaryData;
import com.itl.mes.core.api.entity.TemporaryDataRetryLog;
import com.itl.mes.core.api.vo.ShopOrderFullVo;
import com.itl.mes.core.client.service.CollectionRecordService;
import com.itl.mes.core.client.service.StationService;
import com.itl.mes.core.client.service.TemporaryDataRetryLogService;
import com.itl.mes.core.client.service.TemporaryDataService;
import com.itl.mes.me.api.entity.*;
import com.itl.mes.me.api.service.*;
import com.itl.mes.me.provider.service.impl.OperationServiceImpl;
import com.itl.mom.label.client.service.MeProductStatusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.stream.Collectors;


/**
 * Sfc不合格记录表
 *
 * @author 崔翀赫
 * @date 2021/1/26$
 * @since JDK1.8
 */
@Slf4j
@RestController
@Api(tags = "检查工位")
@RequestMapping("meSfcNcLog")
public class MeSfcNcLogController {
    @Value("${mybatis-plus.configuration.database-id}")
    private String databaseId;
    @Autowired
    private MeSfcNcLogService meSfcNcLogService;
    @Resource
    private MeSfcNcLogTempService ncTempService;
    @Autowired
    private MeSfcService meSfcService;
    @Autowired
    private MeSnDataCollectionService meSnDataCollectionService;
    @Resource
    private MeSnDataCollectionTempService dataTempService;
    @Resource
    private OperationServiceImpl operationService;
    @Resource
    private AssyTempService assyTempService;
    @Resource
    private MeSfcAssyService assyService;
    @Resource
    private LoadingListTempService loadingListTempService;
    @Resource
    private LoadingListService loadingListService;
    @Resource
    private MeSnService snService;
    @Autowired
    private TemporaryDataService temporaryDataService;
    @Autowired
    private CollectionRecordService collectionRecordService;
    @Autowired
    private MeProductStatusService meProductStatusService;
    @Resource
    private DataSourceTransactionManager dataSourceTransactionManager;
    @Resource
    private TransactionDefinition transactionDefinition;
    @Autowired
    private MeSnCrossStationLogService meSnCrossStationLogService;
    @Autowired
    private StationService stationService;
    @Autowired
    private TemporaryDataRetryLogService temporaryDataRetryLogService;

    /**
     * 不合格记录
     */
    @GetMapping("/saveNc")
    @ApiOperation(value = "不合格记录")
    public ResponseData save(@RequestParam String sn, @RequestParam String ncBo, @RequestParam BigDecimal doneQty, @RequestParam BigDecimal scrapQty) {

        MeSfc byId = meSfcService.getById(new MeSfcHandleBO(UserUtils.getSite(), sn).getBo());
        if (byId == null) {
            return ResponseData.error("该标签未维护!");
        }
        if ("已检查".equals(byId.getState())) {
            return ResponseData.error("该标签已检查!");
        }
        meSfcNcLogService.saveNc(byId, ncBo, doneQty, scrapQty);
        return ResponseData.success(meSfcService.getById(new MeSfcHandleBO(UserUtils.getSite(), sn).getBo()));
    }


    /**
     * 合格记录
     */
    @GetMapping("/saveOk")
    @ApiOperation(value = "合格记录")
    public ResponseData saveOk(@RequestParam String sn, @RequestParam BigDecimal doneQty, @RequestParam BigDecimal scrapQty) {

        MeSfc byId = meSfcService.getById(new MeSfcHandleBO(UserUtils.getSite(), sn).getBo());
        if (byId == null) {
            return ResponseData.error("该标签未维护!");
        }
        if ("已检查".equals(byId.getState())) {
            return ResponseData.error("该标签已检查!");
        }
        meSfcNcLogService.saveOk(byId, doneQty, scrapQty);
        return ResponseData.success(meSfcService.getById(new MeSfcHandleBO(UserUtils.getSite(), sn).getBo()));
    }

    @GetMapping("/CrossStation")
    @ApiOperation(value = "过站")
    @Transactional(rollbackFor = Exception.class)
    public ResponseData CrossStation(@RequestParam String sn, @RequestParam String operationBo, @RequestParam(required = false) String station) throws CommonException {
        //todo wip_log
        operationService.checkSn(sn);
        ShopOrderFullVo shopOrderFullVo = operationService.checkShopOrder(sn);
//        operationService.checkSnOperation(sn, operationBo, shopOrderFullVo);
        final Date date = new Date();
        CompletableFuture.runAsync(() -> snService.saveOrUpdate(new MeSn().setSite(UserUtils.getSite()).setSn(sn)));

        final String user = Optional.ofNullable(UserUtils.getCurrentUser()).map(UserTDto::getUserName).orElse("");
        String site = UserUtils.getSite();
        CompletableFuture.runAsync(() -> snService.saveOrUpdate(new MeSn().setSite(site).setSn(sn)
                .setSnTypeBo("过站").setCreateDate(date).setCreateUser(user)
                .setModifyDate(date).setModifyUser(user))).join();

        if(StrUtil.isBlank(station)) {
            station = UserUtils.getStation();
        }
        String result = null;
        ResponseData<List<QualitativeInspectionSaveDTO>> qualitativeInspectionList = temporaryDataService.getQualitativeInspectionListBySn(sn, station);
        List<QualitativeInspectionSaveDTO> qualitativeCollectionRecordList = new ArrayList<>();
        if(qualitativeInspectionList.isSuccess()) {
            qualitativeCollectionRecordList = qualitativeInspectionList.getData();
            if(CollUtil.isNotEmpty(qualitativeCollectionRecordList)) {
                long count = qualitativeCollectionRecordList.stream().filter(e -> StrUtil.equals(e.getResult(), CheckResultEnum.OK.getCode())).count();
                result = (count > 0 && count == qualitativeCollectionRecordList.size()) ? CheckResultEnum.OK.getCode() : CheckResultEnum.NG.getCode();

            }
        }

        ResponseData<List<QualitativeInspectionSaveDTO>> quantifyInspectionList = temporaryDataService.getQuantifyInspectionListBySn(sn, station);
        List<QualitativeInspectionSaveDTO> quantifyCollectionRecordList = new ArrayList<>();
        if(quantifyInspectionList.isSuccess()) {
            quantifyCollectionRecordList = quantifyInspectionList.getData();
            if(CollUtil.isNotEmpty(quantifyCollectionRecordList)) {
                long count = quantifyCollectionRecordList.stream().filter(e -> StrUtil.equals(e.getResult(), CheckResultEnum.OK.getCode())).count();
                result = (count > 0 && count == quantifyCollectionRecordList.size()) ? CheckResultEnum.OK.getCode() : CheckResultEnum.NG.getCode();

            }
        }

        operationService.changeSnRouterCurrentNode(sn, result, shopOrderFullVo.getBo(), operationBo);

        //保存过站次数记录
        MeSnCrossStationLog meSnCrossStationLog = MeSnCrossStationLog.builder()
                .operationBo(operationBo)
                .sn(sn)
                .station(station)
                .site(site)
                .createUser(UserUtils.getUserName())
                .build();
        meSnCrossStationLogService.addLog(meSnCrossStationLog);

        //提交事务
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
        dataSourceTransactionManager.commit(transactionStatus);

        //保存定性检验采集记录
        List<QualitativeInspectionSaveDTO> finalQualitativeCollectionRecordList = qualitativeCollectionRecordList;
        String finalStation = station;
        ThreadUtil.execAsync(() -> saveQualitativeCollectionRecord(sn, shopOrderFullVo.getShopOrder(), site, finalStation, finalQualitativeCollectionRecordList));

        //保存定量检验采集记录
        List<QualitativeInspectionSaveDTO> finalQuantifyCollectionRecordList = quantifyCollectionRecordList;
        ThreadUtil.execAsync(() -> saveQuantifyCollectionRecord(sn, shopOrderFullVo.getShopOrder(), site, finalStation, finalQuantifyCollectionRecordList));

        //保存维修记录
        ThreadUtil.execAsync(() -> saveRepairRecord(sn, finalStation, shopOrderFullVo));

        //通用保存采集记录
        ThreadUtil.execAsync(() -> saveCommon(sn, finalStation));

        return ResponseData.success();
    }

    @PostMapping("/crossStation/retry")
    @ApiOperation(value = "过站重传")
    @Transactional(rollbackFor = Exception.class)
    public ResponseData crossStationRetry(@RequestBody List<String> tempDataIds) {
        Assert.valid(CollUtil.isEmpty(tempDataIds), "id列表不能为空");
        ResponseData<List<TemporaryData>> temporaryDataResult = temporaryDataService.getByIds(tempDataIds);
        Assert.valid(!temporaryDataResult.isSuccess(), temporaryDataResult.getMsg());
        List<TemporaryData> temporaryDataList = temporaryDataResult.getData();
        String userName = UserUtils.getUserName();
        String site = UserUtils.getSite();
        for (TemporaryData temporaryData : temporaryDataList) {
            String stationBo = new StationHandleBO(site, temporaryData.getStation()).getBo();
            ResponseData<Station> stationByIdResult = stationService.getStationById(stationBo);
            Assert.valid(!stationByIdResult.isSuccess(), stationByIdResult.getMsg());
            Station station = stationByIdResult.getData();
            String msg = null;
            try {
                CrossStation(temporaryData.getSn(), station.getOperationBo(), temporaryData.getStation());
            } catch (Exception e) {
                log.error("过站重传捕获异常", e);
                msg = e.getMessage();
            }
            TemporaryDataRetryLog temporaryDataRetryLog = new TemporaryDataRetryLog();
            temporaryDataRetryLog.setTemporaryDataId(temporaryData.getId());
            temporaryDataRetryLog.setLastResultMsg(msg);
            temporaryDataRetryLog.setUpdateUser(userName);
            temporaryDataRetryLogService.saveLog(temporaryDataRetryLog);
        }
        return ResponseData.success();
    }


    /**
     * 保存定性检验采集记录，从临时表中取值保存至采集记录表
     * @return 返回结果 NG/OK, 采集结果有一条为NG则返回NG, 只有全部OK才返回OK
     * */
    private void saveQualitativeCollectionRecord(String sn, String shopOrder, String site, String station, List<QualitativeInspectionSaveDTO> data) {
        //查询是否有暂存记录
        ResponseData<String> saveResult = collectionRecordService.saveTempQualitativeInspection(data, shopOrder, site);
        if(saveResult.isSuccess()) {
            temporaryDataService.remove(sn, station, TemporaryDataTypeEnum.QUALITATIVE.getCode());
        }
    }
    /**
     * 保存定量检验采集记录，从临时表中取值保存至采集记录表
     * @return 返回结果 NG/OK, 采集结果有一条为NG则返回NG, 只有全部OK才返回OK
     * */
    private void saveQuantifyCollectionRecord(String sn, String shopOrder, String site, String station, List<QualitativeInspectionSaveDTO> data) {
        //查询是否有暂存记录
        ResponseData<String> saveResult = collectionRecordService.saveTempQualitativeInspection(data, shopOrder, site);
        if(saveResult.isSuccess()) {
            temporaryDataService.remove(sn, station, TemporaryDataTypeEnum.QUANTIFY.getCode());
        }
    }
    /**
     * 保存定量检验采集记录，从临时表中取值保存至采集记录表
     * */
    private void saveRepairRecord(String sn, String station, ShopOrderFullVo shopOrderFullVo) {
        //查询是否有暂存记录
        ResponseData<List<RepairTempDataDTO>> result = temporaryDataService.getRepairTempDataDtoList(sn, station);
        if(result.isSuccess() && CollUtil.isNotEmpty(result.getData())) {
            List<RepairTempDataDTO> data = result.getData();
            data.forEach(e -> {
                e.setProductionLine(shopOrderFullVo.getProductLine());
                e.setShiftName(shopOrderFullVo.getShiftName());
            });
            ResponseData<Boolean> saveResult = collectionRecordService.saveProductMaintenanceRecord(data);
            if(saveResult.isSuccess() && saveResult.getData()) {
                temporaryDataService.remove(sn, station, TemporaryDataTypeEnum.REPAIR.getCode());
            }
        }
    }

    /**
     * 通用保存采集记录
     * */
    private void saveCommon(String sn, String station) {
        List<String> types = new ArrayList<>();
        //使用CollectionRecordCommonTempDTO暂存的数据过站，只需要在此增加类型即可
        types.add(TemporaryDataTypeEnum.PACK.getCode());
        //加入单体条码绑定
        types.add(TemporaryDataTypeEnum.BARCODEBIND.getCode());
        //加入批次条码绑定
        types.add(TemporaryDataTypeEnum.BATCHBARCODE.getCode());
        //在线打印
        types.add(TemporaryDataTypeEnum.IN_PRODUCT_LINE_PRINT.getCode());
        String typeStr = CollUtil.join(types, StrUtil.COMMA);
        ResponseData<List<CollectionRecordCommonTempDTO>> result = temporaryDataService.getCommons(sn, station, typeStr);
        if(result.isSuccess() && CollUtil.isNotEmpty(result.getData())) {
            List<CollectionRecordCommonTempDTO> data = result.getData();
            ResponseData<Boolean> saveResult = collectionRecordService.saveByCommon(data);
            if(saveResult.isSuccess() && saveResult.getData()) {
                temporaryDataService.removeList(sn, station, typeStr);
            }
        }
    }


    private void extractedData(String sn) {
        QueryWrapper<MeSnDataCollectionTemp> queryWrapper = new QueryWrapper<MeSnDataCollectionTemp>();
        if (databaseId.equals(CommonUtil.DATABASEID)) {
            queryWrapper.eq("sn", sn).select("id", "operation_id", "operation_type", "sn", "remark", "text", "num", "\"option\"", "result", "item_id");
        } else {
            queryWrapper.eq("sn", sn);
        }
        final List<MeSnDataCollectionTemp> list = dataTempService.list(queryWrapper);
        if (CollUtil.isNotEmpty(list)) {
            list.forEach(x -> {
                MeSnDataCollection meSnDataCollection = new MeSnDataCollection();
                BeanUtil.copyProperties(x, meSnDataCollection);
                meSnDataCollectionService.save(meSnDataCollection);
            });
            final List<String> collect = list.stream().map(MeSnDataCollectionTemp::getId).collect(Collectors.toList());
            dataTempService.removeByIds(collect);
        }
    }

    private void extractedNc(String sn) {
        final List<MeSfcNcLogTemp> list = ncTempService.list(new QueryWrapper<MeSfcNcLogTemp>().lambda().eq(MeSfcNcLogTemp::getSfc, sn));
        if (CollUtil.isNotEmpty(list)) {
            ArrayList<MeSfcNcLog> meSfcNcLogs = new ArrayList<>();
            list.forEach(x -> {
                MeSfcNcLog meSfcNcLog = new MeSfcNcLog();
                BeanUtil.copyProperties(x, meSfcNcLog);
                meSfcNcLogs.add(meSfcNcLog);
            });
            meSfcNcLogService.saveBatch(meSfcNcLogs);
            final List<String> collect = list.stream().map(MeSfcNcLogTemp::getBo).collect(Collectors.toList());
            ncTempService.removeByIds(collect);
        }
    }

    private void extractedLoad(String sn) {
        final List<LoadingListTemp> list = loadingListTempService.list(new QueryWrapper<LoadingListTemp>().lambda().eq(LoadingListTemp::getSfc, sn));
        if (CollUtil.isNotEmpty(list)) {
            ArrayList<LoadingList> loadingLists = new ArrayList<>();
            list.forEach(x -> {
                LoadingList loadingList = new LoadingList();
                BeanUtil.copyProperties(x, loadingList);
                loadingLists.add(loadingList);
            });
            loadingListService.saveBatch(loadingLists);
            final List<String> collect = list.stream().map(LoadingListTemp::getId).collect(Collectors.toList());
            loadingListTempService.removeByIds(collect);
        }
    }

    private void extractedAssy(String sn) {
        final List<AssyTemp> list = assyTempService.list(new QueryWrapper<AssyTemp>().lambda().eq(AssyTemp::getSfc, sn));
        if (CollUtil.isNotEmpty(list)) {
            ArrayList<MeSfcAssy> sfcAssies = new ArrayList<>();
            list.forEach(x -> {
                MeSfcAssy sfcAssy = new MeSfcAssy();
                BeanUtil.copyProperties(x, sfcAssy);
                sfcAssies.add(sfcAssy);
            });
            assyService.saveBatch(sfcAssies);
            final List<String> collect = list.stream().map(AssyTemp::getId).collect(Collectors.toList());
            assyTempService.removeByIds(collect);
        }
    }

    @PostMapping("/save")
    @ApiOperation(value = "检验判定")
    @Transactional(rollbackFor = Exception.class)
    public ResponseData saveAll(@RequestBody MeSfcNcLog meSfcNcLog) {
        // todo wiplog
        meSfcNcLog.setStationBo(new StationHandleBO(UserUtils.getSite(), UserUtils.getStation()).getBo())
                .setProductLineBo(new ProductLineHandleBO(UserUtils.getSite(), UserUtils.getProductLine()).getBo());
        saveNc(meSfcNcLog.setRemark("检验判定"));
        return ResponseData.success();
    }


    /**
     * 数据判定
     */
    @PostMapping("/saveDataCollection")
    @ApiOperation(value = "数据判定")
    @Transactional(rollbackFor = Exception.class)
    public ResponseData saveDataCollection(@RequestBody List<MeSnDataCollection> meSnDataCollection) {
        // todo wiplog
        meSnDataCollection.forEach(x -> {
            MeSnDataCollectionTemp meSnDataCollectionTemp = new MeSnDataCollectionTemp();
            BeanUtil.copyProperties(x, meSnDataCollectionTemp);
            x.getNcLogs().setStationBo(new StationHandleBO(UserUtils.getSite(), UserUtils.getStation()).getBo())
                    .setProductLineBo(new ProductLineHandleBO(UserUtils.getSite(), UserUtils.getProductLine()).getBo());
            dataTempService.save(meSnDataCollectionTemp);
            if ("NG".equals(x.getResult())) {
                saveNc(x.getNcLogs().setRemark("数据判定"));
            }
        });
        return ResponseData.success();
    }

    /**
     * 数据收集
     */
    @PostMapping("/saveDataCollections")
    @ApiOperation(value = "数据收集")
    public ResponseData saveDataCollections(@RequestBody List<MeSnDataCollection> meSnDataCollection) {
        // todo wiplog
        meSnDataCollection.forEach(x -> {
            MeSnDataCollectionTemp meSnDataCollectionTemp = new MeSnDataCollectionTemp();
            BeanUtil.copyProperties(x, meSnDataCollectionTemp);
            x.getNcLogs().setStationBo(new StationHandleBO(UserUtils.getSite(), UserUtils.getStation()).getBo())
                    .setProductLineBo(new ProductLineHandleBO(UserUtils.getSite(), UserUtils.getProductLine()).getBo());
            dataTempService.save(meSnDataCollectionTemp);
        });
        return ResponseData.success();
    }

    private void saveNc(MeSfcNcLog meSfcNcLog) {
        MeSfcNcLogTemp meSfcNcLogTemp = new MeSfcNcLogTemp();

        BeanUtil.copyProperties(meSfcNcLog, meSfcNcLogTemp);

        meSfcNcLogTemp.setSite(UserUtils.getSite());
        meSfcNcLogTemp.setRecordTime(new Date());
        final String ncCodeBo = meSfcNcLogTemp.getNcCodeBo();
        if (StrUtil.isNotBlank(ncCodeBo)) {
            for (String s : ncCodeBo.split(";")) {
                if (StrUtil.isNotEmpty(s)) {
                    meSfcNcLogTemp.setBo(IdUtil.fastSimpleUUID());
                    meSfcNcLogTemp.setNcCodeBo(s);
                    ncTempService.save(meSfcNcLogTemp);
                }
            }
        } else {
            meSfcNcLogTemp.setBo(IdUtil.fastSimpleUUID());
            ncTempService.save(meSfcNcLogTemp);
        }
    }

}

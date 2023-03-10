package com.itl.mes.me.provider.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.Assert;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.util.UUID;
import com.itl.mes.core.api.bo.OperationHandleBO;
import com.itl.mes.core.api.bo.StationHandleBO;
import com.itl.mes.core.api.constant.SnTypeEnum;
import com.itl.mes.core.api.constant.TemporaryDataTypeEnum;
import com.itl.mes.core.api.dto.RepairTempDataDTO;
import com.itl.mes.core.api.entity.ProductionDefectRecord;
import com.itl.mes.core.api.entity.TemporaryData;
import com.itl.mes.core.api.vo.ShopOrderFullVo;
import com.itl.mes.core.client.service.CollectionRecordService;
import com.itl.mes.core.client.service.ShopOrderService;
import com.itl.mes.core.client.service.TemporaryDataService;
import com.itl.mes.me.api.dto.RepairInputDto;
import com.itl.mes.me.api.dto.RepairTempDto;
import com.itl.mes.me.api.dto.ScrapOrRepairFinDto;
import com.itl.mes.me.api.entity.MaintenanceMethod;
import com.itl.mes.me.api.entity.MeSfcNcLog;
import com.itl.mes.me.api.entity.MeSfcRepair;
import com.itl.mes.me.api.service.MaintenanceMethodService;
import com.itl.mes.me.api.service.MeSfcRepairService;
import com.itl.mes.me.api.service.OperationService;
import com.itl.mes.me.api.vo.RepairLogListVo;
import com.itl.mes.me.api.vo.RepairObjVo;
import com.itl.mes.me.api.vo.RepairStationVo;
import com.itl.mes.me.api.vo.SendRepairDetailsVo;
import com.itl.mes.me.provider.mapper.MeSfcRepairMapper;
import com.itl.mom.label.api.entity.label.Sn;
import com.itl.mom.label.client.service.SnService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ???????????????
 */
@Service
public class MeSfcRepairServiceImpl extends ServiceImpl<MeSfcRepairMapper, MeSfcRepair> implements MeSfcRepairService {

    @Autowired
    private MeSfcNcLogServiceImpl meSfcNcLogService;
    @Autowired
    private MeSfcRepairMapper meSfcRepairMapper;
    @Autowired
    private MeSfcRepairServiceImpl meSfcRepairService;
    @Autowired
    private CollectionRecordService collectionRecordService;
    @Autowired
    private TemporaryDataService temporaryDataService;
    @Autowired
    private MaintenanceMethodService maintenanceMethodService;
    @Autowired
    private ShopOrderService shopOrderService;

    private OperationService operationService;
    private SnService snService;

    @Autowired
    public void setOperationService(OperationService operationService) {
        this.operationService = operationService;
    }

    @Autowired
    public void setSnService(SnService snService) {
        this.snService = snService;
    }

    /**
     * ??????????????????
     */
    @Override
    public RepairStationVo frontDataVo(String snCode) throws CommonException {

        /**
         * ??????????????????
         */
        ShopOrderFullVo shopOrderFullVo = operationService.checkShopOrder(snCode);
        String shopOrderBo = shopOrderFullVo.getBo();
        String productLine = UserUtils.getProductLine();
        String item = shopOrderFullVo.getItem();

        /**
         * ???????????? ????????????????????????
         */
        // ????????????????????????
        List<MeSfcNcLog> dbSfcNcLogList = meSfcNcLogService.list(new QueryWrapper<MeSfcNcLog>().eq("SFC", snCode));

        if (CollUtil.isNotEmpty(dbSfcNcLogList)) {
            String operation = null;
            String operationBo = dbSfcNcLogList.get(0).getOperationBo();
            if (null != operationBo) {
                operation = new OperationHandleBO(dbSfcNcLogList.get(0).getOperationBo()).getOperation();
            }
            String station = new StationHandleBO(dbSfcNcLogList.get(0).getStationBo()).getStation();
            String userBo = dbSfcNcLogList.get(0).getUserBo();
            Date recordTime = dbSfcNcLogList.get(0).getRecordTime();

            RepairStationVo repairStationVo = new RepairStationVo();
            repairStationVo.setSfc(snCode);
            repairStationVo.setShopOrder(shopOrderBo);
            repairStationVo.setProductLine(productLine);
            repairStationVo.setItem(item);
//            repairStationVo.setScheduleNo(scheduleNo);
            repairStationVo.setOperation(operation);
            repairStationVo.setStation(station);
            repairStationVo.setUserBo(userBo);
            repairStationVo.setRecordTime(recordTime);

            /**
             * ???????????? ??????
             */
            List<String> badCodeBoList = dbSfcNcLogList.stream().map(MeSfcNcLog::getNcCodeBo).collect(Collectors.toList());
            List<SendRepairDetailsVo> sendRepairDetailsVo = meSfcRepairMapper.getSendRepairDetailsVo(badCodeBoList, snCode);
            repairStationVo.setSendRepairDetailsVo(sendRepairDetailsVo);

            /**
             * ?????????????????????????????????
             */
            List<RepairLogListVo> repairLogListVo = meSfcRepairMapper.getRepairLogListVo(snCode);
            repairStationVo.setRepairLogListVo(repairLogListVo);

            /**
             * ??????????????????
             */
            // Integer repairCount = meSfcRepairMapper.selectCount(new QueryWrapper<MeSfcRepair>().groupBy("SFC")); ???????????????
            String site = UserUtils.getSite();
            // List<MeSfcRepair> repairLisCount = meSfcRepairMapper.selectList(new QueryWrapper<MeSfcRepair>().select("DISTINCT SFC"));
            List<MeSfcRepair> repairLisCount = meSfcRepairMapper.selectList(new QueryWrapper<MeSfcRepair>().select("DISTINCT (SFC)").eq("SITE", site));
            if (CollUtil.isNotEmpty(repairLisCount)) {
                int repairCount = repairLisCount.size();
                repairStationVo.setRepairCount(repairCount);
            }

            return repairStationVo;
        } else {
            throw new CommonException("SFC: " + snCode + "??????????????????????????????", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
    }

    /**
     * ??????????????????
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public RepairObjVo saveInputRepair(List<RepairInputDto> repairInputList) throws CommonException {

        //badItemBo ??? replaceItemBo ??????????????? ????????????

        /**
         * ??????????????????
         * {
         *   "badItemBo": "ITEM:1020,MA001,1_1",
         *   "badItemSn": "SN:1040,1002001002002105000008",
         *   "dutyUnit": "1111",
         *   "remark": "2222",
         *   "repairMethod": "11111",
         *   "repairReason": "11111",
         *   "replaceItemSn": "1002001002002105000003",
         *   "sfc": "1002001002002105000001"
         * }
         */

        /**
         * ????????????
         */
        if (CollUtil.isEmpty(repairInputList)) {
            throw new CommonException("?????????????????????", CommonExceptionDefinition.BASIC_EXCEPTION);
        }

        // ??????????????????????????????Repair???
        List<MeSfcRepair> byId = meSfcRepairService.list(new QueryWrapper<MeSfcRepair>().eq("NG_LOG_BO", repairInputList.get(0).getNgLogBo()));
        if (CollUtil.isNotEmpty(byId)) {
            throw new CommonException("???????????????????????????", CommonExceptionDefinition.BASIC_EXCEPTION);
        }

        String site = UserUtils.getSite();
        List<MeSfcRepair> meSfcRepairList = new ArrayList<>();
        for (RepairInputDto repairInputDto : repairInputList) {
            /**
             * ????????????????????????repair???????????????MeSfcNcLog?????????
             * ??????????????????????????????????????????????????????MeSfcNcLog???BO,????????????MeSfcNcLog???BO????????????
             */
            MeSfcNcLog dbMeSfcNcLog = meSfcNcLogService.getOne(new QueryWrapper<MeSfcNcLog>().eq("BO", repairInputDto.getNgLogBo()).eq("SFC", repairInputDto.getSfc()));
            if (dbMeSfcNcLog == null) {
                throw new CommonException("???????????????????????????SN???: " + repairInputDto.getBadItemSn() + " NGLog?????????????????????:)", CommonExceptionDefinition.BASIC_EXCEPTION);
            }

            /**
             * ???????????????itemBo ???????????????
             */
            Sn sn = snService.getSnInfo(repairInputDto.getReplaceItemSn());
            String itemBo = sn.getItemBo();
            MeSfcRepair meSfcRepair = new MeSfcRepair();
            meSfcRepair.setRepairReason(repairInputDto.getRepairReason());
            meSfcRepair.setRepairMethod(repairInputDto.getRepairMethod());
            meSfcRepair.setDutyUnit(repairInputDto.getDutyUnit());
            meSfcRepair.setRemark(repairInputDto.getRemark());
            meSfcRepair.setSfc(repairInputDto.getSfc());
            meSfcRepair.setNgLogBo(dbMeSfcNcLog.getBo());
            meSfcRepair.setReplaceItemSn(repairInputDto.getReplaceItemSn());

            /**
             * ?????????????????????itemBo???????????????
             */
            meSfcRepair.setReplaceItemBo(itemBo);
            String repairBo = UUID.uuid32();

            meSfcRepair.setReplaceItemSn(repairInputDto.getReplaceItemSn());
            meSfcRepair.setNgItemSn(repairInputDto.getBadItemSn());
            meSfcRepair.setBo(repairBo);
            meSfcRepair.setSite(site);
//            meSfcRepair.setWipLogBo(uuid32Wip);
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String format = simpleDateFormat.format(new Date());
            meSfcRepair.setRepairTime(new Date());
            meSfcRepairList.add(meSfcRepair);

            /**
             * ??????me_sfc_nc_log?????? IS_RAW_CHECK ??????????????????????????????true
             */
            dbMeSfcNcLog.setIsRawCheck(true);
            meSfcNcLogService.updateById(dbMeSfcNcLog);
        }
        /**
         * ????????????
         */
        if (meSfcRepairList.size() != 0) {
            meSfcRepairService.saveBatch(meSfcRepairList);
        }

        /**
         * ???????????????????????????
         */
        RepairObjVo repairObjVo = new RepairObjVo();
        List<RepairLogListVo> repairLogListVo = meSfcRepairMapper.getRepairLogListVo(repairInputList.get(0).getSfc());
        if (CollUtil.isNotEmpty(repairLogListVo)) {
            // repairObjVo.setWipLogBo(uuid32Wip);
            repairObjVo.setRepairLogListVo(repairLogListVo);
            return repairObjVo;
        } else {
            return null;
        }
    }

    /**
     * ?????????????????????????????? ???????????? ????????????????????????
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, CommonException.class})
    public Integer scrapOrRepairFinish(ScrapOrRepairFinDto scrapOrRepairFinDto) throws CommonException {

        List<MeSfcNcLog> dbSfcNcLogs = meSfcNcLogService.list(new QueryWrapper<MeSfcNcLog>().eq("SFC", scrapOrRepairFinDto.getSfc()));
        if (null == dbSfcNcLogs || dbSfcNcLogs.isEmpty()) {
            throw new CommonException("SFC: " + scrapOrRepairFinDto.getSfc() + "NGLog????????????", CommonExceptionDefinition.BASIC_EXCEPTION);
        }

        /*
         * MeSfc state ???????????????????????????????????? ??????????????? ???????????????
         * ???????????? FS
         */
        if ("RF".equals(scrapOrRepairFinDto.getInstructions())) {// NGLog?????????????????????????????????????????????????????????????????????????????????
            List<MeSfcNcLog> dbSfcNcLogList = meSfcNcLogService.list(new QueryWrapper<MeSfcNcLog>().eq("SFC", scrapOrRepairFinDto.getSfc()));
            List<Boolean> allBoolean = dbSfcNcLogList.stream().map(MeSfcNcLog::getIsRawCheck).collect(Collectors.toList());
            List<Boolean> falseLis = allBoolean.stream().filter(e -> !e).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(falseLis)) {
                throw new CommonException("SN: " + scrapOrRepairFinDto.getSfc() + "???????????????", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
            // ????????????????????????
            return cumulativeNum();
        }
        return cumulativeNum();
    }

    public Integer cumulativeNum() throws CommonException {
        String site = UserUtils.getSite();

        List<MeSfcRepair> repairLisCount = meSfcRepairMapper.selectList(new QueryWrapper<MeSfcRepair>().select("DISTINCT (SFC)").eq("SITE", site));
        if (CollUtil.isNotEmpty(repairLisCount)) {
            return repairLisCount.size();
        } else {
            throw new CommonException("???????????????????????????", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
    }


    @Override
    public List<ProductionDefectRecord> getListBySn(String sn) {

        // ????????????????????????
        ResponseData<List<ProductionDefectRecord>> result = collectionRecordService.getDefectRecordListBySn(sn);
        if (result.isSuccess() && CollUtil.isNotEmpty(result.getData())) {
            return result.getData();
        } else {
            throw new CommonException("SFC: " + sn + "??????????????????????????????", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
    }

    @Override
    public Boolean scrapped(String sn) {

        ResponseData<List<ProductionDefectRecord>> result = collectionRecordService.getDefectRecordListBySn(sn);
        Assert.valid(!result.isSuccess() || CollUtil.isEmpty(result.getData()), "??????????????????????????????");

        Sn snInfo = snService.getSnInfo(sn);
        Assert.valid(ObjectUtil.isNull(snInfo), "?????????????????????");

        Map<String, String> map = new HashMap<>(16);
        map.put(snInfo.getBo(), SnTypeEnum.SCRAPPED.getCode());
        return snService.changeSnStateByMap(map);
    }

    @Override
    public Boolean saveTemp(String sn, List<RepairTempDto> repairTempDtos) {

        Assert.valid(StrUtil.isEmpty(sn), "sn????????????");
        Assert.valid(CollUtil.isEmpty(repairTempDtos), "??????????????????");

        //?????????????????????
        List<String> defectRecordIds = repairTempDtos.stream().map(RepairTempDto::getDefectRecordId).collect(Collectors.toList());
        ResponseData<List<ProductionDefectRecord>> result = collectionRecordService.getDefectRecordListByIds(defectRecordIds);
        Assert.valid(!result.isSuccess() || CollUtil.isEmpty(result.getData()), "????????????????????????");
        Map<String, ProductionDefectRecord> productionDefectRecordMap = new HashMap<>(16);
        productionDefectRecordMap.putAll(result.getData().stream().collect(Collectors.toMap(ProductionDefectRecord::getId, e -> e)));
        //??????????????????
        List<String> repairMethodIds = repairTempDtos.stream().map(RepairTempDto::getRepairMethodId).collect(Collectors.toList());
        Collection<MaintenanceMethod> maintenanceMethods = maintenanceMethodService.listByIds(repairMethodIds);
        Map<String, MaintenanceMethod> maintenanceMethodMap = new HashMap<>(16);
        if(CollUtil.isNotEmpty(maintenanceMethods)) {
            maintenanceMethodMap.putAll(maintenanceMethods.stream().collect(Collectors.toMap(MaintenanceMethod::getId, e -> e)));
        }

        ArrayList<RepairTempDataDTO> repairTempDataDTOS = new ArrayList<>();

        for (RepairTempDto repairTempDto : repairTempDtos) {
            String repairMethodId = repairTempDto.getRepairMethodId();
            String defectRecordId = repairTempDto.getDefectRecordId();

            RepairTempDataDTO repairTempDataDTO = new RepairTempDataDTO();
            if(maintenanceMethodMap.containsKey(repairMethodId)) {
                MaintenanceMethod maintenanceMethod = maintenanceMethodMap.get(repairMethodId);
                BeanUtils.copyProperties(maintenanceMethod, repairTempDataDTO);
            }
            if(productionDefectRecordMap.containsKey(defectRecordId)) {
                ProductionDefectRecord productionDefectRecord = productionDefectRecordMap.get(defectRecordId);
                BeanUtils.copyProperties(productionDefectRecord, repairTempDataDTO);
            }
            repairTempDataDTO.setRepairUserName(UserUtils.getUserName());
            repairTempDataDTO.setDefectRecordId(repairTempDto.getDefectRecordId());
            repairTempDataDTOS.add(repairTempDataDTO);
        }

        TemporaryData temporaryData = new TemporaryData();
        temporaryData.setSn(sn);
        temporaryData.setContent(JSONUtil.toJsonStr(repairTempDataDTOS));
        temporaryData.setStation(UserUtils.getStation());
        temporaryData.setType(TemporaryDataTypeEnum.REPAIR.getCode());
        ResponseData<Boolean> saveResult = temporaryDataService.addOrUpdate(temporaryData);
        Assert.valid(!saveResult.isSuccess(), "????????????");
        return true;
    }
}

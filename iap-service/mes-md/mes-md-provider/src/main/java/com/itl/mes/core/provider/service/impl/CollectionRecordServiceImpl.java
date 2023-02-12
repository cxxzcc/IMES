package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.attachment.client.service.DictService;
import com.itl.iap.common.base.constants.CommonConstants;
import com.itl.iap.common.base.constants.SystemDictCodeConstant;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.Assert;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.OperationHandleBO;
import com.itl.mes.core.api.bo.ShopOrderHandleBO;
import com.itl.mes.core.api.bo.SnHandleBO;
import com.itl.mes.core.api.constant.CheckResultEnum;
import com.itl.mes.core.api.constant.ProductionCollectionRecordStateEnum;
import com.itl.mes.core.api.dto.CollectionRecordCommonTempDTO;
import com.itl.mes.core.api.dto.DefCodeInfoDto;
import com.itl.mes.core.api.dto.QualitativeInspectionSaveDTO;
import com.itl.mes.core.api.dto.RepairTempDataDTO;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.*;
import com.itl.mes.core.api.vo.ShopOrderFullVo;
import com.itl.mes.core.provider.mapper.CollectionRecordMapper;
import com.itl.mom.label.api.entity.MeProductStatus;
import com.itl.mom.label.client.service.MeProductStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author houfan
 * @since 2021-11-02
 */
@Service
public class CollectionRecordServiceImpl extends ServiceImpl<CollectionRecordMapper, CollectionRecord> implements ICollectionRecordService {

    @Autowired
    private DictService dictService;
    @Autowired
    private IRecordOfProductTestService recordOfProductTestService;
    @Autowired
    private IProductionDefectRecordService productionDefectRecordService;
    @Autowired
    private ShopOrderService shopOrderService;
    @Autowired
    private MeProductStatusService meProductStatusService;
    @Autowired
    private OrderRouterService orderRouterService;
    @Autowired
    private ProductionCollectionRecordService productionCollectionRecordService;
    @Autowired
    private ProductionCollectionRecordKeyService productionCollectionRecordKeyService;
    @Autowired
    private ProductMaintenanceRecordService productMaintenanceRecordService;
    @Autowired
    private ProductMaintenanceRecordKeyService productMaintenanceRecordKeyService;

    @Override
    public Page<CollectionRecord> getPage(Map<String, Object> params) {
        if(StrUtil.isBlank((String)params.get("site"))) {
            params.put("site", UserUtils.getSite());
        }
        Page<CollectionRecord> queryPage = new QueryPage<>(params);
        List<CollectionRecord> list = baseMapper.getPage(queryPage, params);
        Map<String, String> orderTypeMap = new HashMap<>();
        ResponseData<Map<String, String>> dictItemMapByParentCode = dictService.getDictItemMapByParentCode(SystemDictCodeConstant.PRODUCTION_ORDER_TYPE);
        if(dictItemMapByParentCode.isSuccess()) {
            Map<String, String> map = dictItemMapByParentCode.getData();
            if(CollUtil.isNotEmpty(map)) {
                orderTypeMap.putAll(map);
            }
        }
        if(CollUtil.isNotEmpty(list)) {
            list.forEach(e -> {
                if(orderTypeMap.containsKey(e.getWorkOrderType())) {
                    e.setWorkOrderTypeDesc(orderTypeMap.get(e.getWorkOrderType()));
                }
            });
        }
        queryPage.setRecords(list);
        return queryPage;
    }

    @Override
    public String saveTempQualitativeInspection(List<QualitativeInspectionSaveDTO> list, String shopOrder, String site) {
        Assert.valid(CollUtil.isEmpty(list), "暂存数据列表不能为空");

        String sn = null;
        Map<String, CollectionRecord> map = new HashMap<>(16);
        Map<String, String> idMap = new HashMap<>(16);
        Map<String, String> productionCollectionRecordIdMap = new HashMap<>(16);

        Date now = new Date();

        List<ProductionCollectionRecord> productionCollectionRecords = new ArrayList<>();
        List<RecordOfProductTest> recordOfProductTests = new ArrayList<>();
        List<ProductionDefectRecord> productionDefectRecords = new ArrayList<>();
        List<String> hasNgResultSn = list.stream().filter(e -> StrUtil.equals(e.getResult(), CheckResultEnum.NG.getCode())).map(QualitativeInspectionSaveDTO::getSn).collect(Collectors.toList());

        for (QualitativeInspectionSaveDTO qualitativeInspectionSaveDTO : list) {
            if(sn == null) {
                sn = qualitativeInspectionSaveDTO.getSn();
            }
            CollectionRecord collectionRecord = null;
            if(map.containsKey(sn)) {
                collectionRecord = map.get(sn);
            } else {
                collectionRecord = getBySnAndShopOrder(sn, shopOrder, site);
            }

            //根据sn和shopOrder查询是否存在采集记录
            String collectionRecordId = null, productionCollectionRecordId = null;
            CollectionRecord collectionRecordEntity = generateCollectionRecord(qualitativeInspectionSaveDTO.getSn(), shopOrder, site);
            if(!map.containsKey(sn)) {
                if(collectionRecord == null) {
                    //不存在，新增采集记录
                    collectionRecordEntity.setWorkshop(qualitativeInspectionSaveDTO.getWorkShop());
                    collectionRecordEntity.setCreateTime(now);
                    collectionRecordEntity.setCreateUser(UserUtils.getUserName());
                    save(collectionRecordEntity);
                    collectionRecordId = collectionRecordEntity.getId();
                } else {
                    //存在，更新 当前工序， 下工序， 是否已完工
                    CollectionRecord updateEntity = new CollectionRecord();
                    updateEntity.setId(collectionRecord.getId());
                    updateEntity.setCurrentProcess(collectionRecordEntity.getCurrentProcess());
                    updateEntity.setNextProcess(collectionRecordEntity.getNextProcess());
                    updateEntity.setComplete(collectionRecordEntity.getComplete());
                    collectionRecordEntity.setUpdateTime(now);
                    collectionRecordEntity.setUpdateUser(UserUtils.getUserName());
                    updateById(updateEntity);
                    collectionRecordId = updateEntity.getId();
                }
                //新增保存 生产采集记录
                ProductionCollectionRecord productionCollectionRecord = new ProductionCollectionRecord();
                productionCollectionRecord.setCollectionRecordId(collectionRecordId);
                productionCollectionRecord.setProductionLine(collectionRecordEntity.getProductionLine());
                productionCollectionRecord.setProcess(collectionRecordEntity.getCurrentProcess());
                productionCollectionRecord.setResult(hasNgResultSn.contains(sn) ? CheckResultEnum.NG.getCode() : CheckResultEnum.OK.getCode());
                productionCollectionRecord.setStation(qualitativeInspectionSaveDTO.getStation());
                productionCollectionRecord.setClasses(collectionRecordEntity.getShiftName());
                productionCollectionRecord.setClassesId(collectionRecordEntity.getShiftId());
                productionCollectionRecord.setOperationTime(now);
                productionCollectionRecord.setState(ProductionCollectionRecordStateEnum.complete.getCode());
                productionCollectionRecordService.save(productionCollectionRecord);

                idMap.put(sn, collectionRecordId);
                productionCollectionRecordIdMap.put(sn, productionCollectionRecord.getId());
            }
            if(StrUtil.isBlank(collectionRecordId)) {
                collectionRecordId = idMap.get(sn);
            }
            if(StrUtil.isBlank(productionCollectionRecordId)) {
                productionCollectionRecordId = productionCollectionRecordIdMap.get(sn);
            }

            //新增保存 产品检验记录
            RecordOfProductTest recordOfProductTest = new RecordOfProductTest();
            recordOfProductTest.setId(null);
            recordOfProductTest.setCollectionRecordId(collectionRecordId);
            recordOfProductTest.setProductionCollectionRecordId(productionCollectionRecordId);
            recordOfProductTest.setProjectName(qualitativeInspectionSaveDTO.getProjectName());
            recordOfProductTest.setProjectId(qualitativeInspectionSaveDTO.getProjectId());
            recordOfProductTest.setLowerLimit(qualitativeInspectionSaveDTO.getLowerLimit());
            recordOfProductTest.setUppperLimit(qualitativeInspectionSaveDTO.getUpperLimit());
            recordOfProductTest.setTest(qualitativeInspectionSaveDTO.getTest());
            recordOfProductTest.setResult(qualitativeInspectionSaveDTO.getResult());
            recordOfProductTest.setSurveyor(qualitativeInspectionSaveDTO.getSurveyor());
            recordOfProductTest.setProcess(collectionRecordEntity.getCurrentProcess());
            recordOfProductTest.setStation(qualitativeInspectionSaveDTO.getStation());
            recordOfProductTest.setCreateTime(now);
            recordOfProductTest.setCreateUser(UserUtils.getUserName());
            recordOfProductTests.add(recordOfProductTest);

            //新增保存 产品缺陷记录
            List<DefCodeInfoDto> defCodeList = qualitativeInspectionSaveDTO.getDefCodeList();
            if(CollUtil.isNotEmpty(defCodeList)) {
                for (DefCodeInfoDto defCodeInfoDto : defCodeList) {
                    ProductionDefectRecord productionDefectRecord = new ProductionDefectRecord();
                    productionDefectRecord.setId(null);
                    productionDefectRecord.setCollectionRecordId(collectionRecordId);
                    productionDefectRecord.setProcess(collectionRecordEntity.getCurrentProcess());
                    productionDefectRecord.setDefectRecords(defCodeInfoDto.getDefectRecord());
                    productionDefectRecord.setDefectCode(defCodeInfoDto.getDefectCode());
                    productionDefectRecord.setDefectDescription(defCodeInfoDto.getDefectDescription());
                    productionDefectRecord.setDescriptionOfInspectionItems(defCodeInfoDto.getDescriptionOfInspectionItems());
                    productionDefectRecord.setStation(qualitativeInspectionSaveDTO.getStation());
                    productionDefectRecord.setCreateTime(now);
                    productionDefectRecord.setCreateUser(UserUtils.getUserName());

                    productionDefectRecords.add(productionDefectRecord);
                }
            }
            map.put(sn, collectionRecord);
        }
        if(CollUtil.isNotEmpty(recordOfProductTests)) {
            recordOfProductTestService.saveBatch(recordOfProductTests);
        }
        if(CollUtil.isNotEmpty(productionDefectRecords)) {
            productionDefectRecordService.saveBatch(productionDefectRecords);
        }
        long count = recordOfProductTests.stream().filter(e -> StrUtil.equals(e.getResult(), CheckResultEnum.OK.getCode())).count();
        if(count > 0 && count == recordOfProductTests.size()) {
            return CheckResultEnum.OK.getCode();
        }
        return CheckResultEnum.NG.getCode();
    }

    /**
     * 生成采集记录信息
     * @param sn 条码
     * @param shopOrder 工单编号
     * @param site 工厂
     * @return 采集记录
     * */
    private CollectionRecord generateCollectionRecord(String sn, String shopOrder, String site) {
        CollectionRecord collectionRecord = new CollectionRecord();
        collectionRecord.setBarCode(sn);
        collectionRecord.setWorkOrderNumber(shopOrder);
        collectionRecord.setHold(CommonConstants.NUM_ZERO);
        ShopOrderFullVo shopFullOrder = shopOrderService.getShopFullOrder(new ShopOrderHandleBO(site, shopOrder));
        collectionRecord.setShiftName(shopFullOrder.getShiftName());
        collectionRecord.setShiftId(shopFullOrder.getShiftBo());
        collectionRecord.setWorkOrderType(shopFullOrder.getShopOrderType());
        BigDecimal orderQty = shopFullOrder.getOrderQty();
        collectionRecord.setWorkCount(orderQty == null ? null : orderQty.intValue());
        OrderRouter orderRouter = orderRouterService.getOrderRouter(shopFullOrder.getBo());
        collectionRecord.setProcessName(orderRouter.getRouterName());
        collectionRecord.setProcessId(orderRouter.getBo());
        collectionRecord.setProductionLine(shopFullOrder.getProductLine());
        collectionRecord.setComplete(CommonConstants.NUM_ZERO);
        ResponseData<MeProductStatus> result = meProductStatusService.getBySn(sn, site);
        if(result.isSuccess()) {
            MeProductStatus data = result.getData();
            if(data != null) {
                collectionRecord.setCurrentProcess(data.getCurrentOperation());
                collectionRecord.setNextProcess(data.getNextOperation());
                collectionRecord.setProductName(data.getProductName());
                if(CommonConstants.NUM_ONE.equals(data.getDone())) {
                    collectionRecord.setComplete(CommonConstants.NUM_ONE);
                }
            }
        }
        collectionRecord.setProductCode(shopFullOrder.getItem());

        collectionRecord.setSite(site);

        return collectionRecord;
    }

    private CollectionRecord getBySnAndShopOrder(String sn, String shopOrder, String site) {
        return lambdaQuery().eq(CollectionRecord::getSite, site)
                .eq(CollectionRecord::getBarCode, sn).eq(CollectionRecord::getWorkOrderNumber, shopOrder).one();
    }


    @Override
    public Boolean saveProductMaintenanceRecord(List<RepairTempDataDTO> repairTempDataDTOS) {
        if(CollUtil.isEmpty(repairTempDataDTOS)) {
            return false;
        }
        RepairTempDataDTO repairTempDataDTO1 = repairTempDataDTOS.get(0);
        CollectionRecord collectionRecord = getById(repairTempDataDTO1.getCollectionRecordId());

        CollectionRecordCommonTempDTO collectionRecordCommonTempDTO = new CollectionRecordCommonTempDTO();
        collectionRecordCommonTempDTO.setSite(repairTempDataDTO1.getSite());
        collectionRecordCommonTempDTO.setStation(repairTempDataDTO1.getStation());
        collectionRecordCommonTempDTO.setSn(collectionRecord.getBarCode());
        collectionRecordCommonTempDTO.setShopOrder(collectionRecord.getWorkOrderNumber());
        collectionRecordCommonTempDTO.setWorkShop(collectionRecord.getWorkshop());

        List<ProductMaintenanceRecord> productMaintenanceRecords = new ArrayList<>();
        Date now = new Date();
        for (RepairTempDataDTO repairTempDataDTO : repairTempDataDTOS) {
            ProductMaintenanceRecord productMaintenanceRecord = new ProductMaintenanceRecord();
            productMaintenanceRecord.setId(null);
            productMaintenanceRecord.setCollectionRecordId(repairTempDataDTO.getCollectionRecordId());
            productMaintenanceRecord.setClasses(repairTempDataDTO.getShiftName());
            productMaintenanceRecord.setProductionLine(repairTempDataDTO.getProductionLine());
            productMaintenanceRecord.setCreateTime(now);
            productMaintenanceRecord.setCreateUser(repairTempDataDTO.getRepairUserName());
            productMaintenanceRecord.setRepairTime(now);
            productMaintenanceRecord.setDefectCode(repairTempDataDTO.getDefectCode());
            productMaintenanceRecord.setRepairUser(repairTempDataDTO.getRepairUserName());

            ProductMaintenanceMethodRecord productMaintenanceMethodRecord = new ProductMaintenanceMethodRecord();
            productMaintenanceMethodRecord.setId(null);
            productMaintenanceMethodRecord.setMaintenanceCode(repairTempDataDTO.getCode());
            productMaintenanceMethodRecord.setMaintenanceName(repairTempDataDTO.getTitle());
            productMaintenanceMethodRecord.setMaintenanceDesc(repairTempDataDTO.getDescription());
            productMaintenanceRecord.setProductMaintenanceMethodRecords(Arrays.asList(productMaintenanceMethodRecord));

            productMaintenanceRecords.add(productMaintenanceRecord);
        }
        collectionRecordCommonTempDTO.setProductMaintenanceRecords(productMaintenanceRecords);
        //更新不合格记录为已处理
        List<String> defectRecordIds = repairTempDataDTOS.stream().map(RepairTempDataDTO::getDefectRecordId).collect(Collectors.toList());
        productionDefectRecordService.updateHandleFlag(defectRecordIds);
        return saveByCommon(collectionRecordCommonTempDTO);
    }

    @Override
    @Transactional
    public Boolean saveByCommon(CollectionRecordCommonTempDTO repairTempDataDTOS) {

        String sn = repairTempDataDTOS.getSn();
        Assert.valid(StrUtil.isBlank(sn), "sn不能为空");
        String shopOrder = repairTempDataDTOS.getShopOrder();
        Assert.valid(StrUtil.isBlank(shopOrder), "工单编号不能为空");
        String site = repairTempDataDTOS.getSite();
        Assert.valid(StrUtil.isBlank(site), "工厂不能为空");
        String workShop = repairTempDataDTOS.getWorkShop();
        String station = repairTempDataDTOS.getStation();
        Assert.valid(StrUtil.isBlank(station), "工位不能为空");
        Date now = new Date();

        CollectionRecord collectionRecord = getBySnAndShopOrder(sn, shopOrder, site);
        CollectionRecord collectionRecordEntity = generateCollectionRecord(sn, shopOrder, site);
        //采集记录id
        String collectionRecordId = null;

        //保存采集记录
        if(collectionRecord == null) {
            //不存在，新增采集记录
            collectionRecordEntity.setWorkshop(workShop);
            collectionRecordEntity.setCreateTime(now);
            collectionRecordEntity.setCreateUser(UserUtils.getUserName());
            save(collectionRecordEntity);
            collectionRecordId = collectionRecordEntity.getId();
        } else {
            //存在，更新 当前工序， 下工序， 是否已完工
            CollectionRecord updateEntity = new CollectionRecord();
            updateEntity.setId(collectionRecord.getId());
            updateEntity.setCurrentProcess(collectionRecordEntity.getCurrentProcess());
            updateEntity.setNextProcess(collectionRecordEntity.getNextProcess());
            updateEntity.setComplete(collectionRecordEntity.getComplete());
            collectionRecordEntity.setUpdateTime(now);
            collectionRecordEntity.setUpdateUser(UserUtils.getUserName());
            updateById(updateEntity);
            collectionRecordId = updateEntity.getId();
        }

        //保存生产采集记录
        ProductionCollectionRecord productionCollectionRecord = repairTempDataDTOS.getProductionCollectionRecord();
        if(productionCollectionRecord == null) {
            productionCollectionRecord = new ProductionCollectionRecord();
        }
        if(StrUtil.isBlank(productionCollectionRecord.getProductionLine())) {
            productionCollectionRecord.setProductionLine(collectionRecordEntity.getProductionLine());
        }
        if(StrUtil.isBlank(productionCollectionRecord.getProcess())) {
            productionCollectionRecord.setProcess(collectionRecordEntity.getCurrentProcess());
        }
        if(StrUtil.isBlank(productionCollectionRecord.getStation())) {
            productionCollectionRecord.setStation(station);
        }
        if(StrUtil.isBlank(productionCollectionRecord.getClasses())) {
            productionCollectionRecord.setClasses(collectionRecordEntity.getShiftName());
        }
        if(StrUtil.isBlank(productionCollectionRecord.getClassesId())) {
            productionCollectionRecord.setClassesId(collectionRecordEntity.getShiftId());
        }
        if(productionCollectionRecord.getOperationTime() == null) {
            productionCollectionRecord.setOperationTime(now);
        }
        if(productionCollectionRecord.getState() == null) {
            productionCollectionRecord.setState(ProductionCollectionRecordStateEnum.complete.getCode());
        }
        productionCollectionRecord.setCollectionRecordId(collectionRecordId);
        productionCollectionRecord.setId(null);
        productionCollectionRecordService.save(productionCollectionRecord);
        //保存关键件记录
        List<ProductionCollectionRecordKey> productionCollectionRecordKeys = productionCollectionRecord.getProductionCollectionRecordKeys();
        if(CollUtil.isNotEmpty(productionCollectionRecordKeys)) {
            for (ProductionCollectionRecordKey productionCollectionRecordKey : productionCollectionRecordKeys) {
                productionCollectionRecordKey.setId(null);
                productionCollectionRecordKey.setProductionCollectionRecordId(productionCollectionRecord.getId());
                if(productionCollectionRecordKey.getOperationTime() == null) {
                    productionCollectionRecordKey.setOperationTime(now);
                }
            }
            productionCollectionRecordKeyService.saveBatch(productionCollectionRecordKeys);
        }
        //产品维修记录
        List<ProductMaintenanceRecord> productMaintenanceRecords = repairTempDataDTOS.getProductMaintenanceRecords();
        if(CollUtil.isNotEmpty(productMaintenanceRecords)) {
            for (ProductMaintenanceRecord productMaintenanceRecord : productMaintenanceRecords) {
                productMaintenanceRecord.setId(null);
                productMaintenanceRecord.setCollectionRecordId(collectionRecordId);
                productMaintenanceRecord.setRepairTime(now);
                productMaintenanceRecord.setCreateTime(now);
                productMaintenanceRecord.setCreateUser(repairTempDataDTOS.getUserName());
                productMaintenanceRecord.setRepairUser(repairTempDataDTOS.getUserName());
                productMaintenanceRecordService.save(productMaintenanceRecord);
                List<ProductMaintenanceMethodRecord> productMaintenanceMethodRecords = productMaintenanceRecord.getProductMaintenanceMethodRecords();
                if(CollUtil.isNotEmpty(productMaintenanceMethodRecords)) {
                    for (ProductMaintenanceMethodRecord productMaintenanceMethodRecord : productMaintenanceMethodRecords) {
                        productMaintenanceMethodRecord.setId(null);
                        productMaintenanceMethodRecord.setProductionMaintenanceRecordId(productMaintenanceRecord.getId());
                    }
                    productMaintenanceRecordKeyService.saveBatch(productMaintenanceMethodRecords);
                }
            }
        }

        return true;
    }

    @Override
    @Transactional
    public Boolean saveByCommons(List<CollectionRecordCommonTempDTO> repairTempDataDTOS) {
        if(CollUtil.isEmpty(repairTempDataDTOS)) {
            return false;
        }
        repairTempDataDTOS.forEach(e -> {
            saveByCommon(e);
        });
        return true;
    }

    public void updateCollectionRecord(String site,String sn){
         baseMapper.updateBySnAndSite(site,sn);
    }

    @Override
    public Boolean updateNextOperation(List<String> snBos, String operationBo, String operationName) {

        if(CollUtil.isEmpty(snBos)) {
            return null;
        }

        List<String> sns = new ArrayList<>();
        String site = null;
        for (String snBo : snBos) {
            SnHandleBO snHandleBO = new SnHandleBO(snBo);
            sns.add(snHandleBO.getSn());
            if(StrUtil.isBlank(site)) {
                site = snHandleBO.getSite();
            }
        }

        List<CollectionRecord> collectionRecords = lambdaQuery().eq(CollectionRecord::getSite, site)
                .in(CollectionRecord::getBarCode, sns)
                .list();

        if(CollUtil.isEmpty(collectionRecords)) {
            return false;
        }

        List<CollectionRecord> updateList = new ArrayList<>();
        for (CollectionRecord item : collectionRecords) {
            CollectionRecord collectionRecord = new CollectionRecord();
            collectionRecord.setId(item.getId());
            collectionRecord.setNextProcess(new OperationHandleBO(operationBo).getOperation());
            updateList.add(collectionRecord);
        }
        return updateBatchById(updateList);
    }

}

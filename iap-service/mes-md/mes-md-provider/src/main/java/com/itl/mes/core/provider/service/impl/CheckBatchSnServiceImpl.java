package com.itl.mes.core.provider.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.util.UUID;
import com.itl.mes.core.api.constant.ProductionCollectionRecordStateEnum;
import com.itl.mes.core.api.constant.TemporaryDataTypeEnum;
import com.itl.mes.core.api.dto.*;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.CheckBatchSnService;
import com.itl.mes.core.api.vo.CheckBatchSnBomVo;
import com.itl.mes.core.api.vo.CheckBatchSnVo;
import com.itl.mes.core.api.vo.ItemFullVo;
import com.itl.mes.core.client.service.MomLabelService;
import com.itl.mes.core.provider.mapper.*;
import com.itl.mes.core.provider.utils.JSONUtils;
import com.itl.mes.me.api.entity.LabelPrint;
import com.itl.mom.label.api.vo.ItemLabelListVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author GKL
 * @date 2021/11/26 - 17:01
 * @since 2021/11/26 - 17:01 星期五 by GKL
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CheckBatchSnServiceImpl extends ServiceImpl<SnMapper, Sn> implements CheckBatchSnService {
    /**
     * 工位表Mapper
     */
    private final StationMapper stationMapper;
    /**
     * 物料标签表对应Mapper
     */
    private final MomLabelService momLabelService;
    /**
     * 工艺路线
     */
    private final OrderRouterMapper orderRouterMapper;
    /**
     * 工艺路线图
     */
    private final OrderRouterProcessMapper orderRouterProcessMapper;
    /**
     * 工单表Mapper
     */
    private final ShopOrderMapper shopOrderMapper;
    /**
     * 工序Mapper
     */
    private final OperationMapper operationMapper;
    /**
     * 工单bom组件表
     */

    private final ShopOrderBomComponnetMapper shopOrderBomComponnetMapper;
    /**
     * 暂存表中数据
     */
    private final TemporaryDataMapper temporaryDataMapper;
    /**
     * 物料表对应Mapper
     */
    private final ItemMapper itemMapper;
    @Autowired
    private CommonBrowserServiceImpl commonBrowserService;
    @Autowired
    private ItemServiceImpl itemService;
    @Autowired
    private TemporaryDataServiceImpl temporaryDataService;
    private static final String PRESTATE = "STATE:";
    private static final String BOM_ITEM_SN = "bomItemSn";
    private static final String OPERATION_CODE_NAME = "operation";
    private static final String BATCH_NO_NAME = "batchNoName";
    private static final String PRODUCT_BOM_IS_NULL = "产品条码对应的bom清单数据为空";

    private static final String USED_STATUS = "used";


    /**
     * 校验产品批次
     *
     * @param dto sn,工位,工厂
     * @return ResponseData.class
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    @Override
    public ResponseData<CheckBatchSnVo> checkBatchSn(CheckBatchSnDto dto) {
        //1.判断sn不能为空
        String sn = dto.getSn();
        if (StringUtils.isEmpty(sn)) {
            throw new CommonException("条码不能为空，请重新输入", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        String site = dto.getSite();
        site = StringUtils.isEmpty(site) ? UserUtils.getSite() : site;
        //获取到用户信息的工位
        String stationDto = dto.getStation();
        stationDto = StringUtils.isEmpty(stationDto) ? UserUtils.getStation() : stationDto;
        //工位去查对应的工位表找到工序bo
        Station stations = getStation(stationDto, site);
        String operationBo = stations.getOperationBo();
        Sn oldSn = toGetSn(sn, site);
        if (oldSn == null) {
            return ResponseData.error("产品条码不存在，请重新输入");
        }
      /*  if (USED_STATUS.equals(oldSn.getState())) {
            return ResponseData.error("产品条码已上料");
        }*/
        String shopOrder;
        shopOrder = toGetShopOrderNo(oldSn);
        if (StringUtils.isEmpty(shopOrder)) {
            return ResponseData.error("该条码没有属于的工单编码，请重新输入");
        }
        //先查询表中存在
        CheckBatchSnVo checkBatchSnVo = new CheckBatchSnVo();
        List<TemporaryData> temporaryData1 = getTemporaryDataBySnAndType(oldSn.getBo(), BATCH_NO_NAME);
        if (Boolean.FALSE.equals(CollectionUtils.isEmpty(temporaryData1))) {
            String content = temporaryData1.get(0).getContent();
            checkBatchSnVo = JSONUtils.jsonToPojo(content, CheckBatchSnVo.class);
            return ResponseData.success(checkBatchSnVo);
        }
        //通过工单号去查询工单表查询状态是在生产中a)statusBo =503是生产中 状态值 STATE:1040,505需要切割
        ShopOrder shopOrders = getShopOrders(site, shopOrder);
        String processInfo = getProcessInfoByRouterProcess(shopOrders.getBo());
        extracted(operationBo, processInfo);
        //获取到bomBo
        String bo = shopOrders.getBo();
        //zsType :S
        //根据工单bo,类型,工序bo,追溯方式拿到所有数据  shopOrderBomComponentQuery
        LambdaQueryWrapper<ShopOrderBomComponnet> shopOrderBomComponentQuery = new QueryWrapper<ShopOrderBomComponnet>().lambda()
                .eq(ShopOrderBomComponnet::getShopOrderBo, bo)
                .eq(ShopOrderBomComponnet::getOperationBo, operationBo)
                .eq(ShopOrderBomComponnet::getType, "OP")
                .eq(ShopOrderBomComponnet::getZsType, "S");
        List<ShopOrderBomComponnet> shopOrderBomComponents = shopOrderBomComponnetMapper.selectList(shopOrderBomComponentQuery);
        if (CollectionUtils.isEmpty(shopOrderBomComponents)) {
            throw new CommonException("该工位的工序BOM为空，请检查工单-工序BOM", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        String finalSite = site;
        List<CheckBatchSnBomVo> batchSnBomVos = shopOrderBomComponents.stream().map(x -> {
            CheckBatchSnBomVo batchSnBomVo = new CheckBatchSnBomVo();
            batchSnBomVo.setBomCount(x.getQty());
            batchSnBomVo.setLoadedCount(new BigDecimal(0));
            String componentBo = x.getComponentBo();
            //根据bo去查询物料表
            Item items = getItems(finalSite, componentBo);
            batchSnBomVo.setItemBo(componentBo);
            batchSnBomVo.setShopOrderBo(shopOrders.getBo());
            batchSnBomVo.setShopOrder(shopOrders.getShopOrder());
            batchSnBomVo.setItemBom(items.getItem());
            String operationBo1 = x.getOperationBo();
            Operation operations1 = getOperations(operationBo1);
            batchSnBomVo.setOperation(operations1.getOperationName());
            batchSnBomVo.setShopOrderBomComponentBo(x.getBo());
            batchSnBomVo.setVirtualItem(x.getVirtualItem());
            batchSnBomVo.setZsType("批次");
            batchSnBomVo.setComponentPosition(x.getComponentPosition());
            batchSnBomVo.setItemOrder(x.getItemOrder());
            batchSnBomVo.setItemType(x.getItemType());
            return batchSnBomVo;
        }).collect(Collectors.toList());
        checkBatchSnVo.setSnBo(oldSn.getBo());
        checkBatchSnVo.setItemBomList(batchSnBomVos);

        String s = JSONUtils.objectToJson(checkBatchSnVo);
        TemporaryData temporaryData = new TemporaryData();

        temporaryData.setSn(oldSn.getBo());
        temporaryData.setContent(s);
        temporaryData.setCreateTime(new Date());
        temporaryData.setType(BATCH_NO_NAME);
        temporaryData.setCreateUser(UserUtils.getUserId());
        temporaryDataMapper.insert(temporaryData);
        return ResponseData.success(checkBatchSnVo);
    }

    private Sn toGetSn(String sn, String site) {
        LambdaQueryWrapper<Sn> queryWrapper = new QueryWrapper<Sn>().lambda()
                .eq(Sn::getSn, sn)
                //   .ne(Sn::getState, USED_STATUS)
                .eq(Sn::getSite, site);
        //条码表中获取是否存在sn
        return this.getOne(queryWrapper);
    }


    /**
     * 校验物料条码上料,下料修改状态移出数据
     *
     * @param checkBomItemSn checkBomItemSn
     * @return ResponseData.class
     */
    @Override
    public ResponseData<Boolean> checkBomItemSn(CheckBomItemSn checkBomItemSn) {
        //第一步获取
        String station = checkBomItemSn.getStation();
        station = StringUtils.isEmpty(station) ? UserUtils.getStation() : station;
        //工厂
        String site = checkBomItemSn.getSite();
        site = StringUtils.isEmpty(site) ? UserUtils.getSite() : site;
        List<TemporaryData> bomItemSn = getTemporaryDataBySnAndType(station, BOM_ITEM_SN);
        TemporaryData temporaryData = new TemporaryData();
        if (Boolean.FALSE.equals(CollectionUtils.isEmpty(bomItemSn))) {
            temporaryData = bomItemSn.get(0);
        }
        String id = temporaryData.getId();

        //物料条码
        String itemSn = checkBomItemSn.getItemSn();
        if (StringUtils.isEmpty(itemSn)) {
            throw new CommonException("物料条码不能为空", CommonExceptionDefinition.BASIC_EXCEPTION);
        }


        //通过工位去获取对应的条码信息

        Integer state = checkBomItemSn.getState();

        if (state.equals(1)) {
            //下料,移出去
            unUsedItemSn(itemSn, temporaryData, id);
        } else if (state.equals(0)) {
            //上料,加进去,修改状态
            //查询物料标签的状态是否是挂起的
            ItemLabelListVo itemLabelListVo = baseMapper.selectSnBySn(itemSn);
            if (null == itemLabelListVo) {
                throw new CommonException("物料条码不存在", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
            String sfgq = itemLabelListVo.getSfgq();
            if ("Y".equals(sfgq)) {
                throw new CommonException("物料条码挂起状态不能上料", CommonExceptionDefinition.BASIC_EXCEPTION);
            }

            BigDecimal sysl = itemLabelListVo.getSysl();
            if (sysl.compareTo(BigDecimal.valueOf(0)) <= 0) {
                throw new CommonException("剩余数量为0，此物料条码无效", CommonExceptionDefinition.BASIC_EXCEPTION);
            }

            usedItemSn(site, itemSn, temporaryData, id, station, itemLabelListVo);
        } else {
            throw new CommonException("状态数据有误", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return ResponseData.success();
    }

    /**
     * 通过工位获取所有的数据
     *
     * @param station station
     * @return ResponseData.class
     */
    @Override
    public ResponseData<BomItemSnByStation> getUsedListByStation(String station) {
        List<TemporaryData> bomItemSn = getTemporaryDataBySnAndType(station, BOM_ITEM_SN);
        TemporaryData temporaryData = new TemporaryData();
        if (Boolean.FALSE.equals(CollectionUtils.isEmpty(bomItemSn))) {
            temporaryData = bomItemSn.get(0);
        }
        String id = temporaryData.getId();
        if (StringUtils.isEmpty(id)) {
            return ResponseData.success();
        }
        String content = temporaryData.getContent();
        if (StringUtils.isEmpty(content)) {
            return ResponseData.success();
        }
        BomItemSnByStation bomItemSnByStation = JSONUtils.jsonToPojo(content, BomItemSnByStation.class);

        return ResponseData.success(bomItemSnByStation);
    }

    @Override
    public ResponseData<Boolean> checkSnBomAndStation(CheckBomItemAndStationSn checkBomItemAndStationSn) {
        //第一步获取
        String station = checkBomItemAndStationSn.getStation();
        station = StringUtils.isEmpty(station) ? UserUtils.getStation() : station;

        List<TemporaryData> bomItemSn = getTemporaryDataBySnAndType(station, BOM_ITEM_SN);
        TemporaryData temporaryData = new TemporaryData();
        if (Boolean.FALSE.equals(CollectionUtils.isEmpty(bomItemSn))) {
            temporaryData = bomItemSn.get(0);
        }
        String id = temporaryData.getId();
        //获取产品对应的bom
        String snBo = checkBomItemAndStationSn.getSnBo();
        if (StringUtils.isEmpty(snBo)) {
            throw new CommonException("产品条码bo不能为空", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        ResponseData<Boolean> responseData = getBooleanResponseData(temporaryData, id, checkBomItemAndStationSn);
        String code = responseData.getCode();
        if ("203".equals(code)) {
            return responseData;
        }

        return ResponseData.success();
    }

    private ResponseData<Boolean> getBooleanResponseData(TemporaryData temporaryData, String id, CheckBomItemAndStationSn checkBomItemAndStationSn) {
        List<TemporaryData> temporaryDataBySnAndType = getTemporaryDataBySnAndType(checkBomItemAndStationSn.getSnBo(), BATCH_NO_NAME);
        if (CollectionUtils.isEmpty(temporaryDataBySnAndType)) {
            throw new CommonException("获取产品条码对应数据为空", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        //产品条码对应的数量
        TemporaryData temporaryData1 = temporaryDataBySnAndType.get(0);
        String content = temporaryData1.getContent();
        if (StringUtils.isEmpty(content)) {
            throw new CommonException(PRODUCT_BOM_IS_NULL, CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        CheckBatchSnVo checkBatchSnVo = JSONUtils.jsonToPojo(content, CheckBatchSnVo.class);
        if (null == checkBatchSnVo) {
            throw new CommonException(PRODUCT_BOM_IS_NULL, CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        List<CheckBatchSnBomVo> itemBomList = checkBatchSnVo.getItemBomList();
        if (CollectionUtils.isEmpty(itemBomList)) {
            throw new CommonException(PRODUCT_BOM_IS_NULL, CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        BigDecimal bomCountAll = itemBomList.stream().map(CheckBatchSnBomVo::getBomCount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal loadedCountAll = itemBomList.stream().map(CheckBatchSnBomVo::getLoadedCount).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (bomCountAll.compareTo(loadedCountAll) == 0) {
            return ResponseData.success();
        }
        String snBo = checkBomItemAndStationSn.getSnBo();

        if (temporaryData == null || StringUtils.isEmpty(id)) {
            String site = checkBomItemAndStationSn.getSite();
            site = StringUtils.isEmpty(site) ? UserUtils.getSite() : site;
            Sn sn = querySn(site, snBo);
            StringBuilder stringBuffer = new StringBuilder();
            for (CheckBatchSnBomVo checkBatchSnBomVo : itemBomList
            ) {
                stringBuffer.append(checkBatchSnBomVo.getItemBom() + "物料缺" + checkBatchSnBomVo.getBomCount() + ";");
            }
            String s = stringBuffer.toString();
            if (StringUtils.isNotBlank(s)) {
                return ResponseData.error("203", "制作产品[" + sn.getSn() + "]缺料:" + s + ",请点击“上料”按钮，先上料完整。");
            }

        }
        String content1 = temporaryData.getContent();
        BomItemSnByStation bomItemSnByStation = JSONUtils.jsonToPojo(content1, BomItemSnByStation.class);
        if (null == bomItemSnByStation) {
            throw new CommonException("该工位没有对应的已上料清单", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        //转换出来
        List<UsedListDto> usedList = bomItemSnByStation.getUsedList();
        usedList = usedList.stream().sorted(Comparator.comparing(UsedListDto::getDate)).collect(Collectors.toList());
        //耗尽状态需要修改的条码
        List<String> list = new ArrayList<>();
        //产品条码对应的数据
        //采集记录中记录的数据
        ResponseData<Boolean> responseData = toChangeTemporaryData(temporaryData, temporaryData1, checkBatchSnVo, bomItemSnByStation, usedList, list, checkBomItemAndStationSn);
        String code = responseData.getCode();
        if ("203".equals(code)) {
            return responseData;
        }

        if (Boolean.FALSE.equals(CollectionUtils.isEmpty(list))) {
            baseMapper.updateStatusBySnList("DRAINED", list);
            //list查询对应的数据
            LambdaQueryWrapper<Sn> queryWrapper = new QueryWrapper<Sn>().lambda()
                    .in(Sn::getSn, list);
            List<Sn> snList = this.list(queryWrapper);
            List<String> boList = snList.stream().map(Sn::getBo).collect(Collectors.toList());
            return momLabelService.updateSyslByBoList(boList);
        }

        return ResponseData.success();
    }

    private TemporaryData getSnTempraryData(String snBo) {
        LambdaQueryWrapper<Sn> snLambdaQueryWrapper1 = new QueryWrapper<Sn>().lambda()
                .eq(Sn::getBo, snBo);

        Sn sn1 = baseMapper.selectOne(snLambdaQueryWrapper1);
        if (sn1 == null) {
            throw new CommonException("该产品条码不存在", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        String snCode = sn1.getSn();
        List<TemporaryData> temporaryDataBySnAndType = getTemporaryDataBySnAndType(snCode, TemporaryDataTypeEnum.BATCHBARCODE.getCode());
        TemporaryData temporaryDataNow = new TemporaryData();
        if (Boolean.FALSE.equals(CollectionUtils.isEmpty(temporaryDataBySnAndType))) {
            temporaryDataNow = temporaryDataBySnAndType.get(0);
        }
        return temporaryDataNow;
    }

    private ResponseData<Boolean> toChangeTemporaryData(TemporaryData temporaryData, TemporaryData temporaryData1, CheckBatchSnVo checkBatchSnVo, BomItemSnByStation bomItemSnByStation, List<UsedListDto> usedList, List<String> list, CheckBomItemAndStationSn checkBomItemAndStationSn) {
        //通过snBo获取对应组件的数据
        String station = checkBomItemAndStationSn.getStation();
        station = StringUtils.isEmpty(station) ? UserUtils.getStation() : station;
        String site = checkBomItemAndStationSn.getSite();
        site = StringUtils.isEmpty(site) ? UserUtils.getSite() : site;
        String snBo = checkBomItemAndStationSn.getSnBo();
        List<CheckBatchSnBomVo> itemBomList1 = checkBatchSnVo.getItemBomList();
        if (CollectionUtils.isEmpty(itemBomList1)) {
            throw new CommonException("物料bom不存在", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        Map<String, String> stringObjectMap = commonBrowserService.selectByStationName(station, site);
        CheckBatchSnBomVo checkBatchSnBomVo2 = itemBomList1.get(0);

        Map<String, String> stringStringMap = commonBrowserService.selectShopOrder(site, checkBatchSnBomVo2.getShopOrderBo());

        //根据snBo去查询SnusedList
        Sn sn = querySn(site, snBo);
        String snCode = sn.getSn();
        //这里去处理采集数据
        //获取物料编码跟pc
        TemporaryData snTempraryData = getSnTempraryData(snBo);

        List<ProductionCollectionRecordKey> productionCollectionRecordKeys = new ArrayList<>();
        CollectionRecordCommonTempDTO collectionRecordCommonTempDTO = new CollectionRecordCommonTempDTO();
        ProductionCollectionRecord productionCollectionRecord = new ProductionCollectionRecord();
        if (snTempraryData == null || StringUtils.isEmpty(snTempraryData.getId())) {
            //新增
            collectionRecordCommonTempDTO
                    .setSn(snCode)
                    .setSite(site)
                    .setWorkShop(MapUtils.getString(stringStringMap, "workShop"))
                    .setStation(station)
                    //工单编号 ----需要通过工单bo去查询
                    .setShopOrder(MapUtils.getString(stringStringMap, "shopOrder"))
                   .setUserName(UserUtils.getUserName());

            String operationName = MapUtils.getString(stringObjectMap, "OPERATION_NAME");
            Date date = new Date();

            productionCollectionRecord
                    .setId(UUID.uuid32())
                    .setStation(station)
                    .setProcess(operationName)
                    .setProductionLine(MapUtils.getString(stringObjectMap, "PRODUCT_LINE"))
                    .setClasses(MapUtils.getString(stringStringMap, "shiftName"))
                    .setState(ProductionCollectionRecordStateEnum.complete.getCode())
                    .setStateDesc(ProductionCollectionRecordStateEnum.complete.getDesc())
                    .setOperationTime(date);

            snTempraryData.setId(UUID.uuid32());
            snTempraryData.setStation(station);

            snTempraryData.setSn(snCode);
            snTempraryData.setCreateTime(new Date());
            snTempraryData.setType(TemporaryDataTypeEnum.BATCHBARCODE.getCode());
            snTempraryData.setCreateUser(UserUtils.getUserId());

        } else {
            //修改
            String content1 = snTempraryData.getContent();
            collectionRecordCommonTempDTO = JSONUtils.jsonToPojo(content1, CollectionRecordCommonTempDTO.class);
            if (collectionRecordCommonTempDTO == null) {
                throw new CommonException("采集记录通用临时数据结构为空", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
            productionCollectionRecord = collectionRecordCommonTempDTO.getProductionCollectionRecord();
            if (productionCollectionRecord == null) {
                throw new CommonException("采集记录数据为空", CommonExceptionDefinition.BASIC_EXCEPTION);
            }

            productionCollectionRecordKeys = productionCollectionRecord.getProductionCollectionRecordKeys();

        }
        String id = productionCollectionRecord.getId();
        //定义Map去获取数据
        Map<String, BigDecimal> loadedMap = new HashMap<>();
        Map<String, BigDecimal> map = new HashMap<>();
        for (CheckBatchSnBomVo checkBatchSnBomVo : itemBomList1
        ) {
            //组件数量
            BigDecimal bomCount = checkBatchSnBomVo.getBomCount();
            //上料数量
            BigDecimal loadedCount = checkBatchSnBomVo.getLoadedCount();
            //比较的应该是组件数量-上料数量
            BigDecimal subtract = bomCount.subtract(loadedCount);
            if (subtract.compareTo(BigDecimal.valueOf(0)) <= 0) {
                continue;
            }

            String itemBom = checkBatchSnBomVo.getItemBom();
            //物料对应差值判断
            loadedMap.put(itemBom, subtract);
            List<CheckBatchSnBomVo> collect = itemBomList1.stream().filter(x -> itemBom.equals(x.getItemBom())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(collect)) {
                throw new CommonException("产品Bom对应物料不存在", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
            CheckBatchSnBomVo checkBatchSnBomVo1 = collect.get(0);
            String itemBo = checkBatchSnBomVo1.getItemBo();
            LambdaQueryWrapper<Item> itemLambdaQueryWrapper = new QueryWrapper<Item>().lambda()
                    .eq(Item::getBo, itemBo)
                    .eq(Item::getSite, site);

            Item items = itemMapper.selectOne(itemLambdaQueryWrapper);
            if (null == items) {
                throw new CommonException("物料编码在物料表中不存在,请重新输入", CommonExceptionDefinition.BASIC_EXCEPTION);
            }

            ItemFullVo itemFullVo = itemService.getItemFullVoByItemAndVersion(items.getItem(), items.getVersion());
            for (UsedListDto userListDto : usedList
            ) {
                //物料条码
                String s = userListDto.getSn();

                ItemLabelListVo itemLabelListVo = baseMapper.selectSnBySn(s);
                if (null == itemLabelListVo) {
                    throw new CommonException("物料条码不存在", CommonExceptionDefinition.BASIC_EXCEPTION);
                }
                if ("N".equals(itemLabelListVo.getSfgq())) {


                    //物料编码
                    String item = userListDto.getItem();
                    BigDecimal sysl = userListDto.getSysl();

                    //剩余数量大于才比较
                    if (sysl.compareTo(BigDecimal.valueOf(0)) > 0 && item.equals(itemBom)) {

                        String batchNo = baseMapper.selectBatch(s, items.getItem());


                        //相等给一个参数做递减
                        if (subtract.compareTo(sysl) < 0) {
                            //组件数量小于对应的物料数量

                            loadedCount = loadedCount.add(subtract);
                            checkBatchSnBomVo.setLoadedCount(loadedCount);
                            sysl = sysl.subtract(subtract);
                            map.put(s, sysl);
                            //剩余数量要减去组件数量
                            userListDto.setSysl(sysl);

                            ProductionCollectionRecordKey productionCollectionRecordKey = setProductionCollectionKey(items, s, subtract, batchNo, itemFullVo, id);
                            productionCollectionRecordKeys.add(productionCollectionRecordKey);
                            subtract = BigDecimal.valueOf(0);
                            loadedMap.put(itemBom, subtract);
                            break;
                        } else if (subtract.compareTo(sysl) >= 0) {

                            loadedCount = loadedCount.add(sysl);
                            checkBatchSnBomVo.setLoadedCount(loadedCount);
                            //这个物料条码剩余数量为0,已耗尽自动下架
                            userListDto.setSysl(BigDecimal.valueOf(0));
                          //  map.put(s, BigDecimal.valueOf(0));
                            //给状态码
                            //获取当前条码---耗尽
                            list.add(userListDto.getSn());
                            subtract = subtract.subtract(sysl);
                            loadedMap.put(itemBom, subtract);
                            ProductionCollectionRecordKey productionCollectionRecordKey = setProductionCollectionKey(items, s, sysl, batchNo, itemFullVo, id);
                            productionCollectionRecordKeys.add(productionCollectionRecordKey);
                        }
                    }

                }
            }


        }
        //loadedMap
        //map取值
        StringBuilder stringBuffer = new StringBuilder();
        Set<String> strings = loadedMap.keySet();
        for (String test : strings) {
            BigDecimal bigDecimal = loadedMap.get(test);
            if (bigDecimal.compareTo(BigDecimal.valueOf(0)) > 0) {

                stringBuffer.append(test + "物料缺" + bigDecimal + ";");
            }

        }
        String s1 = stringBuffer.toString();
        if (StringUtils.isNotBlank(s1)) {
            return ResponseData.error("203", "制作产品[" + snCode + "]缺料:" + s1 + ",请点击“上料”按钮，先上料完整。");
        }
        //将产品对应的数值也要修改
        //将checkBatchSnBomVo装换出来保存
        //修改产品条码状态为已上料
        Set<String> strings1 = map.keySet();
        for (String key : strings1
        ) {
            //key获取条码表bo
            Sn sn1 = toGetSn(key, site);
            BigDecimal bigDecimal = map.get(key);
            ResponseData<Boolean> responseData = momLabelService.updateSysl(sn1.getBo(), bigDecimal);
            String code = responseData.getCode();
            if (Boolean.FALSE.equals("200".equals(code))) {
                throw new CommonException(responseData.getMsg(), CommonExceptionDefinition.BASIC_EXCEPTION);
            }

        }
       // baseMapper.updateSnStatus(snBo, USED_STATUS);
        changeToSaveData(temporaryData, temporaryData1, checkBatchSnVo, itemBomList1, bomItemSnByStation, usedList);
        //将tempdtaa存入采集数据里面
        productionCollectionRecord.setProductionCollectionRecordKeys(productionCollectionRecordKeys);
        collectionRecordCommonTempDTO.setProductionCollectionRecord(productionCollectionRecord);
        String s = JSONUtils.objectToJson(collectionRecordCommonTempDTO);
        snTempraryData.setContent(s);
        temporaryDataService.addOrUpdate(snTempraryData);


        return ResponseData.success();
    }

    private Sn querySn(String site, String snBo) {
        LambdaQueryWrapper<Sn> snLambdaQueryWrapper = new QueryWrapper<Sn>().lambda()
                .eq(Sn::getBo, snBo)
                .eq(Sn::getSite, site);
        Sn sn = baseMapper.selectOne(snLambdaQueryWrapper);
        if (null == sn) {
            throw new CommonException("产品条码不存在", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return sn;
    }

    private ProductionCollectionRecordKey setProductionCollectionKey(Item item, String itemCode, BigDecimal userNumber, String batchNo, ItemFullVo itemFullVo, String productionCollectionRecordId) {
        ProductionCollectionRecordKey productionCollectionRecordKey = new ProductionCollectionRecordKey();
        productionCollectionRecordKey.setId(UUID.uuid32())
                .setItemName(item.getItemName())
                //物料描述去物料维护的数据
                .setItemDesc(item.getItemDesc())
                .setItemLabel(itemCode)
                .setItemCode(item.getItem())
                .setUseNumber(Integer.valueOf(userNumber.setScale(0, BigDecimal.ROUND_DOWN).intValue()))
                .setItemLot(batchNo)
                .setUnit(itemFullVo.getItemUnit())
                .setOperationTime(new Date())
                .setProductionCollectionRecordId(productionCollectionRecordId);
        return productionCollectionRecordKey;
    }

    private void changeToSaveData(TemporaryData temporaryData, TemporaryData temporaryData1, CheckBatchSnVo checkBatchSnVo, List<CheckBatchSnBomVo> itemBomList, BomItemSnByStation bomItemSnByStation, List<UsedListDto> usedList) {
        checkBatchSnVo.setItemBomList(itemBomList);
        String s = JSONUtils.objectToJson(checkBatchSnVo);
        temporaryData1.setContent(s);
        temporaryDataMapper.deleteById(temporaryData1.getId());

        //temporaryDataMapper.updateById(temporaryData1);
        //将usedListDtos过滤掉sysl为0的其他集合
        usedList = usedList.stream().filter(x -> Boolean.FALSE.equals(BigDecimal.valueOf(0).equals(x.getSysl()))).collect(Collectors.toList());
        bomItemSnByStation.setUsedList(usedList);
        String s1 = JSONUtils.objectToJson(bomItemSnByStation);
        temporaryData.setContent(s1);
        temporaryDataMapper.updateById(temporaryData);
    }

    private void usedItemSn(String site, String itemSn, TemporaryData temporaryData, String id, String station, ItemLabelListVo itemLabelListVo) {
        //查询物料条码状态
        Sn sn = toGetSn(itemSn, site);
        if (null == sn) {
            throw new CommonException("物料条码不存在", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        String state1 = sn.getState();
        if (USED_STATUS.equals(state1)) {
            throw new CommonException("该物料已占用，请重新输入", CommonExceptionDefinition.BASIC_EXCEPTION);
        }

        baseMapper.updateSnStatusBySn(itemSn, USED_STATUS);
        if (temporaryData == null || StringUtils.isEmpty(id)) {
            //新增
            addTemporaryData(station, itemLabelListVo);

        } else {
            //修改
            updateTemporaryData(itemSn, temporaryData, station, itemLabelListVo);
        }


    }

    private void addTemporaryData(String station, ItemLabelListVo itemLabelListVo) {
        BomItemSnByStation bomItemSnByStation = new BomItemSnByStation();
        List<UsedListDto> usedList = new ArrayList<>();
        UsedListDto usedListDto = new UsedListDto();
        BeanUtils.copyProperties(itemLabelListVo, usedListDto);
        usedListDto.setDate(new Date());
        usedList.add(usedListDto);
        bomItemSnByStation.setStation(station);
        bomItemSnByStation.setUsedList(usedList);
        String s = JSONUtils.objectToJson(bomItemSnByStation);
        TemporaryData temporaryData = new TemporaryData();
        temporaryData.setId(UUID.uuid32());
        temporaryData.setSn(station);
        temporaryData.setStation(station);
        temporaryData.setContent(s);
        temporaryData.setType(BOM_ITEM_SN);
        temporaryDataMapper.insert(temporaryData);


    }

    private void updateTemporaryData(String itemSn, TemporaryData temporaryData, String station, ItemLabelListVo itemLabelListVo) {
        String content = temporaryData.getContent();
        //content转成Class
        BomItemSnByStation bomItemSnByStation = JSONUtils.jsonToPojo(content, BomItemSnByStation.class);
        if (null == bomItemSnByStation) {
            throw new CommonException("数据不存在", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        List<UsedListDto> usedList = bomItemSnByStation.getUsedList();
        List<UsedListDto> collect = usedList.stream().filter(x -> itemSn.equals(x.getSn())).collect(Collectors.toList());
        if (Boolean.FALSE.equals(CollectionUtils.isEmpty(collect))) {
            throw new CommonException("上料对应的物料条码已存在", CommonExceptionDefinition.BASIC_EXCEPTION);

        }
        UsedListDto usedListDto = new UsedListDto();
        BeanUtils.copyProperties(itemLabelListVo, usedListDto);
        usedListDto.setDate(new Date());
        usedList.add(usedListDto);
        bomItemSnByStation.setUsedList(usedList);
        bomItemSnByStation.setStation(station);
        String s = JSONUtils.objectToJson(bomItemSnByStation);
        temporaryData.setContent(s);
        temporaryDataMapper.updateById(temporaryData);
    }

    private void unUsedItemSn(String itemSn, TemporaryData temporaryData, String id) {
        if (temporaryData == null || StringUtils.isEmpty(id)) {
            throw new CommonException("下料对应的物料条码不存在", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        String content = temporaryData.getContent();
        //content转成Class
        BomItemSnByStation bomItemSnByStation = JSONUtils.jsonToPojo(content, BomItemSnByStation.class);
        List<UsedListDto> usedList = bomItemSnByStation.getUsedList();
        List<UsedListDto> collect = new ArrayList<>();
        String[] split = itemSn.split(";");
        for (String sn : split
        ) {
            List<UsedListDto> collect1 = usedList.stream().filter(x -> sn.equals(x.getSn())).collect(Collectors.toList());
            collect.addAll(collect1);
        }

        if (CollectionUtils.isEmpty(collect)) {
            throw new CommonException("下料对应的物料条码不存在", CommonExceptionDefinition.BASIC_EXCEPTION);

        }
        usedList.removeAll(collect);
        String s = JSONUtils.objectToJson(bomItemSnByStation);
        temporaryData.setContent(s);
        temporaryDataMapper.updateById(temporaryData);
        //修改物料条码状态为NEW
        baseMapper.updateSnStatusBySn(itemSn, "NEW");
    }


    /**
     * 获取工业路线流程图对应的流程信息
     *
     * @param shopOrders shopOrders
     * @return String.class
     */
    private String getProcessInfoByRouterProcess(String shopOrders) {

        //查询工单路线表
        LambdaQueryWrapper<OrderRouter> orderRouterLambdaQueryWrapper = new QueryWrapper<OrderRouter>().lambda()
                .eq(OrderRouter::getShopOrderBo, shopOrders);
        OrderRouter orderRouter = orderRouterMapper.selectOne(orderRouterLambdaQueryWrapper);
        if (null == orderRouter) {
            throw new CommonException("工艺路线为空", CommonExceptionDefinition.BASIC_EXCEPTION);
        }


        LambdaQueryWrapper<OrderRouterProcess> routerProcessQuery = new QueryWrapper<OrderRouterProcess>().lambda()
                .eq(OrderRouterProcess::getRouterBo, orderRouter.getBo());

        OrderRouterProcess routerProcess = orderRouterProcessMapper.selectOne(routerProcessQuery);
        if (null == routerProcess) {
            throw new CommonException("不存在对应的工艺路线流程", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        //获取流程图信息需要转为json去获取对应实体
        String processInfo = routerProcess.getProcessInfo();
        if (StringUtils.isEmpty(processInfo)) {
            throw new CommonException("不存在对应的工艺路线流程", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return processInfo;
    }

    private String toGetShopOrderNo(Sn oldSn) {
        String shopOrder = null;
        String labelPrintBo = oldSn.getLabelPrintBo();
        if (StringUtils.isEmpty(labelPrintBo)) {
            throw new CommonException("标签打印bo为空", CommonExceptionDefinition.BASIC_EXCEPTION);
        }

        ResponseData<LabelPrint> responseData = momLabelService.getLabelPrintBo(labelPrintBo);
        if (responseData != null) {
            String code = responseData.getCode();
            if ("200".equals(code)) {
                LabelPrint data = responseData.getData();
                if (data != null) {
                    shopOrder = data.getElementBo();
                } else {
                    throw new CommonException("获取标签打印范围数据失败", CommonExceptionDefinition.BASIC_EXCEPTION);
                }

            } else {
                throw new CommonException("获取标签打印范围数据失败", CommonExceptionDefinition.BASIC_EXCEPTION);
            }

        }
        return shopOrder;
    }

    /**
     * 获取生产中的工单
     *
     * @param site      site
     * @param shopOrder shopOrder
     * @return ShopOrder.class
     */
    private ShopOrder getShopOrders(String site, String shopOrder) {
        LambdaQueryWrapper<ShopOrder> shopOrderQuery = new QueryWrapper<ShopOrder>().lambda()
                .eq(ShopOrder::getBo, shopOrder);

        ShopOrder shopOrders1 = shopOrderMapper.selectOne(shopOrderQuery);
        if (null == shopOrders1) {
            throw new CommonException("该工单不存在", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        String stateBo = shopOrders1.getStateBo();

        //暂停506
        String suspendStateBo = PRESTATE + site + ",506";
        String finishStateBo = PRESTATE + site + ",504";
        String closeStateBo = PRESTATE + site + ",505";
        String productSuspendStateBo = PRESTATE + site + ",507";
        String releaseSuspendStateBo = PRESTATE + site + ",508";
        if (stateBo.equals(suspendStateBo)) {
            throw new CommonException("该工单状态是“暂停”，不需要生产", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        if (stateBo.equals(finishStateBo)) {
            throw new CommonException("该工单状态是“完工”，不需要生产", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        if (stateBo.equals(closeStateBo)) {
            throw new CommonException("该工单状态是“关闭”，不需要生产", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        if (stateBo.equals(productSuspendStateBo)) {
            throw new CommonException("该工单状态是“生产中暂停”，不需要生产", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        if (stateBo.equals(releaseSuspendStateBo)) {
            throw new CommonException("该工单状态是“下达暂停”，不需要生产", CommonExceptionDefinition.BASIC_EXCEPTION);
        }


        return shopOrders1;
    }

    /**
     * 获取工位信息
     *
     * @param stationDto 工位
     * @return Station.class
     */
    private Station getStation(String stationDto, String site) {
        LambdaQueryWrapper<Station> query = new QueryWrapper<Station>().lambda()
                .eq(Station::getStation, stationDto)
                .eq(Station::getSite, site);
        Station stations = stationMapper.selectOne(query);
        if (null == stations) {
            throw new CommonException("工位不存在,请先维护工位表信息", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return stations;

    }

    /**
     * 工序与工位对应的工序校验
     *
     * @param operationBo operationBo
     * @param processInfo processInfo
     */
    private void extracted(String operationBo, String processInfo) {
        JSONObject jsonObject = JSONObject.parseObject(processInfo);
        JSONArray nodeList = jsonObject.getJSONArray("nodeList");
        List<String> operationBoList = nodeList.stream().map(
                x -> {
                    JSONObject ja = (JSONObject) x;
                    return ja.getString(OPERATION_CODE_NAME);
                }
        ).filter(Objects::nonNull).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(operationBoList)) {
            throw new CommonException("工艺流程没有对应的工序", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        LambdaQueryWrapper<Operation> operationBoQueryWrapper = new QueryWrapper<Operation>().lambda()
                .in(Operation::getBo, operationBoList);

        //通过条码获取到的所有的工序 追溯方式为【单体】
        List<Operation> operations1 = operationMapper.selectList(operationBoQueryWrapper);
        if (CollectionUtils.isEmpty(operations1)) {
            throw new CommonException("工艺流程没有对应的工序", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        List<Operation> collect = operations1.stream().filter(x -> operationBo.equals(x.getBo())).distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collect)) {
            throw new CommonException("当前工位对应的工序与条码扫描的工序不一致", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
    }

    private List<TemporaryData> getTemporaryDataBySnAndType(String snBo, String type) {
        LambdaQueryWrapper<TemporaryData> temporaryDataLambdaQueryWrapper = new QueryWrapper<TemporaryData>().lambda()
                .eq(TemporaryData::getSn, snBo)
                .eq(TemporaryData::getType, type);
        return temporaryDataMapper.selectList(temporaryDataLambdaQueryWrapper);
    }

    private Item getItems(String finalSite, String componentBo) {
        LambdaQueryWrapper<Item> itemLambdaQueryWrapper = new QueryWrapper<Item>().lambda()
                .eq(Item::getSite, finalSite)
                .eq(Item::getBo, componentBo);
        List<Item> items = itemMapper.selectList(itemLambdaQueryWrapper);
        if (CollectionUtils.isEmpty(items)) {
            throw new CommonException("物料编码获取失败", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return items.get(0);
    }

    /**
     * 获取工序
     *
     * @param operationBo operationBo
     * @return Operation.class
     */
    private Operation getOperations(String operationBo) {
        LambdaQueryWrapper<Operation> operationLambdaQueryWrapper = new QueryWrapper<Operation>().lambda()
                .eq(Operation::getBo, operationBo);
        Operation operations = operationMapper.selectOne(operationLambdaQueryWrapper);

        if (null == operations) {
            throw new CommonException("当前用户工位对应的工序不存在,请核对", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return operations;
    }


}

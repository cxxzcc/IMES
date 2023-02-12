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
import com.itl.mes.core.api.dto.CheckItemCodeAndSnDto;
import com.itl.mes.core.api.dto.CheckItemCodeDto;
import com.itl.mes.core.api.dto.CheckSnBindDto;
import com.itl.mes.core.api.dto.CollectionRecordCommonTempDTO;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.CheckSnBindService;
import com.itl.mes.core.api.vo.CheckSnBindVo;
import com.itl.mes.core.api.vo.CheckSnBomVo;
import com.itl.mes.core.api.vo.ItemFullVo;
import com.itl.mes.core.client.service.MomLabelService;
import com.itl.mes.core.provider.mapper.*;
import com.itl.mes.core.provider.utils.JSONUtils;
import com.itl.mes.me.api.entity.LabelPrint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 验证单体条码绑定ServiceImpl
 *
 * @author GKL
 * @date 2021/11/4 - 14:31
 * @since 2021/11/4 - 14:31 星期四 by GKL
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CheckSnBindServiceImpl extends ServiceImpl<SnMapper, Sn> implements CheckSnBindService {
    /**
     * 工位表Mapper
     */
    private final StationMapper stationMapper;
    /**
     * 工单表Mapper
     */
    private final ShopOrderMapper shopOrderMapper;
    /**
     * 工序Mapper
     */
    private final OperationMapper operationMapper;
    /**
     * 物料表对应Mapper
     */
    private final ItemMapper itemMapper;
    /**
     * 物料标签表对应Mapper
     */
    private final MomLabelService momLabelService;


    /**
     * 工单bom组件表
     */

    private final ShopOrderBomComponnetMapper shopOrderBomComponnetMapper;
    /**
     * 工艺路线
     */
    private final OrderRouterMapper orderRouterMapper;
    /**
     * 工艺路线图
     */
    private final OrderRouterProcessMapper orderRouterProcessMapper;
    /**
     * 暂存表中数据
     */
    private final TemporaryDataMapper temporaryDataMapper;

    @Autowired
    private CommonBrowserServiceImpl commonBrowserService;
    @Autowired
    private TemporaryDataServiceImpl temporaryDataService;
    @Autowired
    private ItemServiceImpl itemService;

    private static final String PRESTATE = "STATE:";
    private static final String ITEM_CODE_IS_NULL = "物料条码不存在，请重新输入";
    private static final String OPERATION_CODE_NAME = "operation";
    private static final String SN_BIND_NAME = "snBind";


    /**
     * 输入sn，校验条码
     *
     * @param dto 条码,工位
     * @return ResponseData。class
     */
    @Override
    public ResponseData<CheckSnBindVo> checkSn(CheckSnBindDto dto) {
        //1.判断sn不能为空
        String sn = dto.getSn();
        //校验参数
        checkParameter(sn);
        //工厂
        String site = dto.getSite();
        site = StringUtils.isEmpty(site) ? UserUtils.getSite() : site;

        //获取到用户信息的工位
        String stationDto = dto.getStation();
        stationDto = StringUtils.isEmpty(stationDto) ? UserUtils.getStation() : stationDto;

        //工位去查对应的工位表找到工序bo
        Station stations = getStation(stationDto, site);
        //存在获取工位表中的工序bo

        //根据工序bo查询出对应的物料
        String operationBo = stations.getOperationBo();
        LambdaQueryWrapper<Sn> queryWrapper = new QueryWrapper<Sn>().lambda()
                .eq(Sn::getSn, sn)
               // .ne(Sn::getState, "used")
                .eq(Sn::getSite, site);
        //条码表中获取是否存在sn
        Sn oldSn = this.getOne(queryWrapper);
        if (null != oldSn) {
            //获取2.	匹配输入值条码编码属于的工单编号 ---查询ShopOrder
            //获取到label_label_printBo
            String labelPrintBo = oldSn.getLabelPrintBo();
            if (StringUtils.isEmpty(labelPrintBo)) {
                throw new CommonException("标签打印bo为空", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
            String shopOrder = null;
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

            List<String> list = new ArrayList<>();
            if (StringUtils.isEmpty(shopOrder)) {
                return ResponseData.error("该条码没有属于的工单编码，请重新输入");
            }

            //通过工单号去查询工单表查询状态是在生产中a)statusBo =503是生产中 状态值 STATE:1040,505需要切割
            ShopOrder shopOrders = getShopOrders(site, shopOrder);
            //一个工单会对应多个工序 工序是通过工艺路线去获取 工单获取工艺路线bo routerMapper  直接去查对应的工艺路线流程
            String processInfo = getProcessInfoByRouterProcess(shopOrders.getBo());
            extracted(operationBo, processInfo);
            //根据工序找到对应的物料清单 追溯方式为【单体】
            //获取到bomBo
            String bo = shopOrders.getBo();

            //中间bom组件表查询到具体的qty      //zsType L 单体
            //根据工单bo查询获取到对应的多个bom组件里面的内容
            //根据工单bo,类型,工序bo,追溯方式拿到所有数据  shopOrderBomComponentQuery
            LambdaQueryWrapper<ShopOrderBomComponnet> shopOrderBomComponentQuery = new QueryWrapper<ShopOrderBomComponnet>().lambda()
                    .eq(ShopOrderBomComponnet::getShopOrderBo, bo)
                    .eq(ShopOrderBomComponnet::getOperationBo, operationBo)
                    .eq(ShopOrderBomComponnet::getType, "OP")
                    .eq(ShopOrderBomComponnet::getZsType, "L");
            List<ShopOrderBomComponnet> shopOrderBomComponents = shopOrderBomComponnetMapper.selectList(shopOrderBomComponentQuery);
            if (CollectionUtils.isEmpty(shopOrderBomComponents)) {
                throw new CommonException("该工位的工序BOM为空，请检查工单-工序BOM", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
            //定义返回的实体

            CheckSnBindVo checkSnBindVo = new CheckSnBindVo();
            String finalSite = site;
            //将sn存入对应的暂存表中
            //先查询表中存在
            List<TemporaryData> temporaryData1 = getTemporaryDataBySnAndType(oldSn.getBo(), SN_BIND_NAME);
            if (Boolean.FALSE.equals(CollectionUtils.isEmpty(temporaryData1))) {
                String content = temporaryData1.get(0).getContent();
                checkSnBindVo = JSONUtils.jsonToPojo(content, CheckSnBindVo.class);
                return ResponseData.success(checkSnBindVo);
            }

            TemporaryData temporaryData = new TemporaryData();

            List<CheckSnBomVo> bomList = shopOrderBomComponents.stream().map(x -> {
                CheckSnBomVo bomVo = new CheckSnBomVo();
                bomVo.setBomCount(x.getQty());
                bomVo.setLoadedCount(new BigDecimal(0));
                String componentBo = x.getComponentBo();
                //根据bo去查询物料表
                Item items = getItems(finalSite, componentBo);
                list.add(items.getItem());
                bomVo.setItemBo(componentBo);
                bomVo.setShopOrderBo(shopOrders.getBo());
                bomVo.setShopOrder(shopOrders.getShopOrder());
                bomVo.setItemBom(items.getItem());
                String operationBo1 = x.getOperationBo();
                Operation operations1 = getOperations(operationBo1);
                bomVo.setOperation(operations1.getOperationName());
                bomVo.setShopOrderBomComponentBo(x.getBo());
                bomVo.setVirtualItem(x.getVirtualItem());
                bomVo.setZsType("单体");
                bomVo.setComponentPosition(x.getComponentPosition());
                bomVo.setItemOrder(x.getItemOrder());
                bomVo.setItemType(x.getItemType());
                return bomVo;
            }).collect(Collectors.toList());
            checkSnBindVo.setSnBo(oldSn.getBo());
            checkSnBindVo.setItemBomList(bomList);
            //远程调用获取物料编码对应的所有条码
            String s = JSONUtils.objectToJson(checkSnBindVo);
            temporaryData.setSn(oldSn.getBo());
            temporaryData.setContent(s);
            temporaryData.setCreateTime(new Date());
            temporaryData.setType(SN_BIND_NAME);
            temporaryData.setCreateUser(UserUtils.getUserId());
            temporaryDataMapper.insert(temporaryData);
            return ResponseData.success(checkSnBindVo);
        } else {
            return ResponseData.error("产品条码不存在，请重新输入");
        }


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
     * 物料编码校验
     *
     * @param dto bom相关参数，物料编码
     * @return ResponseData.class
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    @Override
    public ResponseData<Boolean> checkItemCode(CheckItemCodeDto dto) {
        //工厂
        String site = dto.getSite();
        site = StringUtils.isEmpty(site) ? UserUtils.getSite() : site;

        //获取到用户信息的工位
        String stationDto = dto.getStation();
        stationDto = StringUtils.isEmpty(stationDto) ? UserUtils.getStation() : stationDto;
        //工位去查对应的工位表找到工序bo
        Map<String, String> stringObjectMap = commonBrowserService.selectByStationName(stationDto, site);


        List<CheckItemCodeAndSnDto> itemBomList = dto.getItemBomList();
        if (CollectionUtils.isEmpty(itemBomList)) {
            throw new CommonException("物料bom不能为空", CommonExceptionDefinition.BASIC_EXCEPTION);
        }

        String itemCode = dto.getItemCode();
        if (StringUtils.isEmpty(itemCode)) {
            throw new CommonException("物料对应的条码不能为空", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        String snBo = dto.getSnBo();
        if (StringUtils.isEmpty(snBo)) {
            throw new CommonException("条码bo不能为空", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        //通过sn去查询对应的物料编码
        List<CheckItemCodeAndSnDto> checkItemCodecollects = new ArrayList<>();
        checkItemCodecollects = getCheckItemCodeAndSnDtos(itemBomList, itemCode, checkItemCodecollects);
        LambdaQueryWrapper<Sn> snLambdaQueryWrapper = new QueryWrapper<Sn>().lambda()
                .eq(Sn::getSn, itemCode)
                .eq(Sn::getSite, site);

        Sn sn = baseMapper.selectOne(snLambdaQueryWrapper);

        //根据sn,类型查询唯一的数据
        //根据snBo查询条码表
        LambdaQueryWrapper<Sn> snLambdaQueryWrapper1 = new QueryWrapper<Sn>().lambda()
                .eq(Sn::getBo, snBo);

        Sn sn1 = baseMapper.selectOne(snLambdaQueryWrapper1);
        if (sn1 == null) {
            throw new CommonException("该产品条码不存在", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        String snCode = sn1.getSn();
        String state = sn.getState();
      /*  if ("used".equals(state)) {
            throw new CommonException("该条码已上料", CommonExceptionDefinition.BASIC_EXCEPTION);
        }*/
        // String itemBom = itemBomList.get(0).getItemBom();
        String itemBo = itemBomList.get(0).getItemBo();
        //先查物料表获取物料表bo itemMapper
        LambdaQueryWrapper<Item> itemLambdaQueryWrapper = new QueryWrapper<Item>().lambda()
                .eq(Item::getBo, itemBo)
                .eq(Item::getSite, site);

        Item item = itemMapper.selectOne(itemLambdaQueryWrapper);
        if (null == item) {
            throw new CommonException("物料编码在物料表中不存在,请重新输入", CommonExceptionDefinition.BASIC_EXCEPTION);
        }


        //存在则取第一个数据进行比较
        CheckItemCodeAndSnDto checkItemCodeAndSnDto = checkItemCodecollects.get(0);

        Map<String, String> stringStringMap = commonBrowserService.selectShopOrder(site, checkItemCodeAndSnDto.getShopOrderBo());

        //根据bom组件bo查询获取数据
        String shopOrderBomComponentBo = checkItemCodeAndSnDto.getShopOrderBomComponentBo();
        LambdaQueryWrapper<ShopOrderBomComponnet> shopOrderBomComponentLambdaQueryWrapper = new QueryWrapper<ShopOrderBomComponnet>().lambda()
                .eq(ShopOrderBomComponnet::getBo, shopOrderBomComponentBo);
        ShopOrderBomComponnet shopOrderBomComponnet = shopOrderBomComponnetMapper.selectOne(shopOrderBomComponentLambdaQueryWrapper);
        if (shopOrderBomComponnet != null) {

            //查询条码
            baseMapper.updateSnStatus(sn.getBo(), "used");
            //根据传过来的产品sn
            List<TemporaryData> temporaryData1 = getTemporaryDataBySnAndType(snBo, SN_BIND_NAME);

            List<TemporaryData> temporaryDataBySnAndType = getTemporaryDataBySnAndType(snCode, TemporaryDataTypeEnum.BARCODEBIND.getCode());
            TemporaryData temporaryDataNow = new TemporaryData();
            if (Boolean.FALSE.equals(CollectionUtils.isEmpty(temporaryDataBySnAndType))) {
                temporaryDataNow = temporaryDataBySnAndType.get(0);
            }


            //总上料数量
            BigDecimal loadedAll = new BigDecimal(0);
            //当前上料数量
            BigDecimal currentLoaded = new BigDecimal(0);
            //总组件数量
            BigDecimal compentAll = new BigDecimal(0);
            //当前组件数量
            BigDecimal currentCompent = new BigDecimal(0);
            if (CollectionUtils.isEmpty(temporaryData1)) {
                throw new CommonException("产品条码已经装配完成", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
            TemporaryData temporaryData = temporaryData1.get(0);
            String content = temporaryData.getContent();
            CheckSnBindVo checkSnBindVo = JSONUtils.jsonToPojo(content, CheckSnBindVo.class);
            if (null == checkSnBindVo) {
                throw new CommonException("请先使用产品条码", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
            List<CheckSnBomVo> itemBomList1 = checkSnBindVo.getItemBomList();
            if (CollectionUtils.isEmpty(itemBomList1)) {
                throw new CommonException("对应组件工序bom为空", CommonExceptionDefinition.BASIC_EXCEPTION);
            }


            for (CheckSnBomVo checkSnBomVo : itemBomList1
            ) {
                BigDecimal loadedCount = checkSnBomVo.getLoadedCount();
                loadedCount = (null == loadedCount) ? new BigDecimal(0) : loadedCount;
                if (checkItemCodeAndSnDto.getItemBom().equals(checkSnBomVo.getItemBom())) {
                    checkSnBomVo.setLoadedCount(loadedCount.add(new BigDecimal(1)));
                    currentLoaded = checkSnBomVo.getLoadedCount();
                    currentCompent = checkSnBomVo.getBomCount();
                }
                BigDecimal bomCount = checkSnBomVo.getBomCount();
                bomCount = null == bomCount ? new BigDecimal(0) : bomCount;

                loadedAll = loadedAll.add(checkSnBomVo.getLoadedCount());
                compentAll = compentAll.add(bomCount);
            }

            if (loadedAll.compareTo(compentAll) == 0) {
                //相等说明
              //  baseMapper.updateSnStatus(snBo, "used");
                temporaryDataMapper.deleteById(temporaryData.getId());
                if (temporaryDataNow == null || StringUtils.isEmpty(temporaryDataNow.getId())) {
                    toInsetTempData(site, stationDto, stringObjectMap, snCode, item, stringStringMap, temporaryDataNow, itemCode);

                } else {
                    //只需要部分的数据填充
                    toUpdateTempData(item, temporaryDataNow, itemCode);
                }
                //需要记录时间
                return ResponseData.error("202", "装配完成");

            }
            //比较当前组件是否一致
            if (currentLoaded.compareTo(currentCompent) == 0) {
                //一致
                String s = JSONUtils.objectToJson(checkSnBindVo);
                temporaryData.setContent(s);
                temporaryDataMapper.updateById(temporaryData);
                if (temporaryDataNow == null || StringUtils.isEmpty(temporaryDataNow.getId())) {
                    toInsetTempData(site, stationDto, stringObjectMap, snCode, item, stringStringMap, temporaryDataNow, itemCode);

                } else {
                    //只需要部分的数据填充
                    toUpdateTempData(item, temporaryDataNow, itemCode);

                }
                //需要记录时间
                return ResponseData.error("201", "该物料已上料完成");
            }
            if (currentLoaded.compareTo(currentCompent) > 0) {
                throw new CommonException("组件装配数量等于已上料数量", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
            String s = JSONUtils.objectToJson(checkSnBindVo);
            temporaryData.setContent(s);
            temporaryDataMapper.updateById(temporaryData);
            if (temporaryDataNow == null || StringUtils.isEmpty(temporaryDataNow.getId())) {
                toInsetTempData(site, stationDto, stringObjectMap, snCode, item, stringStringMap, temporaryDataNow, itemCode);

            } else {
                //只需要部分的数据填充
                toUpdateTempData(item, temporaryDataNow, itemCode);

            }

            return ResponseData.success();
        } else {
            throw new CommonException("该物料编码没有对应的工单BOM组件数据", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
    }

    /**
     * 修改暂存表对应数据
     *
     * @param item             item
     * @param temporaryDataNow temporaryDataNow
     */
    private void toUpdateTempData(Item item, TemporaryData temporaryDataNow, String itemCode) {
        String batchNo = baseMapper.selectBatch(itemCode,item.getItem());
        ItemFullVo itemFullVo = itemService.getItemFullVoByItemAndVersion(item.getItem(), item.getVersion());
        String content1 = temporaryDataNow.getContent();
        CollectionRecordCommonTempDTO collectionRecordCommonTempDTO = JSONUtils.jsonToPojo(content1, CollectionRecordCommonTempDTO.class);
        if (collectionRecordCommonTempDTO == null) {
            throw new CommonException("采集记录通用临时数据结构为空", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        ProductionCollectionRecord productionCollectionRecord = collectionRecordCommonTempDTO.getProductionCollectionRecord();
        if (productionCollectionRecord == null) {
            throw new CommonException("采集记录数据为空", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        List<ProductionCollectionRecordKey> productionCollectionRecordKeys = productionCollectionRecord.getProductionCollectionRecordKeys();
        ProductionCollectionRecordKey productionCollectionRecordKey = new ProductionCollectionRecordKey();
        productionCollectionRecordKey.setId(UUID.uuid32())
                .setItemName(item.getItemName())
                //物料描述去物料维护的数据
                .setItemDesc(item.getItemDesc())
                .setItemLabel(itemCode)
                .setItemCode(item.getItem())
                .setUseNumber(1)
                .setItemLot(batchNo)
                .setUnit(itemFullVo.getItemUnit())
                .setOperationTime(new Date())
                .setProductionCollectionRecordId(productionCollectionRecord.getId());
        productionCollectionRecordKeys.add(productionCollectionRecordKey);
        productionCollectionRecord.setProductionCollectionRecordKeys(productionCollectionRecordKeys);
        collectionRecordCommonTempDTO.setProductionCollectionRecord(productionCollectionRecord);
        String s1 = JSONUtils.objectToJson(collectionRecordCommonTempDTO);
        temporaryDataNow.setContent(s1);
        temporaryDataService.addOrUpdate(temporaryDataNow);
    }

    /**
     * 新增暂存表对应数据
     *
     * @param site             site
     * @param stationDto       stationDto
     * @param stringObjectMap  stringObjectMap
     * @param snCode           snCode
     * @param item             item
     * @param stringStringMap  stringStringMap
     * @param temporaryDataNow temporaryDataNow
     */
    private void toInsetTempData(String site, String stationDto, Map<String, String> stringObjectMap, String snCode, Item item, Map<String, String> stringStringMap, TemporaryData temporaryDataNow, String itemCode) {
        //全部新增
        String batchNo = baseMapper.selectBatch(itemCode,item.getItem());
        ItemFullVo itemFullVo = itemService.getItemFullVoByItemAndVersion(item.getItem(), item.getVersion());

        //记录数据
        CollectionRecordCommonTempDTO collectionRecordCommonTempDTO = new CollectionRecordCommonTempDTO();
        collectionRecordCommonTempDTO
                .setSn(snCode)
                .setSite(site)
                .setWorkShop(MapUtils.getString(stringStringMap, "workShop"))
                .setStation(stationDto)
                //工单编号 ----需要通过工单bo去查询
                .setShopOrder(MapUtils.getString(stringStringMap, "shopOrder"))
                .setUserName(UserUtils.getUserName());
        ProductionCollectionRecord productionCollectionRecord = new ProductionCollectionRecord();
        String operationName = MapUtils.getString(stringObjectMap, "OPERATION_NAME");
        Date date = new Date();

        productionCollectionRecord
                .setId(UUID.uuid32())
                .setStation(stationDto)
                .setProcess(operationName)
                .setProductionLine(MapUtils.getString(stringObjectMap, "PRODUCT_LINE"))
                .setClasses(MapUtils.getString(stringStringMap, "shiftName"))
                .setState(ProductionCollectionRecordStateEnum.complete.getCode())
                .setStateDesc(ProductionCollectionRecordStateEnum.complete.getDesc())
                .setOperationTime(date);
        List<ProductionCollectionRecordKey> productionCollectionRecordKeys = new ArrayList<>();
        ProductionCollectionRecordKey productionCollectionRecordKey = new ProductionCollectionRecordKey();
        productionCollectionRecordKey.setId(UUID.uuid32())
                .setItemName(item.getItemName())
                //物料描述去物料维护的数据
                .setItemDesc(item.getItemDesc())
                .setItemLabel(itemCode)
                .setItemCode(item.getItem())
                .setUseNumber(1)
                .setItemLot(batchNo)
                .setUnit(itemFullVo.getItemUnit())
                .setOperationTime(date)
                .setProductionCollectionRecordId(productionCollectionRecord.getId());
        productionCollectionRecordKeys.add(productionCollectionRecordKey);
        productionCollectionRecord.setProductionCollectionRecordKeys(productionCollectionRecordKeys);
        collectionRecordCommonTempDTO.setProductionCollectionRecord(productionCollectionRecord);
        //实体转json
        String s1 = JSONUtils.objectToJson(collectionRecordCommonTempDTO);
        temporaryDataNow.setId(UUID.uuid32());
        temporaryDataNow.setStation(stationDto);
        temporaryDataNow.setContent(s1);
        temporaryDataNow.setSn(snCode);
        temporaryDataNow.setCreateTime(new Date());
        temporaryDataNow.setType(TemporaryDataTypeEnum.BARCODEBIND.getCode());
        temporaryDataNow.setCreateUser(UserUtils.getUserId());
        temporaryDataService.addOrUpdate(temporaryDataNow);
    }

    private List<TemporaryData> getTemporaryDataBySnAndType(String snBo, String type) {
        LambdaQueryWrapper<TemporaryData> temporaryDataLambdaQueryWrapper = new QueryWrapper<TemporaryData>().lambda()
                .eq(TemporaryData::getSn, snBo)
                .eq(TemporaryData::getType, type);
        return temporaryDataMapper.selectList(temporaryDataLambdaQueryWrapper);
    }

    @Override
    public ResponseData<String> queryItemBomBySn(CheckSnBindDto dto) {
        String sn = dto.getSn();
        if (StringUtils.isEmpty(sn)) {
            throw new CommonException("条码不能为空", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return momLabelService.queryItemBySn(sn);


    }

    private List<CheckItemCodeAndSnDto> getCheckItemCodeAndSnDtos(List<CheckItemCodeAndSnDto> itemBomList, String itemCode, List<CheckItemCodeAndSnDto> checkItemCodecollects) {
        ResponseData<String> responseData = momLabelService.queryItemBySn(itemCode);
        if (responseData != null) {
            String code = responseData.getCode();
            String data = responseData.getData();
            if ("200".equals(code)) {
                if (StringUtils.isEmpty(data)) {
                    throw new CommonException(ITEM_CODE_IS_NULL, CommonExceptionDefinition.BASIC_EXCEPTION);
                }
                //物料编码比对
                checkItemCodecollects = itemBomList.stream().filter(x -> data.equals(x.getItemBom())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(checkItemCodecollects)) {
                    throw new CommonException(ITEM_CODE_IS_NULL, CommonExceptionDefinition.BASIC_EXCEPTION);
                }
            } else {
                throw new CommonException(ITEM_CODE_IS_NULL, CommonExceptionDefinition.BASIC_EXCEPTION);
            }
        }
        return checkItemCodecollects;
    }


    /**
     * 校验参数
     *
     * @param sn 条码
     */
    private void checkParameter(String sn) {
        if (StringUtils.isEmpty(sn)) {
            throw new CommonException("条码不能为空，请重新输入", CommonExceptionDefinition.BASIC_EXCEPTION);
        }


    }


}


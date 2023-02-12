package com.itl.mes.me.provider.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.itl.iap.common.base.constants.CommonConstants;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.Assert;
import com.itl.iap.common.base.utils.CommonUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.ProductLineHandleBO;
import com.itl.mes.core.api.bo.SnHandleBO;
import com.itl.mes.core.api.bo.WorkShopHandleBO;
import com.itl.mes.core.api.dto.ProductStateUpdateDto;
import com.itl.mes.core.api.entity.OrderRouter;
import com.itl.mes.core.api.entity.OrderRouterProcess;
import com.itl.mes.core.api.vo.ShopOrderBomComponnetVo;
import com.itl.mes.core.api.vo.ShopOrderFullVo;
import com.itl.mes.core.client.service.OrderRouterService;
import com.itl.mes.core.client.service.RouterService;
import com.itl.mes.core.client.service.ShopOrderBomComponentService;
import com.itl.mes.core.client.service.ShopOrderService;
import com.itl.mes.me.api.dto.OperationQueryDto;
import com.itl.mes.me.api.entity.AssyTemp;
import com.itl.mes.me.api.entity.LoadingList;
import com.itl.mes.me.api.entity.LoadingListTemp;
import com.itl.mes.me.api.entity.Operation;
import com.itl.mes.me.api.service.AssyTempService;
import com.itl.mes.me.api.service.LoadingListService;
import com.itl.mes.me.api.service.LoadingListTempService;
import com.itl.mes.me.api.service.OperationService;
import com.itl.mes.me.api.util.GeneratorId;
import com.itl.mes.me.provider.mapper.OperationMapper;
import com.itl.mom.label.api.entity.label.LabelTypeEntity;
import com.itl.mom.label.api.entity.label.Sn;
import com.itl.mom.label.client.service.LabelService;
import com.itl.mom.label.client.service.MeProductStatusService;
import com.itl.mom.label.client.service.SnService;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 操作
 *
 * @author yx
 * @date 2021-05-31
 */
@Service
@Slf4j
public class OperationServiceImpl extends ServiceImpl<OperationMapper, Operation> implements OperationService {

    @Value("${mybatis-plus.configuration.database-id}")
    private String databaseId;

    private OperationMapper operationMapper;
    private ShopOrderService shopOrderService;
    private SnService snService;
    private RouterService routerService;
    private AssyTempService assyTempService;
    private ShopOrderBomComponentService shopOrderBomComponentService;
    private LoadingListService loadingListService;
    private LoadingListTempService loadingListTempService;
    @Autowired
    private LabelService labelService;
    @Autowired
    private OrderRouterService orderRouterService;
    @Autowired
    private MeProductStatusService meProductStatusService;

    @Resource
    public void setOperationMapper(OperationMapper operationMapper) {
        this.operationMapper = operationMapper;
    }

    @Autowired
    public void setShopOrderService(ShopOrderService shopOrderService) {
        this.shopOrderService = shopOrderService;
    }

    @Autowired
    public void setSnService(SnService snService) {
        this.snService = snService;
    }

    @Autowired
    public void setRouterService(RouterService routerService) {
        this.routerService = routerService;
    }

    @Autowired
    public void setShopOrderBomComponentService(ShopOrderBomComponentService shopOrderBomComponentService) {
        this.shopOrderBomComponentService = shopOrderBomComponentService;
    }

    @Autowired
    public void setLoadingListService(LoadingListService loadingListService) {
        this.loadingListService = loadingListService;
    }

    @Autowired
    public void setLoadingListTempService(LoadingListTempService loadingListTempService) {
        this.loadingListTempService = loadingListTempService;
    }

    @Resource
    public void setAssyTempService(AssyTempService assyTempService) {
        this.assyTempService = assyTempService;
    }

    @Override
    public IPage<Operation> queryPage(OperationQueryDto queryDto) {
        if (queryDto.getPage() == null) {
            queryDto.setPage(new Page(0, 10));
        }

        LambdaQueryChainWrapper<Operation> query = lambdaQuery();
        if (queryDto.getOperation() != null && queryDto.getOperation().length() != 0) {
            query.like(Operation::getOperation, queryDto.getOperation());
        }
        if (queryDto.getDesc() != null && queryDto.getDesc().length() != 0) {
            query.like(Operation::getDesc, queryDto.getDesc());
        }
        query.eq(Operation::getSite, UserUtils.getSite());
        IPage page = null;
        if (databaseId.equals(CommonUtil.DATABASEID)) {
            page = operationMapper.selectPageVo(queryDto.getPage(), queryDto, UserUtils.getSite());
        } else {
            page = query.page(queryDto.getPage());
        }
        List<Operation> records = page.getRecords();
        if(CollectionUtil.isNotEmpty(records)){
            List<String> labelList = records.stream().map(v -> v.getScanSn().split(","))
                    .flatMap(Arrays::stream).distinct()
                    .collect(Collectors.toList());
            ResponseData<List<LabelTypeEntity>> labelTypeByIdList = labelService.getLabelTypeByIdList(labelList);
            if (labelTypeByIdList != null && labelTypeByIdList.getData() != null) {
                List<LabelTypeEntity> data = labelTypeByIdList.getData();
                Map<String, LabelTypeEntity> collect = data.stream().collect(Collectors.toMap(LabelTypeEntity::getId, Function.identity()));
                for (Operation record : records) {
                    String scanSn = record.getScanSn();
                    if (StrUtil.isNotBlank(scanSn)) {
                        String[] split = scanSn.split(",");
                        String scanSnName = "";
                        List<LabelTypeEntity> scanSnJsonList = new ArrayList<>();
                        for (int i = 0; i < split.length; i++) {
                            String s = split[i];
                            if (collect.containsKey(s)) {
                                LabelTypeEntity labelTypeEntity = collect.get(s);
                                scanSnJsonList.add(labelTypeEntity);
                                scanSnName += labelTypeEntity.getLabelType();
                            }
                            if (i < split.length - 1) {
                                scanSnName += ",";
                            }
                        }
                        record.setScanSnName(scanSnName);
                        record.setScanSnJson(JSONUtil.toJsonStr(scanSnJsonList));
                    }
                }
            }
        }
        return page;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void saveAndUpdate(Operation operation) throws CommonException {
//        String userName = "UserUtils.getCurrentUser().getUserName()";
        String userName = UserUtils.getCurrentUser().getUserName();
        operation.setSite(UserUtils.getSite());
        if (operation.getId() == null || "".equals(operation.getId())) {
            // 新增
            operation.setId(new GeneratorId().getSnowflake().nextIdStr());
            operation.setObjectSetBasicAttribute(userName, new Date());
            operation.setScanSn(operation.getScanSn());
            operationMapper.insert(operation);
        } else {
            // 更新
            operation.setCreateUser(null);
            operation.setCreateDate(null);
            operation.setModifyUser(userName);
            operation.setModifyDate(new Date());
            operationMapper.updateById(operation);
        }
    }

    @Override
    public void delete(List<String> bos) {
        Assert.valid(CollUtil.isEmpty(bos), "请选择要删除的数据!");
        operationMapper.deleteBatchIds(bos);
    }

    /**
     * @param sn          工单 sn
     * @param operationBo 工序 主键
     * @return Map<String, Object>
     * @throws CommonException 异常
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public Map<String, Object> checkSnAndOperation(String sn, String operationBo) throws CommonException {
        log.info("OperationServiceImpl--checkSnAndOperation sn:{}, operationBo:{}", sn, operationBo);
        //根据sn查询sn_bo 根据sn_bo和正常状态查询id
        String id = operationMapper.getIdBySnAndState(sn);
        log.info("根据sn查询sn_bo 根据sn_bo和正常状态查询id id:{}", id);
        if (StringUtils.isBlank(id)) {
            throw new CommonException("状态筛选未查询到相关数据", 201);
        }
        // 检验工单是否暂停/关闭
        ShopOrderFullVo shopOrderFullVo = checkShopOrder(sn);
        log.info("检验工单是否暂停/关闭 shopOrderFullVo:{}", shopOrderFullVo);
        // 检验条码是否完工/报废
        Map<String, String> map = checkSn(sn);
        log.info("检验条码是否完工/报废 sn:{}", sn);
        //判断标签类型是否合规（被包含）
        checkLabelType(sn);
        log.info("判断标签类型是否合规（被包含）");
        // 检验条码当前工序与工位工序是否一致
        checkSnOperation(sn, operationBo, shopOrderFullVo);
        log.info("检验条码当前工序与工位工序是否一致");

        Map<String, Object> ret = Maps.newHashMap();
        ret.put("shopOrder", shopOrderFullVo.getShopOrder());
        ret.put("item", map.get("item"));
        ret.put("itemName", map.get("itemName"));

        return ret;
    }

    /**
     * 判断标签类型是否被包含
     *
     * @param sn
     */
    private void checkLabelType(String sn) {
        log.info("判断标签类型是否被包含 sn:{}", sn);
        //查询当前标签类型
        String labelType = operationMapper.findLableTypeBySn(sn);
        if (StringUtils.isBlank(labelType)) {
            throw new CommonException("数据异常,标签类型不存在！", 201);
        }
        //查询工位操作项中所定义的标签类型范围
        Operation operation = operationMapper.selectOne(
                new QueryWrapper<Operation>().lambda().eq(Operation::getOperation, "SN工序校验"));
        if (Objects.isNull(operation) || StringUtils.isBlank(operation.getScanSn())) {
            throw new CommonException("不存在匹配的工位操作项", 201);
        }
        List<LabelTypeEntity> labelTypes = (List<LabelTypeEntity>) JSONObject.parse(operation.getScanSn());
        if (CollectionUtils.isEmpty(labelTypes)) {
            throw new CommonException("标签范围未定义或格式不正确", 201);
        }
        if (!labelTypes.contains(labelType)) {
            throw new CommonException("条码类型不正确!", 201);
        }
    }

    @Override
    public List getAssyList(String productSn, String traceMethod) throws CommonException {
        String shopOrderBo = Optional.ofNullable(operationMapper.getShopOrderBoBySn(productSn))
                .orElseThrow(() -> new CommonException("获取工单号失败!", CommonExceptionDefinition.BASIC_EXCEPTION));

        List<ShopOrderBomComponnetVo> resp = Optional.ofNullable(shopOrderBomComponentService.queryBomByShopOrderBo(shopOrderBo, "OP"))
                .orElseThrow(() -> new CommonException("获取工单工序Bom失败!", CommonExceptionDefinition.BASIC_EXCEPTION));

        if (resp.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        // TODO: 是否需要记录Sfc,log
        List<ShopOrderBomComponnetVo> list = resp.stream().filter(x -> traceMethod.equals(x.getZsType())).collect(Collectors.toList());
        List<String> componentBos = list.stream().map(ShopOrderBomComponnetVo::getItemBo).collect(Collectors.toList());
        // 单体条码绑定判断是否装配完成
        if ("L".equals(traceMethod)) {
            List<AssyTemp> temps = assyTempService.list(new QueryWrapper<AssyTemp>().lambda()
                    .in(AssyTemp::getComponenetBo, componentBos)
                    .eq(AssyTemp::getSfc, productSn)
                    .eq(AssyTemp::getTraceMethod, traceMethod)
            );
            if (CollectionUtil.isNotEmpty(temps)) {
                list.forEach(x -> {
                    String itemSn = temps.stream()
                            .filter(y -> y.getComponenetBo().contains(x.getComponent()))
                            .map(AssyTemp::getAssembledSn)
                            .collect(Collectors.joining(","));
                    x.setItemSn(itemSn);
                    if (StrUtil.isBlank(itemSn)) {
                        x.setAssyFinish(false);
                    } else if (itemSn.split(",").length == x.getQty().intValue()) {
                        x.setAssyFinish(true);
                    } else {
                        x.setAssyFinish(false);
                    }
                });
            } else {
                list.forEach(x -> x.setAssyFinish(false));
            }
        }
        return list;
    }

    @Override
    public Map<String, Object> getBatchAssyList(String productSn, String operationBo) {
        Map<String, Object> ret = Maps.newHashMap();

        CompletableFuture<List<ShopOrderBomComponnetVo>> assyListFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return this.getAssyList(productSn, "S");
            } catch (CommonException e) {
                throw new CompletionException(e);
            }
        });

        CompletableFuture<List<LoadingListTemp>> loadingListFuture = CompletableFuture
                .supplyAsync(() -> loadingListTempService.getLoadingListTemp(productSn, operationBo));

        List<LoadingListTemp> loadingListTemp = loadingListFuture.join();
        List<ShopOrderBomComponnetVo> assyList = assyListFuture.join();
        assyList.parallelStream().forEach(x -> {
            Integer qty = loadingListTemp.stream().filter(y -> y.getComponentBo().equals(x.getItemBo()))
                    .map(LoadingListTemp::getRemainingQty)
                    .reduce(0, (i1, i2) -> i1 + i2);
            x.setAssyFinish(x.getQty().intValue() == qty);
        });
        ret.put("assyList", assyList);
        ret.put("loadingListTemp", loadingListTemp);
        return ret;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public List<ShopOrderBomComponnetVo> bindSingleSn(String productSn, String childSn) throws CommonException {
        // 验证该单体条码是否已经被绑定
        List<AssyTemp> list = assyTempService.list(new QueryWrapper<AssyTemp>().lambda().eq(AssyTemp::getAssembledSn, childSn));
        if (list != null && list.size() > 0) {
            throw new CommonException("检测到扫描的子件条码已经被绑定!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }

        // 查询物料标签表中该条码是否存在且可用
        Sn childSnInfo = Optional.ofNullable(snService.getSnInfo(childSn))
                .orElseThrow(() -> new CommonException("获取子件条码信息失败!", CommonExceptionDefinition.BASIC_EXCEPTION));
        if (!"NEW;DOWN".contains(childSnInfo.getState())) {
            throw new CommonException("子件条码当前状态不可装配!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        final String itemBo = childSnInfo.getItemBo();
        if (itemBo == null || "".equals(itemBo)) {
            throw new CommonException("检测到扫描的子件条码不包含物料信息!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }

        // 装配清单中此料号是否存在且未装配完成
        List<ShopOrderBomComponnetVo> ret = this.getAssyList(productSn, "L");
        ShopOrderBomComponnetVo target = ret.stream().filter(x -> x.getItemBo().equals(itemBo)).findAny()
                .orElseThrow(() -> new CommonException("装配清单中不包含此物料,无需装配!", CommonExceptionDefinition.BASIC_EXCEPTION));
        if (target.getAssyFinish()) {
            throw new CommonException("此物料已装配完成!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        target.setItemSn(Optional.ofNullable(target.getItemSn()).map(x -> x + ",").orElse("") + childSn);
        if (target.getItemSn().split(",").length == target.getQty().intValue()) {
            target.setAssyFinish(true);
        } else {
            target.setAssyFinish(false);
        }
        String site = UserUtils.getSite();
        String userName = UserUtils.getCurrentUser().getUserName();
        String workShopBo = new WorkShopHandleBO(site, UserUtils.getWorkShop()).getBo();
        String productLineBo = new ProductLineHandleBO(site, UserUtils.getProductLine()).getBo();
        Date date = new Date();


        snService.collarSn(childSn, workShopBo, productLineBo);
        snService.save(Lists.newArrayList(childSn), "WIP");

        // 保存到临时表
        AssyTemp assyTemp = new AssyTemp();
        assyTemp.setId(new GeneratorId().getSnowflake().nextIdStr()).setSite(site)
                .setSfc(productSn).setTraceMethod("L").setComponenetBo(itemBo).setAssembledSn(childSn)
                .setQty(BigDecimal.ONE).setAssyTime(date).setAssyUser(userName);
        assyTempService.save(assyTemp);

        return ret;
    }

    @Override
    public void saveAssyTempInfo(String productSn, List<ShopOrderBomComponnetVo> list) {
        List<AssyTemp> alreadySave = assyTempService.lambdaQuery().eq(AssyTemp::getSfc, productSn).eq(AssyTemp::getTraceMethod, "L").list();
        if (alreadySave != null && !alreadySave.isEmpty()) {
            List<String> savedSn = alreadySave.stream().map(AssyTemp::getAssembledSn).collect(Collectors.toList());
            List<String> snList = list.stream().flatMap(x -> Stream.of(x.getItemSn().split(",")))
                    .filter(x -> savedSn.contains(x))
                    .collect(Collectors.toList());
            if (snList.size() > 0) {
                return;
            }
        }
        // 保存到临时表
        assyTempService.saveAssyTempInfo(productSn, list);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public Map<String, Object> bindBatchSn(String productSn, String childSn, String operationBo) throws CommonException {
        // 验证该单体条码是否已经被绑定
        List<LoadingListTemp> list = loadingListTempService.list(new QueryWrapper<LoadingListTemp>().lambda().eq(LoadingListTemp::getAssembledSn, childSn));
        if (list != null && list.size() > 0) {
            throw new CommonException("检测到扫描的子件条码已经被绑定!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }

        // 查询物料标签表中该条码是否存在且可用
        Sn childSnInfo = Optional.ofNullable(snService.getSnInfo(childSn))
                .orElseThrow(() -> new CommonException("获取子件条码信息失败!", CommonExceptionDefinition.BASIC_EXCEPTION));
        if (!"NEW;DOWN".contains(childSnInfo.getState())) {
            throw new CommonException("子件条码当前状态不可装配!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        final String itemBo = childSnInfo.getItemBo();
        if (itemBo == null || "".equals(itemBo)) {
            throw new CommonException("检测到扫描的子件条码不包含物料信息!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }

        // 装配清单中此料号是否存在且未装配完成
        List<ShopOrderBomComponnetVo> assyList = this.getAssyList(productSn, "S");
        List<LoadingListTemp> loadingListTemp = loadingListTempService.getLoadingListTemp(productSn, operationBo);
        assyList.parallelStream().forEach(x -> {
            Integer qty = loadingListTemp.stream().filter(y -> y.getComponentBo().equals(x.getItemBo()))
                    .map(LoadingListTemp::getRemainingQty)
                    .reduce(0, (i1, i2) -> i1 + i2);
            x.setAssyFinish(x.getQty().intValue() == qty);
        });
        ShopOrderBomComponnetVo target = assyList.stream().filter(x -> x.getItemBo().equals(itemBo)).findAny()
                .orElseThrow(() -> new CommonException("装配清单中不包含此物料,无需装配!", CommonExceptionDefinition.BASIC_EXCEPTION));
        if (target.getAssyFinish()) {
            throw new CommonException("此物料已装配完成!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        LoadingListTemp temp = new LoadingListTemp();
        temp.setId(new GeneratorId().getSnowflake().nextIdStr()).setSite(childSnInfo.getSite())
                .setSfc(productSn).setTraceMethod("S").setComponentBo(itemBo).setAssembledSn(childSn)
                .setOperationBo(operationBo).setSnOriginState(childSnInfo.getState());
        if (loadingListTemp.size() > 0) {
            // 该物料的剩余还需上料数量
            Integer itemRemainingQty = loadingListTemp.stream().filter(x -> x.getComponentBo().equals(itemBo))
                    .map(LoadingListTemp::getRemainingQty)
                    .sorted(Comparator.comparingInt(Integer::intValue).reversed())
                    .reduce(target.getQty().intValue(), (t1, t2) -> t1 - t2);
            if (itemRemainingQty.compareTo(0) < 0) {
                throw new CommonException("此物料无需上料", CommonExceptionDefinition.BASIC_EXCEPTION);
            }

            // 该Sn的剩余数量, 默认为Sn的产出数量
            if (list != null && list.size() != 1) {
                throw new CommonException("检测到数据异常!", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
            Integer snRemainingQty = Optional.ofNullable(childSnInfo.getOutQty()).orElseGet(() -> BigDecimal.ZERO).intValue();
            if (list != null) {
                // 临时表有记录,取临时表的记录
                snRemainingQty = list.get(0).getRemainingQty();
            } else {
                List<LoadingList> snLoadingList = loadingListService.lambdaQuery().eq(LoadingList::getAssembledSn, childSn).list();
                if (snLoadingList != null) {
                    // 临时表没有记录, 上料清单表有记录, 取上料清单表的记录
                    snRemainingQty = snLoadingList.stream().map(LoadingList::getRemainingQty).min(Comparator.comparingInt(Integer::intValue)).get();
                }
            }
            temp.setRemainingQty(snRemainingQty);
        } else {
            temp.setRemainingQty(Optional.ofNullable(childSnInfo.getOutQty()).orElseGet(() -> BigDecimal.ZERO).intValue());
        }
        loadingListTemp.add(temp);
        loadingListTempService.save(temp);
        snService.save(Lists.newArrayList(childSn), "WIP");
        assyList.parallelStream().forEach(x -> {
            Integer qty = loadingListTemp.stream().filter(y -> y.getComponentBo().equals(x.getItemBo()))
                    .map(LoadingListTemp::getRemainingQty)
                    .reduce(0, (i1, i2) -> i1 + i2);
            x.setAssyFinish(x.getQty().intValue() == qty);
        });
        Map<String, Object> ret = Maps.newHashMap();
        ret.put("assyList", assyList);
        ret.put("loadingListTemp", loadingListTemp);
        return ret;
    }

    @Override
    public Boolean unload(List<String> ids) {
        return loadingListTempService.unload(ids);
    }

    @Override
    public void changeSnRouterCurrentNode(String sn, String result, String shopOrderBo, String operationBo) throws CommonException {
        List<Map<String, String>> list = operationMapper.getSnRouter(sn);
        if (list.size() != 1) {
            throw new CommonException("检测到SN对应多条工艺路线!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        Map<String, String> map = list.get(0);
        String nodeId = map.get("current");
        String json = map.get("json");
        List<String> currentNodeIds = JsonPath.read(json, String.format("$.nodeList[?(@.operation == '%s')].id", operationBo));
        Assert.valid(CollUtil.isEmpty(currentNodeIds), "条码工艺路线中未找到当前工序对应的节点");
        String currentNodeId = currentNodeIds.get(0);

        List<String> nextNodeIds = JsonPath.read(json, String.format("$.lineList[?(@.from =~ /%s/)].to", currentNodeId));
        Assert.valid(CollUtil.isEmpty(nextNodeIds), "未找到下工序");

        //下工序节点id，下工序bo，下工序名称，下工序类型
        String nextNodeId = null, nextOperation = null, nextOperationName = null, nextOperationType = null;
        if(nextNodeIds.size() == 1) {
            //只有一条分支， 直接下一步
            nextNodeId = nextNodeIds.get(0);
            List<Map<String, String>> operations = JsonPath.read(json, String.format("$.nodeList[?(@.id == '%s')]", nextNodeId));
            Map<String, String> operation = operations.get(0);
            nextOperation = operation.get("operation");
            nextOperationName = operation.get("name");
            nextOperationType = operation.get("type");
        } else {
            //根据条件查询下工序的id
            List<Map<String, String>> operations = JsonPath.read(json, String.format("$.nodeList[?(@.id in $.lineList[?(@.label=='%s' && @.from == '%s')].to)]", result, currentNodeId));
            Assert.valid(CollUtil.isEmpty(operations), "未找到符合条件的下工序");
            Assert.valid(operations.size() > 1, "找到多个符合条件的下工序");
            Map<String, String> operation = operations.get(0);
            nextNodeId = operation.get("id");
            nextOperation = operation.get("operation");
            nextOperationName = operation.get("name");
            nextOperationType = operation.get("type");
        }
        //判断下工序是否为结束工序
        ProductStateUpdateDto productStateUpdateDto = new ProductStateUpdateDto();

        if(StrUtil.equals(nextOperationType, "end")) {
            nextOperation = "";
            nextOperationName = "";

            //下工序为结束工序， 触发产品完工事件
            /**
             * 产品完工事件，需要执行以下步骤
             * 1. 更新工单完工数量
             * 2. 更新工单是否完工标识
             * 3. 更新条码是否完工标识（产品状态管理 me_product_status）
             * 4. 修改采集记录状态数据为完工(此处不必更新，保存采集记录暂存数据的时候会更新的)
             * */
            //更新工单完工数量  + 更新工单是否完工标识
            shopOrderService.updateShopOrderCompleteQtyAndState(shopOrderBo, 1);
            //更新条码是否完工标识（产品状态管理 me_product_status）
            productStateUpdateDto.setDone(CommonConstants.NUM_ONE);
        }

        Assert.valid(StrUtil.isEmpty(nextNodeId), "数据异常，未找到符合条件的下一工序");
        operationMapper.changeSnRouterCurrentNode(sn, nextNodeId);
        //产品状态更新
        productStateUpdateDto.setSnBo(new SnHandleBO(UserUtils.getSite(), sn).getBo());
        productStateUpdateDto.setNextOperationBo(nextOperation);
        productStateUpdateDto.setNextOperationName(nextOperationName);
        meProductStatusService.updateNextProcess(productStateUpdateDto);

    }

    /**
     * 检验工单是否暂停/关闭
     *
     * @param sn 条码
     * @return {@link ShopOrderFullVo} 工单entity
     * <p>
     * 500 新建
     * 501 部分下达
     * 502 已下达
     * 503 生产中
     * 504 完工
     * 505 关闭
     * 506 暂停
     * 507 生产中暂停
     * <p>
     * 已上状态码是根据数据字典来的 不知道是谁配置的
     */
    @Override
    public ShopOrderFullVo checkShopOrder(String sn) throws CommonException {
        String[] str = {"505", "506", "507"};
        List<String> stringLi = Arrays.asList(str);

        String shopOrderBo = operationMapper.getShopOrderBoBySn(sn); // TODO: 2021/11/17  此处查询出来多条导致报错。 报错为me_product_status表脏数据导致 sn_bo=？ and（state=1） 应唯一
        if (StrUtil.isBlank(shopOrderBo)) {
            throw new CommonException("检测到无效条码!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }

        ResponseData<ShopOrderFullVo> shopOrder = shopOrderService.getShopOrderByBo(shopOrderBo);
        ShopOrderFullVo shopOrderFullVo = Optional.ofNullable(shopOrder)
                .map(ResponseData::getData).orElseThrow(() -> new CommonException("获取工单失败!", CommonExceptionDefinition.BASIC_EXCEPTION));

        String orderState = Optional.ofNullable(shopOrderFullVo)
                .map(ShopOrderFullVo::getState).orElseThrow(() -> new CommonException("未获取到工单!", CommonExceptionDefinition.BASIC_EXCEPTION));
        if (stringLi.contains(orderState)) {
            throw new CommonException("检测到工单关闭/暂停!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return shopOrderFullVo;
    }

    /**
     * 检验条码是否完工/报废
     *
     * @param sn 条码
     * @throws CommonException
     */
    public Map<String, String> checkSn(String sn) throws CommonException {
        Map<String, String> ret = snService.getItemInfoAndSnStateBySn(sn);
        String snState = Optional.ofNullable(ret).map(m -> m.get("snState"))
                .orElseThrow(() -> new CommonException("获取条码信息失败!", CommonExceptionDefinition.BASIC_EXCEPTION));
        if ("END;SCRAPPED".contains(snState)) {
            throw new CommonException("检测到SN状态处于完工/报废", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return ret;
    }

    @Override
    public void checkSnOperation(String sn, String operationBo, ShopOrderFullVo shopOrderFullVo) throws CommonException {
        // 从me_sn_router表获取json和“当前处于哪个步骤”
        List<Map<String, String>> list = operationMapper.getSnRouter(sn);

        if (list == null || list.isEmpty()) {
            // 查询工单的工艺路线,默认当前工序为"开始"之后的工序
            OrderRouter orderRouter = Optional.ofNullable(orderRouterService.getOrderRouter(shopOrderFullVo.getBo()))
                    .map(ResponseData::getData).orElseThrow(() -> new CommonException("获取工单工艺路线失败!", CommonExceptionDefinition.BASIC_EXCEPTION));

            // m_router_process 表内包含工艺路线具体的工艺步骤
            OrderRouterProcess orderRouterProcess = Optional.ofNullable(orderRouter)
                    .map(OrderRouter::getOrderRouterProcess).orElseThrow(() -> new CommonException("未获取到工单的工艺路线!", CommonExceptionDefinition.BASIC_EXCEPTION));
            String json = Optional.ofNullable(orderRouterProcess.getProcessInfo()).orElseThrow(() -> new CommonException("工艺步骤为空!!", CommonExceptionDefinition.BASIC_EXCEPTION));

            List<String> nodeList = JsonPath.read(json, "$.lineList[?(@.from nin $.lineList[*].to)].from");
            if (nodeList.size() != 1) {
                throw new CommonException("无法确定工艺步骤中的`开始`!", CommonExceptionDefinition.BASIC_EXCEPTION);
            }

            List<String> nodeList2 = JsonPath.read(json, String.format("$.lineList[?(@.from=~/%s/)].to", nodeList.get(0)));
            if (nodeList2.size() != 1) {
                throw new CommonException("`开始`之后有多条分支!", CommonExceptionDefinition.BASIC_EXCEPTION);
            }

            // 获取第一个执行的节点
            String nodeId = nodeList2.get(0);

            // 核查
            checkOperationRouter(operationBo, nodeId, json);

            // save to sn-router
            operationMapper.saveSnRouter(sn, json, nodeId, UserUtils.getSite());
        } else {
            if (list.size() != 1) {
                throw new CommonException("检测到SN对应多条工艺路线!", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
            String nodeId = list.get(0).get("current");
            String json = list.get(0).get("json");
            // 核查
            checkOperationRouter(operationBo, nodeId, json);
        }
    }


    /**
     * 检查登录工序和当前工艺步骤工序是否一致
     *
     * @param operationBo 当前登录工序 (根据工位获取的)
     * @param nodeId      当前节点 (工序内当前节点id)
     * @param json        工艺步骤json
     * @throws CommonException 异常
     */
    private void checkOperationRouter(String operationBo, String nodeId, String json) throws CommonException {
        List<String> currentOperations = JsonPath.read(json, String.format("$.nodeList[?(@.id =~ /%s/)].operation", nodeId));
        if (currentOperations.size() != 1) {
            throw new CommonException("检测到工艺步骤异常!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        String currentOperation = currentOperations.get(0);
        if (!operationBo.equals(currentOperation)) {
            throw new CommonException(String.format("SN当前工序应为%s!", currentOperation), CommonExceptionDefinition.BASIC_EXCEPTION);
        }
    }
}

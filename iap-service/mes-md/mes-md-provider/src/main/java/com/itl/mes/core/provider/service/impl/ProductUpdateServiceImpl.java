package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.common.base.constants.CommonConstants;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.Assert;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.dto.ProductFinishUpdateDto;
import com.itl.mes.core.api.dto.ProductStateUpdateDto;
import com.itl.mes.core.api.dto.ProductStatusUpdateDto;
import com.itl.mes.core.api.dto.UpdateDoneDto;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.ProductUpdateService;
import com.itl.mes.core.api.vo.MeProductStatusVo;
import com.itl.mes.core.api.vo.ProductStatusUpdateVo;
import com.itl.mes.core.client.service.MomLabelService;
import com.itl.mes.core.provider.mapper.*;
import com.itl.mes.me.api.entity.MeSnRouter;
import com.itl.mes.me.client.service.MeSnCrossStationLogService;
import com.itl.mes.me.client.service.MeSnRouterService;
import com.itl.mom.label.api.entity.MeProductStatus;
import com.itl.mom.label.client.service.MeProductStatusService;
import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 产品状态更新
 *
 * @author GKL
 * @date 2021/11/15 - 17:17
 * @since 2021/11/15 - 17:17 星期一 by GKL
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ProductUpdateServiceImpl implements ProductUpdateService {

    /**
     * 物料标签表对应Mapper
     */
    private final MomLabelService momLabelService;

    /**
     * 工位表Mapper
     */
    private final StationMapper stationMapper;

    private static final String SUCCESSFUL = "200";

    private static final String PRESTATE = "STATE:";
    /**
     * 工单表Mapper
     */
    private final ShopOrderMapper shopOrderMapper;

    /**
     * 工序Mapper
     */
    private final OperationMapper operationMapper;

    private final ProductLineMapper productLineMapper;
    /**
     * 工艺路线
     */
    private final OrderRouterMapper orderRouterMapper;
    /**
     * 工艺路线流程
     */
    private final OrderRouterProcessMapper orderRouterProcessMapper;

    private final CommonBrowserMapper commonBrowserMapper;
    @Autowired
    private CollectionRecordServiceImpl collectionRecordService;
    @Autowired
    private MeSnRouterService meSnRouterService;
    @Autowired
    private MeSnCrossStationLogService meSnCrossStationLogService;
    @Autowired
    private MeProductStatusService meProductStatusService;

    /**
     * 产品状态更新---修改产品状态管理
     *
     * @param dto dto
     * @return ResponseData.class
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    @Override
    public ResponseData<ProductStatusUpdateVo> productStatusUpdate(ProductStatusUpdateDto dto) {
        String site = dto.getSite();
        site = StringUtils.isEmpty(site) ? UserUtils.getSite() : site;
        String station = dto.getStation();
        station = StringUtils.isEmpty(station) ? UserUtils.getStation() : station;
        String sn = dto.getSn();
        if (StringUtils.isEmpty(sn)) {
            throw new CommonException("sn不能为空", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        //feign调用根据sn跟状态去查询到唯一的数据
        int status = 1;
        String shopOrderBo = null;
        ResponseData<List<MeProductStatusVo>> responseData = momLabelService.findProductStatusBySnAndStatus(sn, status);
        if (responseData != null) {

            String code = responseData.getCode();
            String msg = responseData.getMsg();
            if (SUCCESSFUL.equals(code)) {
                List<MeProductStatusVo> datas = responseData.getData();
                if (CollectionUtils.isEmpty(datas)) {
                    throw new CommonException("产品状态管理未查询到数据", CommonExceptionDefinition.BASIC_EXCEPTION);
                }

                List<ProductStateUpdateDto> productStateUpdateDtos = new ArrayList<>();
                for (MeProductStatusVo data : datas
                ) {
                    //判断是否报废
                    Assert.valid(StrUtil.equals(data.getState(), "SCRAPPED"), "[" + sn + "]已报废");
                    //判断是否挂起
                    Assert.valid(StrUtil.equals(data.getHold(), CommonConstants.STR_ONE), "[" + sn + "]已挂起");
                    //条码是否关闭
                    String done = data.getDone();
                    Assert.valid(StrUtil.equals(done, CommonConstants.STR_ONE), "[" + sn + "]已完工");

                    //获取me_product_status id  onLine // 0:未上线，1:已上线
                    String productStateBo = data.getProductStateBo();
                    //定义上线字段onLine // 0:未上线，1:已上线
                    Integer onLine = 1;
                    //当前工位
                    Station stations = getStation(station, site);
                    String productLineBo = stations.getProductLineBo();
                    LambdaQueryWrapper<ProductLine> query = new QueryWrapper<ProductLine>().lambda()
                            .eq(ProductLine::getBo, productLineBo);

                    ProductLine productLine = productLineMapper.selectOne(query);
                    //工单bo
                    shopOrderBo = data.getShopOrderBo();
                    //获取工单
                    ShopOrder shopOrders = getShopOrders(site, shopOrderBo);
                    //判断工单状态
                    compareShopOrder(site, shopOrders);


                    //查询条码工艺路线
                    ResponseData<MeSnRouter> response = meSnRouterService.getBySn(sn, site);
                    Assert.valid(!response.isSuccess(), response.getMsg());
                    //条码工艺路线
                    MeSnRouter meSnRouter = response.getData();
                    String processInfoByRouterProcess = getProcessInfoBySnOrShopOrder(meSnRouter, shopOrders.getBo());
                    //当前工位工序
                    String operationBo = stations.getOperationBo();
                    Operation operation = queryOperation(operationBo);

                    //条码当前工序
                    String snCurrentOperation = null;
                    Map<String, String> operationMap = new HashMap<>();
                    if (meSnRouter != null && StrUtil.isNotBlank(meSnRouter.getCurrent())) {
                        String snCurrentNodeId = meSnRouter.getCurrent();
                        List<Map<String, String>> operations = JsonPath.read(processInfoByRouterProcess, String.format("$.nodeList[?(@.id == '%s')]", snCurrentNodeId));
                        Assert.valid(CollUtil.isEmpty(operations), "条码工艺路线未找到当前工序节点");
                        operationMap = operations.get(0);
                        snCurrentOperation = operationMap.get("operation");
                    }

                    //判断下工序是否为空
                    String nextOperation = data.getNextOperationId();
                    String nextOperation1 = data.getNextOperation();
                    String isSkipId = null;
                    boolean flag = Boolean.TRUE;
                    if (StringUtils.isEmpty(nextOperation)) {
                        //为空 获取开始节点指向的id
                        List<Map<String, String>> operations1 = JsonPath.read(processInfoByRouterProcess, String.format("$.nodeList[?(@.type == '%s')]", "timer"));
                        Assert.valid(CollUtil.isEmpty(operations1), "条码工艺路线未找到开始节点");
                        Map<String, String> opertaion1 = operations1.get(0);
                        String id = opertaion1.get("id");
                        if (StringUtils.isEmpty(id)) {
                            throw new CommonException("开始节点的id不存在", CommonExceptionDefinition.BASIC_EXCEPTION);
                        }

                        Map<String, String> mapByFromId = getMapByFromId(processInfoByRouterProcess, id);
                        String to = mapByFromId.get("to");
                        if (StringUtils.isEmpty(to)) {
                            throw new CommonException("开始节点指向的下工序节点的id为空", CommonExceptionDefinition.BASIC_EXCEPTION);
                        }
                        Map<String, Object> mapById = getMapById(processInfoByRouterProcess, to);
                        String operation1 = (String) mapById.get("operation");
                        if (StringUtils.isEmpty(operation1)) {
                            throw new CommonException("首工序对应的下工序的节点没有对应的工序", CommonExceptionDefinition.BASIC_EXCEPTION);
                        }

                        if (Boolean.FALSE.equals(StrUtil.equals(operationBo, operation1))) {
                            throw new CommonException("工序错误,应为【" +  (String) mapById.get("name") + "】", CommonExceptionDefinition.BASIC_EXCEPTION);
                        }
                        //去保存
                        //下工序为空正常更新数据
                        toSetProductStateUpdateDtos(station, productStateUpdateDtos, productStateBo, onLine, operationBo, operation, productLine);


                    } else {
                        //不为空,判断是否是跳站
                        //判断用户当前节点是否是存在于工艺路程里面
                        List<Map<String, String>> operations = JsonPath.read(processInfoByRouterProcess, String.format("$.nodeList[?(@.operation == '%s')]", operationBo));
                        Assert.valid(CollUtil.isEmpty(operations), "工序错误,应为【" + nextOperation1 + "】");
                        List<Map<String, Object>> operations1 = JsonPath.read(processInfoByRouterProcess, String.format("$.nodeList[?(@.operation == '%s')]", nextOperation));
                        Assert.valid(CollUtil.isEmpty(operations1), "条码工艺路线未找到下工序节点");
                        Map<String, Object> opertaion1 = operations1.get(0);
                        if (Boolean.FALSE.equals(StrUtil.equals(operationBo, nextOperation))) {
                            Boolean isSkip1 = Objects.isNull(opertaion1.get("isSkip")) ? false : (Boolean) opertaion1.get("isSkip");
                            if (isSkip1) {
                                flag = Boolean.FALSE;
                                isSkipId = (String) opertaion1.get("id");
                            }else{
                                judgeRepeat(site, sn, processInfoByRouterProcess, operationBo, snCurrentOperation, operationMap);

                            }

                        }

                    }

                    //判断流程是否正常往下走
                    //给个标记去判断
                    String currentOperation = data.getCurrentOperation();
                    if(StringUtils.isNotBlank(isSkipId)) {
                        judgeFlag(site, sn, processInfoByRouterProcess, operationBo, snCurrentOperation, operationMap, isSkipId, flag, currentOperation);

                    }
                    toSetProductStateUpdateDtos(station, productStateUpdateDtos, productStateBo, onLine, operationBo, operation, productLine);

                }
                momLabelService.productStateUpdate(productStateUpdateDtos);
                shopOrderMapper.updateProductState(shopOrderBo, PRESTATE + site + ",503");
                ProductStatusUpdateVo productStatusUpdateVo = commonBrowserMapper.selectProductStatusUpdate(shopOrderBo, site);
                return ResponseData.success(productStatusUpdateVo);


            } else {
                throw new CommonException(msg, CommonExceptionDefinition.BASIC_EXCEPTION);
            }
        } else {
            throw new CommonException("条码查询不到数据", CommonExceptionDefinition.BASIC_EXCEPTION);
        }


    }

    private void judgeFlag(String site, String sn, String processInfoByRouterProcess, String operationBo, String snCurrentOperation, Map<String, String> operationMap, String isSkipId, boolean flag, String currentOperation) {
        while (Boolean.FALSE.equals(flag)) {

            //跳站,获取跳站信息
            Map<String, String> lineList = getMapByFromId(processInfoByRouterProcess, isSkipId);
            //获取是否跳站
            String label = lineList.get("label");
            String to = lineList.get("to");
            if ("OK".equals(label)) {
                //获取下一个判断的
                Map<String, Object> mapById = getMapById(processInfoByRouterProcess, to);
                //需要查询,此时的工序跟ok对应的工序是否一致
                //获取下一个工序跟当前工序是否一致
                String operation1 = (String) mapById.get("operation");
                if (StrUtil.equals(operation1, operationBo)) {
                    //相等,跳出循环 ---为了修改数据
                    flag = Boolean.TRUE;
                } else {
                    //根据查询是否可以过站

                    Boolean isSkip1 = Objects.isNull(mapById.get("isSkip")) ? false : (Boolean) mapById.get("isSkip");
                    if (Boolean.FALSE.equals(isSkip1)) {
                        //判断当前工序 不可跳站当前工序为空值抛出异常
                        if (StringUtils.isEmpty(currentOperation)) {
                            throw new CommonException("工序错误，应为【" + operationMap.get("name") + "】信息", CommonExceptionDefinition.BASIC_EXCEPTION);
                        } else if (StringUtils.isNotBlank(currentOperation)) {
                            //校验重复过站逻辑
                            judgeRepeat(site, sn, processInfoByRouterProcess, operationBo, snCurrentOperation, operationMap);
                            flag = Boolean.TRUE;
                        }
                    }
                    isSkipId = to;
                }

            } else if (StringUtils.isEmpty(label)) {
                //获取下一个
                Map<String, Object> mapById = getMapById(processInfoByRouterProcess, to);


                String operation1 = (String) mapById.get("operation");
                if (StrUtil.equals(operation1, operationBo)) {
                    //相等,跳出循环
                    flag = Boolean.TRUE;
                } else {
                    //根据查询是否可以过站
                    Boolean isSkip1 = Objects.isNull(mapById.get("isSkip")) ? false : (Boolean) mapById.get("isSkip");
                    if (Boolean.FALSE.equals(isSkip1)) {
                        //判断当前工序 不可跳站当前工序为空值抛出异常
                        if (StringUtils.isEmpty(currentOperation)) {
                            throw new CommonException("工序错误，应为【" + operationMap.get("name") + "】信息", CommonExceptionDefinition.BASIC_EXCEPTION);
                        } else if (StringUtils.isNotBlank(currentOperation)) {
                            //校验重复过站逻辑
                            judgeRepeat(site, sn, processInfoByRouterProcess, operationBo, snCurrentOperation, operationMap);
                            flag = Boolean.TRUE;

                        }
                    }

                    isSkipId = to;
                }
            } else {
                throw new CommonException("跳站操作没有分配可执行的工序", CommonExceptionDefinition.BASIC_EXCEPTION);
            }

        }
    }

    private void judgeRepeat(String site, String sn, String processInfoByRouterProcess, String operationBo, String snCurrentOperation, Map<String, String> operationMap) {
        if (!StrUtil.equals(operationBo, snCurrentOperation)) {


            ResponseData<MeProductStatus> meProductStatusResponseData = meProductStatusService.getBySn(sn, site);
            Assert.valid(!meProductStatusResponseData.isSuccess(), meProductStatusResponseData.getMsg());

            MeProductStatus meProductStatus = meProductStatusResponseData.getData();
            //上工序=产品状态管理的当前工序
            String previousOperation = meProductStatus.getCurrentOperationId();
            //判断当前工序是否为产品状态管理的当前工序
            if (!StrUtil.equals(operationBo, previousOperation)) {
                Assert.valid(StrUtil.equals("end", operationMap.get("type")), "【" + sn + "】末工序已完成");
                Assert.valid(true, "工序错误，应为【" + operationMap.get("name") + "】信息");
            }

            //获取上工序是否可重复过站， 可重复过站次数
            List<Map<String, Object>> operations = JsonPath.read(processInfoByRouterProcess, String.format("$.nodeList[?(@.operation == '%s')]", previousOperation));
            if (CollUtil.isEmpty(operations)) {
                Assert.valid(StrUtil.equals("end", operationMap.get("type")), "【" + sn + "】末工序已完成");
                Assert.valid(true, "工序错误，应为【" + operationMap.get("name") + "】信息");
            }
            Map<String, Object> opertaion = operations.get(0);
            //是否可重复过站，默认N
            Boolean isRepeat = Objects.isNull(opertaion.get("isRepeat")) ? false : (Boolean) opertaion.get("isRepeat");
            //工序可过站次数,默认1
            Integer repeatCount = Objects.isNull(opertaion.get("repeatCount")) ? CommonConstants.NUM_ONE : Integer.parseInt(((String) opertaion.get("repeatCount")));
            //判断是否可重复过站
            if (!isRepeat) {
                Assert.valid(StrUtil.equals("end", operationMap.get("type")), "【" + sn + "】末工序已完成");
                Assert.valid(true, "工序错误，应为【" + operationMap.get("name") + "】信息");
            }

            //是否无限制重复过站， 0=无限制次数
            if (!CommonConstants.NUM_ZERO.equals(repeatCount)) {
                //判断具体次数
                ResponseData<Integer> countBySnResponseData = meSnCrossStationLogService.getCountBySn(sn, site, operationBo);
                Assert.valid(!countBySnResponseData.isSuccess(), countBySnResponseData.getMsg());
                Integer count = countBySnResponseData.getData();
                Assert.valid(count >= repeatCount, "已超出过站次数，请执行【" + operationMap.get("name") + "】");
            }

        }
    }

    private void toSetProductStateUpdateDtos(String station, List<ProductStateUpdateDto> productStateUpdateDtos, String productStateBo, Integer onLine, String operationBo, Operation operation, ProductLine productLine) {
        String productLineName = productLine.getProductLine();
        //定义采集时间
        Date date = new Date();
        //操作人
        String operationName = UserUtils.getUserName();
        ProductStateUpdateDto productStateUpdateDto = new ProductStateUpdateDto();
        productStateUpdateDto.setProductStateBo(productStateBo)
                .setDate(date)
                .setOnLine(onLine)
                .setStationsStationAndPLName(station + "_" + productLineName)
                .setOperationBo(operationBo)
                .setOperationName(operation.getOperationName())
                .setOperationUserName(operationName);
        productStateUpdateDtos.add(productStateUpdateDto);
    }

    private Map<String, String> getMapByFromId(String processInfoByRouterProcess, String isSkipId) {
        List<Map<String, String>> lineLists = JsonPath.read(processInfoByRouterProcess, String.format("$.lineList[?(@.from == '%s')]", isSkipId));
        Assert.valid(CollUtil.isEmpty(lineLists), "条码工艺路线未找到下工序节点");
        Map<String, String> hashMap = new HashMap<>();
        for (Map<String, String> map : lineLists
        ) {
            String label = map.get("label");
            if ("OK".equals(label) || StringUtils.isEmpty(label)) {
                hashMap = map;

            }
        }
        if (CollUtil.isEmpty(hashMap)) {
            throw new CommonException("条码工艺路线未找到下工序节点-工序跳站没有指向跳站工序", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return hashMap;
    }

    private Map<String, Object> getMapById(String processInfoByRouterProcess, String to) {
        List<Map<String, Object>> operations = JsonPath.read(processInfoByRouterProcess, String.format("$.nodeList[?(@.id == '%s')]", to));
        Assert.valid(CollUtil.isEmpty(operations), "条码工艺路线未找到当前工序节点");
        return operations.get(0);
    }

    private void compareShopOrder(String site, ShopOrder shopOrders) {
        String stateBo = shopOrders.getStateBo();
        //暂停506
        String suspendStateBo = PRESTATE + site + ",506";
        String finishStateBo = PRESTATE + site + ",504";
        String closeStateBo = PRESTATE + site + ",505";
        String productSuspendStateBo = PRESTATE + site + ",507";
        String releaseSuspendStateBo = PRESTATE + site + ",508";
        String shopOrder = shopOrders.getShopOrder();
        if (stateBo.equals(suspendStateBo)) {
            throw new CommonException("工单号[" + shopOrder + "],状态是“暂停”，不允许生产", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        if (stateBo.equals(finishStateBo)) {
            throw new CommonException("工单号[" + shopOrder + "],状态是“完工”，不允许生产", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        if (stateBo.equals(closeStateBo)) {
            throw new CommonException("工单号[" + shopOrder + "],状态是“关闭”，不允许生产", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        if (stateBo.equals(productSuspendStateBo)) {
            throw new CommonException("工单号[" + shopOrder + "],状态是“生产中暂停”，不允许生产", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        if (stateBo.equals(releaseSuspendStateBo)) {
            throw new CommonException("工单号[" + shopOrder + "],状态是“下达暂停”，不允许生产", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
    }

    private String checkOperationByProcess1(String operationBo, String processInfoByRouterProcess) {
        JSONObject jsonObject = JSONObject.parseObject(processInfoByRouterProcess);
        JSONArray nodeList = jsonObject.getJSONArray("nodeList");
        String currentId = null;
        String startId = null;
        for (int i = 0; i < nodeList.size(); i++) {
            JSONObject job = nodeList.getJSONObject(i);
            if (operationBo.equals(job.getString("operation"))) {
                currentId = job.getString("id");

            }
            if ("timer".equals(job.getString("type"))) {
                startId = job.getString("id");
            }
        }
        if (StringUtils.isEmpty(currentId)) {
            throw new CommonException("当前工序不存在", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        if (StringUtils.isEmpty(startId)) {
            throw new CommonException("开始工序不存在", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        JSONArray lineList = jsonObject.getJSONArray("lineList");
        String nextId = null;
        for (int i = 0; i < lineList.size(); i++) {
            JSONObject job = lineList.getJSONObject(i);
            String from = job.getString("from");
            String to = job.getString("to");
            if (currentId.equals(from)) {
                nextId = to;
            }
        }
        if (StringUtils.isEmpty(nextId)) {
            throw new CommonException("下工序不存在", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        for (int i = 0; i < nodeList.size(); i++) {
            JSONObject job = nodeList.getJSONObject(i);
            String id = job.getString("id");
            if (nextId.equals(id)) {
                return job.getString("name");
            }
        }
        return null;

    }


    /**
     * 产品完工更新 是否已完工0未完工 1已完工
     * 查询工单表修改完成数量  COMPLETE_QTY
     *
     * @param dto dto
     * @return ResponseData.class
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    @Override
    public ResponseData<Boolean> productFinishUpdate(ProductFinishUpdateDto dto) {
        String site = dto.getSite();
        site = StringUtils.isEmpty(site) ? UserUtils.getSite() : site;

        String sn = dto.getSn();
        if (StringUtils.isEmpty(sn)) {
            throw new CommonException("sn不能为空", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        int status = 1;
        ResponseData<List<MeProductStatusVo>> responseData = momLabelService.findProductStatusBySnAndStatus(sn, status);
        if (responseData == null) {
            throw new CommonException("未获取到产品状态管理相关数据", CommonExceptionDefinition.BASIC_EXCEPTION);
        } else {
            String code = responseData.getCode();
            String msg = responseData.getMsg();
            List<MeProductStatusVo> dataData = responseData.getData();
            if (SUCCESSFUL.equals(code)) {
                if (CollectionUtils.isEmpty(dataData)) {
                    throw new CommonException("产品状态管理相关数据为空", CommonExceptionDefinition.BASIC_EXCEPTION);
                }
                List<UpdateDoneDto> updateDoneDtos = new ArrayList<>();
                List<MeProductStatusVo> collect = dataData.stream().filter(distinctByKey(MeProductStatusVo::getShopOrderBo)).collect(Collectors.toList());
                for (MeProductStatusVo data : dataData
                ) {

                    String productStateBo = data.getProductStateBo();
                    Integer done = 1;
                    UpdateDoneDto updateDoneDto = new UpdateDoneDto();
                    updateDoneDto.setProductStateBo(productStateBo);
                    updateDoneDto.setDone(done);
                    updateDoneDtos.add(updateDoneDto);
                }
                String productFinishBo = "STATE:" + site + ",504";
                for (MeProductStatusVo data : collect
                ) {
                    ShopOrder shopOrders = getShopOrders(site, data.getShopOrderBo());
                    String stateBo = shopOrders.getStateBo();

                    //获取工单啊完成状态
                    if (productFinishBo.equals(stateBo)) {
                        throw new CommonException("该工单已经是已完成状态", CommonExceptionDefinition.BASIC_EXCEPTION);
                    }


                    //获取工单的
                    String bo = shopOrders.getBo();
                    shopOrderMapper.updateShopOrderCompleteQtyByBO(bo, new BigDecimal(1));
                    //工单数量 是否等于已完工数量
                    BigDecimal completeQty = shopOrders.getCompleteQty();
                    if (null == completeQty) {
                        completeQty = new BigDecimal(0);
                    }
                    //工单数量
                    BigDecimal orderQty = shopOrders.getOrderQty();
                    if (null == orderQty) {
                        orderQty = new BigDecimal(0);
                    }
                    if (orderQty.compareTo(completeQty) < 0) {
                        //比较小于0
                        shopOrderMapper.updateProductState(bo, productFinishBo);
                    }
                }

                ResponseData responseData1 = momLabelService.updateProductStatusDoneByBo(updateDoneDtos);
                if (responseData1 != null && Boolean.FALSE.equals(SUCCESSFUL.equals(responseData1.getCode()))) {
                    throw new CommonException("修改产品管理是否完工标识失败", CommonExceptionDefinition.BASIC_EXCEPTION);
                }
                //修改采集记录数据为成功
                collectionRecordService.updateCollectionRecord(site, sn);
                return ResponseData.success();
            } else {
                throw new CommonException(msg, CommonExceptionDefinition.BASIC_EXCEPTION);
            }
        }

    }

    private Operation queryOperation(String operationBo) {
        LambdaQueryWrapper<Operation> query = new QueryWrapper<Operation>().lambda()
                .eq(Operation::getBo, operationBo);
        return operationMapper.selectOne(query);

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
     * 查询条码对应的工艺路线信息， 先根据sn查询me_sn_router  未找到则根据工单查询 m_order_router
     *
     * @param mesnRouter  条码工艺路线信息
     * @param shopOrderBo 工单bo
     * @return 工艺路线Json信息
     */
    private String getProcessInfoBySnOrShopOrder(MeSnRouter mesnRouter, String shopOrderBo) {
        String processInfo = null;

        if (mesnRouter != null) {
            processInfo = mesnRouter.getJson();
        }

        if (StrUtil.isBlank(processInfo)) {
            processInfo = getProcessInfoByRouterProcess(shopOrderBo);
        }
        return processInfo;
    }

    /**
     * 获取工业路线流程图对应的流程信息
     *
     * @param shopOrderBo 工单bo
     * @return String.class
     */
    private String getProcessInfoByRouterProcess(String shopOrderBo) {
        //查询工单路线表
        LambdaQueryWrapper<OrderRouter> orderRouterLambdaQueryWrapper = new QueryWrapper<OrderRouter>().lambda()
                .eq(OrderRouter::getShopOrderBo, shopOrderBo);
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
                .eq(ShopOrder::getSite, site)
                .eq(ShopOrder::getBo, shopOrder);
        ShopOrder shopOrders = shopOrderMapper.selectOne(shopOrderQuery);
        if (null == shopOrders) {
            throw new CommonException("工单数据为空", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return shopOrders;
    }

    private Predicate<? super MeProductStatusVo> distinctByKey(Function<? super MeProductStatusVo, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>(19);
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * // 判断下一工序是否存在,
     * 如果产品状态管理有指定下工序， 判断当前工位对应的工序是否等于下工序
     * 如果产品状态管理没有指定下工序， 判断当前工位对应的工序是否等于开始节点后第一个节点工序
     *
     * @param operationBo     当前工位对应工序bo
     * @param process         工艺路线
     * @param nextOperationBo 下工序bo
     * @param nextOperation   下工序名称
     * @return String.class
     */

    private String checkOperationByProcess(String operationBo, String process, String nextOperationBo, String nextOperation) {
        //当前工位对应的工序等于下工序, 返回
        if (StrUtil.equals(operationBo, nextOperationBo)) {
            return null;
        }
        if (StrUtil.isNotBlank(nextOperationBo)) {
            return nextOperation;
        }

        JSONObject jsonObject = JSONObject.parseObject(process);
        JSONArray nodeList = jsonObject.getJSONArray("nodeList");
        String currentId = null;
        String startId = null;
        for (int i = 0; i < nodeList.size(); i++) {
            JSONObject job = nodeList.getJSONObject(i);
            if (operationBo.equals(job.getString("operation"))) {
                currentId = job.getString("id");

            }
            if ("timer".equals(job.getString("type"))) {
                startId = job.getString("id");
            }
        }
        if (StringUtils.isEmpty(currentId)) {
            throw new CommonException("当前工序不存在", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        if (StringUtils.isEmpty(startId)) {
            throw new CommonException("开始工序不存在", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        JSONArray lineList = jsonObject.getJSONArray("lineList");
        //当前工艺路线开始工序后的第一个工序
        String firstNodeId = null;
        for (int i = 0; i < lineList.size(); i++) {
            JSONObject job = lineList.getJSONObject(i);
            String from = job.getString("from");
            String to = job.getString("to");
            if (startId.equals(from)) {
                firstNodeId = to;
            }
        }
        //判断当前工序是否等于工艺路线开始节点后的第一个工序， 相等则返回通过， 否则返回正确的下工序名称
        if (StrUtil.equals(currentId, firstNodeId)) {
            return null;
        }
        if (StringUtils.isEmpty(firstNodeId)) {
            throw new CommonException("下工序不存在", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        for (int i = 0; i < nodeList.size(); i++) {
            JSONObject job = nodeList.getJSONObject(i);
            String id = job.getString("id");
            if (firstNodeId.equals(id)) {
                return job.getString("name");
            }
        }
        return null;

    }
}

package com.itl.mes.me.provider.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.itl.mes.me.api.bo.OrderAndCodeRuleBO;
import com.itl.mes.me.api.dto.ShopOrderPackDTO;
import com.itl.mes.me.api.entity.ShopOrderPackReal;
import com.itl.mes.me.api.entity.ShopOrderPackSnReal;
import com.itl.mes.me.api.entity.ShopOrderPackSnTemp;
import com.itl.mes.me.api.entity.ShopOrderPackTemp;
import com.itl.mes.me.api.vo.ShopOrderPackRealTreeVO;
import com.itl.mes.me.api.vo.ShopOrderPackRealVO;
import com.itl.mes.me.api.vo.ShopOrderPackTemTreeVO;
import com.itl.mes.me.api.vo.ShopOrderPackTempVO;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.mapper.QueryWrapperUtil;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.Assert;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.CodeRuleHandleBO;
import com.itl.mes.core.api.bo.ItemHandleBO;
import com.itl.mes.core.api.bo.ShopOrderHandleBO;
import com.itl.mes.core.api.bo.StationHandleBO;
import com.itl.mes.core.api.constant.BOPrefixEnum;
import com.itl.mes.core.api.constant.TemporaryDataTypeEnum;
import com.itl.mes.core.api.dto.CodeGenerateDto;
import com.itl.mes.core.api.entity.TemporaryData;
import com.itl.mes.core.api.vo.ShopOrderFullVo;
import com.itl.mes.core.api.vo.ShopOrderPackRuleDetailVo;
import com.itl.mes.core.client.service.CodeRuleService;
import com.itl.mes.core.client.service.ItemFeignService;
import com.itl.mes.core.client.service.ShopOrderService;
import com.itl.mes.core.client.service.TemporaryDataService;
import com.itl.mes.me.api.entity.Pack;
import com.itl.mes.me.api.entity.PackRuleTemp;
import com.itl.mes.me.api.entity.PackTemp;
import com.itl.mes.me.api.service.OperationService;
import com.itl.mes.me.api.service.PackRuleTempService;
import com.itl.mes.me.api.service.PackService;
import com.itl.mes.me.api.service.PackTempService;
import com.itl.mes.me.api.util.GeneratorId;
import com.itl.mes.me.provider.mapper.*;
import com.itl.mom.label.api.entity.label.Sn;
import com.itl.mom.label.client.service.SnService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 包装
 *
 * @author cch
 * @date 2021-06-16
 */
@Service
@AllArgsConstructor
public class PackTempServiceImpl extends ServiceImpl<PackTempMapper, PackTemp> implements PackTempService {
    private PackRuleTempService packRuleTempService;
    private OperationService operationService;
    private ShopOrderService shopOrderService;
    private SnService snService;
    private ItemFeignService itemFeignService;
    private PackService packService;
    private CodeRuleService codeRuleService;
    private ShopOrderPackTempMapper shopOrderPackTempMapper;
    private ShopOrderPackTempSnMapper shopOrderPackTempSnMapper;

    private ShopOrderPackRealMapper shopOrderPackRealMapper;
    private ShopOrderPackRealSnMapper shopOrderPackRealSnMapper;

    private TemporaryDataService temporaryDataService;


    @Override
    public List<ShopOrderPackRealTreeVO> getPackReal(ShopOrderPackDTO shopOrderPackDTO) {
        String site = UserUtils.getSite();
//        String station = UserUtils.getStation();
//        StationHandleBO stationHandleBO = new StationHandleBO(site, station);
        //返回最新的工位,工单包装数据
        QueryWrapper query = new QueryWrapper();
        query.eq("a.site", site);
//        query.eq("a.station_bo", stationHandleBO.getBo());
        List<ShopOrderPackRealVO> shopOrderPackReals = shopOrderPackRealMapper.getPackReal(query);
        QueryWrapper<ShopOrderPackSnReal> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().isNotNull(ShopOrderPackSnReal::getSn)
                .eq(ShopOrderPackSnReal::getSite, site);
//                .eq(ShopOrderPackSnReal::getStationBo, stationHandleBO.getBo());
        QueryWrapperUtil.getMatch(queryWrapper, StrUtil.isNotBlank(shopOrderPackDTO.getShopOrder()), "shop_order", shopOrderPackDTO.getShopOrder());
        QueryWrapperUtil.getMatch(queryWrapper, StrUtil.isNotBlank(shopOrderPackDTO.getSn()), "sn", shopOrderPackDTO.getSn());
        List<ShopOrderPackSnReal> shopOrderPackSnReals = shopOrderPackRealSnMapper.selectList(queryWrapper);
        if (CollectionUtil.isEmpty(shopOrderPackSnReals)) {
            return new ArrayList<>();
        }
        Map<String, List<ShopOrderPackSnReal>> collect = shopOrderPackSnReals.stream().collect(Collectors.groupingBy(ShopOrderPackSnReal::getShopOrder));
        //返回
        List<ShopOrderPackRealTreeVO> result = new ArrayList<>();
        List<ShopOrderPackRealVO> packList = new ArrayList<>();
        shopOrderPackReals.stream().collect(Collectors.groupingBy(ShopOrderPackRealVO::getShopOrder))
                .forEach((k, value) -> {
                    if (collect.containsKey(k)) {
                        List<String> packNos = collect.get(k).stream().map(ShopOrderPackSnReal::getPackNo).distinct().collect(Collectors.toList());
                        AtomicReference<List<String>> temps = new AtomicReference<>(packNos);
                        value.stream().collect(Collectors.groupingBy(ShopOrderPackRealVO::getPackLevel))
                                .entrySet().stream().sorted(Map.Entry.<String, List<ShopOrderPackRealVO>>comparingByKey().reversed())
                                .forEachOrdered(v -> {
                                    List<String> temp = new ArrayList<>();
                                    for (ShopOrderPackRealVO shopOrderPackRealVO : v.getValue()) {
                                        if (temps.get().contains(shopOrderPackRealVO.getPackNo())) {
                                            temp.add(shopOrderPackRealVO.getPackParentNo());
                                            packList.add(shopOrderPackRealVO);
                                        }
                                    }
                                    temps.set(temp);
                                });
                    }
                });
        packList.stream().collect(Collectors.groupingBy(ShopOrderPackRealVO::getShopOrder))
                .forEach((k, v) -> {
                    ShopOrderPackRealVO shopOrderPackRealVO = v.get(0);
                    ShopOrderPackRealTreeVO treeVO = new ShopOrderPackRealTreeVO();
                    treeVO.setShopOrder(shopOrderPackRealVO.getShopOrder());
                    treeVO.setItemName(shopOrderPackRealVO.getItemName());
                    List<TreeNode<String>> nodeList = CollUtil.newArrayList();
                    for (ShopOrderPackRealVO shopOrderPackReal : v) {
                        TreeNode treeNode = new TreeNode(shopOrderPackReal.getPackNo(), shopOrderPackReal.getPackParentNo(), shopOrderPackReal.getPackName(), shopOrderPackReal.getPackLevel());
                        Map<String, Object> extra = new HashMap<>();
                        extra.put("max", shopOrderPackReal.getMax());
                        treeNode.setExtra(extra);
                        nodeList.add(treeNode);
                    }
                    if (collect.containsKey(k)) {
                        List<ShopOrderPackSnReal> shopOrderPackSnReals1 = collect.get(k);
                        treeVO.setCount(shopOrderPackSnReals1.size());
                        for (ShopOrderPackSnReal snReal : shopOrderPackSnReals1) {
                            TreeNode treeNode = new TreeNode(snReal.getSn(), snReal.getPackNo(), snReal.getSn(), "");
                            Map<String, Object> extra = new HashMap<>();
                            extra.put("sn", snReal.getSn());
                            extra.put("createTime", DateUtil.format(snReal.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                            treeNode.setExtra(extra);
                            nodeList.add(treeNode);
                        }
                    }
                    List<Tree<String>> treeList = TreeUtil.build(nodeList, "1");
                    treeVO.setChildren(treeList);
                    result.add(treeVO);
                });
        return result;
    }

    @Override
    @Transactional
    public void saveScanPackSn(List<String> shopOrderList) {
        String site = UserUtils.getSite();
        String station = UserUtils.getStation();
        StationHandleBO stationHandleBO = new StationHandleBO(site, station);
        List<String> shopOrderBoList = new ArrayList<>();
        for (String shopOrder : shopOrderList) {
            String shopOrderBo = BOPrefixEnum.SO.getPrefix() + ":" + site + "," + shopOrder;
            shopOrderBoList.add(shopOrderBo);
        }
        String stationBo = stationHandleBO.getBo();
        LambdaQueryWrapper<ShopOrderPackTemp> tempWrapper = Wrappers.<ShopOrderPackTemp>lambdaQuery()
                .eq(ShopOrderPackTemp::getStationBo, stationBo)
                .in(ShopOrderPackTemp::getShopOrderBo, shopOrderBoList);
        List<ShopOrderPackTemp> shopOrderPackTemps = shopOrderPackTempMapper.selectList(tempWrapper);
        LambdaQueryWrapper<ShopOrderPackSnTemp> snWrapper = Wrappers.<ShopOrderPackSnTemp>lambdaQuery()
                .eq(ShopOrderPackSnTemp::getStationBo, stationBo)
                .in(ShopOrderPackSnTemp::getShopOrder, shopOrderList);
        List<ShopOrderPackSnTemp> shopOrderPackSnTemps = shopOrderPackTempSnMapper.selectList(snWrapper);
        //存储
        List<ShopOrderPackReal> insert1 = new ArrayList<>();
        for (ShopOrderPackTemp shopOrderPackTemp : shopOrderPackTemps) {
            ShopOrderPackReal shopOrderPackReal = BeanUtil.copyProperties(shopOrderPackTemp, ShopOrderPackReal.class);
            shopOrderPackReal.setSite(site);
            insert1.add(shopOrderPackReal);
        }
        shopOrderPackRealMapper.insertBatch(insert1);
        List<ShopOrderPackSnReal> insert2 = new ArrayList<>();
        for (ShopOrderPackSnTemp shopOrderPackSnTemp : shopOrderPackSnTemps) {
            ShopOrderPackSnReal shopOrderPackSnReal = BeanUtil.copyProperties(shopOrderPackSnTemp, ShopOrderPackSnReal.class);
            shopOrderPackSnReal.setSite(site);
            insert2.add(shopOrderPackSnReal);
        }
        shopOrderPackRealSnMapper.insertBatch(insert2);
        //清除
        shopOrderPackTempMapper.delete(tempWrapper);
        shopOrderPackTempSnMapper.delete(snWrapper);
    }

    @Override
    public List<ShopOrderPackTemTreeVO> getPackTempSn() {
        String site = UserUtils.getSite();
        String station = UserUtils.getStation();
        StationHandleBO stationHandleBO = new StationHandleBO(site, station);
        //返回最新的工位,工单包装数据
        List<ShopOrderPackTempVO> shopOrderPackTemps = shopOrderPackTempMapper.getPackTempSn(stationHandleBO.getBo());
        QueryWrapper<ShopOrderPackSnTemp> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().isNotNull(ShopOrderPackSnTemp::getSn)
                .eq(ShopOrderPackSnTemp::getStationBo, stationHandleBO.getBo());
        List<ShopOrderPackSnTemp> shopOrderPackSnTemps = shopOrderPackTempSnMapper.selectList(queryWrapper);
        if (CollectionUtil.isEmpty(shopOrderPackSnTemps)) {
            return new ArrayList<>();
        }
        Map<String, List<ShopOrderPackSnTemp>> collect = shopOrderPackSnTemps.stream().collect(Collectors.groupingBy(ShopOrderPackSnTemp::getShopOrder));
        //返回
        List<ShopOrderPackTemTreeVO> result = new ArrayList<>();
        List<ShopOrderPackTempVO> packList = new ArrayList<>();
        shopOrderPackTemps.stream().collect(Collectors.groupingBy(ShopOrderPackTempVO::getShopOrder))
                .forEach((k, value) -> {
                    if (collect.containsKey(k)) {
                        List<String> packNos = collect.get(k).stream().map(ShopOrderPackSnTemp::getPackNo).distinct().collect(Collectors.toList());
                        AtomicReference<List<String>> temps = new AtomicReference<>(packNos);
                        value.stream().collect(Collectors.groupingBy(ShopOrderPackTempVO::getPackLevel))
                                .entrySet().stream().sorted(Map.Entry.<String, List<ShopOrderPackTempVO>>comparingByKey().reversed())
                                .forEachOrdered(v -> {
                                    List<String> temp = new ArrayList<>();
                                    for (ShopOrderPackTempVO shopOrderPackTempVO : v.getValue()) {
                                        if (temps.get().contains(shopOrderPackTempVO.getPackNo())) {
                                            temp.add(shopOrderPackTempVO.getPackParentNo());
                                            packList.add(shopOrderPackTempVO);
                                        }
                                    }
                                    temps.set(temp);
                                });
                    }
                });
        packList.stream().collect(Collectors.groupingBy(ShopOrderPackTempVO::getShopOrder))
                .forEach((k, v) -> {
                    ShopOrderPackTempVO shopOrderPackTempVO = v.get(0);
                    ShopOrderPackTemTreeVO treeVO = new ShopOrderPackTemTreeVO();
                    treeVO.setShopOrder(shopOrderPackTempVO.getShopOrder());
                    treeVO.setItemName(shopOrderPackTempVO.getItemName());
                    List<TreeNode<String>> nodeList = CollUtil.newArrayList();
                    for (ShopOrderPackTempVO shopOrderPackTemp : v) {
                        TreeNode treeNode = new TreeNode(shopOrderPackTemp.getPackNo(), shopOrderPackTemp.getPackParentNo(), shopOrderPackTemp.getPackName(), shopOrderPackTemp.getPackLevel());
                        Map<String, Object> extra = new HashMap<>();
                        extra.put("max", shopOrderPackTemp.getMax());
                        treeNode.setExtra(extra);
                        nodeList.add(treeNode);
                    }
                    if (collect.containsKey(k)) {
                        List<ShopOrderPackSnTemp> shopOrderPackSnTemps1 = collect.get(k);
                        treeVO.setCount(shopOrderPackSnTemps1.size());
                        for (ShopOrderPackSnTemp snTemp : shopOrderPackSnTemps1) {
                            TreeNode treeNode = new TreeNode(snTemp.getSn(), snTemp.getPackNo(), snTemp.getSn(), "");
                            Map<String, Object> extra = new HashMap<>();
                            extra.put("sn", snTemp.getSn());
                            extra.put("createTime", DateUtil.format(snTemp.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
                            treeNode.setExtra(extra);
                            nodeList.add(treeNode);
                        }
                    }
                    List<Tree<String>> treeList = TreeUtil.build(nodeList, "1");
                    treeVO.setChildren(treeList);
                    result.add(treeVO);
                });
        return result;
    }

    @Override
    @Transactional
    public void scanSnPack(String sn) {
        String site = UserUtils.getSite();
        String station = UserUtils.getStation();
        StationHandleBO stationHandleBO = new StationHandleBO(site, station);
        String stationBo = stationHandleBO.getBo();
        //1.验证
        //1.1 获取条码的工单信息以及包装规则
        List<OrderAndCodeRuleBO> orderAndCodeRuleBOList = packRuleTempService.findOrderAndPackCodeRuleInfo(sn);
        Assert.valid(CollectionUtil.isEmpty(orderAndCodeRuleBOList), "不存在相关条码数据");
        //1.2验证SN关联的工单的包装规则是否存在非最高包装层级
        OrderAndCodeRuleBO orderAndCodeRuleBO = orderAndCodeRuleBOList.get(0);
        String shopOrderBo = orderAndCodeRuleBO.getShopOrderBo();
        String shopOrder = new ShopOrderHandleBO(shopOrderBo).getShopOrder();
        String codeRuleBo = orderAndCodeRuleBO.getCodeRuleBo();
        Assert.valid(StrUtil.isBlank(codeRuleBo), "无需包装");
        //1.3不能重复录入
        ShopOrderPackSnTemp shopOrderPackSnTemp = shopOrderPackTempSnMapper.selectOne(Wrappers.<ShopOrderPackSnTemp>lambdaQuery().eq(ShopOrderPackSnTemp::getSn, sn));
        Assert.valid(shopOrderPackSnTemp != null, "该条码已经录入");
        ShopOrderPackSnReal shopOrderPackSnReal = shopOrderPackRealSnMapper.selectOne(Wrappers.<ShopOrderPackSnReal>lambdaQuery().eq(ShopOrderPackSnReal::getSn, sn));
        Assert.valid(shopOrderPackSnReal != null, "该条码已经包装");
        Integer integer = shopOrderPackTempMapper.selectCount(Wrappers.<ShopOrderPackTemp>lambdaQuery().eq(ShopOrderPackTemp::getStationBo, stationBo).eq(ShopOrderPackTemp::getShopOrderBo, shopOrderBo));
//        //1.4首次扫描需要拆箱包装规则
        if (integer == 0) {
            LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
            List<ShopOrderPackTemp> temps = new ArrayList<>();
            int mul = 1;
            for (int i = 0; i < orderAndCodeRuleBOList.size(); i++) {
                //最后一层不处理
                if ((i + 1) == orderAndCodeRuleBOList.size()) {
                    break;
                }
                OrderAndCodeRuleBO andCodeRuleBO1 = orderAndCodeRuleBOList.get(i);
                //首层生成
                if (i == 0) {
                    int maxQty = Integer.valueOf(andCodeRuleBO1.getMaxQty());
                    ResponseData<List<String>> checkNos = codeRuleService.generatorNextNumberList(
                            CodeGenerateDto.builder().codeRuleBo(codeRuleBo).number(1).build());
                    Assert.valid(checkNos == null || CollectionUtil.isEmpty(checkNos.getData()), "生成包装号失败");
                    List<String> data = checkNos.getData();
                    ShopOrderPackTemp shopOrderPackTemp = new ShopOrderPackTemp();
                    shopOrderPackTemp.setId(IdUtil.fastSimpleUUID());
                    shopOrderPackTemp.setStationBo(stationBo);
                    shopOrderPackTemp.setShopOrderBo(shopOrderBo);
                    shopOrderPackTemp.setPackNo(andCodeRuleBO1.getPackBo());
                    shopOrderPackTemp.setPackLevel(andCodeRuleBO1.getPackLevel());
                    shopOrderPackTemp.setPackNo(data.get(0));
                    shopOrderPackTemp.setPackParentNo(1 + "");
                    shopOrderPackTemp.setMax(andCodeRuleBO1.getMaxQty());
                    for (int i1 = 0; i1 < maxQty; i1++) {
                        queue.add(data.get(0));
                    }
                    temps.add(shopOrderPackTemp);
                }
                OrderAndCodeRuleBO andCodeRuleBO2 = orderAndCodeRuleBOList.get(i + 1);
                String packBo = andCodeRuleBO2.getPackBo();
                String codeRule = andCodeRuleBO2.getCodeRuleBo();
                int maxQty = StrUtil.isNotBlank(andCodeRuleBO1.getMaxQty()) ? Integer.valueOf(andCodeRuleBO1.getMaxQty()) : 0;
                //生成包装号
                maxQty = maxQty * mul;
                ResponseData<List<String>> checkNos = codeRuleService.generatorNextNumberList(
                        CodeGenerateDto.builder().codeRuleBo(codeRule).number(maxQty).build());
                Assert.valid(checkNos == null || CollectionUtil.isEmpty(checkNos.getData()), "生成包装号失败");
                List<String> data = checkNos.getData();
                for (String datum : data) {
                    ShopOrderPackTemp shopOrderPackTemp = new ShopOrderPackTemp();
                    shopOrderPackTemp.setId(IdUtil.fastSimpleUUID());
                    shopOrderPackTemp.setStationBo(stationBo);
                    shopOrderPackTemp.setShopOrderBo(shopOrderBo);
                    shopOrderPackTemp.setPackBo(packBo);
                    shopOrderPackTemp.setPackLevel(andCodeRuleBO2.getPackLevel());
                    shopOrderPackTemp.setPackNo(datum);
                    shopOrderPackTemp.setMax(andCodeRuleBO2.getMaxQty());
                    int max = Integer.valueOf(andCodeRuleBO2.getMaxQty());
                    for (int i1 = 0; i1 < max; i1++) {
                        queue.add(datum);
                    }
                    temps.add(shopOrderPackTemp);
                }
                mul = maxQty;
            }
            for (int i = 1; i < temps.size(); i++) {
                temps.get(i).setPackParentNo(queue.poll());
            }
            List<ShopOrderPackSnTemp> snList = new ArrayList<>();
            while (!queue.isEmpty()) {
                ShopOrderPackSnTemp shopOrderPackSnTemp1 = new ShopOrderPackSnTemp();
                shopOrderPackSnTemp1.setId(IdUtil.fastSimpleUUID());
                shopOrderPackSnTemp1.setPackNo(queue.poll());
                shopOrderPackSnTemp1.setStationBo(stationBo);
                shopOrderPackSnTemp1.setShopOrder(shopOrder);
                snList.add(shopOrderPackSnTemp1);
            }
            shopOrderPackTempMapper.insertBatch(temps);
            shopOrderPackTempSnMapper.insertBatch(snList);
        }
        Integer count = shopOrderPackTempSnMapper.genSnObj(stationBo, shopOrderBo, sn, new Date());

        //采集记录
        TemporaryData temporaryData = new TemporaryData();
        temporaryData.setSn(sn);
        temporaryData.setStation(station);
        temporaryData.setType(TemporaryDataTypeEnum.PACK.getCode());
        Map m = new HashMap(10);
        m.put("sn", sn);
        m.put("site", site);
        m.put("workShop", UserUtils.getWorkShop());
        m.put("station", station);
        m.put("shopOrder", shopOrder);
        m.put("userName", UserUtils.getUserName());
        temporaryData.setContent(JSONUtil.toJsonStr(m));
        temporaryDataService.addOrUpdate(temporaryData);

        if (count == 0) {
            throw new CommonException("已装满！", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
    }


    @Override
    public Map<String, Object> scanSn(String sn, String operationBo) throws CommonException {
        // 根据工序Bo查询在当前工序的包装规则
        List<PackRuleTemp> packRuleList = packRuleTempService.listByOperationBo(operationBo);
        Snowflake snowflake = new GeneratorId().getSnowflake();
        if (packRuleList.isEmpty()) {
            // 获取SN对应的工单信息
            ShopOrderFullVo shopOrderFullVo = operationService.checkShopOrder(sn);
            String shopOrderBo = shopOrderFullVo.getBo();
            // 当前工序不存在包装规则
            // 获取工单包装规则
            List<ShopOrderPackRuleDetailVo> packRuleDetailVoList = Optional.ofNullable(shopOrderService.listPackRuleDetailByShopOrderBo(shopOrderBo))
                    .orElseThrow(() -> new CommonException("服务异常!", CommonExceptionDefinition.BASIC_EXCEPTION));
            if (packRuleDetailVoList.isEmpty()) {
                throw new CommonException(String.format("未检测到工单[%s]的包装规则", shopOrderFullVo.getShopOrder()), CommonExceptionDefinition.BASIC_EXCEPTION);
            }
            // 保存到me_pack_rule_temp
            packRuleTempService.saveBatch(packRuleDetailVoList.stream().map(x -> {
                PackRuleTemp temp = new PackRuleTemp();
                temp.setBo(snowflake.nextIdStr());
                temp.setMinQty(x.getMinQty());
                temp.setMaxQty(x.getMaxQty());
                temp.setPackLevel(x.getPackLevel());
                temp.setPackName(x.getPackName());
                temp.setOperationBo(operationBo);
                temp.setSn(sn);
                temp.setRuleLabelBo(x.getRuleMouldBo());
                return temp;
            }).collect(Collectors.toList()));
            // 重新查询
            packRuleList = packRuleTempService.listByOperationBo(operationBo);
        }
        Map<String, Object> ret = Maps.newHashMap();
        // 返回包装规则
        ret.put("packRule", packRuleList);
        // 查询当前工序待保存的包装信息
        List<PackTemp> packList = this.listWaitToPack(operationBo);
        if (!sn.equals(packRuleList.get(0).getSn())) {
            // sn需要包装
            Sn snInfo = Optional.ofNullable(snService.getSnInfo(sn)).orElse(null);
            // 执行工序和sn的临时包装
            PackTemp packTemp = new PackTemp();
            String shopOrderBo;
            String itemBo;
            if (snInfo != null) {
                // sn 为工单条码
                // 获取SN对应的工单信息
                ShopOrderFullVo shopOrderFullVo = operationService.checkShopOrder(sn);
                shopOrderBo = shopOrderFullVo.getBo();
                itemBo = shopOrderFullVo.getItemBo();
                // 条码当前工序与工位工序是否一致
//                operationService.checkSnOperation(sn, operationBo, shopOrderFullVo);
                packTemp.setPackQty(Optional.ofNullable(snInfo.getPackingQuantity()).map(BigDecimal::intValue).orElse(1));
                packTemp.setPackLevel("2");
            } else {
                Pack pack = Optional.ofNullable(packService.lambdaQuery().eq(Pack::getSn, sn).one())
                        .orElseThrow(() -> new CommonException("扫描条码异常!", CommonExceptionDefinition.BASIC_EXCEPTION));
                packTemp.setPackLevel(pack.getPackLevel());
                packTemp.setPackQty(pack.getPackQty());
                packTemp.setPackDate(pack.getPackDate());
                shopOrderBo = pack.getShopOrderBo();
                itemBo = pack.getItemBo();
            }
            // 当前需要包装的包装等级
            Integer maxQty = packRuleList.stream()
                    .filter(x -> x.getPackLevel().equals(String.valueOf(Integer.parseInt(packTemp.getPackLevel()) + 1)))
                    .findAny().orElseThrow(() -> new CommonException("超过最高层级!", CommonExceptionDefinition.BASIC_EXCEPTION)).getMaxQty();
            ret.put("maxQty", maxQty);
            int tempToPack = 0;
            if (!packList.isEmpty()) {
                // 当前工序存在待保存的包装信息
                // 查询当前待保存的已包装当前等级
                PackTemp pack = packList.get(0);
                if (!pack.getShopOrderBo().equals(shopOrderBo)) {
                    throw new CommonException("工单信息不一致!", CommonExceptionDefinition.BASIC_EXCEPTION);
                }
                if (!pack.getPackLevel().equals(packTemp.getPackLevel())) {
                    throw new CommonException("扫描条码/箱号异常!", CommonExceptionDefinition.BASIC_EXCEPTION);
                }
                tempToPack += packList.size();
            }
            List<Pack> alreadyPackList = packService.listAlreadyPack(shopOrderBo, operationBo);
            if (!alreadyPackList.isEmpty()) {
                // 该层级已包装的数量
                tempToPack += alreadyPackList.stream().filter(x -> x.getPackLevel().equals(packTemp.getPackLevel())).count();
            }
            if (tempToPack > maxQty) {
                throw new CommonException("检测到超量!", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
            packTemp.setBo(snowflake.nextIdStr());
            packTemp.setSite(UserUtils.getSite());
            packTemp.setSn(sn);
            packTemp.setOperationBo(operationBo);
            packTemp.setShopOrderBo(shopOrderBo);
            packTemp.setShopOrder(new ShopOrderHandleBO(shopOrderBo).getShopOrder());
            // TODO setScheduleNo
            packTemp.setItemBo(itemBo);
            packTemp.setItem(new ItemHandleBO(itemBo).getItem());
            packTemp.setItemName(Optional.ofNullable(itemFeignService.getItemList(Sets.newHashSet(itemBo)))
                    .map(x -> x.get(0).getItemName()).orElse(null));
            this.save(packTemp);
            packList.add(packTemp);
        }
        // 返回该工序待保存的装箱信息
        ret.put("pack", packList);
        return ret;
    }

    @Override
    public List<PackTemp> listWaitToPack(String operationBo) {
        List<PackTemp> ret = this.lambdaQuery().eq(PackTemp::getOperationBo, operationBo)
                .list();
        if (ret == null || ret.size() == 0) {
            return Collections.emptyList();
        }
        return ret;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public String executePack(List<PackTemp> list) throws CommonException {
        if (list.isEmpty()) {
            throw new CommonException("装箱信息为空!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        int tempToPack = list.size();
        PackTemp toPack = list.get(0);
        String operationBo = toPack.getOperationBo();
        String shopOrderBo = toPack.getShopOrderBo();
        List<Pack> alreadyPackList = packService.listAlreadyPack(shopOrderBo, operationBo);
        String packLevel = toPack.getPackLevel();
        if (!alreadyPackList.isEmpty()) {
            // 该层级已包装的数量
            tempToPack += alreadyPackList.stream().filter(x -> x.getPackLevel().equals(packLevel)).count();
        }
        List<PackRuleTemp> packRuleList = packRuleTempService.listByOperationBo(operationBo);
        Integer maxQty = packRuleList.stream()
                .filter(x -> x.getPackLevel().equals(String.valueOf(Integer.parseInt(packLevel) + 1)))
                .findAny().orElseThrow(() -> new CommonException("超过最高层级", CommonExceptionDefinition.BASIC_EXCEPTION)).getMaxQty();
        if (tempToPack > maxQty) {
            throw new CommonException("检测到超量!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }

        Pack outerPack = new Pack();
        outerPack.setBo(new GeneratorId().getSnowflake().nextIdStr());
        outerPack.setSite(UserUtils.getSite());
        // TODO scheduleNo
        String packNo = Optional.ofNullable(codeRuleService.getNewsNumberSI(new CodeRuleHandleBO(UserUtils.getSite(), "PACK_NO").getBo()))
                .orElseThrow(() -> new CommonException("生成箱号异常!", CommonExceptionDefinition.BASIC_EXCEPTION));
        outerPack.setSn(packNo);
        outerPack.setPackQty(list.size());
        outerPack.setOperationBo(operationBo);
        outerPack.setShopOrderBo(shopOrderBo);
        outerPack.setItemBo(toPack.getItemBo());
        outerPack.setItemName(toPack.getItemName());
        outerPack.setPackLevel(String.valueOf(Integer.parseInt(toPack.getPackLevel()) + 1));
        outerPack.setPackDate(new Date());

        this.remove(new QueryWrapper<PackTemp>().lambda().eq(PackTemp::getOperationBo, operationBo));
        // 务必保证pack和packTemp的Bo一致
        packService.removeByIds(list.stream().map(PackTemp::getBo).collect(Collectors.toList()));
        List<Pack> packs = Lists.newArrayList(outerPack);
        list.forEach(x -> {
            Pack p = new Pack();
            BeanUtils.copyProperties(x, p);
            p.setOuterPackSn(outerPack.getSn());
            packs.add(p);
        });
        packService.saveBatch(packs);
        packRuleTempService.remove(new QueryWrapper<PackRuleTemp>().lambda().eq(PackRuleTemp::getOperationBo, operationBo));
        return packNo;
    }
}

package com.itl.mom.label.provider.impl.label;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.mapper.QueryWrapperUtil;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.response.ResultResponseEnum;
import com.itl.iap.common.base.utils.Assert;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.ShopOrderHandleBO;
import com.itl.mes.core.api.bo.SnHandleBO;
import com.itl.mes.core.api.constant.SnTypeEnum;
import com.itl.mes.core.api.constant.TemporaryDataTypeEnum;
import com.itl.mes.core.api.dto.CollectionRecordCommonTempDTO;
import com.itl.mes.core.api.entity.Item;
import com.itl.mes.core.api.entity.OrderRouter;
import com.itl.mes.core.api.entity.OrderRouterProcess;
import com.itl.mes.core.api.entity.TemporaryData;
import com.itl.mes.core.api.vo.ShopOrderFullVo;
import com.itl.mes.core.client.service.ItemFeignService;
import com.itl.mes.core.client.service.OrderRouterService;
import com.itl.mes.core.client.service.ShopOrderService;
import com.itl.mes.core.client.service.TemporaryDataService;
import com.itl.mes.me.api.dto.UpdateSnRouteDto;
import com.itl.mes.me.client.service.MeSnRouterService;
import com.itl.mom.label.api.dto.label.*;
import com.itl.mom.label.api.dto.ruleLabel.LabelPrintBarCodeDto;
import com.itl.mom.label.api.dto.ruleLabel.RuleLabelShowDto;
import com.itl.mom.label.api.entity.MeProductStatus;
import com.itl.mom.label.api.entity.label.LabelPrint;
import com.itl.mom.label.api.entity.label.LabelPrintLog;
import com.itl.mom.label.api.entity.label.Sn;
import com.itl.mom.label.api.entity.label.SnItem;
import com.itl.mom.label.api.entity.ruleLabel.RuleLabel;
import com.itl.mom.label.api.entity.ruleLabel.RuleLabelDetail;
import com.itl.mom.label.api.enums.LabelPrintLogStateEnum;
import com.itl.mom.label.api.service.MeProductStatusService;
import com.itl.mom.label.api.service.label.LabelPrintLogService;
import com.itl.mom.label.api.service.label.LabelPrintService;
import com.itl.mom.label.api.service.label.LabelService;
import com.itl.mom.label.api.service.label.SnService;
import com.itl.mom.label.api.service.ruleLabel.RuleLabelDetailService;
import com.itl.mom.label.api.service.ruleLabel.RuleLabelService;
import com.itl.mom.label.api.vo.ItemLabelListVo;
import com.itl.mom.label.api.vo.LabelPrintVo;
import com.itl.mom.label.api.vo.ScanReturnVo;
import com.itl.mom.label.provider.constant.ElementConstant;
import com.itl.mom.label.provider.enums.ElementEnum;
import com.itl.mom.label.provider.enums.IsComplementEnum;
import com.itl.mom.label.provider.enums.LabelTemplateTypeEnum;
import com.itl.mom.label.provider.mapper.label.LabelPrintMapper;
import com.itl.mom.label.provider.mapper.label.SnItemMapper;
import com.itl.mom.label.provider.mapper.label.SnMapper;
import com.itl.mom.label.provider.mapper.ruleLabel.RuleLabelMapper;
import com.itl.mom.label.provider.utils.CommonUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 标签打印范围 服务实现类
 * </p>
 *
 * @author zhongfei
 * @since 2021-01-20
 */
@Service
@Slf4j
public class LabelPrintServiceImpl extends ServiceImpl<LabelPrintMapper, LabelPrint> implements LabelPrintService {

    @Autowired
    LabelPrintMapper labelPrintMapper;

    @Autowired
    SnService snService;

    @Autowired
    SnMapper snMapper;


    @Autowired
    RuleLabelService ruleLabelService;

    @Autowired
    RuleLabelMapper ruleLabelMapper;

    @Autowired
    RuleLabelDetailService ruleLabelDetailService;

    @Autowired
    LabelService labelService;

    @Autowired
    LabelPrintLogService labelPrintLogService;

    @Autowired
    private ShopOrderService shopOrderService;

    @Autowired
    private SnItemMapper snItemMapper;
    @Autowired
    private MeProductStatusService meProductStatusService;

    @Autowired
    private ItemFeignService itemFeignService;

    @Autowired
    CommonUtils commonUtils;
    @Autowired
    private OrderRouterService orderRouterService;
    @Autowired
    private MeSnRouterService meSnRouterService;
    @Autowired
    private TemporaryDataService temporaryDataService;

    @Override
    public void hangup(ItemLabelQueryUpDTO itemLabelQueryUpDTO) {
        List<SnItem> updateData = new ArrayList<>();
        String sfgq = itemLabelQueryUpDTO.getSfgq();
        Assert.valid(!"N".equals(sfgq) && !"Y".equals(sfgq), "状态码值错误");
        for (String id : itemLabelQueryUpDTO.getIdList()) {
            SnItem snItem = new SnItem();
            snItem.setId(id);
            snItem.setSfgq(sfgq);
            updateData.add(snItem);
        }
        snItemMapper.updateBatchById(updateData);
    }

    /**
     * 根据id返回数据
     *
     * @param labelPrintBo
     * @return
     */

    @Override
    public ResponseData<LabelPrint> getLabelPrintBo(String labelPrintBo) {
        LambdaQueryWrapper<LabelPrint> queryWrapper = new QueryWrapper<LabelPrint>().lambda()
                .eq(LabelPrint::getBo, labelPrintBo);
        LabelPrint labelPrints = labelPrintMapper.selectOne(queryWrapper);
        return ResponseData.success(labelPrints);
    }

    @Override
    public ResponseData<List<String>> queryCodeByItem(List<String> itemCode) {
       List<String> list = labelPrintMapper.queryCodeByItem(itemCode);
       return ResponseData.success(list);
    }

    @Override
    public ResponseData<String> queryItemBySn(String sn) {
       String result =  labelPrintMapper.queryItemByCode(sn);
        return ResponseData.success(result);
    }

    @Override
    public String queryShopOrderBySn(String sn) {
        return labelPrintMapper.queryShopOrderBoBySn(sn);
    }

    @Override
    public IPage<ItemLabelListVo> getItemLabelPageList(ItemLabelQueryDTO itemLabelQueryDTO) {
        itemLabelQueryDTO.setSite(UserUtils.getSite());
        QueryWrapper queryWrapper = QueryWrapperUtil.convertQuery(itemLabelQueryDTO);
        IPage<ItemLabelListVo> datas = labelPrintMapper.getItemLabelPageList(itemLabelQueryDTO.getPage(), queryWrapper);
        return datas;
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    public List<Sn> addLabelPrint(LabelPrintSaveDto labelPrintSaveDto) throws CommonException, SQLException {
        //校验元素类型是否合法
        Boolean elementTypeCheck = false;
        for (ElementEnum elementEnum : ElementEnum.values()) {
            if (elementEnum.getType().equals(labelPrintSaveDto.getElementType())) {
                elementTypeCheck = true;
            }
        }
        if (!elementTypeCheck) {
            throw new CommonException("不支持的元素类型", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        LabelPrint labelPrint = new LabelPrint();
        String site = UserUtils.getSite();
        BeanUtil.copyProperties(labelPrintSaveDto, labelPrint);
        String labelPrintId = UUID.randomUUID().toString().replace("-", "");
        labelPrint.setBo(labelPrintId);

        RuleLabel itemRuleLabel = ruleLabelMapper.selectById(labelPrintSaveDto.getRuleLabelBo());
        try {
            if (ObjectUtil.isEmpty(itemRuleLabel)) {
                throw new RuntimeException("未找到对应的物料标签模板，请先维护物料标签模板");
            }
        } catch (Exception e) {
            throw new CommonException(e.getMessage(), CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        List<RuleLabelDetail> list = ruleLabelDetailService.list(
                new QueryWrapper<RuleLabelDetail>()
                        .lambda()
                        .eq(RuleLabelDetail::getIrlBo, itemRuleLabel.getBo()));

        //处理工单标签数量变更
        if (ElementEnum.SHOP_ORDER.getType().equals(labelPrintSaveDto.getElementType())) {
            String shopOrderNum = labelPrintSaveDto.getElementBo().split(",")[1];
            ResponseData<ShopOrderFullVo> shopOrderData = shopOrderService.getShopOrder(shopOrderNum);
            if (!ResultResponseEnum.SUCCESS.getCode().equals(shopOrderData.getCode())) {
                throw new RuntimeException(shopOrderData.getMsg());
            }
            ShopOrderFullVo shopOrderFullVo = shopOrderData.getData();
            BigDecimal orderSum = NumberUtil.add(shopOrderFullVo.getOrderQty(), shopOrderFullVo.getOverfulfillQty());
            if (orderSum.compareTo(NumberUtil.add(shopOrderFullVo.getLabelQty(), new BigDecimal(labelPrintSaveDto.getBarCodeAmount()))) < 0) {
                throw new RuntimeException("打印数量超出工单可打印数量");
            }
        }

        //定义编码规则参数Map,会自动去替换编码规则的值
        Map<String, Object> ruleLabelParams = JSONObject.parseObject(labelPrintSaveDto.getRuleLabelParams());
        List<String> codeList = ruleLabelService.generatorCode(itemRuleLabel, list, labelPrintSaveDto.getCreateSnElementBo(), labelPrintSaveDto.getBarCodeAmount(), ruleLabelParams);
        Date date = new Date();
//        String userName = UserUtils.getCurrentUser().getUserName();
        String userName = "admin";
        List<Sn> sns = new ArrayList<>();
        //定义标签参数Map，用于后面取值去生成标签
        Map<String, Object> labelParams = ruleLabelService.getLabelParams(itemRuleLabel, list, site, labelPrintSaveDto.getElementBo());
        JSONObject.parseObject(labelPrintSaveDto.getLabelParams()).forEach((k, v) -> labelParams.put(k, v));
        for (String code : codeList) {
            if (code.equals(codeList.get(0))) {
                labelPrint.setStartCode(code);
            } else if (code.equals(codeList.get(codeList.size() - 1))) {
                labelPrint.setEndCode(code);
            }
            Sn sn = new Sn();
            String bo = new SnHandleBO(site, code).getBo();
            sn.setBo(bo);
            if (IsComplementEnum.Y.getCode().equals(labelPrintSaveDto.getIsComplement())) {
                sn.setComplementCodeState("Y");
            } else {
                sn.setComplementCodeState("N");
            }
            sn.setState(SnTypeEnum.NEW.getCode());
            sn.setSite(site);
            sn.setLabelPrintBo(labelPrintId);
            sn.setSn(code);
            //遍历标签参数，当有数据未赋值时，将编码规则生成的code赋值上
            Map<String, Object> paramsMap = new HashMap<>();
            for (String key : labelParams.keySet()) {
                if (ObjectUtil.isEmpty(labelParams.get(key))) {
                    paramsMap.put(key, code);
                } else {
                    paramsMap.put(key, labelParams.get(key));
                }
            }
            if (labelPrintSaveDto.getElementType().equals(ElementEnum.ITEM.getType())) {
                sn.setItemBo(labelPrintSaveDto.getElementBo());
            }
            //将标签数据转成json字符串存到数据库，以方便后面生成pdf文件
            sn.setLabelParams(JSON.toJSONString(paramsMap));
            sn.setCreateDate(date);
            sn.setPackingMaxQuantity(labelPrintSaveDto.getMaxQty());
            sn.setPackingQuantity(labelPrintSaveDto.getPackingQuantity());
            sns.add(sn);
        }
        SqlSession sqlSession = null;
        Connection conn = null;
        try {
            sqlSession = SqlHelper.sqlSessionBatch(this.currentModelClass());
            conn = sqlSession.getConfiguration().getEnvironment().getDataSource().getConnection();
            //关闭自动提交，即开启事务
            conn.setAutoCommit(false);
            String sql = "INSERT INTO z_sn (BO, SITE, SN, COMPLEMENT_CODE_STATE, ITEM_BO, STATE, CREATE_DATE, LABEL_PRINT_BO, LABEL_PARAMS, PACKING_QUANTITY, PACKING_MAX_QUANTITY) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement psInsert = conn.prepareStatement(sql);
            int index = 0;
            for (Sn pay : sns) {
                index++;
                psInsert.setString(1, pay.getBo());
                psInsert.setString(2, pay.getSite());
                psInsert.setString(3, pay.getSn());
                psInsert.setString(4, pay.getComplementCodeState());
                psInsert.setString(5, pay.getItemBo());
                psInsert.setString(6, pay.getState());
                psInsert.setTimestamp(7, new Timestamp(pay.getCreateDate().getTime()));
                psInsert.setString(8, pay.getLabelPrintBo());
                psInsert.setString(9, pay.getLabelParams());
                psInsert.setBigDecimal(10, pay.getPackingQuantity());
                psInsert.setBigDecimal(11, pay.getPackingMaxQuantity());
                psInsert.addBatch();
                if (index > 0 && index % 200 == 0) {
                    psInsert.executeBatch();
                    conn.commit();
                    psInsert.clearBatch();
                }
            }
            psInsert.executeBatch();
            conn.commit();
            psInsert.close();
            labelPrint.setCreateUser(userName);
            labelPrint.setCreateDate(date);
            labelPrint.setSite(site);
            labelPrintMapper.insert(labelPrint);
            //处理工单标签数量变更
            if (ElementEnum.SHOP_ORDER.getType().equals(labelPrintSaveDto.getElementType())) {
                shopOrderService.updateShopOrderLabelQtyByBO(labelPrintSaveDto.getElementBo(), new BigDecimal(labelPrintSaveDto.getBarCodeAmount()));
                //保存条码工艺路线
                ResponseData<OrderRouter> orderRouterResult = orderRouterService.getOrderRouter(labelPrintSaveDto.getElementBo());
                String processInfo = null;
                if(orderRouterResult.isSuccess()) {
                    OrderRouter orderRouter = orderRouterResult.getData();
                    if(orderRouter != null) {
                        OrderRouterProcess orderRouterProcess = orderRouter.getOrderRouterProcess();
                        if(orderRouterProcess != null) {
                            processInfo = orderRouterProcess.getProcessInfo();
                        }
                    }
                }
                if(StrUtil.isNotBlank(processInfo)) {
                    UpdateSnRouteDto updateSnRouteDto = new UpdateSnRouteDto();
                    updateSnRouteDto.setSite(site);
                    updateSnRouteDto.setProcessInfo(processInfo);
                    updateSnRouteDto.setSnList(codeList);
                    meSnRouterService.addSnRoute(updateSnRouteDto);
                }
            }

            //新增物料标签
            if (ElementEnum.ITEM.getType().equals(labelPrintSaveDto.getElementType())) {
                String itemBo = sns.get(0).getItemBo();
                Set set = new HashSet();
                set.add(itemBo);
                List<Item> itemList = itemFeignService.getItemList(set);
                BigDecimal sysl = null;
                if (CollectionUtil.isNotEmpty(itemList)) {
                    sysl = itemList.get(0).getLotSize();
                }
                List<SnItem> items = new ArrayList<>();
                for (Sn sn : sns) {
                    SnItem snItem = new SnItem();
                    snItem.setId(IdUtil.fastSimpleUUID())
                            .setSnBo(sn.getBo())
                            .setItemBo(sn.getItemBo())
                            .setSfgq("N")
                            .setSite(site)
                            .setSysl(sysl);
                    items.add(snItem);
                }
                snItemMapper.insertBatch(items);
            }

        } catch (RuntimeException e) {
            sqlSession.rollback();
            conn.rollback();
            throw new RuntimeException("保存条码失败，请检查需要生成的数据是否冲突");
        } finally {
            conn.setAutoCommit(true);
            conn.close();
            sqlSession.close();
        }
        return sns;

    }

    @Override
    public IPage<LabelPrintVo> findList(LabelPrintQueryDto labelPrintQueryDto) throws CommonException {
        if (ObjectUtil.isEmpty(labelPrintQueryDto.getPage())) {
            labelPrintQueryDto.setPage(new Page(0, 10));
        }
        labelPrintQueryDto.setSite(UserUtils.getSite());
        IPage<LabelPrintVo> labelPrintVoIPage;
        switch (labelPrintQueryDto.getElementType()) {
            case ElementConstant.ITEM:
                labelPrintQueryDto.setTableName(ElementEnum.ITEM.getTableName());
                labelPrintQueryDto.setQueryColumn(ElementEnum.ITEM.getQueryColumn());
                labelPrintVoIPage = labelPrintMapper.findList(labelPrintQueryDto.getPage(), labelPrintQueryDto);
                break;
            case ElementConstant.DEVICE:
                labelPrintQueryDto.setTableName(ElementEnum.DEVICE.getTableName());
                labelPrintQueryDto.setQueryColumn(ElementEnum.DEVICE.getQueryColumn());
                labelPrintVoIPage = labelPrintMapper.findList(labelPrintQueryDto.getPage(), labelPrintQueryDto);
                break;
            case ElementConstant.CARRIER:
                labelPrintQueryDto.setTableName(ElementEnum.CARRIER.getTableName());
                labelPrintQueryDto.setQueryColumn(ElementEnum.CARRIER.getQueryColumn());
                labelPrintVoIPage = labelPrintMapper.findList(labelPrintQueryDto.getPage(), labelPrintQueryDto);
                break;
            case ElementConstant.SHOP_ORDER:
                labelPrintQueryDto.setTableName(ElementEnum.SHOP_ORDER.getTableName());
                labelPrintQueryDto.setQueryColumn(ElementEnum.SHOP_ORDER.getQueryColumn());
                labelPrintVoIPage = labelPrintMapper.findList(labelPrintQueryDto.getPage(), labelPrintQueryDto);
                break;
            case ElementConstant.PACKING:
                labelPrintQueryDto.setTableName(ElementEnum.PACKING.getTableName());
                labelPrintQueryDto.setQueryColumn(ElementEnum.PACKING.getQueryColumn());
                labelPrintVoIPage = labelPrintMapper.findPackingLabelPrint(labelPrintQueryDto.getPage(), labelPrintQueryDto);
                break;
            default:
                throw new CommonException("不支持的元素类型", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        return labelPrintVoIPage;
    }

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> barCodePrint(LabelPrintBarCodeDto barCodeDto) throws CommonException, SQLException {

        barCodeDto.setBo(barCodeDto.getBo().replace("=", ""));
        LabelPrint labelPrint = labelPrintMapper.selectById(barCodeDto.getBo());

        //当标签打印是不补码并且有打印过的时候，需要提示不可补码
        if (labelPrint.getIsComplement().equals(IsComplementEnum.N.getValue()) && ObjectUtil.isNotEmpty(labelPrint.getPrintDate())) {

            throw new CommonException("该标签不可补码", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        RuleLabel ruleLabel = ruleLabelMapper.selectById(labelPrint.getRuleLabelBo());

        if (ObjectUtil.isEmpty(ruleLabel)) {

            throw new CommonException("未找到对应的规则模板，未找到对应的规则模板", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        Date date = new Date();
        String userName = UserUtils.getCurrentUser().getUserName();
        labelPrint.setPrintDate(date);
        labelPrintMapper.updateById(labelPrint);

        QueryWrapper<Sn> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("label_print_bo", barCodeDto.getBo());
        if (StrUtil.isNotBlank(barCodeDto.getStartBarCode()) && StrUtil.isNotBlank(barCodeDto.getEndBarCode())) {
            queryWrapper.between(Sn.SN, barCodeDto.getStartBarCode(), barCodeDto.getEndBarCode());
        }
        queryWrapper.orderByAsc(Sn.SN);
        List<Sn> sns = snMapper.selectList(queryWrapper);
        String elementType = labelPrint.getElementType();
        String productName = null, productCode = null, shopOrder = null;
        if(StrUtil.equals(ElementEnum.SHOP_ORDER.getType(), elementType)) {
            String elementBo = labelPrint.getElementBo();
            shopOrder = new ShopOrderHandleBO(elementBo).getShopOrder();
            ResponseData<ShopOrderFullVo> result = shopOrderService.getShopOrder(shopOrder);
            if(result.isSuccess()) {
                ShopOrderFullVo shopOrderFullVo = result.getData();
                productName = shopOrderFullVo.getItemName();
                productCode = shopOrderFullVo.getItem();
            }
        }
        String finalProductName = productName, finalProductCode = productCode, finalShopOrder = shopOrder;
        sns.forEach(x -> meProductStatusService.save(new MeProductStatus().setShopOrder(finalShopOrder).setSnBo(x.getBo()).setProductCode(finalProductCode).setProductName(finalProductName)));
        sns = sns.stream().filter(x -> !SnTypeEnum.SCRAPPED.getCode().equals(x.getState())).collect(Collectors.toList());
        List<LabelPrintLog> labelPrintLogs = new ArrayList<>();

        List<Map<String, Object>> mapList = new ArrayList<>();
        //返回的字符串集合，不同模板类型的返回值不一样
        List<String> respLists = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(sns)) {
            Map<String, Object> map = null;
            Sn sn = null;
            List<Sn> snList = new ArrayList<>();
            ListIterator<Sn> detailListIterator = sns.listIterator();
            while (detailListIterator.hasNext()) {
                //处理并行数据
                if (CollectionUtil.isNotEmpty(barCodeDto.getLabelParamsList())) {
                    for (int i = 0; i < barCodeDto.getParallelAmount(); i++) {
                        if (i == 0) {
                            map = JSONObject.parseObject(sns.get(detailListIterator.nextIndex()).getLabelParams());
                        }
                        if (detailListIterator.hasNext()) {
                            sn = detailListIterator.next();
                            sn.setLastPrintDate(date);
                            sn.setLastPrintUser(userName);
                            sn.setPrintCount(sn.getPrintCount() + 1);
//                            snMapper.updateById(sn);
                            snList.add(sn);
                            //日志表赋值
                            LabelPrintLog labelPrintLog = new LabelPrintLog();
                            labelPrintLog.setLabelPrintBo(labelPrint.getBo());
                            labelPrintLog.setLabelPrintDetailBo(sn.getBo());
                            labelPrintLog.setPrintDate(date);
                            labelPrintLog.setPrintUser(userName);
                            labelPrintLog.setState(LabelPrintLogStateEnum.NEW.getCode());
                            labelPrintLog.setPrintCount(1);
                            labelPrintLog.setPrinter(barCodeDto.getPrinter());
                            labelPrintLogs.add(labelPrintLog);
                            map.put(barCodeDto.getLabelParamsList().get(i), sn.getSn());
                            map.put("snForPdfName", sn.getSn());
                            map.put("fileDate", DateUtil.format(sn.getCreateDate(), "yyyy-MM-dd"));
                        }

                    }
                } else {
                    sn = detailListIterator.next();
                    sn.setLastPrintDate(date);
                    sn.setLastPrintUser(userName);
                    sn.setPrintCount(sn.getPrintCount() + 1);
//                    snMapper.updateById(sn);
                    snList.add(sn);
                    //日志表赋值
                    LabelPrintLog labelPrintLog = new LabelPrintLog();
                    labelPrintLog.setLabelPrintBo(labelPrint.getBo());
                    labelPrintLog.setLabelPrintDetailBo(sn.getBo());
                    labelPrintLog.setPrintDate(date);
                    labelPrintLog.setPrintUser(userName);
                    labelPrintLog.setState(LabelPrintLogStateEnum.NEW.getCode());
                    labelPrintLog.setPrintCount(1);
                    labelPrintLogs.add(labelPrintLog);
                    map.put("fileDate", DateUtil.format(sn.getCreateDate(), "yyyy-MM-dd"));
                    if (labelPrint.getTemplateType().equals(LabelTemplateTypeEnum.JASPER.getValue())) {
                        map = JSONObject.parseObject(sn.getLabelParams());
                        // 传递条码SN供pdf命名使用
                        map.put("snForPdfName", sn.getSn());
                    }
                }

                if (labelPrint.getTemplateType().equals(LabelTemplateTypeEnum.JASPER.getValue())) {
                    mapList.add(map);
                } else if (labelPrint.getTemplateType().equals(LabelTemplateTypeEnum.LODOP.getValue())) {
                    respLists.add(CollectionUtil.isNotEmpty(map) ? JSONObject.toJSONString(map) : sn.getLabelParams());
                }
            }
            SqlSession sqlSession = null;
            Connection conn = null;
            Throwable var5 = null;
            PreparedStatement ps = null;
            PreparedStatement psInsert = null;
            try {
                sqlSession = SqlHelper.sqlSessionBatch(this.currentModelClass());
                conn = sqlSession.getConfiguration().getEnvironment().getDataSource().getConnection();
                //关闭自动提交，即开启事务
                conn.setAutoCommit(false);
                String sql = "UPDATE z_sn SET SITE = ?, SN = ?, COMPLEMENT_CODE_STATE = ?, ITEM_BO = ?, STATE = ?, CREATE_DATE = ?, LABEL_PRINT_BO = ?, LABEL_PARAMS = ?, PRINT_COUNT = ?, LAST_PRINT_USER = ?, LAST_PRINT_DATE = ? WHERE BO = ?";
                ps = conn.prepareStatement(sql);
                if (snList.size() > 0) {
                    int i = 0;
                    for (Sn pay : snList) {
                        i++;
                        ps.setString(1, pay.getSite());
                        ps.setString(2, pay.getSn());
                        ps.setString(3, pay.getComplementCodeState());
                        ps.setString(4, pay.getItemBo());
                        ps.setString(5, pay.getState());
                        ps.setTimestamp(6, new Timestamp(pay.getCreateDate().getTime()));
                        ps.setString(7, pay.getLabelPrintBo());
                        ps.setString(8, pay.getLabelParams());
                        ps.setInt(9, pay.getPrintCount());
                        ps.setString(10, pay.getLastPrintUser());
                        ps.setTimestamp(11, new Timestamp(pay.getLastPrintDate().getTime()));
                        ps.setString(12, pay.getBo());
                        ps.addBatch();
                        if (i > 0 && i % 200 == 0) {
                            ps.executeBatch();
                            conn.commit();
                            ps.clearBatch();
                        }
                    }
                }
                ps.executeBatch();
                conn.commit();
                ps.clearBatch();

                String sqlInset = "INSERT INTO label_label_print_log (BO, LABEL_PRINT_BO, LABEL_PRINT_DETAIL_BO, PRINT_DATE, PRINT_USER, STATE, PRINT_COUNT) VALUES (?, ?, ?, ?, ?, ?, ?)";
                psInsert = conn.prepareStatement(sqlInset);
                int i = 0;
                for (LabelPrintLog pay : labelPrintLogs) {
                    i++;
                    psInsert.setString(1, UUID.randomUUID().toString());
                    psInsert.setString(2, pay.getLabelPrintBo());
                    psInsert.setString(3, pay.getLabelPrintDetailBo());
                    psInsert.setTimestamp(4, new Timestamp(pay.getPrintDate().getTime()));
                    psInsert.setString(5, pay.getPrintUser());
                    psInsert.setString(6, pay.getState());
                    psInsert.setInt(7, pay.getPrintCount());
                    psInsert.addBatch();
                    if (i > 0 && i % 200 == 0) {
                        psInsert.executeBatch();
                        conn.commit();
                        psInsert.clearBatch();
                    }
                }
                psInsert.executeBatch();
                conn.commit();
                psInsert.clearBatch();
                if (labelPrint.getTemplateType().equals(LabelTemplateTypeEnum.JASPER.getValue())) {
                    respLists = commonUtils.conversionUrl(labelService.batchCreatePdf(mapList, ruleLabel.getLabelBo()));
                }

            } catch (Exception e) {
                e.printStackTrace();
                conn.rollback();
                sqlSession.rollback();
                throw new CommonException("条码pdf生成失败", CommonExceptionDefinition.VERIFY_EXCEPTION);
            } finally {
                if (ps != null) {
                    ps.close();
                }
                if (psInsert != null) {
                    psInsert.close();
                }
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
                if (sqlSession != null) {
                    sqlSession.close();
                }
            }
        } else {
            throw new CommonException("没有找到条码明细", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        return respLists;

    }


    @Override
    public ResponseData<ScanReturnVo> scanReturn(String barCode, String elementType) {
        if (StrUtil.isBlank(barCode) || StrUtil.isBlank(elementType)) {
            return ResponseData.error("条码和条码类型都不能为空");
        }
        ElementEnum elementEnum = ElementEnum.valueOf(elementType);
        if (BeanUtil.isEmpty(elementEnum)) {
            return ResponseData.error("条码类型不存在");
        }
        QueryWrapper<Sn> snQueryWrapper = new QueryWrapper<>();
        snQueryWrapper.lambda().eq(Sn::getSn, barCode);

        Sn sn = snService.getOne(snQueryWrapper, false);

        if (BeanUtil.isEmpty(sn)) {
            return ResponseData.error("该条码不存在");
        }
        //判断类型是否一致
        LabelPrint labelPrint = labelPrintMapper.selectById(sn.getLabelPrintBo());
        if (!elementEnum.getType().equals(labelPrint.getElementType())) {
            return ResponseData.error("该条码属于不正确");
        }

        //获取需要标签打印信息
        ScanReturnVo scanReturnVo = new ScanReturnVo();
        if (ElementEnum.PACKING.getType().equals(elementEnum.getType())) {
            scanReturnVo.setBo(sn.getBo());
            LabelPrintSaveDto saveDto = new LabelPrintSaveDto();
            saveDto.setRuleLabelBo(labelPrint.getRuleLabelBo())
                    .setLabelParams(sn.getLabelParams())
                    .setIsComplement(labelPrint.getIsComplement())
                    .setBarCodeAmount(1)
                    .setRuleLabelParams(labelPrint.getRuleLabelParams())
                    .setTemplateType(labelPrint.getTemplateType())
                    .setLodopText(labelPrint.getLodopText())
                    .setElementType(labelPrint.getElementType())
                    .setElementBo(labelPrint.getElementBo());
            scanReturnVo.setLabelCode(sn.getSn());
            scanReturnVo.setSn(sn);
            scanReturnVo.setStatus(sn.getState());
            scanReturnVo.setAmount(sn.getPackingQuantity());
            String elementBo = labelPrint.getElementBo();
            Map<String, Object> set = labelPrintMapper.getItem(elementBo);
            if (set != null) {
                saveDto.setCreateSnElementBo(set.get("packingBo").toString());
                scanReturnVo.setLabelPrintSaveDto(saveDto);
                String itemBo = set.get("itemBo").toString();
                scanReturnVo.setItemBo(itemBo);
                if (itemBo != null) {
                    Map<String, Object> map = labelPrintMapper.getItemName(itemBo);
                    scanReturnVo.setItemName(map.get("itemName").toString());
                }
            }
        }
        return ResponseData.success(scanReturnVo);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveByImport(List<ShopOrderSnImportDto> list) throws CommonException{
        //校验元素类型是否合法(默认："SHOP_ORDER")
        Date date = new Date();
        String elementType = "SHOP_ORDER";
        String userName = UserUtils.getCurrentUser().getUserName();
        String site = UserUtils.getSite();

        LabelPrint labelPrint = new LabelPrint();
        labelPrint.setElementType(elementType); // 元素类型
        String labelPrintId = UUID.randomUUID().toString().replace("-","");
        labelPrint.setBo(labelPrintId);
        labelPrint.setPrintDate(DateUtil.parseDate(DateUtil.today()));
        labelPrint.setCreateUser(userName);
        labelPrint.setCreateDate(date);
        labelPrint.setSite(site);

        // 导入前校验全部工单，并返回拼接错误提示
        List<String> errorMsg = new ArrayList<>();
        List<String> collect1 = list.stream().map(ShopOrderSnImportDto::getShopOrder).distinct().collect(Collectors.toList());
        for(int i=0;i < collect1.size();i++){
            // 工单编号
            String shopOrder = collect1.get(i);
            if(StrUtil.isBlank(shopOrder)) {
                //throw new CommonException("工单编号不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
                errorMsg.add("工单编号不能为空");
            }else {
                // 获取工单导入数量
                long countNum = list.stream().filter(shopOrderSnImportDto -> shopOrder.equals(shopOrderSnImportDto.getShopOrder())).map(ShopOrderSnImportDto::getSn).distinct().count();
                ResponseData<ShopOrderFullVo> shopOrderData = shopOrderService.getShopOrder(shopOrder);
                if(!ResultResponseEnum.SUCCESS.getCode().equals(shopOrderData.getCode())){
                    //throw new CommonException(shopOrderData.getMsg(), CommonExceptionDefinition.VERIFY_EXCEPTION);
                    //errorMsg.add("工单："+shopOrder+"，"+shopOrderData.getMsg());
                    errorMsg.add("工单："+shopOrder+"工单不存在！");
                }else{
                    ShopOrderFullVo shopOrderFullVo = shopOrderData.getData();
                    if(shopOrderFullVo == null){
                        //throw new CommonException("工单不存在！", CommonExceptionDefinition.VERIFY_EXCEPTION);
                        errorMsg.add("工单："+shopOrder+"工单不存在！");
                    }else if(shopOrderFullVo.getOrderQty() == null){
                        //throw new CommonException("工单数量为空！", CommonExceptionDefinition.VERIFY_EXCEPTION);
                        errorMsg.add("工单："+shopOrder+"工单数量为空！");
                    }else{
                        // Integer barCodeAmount = shopOrderFullVo.getOrderQty().intValue() - shopOrderFullVo.getLabelQty().intValue();
                        if(shopOrderFullVo.getOrderQty().compareTo(NumberUtil.add(shopOrderFullVo.getLabelQty(),new BigDecimal(countNum)))<0){
                            //throw new CommonException("打印数量超出工单可打印数量！", CommonExceptionDefinition.VERIFY_EXCEPTION);
                            errorMsg.add("工单："+shopOrder+"打印数量超出工单可打印数量！");
                        }
                    }
                }
            }
        }
        //拼接错误提示
        if(CollUtil.isNotEmpty(errorMsg)) {
            throw new CommonException(CollUtil.join(errorMsg, ";"), CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        // labelPrintMapper.insert(labelPrint);

        // 标签打印详情实体
        //List<LabelPrintDetail> labelPrintDetails = new ArrayList<>();
        Set<String> stringSet = new HashSet<>();
        int barCodeAmountInt = 0; // 打印数量
        //List<Sn> sns = new ArrayList<>();
        for(ShopOrderSnImportDto dto:list){
            String shopOrderStr = dto.getShopOrder(); // 工单编码
            String snStr = dto.getSn(); // sn条码
            String ruleLabelStr = dto.getRuleLabel(); // 规则模板
            Integer isComplementInt = dto.getIsComplement(); // 是否补打

            // 1、工单校验
//            BigDecimal labelQty = new BigDecimal(0);
//            if(StrUtil.isBlank(shopOrderStr)) {
//                throw new CommonException("工单编号不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
//            }else {
//                //String shopOrderNum = dto.getShopOrder();
//                ResponseData<ShopOrderFullVo> shopOrderData = shopOrderService.getShopOrder(shopOrderStr);
//                if(!ResultResponseEnum.SUCCESS.getCode().equals(shopOrderData.getCode())){
//                    throw new CommonException(shopOrderData.getMsg(), CommonExceptionDefinition.VERIFY_EXCEPTION);
//                }
//                ShopOrderFullVo shopOrderFullVo = shopOrderData.getData();
//                if(shopOrderFullVo == null){
//                    throw new CommonException("工单不存在！", CommonExceptionDefinition.VERIFY_EXCEPTION);
//                }else if(shopOrderFullVo.getOrderQty() == null){
//                    throw new CommonException("工单数量为空！", CommonExceptionDefinition.VERIFY_EXCEPTION);
//                }else{
//                    //Integer barCodeAmount = shopOrderFullVo.getOrderQty().intValue() - shopOrderFullVo.getLabelQty().intValue();
//                    Integer barCodeAmount = 1;
//                    if(shopOrderFullVo.getOrderQty().compareTo(NumberUtil.add(shopOrderFullVo.getLabelQty(),new BigDecimal(barCodeAmount)))<0){
//                        throw new CommonException("打印数量超出工单可打印数量！", CommonExceptionDefinition.VERIFY_EXCEPTION);
//                    }
//                }
//                labelQty = shopOrderFullVo.getLabelQty();
//            }

            // 2、校验sn条码是否存在
            if(StrUtil.isBlank(snStr)) {
                throw new CommonException("条码不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }else{
                List<Sn> ret = snService.lambdaQuery().eq(Sn::getSn, snStr).list();
                if (ret != null && ret.size() > 1) {
                    throw new CommonException("条码("+snStr+")已存在多条，无法确定SN的信息!", CommonExceptionDefinition.BASIC_EXCEPTION);
                }else if(ret != null && ret.size() == 1) {
                    throw new CommonException("条码("+snStr+")已存在！", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }
            }

            // 3、规则模板编号校验
            RuleLabelShowDto ruleLabelShowDto = ruleLabelService.getByCode(ruleLabelStr,"SO");
            if(ObjectUtil.isEmpty(ruleLabelShowDto)){
                throw new CommonException("未找到对应的物料标签模板，请先维护物料标签模板", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }

            //处理工单标签数量变更
            ShopOrderHandleBO shopOrderHandleBO = new ShopOrderHandleBO(site,shopOrderStr);
            String shopOrderBoStr = shopOrderHandleBO.getBo();
            stringSet.add(shopOrderBoStr);

            Sn sn = new Sn();
            String bo = new SnHandleBO(site, snStr).getBo();
            sn.setBo(bo);
            if (IsComplementEnum.Y.getCode().equals(isComplementInt)) {
                sn.setComplementCodeState("Y");
            } else {
                sn.setComplementCodeState("N");
            }
            sn.setState(SnTypeEnum.NEW.getCode());
            sn.setSite(site);
            sn.setSn(snStr);
            sn.setShopOrder(shopOrderStr);
            sn.setShapOrderBo(shopOrderBoStr);

            //定义标签参数Map，用于后面取值去生成标签
            // Map<String,Object> labelParams = JSONObject.parseObject(labelPrintSaveDto.getLabelParams());
            Map<String,Object> labelParams = new JSONObject();
            List<RuleLabelDetail> ruleLabelDetailList = ruleLabelShowDto.getDetails();
            for(RuleLabelDetail rld : ruleLabelDetailList){
                if(rld.getRuleVal() == null || "".equals(rld.getRuleVal())){
                    labelParams.put(rld.getTemplateArg(),"");
                }
            }
            //遍历标签参数，当有数据未赋值时，将编码规则生成的code赋值上
            Map<String,Object> paramsMap = new HashMap<>();
            for(String key:labelParams.keySet()){
                if(ObjectUtil.isEmpty(labelParams.get(key))){
                    paramsMap.put(key,dto.getSn());
                }else {
                    paramsMap.put(key,labelParams.get(key));
                }
            }

            //将标签数据转成json字符串存到数据库，以方便后面生成pdf文件
            sn.setLabelParams(JSON.toJSONString(paramsMap));
            sn.setCreateDate(date);
            //sn.setPackingMaxQuantity(labelPrintSaveDto.getMaxQty());
            //sn.setPackingQuantity(labelPrintSaveDto.getPackingQuantity());
            //sns.add(sn);

            //将标签数据转成json字符串存到数据库，以方便后面生成pdf文件
            //LabelPrintDetail labelPrintDetail = new LabelPrintDetail();
            //labelPrintDetail.setLabelPrintBo(labelPrintId);
            //labelPrintDetail.setDetailCode(snStr);
            //labelPrintDetail.setLabelParams(JSON.toJSONString(paramsMap));
            //labelPrintDetail.setPackingMaxQuantity(labelPrintSaveDto.getMaxQty()); // 包装最大数
            //labelPrintDetails.add(labelPrintDetail);

            // excel有多个工单时使用的保存（标签打印实体）
            labelPrint.setElementBo(shopOrderBoStr); // 工单编号
//            barCodeAmountInt = barCodeAmountInt+1;
//            labelPrint.setBarCodeAmount(barCodeAmountInt); // 条码数量
            labelPrint.setIsComplement(isComplementInt); // 是否补打
            String ruleLabelBo = ruleLabelShowDto.getBo(); // 规则BO
            String templateType = ruleLabelShowDto.getTemplateType(); // 规则类型
            String lodopText = ruleLabelShowDto.getLodopText(); // lodop生成的代码段
            labelPrint.setRuleLabelBo(ruleLabelBo); // 规则模板BO
            labelPrint.setTemplateType(templateType); // 规则模板类型
            labelPrint.setLodopText(lodopText);
            // 校验组合唯一：router + site
            String finalLabelPrintId = labelPrintId;
            LambdaQueryWrapper<LabelPrint> query = new QueryWrapper<LabelPrint>().lambda()
                    .and(i -> i.eq(LabelPrint::getBo, finalLabelPrintId)
                            .eq(LabelPrint::getElementBo, shopOrderBoStr));
            List<LabelPrint> queryLabelPrint = labelPrintMapper.selectList(query);
            int fl = 0;
            if (queryLabelPrint != null && queryLabelPrint.size() > 0) {
                barCodeAmountInt = barCodeAmountInt+1;
                labelPrint.setBarCodeAmount(barCodeAmountInt); // 条码数量
                fl = labelPrintMapper.updateById(labelPrint);
            }else{
                labelPrintId = UUID.randomUUID().toString().replace("-","");
                labelPrint.setBo(labelPrintId);
                labelPrint.setPrintDate(DateUtil.parseDate(DateUtil.today()));
                barCodeAmountInt = 1;
                labelPrint.setBarCodeAmount(barCodeAmountInt); // 条码数量
                fl = labelPrintMapper.insert(labelPrint);
            }
            if(fl < 1){
                throw new CommonException("标签打印实体保存失败！", CommonExceptionDefinition.BASIC_EXCEPTION);
            }

            sn.setLabelPrintBo(labelPrintId);
            Boolean snBl = snService.save(sn);
            if(!snBl){
                throw new CommonException("条码("+snStr+")保存失败！", CommonExceptionDefinition.BASIC_EXCEPTION);
            }

            if(ElementEnum.SHOP_ORDER.getType().equals(elementType)){
                // 每个sn保存加1
                BigDecimal newLabelQty = new BigDecimal(1);
                shopOrderService.updateShopOrderLabelQtyByBO(shopOrderBoStr,newLabelQty);
            }

            //保存条码工艺路线
            ResponseData<OrderRouter> orderRouterResult = orderRouterService.getOrderRouter(shopOrderBoStr);
            String processInfo = null;
            if(orderRouterResult.isSuccess()) {
                OrderRouter orderRouter = orderRouterResult.getData();
                if(orderRouter != null) {
                    OrderRouterProcess orderRouterProcess = orderRouter.getOrderRouterProcess();
                    if(orderRouterProcess != null) {
                        processInfo = orderRouterProcess.getProcessInfo();
                    }
                }
            }
            if(StrUtil.isNotBlank(processInfo)) {
                UpdateSnRouteDto updateSnRouteDto = new UpdateSnRouteDto();
                updateSnRouteDto.setSite(site);
                updateSnRouteDto.setProcessInfo(processInfo);
                List<String> codeList = new ArrayList<>();
                codeList.add(snStr);
                updateSnRouteDto.setSnList(codeList);
                meSnRouterService.addSnRoute(updateSnRouteDto);
            }

        }

        // excel有多个工单时使用的保存（标签打印实体）
        /*for(String orderStr : stringSet){
            //ShopOrderHandleBO shopOrderHandleBO = new ShopOrderHandleBO(UserUtils.getSite(),orderStr);
            labelPrint.setElementBo(orderStr);
            labelPrintMapper.insert(labelPrint);
        }*/

    }

    /*@Override
    @Transactional(rollbackFor = Exception.class)
    public void saveByImport(List<ShopOrderSnImportDto> list) throws CommonException{
        //工单可打印数量判断
//        if(shopOrderFullVo.getOrderQty().compareTo(NumberUtil.add(shopOrderFullVo.getLabelQty(),new BigDecimal(labelPrintSaveDto.getBarCodeAmount())))<0){
//            throw new RuntimeException("打印数量超出工单可打印数量");
//        }

        Date date = new Date();
        String userName = UserUtils.getCurrentUser().getUserName();
        String elementType = "SHOP_ORDER";
        String site = UserUtils.getSite();

        // 标签打印实体
        LabelPrint labelPrint = new LabelPrint();
        labelPrint.setElementType(elementType); // 元素类型
        String labelPrintId = UUID.randomUUID().toString().replace("-","");
        labelPrint.setBo(labelPrintId);
        labelPrint.setPrintDate(DateUtil.parseDate(DateUtil.today()));
        labelPrint.setCreateUser(userName);
        labelPrint.setCreateDate(date);
        labelPrint.setSite(UserUtils.getSite());
        // labelPrintMapper.insert(labelPrint);

        // 标签打印详情实体
        List<LabelPrintDetail> labelPrintDetails = new ArrayList<>();
        Set<String> stringSet = new HashSet<>();
        for(ShopOrderSnImportDto dto:list){
//            if(code.equals(list.get(0))){
//                labelPrint.setStartCode(code);
//            }else if(code.equals(list.get(list.size()-1))){
//                labelPrint.setEndCode(code);
//            }
            String snStr = dto.getSn();
            String shopOrderStr = dto.getShopOrder();

            // 校验sn条码是否存在
            if(StrUtil.isBlank(snStr)) {
                throw new CommonException("条码不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }else{
//                Sn snObj = snService.getSnInfo(snStr);
//                if(snObj != null) {
//                    throw new CommonException("条码("+snStr+")已存在！", CommonExceptionDefinition.VERIFY_EXCEPTION);
//                }
            }

            // 工单判断
            if(StrUtil.isBlank(shopOrderStr)) {
                throw new CommonException("工单编号不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }else {
                //String shopOrderNum = dto.getShopOrder();
                ResponseData<ShopOrderFullVo> shopOrderData = shopOrderService.getShopOrder(shopOrderStr);
                if(!ResultResponseEnum.SUCCESS.getCode().equals(shopOrderData.getCode())){
                    throw new CommonException(shopOrderData.getMsg(), CommonExceptionDefinition.VERIFY_EXCEPTION);
                }
                ShopOrderFullVo shopOrderFullVo = shopOrderData.getData();
                if(shopOrderFullVo == null){
                    throw new CommonException("工单不存在！", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }else if(shopOrderFullVo.getOrderQty() == null){
                    throw new CommonException("工单数量为空！", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }else{
                    Integer barCodeAmount = shopOrderFullVo.getOrderQty().intValue() - shopOrderFullVo.getLabelQty().intValue();
                    if(shopOrderFullVo.getOrderQty().compareTo(NumberUtil.add(shopOrderFullVo.getLabelQty(),new BigDecimal(barCodeAmount)))<0){
                        throw new CommonException("打印数量超出工单可打印数量！", CommonExceptionDefinition.VERIFY_EXCEPTION);
                    }
                }
            }

            LabelPrintDetail labelPrintDetail = new LabelPrintDetail();
            labelPrintDetail.setLabelPrintBo(labelPrintId);
            labelPrintDetail.setDetailCode(snStr);

            String ruleLabelCode = dto.getRuleLabel(); // 规则模板编号
            RuleLabelShowDto ruleLabelShowDto = ruleLabelService.getByCode(ruleLabelCode,elementType);
            if(ObjectUtil.isEmpty(ruleLabelShowDto)){
                throw new CommonException("未找到对应的物料标签模板，请先维护物料标签模板", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }

            //定义标签参数Map，用于后面取值去生成标签
            // Map<String,Object> labelParams = JSONObject.parseObject(labelPrintSaveDto.getLabelParams());
            Map<String,Object> labelParams = new JSONObject();
            List<RuleLabelDetail> ruleLabelDetailList = ruleLabelShowDto.getDetails();
            for(RuleLabelDetail rld : ruleLabelDetailList){
                if(rld.getRuleVal() == null || "".equals(rld.getRuleVal())){
                    labelParams.put(rld.getTemplateArg(),"");
                }
            }
            //遍历标签参数，当有数据未赋值时，将编码规则生成的code赋值上
            Map<String,Object> paramsMap = new HashMap<>();
            for(String key:labelParams.keySet()){
                if(ObjectUtil.isEmpty(labelParams.get(key))){
                    paramsMap.put(key,dto.getSn());
                }else {
                    paramsMap.put(key,labelParams.get(key));
                }
            }

            //处理工单标签数量变更
            ShopOrderHandleBO shopOrderHandleBO = new ShopOrderHandleBO(site,dto.getShopOrder());
            stringSet.add(shopOrderHandleBO.getBo());

            //将标签数据转成json字符串存到数据库，以方便后面生成pdf文件
            labelPrintDetail.setLabelParams(JSON.toJSONString(paramsMap));
            //labelPrintDetail.setPackingMaxQuantity(labelPrintSaveDto.getMaxQty()); // 包装最大数
            labelPrintDetails.add(labelPrintDetail);

            if(ElementEnum.SHOP_ORDER.getType().equals(elementType)){
                shopOrderService.updateShopOrderLabelQtyByBO(shopOrderHandleBO.getBo(),new BigDecimal(list.size()));
            }
        }

        // excel有多个工单时使用的保存（标签打印实体）
        if(stringSet == null){
            throw new CommonException("工单编号不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }else{
            for(String orderStr : stringSet){
                //ShopOrderHandleBO shopOrderHandleBO = new ShopOrderHandleBO(UserUtils.getSite(),orderStr);
                labelPrint.setElementBo(orderStr);
                labelPrintMapper.insert(labelPrint);
            }
        }


        //labelPrintDetailService.saveBatch(labelPrintDetails);

        //执行导入
        //todo 条码打印任务插入， 因为工单关联sn是 取自打印任务表

        //条码表 插入
        //Sn sn = new Sn();
        //todo 设置sn字段值
        //snService.save(sn);

        // 产品状态表 插入
        //MeProductStatus meProductStatus = new MeProductStatus();
        //todo 设置meproductStatus的字段值
        //meProductStatusService.insert(meProductStatus);

    }*/

    @Override
    public LabelPrintResponseDTO inProductionLinePrint(LabelInProductLinePrintDTO labelInProductLinePrintDTO) {
        Assert.valid(StrUtil.isBlank(labelInProductLinePrintDTO.getSn()), "条码不能为空");

        String site = UserUtils.getSite();
        //条码信息
        Sn sn = snService.getById(new SnHandleBO(site, labelInProductLinePrintDTO.getSn()));
        //判断是否报废
        Assert.valid(StrUtil.equals(SnTypeEnum.SCRAPPED.getCode(), sn.getState()), "条码已报废 无法打印!");

        //获取标签打印任务信息
        LabelPrint labelPrint = labelPrintMapper.selectById(sn.getLabelPrintBo());

//        Assert.valid(IsComplementEnum.N.getValue().equals(labelPrint.getIsComplement())
//                && ObjectUtil.isNotEmpty(labelPrint.getPrintDate()), "该标签不可补码");

        RuleLabel ruleLabel = ruleLabelMapper.selectById(labelPrint.getRuleLabelBo());
        Assert.valid(ruleLabel == null, "查询不到规则模板");

        //获取打印参数返回
        List<String> respLists = getPrintInfo(labelPrint, ruleLabel, sn);

        //保存日志和暂存数据
        saveLogAndTempData(sn, labelPrint, labelInProductLinePrintDTO.getPrintCount(), LabelPrintLogStateEnum.ON_PRODUCT_LINE.getCode(), labelInProductLinePrintDTO.getPrinter());

        LabelPrintResponseDTO labelPrintResponseDTO = new LabelPrintResponseDTO();
        labelPrintResponseDTO.setPrintParam(respLists);
        labelPrintResponseDTO.setLodopText(labelPrint.getLodopText());
        labelPrintResponseDTO.setTemplateType(labelPrint.getTemplateType());
        return labelPrintResponseDTO;
    }

    @Override
    public Map<String, Object> additionalPrint(LabelInProductLinePrintDTO labelInProductLinePrintDTO) {
        List<String> snList = labelInProductLinePrintDTO.getSnList();
        Assert.valid(CollUtil.isEmpty(snList), "条码不能为空");

        String site = UserUtils.getSite();
        //条码信息
        List<String> snBoList = new ArrayList<>();
        for (String sn : snList) {
            snBoList.add(new SnHandleBO(site, sn).getBo());
        }
        Collection<Sn> sns = snService.listByIds(snBoList);
        Assert.valid(CollUtil.isEmpty(sns), "未找到条码信息");

        StringBuilder msg = new StringBuilder();
        Map<String, List<Sn>> snGroupByState = sns.stream().collect(Collectors.groupingBy(e -> (StrUtil.equals(SnTypeEnum.SCRAPPED.getCode(), e.getState()) ? "scrapped" : "notScrapped")));
        //未报废条码列表
        List<Sn> notScrappedSnList = snGroupByState.get("notScrapped");
        Assert.valid(CollUtil.isEmpty(notScrappedSnList), "条码都已报废");
        //获取标签打印任务信息
        List<String> notScrappedSnLabelPrintBoList = notScrappedSnList.stream().map(Sn::getLabelPrintBo).collect(Collectors.toList());
        List<LabelPrint> labelPrints = labelPrintMapper.selectBatchIds(notScrappedSnLabelPrintBoList);
        Assert.valid(CollUtil.isEmpty(labelPrints), "未找到标签打印任务信息");
        Map<String, List<LabelPrint>> labelPrintsGroupByBo = labelPrints.stream().collect(Collectors.groupingBy(LabelPrint::getBo));

        //获取规则模板信息
        List<String> ruleLabelBoList = labelPrints.stream().map(LabelPrint::getRuleLabelBo).collect(Collectors.toList());
        List<RuleLabel> ruleLabels = ruleLabelMapper.selectBatchIds(ruleLabelBoList);
        Assert.valid(CollUtil.isEmpty(ruleLabels), "查询不到规则模板");
        Map<String, List<RuleLabel>> ruleLabelsGroupByBo = ruleLabels.stream().collect(Collectors.groupingBy(RuleLabel::getBo));

        //结果
        List<LabelPrintResponseDTO> resultList = new ArrayList<>();

        //未找到标签打印任务条码
        List<String> notLabelPrintSnList = new ArrayList<>();
        //未找到规则模板信息条码
        List<String> notruleLabelSnList = new ArrayList<>();

        for (Sn sn : notScrappedSnList) {
            List<LabelPrint> labelPrintListBySn = labelPrintsGroupByBo.get(sn.getLabelPrintBo());
            if(CollUtil.isEmpty(labelPrintListBySn)) {
                notLabelPrintSnList.add(sn.getSn());
                continue;
            }
            LabelPrint labelPrint = labelPrintListBySn.get(0);
            List<RuleLabel> ruleLabelListByLabelPrint = ruleLabelsGroupByBo.get(labelPrint.getRuleLabelBo());
            if(CollUtil.isEmpty(ruleLabelListByLabelPrint)) {
                notruleLabelSnList.add(sn.getSn());
                continue;
            }
            List<String> printInfo = getPrintInfo(labelPrint, ruleLabelListByLabelPrint.get(0), sn);

            LabelPrintResponseDTO labelPrintResponseDTO = new LabelPrintResponseDTO();
            labelPrintResponseDTO.setPrintParam(printInfo);
            labelPrintResponseDTO.setLodopText(labelPrint.getLodopText());
            labelPrintResponseDTO.setTemplateType(labelPrint.getTemplateType());
            resultList.add(labelPrintResponseDTO);

            //保存日志和暂存数据
            saveLogAndTempData(sn, labelPrint, labelInProductLinePrintDTO.getPrintCount(), LabelPrintLogStateEnum.ADDITIONAL.getCode(), labelInProductLinePrintDTO.getPrinter());
        }
        //已报废条码
        List<Sn> scrappedSnList = snGroupByState.get("scrapped");
        if(CollUtil.isNotEmpty(scrappedSnList)) {
            String snStr = scrappedSnList.stream().map(Sn::getSn).collect(Collectors.joining(","));
            msg.append("条码: ");
            msg.append(snStr);
            msg.append("已报废\n");
        }
        if(CollUtil.isNotEmpty(notLabelPrintSnList)) {
            msg.append("条码: ");
            msg.append(CollUtil.join(notLabelPrintSnList, ","));
            msg.append("未找到标签打印任务信息\n");
        }
        if(CollUtil.isNotEmpty(notruleLabelSnList)) {
            msg.append("条码: ");
            msg.append(CollUtil.join(notruleLabelSnList, ","));
            msg.append("未找到规则模板信息\n");
        }
        Map<String, Object> result = new HashMap<>();
        result.put("msg", msg.toString());
        result.put("result", resultList);
        return result;
    }

    /**
     * 获取打印参数
     * */
    private List<String> getPrintInfo(LabelPrint labelPrint, RuleLabel ruleLabel, Sn sn) {
        List<String> respLists = new ArrayList<>();
        if (labelPrint.getTemplateType().equals(LabelTemplateTypeEnum.JASPER.getValue())) {
            List<Map<String, Object>> mapList = new ArrayList<>();
            Map<String, Object> map = JSONObject.parseObject(sn.getLabelParams());
            // 传递条码SN供pdf命名使用
            map.put("snForPdfName", sn.getSn());
            map.put("fileDate", DateUtil.format(sn.getCreateDate(),"yyyy-MM-dd"));
            mapList.add(map);
            respLists = commonUtils.conversionUrl(labelService.batchCreatePdf(mapList, ruleLabel.getLabelBo()));
        } else if (labelPrint.getTemplateType().equals(LabelTemplateTypeEnum.LODOP.getValue())) {
            respLists.add(sn.getLabelParams());
        }
        return respLists;
    }

    /**
     * 保存条码日志和过站暂存数据'
     * @param sn 条码
     * @param labelPrint 打印任务信息
     * @param printCount 打印数量
     * @param printState 打印类型{@link LabelPrintLogStateEnum#getCode()}
     * */
    private void saveLogAndTempData(Sn sn, LabelPrint labelPrint, Integer printCount, String printState, String printer) {
        String userName = UserUtils.getCurrentUser().getUserName();
        String site = UserUtils.getSite();
        String station = UserUtils.getStation();
        String workShop = UserUtils.getWorkShop();
        //保存日志， 暂存数据放到异步线程
        ThreadUtil.execAsync(()->{
            //更新条码最后打印时间
            Date date = new Date();
            sn.setLastPrintUser(userName);
            sn.setLastPrintDate(date);
            sn.setPrintCount(sn.getPrintCount() + 1);
            snService.updateById(sn);

            //保存条码日志
            LabelPrintLog labelPrintLog = new LabelPrintLog();
            labelPrintLog.setLabelPrintBo(labelPrint.getBo());
            labelPrintLog.setLabelPrintDetailBo(sn.getBo());
            labelPrintLog.setPrintDate(date);
            labelPrintLog.setPrintUser(userName);
            labelPrintLog.setState(printState);
            labelPrintLog.setStation(station);
            labelPrintLog.setPrintCount(printCount == null ? 0 : printCount);
            labelPrintLog.setPrinter(printer);
            labelPrintLogService.save(labelPrintLog);

            //保存采集记录暂存数据
            TemporaryData temporaryData = new TemporaryData();
            temporaryData.setSn(sn.getSn());
            temporaryData.setStation(station);
            temporaryData.setType(TemporaryDataTypeEnum.IN_PRODUCT_LINE_PRINT.getCode());

            CollectionRecordCommonTempDTO collectionRecordCommonTempDTO = new CollectionRecordCommonTempDTO();
            collectionRecordCommonTempDTO.setWorkShop(workShop);
            collectionRecordCommonTempDTO.setStation(station);
            collectionRecordCommonTempDTO.setSite(site);
            collectionRecordCommonTempDTO.setSn(sn.getSn());
            collectionRecordCommonTempDTO.setShopOrder(new ShopOrderHandleBO(labelPrint.getElementBo()).getShopOrder());
            collectionRecordCommonTempDTO.setUserName(userName);

            temporaryData.setContent(JSONUtil.toJsonStr(collectionRecordCommonTempDTO));
            temporaryDataService.addOrUpdate(temporaryData);
        });
    }
}

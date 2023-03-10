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
 * ?????????????????? ???????????????
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
        Assert.valid(!"N".equals(sfgq) && !"Y".equals(sfgq), "??????????????????");
        for (String id : itemLabelQueryUpDTO.getIdList()) {
            SnItem snItem = new SnItem();
            snItem.setId(id);
            snItem.setSfgq(sfgq);
            updateData.add(snItem);
        }
        snItemMapper.updateBatchById(updateData);
    }

    /**
     * ??????id????????????
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
        //??????????????????????????????
        Boolean elementTypeCheck = false;
        for (ElementEnum elementEnum : ElementEnum.values()) {
            if (elementEnum.getType().equals(labelPrintSaveDto.getElementType())) {
                elementTypeCheck = true;
            }
        }
        if (!elementTypeCheck) {
            throw new CommonException("????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        LabelPrint labelPrint = new LabelPrint();
        String site = UserUtils.getSite();
        BeanUtil.copyProperties(labelPrintSaveDto, labelPrint);
        String labelPrintId = UUID.randomUUID().toString().replace("-", "");
        labelPrint.setBo(labelPrintId);

        RuleLabel itemRuleLabel = ruleLabelMapper.selectById(labelPrintSaveDto.getRuleLabelBo());
        try {
            if (ObjectUtil.isEmpty(itemRuleLabel)) {
                throw new RuntimeException("?????????????????????????????????????????????????????????????????????");
            }
        } catch (Exception e) {
            throw new CommonException(e.getMessage(), CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        List<RuleLabelDetail> list = ruleLabelDetailService.list(
                new QueryWrapper<RuleLabelDetail>()
                        .lambda()
                        .eq(RuleLabelDetail::getIrlBo, itemRuleLabel.getBo()));

        //??????????????????????????????
        if (ElementEnum.SHOP_ORDER.getType().equals(labelPrintSaveDto.getElementType())) {
            String shopOrderNum = labelPrintSaveDto.getElementBo().split(",")[1];
            ResponseData<ShopOrderFullVo> shopOrderData = shopOrderService.getShopOrder(shopOrderNum);
            if (!ResultResponseEnum.SUCCESS.getCode().equals(shopOrderData.getCode())) {
                throw new RuntimeException(shopOrderData.getMsg());
            }
            ShopOrderFullVo shopOrderFullVo = shopOrderData.getData();
            BigDecimal orderSum = NumberUtil.add(shopOrderFullVo.getOrderQty(), shopOrderFullVo.getOverfulfillQty());
            if (orderSum.compareTo(NumberUtil.add(shopOrderFullVo.getLabelQty(), new BigDecimal(labelPrintSaveDto.getBarCodeAmount()))) < 0) {
                throw new RuntimeException("???????????????????????????????????????");
            }
        }

        //????????????????????????Map,????????????????????????????????????
        Map<String, Object> ruleLabelParams = JSONObject.parseObject(labelPrintSaveDto.getRuleLabelParams());
        List<String> codeList = ruleLabelService.generatorCode(itemRuleLabel, list, labelPrintSaveDto.getCreateSnElementBo(), labelPrintSaveDto.getBarCodeAmount(), ruleLabelParams);
        Date date = new Date();
//        String userName = UserUtils.getCurrentUser().getUserName();
        String userName = "admin";
        List<Sn> sns = new ArrayList<>();
        //??????????????????Map????????????????????????????????????
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
            //????????????????????????????????????????????????????????????????????????code?????????
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
            //?????????????????????json????????????????????????????????????????????????pdf??????
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
            //????????????????????????????????????
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
            //??????????????????????????????
            if (ElementEnum.SHOP_ORDER.getType().equals(labelPrintSaveDto.getElementType())) {
                shopOrderService.updateShopOrderLabelQtyByBO(labelPrintSaveDto.getElementBo(), new BigDecimal(labelPrintSaveDto.getBarCodeAmount()));
                //????????????????????????
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

            //??????????????????
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
            throw new RuntimeException("???????????????????????????????????????????????????????????????");
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
                throw new CommonException("????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        return labelPrintVoIPage;
    }

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> barCodePrint(LabelPrintBarCodeDto barCodeDto) throws CommonException, SQLException {

        barCodeDto.setBo(barCodeDto.getBo().replace("=", ""));
        LabelPrint labelPrint = labelPrintMapper.selectById(barCodeDto.getBo());

        //?????????????????????????????????????????????????????????????????????????????????
        if (labelPrint.getIsComplement().equals(IsComplementEnum.N.getValue()) && ObjectUtil.isNotEmpty(labelPrint.getPrintDate())) {

            throw new CommonException("?????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        RuleLabel ruleLabel = ruleLabelMapper.selectById(labelPrint.getRuleLabelBo());

        if (ObjectUtil.isEmpty(ruleLabel)) {

            throw new CommonException("???????????????????????????????????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
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
        //??????????????????????????????????????????????????????????????????
        List<String> respLists = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(sns)) {
            Map<String, Object> map = null;
            Sn sn = null;
            List<Sn> snList = new ArrayList<>();
            ListIterator<Sn> detailListIterator = sns.listIterator();
            while (detailListIterator.hasNext()) {
                //??????????????????
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
                            //???????????????
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
                    //???????????????
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
                        // ????????????SN???pdf????????????
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
                //????????????????????????????????????
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
                throw new CommonException("??????pdf????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
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
            throw new CommonException("????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        return respLists;

    }


    @Override
    public ResponseData<ScanReturnVo> scanReturn(String barCode, String elementType) {
        if (StrUtil.isBlank(barCode) || StrUtil.isBlank(elementType)) {
            return ResponseData.error("????????????????????????????????????");
        }
        ElementEnum elementEnum = ElementEnum.valueOf(elementType);
        if (BeanUtil.isEmpty(elementEnum)) {
            return ResponseData.error("?????????????????????");
        }
        QueryWrapper<Sn> snQueryWrapper = new QueryWrapper<>();
        snQueryWrapper.lambda().eq(Sn::getSn, barCode);

        Sn sn = snService.getOne(snQueryWrapper, false);

        if (BeanUtil.isEmpty(sn)) {
            return ResponseData.error("??????????????????");
        }
        //????????????????????????
        LabelPrint labelPrint = labelPrintMapper.selectById(sn.getLabelPrintBo());
        if (!elementEnum.getType().equals(labelPrint.getElementType())) {
            return ResponseData.error("????????????????????????");
        }

        //??????????????????????????????
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
        //??????????????????????????????(?????????"SHOP_ORDER")
        Date date = new Date();
        String elementType = "SHOP_ORDER";
        String userName = UserUtils.getCurrentUser().getUserName();
        String site = UserUtils.getSite();

        LabelPrint labelPrint = new LabelPrint();
        labelPrint.setElementType(elementType); // ????????????
        String labelPrintId = UUID.randomUUID().toString().replace("-","");
        labelPrint.setBo(labelPrintId);
        labelPrint.setPrintDate(DateUtil.parseDate(DateUtil.today()));
        labelPrint.setCreateUser(userName);
        labelPrint.setCreateDate(date);
        labelPrint.setSite(site);

        // ?????????????????????????????????????????????????????????
        List<String> errorMsg = new ArrayList<>();
        List<String> collect1 = list.stream().map(ShopOrderSnImportDto::getShopOrder).distinct().collect(Collectors.toList());
        for(int i=0;i < collect1.size();i++){
            // ????????????
            String shopOrder = collect1.get(i);
            if(StrUtil.isBlank(shopOrder)) {
                //throw new CommonException("????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
                errorMsg.add("????????????????????????");
            }else {
                // ????????????????????????
                long countNum = list.stream().filter(shopOrderSnImportDto -> shopOrder.equals(shopOrderSnImportDto.getShopOrder())).map(ShopOrderSnImportDto::getSn).distinct().count();
                ResponseData<ShopOrderFullVo> shopOrderData = shopOrderService.getShopOrder(shopOrder);
                if(!ResultResponseEnum.SUCCESS.getCode().equals(shopOrderData.getCode())){
                    //throw new CommonException(shopOrderData.getMsg(), CommonExceptionDefinition.VERIFY_EXCEPTION);
                    //errorMsg.add("?????????"+shopOrder+"???"+shopOrderData.getMsg());
                    errorMsg.add("?????????"+shopOrder+"??????????????????");
                }else{
                    ShopOrderFullVo shopOrderFullVo = shopOrderData.getData();
                    if(shopOrderFullVo == null){
                        //throw new CommonException("??????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
                        errorMsg.add("?????????"+shopOrder+"??????????????????");
                    }else if(shopOrderFullVo.getOrderQty() == null){
                        //throw new CommonException("?????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
                        errorMsg.add("?????????"+shopOrder+"?????????????????????");
                    }else{
                        // Integer barCodeAmount = shopOrderFullVo.getOrderQty().intValue() - shopOrderFullVo.getLabelQty().intValue();
                        if(shopOrderFullVo.getOrderQty().compareTo(NumberUtil.add(shopOrderFullVo.getLabelQty(),new BigDecimal(countNum)))<0){
                            //throw new CommonException("??????????????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
                            errorMsg.add("?????????"+shopOrder+"??????????????????????????????????????????");
                        }
                    }
                }
            }
        }
        //??????????????????
        if(CollUtil.isNotEmpty(errorMsg)) {
            throw new CommonException(CollUtil.join(errorMsg, ";"), CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        // labelPrintMapper.insert(labelPrint);

        // ????????????????????????
        //List<LabelPrintDetail> labelPrintDetails = new ArrayList<>();
        Set<String> stringSet = new HashSet<>();
        int barCodeAmountInt = 0; // ????????????
        //List<Sn> sns = new ArrayList<>();
        for(ShopOrderSnImportDto dto:list){
            String shopOrderStr = dto.getShopOrder(); // ????????????
            String snStr = dto.getSn(); // sn??????
            String ruleLabelStr = dto.getRuleLabel(); // ????????????
            Integer isComplementInt = dto.getIsComplement(); // ????????????

            // 1???????????????
//            BigDecimal labelQty = new BigDecimal(0);
//            if(StrUtil.isBlank(shopOrderStr)) {
//                throw new CommonException("????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
//            }else {
//                //String shopOrderNum = dto.getShopOrder();
//                ResponseData<ShopOrderFullVo> shopOrderData = shopOrderService.getShopOrder(shopOrderStr);
//                if(!ResultResponseEnum.SUCCESS.getCode().equals(shopOrderData.getCode())){
//                    throw new CommonException(shopOrderData.getMsg(), CommonExceptionDefinition.VERIFY_EXCEPTION);
//                }
//                ShopOrderFullVo shopOrderFullVo = shopOrderData.getData();
//                if(shopOrderFullVo == null){
//                    throw new CommonException("??????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
//                }else if(shopOrderFullVo.getOrderQty() == null){
//                    throw new CommonException("?????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
//                }else{
//                    //Integer barCodeAmount = shopOrderFullVo.getOrderQty().intValue() - shopOrderFullVo.getLabelQty().intValue();
//                    Integer barCodeAmount = 1;
//                    if(shopOrderFullVo.getOrderQty().compareTo(NumberUtil.add(shopOrderFullVo.getLabelQty(),new BigDecimal(barCodeAmount)))<0){
//                        throw new CommonException("??????????????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
//                    }
//                }
//                labelQty = shopOrderFullVo.getLabelQty();
//            }

            // 2?????????sn??????????????????
            if(StrUtil.isBlank(snStr)) {
                throw new CommonException("??????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }else{
                List<Sn> ret = snService.lambdaQuery().eq(Sn::getSn, snStr).list();
                if (ret != null && ret.size() > 1) {
                    throw new CommonException("??????("+snStr+")??????????????????????????????SN?????????!", CommonExceptionDefinition.BASIC_EXCEPTION);
                }else if(ret != null && ret.size() == 1) {
                    throw new CommonException("??????("+snStr+")????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }
            }

            // 3???????????????????????????
            RuleLabelShowDto ruleLabelShowDto = ruleLabelService.getByCode(ruleLabelStr,"SO");
            if(ObjectUtil.isEmpty(ruleLabelShowDto)){
                throw new CommonException("?????????????????????????????????????????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }

            //??????????????????????????????
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

            //??????????????????Map????????????????????????????????????
            // Map<String,Object> labelParams = JSONObject.parseObject(labelPrintSaveDto.getLabelParams());
            Map<String,Object> labelParams = new JSONObject();
            List<RuleLabelDetail> ruleLabelDetailList = ruleLabelShowDto.getDetails();
            for(RuleLabelDetail rld : ruleLabelDetailList){
                if(rld.getRuleVal() == null || "".equals(rld.getRuleVal())){
                    labelParams.put(rld.getTemplateArg(),"");
                }
            }
            //????????????????????????????????????????????????????????????????????????code?????????
            Map<String,Object> paramsMap = new HashMap<>();
            for(String key:labelParams.keySet()){
                if(ObjectUtil.isEmpty(labelParams.get(key))){
                    paramsMap.put(key,dto.getSn());
                }else {
                    paramsMap.put(key,labelParams.get(key));
                }
            }

            //?????????????????????json????????????????????????????????????????????????pdf??????
            sn.setLabelParams(JSON.toJSONString(paramsMap));
            sn.setCreateDate(date);
            //sn.setPackingMaxQuantity(labelPrintSaveDto.getMaxQty());
            //sn.setPackingQuantity(labelPrintSaveDto.getPackingQuantity());
            //sns.add(sn);

            //?????????????????????json????????????????????????????????????????????????pdf??????
            //LabelPrintDetail labelPrintDetail = new LabelPrintDetail();
            //labelPrintDetail.setLabelPrintBo(labelPrintId);
            //labelPrintDetail.setDetailCode(snStr);
            //labelPrintDetail.setLabelParams(JSON.toJSONString(paramsMap));
            //labelPrintDetail.setPackingMaxQuantity(labelPrintSaveDto.getMaxQty()); // ???????????????
            //labelPrintDetails.add(labelPrintDetail);

            // excel?????????????????????????????????????????????????????????
            labelPrint.setElementBo(shopOrderBoStr); // ????????????
//            barCodeAmountInt = barCodeAmountInt+1;
//            labelPrint.setBarCodeAmount(barCodeAmountInt); // ????????????
            labelPrint.setIsComplement(isComplementInt); // ????????????
            String ruleLabelBo = ruleLabelShowDto.getBo(); // ??????BO
            String templateType = ruleLabelShowDto.getTemplateType(); // ????????????
            String lodopText = ruleLabelShowDto.getLodopText(); // lodop??????????????????
            labelPrint.setRuleLabelBo(ruleLabelBo); // ????????????BO
            labelPrint.setTemplateType(templateType); // ??????????????????
            labelPrint.setLodopText(lodopText);
            // ?????????????????????router + site
            String finalLabelPrintId = labelPrintId;
            LambdaQueryWrapper<LabelPrint> query = new QueryWrapper<LabelPrint>().lambda()
                    .and(i -> i.eq(LabelPrint::getBo, finalLabelPrintId)
                            .eq(LabelPrint::getElementBo, shopOrderBoStr));
            List<LabelPrint> queryLabelPrint = labelPrintMapper.selectList(query);
            int fl = 0;
            if (queryLabelPrint != null && queryLabelPrint.size() > 0) {
                barCodeAmountInt = barCodeAmountInt+1;
                labelPrint.setBarCodeAmount(barCodeAmountInt); // ????????????
                fl = labelPrintMapper.updateById(labelPrint);
            }else{
                labelPrintId = UUID.randomUUID().toString().replace("-","");
                labelPrint.setBo(labelPrintId);
                labelPrint.setPrintDate(DateUtil.parseDate(DateUtil.today()));
                barCodeAmountInt = 1;
                labelPrint.setBarCodeAmount(barCodeAmountInt); // ????????????
                fl = labelPrintMapper.insert(labelPrint);
            }
            if(fl < 1){
                throw new CommonException("?????????????????????????????????", CommonExceptionDefinition.BASIC_EXCEPTION);
            }

            sn.setLabelPrintBo(labelPrintId);
            Boolean snBl = snService.save(sn);
            if(!snBl){
                throw new CommonException("??????("+snStr+")???????????????", CommonExceptionDefinition.BASIC_EXCEPTION);
            }

            if(ElementEnum.SHOP_ORDER.getType().equals(elementType)){
                // ??????sn?????????1
                BigDecimal newLabelQty = new BigDecimal(1);
                shopOrderService.updateShopOrderLabelQtyByBO(shopOrderBoStr,newLabelQty);
            }

            //????????????????????????
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

        // excel?????????????????????????????????????????????????????????
        /*for(String orderStr : stringSet){
            //ShopOrderHandleBO shopOrderHandleBO = new ShopOrderHandleBO(UserUtils.getSite(),orderStr);
            labelPrint.setElementBo(orderStr);
            labelPrintMapper.insert(labelPrint);
        }*/

    }

    /*@Override
    @Transactional(rollbackFor = Exception.class)
    public void saveByImport(List<ShopOrderSnImportDto> list) throws CommonException{
        //???????????????????????????
//        if(shopOrderFullVo.getOrderQty().compareTo(NumberUtil.add(shopOrderFullVo.getLabelQty(),new BigDecimal(labelPrintSaveDto.getBarCodeAmount())))<0){
//            throw new RuntimeException("???????????????????????????????????????");
//        }

        Date date = new Date();
        String userName = UserUtils.getCurrentUser().getUserName();
        String elementType = "SHOP_ORDER";
        String site = UserUtils.getSite();

        // ??????????????????
        LabelPrint labelPrint = new LabelPrint();
        labelPrint.setElementType(elementType); // ????????????
        String labelPrintId = UUID.randomUUID().toString().replace("-","");
        labelPrint.setBo(labelPrintId);
        labelPrint.setPrintDate(DateUtil.parseDate(DateUtil.today()));
        labelPrint.setCreateUser(userName);
        labelPrint.setCreateDate(date);
        labelPrint.setSite(UserUtils.getSite());
        // labelPrintMapper.insert(labelPrint);

        // ????????????????????????
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

            // ??????sn??????????????????
            if(StrUtil.isBlank(snStr)) {
                throw new CommonException("??????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }else{
//                Sn snObj = snService.getSnInfo(snStr);
//                if(snObj != null) {
//                    throw new CommonException("??????("+snStr+")????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
//                }
            }

            // ????????????
            if(StrUtil.isBlank(shopOrderStr)) {
                throw new CommonException("????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }else {
                //String shopOrderNum = dto.getShopOrder();
                ResponseData<ShopOrderFullVo> shopOrderData = shopOrderService.getShopOrder(shopOrderStr);
                if(!ResultResponseEnum.SUCCESS.getCode().equals(shopOrderData.getCode())){
                    throw new CommonException(shopOrderData.getMsg(), CommonExceptionDefinition.VERIFY_EXCEPTION);
                }
                ShopOrderFullVo shopOrderFullVo = shopOrderData.getData();
                if(shopOrderFullVo == null){
                    throw new CommonException("??????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }else if(shopOrderFullVo.getOrderQty() == null){
                    throw new CommonException("?????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }else{
                    Integer barCodeAmount = shopOrderFullVo.getOrderQty().intValue() - shopOrderFullVo.getLabelQty().intValue();
                    if(shopOrderFullVo.getOrderQty().compareTo(NumberUtil.add(shopOrderFullVo.getLabelQty(),new BigDecimal(barCodeAmount)))<0){
                        throw new CommonException("??????????????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
                    }
                }
            }

            LabelPrintDetail labelPrintDetail = new LabelPrintDetail();
            labelPrintDetail.setLabelPrintBo(labelPrintId);
            labelPrintDetail.setDetailCode(snStr);

            String ruleLabelCode = dto.getRuleLabel(); // ??????????????????
            RuleLabelShowDto ruleLabelShowDto = ruleLabelService.getByCode(ruleLabelCode,elementType);
            if(ObjectUtil.isEmpty(ruleLabelShowDto)){
                throw new CommonException("?????????????????????????????????????????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }

            //??????????????????Map????????????????????????????????????
            // Map<String,Object> labelParams = JSONObject.parseObject(labelPrintSaveDto.getLabelParams());
            Map<String,Object> labelParams = new JSONObject();
            List<RuleLabelDetail> ruleLabelDetailList = ruleLabelShowDto.getDetails();
            for(RuleLabelDetail rld : ruleLabelDetailList){
                if(rld.getRuleVal() == null || "".equals(rld.getRuleVal())){
                    labelParams.put(rld.getTemplateArg(),"");
                }
            }
            //????????????????????????????????????????????????????????????????????????code?????????
            Map<String,Object> paramsMap = new HashMap<>();
            for(String key:labelParams.keySet()){
                if(ObjectUtil.isEmpty(labelParams.get(key))){
                    paramsMap.put(key,dto.getSn());
                }else {
                    paramsMap.put(key,labelParams.get(key));
                }
            }

            //??????????????????????????????
            ShopOrderHandleBO shopOrderHandleBO = new ShopOrderHandleBO(site,dto.getShopOrder());
            stringSet.add(shopOrderHandleBO.getBo());

            //?????????????????????json????????????????????????????????????????????????pdf??????
            labelPrintDetail.setLabelParams(JSON.toJSONString(paramsMap));
            //labelPrintDetail.setPackingMaxQuantity(labelPrintSaveDto.getMaxQty()); // ???????????????
            labelPrintDetails.add(labelPrintDetail);

            if(ElementEnum.SHOP_ORDER.getType().equals(elementType)){
                shopOrderService.updateShopOrderLabelQtyByBO(shopOrderHandleBO.getBo(),new BigDecimal(list.size()));
            }
        }

        // excel?????????????????????????????????????????????????????????
        if(stringSet == null){
            throw new CommonException("????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }else{
            for(String orderStr : stringSet){
                //ShopOrderHandleBO shopOrderHandleBO = new ShopOrderHandleBO(UserUtils.getSite(),orderStr);
                labelPrint.setElementBo(orderStr);
                labelPrintMapper.insert(labelPrint);
            }
        }


        //labelPrintDetailService.saveBatch(labelPrintDetails);

        //????????????
        //todo ??????????????????????????? ??????????????????sn??? ?????????????????????

        //????????? ??????
        //Sn sn = new Sn();
        //todo ??????sn?????????
        //snService.save(sn);

        // ??????????????? ??????
        //MeProductStatus meProductStatus = new MeProductStatus();
        //todo ??????meproductStatus????????????
        //meProductStatusService.insert(meProductStatus);

    }*/

    @Override
    public LabelPrintResponseDTO inProductionLinePrint(LabelInProductLinePrintDTO labelInProductLinePrintDTO) {
        Assert.valid(StrUtil.isBlank(labelInProductLinePrintDTO.getSn()), "??????????????????");

        String site = UserUtils.getSite();
        //????????????
        Sn sn = snService.getById(new SnHandleBO(site, labelInProductLinePrintDTO.getSn()));
        //??????????????????
        Assert.valid(StrUtil.equals(SnTypeEnum.SCRAPPED.getCode(), sn.getState()), "??????????????? ????????????!");

        //??????????????????????????????
        LabelPrint labelPrint = labelPrintMapper.selectById(sn.getLabelPrintBo());

//        Assert.valid(IsComplementEnum.N.getValue().equals(labelPrint.getIsComplement())
//                && ObjectUtil.isNotEmpty(labelPrint.getPrintDate()), "?????????????????????");

        RuleLabel ruleLabel = ruleLabelMapper.selectById(labelPrint.getRuleLabelBo());
        Assert.valid(ruleLabel == null, "????????????????????????");

        //????????????????????????
        List<String> respLists = getPrintInfo(labelPrint, ruleLabel, sn);

        //???????????????????????????
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
        Assert.valid(CollUtil.isEmpty(snList), "??????????????????");

        String site = UserUtils.getSite();
        //????????????
        List<String> snBoList = new ArrayList<>();
        for (String sn : snList) {
            snBoList.add(new SnHandleBO(site, sn).getBo());
        }
        Collection<Sn> sns = snService.listByIds(snBoList);
        Assert.valid(CollUtil.isEmpty(sns), "?????????????????????");

        StringBuilder msg = new StringBuilder();
        Map<String, List<Sn>> snGroupByState = sns.stream().collect(Collectors.groupingBy(e -> (StrUtil.equals(SnTypeEnum.SCRAPPED.getCode(), e.getState()) ? "scrapped" : "notScrapped")));
        //?????????????????????
        List<Sn> notScrappedSnList = snGroupByState.get("notScrapped");
        Assert.valid(CollUtil.isEmpty(notScrappedSnList), "??????????????????");
        //??????????????????????????????
        List<String> notScrappedSnLabelPrintBoList = notScrappedSnList.stream().map(Sn::getLabelPrintBo).collect(Collectors.toList());
        List<LabelPrint> labelPrints = labelPrintMapper.selectBatchIds(notScrappedSnLabelPrintBoList);
        Assert.valid(CollUtil.isEmpty(labelPrints), "?????????????????????????????????");
        Map<String, List<LabelPrint>> labelPrintsGroupByBo = labelPrints.stream().collect(Collectors.groupingBy(LabelPrint::getBo));

        //????????????????????????
        List<String> ruleLabelBoList = labelPrints.stream().map(LabelPrint::getRuleLabelBo).collect(Collectors.toList());
        List<RuleLabel> ruleLabels = ruleLabelMapper.selectBatchIds(ruleLabelBoList);
        Assert.valid(CollUtil.isEmpty(ruleLabels), "????????????????????????");
        Map<String, List<RuleLabel>> ruleLabelsGroupByBo = ruleLabels.stream().collect(Collectors.groupingBy(RuleLabel::getBo));

        //??????
        List<LabelPrintResponseDTO> resultList = new ArrayList<>();

        //?????????????????????????????????
        List<String> notLabelPrintSnList = new ArrayList<>();
        //?????????????????????????????????
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

            //???????????????????????????
            saveLogAndTempData(sn, labelPrint, labelInProductLinePrintDTO.getPrintCount(), LabelPrintLogStateEnum.ADDITIONAL.getCode(), labelInProductLinePrintDTO.getPrinter());
        }
        //???????????????
        List<Sn> scrappedSnList = snGroupByState.get("scrapped");
        if(CollUtil.isNotEmpty(scrappedSnList)) {
            String snStr = scrappedSnList.stream().map(Sn::getSn).collect(Collectors.joining(","));
            msg.append("??????: ");
            msg.append(snStr);
            msg.append("?????????\n");
        }
        if(CollUtil.isNotEmpty(notLabelPrintSnList)) {
            msg.append("??????: ");
            msg.append(CollUtil.join(notLabelPrintSnList, ","));
            msg.append("?????????????????????????????????\n");
        }
        if(CollUtil.isNotEmpty(notruleLabelSnList)) {
            msg.append("??????: ");
            msg.append(CollUtil.join(notruleLabelSnList, ","));
            msg.append("???????????????????????????\n");
        }
        Map<String, Object> result = new HashMap<>();
        result.put("msg", msg.toString());
        result.put("result", resultList);
        return result;
    }

    /**
     * ??????????????????
     * */
    private List<String> getPrintInfo(LabelPrint labelPrint, RuleLabel ruleLabel, Sn sn) {
        List<String> respLists = new ArrayList<>();
        if (labelPrint.getTemplateType().equals(LabelTemplateTypeEnum.JASPER.getValue())) {
            List<Map<String, Object>> mapList = new ArrayList<>();
            Map<String, Object> map = JSONObject.parseObject(sn.getLabelParams());
            // ????????????SN???pdf????????????
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
     * ???????????????????????????????????????'
     * @param sn ??????
     * @param labelPrint ??????????????????
     * @param printCount ????????????
     * @param printState ????????????{@link LabelPrintLogStateEnum#getCode()}
     * */
    private void saveLogAndTempData(Sn sn, LabelPrint labelPrint, Integer printCount, String printState, String printer) {
        String userName = UserUtils.getCurrentUser().getUserName();
        String site = UserUtils.getSite();
        String station = UserUtils.getStation();
        String workShop = UserUtils.getWorkShop();
        //??????????????? ??????????????????????????????
        ThreadUtil.execAsync(()->{
            //??????????????????????????????
            Date date = new Date();
            sn.setLastPrintUser(userName);
            sn.setLastPrintDate(date);
            sn.setPrintCount(sn.getPrintCount() + 1);
            snService.updateById(sn);

            //??????????????????
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

            //??????????????????????????????
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

package com.itl.mom.label.provider.impl.label;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.itl.iap.common.base.dto.UserTDto;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.response.ResultResponseEnum;
import com.itl.iap.common.base.utils.Assert;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.ShopOrderHandleBO;
import com.itl.mes.core.api.constant.SnTypeEnum;
import com.itl.mes.core.api.entity.Item;
import com.itl.mes.core.api.entity.OrderRouter;
import com.itl.mes.core.api.entity.OrderRouterProcess;
import com.itl.mes.core.api.entity.PackLevel;
import com.itl.mes.core.api.vo.ShopOrderFullVo;
import com.itl.mes.core.api.vo.ShopOrderTwoSaveVo;
import com.itl.mes.core.client.service.ItemFeignService;
import com.itl.mes.core.client.service.OrderRouterService;
import com.itl.mes.core.client.service.PackingService;
import com.itl.mes.core.client.service.ShopOrderService;
import com.itl.mes.me.api.dto.UpdateSnRouteDto;
import com.itl.mes.me.client.service.MeSnRouterService;
import com.itl.mom.label.api.bo.SnHandleBO;
import com.itl.mom.label.api.dto.label.LabelTransferRequestDTO;
import com.itl.mom.label.api.dto.label.SnQueryDto;
import com.itl.mom.label.api.entity.MeProductStatus;
import com.itl.mom.label.api.entity.label.LabelPrint;
import com.itl.mom.label.api.entity.label.LabelPrintLog;
import com.itl.mom.label.api.entity.label.Sn;
import com.itl.mom.label.api.entity.label.SnTransRecord;
import com.itl.mom.label.api.entity.ruleLabel.RuleLabel;
import com.itl.mom.label.api.enums.LabelPrintLogStateEnum;
import com.itl.mom.label.api.service.MeProductStatusService;
import com.itl.mom.label.api.service.label.LabelService;
import com.itl.mom.label.api.service.label.SnService;
import com.itl.mom.label.api.service.label.SnTransRecordService;
import com.itl.mom.label.api.vo.*;
import com.itl.mom.label.provider.constant.ElementConstant;
import com.itl.mom.label.provider.enums.ElementEnum;
import com.itl.mom.label.provider.enums.IsComplementEnum;
import com.itl.mom.label.provider.enums.LabelTemplateTypeEnum;
import com.itl.mom.label.provider.mapper.label.LabelMapper;
import com.itl.mom.label.provider.mapper.label.LabelPrintLogMapper;
import com.itl.mom.label.provider.mapper.label.LabelPrintMapper;
import com.itl.mom.label.provider.mapper.label.SnMapper;
import com.itl.mom.label.provider.mapper.ruleLabel.RuleLabelMapper;
import com.itl.mom.label.provider.utils.CommonUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * <p>
 * 标签打印范围明细 服务实现类
 * </p>
 *
 * @author liuchenghao
 * @since 2021-01-20
 */
@Service
public class SnServiceImpl extends ServiceImpl<SnMapper, Sn> implements SnService {

    @Autowired
    SnMapper snMapper;
    @Autowired
    private SqlSession sqlSession;
    @Autowired
    LabelPrintMapper labelPrintMapper;

    @Autowired
    RuleLabelMapper ruleLabelMapper;

    @Autowired
    LabelService labelService;

    @Autowired
    LabelPrintLogMapper labelPrintLogMapper;

    @Autowired
    CommonUtils commonUtils;

    @Autowired
    private ItemFeignService itemFeignService;

    @Autowired
    private PackingService packingService;
    @Autowired
    private MeProductStatusService meProductStatusService;
    @Autowired
    private LabelMapper labelMapper;
    @Autowired
    private ShopOrderService shopOrderService;
    @Autowired
    private SnTransRecordService snTransRecordService;
    @Autowired
    private OrderRouterService orderRouterService;
    @Autowired
    private MeSnRouterService meSnRouterService;

    @Override
    public IPage<SnVo> findList(SnQueryDto snQueryDto) throws CommonException {

        extracted(snQueryDto);

        IPage<SnVo> snVoIPage;
        switch (snQueryDto.getElementType()) {
            case ElementConstant.ITEM:
                snQueryDto.setTableName(ElementEnum.ITEM.getTableName());
                snQueryDto.setQueryColumn(ElementEnum.ITEM.getQueryColumn());
                snVoIPage = snMapper.findList(snQueryDto.getPage(), snQueryDto);
                break;
            case ElementConstant.DEVICE:
                snQueryDto.setTableName(ElementEnum.DEVICE.getTableName());
                snQueryDto.setQueryColumn(ElementEnum.DEVICE.getQueryColumn());
                snVoIPage = snMapper.findList(snQueryDto.getPage(), snQueryDto);
                break;
            case ElementConstant.CARRIER:
                snQueryDto.setTableName(ElementEnum.CARRIER.getTableName());
                snQueryDto.setQueryColumn(ElementEnum.CARRIER.getQueryColumn());
                snVoIPage = snMapper.findList(snQueryDto.getPage(), snQueryDto);
                break;
            case ElementConstant.SHOP_ORDER:
                snQueryDto.setTableName(ElementEnum.SHOP_ORDER.getTableName());
                snQueryDto.setQueryColumn(ElementEnum.SHOP_ORDER.getQueryColumn());
                snVoIPage = snMapper.findList(snQueryDto.getPage(), snQueryDto);
                break;
            case ElementConstant.PACKING:
                snQueryDto.setTableName(ElementEnum.PACKING.getTableName());
                snQueryDto.setQueryColumn(ElementEnum.PACKING.getQueryColumn());
                snVoIPage = snMapper.findPackingLabelPrintDetail(snQueryDto.getPage(), snQueryDto);
                break;
            default:
                throw new CommonException("不支持的元素类型", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        return snVoIPage;
    }
    @Override
    public IPage<LabelPrintLogVo> findLog(SnQueryDto snQueryDto) throws CommonException {

        extracted(snQueryDto);

        IPage<LabelPrintLogVo> snVoIPage;
        switch (snQueryDto.getElementType()) {
            case ElementConstant.ITEM:
                snQueryDto.setTableName(ElementEnum.ITEM.getTableName());
                snQueryDto.setQueryColumn(ElementEnum.ITEM.getQueryColumn());
                snVoIPage = snMapper.findLog(snQueryDto.getPage(), snQueryDto);
                break;
            case ElementConstant.DEVICE:
                snQueryDto.setTableName(ElementEnum.DEVICE.getTableName());
                snQueryDto.setQueryColumn(ElementEnum.DEVICE.getQueryColumn());
                snVoIPage = snMapper.findLog(snQueryDto.getPage(), snQueryDto);
                break;
            case ElementConstant.CARRIER:
                snQueryDto.setTableName(ElementEnum.CARRIER.getTableName());
                snQueryDto.setQueryColumn(ElementEnum.CARRIER.getQueryColumn());
                snVoIPage = snMapper.findLog(snQueryDto.getPage(), snQueryDto);
                break;
            case ElementConstant.SHOP_ORDER:
                snQueryDto.setTableName(ElementEnum.SHOP_ORDER.getTableName());
                snQueryDto.setQueryColumn(ElementEnum.SHOP_ORDER.getQueryColumn());
                snVoIPage = snMapper.findLog(snQueryDto.getPage(), snQueryDto);
                break;
//            case ElementConstant.PACKING:
//                snQueryDto.setTableName(ElementEnum.PACKING.getTableName());
//                snQueryDto.setQueryColumn(ElementEnum.PACKING.getQueryColumn());
//                snVoIPage = snMapper.findPackingLabelPrintDetail(snQueryDto.getPage(), snQueryDto);
//                break;
            default:
                throw new CommonException("不支持的元素类型", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        return snVoIPage;
    }
    @Override
    public IPage<MeProductStatusVo> findProductStatus(SnQueryDto snQueryDto) throws CommonException {

        extracted(snQueryDto);

        IPage<MeProductStatusVo> snVoIPage;
        snQueryDto.setTableName(ElementEnum.SHOP_ORDER.getTableName());
        snQueryDto.setQueryColumn(ElementEnum.SHOP_ORDER.getQueryColumn());
        snVoIPage = snMapper.findProductStatus(snQueryDto.getPage(), snQueryDto);
        return snVoIPage;
    }

    private void extracted(SnQueryDto snQueryDto) {
        if (ObjectUtil.isEmpty(snQueryDto.getPage())) {
            snQueryDto.setPage(new Page(0, 10));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> barCodeDetailPrint(String detailBo, Integer printCount, String printer) throws CommonException {

        detailBo = detailBo.replace("=", "");
        Sn sn = snMapper.selectById(detailBo);
        if (SnTypeEnum.SCRAPPED.getCode().equals(sn.getState())) {
            throw new CommonException("条码已报废 无法打印!", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        LabelPrint labelPrint = labelPrintMapper.selectById(sn.getLabelPrintBo());
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

        meProductStatusService.save(new MeProductStatus().setShopOrder(shopOrder).setSnBo(sn.getBo()).setProductCode(productCode).setProductName(productName));

        if (labelPrint.getIsComplement().equals(IsComplementEnum.N.getValue()) && ObjectUtil.isNotEmpty(labelPrint.getPrintDate())) {
            throw new CommonException("该标签不可补码", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        RuleLabel ruleLabel = ruleLabelMapper.selectById(labelPrint.getRuleLabelBo());

        if (ObjectUtil.isEmpty(ruleLabel)) {
            throw new CommonException("查询不到规则模板", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        Date date = new Date();
        String userName = UserUtils.getCurrentUser().getUserName();
        sn.setLastPrintUser(userName);
        sn.setLastPrintDate(date);
        sn.setPrintCount(sn.getPrintCount() + 1);
        snMapper.updateById(sn);


        LabelPrintLog labelPrintLog = new LabelPrintLog();
        labelPrintLog.setLabelPrintBo(labelPrint.getBo());
        labelPrintLog.setLabelPrintDetailBo(sn.getBo());
        labelPrintLog.setPrintDate(date);
        labelPrintLog.setPrintUser(UserUtils.getCurrentUser().getUserName());
        labelPrintLog.setState(LabelPrintLogStateEnum.NEW.getCode());
        labelPrintLog.setPrintCount((printCount == null || printCount == 0) ? 1 : printCount);
        labelPrintLog.setPrinter(printer);
        labelPrintLogMapper.insert(labelPrintLog);

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
     * 根据类型检查条码
     *
     * @param barCode     条码
     * @param elementType 条码类型
     * @return
     */
    @Override
    public ResponseData<CheckBarCodeVo> checkBarCode(String barCode, String elementType) {
        if (StrUtil.isBlank(barCode) || StrUtil.isBlank(elementType)) {
            return ResponseData.error("条码和条码类型都不能为空");
        }
        ElementEnum elementEnum = ElementEnum.valueOf(elementType);
        if (BeanUtil.isEmpty(elementEnum)) {
            return ResponseData.error("条码类型不存在");
        }
        QueryWrapper<Sn> detailQueryWrapper = new QueryWrapper<>();
        detailQueryWrapper.lambda().eq(Sn::getSn, barCode);
        Sn printDetail = this.getOne(detailQueryWrapper, false);
        if (BeanUtil.isEmpty(printDetail)) {
            return ResponseData.error("该条码不存在");
        }
        //判断类型是否一致
        LabelPrint labelPrint = labelPrintMapper.selectById(printDetail.getLabelPrintBo());
        if (!elementEnum.getType().equals(labelPrint.getElementType())) {
            return ResponseData.error("该条码属于不正确");
        }
        CheckBarCodeVo checkBarCodeVo = new CheckBarCodeVo();
        checkBarCodeVo.setBo(printDetail.getBo());
        checkBarCodeVo.setState(printDetail.getState());
        checkBarCodeVo.setQualityStatus(printDetail.getQualityStatus());
        Set<String> ids = new HashSet<>();
        if (ElementConstant.ITEM.equals(elementEnum.getType())) {
            checkBarCodeVo.setAmount(1);
            checkBarCodeVo.setItemBo(labelPrint.getElementBo());
            ids.add(labelPrint.getElementBo());
        } else {
            //箱条码处理，待表完善
            ResponseData<PackLevel> packData = packingService.getPackLevelByBo(labelPrint.getElementBo());
            if (!ResultResponseEnum.SUCCESS.getCode().equals(packData.getCode())) {
                return ResponseData.error("查询包装明细信息失败");
            }
            PackLevel packLevel = packData.getData();
            checkBarCodeVo.setAmount(printDetail.getPackingQuantity().intValue());
            checkBarCodeVo.setItemBo(packLevel.getObjectBo());
            ids.add(packLevel.getObjectBo());
        }
        List<Item> itemList = itemFeignService.getItemList(ids);
        if (CollectionUtil.isEmpty(itemList)) {
            return ResponseData.error("没有获取到对应的物料数据");
        }
        Item item = itemList.get(0);
        checkBarCodeVo.setItem(item.getItem());
        checkBarCodeVo.setItemName(item.getItemName());
        checkBarCodeVo.setItemDesc(item.getItemDesc());
        checkBarCodeVo.setUnit(item.getItemUnit());
        return ResponseData.success(checkBarCodeVo);
    }

    @Override
    public Map<String, String> getItemInfoAndSnStateBySn(String sn) {
        return snMapper.getItemInfoAndSnStateBySn(sn);
    }

    @Override
    public void collar(String sn, String workShopBo, String productLineBo) {
        Sn ret = new Sn();
        UserTDto currentUser = UserUtils.getCurrentUser();
        Date date = new Date();
        ret.setGetUser(currentUser.getRealName());
        ret.setGetTime(date);
        ret.setModifyUser(currentUser.getUserName());
        ret.setModifyDate(date);
        ret.setWorkShopBo(workShopBo);
        ret.setProductLineBo(productLineBo);
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.MONTH, 2);
        ret.setDeadline(instance.getTime());
        this.updateById(ret);
    }

    @Override
    public Boolean changeSnStateByMap(Map<String, String> map) {
        List<Sn> list = Lists.newArrayList();
        final String site = UserUtils.getSite();
        map.forEach((k, v) -> list.add(new Sn().setBo(new SnHandleBO(site, k).getBo()).setState(v)));
        return this.updateBatchById(list);
    }

    @Override
    public void saveBatchAuto(List<Sn> sns) throws SQLException {
        Connection conn = null;
        try {
            conn = sqlSession.getConfiguration().getEnvironment().getDataSource().getConnection();
            //关闭自动提交，即开启事务
            conn.setAutoCommit(false);
            String sql = "INSERT INTO z_sn (BO, SITE, SN, COMPLEMENT_CODE_STATE, ITEM_BO, STATE, CREATE_DATE, LABEL_PRINT_BO, LABEL_PARAMS) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement psInsert = conn.prepareStatement(sql);
            int index=0;
            for (Sn pay : sns) {
                index++;
                psInsert.setString(1, pay.getBo());
                psInsert.setString(2, pay.getSite());
                psInsert.setString(3, pay.getSn());
                psInsert.setString(4, pay.getComplementCodeState());
                psInsert.setString(5, pay.getItemBo());
                psInsert.setString(6, pay.getState());
                psInsert.setDate(7, new java.sql.Date(pay.getCreateDate().getTime()));
                psInsert.setString(8, pay.getLabelPrintBo());
                psInsert.setString(9, pay.getLabelParams());
                psInsert.addBatch();
                if(index==1000){
                    psInsert.executeBatch();
                }
            }
            psInsert.executeBatch();
            psInsert.close();
        } catch (RuntimeException |  SQLException e) {
            conn.rollback();
            throw new RuntimeException("保存条码失败，请检查需要生成的数据是否冲突");
        } finally {
            conn.setAutoCommit(true);
        }
    }

    /**
     * 工单拆单-变更条码工单
     * @param snBoList
     * @param newOrderBo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseData updateOrderBo(List<String> snBoList, String newOrderBo) {
        final Sn sn1 = new Sn();
        sn1.setShopOrder(newOrderBo);
        snMapper.update(sn1, new QueryWrapper<Sn>().lambda().in(Sn::getBo, snBoList));
        return ResponseData.success();
    }

    @Override
    /**
     * 拆单使用：查询拆当前工单产品SN，未上线状态的条码BO
     * @param orderBo
     * @param onLine
     * @return
     */
    public List<String> queryOrderBoList(String orderBo,int onLine) {
        List<String> boList = new ArrayList<>();
        boList = snMapper.queryOrderBoList(orderBo,onLine);
        return boList;
    }

    @Override
    public Page<LabelTransVo> labelTransList(Map<String, Object> params) {
        Page<LabelTransVo> queryPage = new QueryPage<>(params);
        params.put("site", UserUtils.getSite());
        List<LabelTransVo> list = labelMapper.labelTransList(queryPage, params);
        if(CollUtil.isNotEmpty(list)) {
            list.forEach(e -> {
                e.setStateDesc(SnTypeEnum.parseDescByCode(e.getState()));
            });
        }
        queryPage.setRecords(list);
        return queryPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean transferLabels(LabelTransferRequestDTO labelTransferRequestDTO) {
        String newOrderNo = labelTransferRequestDTO.getNewOrderNo();
        List<String> labelIds = labelTransferRequestDTO.getLabelIds();
        //校验新工单可打印条码数量是否大于等于所转移条码数量
        ShopOrderHandleBO shopOrderHandleBO = new ShopOrderHandleBO(newOrderNo);
        ResponseData<ShopOrderFullVo> shopOrderData = shopOrderService.getShopOrder(shopOrderHandleBO.getShopOrder());
        if (!shopOrderData.isSuccess()) {
            throw new CommonException(shopOrderData.getMsg(), CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        ShopOrderFullVo shopOrderFullVo = shopOrderData.getData();
        Assert.valid(shopOrderFullVo == null, "未找到工单"+newOrderNo);
        BigDecimal orderSum = NumberUtil.add(shopOrderFullVo.getOrderQty(), shopOrderFullVo.getOverfulfillQty());
        if (orderSum.compareTo(NumberUtil.add(shopOrderFullVo.getLabelQty(), new BigDecimal(labelIds.size()))) < 0) {
            throw new CommonException("工单最多可转移"+ orderSum.subtract(shopOrderFullVo.getLabelQty()) + "个，已超出可转移数", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        //校验条码对应产品状态， 是否为  报废/完工
        Map<String, Object> params = new HashMap<>(16);
        params.put("labelIds", labelIds);
        params.put("site", UserUtils.getSite());
        List<LabelTransVo> labelTransVos = labelMapper.labelTransList(params);
        Assert.valid(CollUtil.isEmpty(labelTransVos), "未找到条码数据");

        //条码数量
        if(labelTransVos.size() != labelIds.size()) {
            List<String> bos = labelTransVos.stream().map(LabelTransVo::getBo).collect(Collectors.toList());
            List<String> subtractList = CollUtil.subtractToList(labelIds, bos);
            Assert.valid(CollUtil.isNotEmpty(subtractList), CollUtil.join(subtractList, ",") + "未找到");
        }

        //已完工的
        List<String> doneLabelCode = labelTransVos.stream().filter(e -> StrUtil.equals(e.getIsDoneFlag(), "Y")).map(LabelTransVo::getDetailCode).collect(Collectors.toList());
        Assert.valid(CollUtil.isNotEmpty(doneLabelCode), "条码"+ CollUtil.join(doneLabelCode, ",") + "已完工，不允许转移");
        //已报废的
        List<String> scrappedCode = labelTransVos.stream().filter(e -> StrUtil.equals(e.getIsScrappedFlag(), "Y")).map(LabelTransVo::getDetailCode).collect(Collectors.toList());
        Assert.valid(CollUtil.isNotEmpty(scrappedCode), "条码"+ CollUtil.join(scrappedCode, ",") + "已报废，不允许转移");

        Collection<Sn> sns = listByIds(labelIds);
        //根据打印任务id分组
        Map<String, List<Sn>> groupByPrintBo = sns.stream().collect(Collectors.groupingBy(Sn::getLabelPrintBo));
        List<String> labelPrintIds = sns.stream().map(Sn::getLabelPrintBo).distinct().collect(Collectors.toList());
        List<LabelPrint> labelPrints = labelPrintMapper.selectBatchIds(labelPrintIds);
        Assert.valid(CollUtil.isEmpty(labelPrints), "未找到标签打印任务");

        Map<String, LabelPrint> labelPrintMap = labelPrints.stream().distinct().collect(Collectors.toMap(LabelPrint::getBo, e -> e));
        String userName = UserUtils.getUserName();
        UserTDto currentUser = UserUtils.getCurrentUser();
        String realName = currentUser == null ? null : currentUser.getRealName();
        String site = UserUtils.getSite();
        Date now = new Date();

        //获取工单工艺路线
        ResponseData<OrderRouter> orderRouterResult = orderRouterService.getOrderRouter(shopOrderFullVo.getBo());
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

        //执行条码转移操作  更新条码绑定新工单
        String finalProcessInfo = processInfo;
        groupByPrintBo.forEach((k, v) -> {
            LabelPrint labelPrint = labelPrintMap.get(k);
            //1. 新增条码转移 任务单
            LabelPrint newLabelPrint = new LabelPrint();
            BeanUtils.copyProperties(labelPrint, newLabelPrint);
            newLabelPrint.setBo(null);
            newLabelPrint.setPrintDate(now);
            newLabelPrint.setCreateDate(now);
            newLabelPrint.setCreateUser(userName);
            newLabelPrint.setBarCodeAmount(v.size());
            newLabelPrint.setElementBo(shopOrderFullVo.getBo());
            labelPrintMapper.insert(newLabelPrint);
            //修改原条码对应的
            List<String> snIds = v.stream().map(Sn::getBo).collect(Collectors.toList());
            List<Sn> updateList = new ArrayList<>();
            snIds.forEach(e-> {
                Sn sn = new Sn();
                sn.setBo(e);
                sn.setLabelPrintBo(newLabelPrint.getBo());
                updateList.add(sn);
            });
            updateBatchById(updateList);
            //处理工单标签数量变更
            if (ElementEnum.SHOP_ORDER.getType().equals(newLabelPrint.getElementType())) {
                shopOrderService.updateShopOrderLabelQtyByBO(newLabelPrint.getElementBo(), new BigDecimal(newLabelPrint.getBarCodeAmount()));
            }
            //修改条码对应工艺路线
            List<String> snList = v.stream().map(Sn::getSn).collect(Collectors.toList());
            saveSnRouter(snList, finalProcessInfo, site);
        });

        //2. 关闭原有的条码对应产品状态记录， 新增新的条码对应产品状态记录
        meProductStatusService.closeBySnboList(labelIds);
        List<MeProductStatus> meProductStatuses = new ArrayList<>();
        List<SnTransRecord> snTransRecords = new ArrayList<>();
        for (LabelTransVo labelTransVo : labelTransVos) {
            MeProductStatus meProductStatus = new MeProductStatus();
            meProductStatus.setSnBo(labelTransVo.getBo());
            meProductStatus.setState(1);
            meProductStatus.setProductCode(shopOrderFullVo.getItem());
            meProductStatus.setProductName(shopOrderFullVo.getItemName());
            meProductStatus.setShopOrder(shopOrderFullVo.getShopOrder());
            meProductStatuses.add(meProductStatus);
            snTransRecords.add(
                SnTransRecord.builder()
                    .snBo(labelTransVo.getBo())
                    .sn(labelTransVo.getDetailCode())
                    .oldOrderNo(labelTransVo.getElementCode())
                    .newOrderNo(shopOrderFullVo.getShopOrder())
                    .userName(userName)
                    .realName(realName)
                    .transDate(now)
                    .site(site)
                    .build()
            );
        }
        //保存条码转移记录
        snTransRecordService.saveBatch(snTransRecords);
        return meProductStatusService.saveBatch(meProductStatuses);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean transferLabelsAsOrder(LabelTransferRequestDTO labelTransferRequestDTO) {
        String newOrderNo = labelTransferRequestDTO.getNewOrderNo();
        List<String> labelIds = labelTransferRequestDTO.getLabelIds();
        //校验新工单可打印条码数量是否大于等于所转移条码数量
//        ShopOrderHandleBO shopOrderHandleBO = new ShopOrderHandleBO(newOrderNo);
//        ResponseData<ShopOrderFullVo> shopOrderData = shopOrderService.getShopOrder(shopOrderHandleBO.getShopOrder());
//        if (!shopOrderData.isSuccess()) {
//            throw new CommonException(shopOrderData.getMsg(), CommonExceptionDefinition.BASIC_EXCEPTION);
//        }
//        ShopOrderFullVo shopOrderFullVo = shopOrderData.getData();
//        Assert.valid(shopOrderFullVo == null, "未找到工单"+newOrderNo);
//        BigDecimal orderSum = NumberUtil.add(shopOrderFullVo.getOrderQty(), shopOrderFullVo.getOverfulfillQty());
//        if (orderSum.compareTo(NumberUtil.add(shopOrderFullVo.getLabelQty(), new BigDecimal(labelIds.size()))) < 0) {
//            throw new CommonException("工单最多可转移"+ orderSum.subtract(shopOrderFullVo.getLabelQty()) + "个，已超出可转移数", CommonExceptionDefinition.VERIFY_EXCEPTION);
//        }
        // 新工单保存对象
        ShopOrderTwoSaveVo shopOrderTwoSaveVo= labelTransferRequestDTO.getShopOrderTwoSaveVo();
        Assert.valid(shopOrderTwoSaveVo == null, "transferLabelsAsOrder()工单参数对象不能为空！");

        // 新工单的工艺路线
        OrderRouter orderRouter= labelTransferRequestDTO.getOrderRouter();
        Assert.valid(orderRouter == null, "transferLabelsAsOrder()工单工艺路线参数对象不能为空！");

        //校验条码对应产品状态， 是否为  报废/完工
        Map<String, Object> params = new HashMap<>(16);
        params.put("labelIds", labelIds);
        params.put("site", UserUtils.getSite());
        List<LabelTransVo> labelTransVos = labelMapper.labelTransListAsOrder(params);
        Assert.valid(CollUtil.isEmpty(labelTransVos), "未找到条码数据");

        //条码数量
        if(labelTransVos.size() != labelIds.size()) {
            List<String> bos = labelTransVos.stream().map(LabelTransVo::getBo).collect(Collectors.toList());
            List<String> subtractList = CollUtil.subtractToList(labelIds, bos);
            Assert.valid(CollUtil.isNotEmpty(subtractList), CollUtil.join(subtractList, ",") + "未找到");
        }

        //已完工的
//        List<String> doneLabelCode = labelTransVos.stream().filter(e -> StrUtil.equals(e.getIsDoneFlag(), "Y")).map(LabelTransVo::getDetailCode).collect(Collectors.toList());
//        Assert.valid(CollUtil.isNotEmpty(doneLabelCode), "条码"+ CollUtil.join(doneLabelCode, ",") + "已完工，不允许转移");
        //已报废的
        List<String> scrappedCode = labelTransVos.stream().filter(e -> StrUtil.equals(e.getIsScrappedFlag(), "Y")).map(LabelTransVo::getDetailCode).collect(Collectors.toList());
        Assert.valid(CollUtil.isNotEmpty(scrappedCode), "条码"+ CollUtil.join(scrappedCode, ",") + "已报废，不允许转移");

        Collection<Sn> sns = listByIds(labelIds);
        //根据打印任务id分组
        Map<String, List<Sn>> groupByPrintBo = sns.stream().collect(Collectors.groupingBy(Sn::getLabelPrintBo));
        List<String> labelPrintIds = sns.stream().map(Sn::getLabelPrintBo).distinct().collect(Collectors.toList());
        List<LabelPrint> labelPrints = labelPrintMapper.selectBatchIds(labelPrintIds);
        Assert.valid(CollUtil.isEmpty(labelPrints), "未找到标签打印任务");

        Map<String, LabelPrint> labelPrintMap = labelPrints.stream().distinct().collect(Collectors.toMap(LabelPrint::getBo, e -> e));
        String userName = UserUtils.getUserName();
        UserTDto currentUser = UserUtils.getCurrentUser();
        String realName = currentUser == null ? null : currentUser.getRealName();
        String site = UserUtils.getSite();
        Date now = new Date();

        //获取工单工艺路线
        /*ResponseData<OrderRouter> orderRouterResult = orderRouterService.getOrderRouter(newOrderNo);
        String processInfo = null;
        if(orderRouterResult.isSuccess()) {
            OrderRouter orderRouter = orderRouterResult.getData();
            if(orderRouter != null) {
                OrderRouterProcess orderRouterProcess = orderRouter.getOrderRouterProcess();
                if(orderRouterProcess != null) {
                    processInfo = orderRouterProcess.getProcessInfo();
                }
            }
        }*/
        String processInfo = null;
        if(orderRouter != null) {
            OrderRouterProcess orderRouterProcess = orderRouter.getOrderRouterProcess();
            if(orderRouterProcess != null) {
                processInfo = orderRouterProcess.getProcessInfo();
            }
        }

        //执行条码转移操作  更新条码绑定新工单
        String finalProcessInfo = processInfo;
        groupByPrintBo.forEach((k, v) -> {
            LabelPrint labelPrint = labelPrintMap.get(k);
            //1. 新增条码转移 任务单
            LabelPrint newLabelPrint = new LabelPrint();
            BeanUtils.copyProperties(labelPrint, newLabelPrint);
            newLabelPrint.setBo(null);
            newLabelPrint.setPrintDate(now);
            newLabelPrint.setCreateDate(now);
            newLabelPrint.setCreateUser(userName);
            newLabelPrint.setBarCodeAmount(v.size());
            newLabelPrint.setElementBo(newOrderNo);
            labelPrintMapper.insert(newLabelPrint);
            //修改原条码对应的
            List<String> snIds = v.stream().map(Sn::getBo).collect(Collectors.toList());
            List<Sn> updateList = new ArrayList<>();
            snIds.forEach(e-> {
                Sn sn = new Sn();
                sn.setBo(e);
                sn.setLabelPrintBo(newLabelPrint.getBo());
                updateList.add(sn);
            });
            updateBatchById(updateList);
            //处理工单标签数量变更
//            if (ElementEnum.SHOP_ORDER.getType().equals(newLabelPrint.getElementType())) {
//                shopOrderService.updateShopOrderLabelQtyByBO(newLabelPrint.getElementBo(), new BigDecimal(newLabelPrint.getBarCodeAmount()));
//            }
            //修改条码对应工艺路线
            List<String> snList = v.stream().map(Sn::getSn).collect(Collectors.toList());
            saveSnRouter(snList, finalProcessInfo, site);
        });

        //2. 关闭原有的条码对应产品状态记录， 新增新的条码对应产品状态记录
        meProductStatusService.closeBySnboList(labelIds);
        List<MeProductStatus> meProductStatuses = new ArrayList<>();
        List<SnTransRecord> snTransRecords = new ArrayList<>();
        for (LabelTransVo labelTransVo : labelTransVos) {
            MeProductStatus meProductStatus = new MeProductStatus();
            meProductStatus.setSnBo(labelTransVo.getBo());
            meProductStatus.setState(1);
            meProductStatus.setProductCode(shopOrderTwoSaveVo.getItemCode());
            meProductStatus.setProductName(shopOrderTwoSaveVo.getItemName());
            meProductStatus.setShopOrder(shopOrderTwoSaveVo.getShopOrder());
            meProductStatuses.add(meProductStatus);
//            snTransRecords.add(
//                    SnTransRecord.builder()
//                            .snBo(labelTransVo.getBo())
//                            .sn(labelTransVo.getDetailCode())
//                            .oldOrderNo(labelTransVo.getElementCode())
//                            .newOrderNo(newOrderNo)
//                            .userName(userName)
//                            .realName(realName)
//                            .transDate(now)
//                            .site(site)
//                            .build()
//            );
        }
        //保存条码转移记录
//        snTransRecordService.saveBatch(snTransRecords);
        return meProductStatusService.saveBatch(meProductStatuses);
        //return true;
    }

    /**
     * 保存条码工艺路线
     * */
    private void saveSnRouter(List<String> sn, String processInfo, String site) {
        //保存条码工艺路线
        if(StrUtil.isNotBlank(processInfo)) {
            UpdateSnRouteDto updateSnRouteDto = new UpdateSnRouteDto();
            updateSnRouteDto.setSite(site);
            updateSnRouteDto.setProcessInfo(processInfo);
            updateSnRouteDto.setSnList(sn);
            meSnRouterService.addSnRoute(updateSnRouteDto);
        }
    }

}

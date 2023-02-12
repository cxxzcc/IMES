package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.CommonUtil;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.base.utils.ValidationUtil;
import com.itl.mes.core.api.bo.*;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.constant.OrderStateEnum;
import com.itl.mes.core.api.dto.CustomDataValRequest;
import com.itl.mes.core.api.dto.MboMitemDTO;
import com.itl.mes.core.api.entity.Bom;
import com.itl.mes.core.api.entity.BomComponnet;
import com.itl.mes.core.api.entity.Item;
import com.itl.mes.core.api.entity.ShopOrder;
import com.itl.mes.core.api.service.*;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import com.itl.mes.core.api.vo.ShopOrderFullVo;
import com.itl.mes.core.client.service.ClassFrequencyService;
import com.itl.mes.core.client.service.MeProductInspectionItemsOrderService;
import com.itl.mes.core.provider.mapper.CustomDataValMapper;
import com.itl.mes.core.provider.mapper.ShopOrderMapper;
import com.itl.mes.me.api.dto.MeProductGroupInspectionItemsDto;
import com.itl.mes.me.api.dto.MeProductInspectionItemsDto;
import com.itl.mes.me.api.entity.MeProductGroupInspectionItemsEntity;
import com.itl.mes.me.api.entity.MeProductInspectionItemsEntity;
import com.itl.mes.me.api.entity.MeProductInspectionItemsOrderEntity;
import com.itl.mes.pp.api.entity.schedule.ScheduleEntity;
import com.itl.mes.pp.api.entity.scheduleplan.ClassFrequencyEntity;
import com.itl.pp.core.client.PpService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 工单表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-06-17
 */
@Service
@Transactional
public class ShopOrderServiceImpl extends ServiceImpl<ShopOrderMapper, ShopOrder> implements ShopOrderService {


    @Autowired
    private ShopOrderMapper shopOrderMapper;

    @Autowired
    private CustomDataValService customDataValService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private MeProductInspectionItemsOrderService meProductInspectionItemsOrderService;

    @Autowired
    private BomService bomService;

    @Autowired
    private PpService ppService;

    @Autowired
    private ProductLineService productLineService;

    @Autowired
    private CustomDataValMapper customDataValMapper;

    @Resource
    private UserUtil userUtil;

    @Autowired
    private BomComponnetService bomComponnetService;


    @Autowired
    private ShopOrderBomComponnetService shopOrderBomComponnetService;

    @Autowired
    private ShopOrderPackRuleService shopOrderPackRuleService;
    @Autowired
    private ClassFrequencyService classFrequencyService;



    @Override
    public List<ShopOrder> selectList() {
        QueryWrapper<ShopOrder> entityWrapper = new QueryWrapper<ShopOrder>();
        return super.list(entityWrapper);
    }

    /**
     * 通过ShopOrderHandleBO查询存在的工单
     *
     * @param shopOrderHandleBO 工单BOHandle
     * @return ShopOrder
     * @throws CommonException 扔异常
     */
    @Override
    public ShopOrder getExistShopOrder(ShopOrderHandleBO shopOrderHandleBO) throws CommonException {
        ShopOrder shopOrder = super.getById(shopOrderHandleBO.getBo());
        /*if (shopOrder == null) {
            throw new CommonException("工单" + shopOrderHandleBO.getShopOrder() + "未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }*/
        return shopOrder;
    }

    /**
     * 通过ShopOrderHandleBO查询工单
     *
     * @param shopOrderHandleBO 工单BOHandle
     * @return 异常
     */
    @Override
    public ShopOrder getShopOrder(ShopOrderHandleBO shopOrderHandleBO) {
        return super.getById(shopOrderHandleBO.getBo());
    }


    /**
     * 查询工单相关数据
     *
     * @param shopOrderHandleBO 工单BOHandle
     * @return ShopOrderFullVo
     * @throws CommonException 异常
     */
    @Override
    public ShopOrderFullVo getShopFullOrder(ShopOrderHandleBO shopOrderHandleBO) throws CommonException {

        ShopOrder shopOrder = getExistShopOrder(shopOrderHandleBO);
        ShopOrderFullVo shopOrderFullVo = new ShopOrderFullVo();
        BeanUtils.copyProperties(shopOrder, shopOrderFullVo);

        ItemHandleBO itemHandleBO = new ItemHandleBO(shopOrder.getItemBo());
        shopOrderFullVo.setItem(itemHandleBO.getItem());
        shopOrderFullVo.setItemVersion(itemHandleBO.getVersion());
        shopOrderFullVo.setState(new StatusHandleBO(shopOrder.getStateBo()).getState());

        //组装工单BOM
        if (!StrUtil.isBlank(shopOrder.getOrderBomBo())) {
            BomHandleBO bomHandleBO = new BomHandleBO(shopOrder.getOrderBomBo());
            shopOrderFullVo.setOrderBom(bomHandleBO.getBom());
            shopOrderFullVo.setOrderBomVersion(bomHandleBO.getVersion());
        }

        //组装BOM
        if (!StrUtil.isBlank(shopOrder.getBomBo())) {

            BomHandleBO bomHandleBO = new BomHandleBO(shopOrder.getBomBo());
            shopOrderFullVo.setBom(bomHandleBO.getBom());
            shopOrderFullVo.setBomVersion(bomHandleBO.getVersion());
        }
        //组装工艺路线
        if (!StrUtil.isBlank(shopOrder.getRouterBo())) {

            RouterHandleBO routerHandleBO = new RouterHandleBO(shopOrder.getRouterBo());
            shopOrderFullVo.setRouter(routerHandleBO.getRouter());
            shopOrderFullVo.setRouterVersion(routerHandleBO.getVersion());
        }
        //组装产线
        if (!StrUtil.isBlank(shopOrder.getProductLineBo())) {

            ProductLineHandleBO productLineHandleBO = new ProductLineHandleBO(shopOrder.getProductLineBo());
            shopOrderFullVo.setProductLine(productLineHandleBO.getProductLine());
        }
        //获取已排程数量
        ResponseData<List<ScheduleEntity>> byScheduleShopOrder = ppService.getByScheduleShopOrder(shopOrderFullVo.getBo());
        List<ScheduleEntity> data = byScheduleShopOrder.getData();
        if(CollectionUtil.isNotEmpty(data)){
            shopOrderFullVo.setSchedulQty(data.size());
        }

        //获取物料描述
        Item exitsItem = itemService.getExitsItemByItemHandleBO(new ItemHandleBO(shopOrder.getItemBo()));
        shopOrderFullVo.setItemBo(shopOrder.getItemBo());
        shopOrderFullVo.setItemDesc(exitsItem.getItemDesc());
        shopOrderFullVo.setItemName(exitsItem.getItemName());
        shopOrderFullVo.setCustomDataAndValVoList(customDataValMapper.selectCustomDataAndValByBoAndDataTypeFast(shopOrderHandleBO.getSite(),
                shopOrderHandleBO.getBo(), CustomDataTypeEnum.SHOP_ORDER.getDataType()));

        //获取班次描述
        if(StrUtil.isNotBlank(shopOrder.getShiftBo())) {
            ResponseData<ClassFrequencyEntity> result = classFrequencyService.getById(shopOrder.getShiftBo());
            if(result.isSuccess()) {
                ClassFrequencyEntity classFrequencyEntity = result.getData();
                if (classFrequencyEntity != null) {
                    shopOrderFullVo.setShiftName(classFrequencyEntity.getName());
                    shopOrderFullVo.setShiftBo(classFrequencyEntity.getId());
                }
            }
        }
        return shopOrderFullVo;
    }

    /**
     * 保存工单相关数据
     *
     * @param shopOrderFullVo 工单shopOrderFullVo
     * @throws CommonException 异常
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void saveShopOrder(ShopOrderFullVo shopOrderFullVo) throws CommonException {
        /*String bo = shopOrderFullVo.getBo();
        if (StrUtil.isBlank(bo)) {
            throw new CommonException("编号不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }*/
        String shopOrderStr = shopOrderFullVo.getShopOrder();
        if (StrUtil.isBlank(shopOrderStr)) {
            throw new CommonException("工单编号不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        ShopOrderHandleBO shopOrderHandleBO = new ShopOrderHandleBO(UserUtils.getSite(), shopOrderFullVo.getShopOrder());
        ShopOrder shopOrder = getShopOrder(shopOrderHandleBO);

        if (shopOrder == null) { //新增
            insertShopOrder(shopOrderFullVo);
        } else { //更新
            if(shopOrder==null){
                throw new CommonException("工单不存在", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            String stateBo = shopOrder.getStateBo();
            String[] split = stateBo.split(",");
            //原本状态
            String state = split[1];
            if(OrderStateEnum.CLOSED.getCode().equals(state)){
                throw new CommonException("关闭后不能修改", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            updateShopOrder(shopOrder, shopOrderFullVo);
        }

        // 产品检验项副本
        String itemBo = shopOrderFullVo.getItemBo();
        String orderBo =shopOrderHandleBO.getBo();

        // 查询产品组检验项
        MeProductGroupInspectionItemsDto meProductGroupInspectionItemsDto = new MeProductGroupInspectionItemsDto();
        meProductGroupInspectionItemsDto.setItemGroupBo(itemBo); // 产品组编码
        List<MeProductGroupInspectionItemsEntity> meProductGroupInspectionItemsEntityList = meProductInspectionItemsOrderService.listGroupItems(meProductGroupInspectionItemsDto);
        if (meProductGroupInspectionItemsEntityList!=null){
            for(int i=0;i<meProductGroupInspectionItemsEntityList.size();i++){
                MeProductInspectionItemsOrderEntity meProductInspectionItemsOrderEntity = new MeProductInspectionItemsOrderEntity();
                meProductInspectionItemsOrderEntity.setOrderBo(orderBo);
                MeProductGroupInspectionItemsEntity meProductGroupInspectionItemsEntity = meProductGroupInspectionItemsEntityList.get(i);
                if (ObjectUtil.isNotEmpty(meProductGroupInspectionItemsEntity)) {
                    BeanUtils.copyProperties(meProductGroupInspectionItemsEntity, meProductInspectionItemsOrderEntity);
                    meProductInspectionItemsOrderEntity.setItemType("1"); // 产品类型（组）
                    meProductInspectionItemsOrderEntity.setItemGroupBo(itemBo);
                    meProductInspectionItemsOrderEntity.setCreateDate(new Date());
                    meProductInspectionItemsOrderEntity.setCreateUser(UserUtils.getCurrentUser().getUserName());
                    meProductInspectionItemsOrderService.save(meProductInspectionItemsOrderEntity);
                }
            }
        }
        // 查询产品检验项
        MeProductInspectionItemsDto meProductInspectionItemsDto = new MeProductInspectionItemsDto();
        meProductInspectionItemsDto.setItemBo(itemBo); // 产品编码
        List<MeProductInspectionItemsEntity> meProductInspectionItemsEntityList = meProductInspectionItemsOrderService.listItems(meProductInspectionItemsDto);
        if (meProductInspectionItemsEntityList!=null){
            for(int j=0;j<meProductInspectionItemsEntityList.size();j++) {
                MeProductInspectionItemsOrderEntity meProductInspectionItemsOrderEntity = new MeProductInspectionItemsOrderEntity();
                meProductInspectionItemsOrderEntity.setOrderBo(orderBo);
                MeProductInspectionItemsEntity meProductInspectionItemsEntity = meProductInspectionItemsEntityList.get(j);
                if (ObjectUtil.isNotEmpty(meProductInspectionItemsEntity)) {
                    BeanUtils.copyProperties(meProductInspectionItemsEntity, meProductInspectionItemsOrderEntity);
                    meProductInspectionItemsOrderEntity.setItemType("0"); // 产品类型（组）
                    meProductInspectionItemsOrderEntity.setItemBo(itemBo);
                    meProductInspectionItemsOrderEntity.setCreateDate(new Date());
                    meProductInspectionItemsOrderEntity.setCreateUser(UserUtils.getCurrentUser().getUserName());
                    meProductInspectionItemsOrderService.save(meProductInspectionItemsOrderEntity);
                }
            }
        }

        // 保存工艺路线副本
/*        RouterHandleBO routerHandleBO = new RouterHandleBO(shopOrder.getRouterBo());
        shopOrderFullVo.getRouter();
        shopOrderFullVo.getRouterVersion();*/

        //保存工单BOM和工序BOM
        if (ObjectUtil.isNotEmpty(shopOrderFullVo.getShopOrderBomComponnetSaveDto())) {
            shopOrderFullVo.getShopOrderBomComponnetSaveDto().setShopOrderBo(shopOrderHandleBO.getBo());
            shopOrderBomComponnetService.save(shopOrderFullVo.getShopOrderBomComponnetSaveDto());
        }
        if (ObjectUtil.isNotEmpty(shopOrderFullVo.getProcessBomComponnetSaveDto())) {
            shopOrderFullVo.getProcessBomComponnetSaveDto().setShopOrderBo(shopOrderHandleBO.getBo());
            shopOrderBomComponnetService.save(shopOrderFullVo.getProcessBomComponnetSaveDto());
        }

        //保存工单包装规则
        if (ObjectUtil.isNotEmpty(shopOrderFullVo.getShopOrderPackRuleSaveDto())) {
            shopOrderFullVo.getShopOrderPackRuleSaveDto().setShopOrderBo(shopOrderHandleBO.getBo());
            shopOrderPackRuleService.save(shopOrderFullVo.getShopOrderPackRuleSaveDto());
        }

        //保存自定义数据
        if (CollUtil.isNotEmpty(shopOrderFullVo.getCustomDataValVoList())) {
            CustomDataValRequest customDataValRequest = new CustomDataValRequest();
            customDataValRequest.setBo(shopOrderHandleBO.getBo());
            customDataValRequest.setSite(shopOrderHandleBO.getSite());
            customDataValRequest.setCustomDataType(CustomDataTypeEnum.SHOP_ORDER.getDataType());
            customDataValRequest.setCustomDataValVoList(shopOrderFullVo.getCustomDataValVoList());
            customDataValService.saveCustomDataVal(customDataValRequest);
        }
    }

    @Override
    public Boolean stopByStatus(ShopOrderFullVo shopOrderFullVo) throws CommonException {
        String bo = shopOrderFullVo.getBo();
        if (StrUtil.isBlank(bo)) {
            throw new CommonException("编号不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        ShopOrder shopOrder = shopOrderMapper.selectById(bo);
        if(shopOrder==null){
            throw new CommonException("工单不存在", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        String stateBo = shopOrder.getStateBo();
        String[] split = stateBo.split(",");
        //原本状态
        String state = split[1];
        //完工 关闭 暂停 生产中暂停
        if (OrderStateEnum.OVER.getCode().equals(state) || OrderStateEnum.CLOSED.getCode().equals(state)
                || OrderStateEnum.PAUSE.getCode().equals(state) || OrderStateEnum.PRO_PAUSE.getCode().equals(state)) {
            throw new CommonException("已处于暂停状态", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if (OrderStateEnum.ING.getCode().equals(state)) {
            shopOrder.setStateBo(new StatusHandleBO(UserUtils.getSite(), OrderStateEnum.PRO_PAUSE.getCode()).getBo());
        } else {
            shopOrder.setStateBo(new StatusHandleBO(UserUtils.getSite(), OrderStateEnum.PAUSE.getCode()).getBo());
        }
        shopOrder.setRecoveryStatus(state);
        updateById(shopOrder);
        return true;
    }

    @Override
    public Boolean recoveryByStatus(ShopOrderFullVo shopOrderFullVo) throws CommonException {
        String bo = shopOrderFullVo.getBo();
        if (StrUtil.isBlank(bo)) {
            throw new CommonException("编号不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        ShopOrder shopOrder = shopOrderMapper.selectById(bo);
        if(shopOrder==null){
            throw new CommonException("工单不存在", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        String stateBo = shopOrder.getStateBo();
        String[] split = stateBo.split(",");
        //原本状态
        String state = split[1];
        if (!OrderStateEnum.PAUSE.getCode().equals(state) && !OrderStateEnum.PRO_PAUSE.getCode().equals(state)) {
            throw new CommonException("未处于暂停状态或生产中暂停状态", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        shopOrder.setStateBo(new StatusHandleBO(UserUtils.getSite(), shopOrder.getRecoveryStatus()).getBo());
        shopOrder.setRecoveryStatus(state);
        updateById(shopOrder);
        return true;
    }

    @Override
    public Boolean closeByStatus(ShopOrderFullVo shopOrderFullVo) throws CommonException {
        String bo = shopOrderFullVo.getBo();
        if (StrUtil.isBlank(bo)) {
            throw new CommonException("编号不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        ShopOrder shopOrder = shopOrderMapper.selectById(bo);
        if(shopOrder==null){
            throw new CommonException("工单不存在", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        String stateBo = shopOrder.getStateBo();
        String[] split = stateBo.split(",");
        //原本状态
        String state = split[1];
        //完工 关闭
        if (OrderStateEnum.OVER.getCode().equals(state) || OrderStateEnum.CLOSED.getCode().equals(state)) {
            throw new CommonException("已处于完工状态或关闭状态", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        shopOrder.setStateBo(new StatusHandleBO(UserUtils.getSite(), OrderStateEnum.CLOSED.getCode()).getBo());
        shopOrder.setRecoveryStatus(state);
        updateById(shopOrder);
        return true;
    }


    /**
     * 新增
     *
     * @param shopOrderFullVo shopOrderFullVo
     * @throws CommonException 异常
     */
    private void insertShopOrder(ShopOrderFullVo shopOrderFullVo) throws CommonException {

        ShopOrderHandleBO shopOrderHandleBO = new ShopOrderHandleBO(UserUtils.getSite(), shopOrderFullVo.getShopOrder());
        ShopOrder shopOrder = new ShopOrder();
        shopOrder.setBo(shopOrderHandleBO.getBo());
        shopOrder.setSite(shopOrderHandleBO.getSite());
        shopOrder.setShopOrder(shopOrderFullVo.getShopOrder());
        shopOrder.setOrderDesc(StrUtil.isBlank(shopOrderFullVo.getOrderDesc()) ? shopOrderFullVo.getShopOrder() : shopOrderFullVo.getOrderDesc());
        shopOrder.setStateBo(new StatusHandleBO(shopOrderHandleBO.getSite(), shopOrderFullVo.getState()).getBo());
        shopOrder.setIsOverfulfill(shopOrderFullVo.getIsOverfulfill());
        shopOrder.setOrderQty(shopOrderFullVo.getOrderQty());
        shopOrder.setRouterBo(new RouterHandleBO(UserUtils.getSite(), shopOrderFullVo.getRouter(), shopOrderFullVo.getRouterVersion()).getBo());
        shopOrder.setOverfulfillQty(shopOrderFullVo.getOverfulfillQty());
        shopOrder.setOrderDeliveryTime(shopOrderFullVo.getOrderDeliveryTime());
        shopOrder.setNegotiationTime(shopOrderFullVo.getNegotiationTime());
        shopOrder.setFixedTime(shopOrderFullVo.getFixedTime());
        shopOrder.setShopOrderType(shopOrderFullVo.getShopOrderType());
        shopOrder.setOrderBomBo(shopOrderFullVo.getOrderBomBo());
        if (shopOrderFullVo.getPlanStartDate() != null) {
            shopOrder.setPlanStartDate(shopOrderFullVo.getPlanStartDate());
        }
        if (shopOrderFullVo.getPlanEndDate() != null) {
            shopOrder.setPlanEndDate(shopOrderFullVo.getPlanEndDate());
        }
        //开始时间不能大于完成时间
        if (shopOrderFullVo.getPlanStartDate() != null && shopOrderFullVo.getPlanEndDate() != null) {

            if (shopOrderFullVo.getPlanStartDate().getTime() > shopOrderFullVo.getPlanEndDate().getTime()) {
                throw new CommonException("计划开始时间不能大于计划完成时间", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
        }

        ItemHandleBO itemHandleBO = new ItemHandleBO(shopOrderHandleBO.getSite(), shopOrderFullVo.getItem(), shopOrderFullVo.getItemVersion());
        itemService.getExitsItemByItemHandleBO(itemHandleBO); //获取存在的物料
        shopOrder.setItemBo(itemHandleBO.getBo());

        //验证物料清单是否存在
        if (!StrUtil.isBlank(shopOrderFullVo.getBom()) && !StrUtil.isBlank(shopOrderFullVo.getBomVersion())) {

            Bom bom = bomService.selectByBom(shopOrderFullVo.getBom(), shopOrderFullVo.getBomVersion());
            shopOrder.setBomBo(bom.getBo());

        }

        //验证产线
        if (!StrUtil.isBlank(shopOrderFullVo.getProductLine())) {

            ProductLineHandleBO productLineHandleBO = new ProductLineHandleBO(shopOrderHandleBO.getSite(), shopOrderFullVo.getProductLine());
            productLineService.getExistProductLineByHandleBO(productLineHandleBO);
            shopOrder.setProductLineBo(productLineHandleBO.getBo());
        }

        shopOrder.setObjectSetBasicAttribute(userUtil.getUser().getUserName(), new Date());
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(shopOrder); //验证工单数据是否合规
        if (validResult.hasErrors()) {
            throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        //保存工单数据
        super.save(shopOrder);

    }

    /**
     * 更新工单数据
     *
     * @param shopOrder       已存在的工单数据
     * @param shopOrderFullVo 接受的数据
     */
    private void updateShopOrder(ShopOrder shopOrder, ShopOrderFullVo shopOrderFullVo) throws CommonException {

        Date frontModifyDate = shopOrderFullVo.getModifyDate(); //前台传递的时间值
        CommonUtil.compareDateSame(frontModifyDate, shopOrder.getModifyDate()); //比较时间是否相等
        ShopOrderHandleBO shopOrderHandleBO = new ShopOrderHandleBO(shopOrder.getBo());
        ShopOrder shopOrderEntity = new ShopOrder();
        shopOrderEntity.setBo(shopOrderHandleBO.getBo());
        shopOrderEntity.setOrderDesc(StrUtil.isBlank(shopOrderFullVo.getOrderDesc()) ? shopOrderFullVo.getShopOrder() : shopOrderFullVo.getOrderDesc());
        shopOrderEntity.setStateBo(new StatusHandleBO(shopOrderHandleBO.getSite(), shopOrderFullVo.getState()).getBo());
        shopOrderEntity.setIsOverfulfill(shopOrderFullVo.getIsOverfulfill());
        shopOrderEntity.setOrderQty(shopOrderFullVo.getOrderQty());
        shopOrderEntity.setOverfulfillQty(shopOrderFullVo.getOverfulfillQty());
        shopOrderEntity.setRouterBo(new RouterHandleBO(UserUtils.getSite(), shopOrderFullVo.getRouter(), shopOrderFullVo.getRouterVersion()).getBo());
        shopOrderEntity.setNegotiationTime(shopOrderFullVo.getNegotiationTime());
        shopOrderEntity.setFixedTime(shopOrderFullVo.getFixedTime());
        shopOrderEntity.setOrderDeliveryTime(shopOrderFullVo.getOrderDeliveryTime());
        shopOrderEntity.setShopOrderType(shopOrderFullVo.getShopOrderType());
        shopOrderEntity.setOrderBomBo(shopOrderFullVo.getOrderBomBo());
        shopOrderEntity.setModifyUser(userUtil.getUser().getUserName());
        shopOrderEntity.setModifyDate(new Date());
        if (shopOrderFullVo.getPlanStartDate() != null) {
            shopOrderEntity.setPlanStartDate(shopOrderFullVo.getPlanStartDate());
        } else {
            shopOrderEntity.setPlanStartDate(null);
        }
        if (shopOrderFullVo.getPlanEndDate() != null) {
            shopOrderEntity.setPlanEndDate(shopOrderFullVo.getPlanEndDate());
        } else {
            shopOrderEntity.setPlanEndDate(null);
        }
        //开始时间不能大于完成时间
        if (shopOrderEntity.getPlanStartDate() != null && shopOrderEntity.getPlanEndDate() != null) {

            if (shopOrderEntity.getPlanStartDate().getTime() > shopOrderEntity.getPlanEndDate().getTime()) {
                throw new CommonException("计划开始时间不能大于计划完成时间", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
        }

        ItemHandleBO itemHandleBO = new ItemHandleBO(shopOrderHandleBO.getSite(), shopOrderFullVo.getItem(), shopOrderFullVo.getItemVersion());
        itemService.getExitsItemByItemHandleBO(itemHandleBO); //获取存在的物料
        shopOrderEntity.setItemBo(itemHandleBO.getBo());


        if (!StrUtil.isBlank(shopOrderFullVo.getBom()) && !StrUtil.isBlank(shopOrderFullVo.getBomVersion())) {
            //验证物料清单是否存在
            Bom bom = bomService.selectByBom(shopOrderFullVo.getBom(), shopOrderFullVo.getBomVersion());
            shopOrderEntity.setBomBo(bom.getBo());
        } else {
            shopOrderEntity.setBomBo("");
        }

        if (!StrUtil.isBlank(shopOrderFullVo.getProductLine())) {
            //验证产线是否存在
            ProductLineHandleBO productLineHandleBO = new ProductLineHandleBO(shopOrderHandleBO.getSite(), shopOrderFullVo.getProductLine());
            productLineService.getExistProductLineByHandleBO(productLineHandleBO);
            shopOrderEntity.setProductLineBo(productLineHandleBO.getBo());
        } else {
            shopOrderEntity.setProductLineBo("");
        }

        //验证工单指定属性是否合规
        ValidationUtil.ValidResult validResult =
                ValidationUtil.validateProperties(shopOrderEntity, "orderDesc", "stateBo", "isOverfulfill", "orderQty");
        if (validResult.hasErrors()) {
            throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        Integer integer = shopOrderMapper.updateShopOrder(shopOrderEntity, shopOrder.getModifyDate());//更新工单数据
        if (integer == 0) {
            throw new CommonException("数据已修改，请查询后再执行操作", CommonExceptionDefinition.BASIC_EXCEPTION);
        }

    }


    /**
     * 验证工单是否被使用
     *
     * @param shopOrder 工单实类数据
     * @return boolean
     * @throws CommonException 异常
     */
    private boolean validateShopOrderIsUsed(ShopOrder shopOrder) throws CommonException {
        if (shopOrder == null) {
            return false;
        } else {
            //工单已排产
            if (shopOrder.getSchedulQty() != null && shopOrder.getSchedulQty().doubleValue() > 0) return true;
            //工单已下达
            if (shopOrder.getReleaseQty() != null && shopOrder.getReleaseQty().doubleValue() > 0) return true;
            //其它验证
            //其它验证
            //其它验证
            //其它验证
            return false;
        }

    }


    /**
     * 删除工单数据
     *
     * @param shopOrderHandleBO 工单BOHandle
     * @param modifyDate        修改时间
     * @throws CommonException 异常
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void deleteShopOrderByHandleBO(ShopOrderHandleBO shopOrderHandleBO, Date modifyDate) throws CommonException {

        ShopOrder shopOrder = getExistShopOrder(shopOrderHandleBO);
        CommonUtil.compareDateSame(modifyDate, shopOrder.getModifyDate());
        boolean isUsed = validateShopOrderIsUsed(shopOrder);
        if (isUsed) {
            throw new CommonException("工单" + shopOrder + "已生产，不能删除", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        QueryWrapper<ShopOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(ShopOrder.BO, shopOrder.getBo()).eq(ShopOrder.MODIFY_DATE, modifyDate);

        //删除工单数据
        Integer integer = shopOrderMapper.delete(wrapper);
        if (integer == 0) {
            throw new CommonException("数据已修改，请查询后再执行删除操作", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        //删除工单自定义数据
        customDataValService.deleteCustomDataValByBoAndType(shopOrderHandleBO.getSite(), shopOrderHandleBO.getBo(), CustomDataTypeEnum.SHOP_ORDER);


    }


    @Override
    public List<ShopOrder> getShopOrderByBomBO(String bomBO) {
        QueryWrapper<ShopOrder> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(ShopOrder.SITE, UserUtils.getSite());
        entityWrapper.eq(ShopOrder.BOM_BO, bomBO);
        List<ShopOrder> shopOrders = shopOrderMapper.selectList(entityWrapper);
        return shopOrders;
    }


    /**
     * 更新工单完成数量
     *
     * @param bo          工单BO
     * @param completeQty 完成数量
     * @return Integer
     */
    @Override
    public Integer updateShopOrderCompleteQtyByBO(String bo, BigDecimal completeQty) {

        return shopOrderMapper.updateShopOrderCompleteQtyByBO(bo, completeQty);
    }

    /**
     * 更新工单报废数量
     *
     * @param bo       工单BO
     * @param scrapTty 报废数量
     * @return Integer
     */
    @Override
    public Integer updateShopOrderScrapQtyByBO(String bo, BigDecimal scrapTty) {
        return shopOrderMapper.updateShopOrderCompleteQtyByBO(bo, scrapTty);
    }

    /**
     * 更新工单标签数量
     *
     * @param bo       工单BO
     * @param labelQty 标签数量
     * @return Integer
     */
    @Override
    public Integer updateShopOrderLabelQtyByBO(String bo, BigDecimal labelQty) {
        return shopOrderMapper.updateShopOrderLabelQtyByBO(bo, labelQty);
    }

    /**
     * 更新工单排产数量
     *
     * @param bo          工单BO
     * @param scheduleQty 排产数量
     * @return Integer
     */
    @Override
    public Integer updateShopOrderScheduleQtyByBO(String bo, BigDecimal scheduleQty) {
        return shopOrderMapper.updateShopOrderScheduleQtyByBO(bo, scheduleQty);
    }

    /**
     * 更新工单排产数量
     *
     * @param bo          工单BO
     * @param scheduleQty 排产数量
     * @return Integer
     */
    @Override
    public Integer updateShopOrderScheduleQtyAndOrderQtyByBO(String bo, BigDecimal scheduleQty, BigDecimal orderQty) {
        return shopOrderMapper.updateShopOrderScheduleQtyAndOrderQtyByBO(bo, scheduleQty, orderQty);
    }

    @Override
    @Transactional
    public Object getAllOrder(Map<String, Object> params, Integer pageNum, Integer pageSize) {
        Page page = new Page(pageNum, pageSize);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String site = UserUtils.getSite();
        //   String site = "1040";
        params.put("site", site);
        //先判断是否为自定义数据的字段并对自定义数据的查询
        List<String> boList = judgeCustomData(params);
        params.put("stateBo", Arrays.asList("STATE:" + site + ",500", "STATE:" + site + ",501"));
        if (boList != null && !boList.isEmpty()) params.put("boList", boList);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (params.get("timeParam") != null && StringUtils.isNotBlank(params.get("timeParam").toString())) {

            try {
                params.put("startTime", sdf.parse(format.format(new Date()) + " 00:00:00"));
                params.put("endTime", sdf.parse(afterNDay(Integer.valueOf(params.get("timeParam").toString())) + " 24:00:00"));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        List<Map<String, Object>> bindings = shopOrderMapper.getBindingBySite(site);
        List<Map<String, Object>> noFirstBinding = bindings.stream().filter(e -> Integer.valueOf(e.get("sort").toString()) != 1).collect(Collectors.toList());
        List<Map<String, Object>> firstBinding = bindings.stream().filter(e -> Integer.valueOf(e.get("sort").toString()) == 1).collect(Collectors.toList());
        if (noFirstBinding != null && !noFirstBinding.isEmpty()) {
            List<String> bo = noFirstBinding.stream().map(e -> e.get("bo").toString()).collect(Collectors.toList());
            if (bo != null && !bo.isEmpty()) {
                params.put("bindings", bo);
            }
        }

        List<String> orderList = shopOrderMapper.getScheduleShopOrder();
        if (orderList != null && !orderList.isEmpty()) {
            if (params.get("bindings") != null) {
                List<String> bo = (List<String>) params.get("bindings");
                bo.addAll(orderList);
            } else {
                params.put("bindings", orderList);
            }

        }


        IPage list = shopOrderMapper.getList(page, params);
        List<ShopOrder> shopOrderList = list.getRecords();

        List<Map> shopOrderFullVos = new ArrayList<>();

        shopOrderList.forEach(shopOrder -> {
            Map result = createShopOrderFullVo(shopOrder, site);
            shopOrderFullVos.add(result);

            if (firstBinding != null && !firstBinding.isEmpty()) {
                String no = "";
                for (Map<String, Object> stringObjectMap : firstBinding) {
                    if (shopOrder.getShopOrder().equals(stringObjectMap.get("bo").toString())) {
                        no = stringObjectMap.get("no").toString();
                    }
                }
                if (StringUtils.isNotBlank(no)) {
                    Map last = shopOrderFullVos.get(shopOrderFullVos.size() - 1);
                    String finalNo = no;
                    List<Map<String, Object>> bind = noFirstBinding.stream().filter(e -> finalNo.equals(e.get("no").toString())).collect(Collectors.toList());
                    bind.stream().sorted(Comparator.comparing(e -> e.get("sort").toString()));
                    last.put("margin", bind.size());
                    last.put("no", no);
                    bind.forEach(map -> {
                        QueryWrapper<ShopOrder> entityWrapper = new QueryWrapper<>();
                        entityWrapper.eq("SHOP_ORDER", map.get("bo").toString());
                        ShopOrder order = shopOrderMapper.selectOne(entityWrapper);
                        Map shopOrderFullVo = createShopOrderFullVo(order, site);
                        shopOrderFullVo.put("no", finalNo);
                        shopOrderFullVos.add(shopOrderFullVo);
                    });
                }
            }
        });
        list.setRecords(shopOrderFullVos);
        return list;
    }

    @Override
    public void updateShopOrderFullVo(ShopOrderFullVo shopOrderFullVo) {
        ShopOrder shopOrder = new ShopOrder();
        BeanUtils.copyProperties(shopOrderFullVo, shopOrder);
        shopOrderMapper.updateById(shopOrder);
    }

    @Override
    public void updateEmergenc(List<Map<String, Object>> shopOrderList) {
        shopOrderList.forEach(shopOrder -> {
            Map<String, Object> params = new HashMap<>();
            params.put("shopOrder", shopOrder.get("shopOrder").toString());
            params.put("emergencyState", shopOrder.get("emergencyState").toString());
            params.put("emergencyBz", shopOrder.get("emergencyBz").toString());
            shopOrderMapper.updateEmergenc(params);
        });
    }

    @Override
    public void updateFixedTime(String shopOrder, String fixedTime) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        QueryWrapper<ShopOrder> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq("SHOP_ORDER", shopOrder);
        ShopOrder order = shopOrderMapper.selectOne(entityWrapper);
        try {
            order.setFixedTime(sd.parse(fixedTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        shopOrderMapper.updateById(order);
    }

    @Override
    public List<MboMitemDTO> getBomComponents(String shopOrderBo) {
        ShopOrder shopOrder = getById(shopOrderBo);
        String bomBo = Optional.ofNullable(shopOrder).map(ShopOrder::getBomBo).orElse(null);
        if (bomBo != null) {
            List<BomComponnet> list = bomComponnetService.list(new QueryWrapper<BomComponnet>().lambda()
                    .eq(BomComponnet::getBomBo, bomBo)
                    .select(BomComponnet::getComponentBo));
            if (CollUtil.isNotEmpty(list)) {
                List<String> itemBos = list.stream().map(BomComponnet::getComponentBo).collect(Collectors.toList());
                return shopOrderMapper.getBomComponents(itemBos);
            }
        }
        return null;
    }


    @Override
    public Boolean updateShopOrderCompleteQtyAndState(String shopOrderBo, Integer completeQty) {
        return baseMapper.updateShopOrderCompleteQtyAndState(shopOrderBo, completeQty, new StatusHandleBO(UserUtils.getSite(), OrderStateEnum.OVER.getCode()).getBo()) > 0;
    }

    public Map createShopOrderFullVo(ShopOrder shopOrder, String site) {
        ShopOrderFullVo shopOrderFullVo = new ShopOrderFullVo();
        BeanUtils.copyProperties(shopOrder, shopOrderFullVo);

        if (shopOrder.getItemBo() != null) {
            ItemHandleBO itemHandleBO = new ItemHandleBO(shopOrder.getItemBo());
            shopOrderFullVo.setItem(itemHandleBO.getItem());
            shopOrderFullVo.setItemVersion(itemHandleBO.getVersion());
            shopOrderFullVo.setState(new StatusHandleBO(shopOrder.getStateBo()).getState());
        }


        //组装BOM
        if (!StrUtil.isBlank(shopOrder.getBomBo())) {

            BomHandleBO bomHandleBO = new BomHandleBO(shopOrder.getBomBo());
            shopOrderFullVo.setBom(bomHandleBO.getBom());
            shopOrderFullVo.setBomVersion(bomHandleBO.getVersion());
        }
        //组装工艺路线
        if (!StrUtil.isBlank(shopOrder.getRouterBo())) {

            RouterHandleBO routerHandleBO = new RouterHandleBO(shopOrder.getRouterBo());
            shopOrderFullVo.setRouter(routerHandleBO.getRouter());
            shopOrderFullVo.setRouterVersion(routerHandleBO.getVersion());
        }
        //组装产线
        if (!StrUtil.isBlank(shopOrder.getProductLineBo())) {

            ProductLineHandleBO productLineHandleBO = new ProductLineHandleBO(shopOrder.getProductLineBo());
            shopOrderFullVo.setProductLine(productLineHandleBO.getProductLine());
        }

        //获取物料描述
//        Item exitsItem = null;
//        try {
//            exitsItem = itemService.getExitsItemByItemHandleBO(new ItemHandleBO(shopOrder.getItemBo()));
//        } catch (CommonException e) {
//            throw new RuntimeException("获取物料失败");
//        }
//        shopOrderFullVo.setItemDesc(exitsItem.getItemDesc());

        //ZHUAN MAP
        Map result = JSONObject.parseObject(JSONObject.toJSONString(shopOrderFullVo), Map.class);

        List<CustomDataAndValVo> customDataAndValVos = customDataValService.selectOnlyCustomData(site, shopOrder.getBo(), CustomDataTypeEnum.SHOP_ORDER.getDataType());

//        List<CustomDataAndValVo> customDataAndValVos = customDataValService.selectCustomDataAndValByBoAndDataType(site, shopOrder.getBo(), CustomDataTypeEnum.SHOP_ORDER.getDataType());
        customDataAndValVos.forEach(vo -> {
            result.put(vo.getCdField(), vo.getVals());
        });
        String planStartDateStr = result.get("planStartDate") == null ? "" : result.get("planStartDate").toString();
        if (StringUtils.isNotBlank(planStartDateStr)) result.put("planStartDate", stringToDate(planStartDateStr));
        String planEndDateStr = result.get("planEndDate") == null ? "" : result.get("planEndDate").toString();
        if (StringUtils.isNotBlank(planEndDateStr)) result.put("planEndDate", stringToDate(planEndDateStr));
        String negotiationTimeStr = result.get("negotiationTime") == null ? "" : result.get("negotiationTime").toString();
        if (StringUtils.isNotBlank(negotiationTimeStr)) result.put("negotiationTime", stringToDate(negotiationTimeStr));
        String fixedTimeStr = result.get("fixedTime") == null ? "" : result.get("fixedTime").toString();
        if (StringUtils.isNotBlank(fixedTimeStr)) result.put("fixedTime", stringToDate(fixedTimeStr));
        String orderDeliveryTimeStr = result.get("orderDeliveryTime") == null ? "" : result.get("orderDeliveryTime").toString();
        if (StringUtils.isNotBlank(orderDeliveryTimeStr))
            result.put("orderDeliveryTime", stringToDate(orderDeliveryTimeStr));
        return result;
    }

    public String stringToDate(String lo) {
        long time = Long.parseLong(lo);
        Date date = new Date(time);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sd.format(date);
    }

    public String afterNDay(int n) {
        Calendar c = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        c.setTime(new Date());
        c.add(Calendar.DATE, n);
        Date d2 = c.getTime();
        String s = df.format(d2);
        return s;
    }

    private List<String> judgeCustomData(Map<String, Object> params) {
        List<String> boList = null;
        List<String> newList = new ArrayList<>();
        int n = 0;
        Set<String> keys = params.keySet();
        for (String key : keys) {
            if (key.startsWith("filter") && params.get(key) != null && StringUtils.isNotBlank(params.get(key).toString())) {
                //有查询
                String filter_ = key.replaceAll("filter_", "");
                Map<String, Object> map = new HashMap<>();
                map.put("key", filter_);
                map.put("value", params.get(key).toString());
                map.put("site", params.get("site").toString());
                n++;
                //根据参数进行查询
                boList = shopOrderMapper.getIdsByVals(map);
                boList.forEach(str -> {
                    newList.add(str);
                });

            }
        }

        if (n > 1) {
            List<String> list = new ArrayList<>();
            for (String str : newList) {
                long count = newList.stream().filter(e -> str.equals(e)).count();
                if (n == (int) count) {
                    list.add(str);
                }
            }
            if (list.isEmpty()) {
                list.add("noEmpty");
            }
            return list;
        } else {
            if (boList != null && boList.isEmpty()) {
                boList.add("noEmpty");
            }
            return boList;

        }
    }

}

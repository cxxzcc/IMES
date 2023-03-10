package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
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
import com.itl.iap.common.base.utils.Assert;
import com.itl.iap.common.base.utils.CommonUtil;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.*;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.constant.OrderStateEnum;
import com.itl.mes.core.api.dto.ItemPackRuleDetailDto;
import com.itl.mes.core.api.dto.MboMitemDTO;
import com.itl.mes.core.api.dto.ShopOrderBomComponnetSaveDto;
import com.itl.mes.core.api.dto.ShopOrderPackRuleSaveDto;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.*;
import com.itl.mes.core.api.vo.*;
import com.itl.mes.core.client.service.ClassFrequencyService;
import com.itl.mes.core.client.service.MeProductInspectionItemsOrderService;
import com.itl.mes.core.provider.mapper.CustomDataValMapper;
import com.itl.mes.core.provider.mapper.ShopOrderBomComponnetMapper;
import com.itl.mes.core.provider.mapper.ShopOrderMapper;
import com.itl.mes.me.api.dto.MeProductGroupInspectionItemsDto;
import com.itl.mes.me.api.dto.MeProductInspectionItemsDto;
import com.itl.mes.me.api.dto.MeProductInspectionItemsOrderDto;
import com.itl.mes.me.api.entity.MeProductGroupInspectionItemsEntity;
import com.itl.mes.me.api.entity.MeProductInspectionItemsEntity;
import com.itl.mes.me.api.entity.MeProductInspectionItemsOrderEntity;
import com.itl.mes.me.api.vo.MeProductInspectionItemsNcCodeVo;
import com.itl.mes.me.client.service.MeProductInspectionItemsNcCodeService;
import com.itl.mes.pp.api.entity.scheduleplan.ClassFrequencyEntity;
import com.itl.mom.label.api.dto.label.LabelTransferRequestDTO;
import com.itl.mom.label.client.service.SnService;
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
 * ????????? ???????????????
 * </p>
 *
 * @author space
 * @since 2019-06-17
 */
@Service
@Transactional
public class ShopOrderTwoServiceImpl extends ServiceImpl<ShopOrderMapper, ShopOrder> implements ShopOrderTwoService {


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
    public ShiftService shiftService;

    /**
     * ??????
     */
    @Autowired
    private ClassFrequencyService classFrequencyService;

    @Autowired
    private CustomDataValMapper customDataValMapper;

    @Autowired
    private ItemGroupService itemGroupService;

    @Resource
    private UserUtil userUtil;

    @Autowired
    private BomComponnetService bomComponnetService;


    @Autowired
    private ShopOrderBomComponnetService shopOrderBomComponnetService;

    @Autowired
    private ShopOrderBomComponnetMapper shopOrderBomComponnetMapper;

    @Autowired
    private ShopOrderPackRuleService shopOrderPackRuleService;

    @Autowired
    private RouterService routerService;

    @Autowired
    private OrderRouterService orderRouterService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private IProcessRouteFitProductService productService; //????????????????????????
    @Autowired
    private IProcessRouteFitProductGroupService productGroupService; //???????????????????????????
    @Autowired
    private IProcessRouteFitProductionLineService productionLineService; //????????????????????????

    @Autowired
    private SnService snService;

    /**
     * ????????????????????????????????????????????????
     */
    @Autowired
    private MeProductInspectionItemsNcCodeService meProductInspectionItemsNcCodeService;

    /**
     * ????????????????????????????????????????????????-????????????
     */
    @Autowired
    private MeProductInspectionItemsOrderNcCodeService meProductInspectionItemsOrderNcCodeService;


    @Override
    public List<ShopOrder> selectList() {
        QueryWrapper<ShopOrder> entityWrapper = new QueryWrapper<ShopOrder>();
        return super.list(entityWrapper);
    }

    /**
     * ??????ShopOrderHandleBO?????????????????????
     *
     * @param shopOrderHandleBO ??????BOHandle
     * @return ShopOrder
     * @throws CommonException ?????????
     */
    @Override
    public ShopOrder getExistShopOrder(ShopOrderHandleBO shopOrderHandleBO) throws CommonException {
        ShopOrder shopOrder = super.getById(shopOrderHandleBO.getBo());
        /*if (shopOrder == null) {
            throw new CommonException("??????" + shopOrderHandleBO.getShopOrder() + "?????????", CommonExceptionDefinition.BASIC_EXCEPTION);
        }*/
        return shopOrder;
    }

    /**
     * ??????ShopOrderHandleBO????????????
     *
     * @param shopOrderHandleBO ??????BOHandle
     * @return ??????
     */
    @Override
    public ShopOrder getShopOrder(ShopOrderHandleBO shopOrderHandleBO) {
        return super.getById(shopOrderHandleBO.getBo());
    }


    /**
     * ????????????????????????
     *
     * @param shopOrderHandleBO ??????BOHandle
     * @return ShopOrderTwoFullVo
     * @throws CommonException ??????
     */
    @Override
    public ShopOrderTwoFullVo getShopTwoFullOrder(ShopOrderHandleBO shopOrderHandleBO) throws CommonException {

        ShopOrder shopOrder = getExistShopOrder(shopOrderHandleBO);
        if(shopOrder == null){
            return null;
        }
        ShopOrderTwoFullVo shopOrderTwoFullVo = new ShopOrderTwoFullVo();
        BeanUtils.copyProperties(shopOrder, shopOrderTwoFullVo);

        // ??????????????????

        // ????????????
        if(shopOrder.getSchedulQty() != null){
            shopOrderTwoFullVo.setSchedulQty(shopOrder.getSchedulQty().intValue());
        }

        ItemHandleBO itemHandleBO = new ItemHandleBO(shopOrder.getItemBo());
        shopOrderTwoFullVo.setItemCode(itemHandleBO.getItem());
        shopOrderTwoFullVo.setItemVersion(itemHandleBO.getVersion());
        shopOrderTwoFullVo.setState(new StatusHandleBO(shopOrder.getStateBo()).getState());

        //????????????BOM
        if (!StrUtil.isBlank(shopOrder.getOrderBomBo())) {
            BomHandleBO bomHandleBO = new BomHandleBO(shopOrder.getOrderBomBo());
            shopOrderTwoFullVo.setOrderBom(bomHandleBO.getBom());
            shopOrderTwoFullVo.setOrderBomVersion(bomHandleBO.getVersion());
        }

        //??????BOM
        if (!StrUtil.isBlank(shopOrder.getBomBo())) {
            BomHandleBO bomHandleBO = new BomHandleBO(shopOrder.getBomBo());
            shopOrderTwoFullVo.setBom(bomHandleBO.getBom());
            shopOrderTwoFullVo.setBomVersion(bomHandleBO.getVersion());
        }
        //??????????????????
        if (!StrUtil.isBlank(shopOrder.getRouterBo())) {
            RouterHandleBO routerHandleBO = new RouterHandleBO(shopOrder.getRouterBo());
            shopOrderTwoFullVo.setRouter(routerHandleBO.getRouter());
            shopOrderTwoFullVo.setRouterVersion(routerHandleBO.getVersion());
        }
        //????????????
        if (!StrUtil.isBlank(shopOrder.getProductLineBo())) {
            // ProductLineHandleBO productLineHandleBO = new ProductLineHandleBO(shopOrder.getProductLineBo());
            ProductLine productLine = productLineService.getById(shopOrder.getProductLineBo());
            if(productLine != null){
                shopOrderTwoFullVo.setProductionLineCode(productLine.getProductLine()); // ????????????
                shopOrderTwoFullVo.setProductionLineDesc(productLine.getProductLineDesc()); // ????????????
            }
        }
        //????????????
        if (!StrUtil.isBlank(shopOrder.getShiftBo())) {
            // Shift shift = shiftService.getById(shopOrder.getShiftBo());
            ResponseData<ClassFrequencyEntity> classFrequencyEntityResponseData = classFrequencyService.getById(shopOrder.getShiftBo());
            if(classFrequencyEntityResponseData != null){
                ClassFrequencyEntity classFrequencyEntity = classFrequencyEntityResponseData.getData();
                if(classFrequencyEntity != null){
                    shopOrderTwoFullVo.setShiftCode(classFrequencyEntity.getCode()); // ????????????
                    shopOrderTwoFullVo.setShiftName(classFrequencyEntity.getName()); // ????????????
                }

            }
        }

        //?????????????????????
        /*ResponseData<List<ScheduleEntity>> byScheduleShopOrder = ppService.getByScheduleShopOrder(shopOrderTwoFullVo.getBo());
        List<ScheduleEntity> data = byScheduleShopOrder.getData();
        if(CollectionUtil.isNotEmpty(data)){
            shopOrderTwoFullVo.setSchedulQty(data.size());
        }*/

        // ????????????
        String customeBo = shopOrder.getCustomerBo();
        if(customeBo != null && !"".equals(customeBo)){
            Customer customer = customerService.getById(customeBo);
            shopOrderTwoFullVo.setCustomer(customer);
        }

        //??????????????????
        Item exitsItem = itemService.getExitsItemByItemHandleBO(new ItemHandleBO(shopOrder.getItemBo()));
        shopOrderTwoFullVo.setItemBo(shopOrder.getItemBo());
        shopOrderTwoFullVo.setItemName(exitsItem.getItemName());
        shopOrderTwoFullVo.setItemDesc(exitsItem.getItemDesc());
        shopOrderTwoFullVo.setCustomDataAndValVoList(customDataValMapper.selectCustomDataAndValByBoAndDataTypeFast(shopOrderHandleBO.getSite(),
                shopOrderHandleBO.getBo(), CustomDataTypeEnum.SHOP_ORDER.getDataType()));
        return shopOrderTwoFullVo;
    }

    /**
     * ????????????BOM
     * @param shopOrderBo
     * @param orderRouterProcess
     * @return
     * @throws CommonException
     */
    @Override
    public ShopOrderBomComponnetSaveDto getShopOrderBomComponnetSaveDto(String shopOrderBo, OrderRouterProcess orderRouterProcess) throws CommonException {
        /*String shopOrderBo = "SO:1040,11300950";
        String routerBoStr = "ROUTER:1040,1130????????????,1";
        String siteStr = "1040";
        String processInfoStr = "{\"nodeList\":[{\"id\":\"u99uwh2rv8\",\"name\":\"????????????\",\"type\":\"timer\",\"left\":\"-18px\",\"top\":\"88.5px\",\"ico\":\"craftRouteStart\",\"state\":\"success\"},{\"id\":\"j1pc19m767\",\"name\":\"????????????\",\"type\":\"end\",\"left\":\"128px\",\"top\":\"159.5px\",\"ico\":\"craftRouteEnd\",\"state\":\"success\"},{\"id\":\"q3ejsf0wz9\",\"name\":\"??????-??????????????????\",\"operation\":\"OP:1040,??????-??????????????????,1\",\"type\":\"OP:1040,??????-??????????????????,1\",\"left\":\"263px\",\"top\":\"91.5px\",\"ico\":\"craftRouteList\",\"state\":\"success\",\"BOM\":[{\"GROUP_NAME\":\"?????????002\",\"ITEM_GROUP\":\"WLZ002\"}]}],\"lineList\":[{\"from\":\"u99uwh2rv8\",\"to\":\"q3ejsf0wz9\"},{\"from\":\"q3ejsf0wz9\",\"to\":\"j1pc19m767\"}]}";
        */
        String routerBoStr = orderRouterProcess.getProcessInfo();
        String siteStr = orderRouterProcess.getSite();
        String processInfoStr = orderRouterProcess.getProcessInfo();
        orderRouterProcess.setRouterBo(routerBoStr);
        orderRouterProcess.setSite(siteStr);
        orderRouterProcess.setProcessInfo(processInfoStr);

        ShopOrderBomComponnetSaveDto shopOrderBomComponnetSaveDto = new ShopOrderBomComponnetSaveDto();
        shopOrderBomComponnetSaveDto.setType("OP");
        shopOrderBomComponnetSaveDto.setShopOrderBo(shopOrderBo);
        JSONObject processInfoObj = JSONObject.parseObject(processInfoStr);
        List<Object> nodeList = (List<Object>) processInfoObj.get("nodeList");
        // ?????????????????????BOM??????
        List<ShopOrderBomComponnet> shopOrderBomComponnetList = new ArrayList<>();
        for(int i=0; i<nodeList.size(); i++){
            Map<String,Object> nodeListObj = (Map<String, Object>) nodeList.get(i);
            String operation = (String) nodeListObj.get("operation");
            if(operation != null && !"".equals(operation)){
                // ????????????????????????????????????
                List operationBomList = (List) nodeListObj.get("BOM");
                if(operationBomList != null && !"".equals(operationBomList)){
                    for(int j=0;j<operationBomList.size();j++){
                        Map<String,String> operationBom = (Map<String,String>) operationBomList.get(j);
                        String groupName = operationBom.get("GROUP_NAME");
                        String itemGroup = operationBom.get("ITEM_GROUP");
                        // ItemGroupHandleBO itemGroupHandleBO = new ItemGroupHandleBO(UserUtils.getSite(),itemGroup);
                        // ???????????????????????????????????????????????????
                        ItemGroupVo itemGroupVo = itemGroupService.getItemGroupVoByGroup(itemGroup);
                        if(itemGroupVo == null || "".equals(itemGroupVo)){
                            // throw new CommonException("??????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
                        }else{
                            // ????????????????????????????????????????????????????????????
                            List<ItemNameDescVo> itemNameDescVoList = itemGroupVo.getAssignedItemList();
                            if(itemNameDescVoList != null && itemNameDescVoList.size() > 0){
                                // ?????????????????????BOM??????
                                // List<ShopOrderBomComponnet> shopOrderBomComponnetList = new ArrayList<>();
                                for(int k=0;k<itemNameDescVoList.size();k++){
                                    ItemNameDescVo itemNameDescVo = itemNameDescVoList.get(k);
                                    String itemCode = itemNameDescVo.getItem(); // ????????????
                                    String itemName = itemNameDescVo.getItemName(); // ????????????
                                    String itemDesc = itemNameDescVo.getItemDesc(); // ????????????
                                    String itemVersion = itemNameDescVo.getVersion(); // ????????????
                                    ItemHandleBO itemHandleBO = new ItemHandleBO(UserUtils.getSite(),itemCode,itemVersion);
                                    String itemBo = itemHandleBO.getBo();
                                    QueryWrapper<ShopOrderBomComponnet> queryWrapper = new QueryWrapper<ShopOrderBomComponnet>();
                                    queryWrapper.lambda().eq(ShopOrderBomComponnet::getComponentBo, itemBo)
                                            .eq(ShopOrderBomComponnet::getOperationBo, operation)
                                            .eq(ShopOrderBomComponnet::getShopOrderBo, shopOrderBo)
                                            .eq(ShopOrderBomComponnet::getType, "SO");
                                    List<ShopOrderBomComponnet> list = shopOrderBomComponnetMapper.selectList(queryWrapper);
                                /*List<ShopOrderBomComponnet> list = shopOrderBomComponnetService.list(new QueryWrapper<ShopOrderBomComponnet>().lambda()
                                        .eq(ShopOrderBomComponnet::getComponentBo, itemBo)
                                        .eq(ShopOrderBomComponnet::getOperationBo, operation)
                                        .select(ShopOrderBomComponnet::getShopOrderBo));*/
                                    if (CollUtil.isNotEmpty(list)) {
                                        List<String> itemBos = list.stream().map(ShopOrderBomComponnet::getBo).collect(Collectors.toList());
                                        for (ShopOrderBomComponnet bom: list) {
                                            bom.setBo(null);
                                            bom.setType("OP");
                                            shopOrderBomComponnetList.add(bom);
                                        }
                                    }

                                }
                                // shopOrderBomComponnetSaveDto.setShopOrderBomComponnets(shopOrderBomComponnetList);
                            }

                        }
                    }

                }
            }
            // BOM?????????????????????
            shopOrderBomComponnetSaveDto.setShopOrderBomComponnets(shopOrderBomComponnetList);
        }
        return shopOrderBomComponnetSaveDto;

    }

    /**
     * ????????????????????????
     *
     * @param shopOrderTwoSaveVo ??????shopOrderTwoFullVo
     * @throws CommonException ??????
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void saveShopOrder(ShopOrderTwoSaveVo shopOrderTwoSaveVo) throws CommonException {
        /*String bo = shopOrderTwoFullVo.getBo();
        if (StrUtil.isBlank(bo)) {
            throw new CommonException("??????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }*/
        // ????????????
        String shopOrderStr = shopOrderTwoSaveVo.getShopOrder();
        if (StrUtil.isBlank(shopOrderStr)) {
            throw new CommonException("????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        ShopOrderHandleBO shopOrderHandleBO = new ShopOrderHandleBO(UserUtils.getSite(), shopOrderTwoSaveVo.getShopOrder());
        ShopOrder shopOrder = getShopOrder(shopOrderHandleBO);

        // ?????????????????????
        String itemBo = shopOrderTwoSaveVo.getItemBo(); // ??????ID
        String itemCode = shopOrderTwoSaveVo.getItemCode();
        String itemVersion = shopOrderTwoSaveVo.getItemVersion();
        if(itemBo == null){
            if(itemCode != null && !"".equals(itemCode)){
                ItemHandleBO itemHandleBO = new ItemHandleBO(UserUtils.getSite(),shopOrderTwoSaveVo.getItemCode(),shopOrderTwoSaveVo.getItemVersion());
                itemBo = itemHandleBO.getBo();
            }else{
                throw new CommonException("??????Bo???????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
        }

        String orderBo =shopOrderHandleBO.getBo(); // ??????ID
        // ????????????????????????????????????+??????BOM???????????????BOM?????????
        OrderRouterProcess orderRouterProcess = new OrderRouterProcess();
        String site = UserUtils.getSite();
        String userName = UserUtils.getCurrentUser().getUserName();

        if (shopOrder == null) { //??????
            insertShopOrder(shopOrderTwoSaveVo);

            // ????????????????????????
            MeProductGroupInspectionItemsDto meProductGroupInspectionItemsDto = new MeProductGroupInspectionItemsDto();
            ItemHandleBO itemHandleBO = new ItemHandleBO(itemBo);
            List<String> itemGroupStrList = itemGroupService.getAssignedItemGroupListBySiteAndItemBO(itemHandleBO);
            for(int z = 0; itemGroupStrList.size() > z; z++){
                String itemGroupStr = itemGroupStrList.get(z);
                ItemGroupHandleBO itemGroupHandleBO = new ItemGroupHandleBO(UserUtils.getSite(),itemGroupStr);
                meProductGroupInspectionItemsDto.setItemGroupBo(itemGroupHandleBO.getBo()); // ???????????????BO
                List<MeProductGroupInspectionItemsEntity> meProductGroupInspectionItemsEntityList = meProductInspectionItemsOrderService.listGroupItems(meProductGroupInspectionItemsDto);
                if(meProductGroupInspectionItemsEntityList != null && !"".equals(meProductGroupInspectionItemsEntityList)){
                    for(int i=0;i<meProductGroupInspectionItemsEntityList.size();i++){
                        MeProductInspectionItemsOrderEntity meProductInspectionItemsOrderEntity = new MeProductInspectionItemsOrderEntity();
                        MeProductGroupInspectionItemsEntity meProductGroupInspectionItemsEntity = meProductGroupInspectionItemsEntityList.get(i);
                        if (ObjectUtil.isNotEmpty(meProductGroupInspectionItemsEntity)) {
                            BeanUtils.copyProperties(meProductGroupInspectionItemsEntity, meProductInspectionItemsOrderEntity);
                            String operation = meProductInspectionItemsOrderEntity.getOperation();
                            if(operation == null || "".equals(operation)){
                                String operationBo = meProductInspectionItemsOrderEntity.getOperationBo();
                                if(operationBo != null && !"".equals(operationBo)){
                                    OperationHandleBO operationHandleBO = new OperationHandleBO(operationBo);
                                    operation = operationHandleBO.getOperation();
                                    meProductInspectionItemsOrderEntity.setOperation(operation);
                                }
                            }
                            meProductInspectionItemsOrderEntity.setId(null);
                            meProductInspectionItemsOrderEntity.setOrderBo(orderBo);
                            meProductInspectionItemsOrderEntity.setItemType("1"); // ?????????????????????
                            meProductInspectionItemsOrderEntity.setItemGroupBo(itemGroupHandleBO.getBo());
                            meProductInspectionItemsOrderEntity.setCreateDate(new Date());
                            meProductInspectionItemsOrderEntity.setCreateUser(UserUtils.getCurrentUser().getUserName());

                            // ?????????????????????????????????????????????
                            Integer inspectionItemId = meProductGroupInspectionItemsEntity.getId();
                            String itemType = "1";
                            MeProductInspectionItemsNcCodeVo meProductInspectionItemsNcCodeVo = new MeProductInspectionItemsNcCodeVo();
                            meProductInspectionItemsNcCodeVo.setInspectionItemId(inspectionItemId);
                            meProductInspectionItemsNcCodeVo.setItemType(itemType); // 0????????????1????????????
                            // ??????????????????????????????????????????????????????????????????????????????
                            // int deleteInt = meProductInspectionItemsOrderNcCodeService.deleteByInspectionItemIdItemType(orderBo,inspectionItemId,itemType);
                            // ????????????????????????????????????????????????
                            ResponseData<List<MeProductInspectionItemsNcCodeVo>> listResponseData = meProductInspectionItemsNcCodeService.listItemNcCodesTwo(meProductInspectionItemsNcCodeVo);
                            Assert.valid(!listResponseData.isSuccess(), listResponseData.getMsg());
                            List<MeProductInspectionItemsNcCodeVo> meProductInspectionItemsNcCodeVoList = listResponseData.getData();
                            if(meProductInspectionItemsNcCodeVoList != null && !"".equals(meProductInspectionItemsNcCodeVoList)){
                                List<MeProductInspectionItemsOrderNcCode> meProductInspectionItemsOrderNcCodeList = new ArrayList<>();
                                for (MeProductInspectionItemsNcCodeVo itemsNcCodeVo : meProductInspectionItemsNcCodeVoList) {
                                    MeProductInspectionItemsOrderNcCode meProductInspectionItemsOrderNcCode = new MeProductInspectionItemsOrderNcCode();
                                    BeanUtils.copyProperties(itemsNcCodeVo, meProductInspectionItemsOrderNcCode);
                                    meProductInspectionItemsOrderNcCode.setBo(null);
                                    meProductInspectionItemsOrderNcCode.setOrderBo(orderBo);
                                    meProductInspectionItemsOrderNcCode.setSite(site);
                                    meProductInspectionItemsOrderNcCode.setItemType(itemType);
                                    meProductInspectionItemsOrderNcCode.setCreateDate(new Date());
                                    meProductInspectionItemsOrderNcCode.setCreateUser(userName);
                                    // meProductInspectionItemsOrderNcCodeService.save(meProductInspectionItemsOrderNcCode);
                                    meProductInspectionItemsOrderNcCodeList.add(meProductInspectionItemsOrderNcCode);
                                }
                                meProductInspectionItemsOrderEntity.setMeProductInspectionItemsOrderNcCodeList(meProductInspectionItemsOrderNcCodeList);
                            }

                            ResponseData responseData = meProductInspectionItemsOrderService.save(meProductInspectionItemsOrderEntity);
                            Assert.valid(!responseData.isSuccess(), responseData.getMsg());

//                            // ?????????????????????????????????????????????
//                            Integer inspectionItemId = meProductGroupInspectionItemsEntity.getId();
//                            String itemType = "1";
//                            MeProductInspectionItemsNcCodeVo meProductInspectionItemsNcCodeVo = new MeProductInspectionItemsNcCodeVo();
//                            meProductInspectionItemsNcCodeVo.setInspectionItemId(inspectionItemId);
//                            meProductInspectionItemsNcCodeVo.setItemType(itemType); // 0????????????1????????????
//                            // ??????????????????????????????????????????????????????????????????????????????
//                            // int deleteInt = meProductInspectionItemsOrderNcCodeService.deleteByInspectionItemIdItemType(orderBo,inspectionItemId,itemType);
//                            // ????????????????????????????????????????????????
//                            ResponseData<List<MeProductInspectionItemsNcCodeVo>> listResponseData = meProductInspectionItemsNcCodeService.listItemNcCodesTwo(meProductInspectionItemsNcCodeVo);
//                            Assert.valid(!listResponseData.isSuccess(), listResponseData.getMsg());
//                            List<MeProductInspectionItemsNcCodeVo> meProductInspectionItemsNcCodeVoList = listResponseData.getData();
//                            if(meProductInspectionItemsNcCodeVoList != null && !"".equals(meProductInspectionItemsNcCodeVoList)){
//                                MeProductInspectionItemsOrderNcCode meProductInspectionItemsOrderNcCode = new MeProductInspectionItemsOrderNcCode();
//                                for (MeProductInspectionItemsNcCodeVo itemsNcCodeVo : meProductInspectionItemsNcCodeVoList) {
//                                    BeanUtils.copyProperties(itemsNcCodeVo, meProductInspectionItemsOrderNcCode);
//                                    meProductInspectionItemsOrderNcCode.setBo(null);
//                                    meProductInspectionItemsOrderNcCode.setOrderBo(orderBo);
//                                    // ?????????????????????????????????ID?????????????????????
//                                    meProductInspectionItemsOrderNcCode.setInspectionItemId(meProductInspectionItemsOrderEntity.getId());
//                                    meProductInspectionItemsOrderNcCode.setSite(site);
//                                    meProductInspectionItemsOrderNcCode.setCreateDate(new Date());
//                                    meProductInspectionItemsOrderNcCode.setCreateUser(userName);
//                                    meProductInspectionItemsOrderNcCodeService.save(meProductInspectionItemsOrderNcCode);
//                                }
//                            }

                        }
                    }
                }
            }

            // ?????????????????????
            MeProductInspectionItemsDto meProductInspectionItemsDto = new MeProductInspectionItemsDto();
            meProductInspectionItemsDto.setItemBo(itemBo); // ????????????
            List<MeProductInspectionItemsEntity> meProductInspectionItemsEntityList = meProductInspectionItemsOrderService.listItems(meProductInspectionItemsDto);
            if(meProductInspectionItemsEntityList != null && !"".equals(meProductInspectionItemsEntityList)){
                // List<MeProductInspectionItemsOrderEntity> meProductInspectionItemsOrderEntityList = new ArrayList<>();
                for(int j=0;j<meProductInspectionItemsEntityList.size();j++) {
                    MeProductInspectionItemsOrderEntity meProductInspectionItemsOrderEntity = new MeProductInspectionItemsOrderEntity();
                    MeProductInspectionItemsEntity meProductInspectionItemsEntity = meProductInspectionItemsEntityList.get(j);
                    if (ObjectUtil.isNotEmpty(meProductInspectionItemsEntity)) {
                        BeanUtils.copyProperties(meProductInspectionItemsEntity, meProductInspectionItemsOrderEntity);
                        String operation = meProductInspectionItemsOrderEntity.getOperation();
                        if(operation == null || "".equals(operation)){
                            String operationBo = meProductInspectionItemsOrderEntity.getOperationBo();
                            if(operationBo != null && !"".equals(operationBo)){
                                OperationHandleBO operationHandleBO = new OperationHandleBO(operationBo);
                                operation = operationHandleBO.getOperation();
                                meProductInspectionItemsOrderEntity.setOperation(operation);
                            }
                        }
                        meProductInspectionItemsOrderEntity.setId(null);
                        meProductInspectionItemsOrderEntity.setOrderBo(orderBo);
                        meProductInspectionItemsOrderEntity.setItemType("0"); // ?????????????????????
                        meProductInspectionItemsOrderEntity.setItemBo(itemBo);
                        meProductInspectionItemsOrderEntity.setCreateDate(new Date());
                        meProductInspectionItemsOrderEntity.setCreateUser(UserUtils.getCurrentUser().getUserName());

                        // ??????????????????????????????????????????
                        Integer inspectionItemId = meProductInspectionItemsEntity.getId();
                        String itemType = "0";
                        MeProductInspectionItemsNcCodeVo meProductInspectionItemsNcCodeVo = new MeProductInspectionItemsNcCodeVo();
                        meProductInspectionItemsNcCodeVo.setInspectionItemId(inspectionItemId);
                        meProductInspectionItemsNcCodeVo.setItemType(itemType); // 0????????????1????????????
                        // ???????????????????????????????????????????????????????????????????????????
                        // int deleteInt = meProductInspectionItemsOrderNcCodeService.deleteByInspectionItemIdItemType(orderBo,inspectionItemId,itemType);
                        // ?????????????????????????????????????????????
                        ResponseData<List<MeProductInspectionItemsNcCodeVo>> listResponseData = meProductInspectionItemsNcCodeService.listItemNcCodesTwo(meProductInspectionItemsNcCodeVo);
                        Assert.valid(!listResponseData.isSuccess(), listResponseData.getMsg());
                        List<MeProductInspectionItemsNcCodeVo> meProductInspectionItemsNcCodeVoList = listResponseData.getData();
                        if(meProductInspectionItemsNcCodeVoList != null && !"".equals(meProductInspectionItemsNcCodeVoList)){
                            List<MeProductInspectionItemsOrderNcCode> meProductInspectionItemsOrderNcCodeList = new ArrayList<>();
                            for (MeProductInspectionItemsNcCodeVo itemsNcCodeVo : meProductInspectionItemsNcCodeVoList) {
                                MeProductInspectionItemsOrderNcCode meProductInspectionItemsOrderNcCode = new MeProductInspectionItemsOrderNcCode();
                                BeanUtils.copyProperties(itemsNcCodeVo, meProductInspectionItemsOrderNcCode);
                                meProductInspectionItemsOrderNcCode.setBo(null);
                                meProductInspectionItemsOrderNcCode.setOrderBo(orderBo);
                                meProductInspectionItemsOrderNcCode.setSite(site);
                                meProductInspectionItemsOrderNcCode.setItemType(itemType);
                                meProductInspectionItemsOrderNcCode.setCreateDate(new Date());
                                meProductInspectionItemsOrderNcCode.setCreateUser(userName);
                                // meProductInspectionItemsOrderNcCodeService.save(meProductInspectionItemsOrderNcCode);
                                meProductInspectionItemsOrderNcCodeList.add(meProductInspectionItemsOrderNcCode);
                            }
                            meProductInspectionItemsOrderEntity.setMeProductInspectionItemsOrderNcCodeList(meProductInspectionItemsOrderNcCodeList);
                        }

                        ResponseData responseData = meProductInspectionItemsOrderService.save(meProductInspectionItemsOrderEntity);
                        //meProductInspectionItemsOrderEntityList.add(j,meProductInspectionItemsOrderEntity);
                        Assert.valid(!responseData.isSuccess(), responseData.getMsg());

//                        // ??????????????????????????????????????????
//                        Integer inspectionItemId = meProductInspectionItemsEntity.getId();
//                        String itemType = "0";
//                        MeProductInspectionItemsNcCodeVo meProductInspectionItemsNcCodeVo = new MeProductInspectionItemsNcCodeVo();
//                        meProductInspectionItemsNcCodeVo.setInspectionItemId(inspectionItemId);
//                        meProductInspectionItemsNcCodeVo.setItemType(itemType); // 0????????????1????????????
//                        // ???????????????????????????????????????????????????????????????????????????
//                        // int deleteInt = meProductInspectionItemsOrderNcCodeService.deleteByInspectionItemIdItemType(orderBo,inspectionItemId,itemType);
//                        // ?????????????????????????????????????????????
//                        ResponseData<List<MeProductInspectionItemsNcCodeVo>> listResponseData = meProductInspectionItemsNcCodeService.listItemNcCodesTwo(meProductInspectionItemsNcCodeVo);
//                        Assert.valid(!listResponseData.isSuccess(), listResponseData.getMsg());
//                        List<MeProductInspectionItemsNcCodeVo> meProductInspectionItemsNcCodeVoList = listResponseData.getData();
//                        if(meProductInspectionItemsNcCodeVoList != null && !"".equals(meProductInspectionItemsNcCodeVoList)){
//                            MeProductInspectionItemsOrderNcCode meProductInspectionItemsOrderNcCode = new MeProductInspectionItemsOrderNcCode();
//                            for (MeProductInspectionItemsNcCodeVo itemsNcCodeVo : meProductInspectionItemsNcCodeVoList) {
//                                BeanUtils.copyProperties(itemsNcCodeVo, meProductInspectionItemsOrderNcCode);
//                                meProductInspectionItemsOrderNcCode.setBo(null);
//                                meProductInspectionItemsOrderNcCode.setOrderBo(orderBo);
//                                // ?????????????????????????????????ID?????????????????????
//                                meProductInspectionItemsOrderNcCode.setInspectionItemId(meProductInspectionItemsOrderEntity.getId());
//                                meProductInspectionItemsOrderNcCode.setSite(site);
//                                meProductInspectionItemsOrderNcCode.setCreateDate(new Date());
//                                meProductInspectionItemsOrderNcCode.setCreateUser(userName);
//                                meProductInspectionItemsOrderNcCodeService.save(meProductInspectionItemsOrderNcCode);
//                            }
//                        }
                    }
                }
                //meProductInspectionItemsOrderService.saveList(meProductInspectionItemsOrderEntityList);
            }

            /**
             * ????????????????????????
             */
            // String itemCode = shopOrderTwoSaveVo.getItemCode(); // ????????????
            String shopOrderType = shopOrderTwoSaveVo.getShopOrderType(); // ????????????
            String productionLineCode = shopOrderTwoSaveVo.getProductionLineCode(); // ????????????Code

            if(itemCode == null || "".equals(itemCode)){
                throw new CommonException("???????????????itemCode??????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }else if(shopOrderType == null || "".equals(shopOrderType)){
                throw new CommonException("???????????????shopOrderType??????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }else if(productionLineCode == null || "".equals(productionLineCode)){
                throw new CommonException("???????????????productionLineCode??????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }

            // ???????????????????????????????????????????????????????????????????????????????????????
            /*process_route_fit_product ???????????????????????????-?????????
            process_route_fit_product_group ???????????????????????????
            process_route_fit_production_line ???????????????????????????*/

            String routerCode = shopOrderTwoSaveVo.getRouter();
            String routerVersion = shopOrderTwoSaveVo.getRouterVersion();
            // productService; //????????????????????????
            ProcessRouteFitProduct processRouteFitProduct = new ProcessRouteFitProduct();
            processRouteFitProduct.setItemCode(itemCode);
            processRouteFitProduct.setOrderType(shopOrderType);
            List<ProcessRouteFitProduct> processRouteFitProductList = productService.getProductRouteFitProduct(processRouteFitProduct);
            if(processRouteFitProductList != null && processRouteFitProductList.size() == 1){
                processRouteFitProduct = processRouteFitProductList.get(0);
                routerCode = processRouteFitProduct.getRouteCode();
                // routerVersion = processRouteFitProduct.getRouterVersion();
            }else if(processRouteFitProductList == null || processRouteFitProductList.size() < 1){
                // productGroupService; //???????????????????????????
                ProcessRouteFitProductGroup processRouteFitProductGroup = new ProcessRouteFitProductGroup();
                ItemHandleBO itemRouterHandleBO = new ItemHandleBO(itemBo);
                List<String> itemRouterGroupStrList = itemGroupService.getAssignedItemGroupListBySiteAndItemBO(itemRouterHandleBO);
                if(itemRouterGroupStrList != null && itemRouterGroupStrList.size() == 1){
                    String itemRouterGroupStr = itemRouterGroupStrList.get(0);
                    // ItemGroupHandleBO itemRouterGroupHandleBO = new ItemGroupHandleBO(UserUtils.getSite(),itemRouterGroupStr);
                    // processRouteFitProductGroup.setItemGroupBo(itemRouterGroupHandleBO.getBo()); // ???????????????BO
                    processRouteFitProductGroup.setItemGroup(itemRouterGroupStr); // ?????????????????????
                    processRouteFitProductGroup.setOrderType(shopOrderType);
                    List<ProcessRouteFitProductGroup> processRouteFitProductGroupList = productGroupService.getProcessRouteFitProductGroup(processRouteFitProductGroup);
                    if(processRouteFitProductGroupList != null && processRouteFitProductGroupList.size() == 1){
                        processRouteFitProductGroup = processRouteFitProductGroupList.get(0);
                        routerCode = processRouteFitProductGroup.getRouteCode();
                        // routerVersion = processRouteFitProductGroup.getRouterVersion();
                    }else if(processRouteFitProductGroupList == null || processRouteFitProductGroupList.size() < 1){
                        // productionLineService; //????????????????????????
                        ProcessRouteFitProductionLine processRouteFitProductionLine = new ProcessRouteFitProductionLine();
                        processRouteFitProductionLine.setProductLine(productionLineCode);
                        processRouteFitProductionLine.setOrderType(shopOrderType);
                        List<ProcessRouteFitProductionLine> processRouteFitProductionLineList = productionLineService.getProcessRouteFitProductionLine(processRouteFitProductionLine);
                        if(processRouteFitProductionLineList != null && processRouteFitProductionLineList.size() == 1){
                            processRouteFitProductionLine = processRouteFitProductionLineList.get(0);
                            routerCode = processRouteFitProductionLine.getRouteCode();
                            // routerVersion = processRouteFitProductionLine.getRouterVersion();
                        }else if(processRouteFitProductionLineList == null || processRouteFitProductionLineList.size() < 1){
                            throw new CommonException("??????????????????????????????????????????????????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
                        }else{
                            throw new CommonException("?????????????????????????????????????????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
                        }
                    }else{
                        throw new CommonException("????????????????????????????????????????????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
                    }
                }else if(itemRouterGroupStrList == null || itemRouterGroupStrList.size() < 1){
                    // productionLineService; //????????????????????????
                    ProcessRouteFitProductionLine processRouteFitProductionLine = new ProcessRouteFitProductionLine();
                    processRouteFitProductionLine.setProductLine(productionLineCode);
                    processRouteFitProductionLine.setOrderType(shopOrderType);
                    List<ProcessRouteFitProductionLine> processRouteFitProductionLineList = productionLineService.getProcessRouteFitProductionLine(processRouteFitProductionLine);
                    if(processRouteFitProductionLineList != null && processRouteFitProductionLineList.size() == 1){
                        processRouteFitProductionLine = processRouteFitProductionLineList.get(0);
                        routerCode = processRouteFitProductionLine.getRouteCode();
                        // routerVersion = processRouteFitProductionLine.getRouterVersion();
                    }else if(processRouteFitProductionLineList == null || processRouteFitProductionLineList.size() < 1){
                        throw new CommonException("??????????????????????????????????????????????????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
                    }else{
                        throw new CommonException("?????????????????????????????????????????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
                    }
                }else{
                    throw new CommonException("??????????????????????????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }
            }else{
                throw new CommonException("?????????????????????????????????????????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            // ??????????????????????????????
            OrderRouterVo orderRouterVo = new OrderRouterVo();
            Router router = new Router();
            router = routerService.getRouterCode(routerCode);
            /*if(routerVersion == null || "".equals(routerVersion)){
                router = routerService.getRouterCode(routerCode);
            }else{
                RouterHandleBO routerHandleBO = new RouterHandleBO(UserUtils.getSite(),routerCode,routerVersion);
                router = routerService.getRouter(routerHandleBO.getBo());
            }*/
            if(router == null){
                throw new CommonException("?????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            BeanUtils.copyProperties(router, orderRouterVo);
            orderRouterVo.setBo(null);
            orderRouterVo.setShopOrderBo(orderBo);
            orderRouterVo.setProcessInfo(router.getRouterProcess().getProcessInfo());
            orderRouterService.saveOrderRouter(orderRouterVo);

            // ?????????????????????
            RouterProcess routerProcess = router.getRouterProcess();
            routerProcess.getRouterBo();
            String processInfo = routerProcess.getProcessInfo();
            Object processInfoObj = JSONObject.parse(processInfo);

            // ???OrderRouterProcess?????????
            orderRouterProcess.setProcessInfo(processInfo);

        } else { //??????
            if(shopOrder==null){
                throw new CommonException("???????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            String stateBo = shopOrder.getStateBo();
            String[] split = stateBo.split(",");
            //????????????
            String state = split[1];
            if(OrderStateEnum.CLOSED.getCode().equals(state)){
                throw new CommonException("?????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            updateShopOrder(shopOrder, shopOrderTwoSaveVo);
        }

        //????????????BOM
        if (ObjectUtil.isNotEmpty(shopOrderTwoSaveVo.getShopOrderBomComponnetSaveDto())) {
            // shopOrderTwoSaveVo.getShopOrderBomComponnetSaveDto().setShopOrderBo(shopOrderHandleBO.getBo());
            ShopOrderBomComponnetSaveDto shopOrderBomComponnetSaveDto = shopOrderTwoSaveVo.getShopOrderBomComponnetSaveDto();
            shopOrderBomComponnetSaveDto.setShopOrderBo(orderBo);
            shopOrderBomComponnetSaveDto.setType("SO");
            shopOrderTwoSaveVo.setShopOrderBomComponnetSaveDto(shopOrderBomComponnetSaveDto);
            shopOrderBomComponnetService.save(shopOrderTwoSaveVo.getShopOrderBomComponnetSaveDto());
        }

        // ????????????BOM
        OrderRouter orderRouter = orderRouterService.getOrderRouter(orderBo);
        if(orderRouter == null){
            throw new CommonException("??????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        // ???????????????BOM
        OrderRouterProcess orderRouterProcess1 = orderRouter.getOrderRouterProcess();
        ShopOrderBomComponnetSaveDto shopOrderBomComponnetSaveDto = getShopOrderBomComponnetSaveDto(orderBo, orderRouterProcess1);

        // ???????????????BOM
        ShopOrderBomComponnetSaveDto shopOrderBomComponnetSaveDto1 =  shopOrderTwoSaveVo.getProcessBomComponnetSaveDto();
        if(ObjectUtil.isNotEmpty(shopOrderBomComponnetSaveDto1)) {
            shopOrderBomComponnetSaveDto1.setShopOrderBo(orderBo);
            List<ShopOrderBomComponnet> shopOrderBomComponnets = shopOrderBomComponnetSaveDto1.getShopOrderBomComponnets();
            if(shopOrderBomComponnets != null && shopOrderBomComponnets.size() > 0){
                if (ObjectUtil.isNotEmpty(shopOrderBomComponnetSaveDto)){
                    List<ShopOrderBomComponnet> shopOrderBomComponnets2 = shopOrderBomComponnetSaveDto.getShopOrderBomComponnets();
                    if(shopOrderBomComponnets2 != null && shopOrderBomComponnets2.size() > 0){
                        for (ShopOrderBomComponnet shopOrderBomComponnet1 : shopOrderBomComponnets2) {
                            String operationBo = shopOrderBomComponnet1.getOperationBo();
                            String componentBo = shopOrderBomComponnet1.getComponentBo();
                            // ???????????????????????????+????????????????????????
                            shopOrderBomComponnets.add(shopOrderBomComponnet1);
                            long countNum = shopOrderBomComponnets.stream().filter(shopOrderBomComponnet -> operationBo.equals(shopOrderBomComponnet.getOperationBo()) && componentBo.equals(shopOrderBomComponnet.getComponentBo())).map(ShopOrderBomComponnet::getComponentBo).count();
                            if(countNum > 1){
                                shopOrderBomComponnets.remove(shopOrderBomComponnet1);
                            }
                        }
                    }
                    shopOrderBomComponnetSaveDto1.setShopOrderBomComponnets(shopOrderBomComponnets);
                }
                shopOrderBomComponnetService.save(shopOrderBomComponnetSaveDto1);
            }else if (ObjectUtil.isNotEmpty(shopOrderBomComponnetSaveDto)) {
                List<ShopOrderBomComponnet> shopOrderBomComponnets2 = shopOrderBomComponnetSaveDto.getShopOrderBomComponnets();
                if(shopOrderBomComponnets2 != null && shopOrderBomComponnets2.size() > 0){
                    shopOrderBomComponnetService.save(shopOrderBomComponnetSaveDto);
                }
            }
        }else if (ObjectUtil.isNotEmpty(shopOrderBomComponnetSaveDto)) {
            List<ShopOrderBomComponnet> shopOrderBomComponnets = shopOrderBomComponnetSaveDto.getShopOrderBomComponnets();
            if(shopOrderBomComponnets != null && shopOrderBomComponnets.size() > 0){
                shopOrderBomComponnetService.save(shopOrderBomComponnetSaveDto);
            }
        }

        /*if(ObjectUtil.isNotEmpty(shopOrderBomComponnetSaveDto)){
            *//*shopOrderBomComponnetSaveDto.setShopOrderBo(orderBo);
            shopOrderBomComponnetSaveDto.setType("OP");*//*
            List<ShopOrderBomComponnet> shopOrderBomComponnets = shopOrderBomComponnetSaveDto.getShopOrderBomComponnets();
            if(shopOrderBomComponnets != null && shopOrderBomComponnets.size() > 0){
                if (ObjectUtil.isNotEmpty(shopOrderTwoSaveVo.getProcessBomComponnetSaveDto())){
                    List<ShopOrderBomComponnet> shopOrderBomComponnets2 = shopOrderTwoSaveVo.getProcessBomComponnetSaveDto().getShopOrderBomComponnets();
                    for (ShopOrderBomComponnet shopOrderBomComponnet : shopOrderBomComponnets2) {
                        shopOrderBomComponnets.add(shopOrderBomComponnet);
                    }
                    *//*if(shopOrderBomComponnets2 != null && shopOrderBomComponnets2.size() > 0){
                        for(int z=0;z<shopOrderBomComponnets2.size();z++){
                            ShopOrderBomComponnet shopOrderBomComponnet = shopOrderBomComponnets2.get(z);
                            if(shopOrderBomComponnet != null && !"".equals(shopOrderBomComponnet)){
                                shopOrderBomComponnets.add(shopOrderBomComponnet);
                            }
                        }
                    }*//*
                }
                shopOrderBomComponnetService.save(shopOrderBomComponnetSaveDto);
            }else if (ObjectUtil.isNotEmpty(shopOrderTwoSaveVo.getProcessBomComponnetSaveDto())) {
                shopOrderTwoSaveVo.getProcessBomComponnetSaveDto().setShopOrderBo(shopOrderHandleBO.getBo());
                shopOrderBomComponnetService.save(shopOrderTwoSaveVo.getProcessBomComponnetSaveDto());
            }
        }else if (ObjectUtil.isNotEmpty(shopOrderTwoSaveVo.getProcessBomComponnetSaveDto())) {
            shopOrderTwoSaveVo.getProcessBomComponnetSaveDto().setShopOrderBo(shopOrderHandleBO.getBo());
            shopOrderBomComponnetService.save(shopOrderTwoSaveVo.getProcessBomComponnetSaveDto());
        }*/

        //????????????????????????
        if (ObjectUtil.isNotEmpty(shopOrderTwoSaveVo.getShopOrderPackRuleSaveDto())) {
            shopOrderTwoSaveVo.getShopOrderPackRuleSaveDto().setShopOrderBo(shopOrderHandleBO.getBo());
            shopOrderPackRuleService.save(shopOrderTwoSaveVo.getShopOrderPackRuleSaveDto());
        }else{
            // ????????????????????????
            ItemFullVo itemFullVo = itemService.getItemFullVoByItemAndVersion(itemCode, itemVersion);
            if(itemFullVo != null){
                if(itemFullVo.getPackingRuleList() == null || itemFullVo.getPackingRuleList().size() <= 0){
                    throw new CommonException("???????????????????????????!", CommonExceptionDefinition.BASIC_EXCEPTION);
                }
                List<ItemPackRuleDetailDto> itemPackRuleDetailDtoList = itemFullVo.getPackingRuleList();
                if(itemPackRuleDetailDtoList == null || itemPackRuleDetailDtoList.size() <= 0){
                    throw new CommonException("???????????????????????????!!", CommonExceptionDefinition.BASIC_EXCEPTION);
                }
                ShopOrderPackRuleSaveDto shopOrderPackRuleSaveDto = new ShopOrderPackRuleSaveDto();
                shopOrderPackRuleSaveDto.setShopOrderBo(shopOrderHandleBO.getBo()); // ??????BO
                String packRule = itemPackRuleDetailDtoList.get(0).getRuleRuleBo();
                shopOrderPackRuleSaveDto.setPackRuleBo(packRule); // (PACK_RULE_BO)????????????BO
                shopOrderPackRuleSaveDto.setItemBo(itemBo); // ??????BO
                List<ShopOrderPackRuleDetail> shopOrderPackRuleDetailList = new ArrayList<>();
                for(int p=0;p<itemPackRuleDetailDtoList.size();p++){
                    ShopOrderPackRuleDetail shopOrderPackRuleDetail = new ShopOrderPackRuleDetail(); // ????????????????????????
                    ItemPackRuleDetailDto itemPackRuleDetailDto = itemPackRuleDetailDtoList.get(p);
                    if(itemPackRuleDetailDto == null || "".equals(itemPackRuleDetailDto)){
                        throw new CommonException("???????????????????????????????????????!!", CommonExceptionDefinition.BASIC_EXCEPTION);
                    }
                    BeanUtil.copyProperties(itemPackRuleDetailDto,shopOrderPackRuleDetail);
                    /*if(packRule.equals(itemPackRuleDetailDto.getRuleRuleBo())){
                        throw new CommonException("??????????????????bo???????????????????????????!!", CommonExceptionDefinition.BASIC_EXCEPTION);
                    }*/
                    shopOrderPackRuleDetail.setPackRuleBo(itemPackRuleDetailDto.getRuleRuleBo()); // ????????????BO
                    shopOrderPackRuleDetail.setShopOrderBo(shopOrderHandleBO.getBo()); // ??????BO
                    shopOrderPackRuleDetailList.add(shopOrderPackRuleDetail);
                }
                shopOrderPackRuleSaveDto.setShopOrderPackRuleDetails(shopOrderPackRuleDetailList);
                // shopOrderTwoSaveVo.setShopOrderPackRuleSaveDto(shopOrderPackRuleSaveDto);
                shopOrderPackRuleService.save(shopOrderPackRuleSaveDto);
            }else{
                throw new CommonException("?????????????????????!!!", CommonExceptionDefinition.BASIC_EXCEPTION);
            }

            /*ItemFullVo itemFullVo = itemService.getItemFullVoByItemAndVersion(itemCode, itemVersion);
            if(itemFullVo != null){
                //throw new CommonException("?????????????????????!", CommonExceptionDefinition.BASIC_EXCEPTION);
                if(itemFullVo.getPackingRuleList() != null && itemFullVo.getPackingRuleList().size() >= 1){
                    //throw new CommonException("?????????????????????!", CommonExceptionDefinition.BASIC_EXCEPTION);
                    ItemPackRuleDetailDto shopOrderPackRuleDetail = itemFullVo.getPackingRuleList().get(0);
                    ShopOrderPackRuleSaveDto shopOrderPackRuleSaveDto = new ShopOrderPackRuleSaveDto();
                    shopOrderPackRuleSaveDto.setShopOrderBo(shopOrderHandleBO.getBo());
                    shopOrderPackRuleSaveDto.setPackRuleBo(shopOrderPackRuleDetail.getBo());
                    shopOrderPackRuleSaveDto.setItemBo(shopOrderPackRuleDetail.getItemBo());
                    //shopOrderTwoSaveVo.setShopOrderPackRuleSaveDto(shopOrderPackRuleSaveDto);
                    shopOrderPackRuleService.save(shopOrderPackRuleSaveDto);
                }*//*else if(itemFullVo.getPackingRuleList().size() > 1){
                    throw new CommonException("????????????????????????!", CommonExceptionDefinition.BASIC_EXCEPTION);
                }*//*
            }*/

        }

    }

    /**
     * ????????????????????????
     *
     * @param shopOrderTwoAsSaveVo ??????shopOrderTwoFullVo
     * @throws CommonException ??????
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void saveAsShopOrder(ShopOrderTwoAsSaveVo shopOrderTwoAsSaveVo){
        // ??????ID
        String shopOrderBoStr = shopOrderTwoAsSaveVo.getBo();
        if (StrUtil.isBlank(shopOrderBoStr)) {
            throw new CommonException("??????Bo????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        ShopOrderHandleBO shopOrderHandleBO1 = new ShopOrderHandleBO(shopOrderBoStr);
        ShopOrderTwoFullVo shopOrderTwoFullVo = getShopTwoFullOrder(shopOrderHandleBO1);
        if(shopOrderTwoFullVo == null || "".equals(shopOrderTwoFullVo)){
            throw new CommonException("??????ID?????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        // ????????????
        String shopOrderStr = shopOrderTwoFullVo.getShopOrder();
        if (StrUtil.isBlank(shopOrderStr)) {
            throw new CommonException("????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        // ?????????????????????????????????????????????
        String state = shopOrderTwoFullVo.getState();
        //?????? ???????????????
        if (!OrderStateEnum.PAUSE.getCode().equals(state) && !OrderStateEnum.PRO_PAUSE.getCode().equals(state)) {
            throw new CommonException("???????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        // ??????????????????
        ShopOrderTwoSaveVo shopOrderTwoSaveVo = new ShopOrderTwoSaveVo();
        BeanUtils.copyProperties(shopOrderTwoFullVo, shopOrderTwoSaveVo);

        Customer customer = shopOrderTwoFullVo.getCustomer();
        if(customer != null && !"".equals(customer)){
            // ??????BO
            String customerBo = customer.getBo();
            // ????????????
            String customerCode = customer.getCustomer();
            // ????????????
            String customerName = customer.getCustomerName();
            // ??????????????????????????????
            shopOrderTwoSaveVo.setCustomerBo(customerBo);
            shopOrderTwoSaveVo.setCustomerCode(customerCode);
            shopOrderTwoSaveVo.setCustomerName(customerName);
        }

        // ????????????
        String productionLineBo = shopOrderTwoAsSaveVo.getProductionLineBo();
        String productionLineCode = shopOrderTwoAsSaveVo.getProductionLineCode();
        String productionLineDesc = shopOrderTwoAsSaveVo.getProductionLineDesc();
        String shiftBo = shopOrderTwoAsSaveVo.getShiftBo();
        String shiftCode = shopOrderTwoAsSaveVo.getShiftCode();
        if(shopOrderTwoFullVo.getSchedulQty() <= 0){
            throw new CommonException("???????????????0?????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        shopOrderTwoSaveVo.setSchedulQty(new BigDecimal(shopOrderTwoFullVo.getSchedulQty()));

        // ????????????
        if(productionLineBo == null || "".equals(productionLineBo)) {
            throw new CommonException("??????BO???????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }else if(productionLineCode == null || "".equals(productionLineCode)) {
            throw new CommonException("??????CODE???????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }else if(shiftBo == null || "".equals(shiftBo)) {
            throw new CommonException("??????BO???????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }else if(shiftCode == null || "".equals(shiftCode)) {
            throw new CommonException("??????CODE???????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        // ????????????????????????
        shopOrderTwoSaveVo.setProductionLineBo(productionLineBo);
        shopOrderTwoSaveVo.setProductionLineCode(productionLineCode);
        shopOrderTwoSaveVo.setProductionLineDesc(productionLineDesc);
        shopOrderTwoSaveVo.setShiftBo(shiftBo);
        shopOrderTwoSaveVo.setShiftCode(shiftCode);

        // ????????????(???????????????????????????????????????)
        String shopOrderType = shopOrderTwoAsSaveVo.getShopOrderType();
        if(shopOrderType != null && !"".equals(shopOrderType)){
            shopOrderTwoSaveVo.setShopOrderType(shopOrderType);
        }


        /**
         * ??????????????????
         */
        // ???????????????0???("%02d",9)
        //int length = targetString.length();
        //String codeFormat = "%0"+String.valueOf(length)+"d";//%03d
        //String str = String.format(codeFormat,targetIntNum);
        String shopOrderNewStr = "";
        Boolean targetIntNumBl = true;
        String codeFormat = "%02d";
        for(int targetIntNum = 1;targetIntNumBl;targetIntNum++){
            // ???????????????
            String str = String.format(codeFormat,targetIntNum);
            shopOrderNewStr = shopOrderStr+"-"+str;
            ShopOrderHandleBO shopOrderHandleBO2 = new ShopOrderHandleBO(UserUtils.getSite(),shopOrderNewStr);
            ShopOrder shopOrder = getShopOrder(shopOrderHandleBO2);
            if(shopOrder == null){
                shopOrderTwoSaveVo.setShopOrder(shopOrderNewStr);
                targetIntNumBl = false;
            }
        }


        // ????????????????????????
        shopOrderTwoSaveVo.setState(OrderStateEnum.ORDER.getCode());

        // ?????????
        ShopOrderHandleBO shopOrderHandleBO = new ShopOrderHandleBO(UserUtils.getSite(), shopOrderTwoSaveVo.getShopOrder());
        ShopOrder shopOrder = getShopOrder(shopOrderHandleBO);

        // ?????????????????????
        //ShopOrder shopOrderUpdateQty = getShopOrder(shopOrderHandleBO1);
        //shopOrderUpdateQty.setBo(shopOrderBoStr);
        // ??????????????????SN??????
        int schedulQtyOld = 0;
        ResponseData<List<String>> responseDataSnBoOnLineList = snService.queryOrderBoList(shopOrderBoStr,1);
        Assert.valid(!responseDataSnBoOnLineList.isSuccess(), responseDataSnBoOnLineList.getMsg());
        List<String> snBoOnLineList = responseDataSnBoOnLineList.getData();
        if(snBoOnLineList != null && !"".equals(snBoOnLineList)){
            schedulQtyOld = snBoOnLineList.size();
        }
        // int schedulQtyOldSum = -(shopOrderTwoSaveVo.getSchedulQty().intValue() - schedulQtyOld);
        // shopOrderMapper.updateShopOrderScheduleQtyByBO(shopOrderBoStr, new BigDecimal(schedulQtyOldSum));
        // updateShopOrderScheduleQtyByBO(shopOrderBoStr, new BigDecimal(schedulQtyOldSum));

        // ?????????????????????
        int schedulQtyNew = shopOrderTwoSaveVo.getSchedulQty().intValue() - schedulQtyOld;
        //shopOrderTwoSaveVo.setSchedulQty(BigDecimal.valueOf(schedulQtyNew));
        shopOrderTwoSaveVo.setSchedulQty(new BigDecimal(schedulQtyNew));

        // ????????????????????????
        shopOrderTwoSaveVo.setLabelQty(new BigDecimal(0));
        // ????????????????????????SN???????????????(0???????????????1????????????)
        ResponseData<List<String>> responseDataSnBoList = snService.queryOrderBoList(shopOrderBoStr,0);
        Assert.valid(!responseDataSnBoList.isSuccess(), responseDataSnBoList.getMsg());
        List<String> snBoList = responseDataSnBoList.getData();
        // ????????????SN??????(????????????)
        if(snBoList != null && snBoList.size() > 0){
            shopOrderTwoSaveVo.setLabelQty(new BigDecimal(snBoList.size()));
        }

        // ?????????????????????
        String itemBo = shopOrderTwoSaveVo.getItemBo(); // ??????ID
        String itemCode = shopOrderTwoSaveVo.getItemCode();
        String itemVersion = shopOrderTwoSaveVo.getItemVersion();
        if(itemBo == null){
            if(itemCode != null && !"".equals(itemCode)){
                ItemHandleBO itemHandleBO = new ItemHandleBO(UserUtils.getSite(),shopOrderTwoSaveVo.getItemCode(),shopOrderTwoSaveVo.getItemVersion());
                itemBo = itemHandleBO.getBo();
            }else{
                throw new CommonException("??????Bo???????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
        }


        if (shopOrder == null) { //??????
            insertShopOrder(shopOrderTwoSaveVo);
        }else{
            throw new CommonException("????????????"+shopOrderTwoSaveVo.getShopOrder()+"????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        // ?????????????????????
        // String itemBo = shopOrderTwoSaveVo.getItemBo();
        String orderBo =shopOrderHandleBO.getBo();
        String site = UserUtils.getSite();
        String userName = UserUtils.getCurrentUser().getUserName();

        // ????????????????????????
        MeProductGroupInspectionItemsDto meProductGroupInspectionItemsDto = new MeProductGroupInspectionItemsDto();
        ItemHandleBO itemHandleBO = new ItemHandleBO(itemBo);
        List<String> itemGroupStrList = itemGroupService.getAssignedItemGroupListBySiteAndItemBO(itemHandleBO);
        if(itemGroupStrList != null || "".equals(itemGroupStrList)){
            for(int z = 0; itemGroupStrList.size() > z; z++){
                String itemGroupStr = itemGroupStrList.get(z);
                ItemGroupHandleBO itemGroupHandleBO = new ItemGroupHandleBO(UserUtils.getSite(),itemGroupStr);
                meProductGroupInspectionItemsDto.setItemGroupBo(itemGroupHandleBO.getBo()); // ???????????????BO
                List<MeProductGroupInspectionItemsEntity> meProductGroupInspectionItemsEntityList = meProductInspectionItemsOrderService.listGroupItems(meProductGroupInspectionItemsDto);
                if(meProductGroupInspectionItemsEntityList != null && !"".equals(meProductGroupInspectionItemsEntityList)){
                    for(int i=0;i<meProductGroupInspectionItemsEntityList.size();i++){
                        MeProductInspectionItemsOrderEntity meProductInspectionItemsOrderEntity = new MeProductInspectionItemsOrderEntity();
                        MeProductGroupInspectionItemsEntity meProductGroupInspectionItemsEntity = meProductGroupInspectionItemsEntityList.get(i);
                        if (ObjectUtil.isNotEmpty(meProductGroupInspectionItemsEntity)) {
                            BeanUtils.copyProperties(meProductGroupInspectionItemsEntity, meProductInspectionItemsOrderEntity);
                            String operation = meProductInspectionItemsOrderEntity.getOperation();
                            if(operation == null || "".equals(operation)){
                                String operationBo = meProductInspectionItemsOrderEntity.getOperationBo();
                                if(operationBo != null && !"".equals(operationBo)){
                                    OperationHandleBO operationHandleBO = new OperationHandleBO(operationBo);
                                    operation = operationHandleBO.getOperation();
                                    meProductInspectionItemsOrderEntity.setOperation(operation);
                                }
                            }
                            meProductInspectionItemsOrderEntity.setId(null);
                            meProductInspectionItemsOrderEntity.setOrderBo(orderBo);
                            meProductInspectionItemsOrderEntity.setItemType("1"); // ?????????????????????
                            meProductInspectionItemsOrderEntity.setItemGroupBo(itemGroupHandleBO.getBo());
                            meProductInspectionItemsOrderEntity.setCreateDate(new Date());
                            meProductInspectionItemsOrderEntity.setCreateUser(UserUtils.getCurrentUser().getUserName());

                            // ?????????????????????????????????????????????
                            Integer inspectionItemId = meProductGroupInspectionItemsEntity.getId();
                            String itemType = "1";
                            MeProductInspectionItemsNcCodeVo meProductInspectionItemsNcCodeVo = new MeProductInspectionItemsNcCodeVo();
                            meProductInspectionItemsNcCodeVo.setInspectionItemId(inspectionItemId);
                            meProductInspectionItemsNcCodeVo.setItemType(itemType); // 0????????????1????????????
                            // ??????????????????????????????????????????????????????????????????????????????
                            // int deleteInt = meProductInspectionItemsOrderNcCodeService.deleteByInspectionItemIdItemType(orderBo,inspectionItemId,itemType);
                            // ????????????????????????????????????????????????
                            ResponseData<List<MeProductInspectionItemsNcCodeVo>> listResponseData = meProductInspectionItemsNcCodeService.listItemNcCodesTwo(meProductInspectionItemsNcCodeVo);
                            Assert.valid(!listResponseData.isSuccess(), listResponseData.getMsg());
                            List<MeProductInspectionItemsNcCodeVo> meProductInspectionItemsNcCodeVoList = listResponseData.getData();
                            if(meProductInspectionItemsNcCodeVoList != null && !"".equals(meProductInspectionItemsNcCodeVoList)){
                                List<MeProductInspectionItemsOrderNcCode> meProductInspectionItemsOrderNcCodeList = new ArrayList<>();
                                for (MeProductInspectionItemsNcCodeVo itemsNcCodeVo : meProductInspectionItemsNcCodeVoList) {
                                    MeProductInspectionItemsOrderNcCode meProductInspectionItemsOrderNcCode = new MeProductInspectionItemsOrderNcCode();
                                    BeanUtils.copyProperties(itemsNcCodeVo, meProductInspectionItemsOrderNcCode);
                                    meProductInspectionItemsOrderNcCode.setBo(null);
                                    meProductInspectionItemsOrderNcCode.setOrderBo(orderBo);
                                    meProductInspectionItemsOrderNcCode.setSite(site);
                                    meProductInspectionItemsOrderNcCode.setItemType(itemType);
                                    meProductInspectionItemsOrderNcCode.setCreateDate(new Date());
                                    meProductInspectionItemsOrderNcCode.setCreateUser(userName);
                                    // meProductInspectionItemsOrderNcCodeService.save(meProductInspectionItemsOrderNcCode);
                                    meProductInspectionItemsOrderNcCodeList.add(meProductInspectionItemsOrderNcCode);
                                }
                                meProductInspectionItemsOrderEntity.setMeProductInspectionItemsOrderNcCodeList(meProductInspectionItemsOrderNcCodeList);
                            }

                            ResponseData responseData = meProductInspectionItemsOrderService.save(meProductInspectionItemsOrderEntity);
                            Assert.valid(!responseData.isSuccess(), responseData.getMsg());

//                            // ??????????????????????????????????????????
//                            Integer inspectionItemId = meProductGroupInspectionItemsEntity.getId();
//                            MeProductInspectionItemsNcCodeVo meProductInspectionItemsNcCodeVo = new MeProductInspectionItemsNcCodeVo();
//                            meProductInspectionItemsNcCodeVo.setInspectionItemId(inspectionItemId);
//                            meProductInspectionItemsNcCodeVo.setItemType("1"); // 0????????????1????????????
//                            ResponseData<List<MeProductInspectionItemsNcCodeVo>> listResponseData = meProductInspectionItemsNcCodeService.listItemNcCodesTwo(meProductInspectionItemsNcCodeVo);
//                            Assert.valid(!listResponseData.isSuccess(), listResponseData.getMsg());
//                            List<MeProductInspectionItemsNcCodeVo> meProductInspectionItemsNcCodeVoList = listResponseData.getData();
//                            if(meProductInspectionItemsNcCodeVoList != null && !"".equals(meProductInspectionItemsNcCodeVoList)){
//                                MeProductInspectionItemsOrderNcCode meProductInspectionItemsOrderNcCode = new MeProductInspectionItemsOrderNcCode();
//                                for (MeProductInspectionItemsNcCodeVo itemsNcCodeVo : meProductInspectionItemsNcCodeVoList) {
//                                    BeanUtils.copyProperties(itemsNcCodeVo, meProductInspectionItemsOrderNcCode);
//                                    meProductInspectionItemsOrderNcCode.setBo(null);
//                                    meProductInspectionItemsOrderNcCode.setOrderBo(orderBo);
//                                    meProductInspectionItemsOrderNcCode.setSite(site);
//                                    meProductInspectionItemsOrderNcCode.setCreateDate(new Date());
//                                    meProductInspectionItemsOrderNcCode.setCreateUser(userName);
//                                    meProductInspectionItemsOrderNcCodeService.save(meProductInspectionItemsOrderNcCode);
//                                }
//                            }
                        }
                    }
                }
            }
        }

        // ?????????????????????
        MeProductInspectionItemsDto meProductInspectionItemsDto = new MeProductInspectionItemsDto();
        meProductInspectionItemsDto.setItemBo(itemBo); // ????????????
        List<MeProductInspectionItemsEntity> meProductInspectionItemsEntityList = meProductInspectionItemsOrderService.listItems(meProductInspectionItemsDto);
        if(meProductInspectionItemsEntityList != null && !"".equals(meProductInspectionItemsEntityList)){
            // List<MeProductInspectionItemsOrderEntity> meProductInspectionItemsOrderEntityList = new ArrayList<>();
            for(int j=0;j<meProductInspectionItemsEntityList.size();j++) {
                MeProductInspectionItemsOrderEntity meProductInspectionItemsOrderEntity = new MeProductInspectionItemsOrderEntity();
                MeProductInspectionItemsEntity meProductInspectionItemsEntity = meProductInspectionItemsEntityList.get(j);
                if (ObjectUtil.isNotEmpty(meProductInspectionItemsEntity)) {
                    BeanUtils.copyProperties(meProductInspectionItemsEntity, meProductInspectionItemsOrderEntity);
                    String operation = meProductInspectionItemsOrderEntity.getOperation();
                    if(operation == null || "".equals(operation)){
                        String operationBo = meProductInspectionItemsOrderEntity.getOperationBo();
                        if(operationBo != null && !"".equals(operationBo)){
                            OperationHandleBO operationHandleBO = new OperationHandleBO(operationBo);
                            operation = operationHandleBO.getOperation();
                            meProductInspectionItemsOrderEntity.setOperation(operation);
                        }
                    }
                    meProductInspectionItemsOrderEntity.setId(null);
                    meProductInspectionItemsOrderEntity.setOrderBo(orderBo);
                    meProductInspectionItemsOrderEntity.setItemType("0"); // ?????????????????????
                    meProductInspectionItemsOrderEntity.setItemBo(itemBo);
                    meProductInspectionItemsOrderEntity.setCreateDate(new Date());
                    meProductInspectionItemsOrderEntity.setCreateUser(UserUtils.getCurrentUser().getUserName());

                    // ??????????????????????????????????????????
                    Integer inspectionItemId = meProductInspectionItemsEntity.getId();
                    String itemType = "0";
                    MeProductInspectionItemsNcCodeVo meProductInspectionItemsNcCodeVo = new MeProductInspectionItemsNcCodeVo();
                    meProductInspectionItemsNcCodeVo.setInspectionItemId(inspectionItemId);
                    meProductInspectionItemsNcCodeVo.setItemType(itemType); // 0????????????1????????????
                    // ???????????????????????????????????????????????????????????????????????????
                    // int deleteInt = meProductInspectionItemsOrderNcCodeService.deleteByInspectionItemIdItemType(orderBo,inspectionItemId,itemType);
                    // ?????????????????????????????????????????????
                    ResponseData<List<MeProductInspectionItemsNcCodeVo>> listResponseData = meProductInspectionItemsNcCodeService.listItemNcCodesTwo(meProductInspectionItemsNcCodeVo);
                    Assert.valid(!listResponseData.isSuccess(), listResponseData.getMsg());
                    List<MeProductInspectionItemsNcCodeVo> meProductInspectionItemsNcCodeVoList = listResponseData.getData();
                    if(meProductInspectionItemsNcCodeVoList != null && !"".equals(meProductInspectionItemsNcCodeVoList)){
                        List<MeProductInspectionItemsOrderNcCode> meProductInspectionItemsOrderNcCodeList = new ArrayList<>();
                        for (MeProductInspectionItemsNcCodeVo itemsNcCodeVo : meProductInspectionItemsNcCodeVoList) {
                            MeProductInspectionItemsOrderNcCode meProductInspectionItemsOrderNcCode = new MeProductInspectionItemsOrderNcCode();
                            BeanUtils.copyProperties(itemsNcCodeVo, meProductInspectionItemsOrderNcCode);
                            meProductInspectionItemsOrderNcCode.setBo(null);
                            meProductInspectionItemsOrderNcCode.setOrderBo(orderBo);
                            meProductInspectionItemsOrderNcCode.setSite(site);
                            meProductInspectionItemsOrderNcCode.setItemType(itemType);
                            meProductInspectionItemsOrderNcCode.setCreateDate(new Date());
                            meProductInspectionItemsOrderNcCode.setCreateUser(userName);
                            // meProductInspectionItemsOrderNcCodeService.save(meProductInspectionItemsOrderNcCode);
                            meProductInspectionItemsOrderNcCodeList.add(meProductInspectionItemsOrderNcCode);
                        }
                        meProductInspectionItemsOrderEntity.setMeProductInspectionItemsOrderNcCodeList(meProductInspectionItemsOrderNcCodeList);
                    }

                    ResponseData responseData = meProductInspectionItemsOrderService.save(meProductInspectionItemsOrderEntity);
                    Assert.valid(!responseData.isSuccess(), responseData.getMsg());
                    //meProductInspectionItemsOrderEntityList.add(j,meProductInspectionItemsOrderEntity);

//                    // ??????????????????????????????????????????
//                    Integer inspectionItemId = meProductInspectionItemsEntity.getId();
//                    MeProductInspectionItemsNcCodeVo meProductInspectionItemsNcCodeVo = new MeProductInspectionItemsNcCodeVo();
//                    meProductInspectionItemsNcCodeVo.setInspectionItemId(inspectionItemId);
//                    meProductInspectionItemsNcCodeVo.setItemType("0"); // 0????????????1????????????
//                    ResponseData<List<MeProductInspectionItemsNcCodeVo>> listResponseData = meProductInspectionItemsNcCodeService.listItemNcCodesTwo(meProductInspectionItemsNcCodeVo);
//                    Assert.valid(!listResponseData.isSuccess(), listResponseData.getMsg());
//                    List<MeProductInspectionItemsNcCodeVo> meProductInspectionItemsNcCodeVoList = listResponseData.getData();
//                    if(meProductInspectionItemsNcCodeVoList != null && !"".equals(meProductInspectionItemsNcCodeVoList)){
//                        MeProductInspectionItemsOrderNcCode meProductInspectionItemsOrderNcCode = new MeProductInspectionItemsOrderNcCode();
//                        for (MeProductInspectionItemsNcCodeVo itemsNcCodeVo : meProductInspectionItemsNcCodeVoList) {
//                            BeanUtils.copyProperties(itemsNcCodeVo, meProductInspectionItemsOrderNcCode);
//                            meProductInspectionItemsOrderNcCode.setBo(null);
//                            meProductInspectionItemsOrderNcCode.setOrderBo(orderBo);
//                            meProductInspectionItemsOrderNcCode.setSite(site);
//                            meProductInspectionItemsOrderNcCode.setCreateDate(new Date());
//                            meProductInspectionItemsOrderNcCode.setCreateUser(userName);
//                            meProductInspectionItemsOrderNcCodeService.save(meProductInspectionItemsOrderNcCode);
//                        }
//                    }
                }
            }
            //meProductInspectionItemsOrderService.saveList(meProductInspectionItemsOrderEntityList);
        }

        // ????????????????????????
//        /*RouterHandleBO routerHandleBO = new RouterHandleBO(shopOrder.getRouterBo());
//        shopOrderTwoSaveVo.getRouter();
//        shopOrderTwoSaveVo.getRouterVersion();*/
//        String routerCode = shopOrderTwoSaveVo.getRouter();
//        String routerVersion = shopOrderTwoSaveVo.getRouterVersion();
//        OrderRouterVo orderRouterVo = new OrderRouterVo();
//        OrderRouter orderRouter = orderRouterService.getOrderRouter(shopOrderBoStr);
//        if(orderRouter != null && !"".equals(orderRouter)){
//            BeanUtils.copyProperties(orderRouter, orderRouterVo);
//            /*String routerCode = shopOrderTwoSaveVo.getRouter();
//            RouterHandleBO routerHandleBO = new RouterHandleBO(UserUtils.getSite(),routerCode,shopOrderTwoSaveVo.getRouterVersion());
//            Router router = routerService.getRouter(routerHandleBO.getBo());
//            BeanUtils.copyProperties(router, orderRouterVo);*/
//            orderRouterVo.setBo(null);
//            orderRouterVo.setShopOrderBo(orderBo);
//            orderRouterVo.setProcessInfo(orderRouter.getOrderRouterProcess() == null ? "" : orderRouter.getOrderRouterProcess().toString());
//            orderRouterService.saveOrderRouter(orderRouterVo);
//            /*try {
//                orderRouterService.saveOrderRouter(orderRouterVo);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }*/
//        }else if(!"".equals(routerCode) && !"".equals(routerVersion)) {
//            RouterHandleBO routerHandleBO = new RouterHandleBO(UserUtils.getSite(),routerCode,routerVersion);
//            Router router = routerService.getRouter(routerHandleBO.getBo());
//            BeanUtils.copyProperties(router, orderRouterVo);
//            orderRouterVo.setBo(null);
//            orderRouterVo.setShopOrderBo(orderBo);
//            orderRouterVo.setProcessInfo(router.getRouterProcess().getProcessInfo());
//            orderRouterService.saveOrderRouter(orderRouterVo);
//            /*try {
//                orderRouterService.saveOrderRouter(orderRouterVo);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }*/
//        }

        /**
         * ????????????????????????
         */
        // String itemCode = shopOrderTwoSaveVo.getItemCode(); // ????????????
        // String shopOrderType = shopOrderTwoSaveVo.getShopOrderType(); // ????????????
        // String productionLineCode = shopOrderTwoSaveVo.getProductionLineCode(); // ????????????Code

        if(itemCode == null || "".equals(itemCode)){
            throw new CommonException("???????????????itemCode??????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }else if(shopOrderType == null || "".equals(shopOrderType)){
            throw new CommonException("???????????????shopOrderType??????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }else if(productionLineCode == null || "".equals(productionLineCode)){
            throw new CommonException("???????????????productionLineCode??????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        // ???????????????????????????????????????????????????????????????????????????????????????
            /*process_route_fit_product ???????????????????????????-?????????
            process_route_fit_product_group ???????????????????????????
            process_route_fit_production_line ???????????????????????????*/

        String routerCode = shopOrderTwoSaveVo.getRouter();
        String routerVersion = shopOrderTwoSaveVo.getRouterVersion();
        // productService; //????????????????????????
        ProcessRouteFitProduct processRouteFitProduct = new ProcessRouteFitProduct();
        processRouteFitProduct.setItemCode(itemCode);
        processRouteFitProduct.setOrderType(shopOrderType);
        List<ProcessRouteFitProduct> processRouteFitProductList = productService.getProductRouteFitProduct(processRouteFitProduct);
        if(processRouteFitProductList != null && processRouteFitProductList.size() == 1){
            processRouteFitProduct = processRouteFitProductList.get(0);
            routerCode = processRouteFitProduct.getRouteCode();
            // routerVersion = processRouteFitProduct.getRouterVersion();
        }else if(processRouteFitProductList == null || processRouteFitProductList.size() < 1){
            // productGroupService; //???????????????????????????
            ProcessRouteFitProductGroup processRouteFitProductGroup = new ProcessRouteFitProductGroup();
            ItemHandleBO itemRouterHandleBO = new ItemHandleBO(itemBo);
            List<String> itemRouterGroupStrList = itemGroupService.getAssignedItemGroupListBySiteAndItemBO(itemRouterHandleBO);
            if(itemRouterGroupStrList != null && itemRouterGroupStrList.size() == 1){
                String itemRouterGroupStr = itemRouterGroupStrList.get(0);
                // ItemGroupHandleBO itemRouterGroupHandleBO = new ItemGroupHandleBO(UserUtils.getSite(),itemRouterGroupStr);
                // processRouteFitProductGroup.setItemGroupBo(itemRouterGroupHandleBO.getBo()); // ???????????????BO
                processRouteFitProductGroup.setItemGroup(itemRouterGroupStr); // ?????????????????????
                processRouteFitProductGroup.setOrderType(shopOrderType);
                List<ProcessRouteFitProductGroup> processRouteFitProductGroupList = productGroupService.getProcessRouteFitProductGroup(processRouteFitProductGroup);
                if(processRouteFitProductGroupList != null && processRouteFitProductGroupList.size() == 1){
                    processRouteFitProductGroup = processRouteFitProductGroupList.get(0);
                    routerCode = processRouteFitProductGroup.getRouteCode();
                    // routerVersion = processRouteFitProductGroup.getRouterVersion();
                }else if(processRouteFitProductGroupList == null || processRouteFitProductGroupList.size() < 1){
                    // productionLineService; //????????????????????????
                    ProcessRouteFitProductionLine processRouteFitProductionLine = new ProcessRouteFitProductionLine();
                    processRouteFitProductionLine.setProductLine(productionLineCode);
                    processRouteFitProductionLine.setOrderType(shopOrderType);
                    List<ProcessRouteFitProductionLine> processRouteFitProductionLineList = productionLineService.getProcessRouteFitProductionLine(processRouteFitProductionLine);
                    if(processRouteFitProductionLineList != null && processRouteFitProductionLineList.size() == 1){
                        processRouteFitProductionLine = processRouteFitProductionLineList.get(0);
                        routerCode = processRouteFitProductionLine.getRouteCode();
                        // routerVersion = processRouteFitProductionLine.getRouterVersion();
                    }else if(processRouteFitProductionLineList == null || processRouteFitProductionLineList.size() < 1){
                        throw new CommonException("??????????????????????????????????????????????????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
                    }else{
                        throw new CommonException("?????????????????????????????????????????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
                    }
                }else{
                    throw new CommonException("????????????????????????????????????????????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }
            }else if(itemRouterGroupStrList == null || itemRouterGroupStrList.size() < 1){
                // productionLineService; //????????????????????????
                ProcessRouteFitProductionLine processRouteFitProductionLine = new ProcessRouteFitProductionLine();
                processRouteFitProductionLine.setProductLine(productionLineCode);
                processRouteFitProductionLine.setOrderType(shopOrderType);
                List<ProcessRouteFitProductionLine> processRouteFitProductionLineList = productionLineService.getProcessRouteFitProductionLine(processRouteFitProductionLine);
                if(processRouteFitProductionLineList != null && processRouteFitProductionLineList.size() == 1){
                    processRouteFitProductionLine = processRouteFitProductionLineList.get(0);
                    routerCode = processRouteFitProductionLine.getRouteCode();
                    // routerVersion = processRouteFitProductionLine.getRouterVersion();
                }else if(processRouteFitProductionLineList == null || processRouteFitProductionLineList.size() < 1){
                    throw new CommonException("??????????????????????????????????????????????????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }else{
                    throw new CommonException("?????????????????????????????????????????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }
            }else{
                throw new CommonException("??????????????????????????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
        }else{
            throw new CommonException("?????????????????????????????????????????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        // ??????????????????????????????
        OrderRouterVo orderRouterVo = new OrderRouterVo();
        Router router = new Router();
        router = routerService.getRouterCode(routerCode);
            /*if(routerVersion == null || "".equals(routerVersion)){
                router = routerService.getRouterCode(routerCode);
            }else{
                RouterHandleBO routerHandleBO = new RouterHandleBO(UserUtils.getSite(),routerCode,routerVersion);
                router = routerService.getRouter(routerHandleBO.getBo());
            }*/
        if(router == null){
            throw new CommonException("?????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        BeanUtils.copyProperties(router, orderRouterVo);
        orderRouterVo.setBo(null);
        orderRouterVo.setShopOrderBo(orderBo);
        orderRouterVo.setProcessInfo(router.getRouterProcess().getProcessInfo());
        // ????????????????????????????????????
//        if(shopOrderTwoSaveVo.getRouter() == routerCode || routerCode.equals(shopOrderTwoSaveVo.getRouter())){
//            OrderRouter queryRouter = orderRouterService.getOrderRouter(shopOrderBoStr);
//            OrderRouterProcess orderRouterProcess = queryRouter.getOrderRouterProcess();
//            if(orderRouterProcess != null && !"".equals(orderRouterProcess)){
//                orderRouterVo.setProcessInfo(orderRouterProcess.getProcessInfo());
//            }
//        }
        orderRouterService.saveOrderRouter(orderRouterVo);


        //????????????BOM??????????????????????????????
        List<ShopOrderBomComponnet> shopOrderBomComponnetList = new ArrayList<>();
        QueryWrapper<ShopOrderBomComponnet> queryWrapper = new QueryWrapper<ShopOrderBomComponnet>();
        queryWrapper.lambda().eq(ShopOrderBomComponnet::getShopOrderBo, shopOrderBoStr)
                .eq(ShopOrderBomComponnet::getType, "SO");
        List<ShopOrderBomComponnet> list = shopOrderBomComponnetMapper.selectList(queryWrapper);
        if (CollUtil.isNotEmpty(list)) {
            //List<String> itemBos = list.stream().map(ShopOrderBomComponnet::getBo).collect(Collectors.toList());
            for (ShopOrderBomComponnet bom: list) {
                bom.setBo(null);
                bom.setType("SO");
                bom.setShopOrderBo(orderBo); // ?????????BO
                shopOrderBomComponnetList.add(bom);
            }
            ShopOrderBomComponnetSaveDto shopOrderBomComponnetSaveDto = new ShopOrderBomComponnetSaveDto();
            shopOrderBomComponnetSaveDto.setShopOrderBo(orderBo); // ?????????BO
            shopOrderBomComponnetSaveDto.setType("SO");
            shopOrderBomComponnetSaveDto.setShopOrderBomComponnets(shopOrderBomComponnetList);
            shopOrderTwoSaveVo.setShopOrderBomComponnetSaveDto(shopOrderBomComponnetSaveDto);
            shopOrderBomComponnetService.save(shopOrderBomComponnetSaveDto);
        }
        /*if (ObjectUtil.isNotEmpty(shopOrderTwoSaveVo.getShopOrderBomComponnetSaveDto())) {
            // shopOrderTwoSaveVo.getShopOrderBomComponnetSaveDto().setShopOrderBo(shopOrderHandleBO.getBo());
            ShopOrderBomComponnetSaveDto shopOrderBomComponnetSaveDto = shopOrderTwoSaveVo.getShopOrderBomComponnetSaveDto();
            shopOrderBomComponnetSaveDto.setShopOrderBo(orderBo);
            List<ShopOrderBomComponnet> shopOrderBomComponnets = shopOrderBomComponnetSaveDto.getShopOrderBomComponnets();
            if(shopOrderBomComponnets != null || !"".equals(shopOrderBomComponnets)){
                for (int bomInt = 0; bomInt < shopOrderBomComponnets.size(); bomInt++) {
                    ShopOrderBomComponnet shopOrderBomComponnet = shopOrderBomComponnets.get(bomInt);
                    shopOrderBomComponnet.setShopOrderBo(orderBo);
                }
            }
            shopOrderTwoSaveVo.setShopOrderBomComponnetSaveDto(shopOrderBomComponnetSaveDto);
            shopOrderBomComponnetService.save(shopOrderBomComponnetSaveDto);
        }*/

        // ????????????BOM
//        if (ObjectUtil.isNotEmpty(shopOrderTwoSaveVo.getProcessBomComponnetSaveDto())) {
//            // shopOrderTwoSaveVo.getProcessBomComponnetSaveDto().setShopOrderBo(shopOrderHandleBO.getBo());
//            ShopOrderBomComponnetSaveDto shopOrderBomComponnetSaveDto = shopOrderTwoSaveVo.getProcessBomComponnetSaveDto();
//            shopOrderBomComponnetSaveDto.setShopOrderBo(orderBo);
//            List<ShopOrderBomComponnet> shopOrderBomComponnets = shopOrderBomComponnetSaveDto.getShopOrderBomComponnets();
//            if(shopOrderBomComponnets != null || !"".equals(shopOrderBomComponnets)){
//                for (int bomInt = 0; bomInt < shopOrderBomComponnets.size(); bomInt++) {
//                    ShopOrderBomComponnet shopOrderBomComponnet = shopOrderBomComponnets.get(bomInt);
//                    shopOrderBomComponnet.setShopOrderBo(orderBo);
//                }
//            }
//            shopOrderTwoSaveVo.setProcessBomComponnetSaveDto(shopOrderBomComponnetSaveDto);
//            shopOrderBomComponnetService.save(shopOrderBomComponnetSaveDto);
//        }

        //????????????BOM
//        if (ObjectUtil.isNotEmpty(shopOrderTwoSaveVo.getShopOrderBomComponnetSaveDto())) {
//            // shopOrderTwoSaveVo.getShopOrderBomComponnetSaveDto().setShopOrderBo(shopOrderHandleBO.getBo());
//            ShopOrderBomComponnetSaveDto shopOrderBomComponnetSaveDto = shopOrderTwoSaveVo.getShopOrderBomComponnetSaveDto();
//            shopOrderBomComponnetSaveDto.setShopOrderBo(orderBo);
//            shopOrderBomComponnetSaveDto.setType("SO");
//            shopOrderTwoSaveVo.setShopOrderBomComponnetSaveDto(shopOrderBomComponnetSaveDto);
//            shopOrderBomComponnetService.save(shopOrderTwoSaveVo.getShopOrderBomComponnetSaveDto());
//        }

        // ????????????BOM
        OrderRouter orderRouter = orderRouterService.getOrderRouter(orderBo);
        if(orderRouter == null){
            throw new CommonException("??????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        // ???????????????BOM
        OrderRouterProcess orderRouterProcess1 = orderRouter.getOrderRouterProcess();
        ShopOrderBomComponnetSaveDto shopOrderBomComponnetSaveDto = getShopOrderBomComponnetSaveDto(orderBo, orderRouterProcess1);

        // ???????????????BOM
        ShopOrderBomComponnetSaveDto shopOrderBomComponnetSaveDto1 =  shopOrderTwoSaveVo.getProcessBomComponnetSaveDto();
        if(ObjectUtil.isNotEmpty(shopOrderBomComponnetSaveDto1)) {
            shopOrderBomComponnetSaveDto1.setShopOrderBo(orderBo);
            List<ShopOrderBomComponnet> shopOrderBomComponnets = shopOrderBomComponnetSaveDto1.getShopOrderBomComponnets();
            if(shopOrderBomComponnets != null && shopOrderBomComponnets.size() > 0){
                if (ObjectUtil.isNotEmpty(shopOrderBomComponnetSaveDto)){
                    List<ShopOrderBomComponnet> shopOrderBomComponnets2 = shopOrderBomComponnetSaveDto.getShopOrderBomComponnets();
                    if(shopOrderBomComponnets2 != null && shopOrderBomComponnets2.size() > 0){
                        for (ShopOrderBomComponnet shopOrderBomComponnet1 : shopOrderBomComponnets2) {
                            String operationBo = shopOrderBomComponnet1.getOperationBo();
                            String componentBo = shopOrderBomComponnet1.getComponentBo();
                            // ???????????????????????????+????????????????????????
                            shopOrderBomComponnets.add(shopOrderBomComponnet1);
                            long countNum = shopOrderBomComponnets.stream().filter(shopOrderBomComponnet -> operationBo.equals(shopOrderBomComponnet.getOperationBo()) && componentBo.equals(shopOrderBomComponnet.getComponentBo())).map(ShopOrderBomComponnet::getComponentBo).count();
                            if(countNum > 1){
                                shopOrderBomComponnets.remove(shopOrderBomComponnet1);
                            }
                        }
                    }
                    shopOrderBomComponnetSaveDto1.setShopOrderBomComponnets(shopOrderBomComponnets);
                }
                shopOrderBomComponnetService.save(shopOrderBomComponnetSaveDto1);
            }else if (ObjectUtil.isNotEmpty(shopOrderBomComponnetSaveDto)) {
                List<ShopOrderBomComponnet> shopOrderBomComponnets2 = shopOrderBomComponnetSaveDto.getShopOrderBomComponnets();
                if(shopOrderBomComponnets2 != null && shopOrderBomComponnets2.size() > 0){
                    shopOrderBomComponnetService.save(shopOrderBomComponnetSaveDto);
                }
            }
        }else if (ObjectUtil.isNotEmpty(shopOrderBomComponnetSaveDto)) {
            List<ShopOrderBomComponnet> shopOrderBomComponnets = shopOrderBomComponnetSaveDto.getShopOrderBomComponnets();
            if(shopOrderBomComponnets != null && shopOrderBomComponnets.size() > 0){
                shopOrderBomComponnetService.save(shopOrderBomComponnetSaveDto);
            }
        }


        // ????????????SN??????(????????????)
        if(snBoList != null && snBoList.size() > 0){
            LabelTransferRequestDTO labelTransferRequestDTO = new LabelTransferRequestDTO();
            labelTransferRequestDTO.setLabelIds(snBoList);
            labelTransferRequestDTO.setNewOrderNo(new ShopOrderHandleBO(UserUtils.getSite(), shopOrderNewStr).getBo());
            labelTransferRequestDTO.setShopOrderTwoSaveVo(shopOrderTwoSaveVo);
            labelTransferRequestDTO.setOrderRouter(orderRouter);
            ResponseData<Boolean> snResponseData = snService.transferLabelsAsOrder(labelTransferRequestDTO);
            Assert.valid(!snResponseData.isSuccess(), snResponseData.getMsg());
            // snService.updateOrderBo(snBoList,shopOrderNewStr);
        }

//        // ????????????????????????SN???????????????(0???????????????1????????????)
//        ResponseData<List<String>> responseDataSnBoList = snService.queryOrderBoList(shopOrderBoStr,0);
//        Assert.valid(!responseDataSnBoList.isSuccess(), responseDataSnBoList.getMsg());
//        List<String> snBoList = responseDataSnBoList.getData();
//        // ????????????SN??????(????????????)
//        if(snBoList != null && snBoList.size() > 0){
//            LabelTransferRequestDTO labelTransferRequestDTO = new LabelTransferRequestDTO();
//            labelTransferRequestDTO.setLabelIds(snBoList);
//            labelTransferRequestDTO.setNewOrderNo(new ShopOrderHandleBO(UserUtils.getSite(), shopOrderNewStr).getBo());
//            ResponseData<Boolean> snResponseData = snService.transferLabelsAsOrder(labelTransferRequestDTO);
//            Assert.valid(!snResponseData.isSuccess(), snResponseData.getMsg());
//            // snService.updateOrderBo(snBoList,shopOrderNewStr);
//        }

        //String itemCode = itemHandleBO.getItem();
        //String itemVersion = itemHandleBO.getVersion();
        //????????????????????????
        if (ObjectUtil.isNotEmpty(shopOrderTwoSaveVo.getShopOrderPackRuleSaveDto())) {
            shopOrderTwoSaveVo.getShopOrderPackRuleSaveDto().setShopOrderBo(shopOrderHandleBO.getBo());
            shopOrderPackRuleService.save(shopOrderTwoSaveVo.getShopOrderPackRuleSaveDto());
        }else{
            // ????????????????????????
            ItemFullVo itemFullVo = itemService.getItemFullVoByItemAndVersion(itemCode, itemVersion);
            if(itemFullVo != null){
                if(itemFullVo.getPackingRuleList() == null || itemFullVo.getPackingRuleList().size() <= 0){
                    throw new CommonException("???????????????????????????!", CommonExceptionDefinition.BASIC_EXCEPTION);
                }
                List<ItemPackRuleDetailDto> itemPackRuleDetailDtoList = itemFullVo.getPackingRuleList();
                if(itemPackRuleDetailDtoList == null || itemPackRuleDetailDtoList.size() <= 0){
                    throw new CommonException("???????????????????????????!!", CommonExceptionDefinition.BASIC_EXCEPTION);
                }
                ShopOrderPackRuleSaveDto shopOrderPackRuleSaveDto = new ShopOrderPackRuleSaveDto();
                shopOrderPackRuleSaveDto.setShopOrderBo(shopOrderHandleBO.getBo()); // ??????BO
                String packRule = itemPackRuleDetailDtoList.get(0).getRuleRuleBo();
                shopOrderPackRuleSaveDto.setPackRuleBo(packRule); // (PACK_RULE_BO)????????????BO
                shopOrderPackRuleSaveDto.setItemBo(itemBo); // ??????BO
                List<ShopOrderPackRuleDetail> shopOrderPackRuleDetailList = new ArrayList<>();
                for(int p=0;p<itemPackRuleDetailDtoList.size();p++){
                    ShopOrderPackRuleDetail shopOrderPackRuleDetail = new ShopOrderPackRuleDetail(); // ????????????????????????
                    ItemPackRuleDetailDto itemPackRuleDetailDto = itemPackRuleDetailDtoList.get(p);
                    if(itemPackRuleDetailDto == null || "".equals(itemPackRuleDetailDto)){
                        throw new CommonException("???????????????????????????????????????!!", CommonExceptionDefinition.BASIC_EXCEPTION);
                    }
                    BeanUtil.copyProperties(itemPackRuleDetailDto,shopOrderPackRuleDetail);
                    /*if(packRule.equals(itemPackRuleDetailDto.getRuleRuleBo())){
                        throw new CommonException("??????????????????bo???????????????????????????!!", CommonExceptionDefinition.BASIC_EXCEPTION);
                    }*/
                    shopOrderPackRuleDetail.setPackRuleBo(itemPackRuleDetailDto.getRuleRuleBo()); // ????????????BO
                    shopOrderPackRuleDetail.setShopOrderBo(shopOrderHandleBO.getBo()); // ??????BO
                    shopOrderPackRuleDetailList.add(shopOrderPackRuleDetail);
                }
                shopOrderPackRuleSaveDto.setShopOrderPackRuleDetails(shopOrderPackRuleDetailList);
                // shopOrderTwoSaveVo.setShopOrderPackRuleSaveDto(shopOrderPackRuleSaveDto);
                shopOrderPackRuleService.save(shopOrderPackRuleSaveDto);
            }else{
                throw new CommonException("?????????????????????!!!", CommonExceptionDefinition.BASIC_EXCEPTION);
            }

        }
        //updateShopOrderScheduleQtyByBO(shopOrderBoStr, new BigDecimal(schedulQtyOldSum));
    }

    @Override
    public Boolean stopByStatus(ShopOrderTwoFullVo shopOrderTwoFullVo) throws CommonException {
        String bo = shopOrderTwoFullVo.getBo();
        if (StrUtil.isBlank(bo)) {
            throw new CommonException("??????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        ShopOrder shopOrder = shopOrderMapper.selectById(bo);
        if(shopOrder==null){
            throw new CommonException("???????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        String stateBo = shopOrder.getStateBo();
        String[] split = stateBo.split(",");
        //????????????
        String state = split[1];
        //?????? ?????? ?????? ???????????????
        if (OrderStateEnum.OVER.getCode().equals(state) || OrderStateEnum.CLOSED.getCode().equals(state)
                || OrderStateEnum.PAUSE.getCode().equals(state) || OrderStateEnum.PRO_PAUSE.getCode().equals(state)) {
            throw new CommonException("?????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
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
    public Boolean recoveryByStatus(ShopOrderTwoFullVo shopOrderTwoFullVo) throws CommonException {
        String bo = shopOrderTwoFullVo.getBo();
        if (StrUtil.isBlank(bo)) {
            throw new CommonException("??????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        ShopOrder shopOrder = shopOrderMapper.selectById(bo);
        if(shopOrder==null){
            throw new CommonException("???????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        String stateBo = shopOrder.getStateBo();
        String[] split = stateBo.split(",");
        //????????????
        String state = split[1];
        if (!OrderStateEnum.PAUSE.getCode().equals(state) && !OrderStateEnum.PRO_PAUSE.getCode().equals(state)) {
            throw new CommonException("?????????????????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        shopOrder.setStateBo(new StatusHandleBO(UserUtils.getSite(), shopOrder.getRecoveryStatus()).getBo());
        shopOrder.setRecoveryStatus(state);
        updateById(shopOrder);
        return true;
    }

    @Override
    public Boolean closeByStatus(ShopOrderTwoFullVo shopOrderTwoFullVo) throws CommonException {
        String bo = shopOrderTwoFullVo.getBo();
        if (StrUtil.isBlank(bo)) {
            throw new CommonException("??????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        ShopOrder shopOrder = shopOrderMapper.selectById(bo);
        if(shopOrder==null){
            throw new CommonException("???????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        String stateBo = shopOrder.getStateBo();
        String[] split = stateBo.split(",");
        //????????????
        String state = split[1];
        //?????? ??????
        if (OrderStateEnum.OVER.getCode().equals(state) || OrderStateEnum.CLOSED.getCode().equals(state)) {
            throw new CommonException("????????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        shopOrder.setStateBo(new StatusHandleBO(UserUtils.getSite(), OrderStateEnum.CLOSED.getCode()).getBo());
        shopOrder.setRecoveryStatus(state);
        updateById(shopOrder);
        return true;
    }


    /**
     * ??????
     *
     * @param shopOrderTwoSaveVo shopOrderTwoFullVo
     * @throws CommonException ??????
     */
    private void insertShopOrder(ShopOrderTwoSaveVo shopOrderTwoSaveVo) throws CommonException {

        ShopOrderHandleBO shopOrderHandleBO = new ShopOrderHandleBO(UserUtils.getSite(), shopOrderTwoSaveVo.getShopOrder());
        ShopOrder shopOrder = new ShopOrder();
        shopOrder.setBo(shopOrderHandleBO.getBo());
        shopOrder.setSite(shopOrderHandleBO.getSite());
        shopOrder.setShopOrder(shopOrderTwoSaveVo.getShopOrder());
        shopOrder.setOrderDesc(StrUtil.isBlank(shopOrderTwoSaveVo.getOrderDesc()) ? shopOrderTwoSaveVo.getShopOrder() : shopOrderTwoSaveVo.getOrderDesc());
        shopOrder.setShopOrderType(shopOrderTwoSaveVo.getShopOrderType());
        shopOrder.setStateBo(new StatusHandleBO(shopOrderHandleBO.getSite(), shopOrderTwoSaveVo.getState()).getBo());
        shopOrder.setOrderQty(shopOrderTwoSaveVo.getSchedulQty());
        shopOrder.setSchedulQty(shopOrderTwoSaveVo.getSchedulQty()); //????????????
        shopOrder.setReleaseQty(shopOrderTwoSaveVo.getSchedulQty()); //????????????
        shopOrder.setLabelQty(shopOrderTwoSaveVo.getLabelQty()); // ????????????
        shopOrder.setErpOrderBo(shopOrderTwoSaveVo.getErpOrderBo());// ERP????????????ID
        shopOrder.setErpOrderCode(shopOrderTwoSaveVo.getErpOrderCode());// ERP????????????Code
        shopOrder.setErpOrderDesc(shopOrderTwoSaveVo.getErpOrderDesc());// ERP????????????Desc
        shopOrder.setCustomerBo(shopOrderTwoSaveVo.getCustomerBo());// ??????ID

        // ??????????????????
        if(!StrUtil.isBlank(shopOrderTwoSaveVo.getItemBo())){
            shopOrder.setItemBo(shopOrderTwoSaveVo.getItemBo()); //????????????Bo
            //shopOrder.setItemCode(shopOrderTwoSaveVo.getItemCode()); //????????????Code
            //shopOrder.setItemVersion(shopOrderTwoSaveVo.getItemVersion());  //??????????????????
            shopOrder.setItemDesc(shopOrderTwoSaveVo.getItemDesc()); //??????????????????
        }else if(!StrUtil.isBlank(shopOrderTwoSaveVo.getItemCode())){
            ItemHandleBO itemHandleBO = new ItemHandleBO(shopOrderHandleBO.getSite(), shopOrderTwoSaveVo.getItemCode(), shopOrderTwoSaveVo.getItemVersion());
            itemService.getExitsItemByItemHandleBO(itemHandleBO); //?????????????????????
            shopOrder.setItemBo(itemHandleBO.getBo());
            //shopOrder.setItemCode(shopOrderTwoSaveVo.getItemCode()); //????????????Code
            //shopOrder.setItemVersion(shopOrderTwoSaveVo.getItemVersion());  //??????????????????
            shopOrder.setItemDesc(shopOrderTwoSaveVo.getItemDesc()); //??????????????????
        }

        shopOrder.setRouterBo(new RouterHandleBO(UserUtils.getSite(), shopOrderTwoSaveVo.getRouter(), shopOrderTwoSaveVo.getRouterVersion()).getBo());
        shopOrder.setOrderBomBo(shopOrderTwoSaveVo.getOrderBomBo());

        if (shopOrderTwoSaveVo.getPlanStartDate() != null) {
            shopOrder.setPlanStartDate(shopOrderTwoSaveVo.getPlanStartDate());
        }
        if (shopOrderTwoSaveVo.getPlanEndDate() != null) {
            shopOrder.setPlanEndDate(shopOrderTwoSaveVo.getPlanEndDate());
        }
        //????????????????????????????????????
        if (shopOrderTwoSaveVo.getPlanStartDate() != null && shopOrderTwoSaveVo.getPlanEndDate() != null) {
            if (shopOrderTwoSaveVo.getPlanStartDate().getTime() > shopOrderTwoSaveVo.getPlanEndDate().getTime()) {
                throw new CommonException("????????????????????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
        }

        //??????????????????????????????
        if (!StrUtil.isBlank(shopOrderTwoSaveVo.getBom()) && !StrUtil.isBlank(shopOrderTwoSaveVo.getBomVersion())) {
            try {
                Bom bom = bomService.selectByBom(shopOrderTwoSaveVo.getBom(), shopOrderTwoSaveVo.getBomVersion());
                shopOrder.setBomBo(bom.getBo());
            }catch (CommonException commonException){
                throw new CommonException("??????????????????BOM?????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
        }

        //????????????
        String productionLineBo = shopOrderTwoSaveVo.getProductionLineBo();
        if(!StrUtil.isBlank(productionLineBo)){
            ProductLineHandleBO productLineHandleBO = new ProductLineHandleBO(productionLineBo);
            productLineService.getExistProductLineByHandleBO(productLineHandleBO);
            shopOrder.setProductLineBo(productionLineBo);
        }else if(!StrUtil.isBlank(shopOrderTwoSaveVo.getProductionLineCode())) {
            ProductLineHandleBO productLineHandleBO = new ProductLineHandleBO(shopOrderHandleBO.getSite(), shopOrderTwoSaveVo.getProductionLineCode());
            productLineService.getExistProductLineByHandleBO(productLineHandleBO);
            shopOrder.setProductLineBo(productLineHandleBO.getBo());
        }else {
            throw new CommonException("??????BO??????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        //????????????
        String shiftBo = shopOrderTwoSaveVo.getShiftBo();
        if (!StrUtil.isBlank(shiftBo)) {
            // ClassFrequencyEntity classFrequencyEntity = classFrequencyService.findById(shiftBo);
            shopOrder.setShiftBo(shiftBo);
        }else {
            throw new CommonException("?????????shiftBo??????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        /*if (!StrUtil.isBlank(shiftBo)) {
            shiftService.selectShift(shiftBo);
            shopOrder.setShiftBo(shiftBo);
        }else if (!StrUtil.isBlank(shopOrderTwoSaveVo.getShiftCode())) {
            ShiftHandleBO shiftHandleBO = new ShiftHandleBO(shopOrderHandleBO.getSite(), shopOrderTwoSaveVo.getShiftCode());
            shiftService.selectShift(shiftHandleBO.getBo());
            shopOrder.setShiftBo(shiftHandleBO.getBo());
        }else {
            shopOrder.setShiftBo("");
        }*/

        shopOrder.setObjectSetBasicAttribute(userUtil.getUser().getUserName(), new Date());
        /*ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(shopOrder); //??????????????????????????????
        if (validResult.hasErrors()) {
            throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
        }*/
        //??????????????????
        super.save(shopOrder);

    }

    /**
     * ??????????????????
     *
     * @param shopOrder       ????????????????????????
     * @param shopOrderTwoSaveVo ???????????????
     */
    private void updateShopOrder(ShopOrder shopOrder, ShopOrderTwoSaveVo shopOrderTwoSaveVo) throws CommonException {

        Date frontModifyDate = shopOrderTwoSaveVo.getModifyDate(); //????????????????????????
        CommonUtil.compareDateSame(frontModifyDate, shopOrder.getModifyDate()); //????????????????????????
        ShopOrderHandleBO shopOrderHandleBO = new ShopOrderHandleBO(shopOrder.getBo());
        ShopOrder shopOrderEntity = new ShopOrder();
        shopOrderEntity.setBo(shopOrderHandleBO.getBo());
        shopOrderEntity.setSite(shopOrderHandleBO.getSite());
        shopOrderEntity.setOrderDesc(StrUtil.isBlank(shopOrderTwoSaveVo.getOrderDesc()) ? shopOrderTwoSaveVo.getShopOrder() : shopOrderTwoSaveVo.getOrderDesc());
        shopOrderEntity.setStateBo(new StatusHandleBO(shopOrderHandleBO.getSite(), shopOrderTwoSaveVo.getState()).getBo());
        shopOrderEntity.setRouterBo(new RouterHandleBO(UserUtils.getSite(), shopOrderTwoSaveVo.getRouter(), shopOrderTwoSaveVo.getRouterVersion()).getBo());
        shopOrderEntity.setShopOrderType(shopOrderTwoSaveVo.getShopOrderType());
        shopOrderEntity.setOrderQty(shopOrderTwoSaveVo.getSchedulQty());
        shopOrderEntity.setSchedulQty(shopOrderTwoSaveVo.getSchedulQty()); //????????????
        shopOrderEntity.setReleaseQty(shopOrderTwoSaveVo.getSchedulQty()); //????????????
        shopOrderEntity.setErpOrderBo(shopOrderTwoSaveVo.getErpOrderBo());// ERP????????????ID
        shopOrderEntity.setErpOrderCode(shopOrderTwoSaveVo.getErpOrderCode());// ERP????????????Code
        shopOrderEntity.setErpOrderDesc(shopOrderTwoSaveVo.getErpOrderDesc());// ERP????????????Desc
        shopOrderEntity.setCustomerBo(shopOrderTwoSaveVo.getCustomerBo());// ??????ID

        shopOrderEntity.setModifyUser(userUtil.getUser().getUserName());
        shopOrderEntity.setModifyDate(new Date());
        if (shopOrderTwoSaveVo.getPlanStartDate() != null) {
            shopOrderEntity.setPlanStartDate(shopOrderTwoSaveVo.getPlanStartDate());
        } else {
            shopOrderEntity.setPlanStartDate(null);
        }
        if (shopOrderTwoSaveVo.getPlanEndDate() != null) {
            shopOrderEntity.setPlanEndDate(shopOrderTwoSaveVo.getPlanEndDate());
        } else {
            shopOrderEntity.setPlanEndDate(null);
        }
        //????????????????????????????????????
        if (shopOrderEntity.getPlanStartDate() != null && shopOrderEntity.getPlanEndDate() != null) {
            if (shopOrderEntity.getPlanStartDate().getTime() > shopOrderEntity.getPlanEndDate().getTime()) {
                throw new CommonException("????????????????????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
        }

        /**
         * ????????????
         */
        //shopOrderEntity.setOrderBomBo(shopOrderTwoSaveVo.getOrderBomBo());
        //shopOrder.setSchedulQty(shopOrderTwoSaveVo.getSchedulQty()); //????????????
        //shopOrder.setItemBo(shopOrderTwoSaveVo.getItemBo()); //????????????Bo
        //shopOrder.setItemCode(shopOrderTwoSaveVo.getItemCode()); //????????????Code
        //shopOrder.setItemVersion(shopOrderTwoSaveVo.getItemVersion());  //??????????????????
        //shopOrder.setItemDesc(shopOrderTwoSaveVo.getItemDesc()); //??????????????????

        /*ItemHandleBO itemHandleBO = new ItemHandleBO(shopOrderHandleBO.getSite(), shopOrderTwoSaveVo.getItemCode(), shopOrderTwoSaveVo.getItemVersion());
        itemService.getExitsItemByItemHandleBO(itemHandleBO); //?????????????????????
        shopOrderEntity.setItemBo(itemHandleBO.getBo());*/
        // ??????????????????
        if(!StrUtil.isBlank(shopOrderTwoSaveVo.getItemBo())){
            shopOrderEntity.setItemBo(shopOrderTwoSaveVo.getItemBo()); //????????????Bo
            //shopOrder.setItemCode(shopOrderTwoSaveVo.getItemCode()); //????????????Code
            //shopOrder.setItemVersion(shopOrderTwoSaveVo.getItemVersion());  //??????????????????
            shopOrderEntity.setItemDesc(shopOrderTwoSaveVo.getItemDesc()); //??????????????????
        }else if(!StrUtil.isBlank(shopOrderTwoSaveVo.getItemCode())){
            ItemHandleBO itemHandleBO = new ItemHandleBO(shopOrderHandleBO.getSite(), shopOrderTwoSaveVo.getItemCode(), shopOrderTwoSaveVo.getItemVersion());
            itemService.getExitsItemByItemHandleBO(itemHandleBO); //?????????????????????
            shopOrderEntity.setItemBo(itemHandleBO.getBo());
            //shopOrder.setItemCode(shopOrderTwoSaveVo.getItemCode()); //????????????Code
            //shopOrder.setItemVersion(shopOrderTwoSaveVo.getItemVersion());  //??????????????????
            shopOrderEntity.setItemDesc(shopOrderTwoSaveVo.getItemDesc()); //??????????????????
        }


        if (!StrUtil.isBlank(shopOrderTwoSaveVo.getBom()) && !StrUtil.isBlank(shopOrderTwoSaveVo.getBomVersion())) {
            //??????????????????????????????
            Bom bom = bomService.selectByBom(shopOrderTwoSaveVo.getBom(), shopOrderTwoSaveVo.getBomVersion());
            shopOrderEntity.setBomBo(bom.getBo());
        } else {
            shopOrderEntity.setBomBo("");
        }

        //????????????
        String productionLineBo = shopOrderTwoSaveVo.getProductionLineBo();
        if(!StrUtil.isBlank(productionLineBo)){
            ProductLineHandleBO productLineHandleBO = new ProductLineHandleBO(productionLineBo);
            productLineService.getExistProductLineByHandleBO(productLineHandleBO);
            shopOrderEntity.setProductLineBo(productionLineBo);
        }else if(!StrUtil.isBlank(shopOrderTwoSaveVo.getProductionLineCode())) {
            ProductLineHandleBO productLineHandleBO = new ProductLineHandleBO(shopOrderHandleBO.getSite(), shopOrderTwoSaveVo.getProductionLineCode());
            productLineService.getExistProductLineByHandleBO(productLineHandleBO);
            shopOrderEntity.setProductLineBo(productLineHandleBO.getBo());
        }else {
            shopOrderEntity.setProductLineBo("");
        }

        //????????????
        String shiftBo = shopOrderTwoSaveVo.getShiftBo();
        if (!StrUtil.isBlank(shiftBo)) {
            //shiftService.selectShift(shiftBo);
            shopOrderEntity.setShiftBo(shiftBo);
        }else if (!StrUtil.isBlank(shopOrderTwoSaveVo.getShiftCode())) {
            ShiftHandleBO shiftHandleBO = new ShiftHandleBO(shopOrderHandleBO.getSite(), shopOrderTwoSaveVo.getShiftCode());
            //shiftService.selectShift(shiftHandleBO.getBo());
            shopOrderEntity.setShiftBo(shiftHandleBO.getBo());
        }else {
            shopOrderEntity.setShiftBo("");
        }

        //????????????????????????????????????
        /*ValidationUtil.ValidResult validResult =
                ValidationUtil.validateProperties(shopOrderEntity, "orderDesc", "stateBo", "isOverfulfill", "orderQty");
        if (validResult.hasErrors()) {
            throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
        }*/
        Integer integer = shopOrderMapper.updateShopOrder(shopOrderEntity, shopOrder.getModifyDate());//??????????????????
        if (integer == 0) {
            throw new CommonException("?????????????????????????????????????????????", CommonExceptionDefinition.BASIC_EXCEPTION);
        }

    }


    /**
     * ???????????????????????????
     *
     * @param shopOrder ??????????????????
     * @return boolean
     * @throws CommonException ??????
     */
    private boolean validateShopOrderIsUsed(ShopOrder shopOrder) throws CommonException {
        if (shopOrder == null) {
            return false;
        } else {
            //???????????????
            if (shopOrder.getSchedulQty() != null && shopOrder.getSchedulQty().doubleValue() > 0) return true;
            //???????????????
            if (shopOrder.getReleaseQty() != null && shopOrder.getReleaseQty().doubleValue() > 0) return true;
            //????????????
            //????????????
            //????????????
            //????????????
            return false;
        }

    }


    /**
     * ??????????????????
     *
     * @param shopOrderHandleBO ??????BOHandle
     * @throws CommonException ??????
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void deleteShopOrderByHandleBO(ShopOrderHandleBO shopOrderHandleBO) throws CommonException {

        ShopOrder shopOrder = getExistShopOrder(shopOrderHandleBO);
        shopOrder.setModifyUser(userUtil.getUser().getUserName());
        shopOrder.setModifyDate(new Date());
        //CommonUtil.compareDateSame(modifyDate, shopOrder.getModifyDate());
        boolean isUsed = validateShopOrderIsUsed(shopOrder);
        if (isUsed) {
            throw new CommonException("??????" + shopOrder + "????????????????????????", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        QueryWrapper<ShopOrder> wrapper = new QueryWrapper<>();
        //wrapper.eq(ShopOrder.BO, shopOrder.getBo()).eq(ShopOrder.MODIFY_DATE, modifyDate);
        wrapper.eq(ShopOrder.BO, shopOrder.getBo());

        //??????????????????
        Integer integer = shopOrderMapper.delete(wrapper);
        if (integer == 0) {
            throw new CommonException("???????????????????????????????????????????????????", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        //???????????????????????????
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
     * ????????????????????????
     *
     * @param bo          ??????BO
     * @param completeQty ????????????
     * @return Integer
     */
    @Override
    public Integer updateShopOrderCompleteQtyByBO(String bo, BigDecimal completeQty) {

        return shopOrderMapper.updateShopOrderCompleteQtyByBO(bo, completeQty);
    }

    /**
     * ????????????????????????
     *
     * @param bo       ??????BO
     * @param scrapTty ????????????
     * @return Integer
     */
    @Override
    public Integer updateShopOrderScrapQtyByBO(String bo, BigDecimal scrapTty) {
        return shopOrderMapper.updateShopOrderCompleteQtyByBO(bo, scrapTty);
    }

    /**
     * ????????????????????????
     *
     * @param bo       ??????BO
     * @param labelQty ????????????
     * @return Integer
     */
    @Override
    public Integer updateShopOrderLabelQtyByBO(String bo, BigDecimal labelQty) {
        return shopOrderMapper.updateShopOrderLabelQtyByBO(bo, labelQty);
    }

    /**
     * ????????????????????????
     *
     * @param bo          ??????BO
     * @param scheduleQty ????????????
     * @return Integer
     */
    @Override
    public Integer updateShopOrderScheduleQtyByBO(String bo, BigDecimal scheduleQty) {
        return shopOrderMapper.updateShopOrderScheduleQtyByBO(bo, scheduleQty);
    }

    /**
     * ????????????????????????
     *
     * @param bo          ??????BO
     * @param scheduleQty ????????????
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
        //????????????????????????????????????????????????????????????????????????
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

        List<Map> shopOrderTwoFullVos = new ArrayList<>();

        shopOrderList.forEach(shopOrder -> {
            Map result = createShopOrderTwoFullVo(shopOrder, site);
            shopOrderTwoFullVos.add(result);

            if (firstBinding != null && !firstBinding.isEmpty()) {
                String no = "";
                for (Map<String, Object> stringObjectMap : firstBinding) {
                    if (shopOrder.getShopOrder().equals(stringObjectMap.get("bo").toString())) {
                        no = stringObjectMap.get("no").toString();
                    }
                }
                if (StringUtils.isNotBlank(no)) {
                    Map last = shopOrderTwoFullVos.get(shopOrderTwoFullVos.size() - 1);
                    String finalNo = no;
                    List<Map<String, Object>> bind = noFirstBinding.stream().filter(e -> finalNo.equals(e.get("no").toString())).collect(Collectors.toList());
                    bind.stream().sorted(Comparator.comparing(e -> e.get("sort").toString()));
                    last.put("margin", bind.size());
                    last.put("no", no);
                    bind.forEach(map -> {
                        QueryWrapper<ShopOrder> entityWrapper = new QueryWrapper<>();
                        entityWrapper.eq("SHOP_ORDER", map.get("bo").toString());
                        ShopOrder order = shopOrderMapper.selectOne(entityWrapper);
                        Map shopOrderTwoFullVo = createShopOrderTwoFullVo(order, site);
                        shopOrderTwoFullVo.put("no", finalNo);
                        shopOrderTwoFullVos.add(shopOrderTwoFullVo);
                    });
                }
            }
        });
        list.setRecords(shopOrderTwoFullVos);
        return list;
    }

    @Override
    public void updateShopOrderTwoFullVo(ShopOrderTwoFullVo shopOrderTwoFullVo) {
        ShopOrder shopOrder = new ShopOrder();
        BeanUtils.copyProperties(shopOrderTwoFullVo, shopOrder);
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


    public Map createShopOrderTwoFullVo(ShopOrder shopOrder, String site) {
        ShopOrderTwoFullVo shopOrderTwoFullVo = new ShopOrderTwoFullVo();
        BeanUtils.copyProperties(shopOrder, shopOrderTwoFullVo);

        if (shopOrder.getItemBo() != null) {
            ItemHandleBO itemHandleBO = new ItemHandleBO(shopOrder.getItemBo());
            shopOrderTwoFullVo.setItemCode(itemHandleBO.getItem());
            shopOrderTwoFullVo.setItemVersion(itemHandleBO.getVersion());
            shopOrderTwoFullVo.setState(new StatusHandleBO(shopOrder.getStateBo()).getState());
        }


        //??????BOM
        if (!StrUtil.isBlank(shopOrder.getBomBo())) {

            BomHandleBO bomHandleBO = new BomHandleBO(shopOrder.getBomBo());
            shopOrderTwoFullVo.setBom(bomHandleBO.getBom());
            shopOrderTwoFullVo.setBomVersion(bomHandleBO.getVersion());
        }
        //??????????????????
        if (!StrUtil.isBlank(shopOrder.getRouterBo())) {

            RouterHandleBO routerHandleBO = new RouterHandleBO(shopOrder.getRouterBo());
            shopOrderTwoFullVo.setRouter(routerHandleBO.getRouter());
            shopOrderTwoFullVo.setRouterVersion(routerHandleBO.getVersion());
        }

        //????????????
        if (!StrUtil.isBlank(shopOrder.getProductLineBo())) {
            // ProductLineHandleBO productLineHandleBO = new ProductLineHandleBO(shopOrder.getProductLineBo());
            ProductLine productLine = productLineService.getById(shopOrder.getProductLineBo());
            if(productLine != null){
                shopOrderTwoFullVo.setProductionLineCode(productLine.getProductLine()); // ????????????
                shopOrderTwoFullVo.setProductionLineDesc(productLine.getProductLineDesc()); // ????????????
            }
        }
        //????????????
        if (!StrUtil.isBlank(shopOrder.getShiftBo())) {
            //  Shift shift = shiftService.getById(shopOrder.getShiftBo());
            ResponseData<ClassFrequencyEntity> classFrequencyEntityResponseData = classFrequencyService.getById(shopOrder.getShiftBo());
            if(classFrequencyEntityResponseData != null){
                ClassFrequencyEntity classFrequencyEntity = classFrequencyEntityResponseData.getData();
                if(classFrequencyEntity != null){
                    shopOrderTwoFullVo.setShiftCode(classFrequencyEntity.getCode()); // ????????????
                    shopOrderTwoFullVo.setShiftName(classFrequencyEntity.getName()); // ????????????
                    // shopOrderTwoFullVo.setShiftDesc(classFrequencyEntity.g()); // ????????????
                }
            }

        }

        // ????????????
        String customeBo = shopOrder.getCustomerBo();
        if(customeBo != null && !"".equals(customeBo)){
            Customer customer = customerService.getById(customeBo);
            shopOrderTwoFullVo.setCustomer(customer);
        }


        //??????????????????
//        Item exitsItem = null;
//        try {
//            exitsItem = itemService.getExitsItemByItemHandleBO(new ItemHandleBO(shopOrder.getItemBo()));
//        } catch (CommonException e) {
//            throw new RuntimeException("??????????????????");
//        }
//        shopOrderTwoFullVo.setItemDesc(exitsItem.getItemDesc());

        //ZHUAN MAP
        Map result = JSONObject.parseObject(JSONObject.toJSONString(shopOrderTwoFullVo), Map.class);

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
                //?????????
                String filter_ = key.replaceAll("filter_", "");
                Map<String, Object> map = new HashMap<>();
                map.put("key", filter_);
                map.put("value", params.get(key).toString());
                map.put("site", params.get("site").toString());
                n++;
                //????????????????????????
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

    /**
     * ?????????????????????????????????
     * @param itemBo
     * @param orderBo
     * @return
     * @throws CommonException
     */
    @Override
    public Boolean updateOrderProductInspectionItems(String itemBo, String orderBo) throws CommonException {
        // ?????????????????????
        /*String itemBo = shopOrderTwoSaveVo.getItemBo();
        String orderBo =shopOrderHandleBO.getBo();*/

        // ????????????????????????????????????????????????
        MeProductInspectionItemsOrderDto meProductInspectionItemsOrderDto = new MeProductInspectionItemsOrderDto();
        ResponseData responseData = meProductInspectionItemsOrderService.deleteOrderItems(orderBo);
        if(responseData.getCode() == "999"){
            // throw new CommonException("?????????????????????????????????????????????", CommonExceptionDefinition.BASIC_EXCEPTION);
            throw new CommonException(responseData.getMsg(), CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        // ????????????????????????
        MeProductGroupInspectionItemsDto meProductGroupInspectionItemsDto = new MeProductGroupInspectionItemsDto();
        meProductGroupInspectionItemsDto.setItemGroupBo(itemBo); // ???????????????
        List<MeProductGroupInspectionItemsEntity> meProductGroupInspectionItemsEntityList = meProductInspectionItemsOrderService.listGroupItems(meProductGroupInspectionItemsDto);
        for(int i=0;i<meProductGroupInspectionItemsEntityList.size();i++){
            MeProductInspectionItemsOrderEntity meProductInspectionItemsOrderEntity = new MeProductInspectionItemsOrderEntity();
            meProductInspectionItemsOrderEntity.setOrderBo(orderBo);
            MeProductGroupInspectionItemsEntity meProductGroupInspectionItemsEntity = meProductGroupInspectionItemsEntityList.get(i);
            if (ObjectUtil.isNotEmpty(meProductGroupInspectionItemsEntity)) {
                BeanUtils.copyProperties(meProductGroupInspectionItemsEntity, meProductInspectionItemsOrderEntity);
                meProductInspectionItemsOrderEntity.setItemType("1"); // ?????????????????????
                meProductInspectionItemsOrderEntity.setItemGroupBo(itemBo);
                meProductInspectionItemsOrderEntity.setCreateDate(new Date());
                meProductInspectionItemsOrderEntity.setCreateUser(UserUtils.getCurrentUser().getUserName());
                meProductInspectionItemsOrderService.save(meProductInspectionItemsOrderEntity);

                // ?????????????????????????????????????????????
//                Integer inspectionItemId = meProductGroupInspectionItemsEntity.getId();
//                MeProductInspectionItemsNcCode meProductInspectionItemsNcCode = new MeProductInspectionItemsNcCode();
//                meProductInspectionItemsNcCode.setInspectionItemId(inspectionItemId);
//                meProductInspectionItemsNcCode.setItemType("1"); // 0????????????1????????????
//                ResponseData<List<MeProductInspectionItemsNcCode>> listResponseData = meProductInspectionItemsNcCodeService.listItemNcCodes(meProductInspectionItemsNcCode);
//                Assert.valid(!listResponseData.isSuccess(), listResponseData.getMsg());
//                List<MeProductInspectionItemsNcCode> meProductInspectionItemsNcCodeList = listResponseData.getData();
//                if(meProductInspectionItemsNcCodeList != null && !"".equals(meProductInspectionItemsNcCodeList)){
//                    MeProductInspectionItemsOrderNcCode meProductInspectionItemsOrderNcCode = new MeProductInspectionItemsOrderNcCode();
//                    for (MeProductInspectionItemsNcCode itemsNcCode : meProductInspectionItemsNcCodeList) {
//                        BeanUtils.copyProperties(itemsNcCode, meProductInspectionItemsOrderNcCode);
//                        meProductInspectionItemsOrderNcCode.setBo(null);
//                        meProductInspectionItemsOrderNcCodeService.save(meProductInspectionItemsOrderNcCode);
//                    }
//                }
            }
        }
        // ?????????????????????
        MeProductInspectionItemsDto meProductInspectionItemsDto = new MeProductInspectionItemsDto();
        meProductInspectionItemsDto.setItemBo(itemBo); // ????????????
        List<MeProductInspectionItemsEntity> meProductInspectionItemsEntityList = meProductInspectionItemsOrderService.listItems(meProductInspectionItemsDto);
        for(int j=0;j<meProductInspectionItemsEntityList.size();j++) {
            MeProductInspectionItemsOrderEntity meProductInspectionItemsOrderEntity = new MeProductInspectionItemsOrderEntity();
            meProductInspectionItemsOrderEntity.setOrderBo(orderBo);
            MeProductInspectionItemsEntity meProductInspectionItemsEntity = meProductInspectionItemsEntityList.get(j);
            if (ObjectUtil.isNotEmpty(meProductInspectionItemsEntity)) {
                BeanUtils.copyProperties(meProductInspectionItemsEntity, meProductInspectionItemsOrderEntity);
                meProductInspectionItemsOrderEntity.setItemType("0"); // ?????????????????????
                meProductInspectionItemsOrderEntity.setItemBo(itemBo);
                meProductInspectionItemsOrderEntity.setCreateDate(new Date());
                meProductInspectionItemsOrderEntity.setCreateUser(UserUtils.getCurrentUser().getUserName());
                meProductInspectionItemsOrderService.save(meProductInspectionItemsOrderEntity);

                // ??????????????????????????????????????????
//                Integer inspectionItemId = meProductInspectionItemsEntity.getId();
//                MeProductInspectionItemsNcCode meProductInspectionItemsNcCode = new MeProductInspectionItemsNcCode();
//                meProductInspectionItemsNcCode.setInspectionItemId(inspectionItemId);
//                meProductInspectionItemsNcCode.setItemType("0"); // 0????????????1????????????
//                ResponseData<List<MeProductInspectionItemsNcCode>> listResponseData = meProductInspectionItemsNcCodeService.listItemNcCodes(meProductInspectionItemsNcCode);
//                Assert.valid(!listResponseData.isSuccess(), listResponseData.getMsg());
//                List<MeProductInspectionItemsNcCode> meProductInspectionItemsNcCodeList = listResponseData.getData();
//                if(meProductInspectionItemsNcCodeList != null && !"".equals(meProductInspectionItemsNcCodeList)){
//                    MeProductInspectionItemsOrderNcCode meProductInspectionItemsOrderNcCode = new MeProductInspectionItemsOrderNcCode();
//                    for (MeProductInspectionItemsNcCode itemsNcCode : meProductInspectionItemsNcCodeList) {
//                        BeanUtils.copyProperties(itemsNcCode, meProductInspectionItemsOrderNcCode);
//                        meProductInspectionItemsOrderNcCode.setBo(null);
//                        meProductInspectionItemsOrderNcCodeService.save(meProductInspectionItemsOrderNcCode);
//                    }
//                }
            }
        }
        return true;
    }

}

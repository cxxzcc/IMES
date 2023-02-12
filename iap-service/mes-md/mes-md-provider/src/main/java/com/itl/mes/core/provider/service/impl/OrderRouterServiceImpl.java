package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.serviceImpl.ImomServiceImpl;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.ItemHandleBO;
import com.itl.mes.core.api.bo.ShopOrderHandleBO;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.dto.CustomDataValRequest;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.*;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import com.itl.mes.core.api.vo.CustomDataValVo;
import com.itl.mes.core.api.vo.OrderRouterVo;
import com.itl.mes.core.api.vo.ShopOrderTwoFullVo;
import com.itl.mes.core.provider.mapper.OrderRouterMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 工艺路线-工单副本
 * </p>
 *
 * @author chenjx1
 * @since 2021-10-26
 */
@Service
@Transactional
public class OrderRouterServiceImpl extends ImomServiceImpl<OrderRouterMapper, OrderRouter> implements OrderRouterService {

    @Autowired
    OrderRouterProcessService orderRouterProcessService;

    @Autowired
    ItemService itemService;
    @Autowired
    SnService snService;
    @Autowired
    CustomDataValService customDataValService;

    @Resource
    OrderRouterMapper orderRouterMapper;

    @Autowired
    private ShopOrderTwoService shopOrderTwoService;

    @Autowired
    private RouterService routerService;

    @Autowired
    private IProcessRouteFitProductService productService; //产品工艺路线设置
    @Autowired
    private IProcessRouteFitProductGroupService productGroupService; //产品组工艺路线设置
    @Autowired
    private IProcessRouteFitProductionLineService productionLineService; //产线工艺路线设置

    @Autowired
    private ItemGroupService itemGroupService;

    /**
     * 获取工单工艺路线信息
     */
    @Override
    public List<OrderRouter> getOrderRouterList(OrderRouter OrderRouter) throws CommonException {
        QueryWrapper<OrderRouter> orderRouterWrapper = new QueryWrapper<>();
        orderRouterWrapper.setEntity(OrderRouter);
        List<OrderRouter> orderRouterList = orderRouterMapper.selectList(orderRouterWrapper);

        for(OrderRouter orderRouter : orderRouterList){
            String bo = orderRouter.getBo();
            // 工单工艺路线路线图
            Item item = itemService.getById(orderRouter.getItemBo());
            orderRouter.setItem(item);
            OrderRouterProcess orderRouterProcess = orderRouterProcessService.getById(bo);
            orderRouter.setOrderRouterProcess(orderRouterProcess);

            // 自定义数据
            List<CustomDataAndValVo> customDataAndValVos = customDataValService.selectCustomDataAndValByBoAndDataType(orderRouter.getSite(),
                    orderRouter.getBo(), CustomDataTypeEnum.ROUTER.getDataType());
            orderRouter.setCustomDataAndValVoList(customDataAndValVos);
        }
        return orderRouterList;
    }

    /**
     * 获取工单工艺路线信息
     */
    @Override
    public OrderRouter getOrderRouter(String shopOrderBo) throws CommonException {
        QueryWrapper<OrderRouter> orderRouterWrapper = new QueryWrapper<>();
        orderRouterWrapper.eq("SHOP_ORDER_BO", shopOrderBo);
        OrderRouter orderRouter = orderRouterMapper.selectOne(orderRouterWrapper);
        if(orderRouter == null){
            return orderRouter;
        }
        String bo = orderRouter.getBo();
        // 工单工艺路线路线图
        Item item = itemService.getById(orderRouter.getItemBo());
        orderRouter.setItem(item);
        OrderRouterProcess orderRouterProcess = orderRouterProcessService.getById(bo);
        orderRouter.setOrderRouterProcess(orderRouterProcess);

        // 自定义数据
        List<CustomDataAndValVo> customDataAndValVos = customDataValService.selectCustomDataAndValByBoAndDataType(orderRouter.getSite(),
                orderRouter.getBo(), CustomDataTypeEnum.ROUTER.getDataType());
        orderRouter.setCustomDataAndValVoList(customDataAndValVos);
        return orderRouter;
    }

    /**
     * 获取工单工艺路线信息
     */
    @Override
    public OrderRouter getOrderRouterById(String bo) throws CommonException {
        OrderRouter orderRouter = getById(bo);
        if (null == orderRouter) {
            return null;
        }

        // 工单工艺路线路线图
        Item item = itemService.getById(orderRouter.getItemBo());
        orderRouter.setItem(item);
        OrderRouterProcess orderRouterProcess = orderRouterProcessService.getById(bo);
        orderRouter.setOrderRouterProcess(orderRouterProcess);

        // 自定义数据
        List<CustomDataAndValVo> customDataAndValVos = customDataValService.selectCustomDataAndValByBoAndDataType(orderRouter.getSite(),
                orderRouter.getBo(), CustomDataTypeEnum.ROUTER.getDataType());
        orderRouter.setCustomDataAndValVoList(customDataAndValVos);
        return orderRouter;
    }

    /**
     * 保存工单工艺路线
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public boolean saveOrderRouter(OrderRouterVo orderRouterVo) throws CommonException {
        String site = orderRouterVo.getSite();
        if(site == null || "".equals(site)){
            site = UserUtils.getSite();
        }
        String shopOrderBo = orderRouterVo.getShopOrderBo();
        if(shopOrderBo == null || "".equals(shopOrderBo)){
            // throw new Exception(String.format("工单工艺路线保存是（shopOrderBo）不能为空！", site, orderRouterVo.getShopOrderBo()));
            throw new CommonException("工单工艺路线保存是（shopOrderBo）不能为空！", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        String routerBo = orderRouterVo.getBo();
        if(routerBo == null || "".equals(routerBo)){
            // OrderRouter orderRouterObj = getOrderRouter(shopOrderBo);
            /**
             * 保存工艺路线副本
             */
            ShopOrderHandleBO shopOrderHandleBO = new ShopOrderHandleBO(shopOrderBo);
            ShopOrderTwoFullVo shopOrderTwoFullVo = shopOrderTwoService.getShopTwoFullOrder(shopOrderHandleBO);
            if(shopOrderTwoFullVo == null){
                // throw new Exception(String.format("工单工艺路线保存时，根据（shopOrderBo）未查到工单信息！", site, shopOrderBo));
                throw new CommonException("工单工艺路线保存时，根据（shopOrderBo）未查到工单信息！", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            // 产品检验项副本
            String itemBo = shopOrderTwoFullVo.getItemBo(); // 物料ID
            String itemCode = shopOrderTwoFullVo.getItemCode();// 物料编码
            String itemVersion = shopOrderTwoFullVo.getItemVersion();
            if(itemBo == null){
                if(itemCode != null && !"".equals(itemCode)){
                    ItemHandleBO itemHandleBO = new ItemHandleBO(UserUtils.getSite(),itemCode,itemVersion);
                    itemBo = itemHandleBO.getBo();
                }else{
                    throw new CommonException("物料Bo或物料编码不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }
            }

            String shopOrderType = shopOrderTwoFullVo.getShopOrderType();// 工单类型
            String productionLineCode = shopOrderTwoFullVo.getProductionLineCode();// 产线编码Code

            if(itemCode == null || "".equals(itemCode)){
                throw new CommonException("物料编码（itemCode）不能为空！", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }else if(shopOrderType == null || "".equals(shopOrderType)){
                throw new CommonException("工单类型（shopOrderType）不能为空！", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }else if(productionLineCode == null || "".equals(productionLineCode)){
                throw new CommonException("产线编码（productionLineCode）不能为空！", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }

            // 工艺路线的设置表，从上到下依次是产品、产品组、产线工艺路线
            /*process_route_fit_product 产品工艺路线设置表-中间表
            process_route_fit_product_group 产品组工艺路线设置
            process_route_fit_production_line 产线工艺路线设置表*/

            String routerCode = shopOrderTwoFullVo.getRouter();
            String routerVersion = shopOrderTwoFullVo.getRouterVersion();
            // productService; //产品工艺路线设置
            ProcessRouteFitProduct processRouteFitProduct = new ProcessRouteFitProduct();
            processRouteFitProduct.setItemCode(itemCode);
            processRouteFitProduct.setOrderType(shopOrderType);
            List<ProcessRouteFitProduct> processRouteFitProductList = productService.getProductRouteFitProduct(processRouteFitProduct);
            if(processRouteFitProductList != null && processRouteFitProductList.size() == 1){
                processRouteFitProduct = processRouteFitProductList.get(0);
                routerCode = processRouteFitProduct.getRouteCode();
                // routerVersion = processRouteFitProduct.getRouterVersion();
            }else if(processRouteFitProductList == null || processRouteFitProductList.size() < 1){
                // productGroupService; //产品组工艺路线设置
                ProcessRouteFitProductGroup processRouteFitProductGroup = new ProcessRouteFitProductGroup();
                ItemHandleBO itemRouterHandleBO = new ItemHandleBO(itemBo);
                List<String> itemRouterGroupStrList = itemGroupService.getAssignedItemGroupListBySiteAndItemBO(itemRouterHandleBO);
                if(itemRouterGroupStrList != null && itemRouterGroupStrList.size() == 1){
                    String itemRouterGroupStr = itemRouterGroupStrList.get(0);
                    // ItemGroupHandleBO itemRouterGroupHandleBO = new ItemGroupHandleBO(UserUtils.getSite(),itemRouterGroupStr);
                    // processRouteFitProductGroup.setItemGroupBo(itemRouterGroupHandleBO.getBo()); // 产品组编码BO
                    processRouteFitProductGroup.setItemGroup(itemRouterGroupStr); // 工艺路线组编码
                    processRouteFitProductGroup.setOrderType(shopOrderType);
                    List<ProcessRouteFitProductGroup> processRouteFitProductGroupList = productGroupService.getProcessRouteFitProductGroup(processRouteFitProductGroup);
                    if(processRouteFitProductGroupList != null && processRouteFitProductGroupList.size() == 1){
                        processRouteFitProductGroup = processRouteFitProductGroupList.get(0);
                        routerCode = processRouteFitProductGroup.getRouteCode();
                        // routerVersion = processRouteFitProductGroup.getRouterVersion();
                    }else if(processRouteFitProductGroupList == null || processRouteFitProductGroupList.size() < 1){
                        // productionLineService; //产线工艺路线设置
                        ProcessRouteFitProductionLine processRouteFitProductionLine = new ProcessRouteFitProductionLine();
                        processRouteFitProductionLine.setProductLine(productionLineCode);
                        processRouteFitProductionLine.setOrderType(shopOrderType);
                        List<ProcessRouteFitProductionLine> processRouteFitProductionLineList = productionLineService.getProcessRouteFitProductionLine(processRouteFitProductionLine);
                        if(processRouteFitProductionLineList != null && processRouteFitProductionLineList.size() == 1){
                            processRouteFitProductionLine = processRouteFitProductionLineList.get(0);
                            routerCode = processRouteFitProductionLine.getRouteCode();
                            // routerVersion = processRouteFitProductionLine.getRouterVersion();
                        }else if(processRouteFitProductionLineList == null || processRouteFitProductionLineList.size() < 1){
                            throw new CommonException("未能找到产品、产品组、产线工艺路线设置，请前往配置！", CommonExceptionDefinition.VERIFY_EXCEPTION);
                        }else{
                            throw new CommonException("根据产品工艺路线设置，匹配出多条产线工艺路线！", CommonExceptionDefinition.VERIFY_EXCEPTION);
                        }
                    }else{
                        throw new CommonException("根据产品工艺路线设置，匹配出多条产品组工艺路线！", CommonExceptionDefinition.VERIFY_EXCEPTION);
                    }
                }else if(itemRouterGroupStrList == null || itemRouterGroupStrList.size() < 1){
                    // productionLineService; //产线工艺路线设置
                    ProcessRouteFitProductionLine processRouteFitProductionLine = new ProcessRouteFitProductionLine();
                    processRouteFitProductionLine.setProductLine(productionLineCode);
                    processRouteFitProductionLine.setOrderType(shopOrderType);
                    List<ProcessRouteFitProductionLine> processRouteFitProductionLineList = productionLineService.getProcessRouteFitProductionLine(processRouteFitProductionLine);
                    if(processRouteFitProductionLineList != null && processRouteFitProductionLineList.size() == 1){
                        processRouteFitProductionLine = processRouteFitProductionLineList.get(0);
                        routerCode = processRouteFitProductionLine.getRouteCode();
                        // routerVersion = processRouteFitProductionLine.getRouterVersion();
                    }else if(processRouteFitProductionLineList == null || processRouteFitProductionLineList.size() < 1){
                        throw new CommonException("未能找到产品、产品组、产线工艺路线设置，请前往配置！", CommonExceptionDefinition.VERIFY_EXCEPTION);
                    }else{
                        throw new CommonException("根据产品工艺路线设置，匹配出多条产线工艺路线！", CommonExceptionDefinition.VERIFY_EXCEPTION);
                    }
                }else{
                    throw new CommonException("根据产品工艺路线设置，有多条产品组！", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }
            }else{
                throw new CommonException("根据产品工艺路线设置，匹配出多条产品工艺路线！", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            // 保存工单工艺路线副本
            // OrderRouterVo orderRouterVo = new OrderRouterVo();
            Router router = new Router();
            router = routerService.getRouterCode(routerCode);
            /*if(routerVersion == null || "".equals(routerVersion) || "null".equals(routerVersion)){
                router = routerService.getRouterCode(routerCode);
            }else{
                RouterHandleBO routerHandleBO = new RouterHandleBO(UserUtils.getSite(),routerCode,routerVersion);
                router = routerService.getRouter(routerHandleBO.getBo());
            }*/
            if(router == null){
                throw new CommonException("工单未匹配到工艺路线！！", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            BeanUtils.copyProperties(router, orderRouterVo);
            orderRouterVo.setBo(routerBo);
            orderRouterVo.setProcessInfo(router.getRouterProcess().getProcessInfo());
        }else{
            OrderRouter orderRouterObj = getOrderRouterById(routerBo);
            if(orderRouterObj == null || "".equals(orderRouterObj)){
                throw new CommonException("("+routerBo+")的工单工艺路线不存在！", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
        }

        if(orderRouterVo.getRouter() == null || "".equals(orderRouterVo.getRouter())){
            throw new CommonException("工单工艺路线保存时（router）不能为空！", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }else if(orderRouterVo.getVersion() == null || "".equals(orderRouterVo.getVersion())){
            throw new CommonException("工单工艺路线保存时（version）不能为空！", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        OrderRouter orderRouter = new OrderRouter(site, orderRouterVo.getRouter(), orderRouterVo.getVersion(),shopOrderBo);
        orderRouter.setSite(site);
        orderRouter.setRouter(orderRouterVo.getRouter());
        orderRouter.setRouterType(orderRouterVo.getRouterType());
        orderRouter.setRouterName(orderRouterVo.getRouterName());
        orderRouter.setRouterDesc(orderRouterVo.getRouterDesc());
        orderRouter.setState(orderRouterVo.getState());
        orderRouter.setVersion(orderRouterVo.getVersion());
        orderRouter.setItemBo(orderRouterVo.getItemBo());
        orderRouter.setShopOrderBo(orderRouterVo.getShopOrderBo());

        if (null != orderRouterVo.getProcessInfo()) {
            OrderRouterProcess routerProcess = new OrderRouterProcess(orderRouter.getBo());
            routerProcess.setProcessInfo(orderRouterVo.getProcessInfo());
            routerProcess.setSite(site);
            orderRouter.setOrderRouterProcess(routerProcess);
        }
        orderRouter.setCustomDataValVoList(orderRouterVo.getCustomDataValVoList());

        boolean isUpdate = false; //是否更新操作
        LambdaQueryWrapper<OrderRouter> query = new QueryWrapper<OrderRouter>().lambda()
                .and(i -> i.eq(OrderRouter::getRouter, orderRouter.getRouter())
                        .eq(OrderRouter::getSite, orderRouter.getSite())
                        .eq(OrderRouter::getVersion, orderRouter.getVersion())
                        .eq(OrderRouter::getShopOrderBo, orderRouter.getShopOrderBo()));
        if (orderRouterVo.getBo() == null || "".equals(orderRouterVo.getBo())) {
            List<OrderRouter> queryRouter = orderRouterMapper.selectList(query);
            if (queryRouter != null && queryRouter.size() > 0) {
                // throw new Exception(String.format("工厂%s中已存在工单工艺路线%s", orderRouter.getSite(), orderRouter.getRouter()));
                throw new CommonException("工厂%s中已存在工单工艺路线%s", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            orderRouter.setCreateDate(DateTime.now());
            orderRouter.setCreateUser(UserUtils.getUser());
            int i = orderRouterMapper.insert(orderRouter);
            if(i < 1){
                return false;
            }
        } else {
            query.ne(OrderRouter::getBo, orderRouterVo.getBo());
            List<OrderRouter> queryRouter = orderRouterMapper.selectList(query);
            if (queryRouter != null && queryRouter.size() > 0) {
                // throw new Exception(String.format("工厂%s中已存在工单工艺路线%s", orderRouter.getSite(), orderRouter.getRouter()));
                throw new CommonException("工厂%s中已存在工单工艺路线%s", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            orderRouter.setModifyDate(DateTime.now());
            orderRouter.setModifyUser(UserUtils.getUser());
            int u = orderRouterMapper.updateById(orderRouter);
            if(u < 1){
                return false;
            }
            isUpdate = true;
        }

        OrderRouterProcess routerProcess = orderRouter.getOrderRouterProcess();
        if (null != routerProcess) {
            routerProcess.setSite(orderRouter.getSite());
            routerProcess.setRouterBo(orderRouter.getBo());
            if (isUpdate) {
                orderRouterProcessService.updateById(routerProcess);
            } else {
                orderRouterProcessService.save(routerProcess);
            }
        }

        //保存自定义数据
        List<CustomDataValVo> customDataValVoList = orderRouter.getCustomDataValVoList();
        if (CollUtil.isNotEmpty(customDataValVoList)) {
            CustomDataValRequest customDataValRequest = new CustomDataValRequest();
            customDataValRequest.setBo(orderRouter.getBo());
            customDataValRequest.setSite(site);
            customDataValRequest.setCustomDataType(CustomDataTypeEnum.ROUTER.getDataType());
            customDataValRequest.setCustomDataValVoList(customDataValVoList);
            customDataValService.saveCustomDataVal(customDataValRequest);
        }

//        final List<String> snBo = orderRouterVo.getSnBo();
//        if (CollUtil.isNotEmpty(snBo)){
//            snBo.forEach(x-> {
//                final Sn sn = new Sn();
//                sn.setRouterId(orderRouter.getBo());
//                snService.updateById(sn);
//            });
//        }
        return true;
    }

    /**
     * 删除工单工艺路线
     *
     * @param orderRouter 工单工艺路线
     * @throws CommonException 异常
     */
    @Override
    public void deleteOrderRouter(OrderRouter orderRouter) throws CommonException {
        LambdaQueryWrapper<OrderRouter> query = new QueryWrapper<OrderRouter>().lambda()
                .eq(OrderRouter::getBo, orderRouter.getBo());

        //删除工艺路线
        Integer integer = orderRouterMapper.delete(query);

        //删除自定义数据
        customDataValService.deleteCustomDataValByBoAndType(UserUtils.getSite(), orderRouter.getBo(), CustomDataTypeEnum.ROUTER);

    }
}

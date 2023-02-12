package com.itl.mes.pp.provider.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.Assert;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.ProductLineHandleBO;
import com.itl.mes.core.api.bo.ShopOrderHandleBO;
import com.itl.mes.core.api.bo.WorkShopHandleBO;
import com.itl.mes.core.api.constant.OrderStateEnum;
import com.itl.mes.core.api.constant.ProductionOrderStatusEnum;
import com.itl.mes.core.api.dto.ItemPackRuleDetailDto;
import com.itl.mes.core.api.dto.ShopOrderPackRuleSaveDto;
import com.itl.mes.core.api.entity.ConfigItem;
import com.itl.mes.core.api.entity.ProductionOrder;
import com.itl.mes.core.api.entity.ShopOrder;
import com.itl.mes.core.api.entity.ShopOrderPackRuleDetail;
import com.itl.mes.core.api.vo.ItemFullVo;
import com.itl.mes.core.api.vo.ShopOrderFullVo;
import com.itl.mes.core.api.vo.ShopOrderTwoSaveVo;
import com.itl.mes.core.client.service.*;
import com.itl.mes.pp.api.dto.schedule.*;
import com.itl.mes.pp.api.entity.schedule.ScheduleEntity;
import com.itl.mes.pp.api.entity.schedule.ScheduleProductionLineEntity;
import com.itl.mes.pp.api.entity.schedule.ScheduleReceiveEntity;
import com.itl.mes.pp.api.entity.schedule.ScheduleShopOrderEntity;
import com.itl.mes.pp.api.service.ResourcesCalendarService;
import com.itl.mes.pp.api.service.ScheduleService;
import com.itl.mes.pp.provider.config.Constant;
import com.itl.mes.pp.provider.mapper.ScheduleMapper;
import com.itl.mes.pp.provider.mapper.ScheduleProductionLineMapper;
import com.itl.mes.pp.provider.mapper.ScheduleReceiveMapper;
import com.itl.mes.pp.provider.mapper.ScheduleShopOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liuchenghao
 * @date 2020/11/12 9:55
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {


    @Autowired
    ScheduleMapper scheduleMapper;

    /**
     * 工单保存
     */
    @Autowired
    ShopOrderTwoService shopOrderTwoService;

    /**
     * 工艺路线匹配
     */
    @Autowired
    RouterFitService routerFitService;

    /**
     * 包装规则
     */
    @Autowired
    ItemService itemService;

    /**
     * 配置项
     */
    @Autowired
    ConfigItemService configItemService;

    /**
     * 数据字典
     */
    /*@Autowired
    IapDictItemTService iapDictItemService;*/

    /**
     * 订单
     */
    @Autowired
    ProductionOrderService productionOrderService;

    @Autowired
    ScheduleProductionLineMapper scheduleProductionLineMapper;

    @Autowired
    ScheduleShopOrderMapper scheduleShopOrderMapper;

    @Autowired
    ScheduleReceiveMapper scheduleReceiveMapper;

    @Autowired
    ResourcesCalendarService resourcesCalendarService;

    @Autowired
    CodeRuleService codeRuleService;

    @Override
    public List<ScheduleEntity> getByScheduleShopOrder(String shopOrderBO) {
        QueryWrapper<ScheduleShopOrderEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ScheduleShopOrderEntity::getShopOrderBo, shopOrderBO);
        List<ScheduleShopOrderEntity> scheduleShopOrderEntities = scheduleShopOrderMapper.selectList(queryWrapper);
        if (CollectionUtil.isEmpty(scheduleShopOrderEntities)) {
            return new ArrayList<ScheduleEntity>();
        }
        List<String> collect = scheduleShopOrderEntities.stream().map(ScheduleShopOrderEntity::getScheduleBo).collect(Collectors.toList());
        QueryWrapper<ScheduleEntity> query = new QueryWrapper<>();
        query.lambda().in(ScheduleEntity::getBo, collect);
        List<ScheduleEntity> scheduleEntityList = scheduleMapper.selectList(query);
        return scheduleEntityList;
    }

    @Override
    public IPage<ScheduleRespDTO> findList(ScheduleQueryDTO scheduleQueryDTO) {

        if (ObjectUtil.isEmpty(scheduleQueryDTO.getPage())) {
            scheduleQueryDTO.setPage(new Page(0, 10));
        }
        scheduleQueryDTO.setSite(UserUtils.getSite());
        return scheduleMapper.findList(scheduleQueryDTO.getPage(), scheduleQueryDTO);
    }

    @Override
    public IPage<ScheduleRespTwoDTO> findTwoList(ScheduleQueryTwoDTO scheduleQueryTwoDTO) {

        if (ObjectUtil.isEmpty(scheduleQueryTwoDTO.getPage())) {
            scheduleQueryTwoDTO.setPage(new Page(0, 10));
        }
        scheduleQueryTwoDTO.setSite(UserUtils.getSite());
        //scheduleQueryTwoDTO.setSite("1040");
        return scheduleMapper.findTwoList(scheduleQueryTwoDTO.getPage(), scheduleQueryTwoDTO);
    }

    @Override
    public Page<ScheduleRespTwoDTO> findTwoPage(Map<String,Object> params) {
        Page<ScheduleRespTwoDTO> page = new QueryPage<>(params);
        params.put("site", UserUtils.getSite());
        List<ScheduleRespTwoDTO> list = scheduleMapper.findTwoPage(page, params);
        return page.setRecords(list);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(ScheduleSaveDTO scheduleSaveDTO) throws CommonException {

        ScheduleEntity scheduleEntity = new ScheduleEntity();
        BeanUtil.copyProperties(scheduleSaveDTO, scheduleEntity);
        // 修改
        if (StrUtil.isNotEmpty(scheduleSaveDTO.getBo())) {
            // 修改排程状态
            if (Constant.ScheduleState.RECEIVE.getValue() == scheduleSaveDTO.getState()) {
                throw new CommonException("接收状态不允许修改", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            scheduleEntity.setModifyUser(UserUtils.getCurrentUser().getUserName());
            scheduleEntity.setModifyDate(new Date());
            scheduleMapper.updateById(scheduleEntity);
            // 清除排程下的产线信息
            QueryWrapper<ScheduleProductionLineEntity> productionLineEntityQueryWrapper = new QueryWrapper();
            productionLineEntityQueryWrapper.eq("schedule_bo", scheduleEntity.getBo());
            scheduleProductionLineMapper.delete(productionLineEntityQueryWrapper);
            // 保存排程分配的产线信息
            scheduleSaveDTO.getScheduleProductionLineSaveDTOList().forEach(scheduleProductionLineSaveDTO -> {
                ScheduleProductionLineEntity scheduleProductionLineEntity = new ScheduleProductionLineEntity();
                BeanUtil.copyProperties(scheduleProductionLineSaveDTO, scheduleProductionLineEntity);
                scheduleProductionLineEntity.setBo(null);
                scheduleProductionLineEntity.setScheduleBo(scheduleSaveDTO.getBo());
                scheduleProductionLineMapper.insert(scheduleProductionLineEntity);
            });
            // 删除排程的工单信息
            QueryWrapper<ScheduleShopOrderEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("schedule_bo", scheduleSaveDTO.getBo());
            scheduleShopOrderMapper.delete(queryWrapper);
            // 保存排程的工单信息
            scheduleSaveDTO.getShopOrders().forEach(shopOrder -> {
                ScheduleShopOrderEntity scheduleShopOrderEntity = new ScheduleShopOrderEntity();
                scheduleShopOrderEntity.setScheduleBo(scheduleSaveDTO.getBo());
                scheduleShopOrderEntity.setShopOrderBo(shopOrder);
                scheduleShopOrderMapper.insert(scheduleShopOrderEntity);
            });

        } else {
            scheduleEntity.setCreateUser(UserUtils.getCurrentUser().getUserName());
            scheduleEntity.setState(Constant.ScheduleState.CREATE.getValue());
            scheduleMapper.insert(scheduleEntity);
            scheduleSaveDTO.getScheduleProductionLineSaveDTOList().forEach(scheduleProductionLineSaveDTO -> {
                ScheduleProductionLineEntity scheduleProductionLineEntity = new ScheduleProductionLineEntity();
                BeanUtil.copyProperties(scheduleProductionLineSaveDTO, scheduleProductionLineEntity);
                scheduleProductionLineEntity.setScheduleBo(scheduleEntity.getBo());
                scheduleProductionLineMapper.insert(scheduleProductionLineEntity);
            });
            scheduleSaveDTO.getShopOrders().forEach(shopOrder -> {
                ScheduleShopOrderEntity scheduleShopOrderEntity = new ScheduleShopOrderEntity();
                scheduleShopOrderEntity.setScheduleBo(scheduleEntity.getBo());
                scheduleShopOrderEntity.setShopOrderBo(shopOrder);
                scheduleShopOrderMapper.insert(scheduleShopOrderEntity);
            });
        }
    }


    // 优化2
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveTwo(List<ScheduleSaveTwoDTO> scheduleSaveTwoDTOList) throws CommonException {
        for(ScheduleSaveTwoDTO scheduleSaveTwoDTO : scheduleSaveTwoDTOList){
            ScheduleEntity scheduleEntity = new ScheduleEntity();
            BeanUtil.copyProperties(scheduleSaveTwoDTO, scheduleEntity);

            // 设置工厂编号
            scheduleEntity.setSite(UserUtils.getSite());

            // 编码配置规则类型（排程编码）
            String codeRuleType = scheduleSaveTwoDTO.getCodeRuleType();
            if(codeRuleType == null || "".equals(codeRuleType)){
                ConfigItem configItem = new ConfigItem();
                configItem.setItemCode("NewSchedule");
                configItem.setConfigItemKey("codeRuleType");
                List<ConfigItem> configItemList = configItemService.query(configItem).getData();
                if(configItemList == null || "".equals(configItemList) || configItemList.size() <= 0){
                    throw new CommonException("未找到排程号编码规则！", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }else if(configItemList.size() > 1){
                    throw new CommonException("找到多条排程号编码规则！", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }
                codeRuleType = configItemList.get(0).getConfigItemValue();
            }

            ResponseData<String> scheduleData = codeRuleService.generatorNextNumber(codeRuleType);
            String scheduleNo = scheduleData.getData();
            if(scheduleNo == null){
                throw new CommonException(scheduleData.getMsg(), CommonExceptionDefinition.VERIFY_EXCEPTION);
            }

            // 订单数量与已排程数量校验
            String orderId = scheduleSaveTwoDTO.getOrderId();
            if(orderId == null || "".equals(orderId)){
                throw new CommonException("订单ID不能为空！", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            String orderNo = scheduleSaveTwoDTO.getOrderNo();
            if(orderNo == null || "".equals(orderNo)){
                throw new CommonException("订单编号不能为空！", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }

            // 校验对象
            List<ScheduleProductionLineSaveTwoDTO> scheduleProductionLineSaveTwoDTOList = scheduleSaveTwoDTO.getScheduleProductionLineSaveTwoDTOList();
            if(scheduleProductionLineSaveTwoDTOList == null || scheduleProductionLineSaveTwoDTOList.size() < 1){
                throw new CommonException("产能分配对象不能为空！", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }

            // 产线排程数量之和，进行校验使用：1
            int schedulePlineQuantity = 0;

            // 修改
            if (StrUtil.isNotEmpty(scheduleSaveTwoDTO.getBo())) {

                if (Constant.ScheduleState.RELEASE.getValue() == scheduleSaveTwoDTO.getState()) {
                    throw new CommonException("下达状态不允许修改", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }
                if (Constant.ScheduleState.RECEIVE.getValue() == scheduleSaveTwoDTO.getState()) {
                    throw new CommonException("接收状态不允许修改", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }
                scheduleEntity.setModifyUser(UserUtils.getCurrentUser().getUserName());
                scheduleEntity.setModifyDate(new Date());
                scheduleMapper.updateById(scheduleEntity);
                /*QueryWrapper<ScheduleProductionLineEntity> productionLineEntityQueryWrapper = new QueryWrapper();
                productionLineEntityQueryWrapper.eq("schedule_bo", scheduleEntity.getBo());
                scheduleProductionLineMapper.delete(productionLineEntityQueryWrapper);*/
                for(int i=0;i < scheduleProductionLineSaveTwoDTOList.size();i++){
                    ScheduleProductionLineSaveTwoDTO scheduleProductionLineSaveTwoDTO = scheduleProductionLineSaveTwoDTOList.get(i);
                    if(scheduleProductionLineSaveTwoDTO.getBo() == null || "".equals(scheduleProductionLineSaveTwoDTO.getBo())){
                        throw new CommonException("产能分配排程ID不能为空！", CommonExceptionDefinition.VERIFY_EXCEPTION);
                    }
                    ScheduleProductionLineEntity scheduleProductionLineEntity = scheduleProductionLineMapper.selectById(scheduleProductionLineSaveTwoDTO.getBo());
                    BigDecimal plineQuantity = scheduleProductionLineEntity.getQuantity(); // 修改前的排程数量
                    BeanUtil.copyProperties(scheduleProductionLineSaveTwoDTO, scheduleProductionLineEntity);

                    // 车间信息
                    WorkShopHandleBO workShopHandleBO = new WorkShopHandleBO(UserUtils.getSite(),scheduleProductionLineEntity.getWorkShop());
                    scheduleProductionLineEntity.setWorkShopBo(workShopHandleBO.getBo()); // 车间ID
                    scheduleProductionLineEntity.setBo(scheduleProductionLineSaveTwoDTO.getBo());
                    scheduleProductionLineEntity.setScheduleBo(scheduleSaveTwoDTO.getBo());
                    scheduleProductionLineEntity.setModifyDate(new Date());
                    scheduleProductionLineEntity.setModifyUser(UserUtils.getCurrentUser().getUserName());
                    // 设置工单号
                    /*int targetIntNum = 1 + i;
                    String codeFormat = "%02d";
                    String str = String.format(codeFormat,targetIntNum);
                    String shopOrderNewStr = orderNo+"-"+str;
                    scheduleProductionLineEntity.setShopOrder(shopOrderNewStr);
                    // 设置任务状态（未下达）
                    scheduleProductionLineEntity.setState("ADV_S1");*/

                    // 产线排程数量之和，进行校验使用：2
                    if(scheduleProductionLineSaveTwoDTO.getQuantity() <= 0){
                        throw new CommonException("产能分配数量不能为小于1", CommonExceptionDefinition.VERIFY_EXCEPTION);
                    }
                    schedulePlineQuantity = schedulePlineQuantity + scheduleProductionLineSaveTwoDTO.getQuantity();
                    if(schedulePlineQuantity != plineQuantity.intValue()){
                        throw new CommonException("产能分配数量不能修改！", CommonExceptionDefinition.VERIFY_EXCEPTION);
                    }

                    // 更新订单排程数量
                    /*int scheduleQuantity = scheduleSaveTwoDTO.getQuantity().intValue();
                    if(scheduleQuantity <= 0 || "".equals(scheduleQuantity)){
                        throw new CommonException("排程数量不能为0或空！", CommonExceptionDefinition.VERIFY_EXCEPTION);
                    }
                    ProductionOrder productionOrder = productionOrderService.getDetailById(orderId).getData();
                    int orderQuantity = productionOrder.getQuantity(); // 订单数量
                    int scheduleQuantityNew = productionOrder.getScheduledQuantity() + scheduleQuantity; // 已排产数量
                    if(orderQuantity < scheduleQuantityNew){
                        throw new CommonException("排程数量已大于订单数量！", CommonExceptionDefinition.VERIFY_EXCEPTION);
                    }
                    // 更新订单已排产数量
                    productionOrder.setId(scheduleSaveTwoDTO.getOrderId());
                    productionOrder.setScheduledQuantity(scheduleQuantityNew);
                    productionOrderService.saveOrder(productionOrder);*/

                    scheduleProductionLineMapper.updateById(scheduleProductionLineEntity);
                }
                /*QueryWrapper<ScheduleShopOrderEntity> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("schedule_bo", scheduleSaveTwoDTO.getBo());
                scheduleShopOrderMapper.delete(queryWrapper);
                scheduleSaveTwoDTO.getShopOrders().forEach(shopOrder -> {
                    ScheduleShopOrderEntity scheduleShopOrderEntity = new ScheduleShopOrderEntity();
                    scheduleShopOrderEntity.setScheduleBo(scheduleSaveTwoDTO.getBo());
                    scheduleShopOrderEntity.setShopOrderBo(shopOrder);
                    scheduleShopOrderMapper.insert(scheduleShopOrderEntity);
                });*/

            } else { // 新增
                scheduleEntity.setCreateUser(UserUtils.getCurrentUser().getUserName());
                scheduleEntity.setState(Constant.ScheduleState.CREATE.getValue());
                //scheduleEntity.setBo(scheduleBo);
                scheduleEntity.setScheduleNo(scheduleNo);
                scheduleMapper.insert(scheduleEntity);

                // 更新订单排程数量
                int scheduleQuantity = scheduleSaveTwoDTO.getQuantity().intValue();
                if(scheduleQuantity <= 0 || "".equals(scheduleQuantity)){
                    throw new CommonException("排程数量不能为0或空！", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }
                ProductionOrder productionOrder = productionOrderService.getDetailById(orderId).getData();
                int orderQuantity = productionOrder.getQuantity(); // 订单数量
                int scheduleQuantityNew = productionOrder.getScheduledQuantity() + scheduleQuantity; // 已排产数量
                if(orderQuantity < scheduleQuantityNew){
                    throw new CommonException("排程数量已大于订单数量！", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }
                // 更新订单已排产数量
                productionOrder.setId(scheduleSaveTwoDTO.getOrderId());
                productionOrder.setScheduledQuantity(scheduleQuantityNew);
                // productionOrder.setStatus(ProductionOrderStatusEnum.ASSIGNED.getCode()); //更新订单状态
                productionOrderService.saveOrder(productionOrder);

                int targetIntNumF = 1;
                for(int i=0;i < scheduleProductionLineSaveTwoDTOList.size();i++){
                    ScheduleProductionLineSaveTwoDTO scheduleProductionLineSaveTwoDTO = scheduleProductionLineSaveTwoDTOList.get(i);
                    ScheduleProductionLineEntity scheduleProductionLineEntity = new ScheduleProductionLineEntity();
                    BeanUtil.copyProperties(scheduleProductionLineSaveTwoDTO, scheduleProductionLineEntity);
                    // 车间信息
                    WorkShopHandleBO workShopHandleBO = new WorkShopHandleBO(UserUtils.getSite(),scheduleProductionLineEntity.getWorkShop());
                    scheduleProductionLineEntity.setWorkShopBo(workShopHandleBO.getBo()); // 车间ID
                    scheduleProductionLineEntity.setScheduleBo(scheduleEntity.getBo());
                    scheduleProductionLineEntity.setStationBo("");

                    /**
                     * 生成新工单号
                     */
                    // 数字前面补0：("%02d",9)
                    //int length = targetString.length();
                    //String codeFormat = "%0"+String.valueOf(length)+"d";//%03d
                    //String str = String.format(codeFormat,targetIntNum);
                    String shopOrderNewStr = "";
                    Boolean targetIntNumBl = true;
                    String codeFormat = "%02d";
                    for(int targetIntNum = targetIntNumF;targetIntNumBl;targetIntNum++){
                        // 已存在加一
                        String str = String.format(codeFormat,targetIntNum);
                        shopOrderNewStr = orderNo+"-"+str;
                        // ShopOrderHandleBO shopOrderHandleBO2 = new ShopOrderHandleBO(UserUtils.getSite(),shopOrderNewStr);
                        ResponseData<ShopOrderFullVo> shopOrder = shopOrderTwoService.getShopOrder(shopOrderNewStr);
                        if(shopOrder == null || shopOrder.getData() == null){
                            ShopOrderHandleBO shopOrderHandleBO = new ShopOrderHandleBO(UserUtils.getSite(),shopOrderNewStr);
                            List<ScheduleEntity> scheduleEntityList = getByScheduleShopOrder(shopOrderHandleBO.getBo());
                            if(scheduleEntityList == null || scheduleEntityList.size() < 1){
                                scheduleProductionLineEntity.setShopOrder(shopOrderNewStr);
                                // 设置任务状态（未下达）
                                scheduleProductionLineEntity.setState("ADV_S1");
                                targetIntNumF = targetIntNum+1;
                                targetIntNumBl = false;
                            }

                        }
                    }

                    /*// 设置工单号
                    int targetIntNum = 1 + i;
                    String codeFormat = "%02d";
                    String str = String.format(codeFormat,targetIntNum);
                    String shopOrderNewStr = orderNo+"-"+str;
                    scheduleProductionLineEntity.setShopOrder(shopOrderNewStr);
                    // 设置任务状态（未下达）
                    scheduleProductionLineEntity.setState("ADV_S1");*/

                    // 产线排程数量之和，进行校验使用：2
                    if(scheduleProductionLineSaveTwoDTO.getQuantity() <= 0){
                        throw new CommonException("产能分配数量不能为小于1", CommonExceptionDefinition.VERIFY_EXCEPTION);
                    }
                    schedulePlineQuantity = schedulePlineQuantity + scheduleProductionLineSaveTwoDTO.getQuantity();

                    scheduleProductionLineMapper.insert(scheduleProductionLineEntity);
                }

                // 产线排程数量之和与排程数量校验，进行校验使用：3
                if(scheduleQuantity != schedulePlineQuantity){
                    throw new CommonException("排程数量与产能分配不符，请确认数量是否准确！", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }
                /*scheduleSaveTwoDTO.getShopOrders().forEach(shopOrder -> {
                    ScheduleShopOrderEntity scheduleShopOrderEntity = new ScheduleShopOrderEntity();
                    scheduleShopOrderEntity.setScheduleBo(scheduleEntity.getBo());
                    scheduleShopOrderEntity.setShopOrderBo(shopOrder);
                    scheduleShopOrderMapper.insert(scheduleShopOrderEntity);
                });*/
            }

        }

    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<String> ids) throws CommonException {

        for (String id : ids) {
            QueryWrapper<ScheduleEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.ne("state", 1);
            queryWrapper.eq("bo", id);
            Integer count = scheduleMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new CommonException("有排程数据已被下达，无法删除，请检查数据", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }

            // 更新订单数量
            ScheduleRespTwoDTO scheduleRespTwoDTO = scheduleMapper.queryTwoById(id);
            String orderId = scheduleRespTwoDTO.getOrderId();
            if(orderId == null || "".equals(orderId)){
                throw new CommonException("订单ID不能为空！", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            if(scheduleRespTwoDTO.getQuantity() == null || "".equals(scheduleRespTwoDTO.getQuantity())){
                throw new CommonException("排程数量不能为空！", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            int scheduleQuantity = scheduleRespTwoDTO.getQuantity().intValue();
            ProductionOrder productionOrder = productionOrderService.getDetailById(orderId).getData();
            int orderQuantity = productionOrder.getQuantity(); // 订单数量
            int scheduleQuantityNew = productionOrder.getScheduledQuantity() - scheduleQuantity; // 已排产数量
            if(orderQuantity < scheduleQuantityNew){
                throw new CommonException("排程数量已大于订单数量！", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            // 更新订单已排产数量
            productionOrder.setId(orderId);
            productionOrder.setScheduledQuantity(scheduleQuantityNew);
            productionOrderService.saveOrder(productionOrder);

            QueryWrapper<ScheduleProductionLineEntity> deleteProductionLineWrapper = new QueryWrapper<>();
            deleteProductionLineWrapper.eq("bo", id);
            QueryWrapper<ScheduleShopOrderEntity> deleteShopOrderWrapper = new QueryWrapper<>();
            deleteShopOrderWrapper.eq("bo", id);
            scheduleMapper.deleteById(id);
            scheduleProductionLineMapper.delete(deleteProductionLineWrapper);
            scheduleShopOrderMapper.delete(deleteShopOrderWrapper);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deletePline(List<String> ids) throws CommonException {

        for (String id : ids) {
            // 方式2
            ScheduleProductionLineEntity scheduleProductionLineEntity = scheduleProductionLineMapper.selectById(id);
            if(scheduleProductionLineEntity == null || "".equals(scheduleProductionLineEntity)){
                throw new CommonException("未找到对应的产线排程信息，请确认ID是否正确！", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            if (scheduleProductionLineEntity.getState() == "ADV_S2" || "ADV_S2".equals(scheduleProductionLineEntity.getState())) {
                throw new CommonException("有排程数据已被下达，无法删除，请检查数据", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }

            // 订单数量与已排程数量校验
            ScheduleSaveTwoDTO scheduleSaveTwoDTO = scheduleMapper.findTwoById(id);
            if(scheduleSaveTwoDTO == null || "".equals(scheduleSaveTwoDTO)){
                throw new CommonException("无法找到对应的排程信息！", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }

            String orderId = scheduleSaveTwoDTO.getOrderId();
            if(orderId == null || "".equals(orderId)){
                throw new CommonException("订单ID不能为空！", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            if(scheduleSaveTwoDTO.getQuantity() == null || "".equals(scheduleSaveTwoDTO.getQuantity())){
                throw new CommonException("排程数量不能为空！", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            int scheduleQuantity = scheduleSaveTwoDTO.getQuantity().intValue();
            if(scheduleProductionLineEntity.getQuantity() == null || "".equals(scheduleProductionLineEntity.getQuantity())){
                throw new CommonException("产线排程数量不能为空！", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            int schedulePlineQuantity = scheduleProductionLineEntity.getQuantity().intValue();

            ProductionOrder productionOrder = productionOrderService.getDetailById(orderId).getData();
            int orderQuantity = productionOrder.getQuantity(); // 订单数量
            int scheduleQuantityNew = productionOrder.getScheduledQuantity() - schedulePlineQuantity; // 已排产数量
            if(orderQuantity < scheduleQuantityNew){
                throw new CommonException("排程数量已大于订单数量！", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            // 更新订单已排产数量
            productionOrder.setId(scheduleSaveTwoDTO.getOrderId());
            productionOrder.setScheduledQuantity(scheduleQuantityNew);
            productionOrderService.saveOrder(productionOrder);

            // 校验排程产线是否有数据，无则删除对应排程
            String scheduleBo = scheduleProductionLineEntity.getScheduleBo();
            if(scheduleBo != null || !"".equals(scheduleBo)){
                QueryWrapper<ScheduleProductionLineEntity> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("SCHEDULE_BO", scheduleBo);
                Integer count = scheduleProductionLineMapper.selectCount(queryWrapper);
                if (count <= 1) {
                    scheduleMapper.deleteById(scheduleBo);
                }
                // 删除排程产线
                int i = scheduleProductionLineMapper.deleteById(id);
                if(i <= 0){
                    return;
                }
            }else{
                throw new CommonException("未找到排程ID，无法删除，请检查数据", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }

        }
    }

    @Override
    public ScheduleDetailRespDTO findById(String id) {

        ScheduleDetailRespDTO scheduleRespDTO = scheduleMapper.findById(id);
        List<String> shopOrderBos = new ArrayList<>();
        shopOrderBos.add(scheduleRespDTO.getShopOrderBo());
        scheduleRespDTO.setShopOrderBos(shopOrderBos);
        return scheduleRespDTO;
    }

    /**
     * ScheduleSaveTwoDTO
     * @param id
     * @return
     */
    @Override
    public ScheduleRespTwoDTO queryTwoById(String id) {
        ScheduleRespTwoDTO scheduleRespTwoDTO = scheduleMapper.queryTwoById(id);
        List<String> shopOrderBos = new ArrayList<>();
        shopOrderBos.add(scheduleRespTwoDTO.getShopOrder());
        scheduleRespTwoDTO.setShopOrders(shopOrderBos);
        return scheduleRespTwoDTO;
    }

    /**
     * 排程编辑是使用
     * ScheduleSaveTwoDTO
     * @param schedulePlineBo
     * @return
     */
    @Override
    public ScheduleSaveTwoDTO findTwoById(String schedulePlineBo) {
        ScheduleSaveTwoDTO scheduleSaveTwoDTO = scheduleMapper.findTwoById(schedulePlineBo);
        if(scheduleSaveTwoDTO == null || "".equals(scheduleSaveTwoDTO)){
            throw new CommonException("找不到对应排程信息!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        // 查询产线分配，并赋值给返回对象
        ScheduleProductionLineQueryTwoDTO scheduleProductionLineQueryTwoDTO = new ScheduleProductionLineQueryTwoDTO();
        //scheduleProductionLineQueryTwoDTO.setScheduleBo(scheduleSaveTwoDTO.getBo());
        scheduleProductionLineQueryTwoDTO.setBo(scheduleSaveTwoDTO.getSchedulePlineBo());
        List<ScheduleProductionLineSaveTwoDTO> scheduleProductionLineSaveTwoDTOList = scheduleProductionLineMapper.findTwoList(scheduleProductionLineQueryTwoDTO);
        scheduleSaveTwoDTO.setScheduleProductionLineSaveTwoDTOList(scheduleProductionLineSaveTwoDTOList);

        List<String> shopOrderBos = new ArrayList<>();
        scheduleSaveTwoDTO.getScheduleProductionLineSaveTwoDTOList().forEach(scheduleProductionLineSaveTwoDTO -> {
            shopOrderBos.add(scheduleProductionLineSaveTwoDTO.getShopOrder());
        });
        scheduleSaveTwoDTO.setShopOrders(shopOrderBos);
        return scheduleSaveTwoDTO;
    }

    @Override
    public ScheduleDetailRespDTO findByIdWithCount(String id) throws CommonException {
        ProductLineHandleBO productLineHandleBO = new ProductLineHandleBO(UserUtils.getSite(), UserUtils.getProductLine());
        ScheduleDetailRespDTO scheduleRespDTO = Optional.ofNullable(scheduleMapper.findByIdWithCount(id, productLineHandleBO.getBo()))
                .map(x -> x.get(0))
                .orElseThrow(() -> new CommonException("未查询到排程!", CommonExceptionDefinition.BASIC_EXCEPTION));
        if (ObjectUtil.isNotNull(scheduleRespDTO)) {
            List<String> shopOrderBos = new ArrayList<>();
            shopOrderBos.add(scheduleRespDTO.getShopOrderBo());
            scheduleRespDTO.setShopOrderBos(shopOrderBos);
        }
        return scheduleRespDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateState(String bo) {

        //排程下达，先更新排程表状态，再将排程表和排程工单关联表和排程产线关联表的数据保存排程派工表
        ScheduleEntity updateEntity = new ScheduleEntity();
        updateEntity.setState(Constant.ScheduleState.RELEASE.getValue());
        updateEntity.setModifyUser(UserUtils.getCurrentUser().getUserName());
        updateEntity.setModifyDate(new Date());
        updateEntity.setBo(bo);
        scheduleMapper.updateById(updateEntity);
        saveScheduleReceive(bo);
    }

    /**
     * 根据产线（工单）排程下达，并新建工单
     * @param schedulePlineBo
     */
    //@Transactional(rollbackFor = Exception.class)
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void updateScheduleState(String schedulePlineBo) throws CommonException {

        //排程下达，先更新排程表状态，再将排程表和排程工单关联表和排程产线关联表的数据保存排程派工表
        ScheduleProductionLineEntity scheduleProductionLineEntity = new ScheduleProductionLineEntity();
        scheduleProductionLineEntity.setState(Constant.ScheduleTwoState.RELEASE.getValue());
        scheduleProductionLineEntity.setModifyUser(UserUtils.getCurrentUser().getUserName());
        scheduleProductionLineEntity.setModifyDate(new Date());
        scheduleProductionLineEntity.setBo(schedulePlineBo);
        scheduleProductionLineMapper.updateById(scheduleProductionLineEntity);
        // 更新排程派工
        saveSchedulePlineReceive(schedulePlineBo);

        // 下达时保存工单
        ShopOrderTwoSaveVo shopOrderTwoSaveVo = getShopFullOrderByScheduleBo(schedulePlineBo);
        ResponseData responseData = shopOrderTwoService.saveShopOrder(shopOrderTwoSaveVo);
        Assert.valid(!responseData.isSuccess(), responseData.getMsg());

        // 更新订单状态
        ProductionOrder productionOrder = new ProductionOrder();
        if(shopOrderTwoSaveVo.getErpOrderBo() == null || "".equals(shopOrderTwoSaveVo.getErpOrderBo())){
            throw new CommonException("更新订单状态时获取的工单ID为空!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        //ResponseData<ProductionOrder> orderResponseData = productionOrderService.getDetailById(shopOrderTwoSaveVo.getErpOrderBo());
        //Assert.valid(!orderResponseData.isSuccess(), orderResponseData.getMsg());
        //productionOrder = orderResponseData.getData();
        productionOrder.setId(shopOrderTwoSaveVo.getErpOrderBo());
        productionOrder.setStatus(ProductionOrderStatusEnum.ASSIGNED.getCode()); //更新订单状态
        ResponseData<Boolean> responseDataBoolean = productionOrderService.saveOrder(productionOrder);
        Assert.valid(!responseDataBoolean.isSuccess(), responseData.getMsg());

    }

    /**
     * 根据产线（工单）批量排程下达，并新建工单
     * @param bos
     */
    //@Transactional(rollbackFor = Exception.class)
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    @Override
    public void batchUpdateScheduleState(List<String> bos) {
        List<ScheduleProductionLineEntity> list = scheduleProductionLineMapper.selectList(new QueryWrapper<ScheduleProductionLineEntity>().lambda().in(ScheduleProductionLineEntity::getBo, bos));
        if (CollUtil.isNotEmpty(list)) {
            Set<String> set = list.stream().map(x -> x.getState()).filter(x -> x != null).collect(Collectors.toSet());
            if (set.contains(Constant.ScheduleTwoState.RELEASE.getValue())) {
                throw new CommonException("存在已下达排程!", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
        }

        bos.forEach(schedulePlineBo -> {
            //排程下达，先更新排程表状态，再将排程表和排程工单关联表和排程产线关联表的数据保存排程派工表
            ScheduleProductionLineEntity scheduleProductionLineEntity = new ScheduleProductionLineEntity();
            scheduleProductionLineEntity.setState(Constant.ScheduleTwoState.RELEASE.getValue());
            scheduleProductionLineEntity.setModifyUser(UserUtils.getCurrentUser().getUserName());
            scheduleProductionLineEntity.setModifyDate(new Date());
            scheduleProductionLineEntity.setBo(schedulePlineBo);
            scheduleProductionLineMapper.updateById(scheduleProductionLineEntity);
            // 更新排程派工
            saveSchedulePlineReceive(schedulePlineBo);
            // 下达时保存工单
            ShopOrderTwoSaveVo shopOrderTwoSaveVo = getShopFullOrderByScheduleBo(schedulePlineBo);
            ResponseData responseData = shopOrderTwoService.saveShopOrder(shopOrderTwoSaveVo);
            Assert.valid(!responseData.isSuccess(), responseData.getMsg());

            // 更新订单状态
            ProductionOrder productionOrder = new ProductionOrder();
            if(shopOrderTwoSaveVo.getErpOrderBo() == null || "".equals(shopOrderTwoSaveVo.getErpOrderBo())){
                throw new CommonException("更新订单状态时获取的工单ID为空!", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
            //ResponseData<ProductionOrder> orderResponseData = productionOrderService.getDetailById(shopOrderTwoSaveVo.getErpOrderBo());
            //Assert.valid(!orderResponseData.isSuccess(), orderResponseData.getMsg());
            //productionOrder = orderResponseData.getData();
            productionOrder.setId(shopOrderTwoSaveVo.getErpOrderBo());
            productionOrder.setStatus(ProductionOrderStatusEnum.ASSIGNED.getCode()); //更新订单状态
            ResponseData<Boolean> responseDataBoolean = productionOrderService.saveOrder(productionOrder);
            Assert.valid(!responseDataBoolean.isSuccess(), responseData.getMsg());
        });

    }

    /**
     * 查询工单相关数据
     * @param schedulePlineBo 产线排程ID
     * @return ShopOrderFullVo
     * @throws CommonException 异常
     */
    public ShopOrderTwoSaveVo getShopFullOrderByScheduleBo(String schedulePlineBo) throws CommonException {
        ShopOrderTwoSaveVo shopOrderTwoSaveVo = new ShopOrderTwoSaveVo();

        // 根据排程ID查询排程信息
        // ScheduleRespTwoDTO scheduleRespTwoDTO = new ScheduleRespTwoDTO();
        ScheduleSaveTwoDTO scheduleSaveTwoDTO = findTwoById(schedulePlineBo);
        ScheduleProductionLineSaveTwoDTO scheduleProductionLineSaveTwoDTO = scheduleProductionLineMapper.findTwoById(schedulePlineBo);
        String scheduleNo = scheduleSaveTwoDTO.getScheduleNo();
        if (StrUtil.isBlank(scheduleNo)) {
            throw new CommonException("排程编号不能为空！", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        // 设置工单号
        /*QueryWrapper<ScheduleShopOrderEntity> scheduleShopOrderEntityQueryWrapper = new QueryWrapper<>();
        scheduleShopOrderEntityQueryWrapper.eq("schedule_bo", scheduleBo);
        List<ScheduleShopOrderEntity> scheduleShopOrderEntities = scheduleShopOrderMapper.selectList(scheduleShopOrderEntityQueryWrapper);
        for(int i = 0 ;i<scheduleShopOrderEntities.size();i++){
            String shopOrderStr = scheduleShopOrderEntities.get(i).getShopOrderBo();
            if (StrUtil.isBlank(shopOrderStr)) {
                throw new CommonException("工单编号不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            shopOrderTwoSaveVo.setShopOrder(shopOrderStr);
        }*/

        // 设置工单号
        /*int targetIntNum = 1;
        String codeFormat = "%02d";
        String str = String.format(codeFormat,targetIntNum);
        String shopOrderNewStr = scheduleNo+"-"+str;
        shopOrderTwoSaveVo.setShopOrder(shopOrderNewStr);*/
        shopOrderTwoSaveVo.setShopOrder(scheduleProductionLineSaveTwoDTO.getShopOrder());

        ShopOrderHandleBO shopOrderHandleBO = new ShopOrderHandleBO(UserUtils.getSite(), shopOrderTwoSaveVo.getShopOrder());
        ShopOrder shopOrder = shopOrderTwoService.getExistShopOrder(shopOrderHandleBO);
        if (shopOrder != null) {
            throw new CommonException("工单" + shopOrderHandleBO.getShopOrder() + "已存在", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        // BeanUtils.copyProperties(shopOrder, shopOrderFullVo);

        //获取物料描述
        shopOrderTwoSaveVo.setItemBo(scheduleSaveTwoDTO.getItemBo());
        shopOrderTwoSaveVo.setItemCode(scheduleSaveTwoDTO.getItemCode());
        shopOrderTwoSaveVo.setItemDesc(scheduleSaveTwoDTO.getItemName());
        shopOrderTwoSaveVo.setItemVersion(scheduleSaveTwoDTO.getItemVersion());
        shopOrderTwoSaveVo.setState(OrderStateEnum.ORDER.getCode()); // 工单状态：已下单
        shopOrderTwoSaveVo.setErpOrderBo(scheduleSaveTwoDTO.getOrderId());
        shopOrderTwoSaveVo.setErpOrderCode(scheduleSaveTwoDTO.getOrderNo());
        shopOrderTwoSaveVo.setErpOrderDesc(scheduleSaveTwoDTO.getOrderDesc());
        /*//组装工单BOM
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
        }*/

        //组装工艺路线
        // String orderType, String itemBo, String productBo
        ResponseData<ProductionOrder> productionOrderResponseData = productionOrderService.getDetailById(scheduleSaveTwoDTO.getOrderId());
        if(productionOrderResponseData == null || productionOrderResponseData.getData() == null || "".equals(productionOrderResponseData.getData())){
            throw new CommonException("未找到对应的订单类型！", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        // 订单、工单类型
        String orderType = productionOrderResponseData.getData().getType();
        shopOrderTwoSaveVo.setShopOrderType(orderType);
        // 获取工艺路线
        /*RouterFitDto responseData = routerFitService.getRouterAndBom(orderType,itemBo,productBo).getData();
        if (responseData != null) {
            responseData.getRouterBo();
            shopOrderTwoSaveVo.setRouter(responseData.getRouter());
            shopOrderTwoSaveVo.setRouterVersion(responseData.getRouterVersion());
        }else{
            shopOrderTwoSaveVo.setRouter("测试工艺路线");
            shopOrderTwoSaveVo.setRouterVersion("1");
            //throw new CommonException("未找到相应的工艺路线!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }*/

        //组装产线
        shopOrderTwoSaveVo.setProductionLineBo(scheduleProductionLineSaveTwoDTO.getProductionLineBo());
        shopOrderTwoSaveVo.setProductionLineCode(scheduleProductionLineSaveTwoDTO.getProductionLineCode());
        shopOrderTwoSaveVo.setProductionLineDesc(scheduleProductionLineSaveTwoDTO.getProductionLineDesc());

        //组装班次
        shopOrderTwoSaveVo.setShiftBo(scheduleProductionLineSaveTwoDTO.getShiftBo());
        shopOrderTwoSaveVo.setShiftCode(scheduleProductionLineSaveTwoDTO.getShiftCode());

        // 工单开始、结束时间
        shopOrderTwoSaveVo.setPlanStartDate(scheduleProductionLineSaveTwoDTO.getStartDate()); // 工单开始时间
        shopOrderTwoSaveVo.setPlanEndDate(scheduleProductionLineSaveTwoDTO.getEndDate()); // 工单结束时间

        //获取已排程数量
        shopOrderTwoSaveVo.setSchedulQty(scheduleSaveTwoDTO.getQuantity());

        // 保存工单包装规则
        ResponseData<ItemFullVo> itemFullVoResponseData = itemService.getItemByItemAndVersion(scheduleSaveTwoDTO.getItemCode(), shopOrderTwoSaveVo.getItemVersion());
        if(itemFullVoResponseData.getData().getPackingRuleList() == null || itemFullVoResponseData.getData().getPackingRuleList().size() <= 0){
            throw new CommonException("物料包装规则未维护!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }/*else if(itemFullVoResponseData.getData().getPackingRuleList().size() > 1){
            throw new CommonException("存在多个包装规则!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }*/
        List<ItemPackRuleDetailDto> itemPackRuleDetailDtoList = itemFullVoResponseData.getData().getPackingRuleList();
        if(itemPackRuleDetailDtoList == null || itemPackRuleDetailDtoList.size() <= 0){
            throw new CommonException("物料包装规则未维护!!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        ShopOrderPackRuleSaveDto shopOrderPackRuleSaveDto = new ShopOrderPackRuleSaveDto();
        shopOrderPackRuleSaveDto.setShopOrderBo(shopOrderHandleBO.getBo()); // 工单BO
        String packRule = itemPackRuleDetailDtoList.get(0).getRuleRuleBo();
        shopOrderPackRuleSaveDto.setPackRuleBo(packRule); // (PACK_RULE_BO)包装规则BO
        shopOrderPackRuleSaveDto.setItemBo(scheduleSaveTwoDTO.getItemBo()); // 物料BO
        List<ShopOrderPackRuleDetail> shopOrderPackRuleDetailList = new ArrayList<>();
        for(int p=0;p<itemPackRuleDetailDtoList.size();p++){
            ShopOrderPackRuleDetail shopOrderPackRuleDetail = new ShopOrderPackRuleDetail(); // 工单包装规则实体
            ItemPackRuleDetailDto itemPackRuleDetailDto = itemPackRuleDetailDtoList.get(p);
            BeanUtil.copyProperties(itemPackRuleDetailDto,shopOrderPackRuleDetail);
            /*if(packRule.equals(itemPackRuleDetailDto.getRuleRuleBo())){
                throw new CommonException("物料包装规则bo存在多个，且不一致!!", CommonExceptionDefinition.BASIC_EXCEPTION);
            }*/
            shopOrderPackRuleDetail.setPackRuleBo(itemPackRuleDetailDto.getRuleRuleBo()); // 包装规则BO
            shopOrderPackRuleDetail.setShopOrderBo(shopOrderHandleBO.getBo()); // 工单BO
            shopOrderPackRuleDetailList.add(shopOrderPackRuleDetail);
        }
        shopOrderPackRuleSaveDto.setShopOrderPackRuleDetails(shopOrderPackRuleDetailList);
        shopOrderTwoSaveVo.setShopOrderPackRuleSaveDto(shopOrderPackRuleSaveDto);

        return shopOrderTwoSaveVo;
    }

    @Override
    public void batchRelease(List<String> bos) throws CommonException {
        List<ScheduleEntity> list = scheduleMapper.selectList(new QueryWrapper<ScheduleEntity>().lambda().in(ScheduleEntity::getBo, bos));
        if (CollUtil.isNotEmpty(list)) {
            Set<Integer> set = list.stream().map(x -> x.getState()).filter(x -> x != null).collect(Collectors.toSet());
            if (set.contains(Constant.ScheduleState.RELEASE.getValue())) {
                throw new CommonException("存在已下达排程!", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
        }

        bos.forEach(bo -> {
            ScheduleEntity scheduleEntity = new ScheduleEntity();
            scheduleEntity.setState(Constant.ScheduleState.RELEASE.getValue());
            scheduleEntity.setBo(bo);
            scheduleMapper.updateById(scheduleEntity);
            saveScheduleReceive(bo);
        });
    }

    @Override
    public IPage<ProductionLineResDTO> getInitScheduleProductionLineList(ScheduleProductionLineQueryDTO scheduleProductionLineQueryDTO) {

        if (ObjectUtil.isEmpty(scheduleProductionLineQueryDTO.getPage())) {
            scheduleProductionLineQueryDTO.setPage(new Page(0, 10));
        }

        return scheduleMapper.findProductionLine(scheduleProductionLineQueryDTO.getPage(), scheduleProductionLineQueryDTO.getWorkShopBo());
    }

    @Override
    public List<StationResDTO> getStationList(String productionLineBo) {
        return scheduleMapper.findProductionLineStation(productionLineBo);
    }

    @Override
    public IPage<ScheduleProductionLineRespDTO> getScheduleProductionLine(ScheduleProductionLineQueryDTO scheduleProductionLineQueryDTO) {

        if (ObjectUtil.isEmpty(scheduleProductionLineQueryDTO.getPage())) {
            scheduleProductionLineQueryDTO.setPage(new Page(0, 10));
        }

        return scheduleProductionLineMapper.findList(scheduleProductionLineQueryDTO.getPage(), scheduleProductionLineQueryDTO);
    }

    @Override
    public IPage<Map<String, Object>> getByProductLine(ScheduleShowDto scheduleShowDto) {
        scheduleShowDto.setPage(
                Optional.ofNullable(scheduleShowDto.getPage())
                        .orElseGet(() -> new Page(0, 10))
        );
        scheduleShowDto.setSite(UserUtils.getSite());
        return scheduleMapper.getByProductLine(scheduleShowDto.getPage(), scheduleShowDto);
    }


    private void saveScheduleReceive(String scheduleBo) {
        //获取排程数据
        ScheduleEntity scheduleEntity = scheduleMapper.selectById(scheduleBo);
        //获取排程产线表的数据
        QueryWrapper<ScheduleProductionLineEntity> scheduleProductionLineEntityQueryWrapper = new QueryWrapper<>();
        scheduleProductionLineEntityQueryWrapper.eq("schedule_bo", scheduleBo);
        List<ScheduleProductionLineEntity> scheduleProductionLineEntities = scheduleProductionLineMapper.selectList(scheduleProductionLineEntityQueryWrapper);
        //获取排程工单数据,目前版本只是一个工单对应一个排程，后面如果有拓展需修改成一次性查询所有数据的sql,以优化性能
        QueryWrapper<ScheduleShopOrderEntity> scheduleShopOrderEntityQueryWrapper = new QueryWrapper<>();
        scheduleShopOrderEntityQueryWrapper.eq("schedule_bo", scheduleBo);
        List<ScheduleShopOrderEntity> scheduleShopOrderEntities = scheduleShopOrderMapper.selectList(scheduleShopOrderEntityQueryWrapper);

        //定义产线的开始时间和结束时间的map集合，用于计算机台产能的已排时间
        List<Map<String, Object>> mapList = new ArrayList<>();
        scheduleProductionLineEntities.forEach(scheduleProductionLineEntity -> {
            ScheduleReceiveEntity scheduleReceiveEntity = new ScheduleReceiveEntity();
            scheduleReceiveEntity.setShopOrderBo(scheduleShopOrderEntities.get(0).getShopOrderBo());
            scheduleReceiveEntity.setScheduleBo(scheduleEntity.getBo());
            scheduleReceiveEntity.setScheduleQty(scheduleProductionLineEntity.getQuantity());
            scheduleReceiveEntity.setState(Constant.ScheduleReceiveState.CREATE.getValue());
            scheduleReceiveEntity.setProductionLineBo(scheduleProductionLineEntity.getProductionLineBo());
            scheduleReceiveEntity.setCreateDate(new Date());
//            scheduleReceiveEntity.setCreateUser(UserUtils.getCurrentUser().getUserName());
            if (StrUtil.isNotBlank(scheduleProductionLineEntity.getStationBo())) {
                scheduleReceiveEntity.setStationBo(scheduleProductionLineEntity.getStationBo());
            }
            scheduleReceiveEntity.setWorkShopBo(scheduleEntity.getWorkshopBo());
            scheduleReceiveMapper.insert(scheduleReceiveEntity);

            //定义产线的开始时间和结束时间的map
            Map<String, Object> map = new HashMap<>();
            map.put("productLineBo", scheduleProductionLineEntity.getProductionLineBo());
            map.put("startDate", scheduleProductionLineEntity.getStartDate());
            map.put("endDate", scheduleProductionLineEntity.getEndDate());
            mapList.add(map);
        });

        resourcesCalendarService.occupyResourcesCalendar(mapList);

    }

    /**
     * 排程产线下达派工
     * @param schedulePlineBo
     */
    private void saveSchedulePlineReceive(String schedulePlineBo) {
        //获取产线排程数据
        ScheduleProductionLineEntity scheduleProductionLineEntity = scheduleProductionLineMapper.selectById(schedulePlineBo);
        Assert.valid(scheduleProductionLineEntity == null, "未找到产线排程数" + schedulePlineBo);

        //定义产线的开始时间和结束时间的map集合，用于计算机台产能的已排时间
        List<Map<String, Object>> mapList = new ArrayList<>();
        ScheduleReceiveEntity scheduleReceiveEntity = new ScheduleReceiveEntity();
        ShopOrderHandleBO shopOrderHandleBO = new ShopOrderHandleBO(UserUtils.getSite(),scheduleProductionLineEntity.getShopOrder());
        scheduleReceiveEntity.setSchedulePlineBo(scheduleProductionLineEntity.getBo());
        scheduleReceiveEntity.setShopOrderBo(shopOrderHandleBO.getBo());
        scheduleReceiveEntity.setScheduleBo(scheduleProductionLineEntity.getScheduleBo());
        scheduleReceiveEntity.setScheduleQty(scheduleProductionLineEntity.getQuantity());
        scheduleReceiveEntity.setState(Constant.ScheduleReceiveState.CREATE.getValue());
        scheduleReceiveEntity.setCreateDate(new Date());
        scheduleReceiveEntity.setCreateUser(UserUtils.getCurrentUser().getUserName());
        scheduleReceiveEntity.setWorkShopBo(scheduleProductionLineEntity.getWorkShopBo());
        scheduleReceiveEntity.setProductionLineBo(scheduleProductionLineEntity.getProductionLineBo());
        scheduleReceiveEntity.setShiftBo(scheduleProductionLineEntity.getShiftBo());
        if (StrUtil.isNotBlank(scheduleProductionLineEntity.getStationBo())) {
            scheduleReceiveEntity.setStationBo(scheduleProductionLineEntity.getStationBo());
        }
        scheduleReceiveMapper.insert(scheduleReceiveEntity);

        //定义产线的开始时间和结束时间的map
        Map<String, Object> map = new HashMap<>();
        map.put("productLineBo", scheduleProductionLineEntity.getProductionLineBo());
        map.put("startDate", scheduleProductionLineEntity.getStartDate());
        map.put("endDate", scheduleProductionLineEntity.getEndDate());
        mapList.add(map);

        resourcesCalendarService.occupyResourcesCalendar(mapList);

    }

}

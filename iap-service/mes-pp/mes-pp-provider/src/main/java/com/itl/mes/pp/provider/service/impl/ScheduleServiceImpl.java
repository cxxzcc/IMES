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
     * ????????????
     */
    @Autowired
    ShopOrderTwoService shopOrderTwoService;

    /**
     * ??????????????????
     */
    @Autowired
    RouterFitService routerFitService;

    /**
     * ????????????
     */
    @Autowired
    ItemService itemService;

    /**
     * ?????????
     */
    @Autowired
    ConfigItemService configItemService;

    /**
     * ????????????
     */
    /*@Autowired
    IapDictItemTService iapDictItemService;*/

    /**
     * ??????
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
        // ??????
        if (StrUtil.isNotEmpty(scheduleSaveDTO.getBo())) {
            // ??????????????????
            if (Constant.ScheduleState.RECEIVE.getValue() == scheduleSaveDTO.getState()) {
                throw new CommonException("???????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            scheduleEntity.setModifyUser(UserUtils.getCurrentUser().getUserName());
            scheduleEntity.setModifyDate(new Date());
            scheduleMapper.updateById(scheduleEntity);
            // ??????????????????????????????
            QueryWrapper<ScheduleProductionLineEntity> productionLineEntityQueryWrapper = new QueryWrapper();
            productionLineEntityQueryWrapper.eq("schedule_bo", scheduleEntity.getBo());
            scheduleProductionLineMapper.delete(productionLineEntityQueryWrapper);
            // ?????????????????????????????????
            scheduleSaveDTO.getScheduleProductionLineSaveDTOList().forEach(scheduleProductionLineSaveDTO -> {
                ScheduleProductionLineEntity scheduleProductionLineEntity = new ScheduleProductionLineEntity();
                BeanUtil.copyProperties(scheduleProductionLineSaveDTO, scheduleProductionLineEntity);
                scheduleProductionLineEntity.setBo(null);
                scheduleProductionLineEntity.setScheduleBo(scheduleSaveDTO.getBo());
                scheduleProductionLineMapper.insert(scheduleProductionLineEntity);
            });
            // ???????????????????????????
            QueryWrapper<ScheduleShopOrderEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("schedule_bo", scheduleSaveDTO.getBo());
            scheduleShopOrderMapper.delete(queryWrapper);
            // ???????????????????????????
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


    // ??????2
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveTwo(List<ScheduleSaveTwoDTO> scheduleSaveTwoDTOList) throws CommonException {
        for(ScheduleSaveTwoDTO scheduleSaveTwoDTO : scheduleSaveTwoDTOList){
            ScheduleEntity scheduleEntity = new ScheduleEntity();
            BeanUtil.copyProperties(scheduleSaveTwoDTO, scheduleEntity);

            // ??????????????????
            scheduleEntity.setSite(UserUtils.getSite());

            // ??????????????????????????????????????????
            String codeRuleType = scheduleSaveTwoDTO.getCodeRuleType();
            if(codeRuleType == null || "".equals(codeRuleType)){
                ConfigItem configItem = new ConfigItem();
                configItem.setItemCode("NewSchedule");
                configItem.setConfigItemKey("codeRuleType");
                List<ConfigItem> configItemList = configItemService.query(configItem).getData();
                if(configItemList == null || "".equals(configItemList) || configItemList.size() <= 0){
                    throw new CommonException("?????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }else if(configItemList.size() > 1){
                    throw new CommonException("????????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }
                codeRuleType = configItemList.get(0).getConfigItemValue();
            }

            ResponseData<String> scheduleData = codeRuleService.generatorNextNumber(codeRuleType);
            String scheduleNo = scheduleData.getData();
            if(scheduleNo == null){
                throw new CommonException(scheduleData.getMsg(), CommonExceptionDefinition.VERIFY_EXCEPTION);
            }

            // ????????????????????????????????????
            String orderId = scheduleSaveTwoDTO.getOrderId();
            if(orderId == null || "".equals(orderId)){
                throw new CommonException("??????ID???????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            String orderNo = scheduleSaveTwoDTO.getOrderNo();
            if(orderNo == null || "".equals(orderNo)){
                throw new CommonException("???????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }

            // ????????????
            List<ScheduleProductionLineSaveTwoDTO> scheduleProductionLineSaveTwoDTOList = scheduleSaveTwoDTO.getScheduleProductionLineSaveTwoDTOList();
            if(scheduleProductionLineSaveTwoDTOList == null || scheduleProductionLineSaveTwoDTOList.size() < 1){
                throw new CommonException("?????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }

            // ????????????????????????????????????????????????1
            int schedulePlineQuantity = 0;

            // ??????
            if (StrUtil.isNotEmpty(scheduleSaveTwoDTO.getBo())) {

                if (Constant.ScheduleState.RELEASE.getValue() == scheduleSaveTwoDTO.getState()) {
                    throw new CommonException("???????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }
                if (Constant.ScheduleState.RECEIVE.getValue() == scheduleSaveTwoDTO.getState()) {
                    throw new CommonException("???????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
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
                        throw new CommonException("??????????????????ID???????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
                    }
                    ScheduleProductionLineEntity scheduleProductionLineEntity = scheduleProductionLineMapper.selectById(scheduleProductionLineSaveTwoDTO.getBo());
                    BigDecimal plineQuantity = scheduleProductionLineEntity.getQuantity(); // ????????????????????????
                    BeanUtil.copyProperties(scheduleProductionLineSaveTwoDTO, scheduleProductionLineEntity);

                    // ????????????
                    WorkShopHandleBO workShopHandleBO = new WorkShopHandleBO(UserUtils.getSite(),scheduleProductionLineEntity.getWorkShop());
                    scheduleProductionLineEntity.setWorkShopBo(workShopHandleBO.getBo()); // ??????ID
                    scheduleProductionLineEntity.setBo(scheduleProductionLineSaveTwoDTO.getBo());
                    scheduleProductionLineEntity.setScheduleBo(scheduleSaveTwoDTO.getBo());
                    scheduleProductionLineEntity.setModifyDate(new Date());
                    scheduleProductionLineEntity.setModifyUser(UserUtils.getCurrentUser().getUserName());
                    // ???????????????
                    /*int targetIntNum = 1 + i;
                    String codeFormat = "%02d";
                    String str = String.format(codeFormat,targetIntNum);
                    String shopOrderNewStr = orderNo+"-"+str;
                    scheduleProductionLineEntity.setShopOrder(shopOrderNewStr);
                    // ?????????????????????????????????
                    scheduleProductionLineEntity.setState("ADV_S1");*/

                    // ????????????????????????????????????????????????2
                    if(scheduleProductionLineSaveTwoDTO.getQuantity() <= 0){
                        throw new CommonException("?????????????????????????????????1", CommonExceptionDefinition.VERIFY_EXCEPTION);
                    }
                    schedulePlineQuantity = schedulePlineQuantity + scheduleProductionLineSaveTwoDTO.getQuantity();
                    if(schedulePlineQuantity != plineQuantity.intValue()){
                        throw new CommonException("?????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
                    }

                    // ????????????????????????
                    /*int scheduleQuantity = scheduleSaveTwoDTO.getQuantity().intValue();
                    if(scheduleQuantity <= 0 || "".equals(scheduleQuantity)){
                        throw new CommonException("?????????????????????0?????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
                    }
                    ProductionOrder productionOrder = productionOrderService.getDetailById(orderId).getData();
                    int orderQuantity = productionOrder.getQuantity(); // ????????????
                    int scheduleQuantityNew = productionOrder.getScheduledQuantity() + scheduleQuantity; // ???????????????
                    if(orderQuantity < scheduleQuantityNew){
                        throw new CommonException("????????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
                    }
                    // ???????????????????????????
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

            } else { // ??????
                scheduleEntity.setCreateUser(UserUtils.getCurrentUser().getUserName());
                scheduleEntity.setState(Constant.ScheduleState.CREATE.getValue());
                //scheduleEntity.setBo(scheduleBo);
                scheduleEntity.setScheduleNo(scheduleNo);
                scheduleMapper.insert(scheduleEntity);

                // ????????????????????????
                int scheduleQuantity = scheduleSaveTwoDTO.getQuantity().intValue();
                if(scheduleQuantity <= 0 || "".equals(scheduleQuantity)){
                    throw new CommonException("?????????????????????0?????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }
                ProductionOrder productionOrder = productionOrderService.getDetailById(orderId).getData();
                int orderQuantity = productionOrder.getQuantity(); // ????????????
                int scheduleQuantityNew = productionOrder.getScheduledQuantity() + scheduleQuantity; // ???????????????
                if(orderQuantity < scheduleQuantityNew){
                    throw new CommonException("????????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }
                // ???????????????????????????
                productionOrder.setId(scheduleSaveTwoDTO.getOrderId());
                productionOrder.setScheduledQuantity(scheduleQuantityNew);
                // productionOrder.setStatus(ProductionOrderStatusEnum.ASSIGNED.getCode()); //??????????????????
                productionOrderService.saveOrder(productionOrder);

                int targetIntNumF = 1;
                for(int i=0;i < scheduleProductionLineSaveTwoDTOList.size();i++){
                    ScheduleProductionLineSaveTwoDTO scheduleProductionLineSaveTwoDTO = scheduleProductionLineSaveTwoDTOList.get(i);
                    ScheduleProductionLineEntity scheduleProductionLineEntity = new ScheduleProductionLineEntity();
                    BeanUtil.copyProperties(scheduleProductionLineSaveTwoDTO, scheduleProductionLineEntity);
                    // ????????????
                    WorkShopHandleBO workShopHandleBO = new WorkShopHandleBO(UserUtils.getSite(),scheduleProductionLineEntity.getWorkShop());
                    scheduleProductionLineEntity.setWorkShopBo(workShopHandleBO.getBo()); // ??????ID
                    scheduleProductionLineEntity.setScheduleBo(scheduleEntity.getBo());
                    scheduleProductionLineEntity.setStationBo("");

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
                    for(int targetIntNum = targetIntNumF;targetIntNumBl;targetIntNum++){
                        // ???????????????
                        String str = String.format(codeFormat,targetIntNum);
                        shopOrderNewStr = orderNo+"-"+str;
                        // ShopOrderHandleBO shopOrderHandleBO2 = new ShopOrderHandleBO(UserUtils.getSite(),shopOrderNewStr);
                        ResponseData<ShopOrderFullVo> shopOrder = shopOrderTwoService.getShopOrder(shopOrderNewStr);
                        if(shopOrder == null || shopOrder.getData() == null){
                            ShopOrderHandleBO shopOrderHandleBO = new ShopOrderHandleBO(UserUtils.getSite(),shopOrderNewStr);
                            List<ScheduleEntity> scheduleEntityList = getByScheduleShopOrder(shopOrderHandleBO.getBo());
                            if(scheduleEntityList == null || scheduleEntityList.size() < 1){
                                scheduleProductionLineEntity.setShopOrder(shopOrderNewStr);
                                // ?????????????????????????????????
                                scheduleProductionLineEntity.setState("ADV_S1");
                                targetIntNumF = targetIntNum+1;
                                targetIntNumBl = false;
                            }

                        }
                    }

                    /*// ???????????????
                    int targetIntNum = 1 + i;
                    String codeFormat = "%02d";
                    String str = String.format(codeFormat,targetIntNum);
                    String shopOrderNewStr = orderNo+"-"+str;
                    scheduleProductionLineEntity.setShopOrder(shopOrderNewStr);
                    // ?????????????????????????????????
                    scheduleProductionLineEntity.setState("ADV_S1");*/

                    // ????????????????????????????????????????????????2
                    if(scheduleProductionLineSaveTwoDTO.getQuantity() <= 0){
                        throw new CommonException("?????????????????????????????????1", CommonExceptionDefinition.VERIFY_EXCEPTION);
                    }
                    schedulePlineQuantity = schedulePlineQuantity + scheduleProductionLineSaveTwoDTO.getQuantity();

                    scheduleProductionLineMapper.insert(scheduleProductionLineEntity);
                }

                // ?????????????????????????????????????????????????????????????????????3
                if(scheduleQuantity != schedulePlineQuantity){
                    throw new CommonException("??????????????????????????????????????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
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
                throw new CommonException("????????????????????????????????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }

            // ??????????????????
            ScheduleRespTwoDTO scheduleRespTwoDTO = scheduleMapper.queryTwoById(id);
            String orderId = scheduleRespTwoDTO.getOrderId();
            if(orderId == null || "".equals(orderId)){
                throw new CommonException("??????ID???????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            if(scheduleRespTwoDTO.getQuantity() == null || "".equals(scheduleRespTwoDTO.getQuantity())){
                throw new CommonException("???????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            int scheduleQuantity = scheduleRespTwoDTO.getQuantity().intValue();
            ProductionOrder productionOrder = productionOrderService.getDetailById(orderId).getData();
            int orderQuantity = productionOrder.getQuantity(); // ????????????
            int scheduleQuantityNew = productionOrder.getScheduledQuantity() - scheduleQuantity; // ???????????????
            if(orderQuantity < scheduleQuantityNew){
                throw new CommonException("????????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            // ???????????????????????????
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
            // ??????2
            ScheduleProductionLineEntity scheduleProductionLineEntity = scheduleProductionLineMapper.selectById(id);
            if(scheduleProductionLineEntity == null || "".equals(scheduleProductionLineEntity)){
                throw new CommonException("????????????????????????????????????????????????ID???????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            if (scheduleProductionLineEntity.getState() == "ADV_S2" || "ADV_S2".equals(scheduleProductionLineEntity.getState())) {
                throw new CommonException("????????????????????????????????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }

            // ????????????????????????????????????
            ScheduleSaveTwoDTO scheduleSaveTwoDTO = scheduleMapper.findTwoById(id);
            if(scheduleSaveTwoDTO == null || "".equals(scheduleSaveTwoDTO)){
                throw new CommonException("????????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }

            String orderId = scheduleSaveTwoDTO.getOrderId();
            if(orderId == null || "".equals(orderId)){
                throw new CommonException("??????ID???????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            if(scheduleSaveTwoDTO.getQuantity() == null || "".equals(scheduleSaveTwoDTO.getQuantity())){
                throw new CommonException("???????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            int scheduleQuantity = scheduleSaveTwoDTO.getQuantity().intValue();
            if(scheduleProductionLineEntity.getQuantity() == null || "".equals(scheduleProductionLineEntity.getQuantity())){
                throw new CommonException("?????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            int schedulePlineQuantity = scheduleProductionLineEntity.getQuantity().intValue();

            ProductionOrder productionOrder = productionOrderService.getDetailById(orderId).getData();
            int orderQuantity = productionOrder.getQuantity(); // ????????????
            int scheduleQuantityNew = productionOrder.getScheduledQuantity() - schedulePlineQuantity; // ???????????????
            if(orderQuantity < scheduleQuantityNew){
                throw new CommonException("????????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            // ???????????????????????????
            productionOrder.setId(scheduleSaveTwoDTO.getOrderId());
            productionOrder.setScheduledQuantity(scheduleQuantityNew);
            productionOrderService.saveOrder(productionOrder);

            // ????????????????????????????????????????????????????????????
            String scheduleBo = scheduleProductionLineEntity.getScheduleBo();
            if(scheduleBo != null || !"".equals(scheduleBo)){
                QueryWrapper<ScheduleProductionLineEntity> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("SCHEDULE_BO", scheduleBo);
                Integer count = scheduleProductionLineMapper.selectCount(queryWrapper);
                if (count <= 1) {
                    scheduleMapper.deleteById(scheduleBo);
                }
                // ??????????????????
                int i = scheduleProductionLineMapper.deleteById(id);
                if(i <= 0){
                    return;
                }
            }else{
                throw new CommonException("???????????????ID?????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
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
     * ?????????????????????
     * ScheduleSaveTwoDTO
     * @param schedulePlineBo
     * @return
     */
    @Override
    public ScheduleSaveTwoDTO findTwoById(String schedulePlineBo) {
        ScheduleSaveTwoDTO scheduleSaveTwoDTO = scheduleMapper.findTwoById(schedulePlineBo);
        if(scheduleSaveTwoDTO == null || "".equals(scheduleSaveTwoDTO)){
            throw new CommonException("???????????????????????????!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        // ?????????????????????????????????????????????
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
                .orElseThrow(() -> new CommonException("??????????????????!", CommonExceptionDefinition.BASIC_EXCEPTION));
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

        //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        ScheduleEntity updateEntity = new ScheduleEntity();
        updateEntity.setState(Constant.ScheduleState.RELEASE.getValue());
        updateEntity.setModifyUser(UserUtils.getCurrentUser().getUserName());
        updateEntity.setModifyDate(new Date());
        updateEntity.setBo(bo);
        scheduleMapper.updateById(updateEntity);
        saveScheduleReceive(bo);
    }

    /**
     * ??????????????????????????????????????????????????????
     * @param schedulePlineBo
     */
    //@Transactional(rollbackFor = Exception.class)
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void updateScheduleState(String schedulePlineBo) throws CommonException {

        //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        ScheduleProductionLineEntity scheduleProductionLineEntity = new ScheduleProductionLineEntity();
        scheduleProductionLineEntity.setState(Constant.ScheduleTwoState.RELEASE.getValue());
        scheduleProductionLineEntity.setModifyUser(UserUtils.getCurrentUser().getUserName());
        scheduleProductionLineEntity.setModifyDate(new Date());
        scheduleProductionLineEntity.setBo(schedulePlineBo);
        scheduleProductionLineMapper.updateById(scheduleProductionLineEntity);
        // ??????????????????
        saveSchedulePlineReceive(schedulePlineBo);

        // ?????????????????????
        ShopOrderTwoSaveVo shopOrderTwoSaveVo = getShopFullOrderByScheduleBo(schedulePlineBo);
        ResponseData responseData = shopOrderTwoService.saveShopOrder(shopOrderTwoSaveVo);
        Assert.valid(!responseData.isSuccess(), responseData.getMsg());

        // ??????????????????
        ProductionOrder productionOrder = new ProductionOrder();
        if(shopOrderTwoSaveVo.getErpOrderBo() == null || "".equals(shopOrderTwoSaveVo.getErpOrderBo())){
            throw new CommonException("????????????????????????????????????ID??????!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        //ResponseData<ProductionOrder> orderResponseData = productionOrderService.getDetailById(shopOrderTwoSaveVo.getErpOrderBo());
        //Assert.valid(!orderResponseData.isSuccess(), orderResponseData.getMsg());
        //productionOrder = orderResponseData.getData();
        productionOrder.setId(shopOrderTwoSaveVo.getErpOrderBo());
        productionOrder.setStatus(ProductionOrderStatusEnum.ASSIGNED.getCode()); //??????????????????
        ResponseData<Boolean> responseDataBoolean = productionOrderService.saveOrder(productionOrder);
        Assert.valid(!responseDataBoolean.isSuccess(), responseData.getMsg());

    }

    /**
     * ????????????????????????????????????????????????????????????
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
                throw new CommonException("?????????????????????!", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
        }

        bos.forEach(schedulePlineBo -> {
            //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            ScheduleProductionLineEntity scheduleProductionLineEntity = new ScheduleProductionLineEntity();
            scheduleProductionLineEntity.setState(Constant.ScheduleTwoState.RELEASE.getValue());
            scheduleProductionLineEntity.setModifyUser(UserUtils.getCurrentUser().getUserName());
            scheduleProductionLineEntity.setModifyDate(new Date());
            scheduleProductionLineEntity.setBo(schedulePlineBo);
            scheduleProductionLineMapper.updateById(scheduleProductionLineEntity);
            // ??????????????????
            saveSchedulePlineReceive(schedulePlineBo);
            // ?????????????????????
            ShopOrderTwoSaveVo shopOrderTwoSaveVo = getShopFullOrderByScheduleBo(schedulePlineBo);
            ResponseData responseData = shopOrderTwoService.saveShopOrder(shopOrderTwoSaveVo);
            Assert.valid(!responseData.isSuccess(), responseData.getMsg());

            // ??????????????????
            ProductionOrder productionOrder = new ProductionOrder();
            if(shopOrderTwoSaveVo.getErpOrderBo() == null || "".equals(shopOrderTwoSaveVo.getErpOrderBo())){
                throw new CommonException("????????????????????????????????????ID??????!", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
            //ResponseData<ProductionOrder> orderResponseData = productionOrderService.getDetailById(shopOrderTwoSaveVo.getErpOrderBo());
            //Assert.valid(!orderResponseData.isSuccess(), orderResponseData.getMsg());
            //productionOrder = orderResponseData.getData();
            productionOrder.setId(shopOrderTwoSaveVo.getErpOrderBo());
            productionOrder.setStatus(ProductionOrderStatusEnum.ASSIGNED.getCode()); //??????????????????
            ResponseData<Boolean> responseDataBoolean = productionOrderService.saveOrder(productionOrder);
            Assert.valid(!responseDataBoolean.isSuccess(), responseData.getMsg());
        });

    }

    /**
     * ????????????????????????
     * @param schedulePlineBo ????????????ID
     * @return ShopOrderFullVo
     * @throws CommonException ??????
     */
    public ShopOrderTwoSaveVo getShopFullOrderByScheduleBo(String schedulePlineBo) throws CommonException {
        ShopOrderTwoSaveVo shopOrderTwoSaveVo = new ShopOrderTwoSaveVo();

        // ????????????ID??????????????????
        // ScheduleRespTwoDTO scheduleRespTwoDTO = new ScheduleRespTwoDTO();
        ScheduleSaveTwoDTO scheduleSaveTwoDTO = findTwoById(schedulePlineBo);
        ScheduleProductionLineSaveTwoDTO scheduleProductionLineSaveTwoDTO = scheduleProductionLineMapper.findTwoById(schedulePlineBo);
        String scheduleNo = scheduleSaveTwoDTO.getScheduleNo();
        if (StrUtil.isBlank(scheduleNo)) {
            throw new CommonException("???????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        // ???????????????
        /*QueryWrapper<ScheduleShopOrderEntity> scheduleShopOrderEntityQueryWrapper = new QueryWrapper<>();
        scheduleShopOrderEntityQueryWrapper.eq("schedule_bo", scheduleBo);
        List<ScheduleShopOrderEntity> scheduleShopOrderEntities = scheduleShopOrderMapper.selectList(scheduleShopOrderEntityQueryWrapper);
        for(int i = 0 ;i<scheduleShopOrderEntities.size();i++){
            String shopOrderStr = scheduleShopOrderEntities.get(i).getShopOrderBo();
            if (StrUtil.isBlank(shopOrderStr)) {
                throw new CommonException("????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            shopOrderTwoSaveVo.setShopOrder(shopOrderStr);
        }*/

        // ???????????????
        /*int targetIntNum = 1;
        String codeFormat = "%02d";
        String str = String.format(codeFormat,targetIntNum);
        String shopOrderNewStr = scheduleNo+"-"+str;
        shopOrderTwoSaveVo.setShopOrder(shopOrderNewStr);*/
        shopOrderTwoSaveVo.setShopOrder(scheduleProductionLineSaveTwoDTO.getShopOrder());

        ShopOrderHandleBO shopOrderHandleBO = new ShopOrderHandleBO(UserUtils.getSite(), shopOrderTwoSaveVo.getShopOrder());
        ShopOrder shopOrder = shopOrderTwoService.getExistShopOrder(shopOrderHandleBO);
        if (shopOrder != null) {
            throw new CommonException("??????" + shopOrderHandleBO.getShopOrder() + "?????????", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        // BeanUtils.copyProperties(shopOrder, shopOrderFullVo);

        //??????????????????
        shopOrderTwoSaveVo.setItemBo(scheduleSaveTwoDTO.getItemBo());
        shopOrderTwoSaveVo.setItemCode(scheduleSaveTwoDTO.getItemCode());
        shopOrderTwoSaveVo.setItemDesc(scheduleSaveTwoDTO.getItemName());
        shopOrderTwoSaveVo.setItemVersion(scheduleSaveTwoDTO.getItemVersion());
        shopOrderTwoSaveVo.setState(OrderStateEnum.ORDER.getCode()); // ????????????????????????
        shopOrderTwoSaveVo.setErpOrderBo(scheduleSaveTwoDTO.getOrderId());
        shopOrderTwoSaveVo.setErpOrderCode(scheduleSaveTwoDTO.getOrderNo());
        shopOrderTwoSaveVo.setErpOrderDesc(scheduleSaveTwoDTO.getOrderDesc());
        /*//????????????BOM
        if (!StrUtil.isBlank(shopOrder.getOrderBomBo())) {
            BomHandleBO bomHandleBO = new BomHandleBO(shopOrder.getOrderBomBo());
            shopOrderFullVo.setOrderBom(bomHandleBO.getBom());
            shopOrderFullVo.setOrderBomVersion(bomHandleBO.getVersion());
        }

        //??????BOM
        if (!StrUtil.isBlank(shopOrder.getBomBo())) {
            BomHandleBO bomHandleBO = new BomHandleBO(shopOrder.getBomBo());
            shopOrderFullVo.setBom(bomHandleBO.getBom());
            shopOrderFullVo.setBomVersion(bomHandleBO.getVersion());
        }*/

        //??????????????????
        // String orderType, String itemBo, String productBo
        ResponseData<ProductionOrder> productionOrderResponseData = productionOrderService.getDetailById(scheduleSaveTwoDTO.getOrderId());
        if(productionOrderResponseData == null || productionOrderResponseData.getData() == null || "".equals(productionOrderResponseData.getData())){
            throw new CommonException("?????????????????????????????????", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        // ?????????????????????
        String orderType = productionOrderResponseData.getData().getType();
        shopOrderTwoSaveVo.setShopOrderType(orderType);
        // ??????????????????
        /*RouterFitDto responseData = routerFitService.getRouterAndBom(orderType,itemBo,productBo).getData();
        if (responseData != null) {
            responseData.getRouterBo();
            shopOrderTwoSaveVo.setRouter(responseData.getRouter());
            shopOrderTwoSaveVo.setRouterVersion(responseData.getRouterVersion());
        }else{
            shopOrderTwoSaveVo.setRouter("??????????????????");
            shopOrderTwoSaveVo.setRouterVersion("1");
            //throw new CommonException("??????????????????????????????!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }*/

        //????????????
        shopOrderTwoSaveVo.setProductionLineBo(scheduleProductionLineSaveTwoDTO.getProductionLineBo());
        shopOrderTwoSaveVo.setProductionLineCode(scheduleProductionLineSaveTwoDTO.getProductionLineCode());
        shopOrderTwoSaveVo.setProductionLineDesc(scheduleProductionLineSaveTwoDTO.getProductionLineDesc());

        //????????????
        shopOrderTwoSaveVo.setShiftBo(scheduleProductionLineSaveTwoDTO.getShiftBo());
        shopOrderTwoSaveVo.setShiftCode(scheduleProductionLineSaveTwoDTO.getShiftCode());

        // ???????????????????????????
        shopOrderTwoSaveVo.setPlanStartDate(scheduleProductionLineSaveTwoDTO.getStartDate()); // ??????????????????
        shopOrderTwoSaveVo.setPlanEndDate(scheduleProductionLineSaveTwoDTO.getEndDate()); // ??????????????????

        //?????????????????????
        shopOrderTwoSaveVo.setSchedulQty(scheduleSaveTwoDTO.getQuantity());

        // ????????????????????????
        ResponseData<ItemFullVo> itemFullVoResponseData = itemService.getItemByItemAndVersion(scheduleSaveTwoDTO.getItemCode(), shopOrderTwoSaveVo.getItemVersion());
        if(itemFullVoResponseData.getData().getPackingRuleList() == null || itemFullVoResponseData.getData().getPackingRuleList().size() <= 0){
            throw new CommonException("???????????????????????????!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }/*else if(itemFullVoResponseData.getData().getPackingRuleList().size() > 1){
            throw new CommonException("????????????????????????!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }*/
        List<ItemPackRuleDetailDto> itemPackRuleDetailDtoList = itemFullVoResponseData.getData().getPackingRuleList();
        if(itemPackRuleDetailDtoList == null || itemPackRuleDetailDtoList.size() <= 0){
            throw new CommonException("???????????????????????????!!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        ShopOrderPackRuleSaveDto shopOrderPackRuleSaveDto = new ShopOrderPackRuleSaveDto();
        shopOrderPackRuleSaveDto.setShopOrderBo(shopOrderHandleBO.getBo()); // ??????BO
        String packRule = itemPackRuleDetailDtoList.get(0).getRuleRuleBo();
        shopOrderPackRuleSaveDto.setPackRuleBo(packRule); // (PACK_RULE_BO)????????????BO
        shopOrderPackRuleSaveDto.setItemBo(scheduleSaveTwoDTO.getItemBo()); // ??????BO
        List<ShopOrderPackRuleDetail> shopOrderPackRuleDetailList = new ArrayList<>();
        for(int p=0;p<itemPackRuleDetailDtoList.size();p++){
            ShopOrderPackRuleDetail shopOrderPackRuleDetail = new ShopOrderPackRuleDetail(); // ????????????????????????
            ItemPackRuleDetailDto itemPackRuleDetailDto = itemPackRuleDetailDtoList.get(p);
            BeanUtil.copyProperties(itemPackRuleDetailDto,shopOrderPackRuleDetail);
            /*if(packRule.equals(itemPackRuleDetailDto.getRuleRuleBo())){
                throw new CommonException("??????????????????bo???????????????????????????!!", CommonExceptionDefinition.BASIC_EXCEPTION);
            }*/
            shopOrderPackRuleDetail.setPackRuleBo(itemPackRuleDetailDto.getRuleRuleBo()); // ????????????BO
            shopOrderPackRuleDetail.setShopOrderBo(shopOrderHandleBO.getBo()); // ??????BO
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
                throw new CommonException("?????????????????????!", CommonExceptionDefinition.BASIC_EXCEPTION);
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
        //??????????????????
        ScheduleEntity scheduleEntity = scheduleMapper.selectById(scheduleBo);
        //??????????????????????????????
        QueryWrapper<ScheduleProductionLineEntity> scheduleProductionLineEntityQueryWrapper = new QueryWrapper<>();
        scheduleProductionLineEntityQueryWrapper.eq("schedule_bo", scheduleBo);
        List<ScheduleProductionLineEntity> scheduleProductionLineEntities = scheduleProductionLineMapper.selectList(scheduleProductionLineEntityQueryWrapper);
        //????????????????????????,??????????????????????????????????????????????????????????????????????????????????????????????????????????????????sql,???????????????
        QueryWrapper<ScheduleShopOrderEntity> scheduleShopOrderEntityQueryWrapper = new QueryWrapper<>();
        scheduleShopOrderEntityQueryWrapper.eq("schedule_bo", scheduleBo);
        List<ScheduleShopOrderEntity> scheduleShopOrderEntities = scheduleShopOrderMapper.selectList(scheduleShopOrderEntityQueryWrapper);

        //?????????????????????????????????????????????map????????????????????????????????????????????????
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

            //?????????????????????????????????????????????map
            Map<String, Object> map = new HashMap<>();
            map.put("productLineBo", scheduleProductionLineEntity.getProductionLineBo());
            map.put("startDate", scheduleProductionLineEntity.getStartDate());
            map.put("endDate", scheduleProductionLineEntity.getEndDate());
            mapList.add(map);
        });

        resourcesCalendarService.occupyResourcesCalendar(mapList);

    }

    /**
     * ????????????????????????
     * @param schedulePlineBo
     */
    private void saveSchedulePlineReceive(String schedulePlineBo) {
        //????????????????????????
        ScheduleProductionLineEntity scheduleProductionLineEntity = scheduleProductionLineMapper.selectById(schedulePlineBo);
        Assert.valid(scheduleProductionLineEntity == null, "????????????????????????" + schedulePlineBo);

        //?????????????????????????????????????????????map????????????????????????????????????????????????
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

        //?????????????????????????????????????????????map
        Map<String, Object> map = new HashMap<>();
        map.put("productLineBo", scheduleProductionLineEntity.getProductionLineBo());
        map.put("startDate", scheduleProductionLineEntity.getStartDate());
        map.put("endDate", scheduleProductionLineEntity.getEndDate());
        mapList.add(map);

        resourcesCalendarService.occupyResourcesCalendar(mapList);

    }

}

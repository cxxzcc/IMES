package com.itl.mes.me.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.Assert;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.entity.MeProductInspectionItemsOrderNcCode;
import com.itl.mes.core.client.service.MeProductInspectionItemsOrderNcCodeService;
import com.itl.mes.me.api.entity.MeProductInspectionItemsOrderEntity;
import com.itl.mes.me.api.service.MeProductInspectionItemsOrderService;
import com.itl.mes.me.provider.mapper.MeProductInspectionItemsOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@Service("meProductInspectionItemsOrderService")
public class MeProductInspectionItemsOrderServiceImpl extends ServiceImpl<MeProductInspectionItemsOrderMapper, MeProductInspectionItemsOrderEntity> implements MeProductInspectionItemsOrderService {

    @Resource
    private MeProductInspectionItemsOrderMapper meProductInspectionItemsOrderMapper;

    @Autowired
    private MeProductInspectionItemsOrderNcCodeService meProductInspectionItemsOrderNcCodeService;


    @Override
    @Transactional
    public boolean save(MeProductInspectionItemsOrderEntity meProductInspectionItemsOrderEntity) throws CommonException {
        String site = UserUtils.getSite();
        String userName = UserUtils.getCurrentUser().getUserName();
        String orderBo = meProductInspectionItemsOrderEntity.getOrderBo();
        if(orderBo == null || "".equals(orderBo)){
            throw new CommonException("工单检验项目保存参数orderBo不能为空！", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        Integer id = meProductInspectionItemsOrderEntity.getId();
        int i = 0;
        if(id == null || "".equals(id)){
            i = meProductInspectionItemsOrderMapper.insert(meProductInspectionItemsOrderEntity);
            if(i != 1){
                throw new CommonException("新增保存失败！", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
        }else{
            i = meProductInspectionItemsOrderMapper.updateById(meProductInspectionItemsOrderEntity);
            if(i != 1){
                throw new CommonException("修改保存失败！", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
        }

        // 不良代码集合
        Integer inspectionItemId = meProductInspectionItemsOrderEntity.getId();
        String itemType = meProductInspectionItemsOrderEntity.getItemType();
        if(inspectionItemId == null || "".equals(inspectionItemId)){
            throw new CommonException("不良代码无法获取inspectionItemId参数！", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if(itemType == null || "".equals(itemType)){
            itemType = "0";
        }
        ResponseData deleteResponseData = meProductInspectionItemsOrderNcCodeService.deleteByInspectionItemIdItemType(orderBo,inspectionItemId,itemType);
        Assert.valid(!deleteResponseData.isSuccess(), deleteResponseData.getMsg());
        List<MeProductInspectionItemsOrderNcCode> meProductInspectionItemsOrderNcCodeList = meProductInspectionItemsOrderEntity.getMeProductInspectionItemsOrderNcCodeList();
        if(meProductInspectionItemsOrderNcCodeList != null && !"".equals(meProductInspectionItemsOrderNcCodeList)){
            // 校验检验项目的不良代码是否重复
            // long countNum = meProductInspectionItemsOrderNcCodeList.stream().filter(MeProductInspectionItemsOrderNcCode -> orderBo.equals(MeProductInspectionItemsOrderNcCode.getOrderBo()) && inspectionItemId.equals(MeProductInspectionItemsOrderNcCode.getInspectionItemId())).map(MeProductInspectionItemsOrderNcCode::getNcCode).count();
            long count = meProductInspectionItemsOrderNcCodeList.stream().map(MeProductInspectionItemsOrderNcCode::getNcCode).distinct().count();
            if(count < meProductInspectionItemsOrderNcCodeList.size()){
                throw new CommonException("检验项目的不良代码存在重复！", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
            for (MeProductInspectionItemsOrderNcCode meProductInspectionItemsOrderNcCode : meProductInspectionItemsOrderNcCodeList) {
                meProductInspectionItemsOrderNcCode.setBo(null);
                meProductInspectionItemsOrderNcCode.setOrderBo(orderBo);
                meProductInspectionItemsOrderNcCode.setInspectionItemId(inspectionItemId);
                meProductInspectionItemsOrderNcCode.setItemType(itemType);
                meProductInspectionItemsOrderNcCode.setSite(site);
                meProductInspectionItemsOrderNcCode.setCreateDate(new Date());
                meProductInspectionItemsOrderNcCode.setCreateUser(userName);
                ResponseData saveResponseData = meProductInspectionItemsOrderNcCodeService.save(meProductInspectionItemsOrderNcCode);
                Assert.valid(!saveResponseData.isSuccess(), saveResponseData.getMsg());
            }
        }
        return true;
    }

    @Override
    @Transactional
    public boolean saveList(List<MeProductInspectionItemsOrderEntity> meProductInspectionItemsOrderEntityList) throws CommonException {
        String site = UserUtils.getSite();
        // 先删除工单检验项副本
        for(MeProductInspectionItemsOrderEntity meProductInspectionItemsOrderEntity : meProductInspectionItemsOrderEntityList){
            String orderBo = meProductInspectionItemsOrderEntity.getOrderBo();
            if(orderBo == null || "".equals(orderBo)){
                throw new CommonException("工单检验项目保存参数orderBo不能为空！", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
            deleteOrderItems(orderBo);
            ResponseData deleteResponseData = meProductInspectionItemsOrderNcCodeService.deleteNcCodesByOrderBo(orderBo);
            Assert.valid(!deleteResponseData.isSuccess(), deleteResponseData.getMsg());
//            if(orderBo != null || !"".equals(orderBo)){
//                deleteOrderItems(orderBo);
//            }
        }
        // 保存工单检验项副本
        for(MeProductInspectionItemsOrderEntity meProductInspectionItemsOrderEntity : meProductInspectionItemsOrderEntityList){
            String orderBo = meProductInspectionItemsOrderEntity.getOrderBo();
            if(orderBo == null || "".equals(orderBo)){
                throw new CommonException("工单检验项目保存参数orderBo不能为空！", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
            Integer id = meProductInspectionItemsOrderEntity.getId();
            int i = 0;
            if(id == null || "".equals(id)){
                i = meProductInspectionItemsOrderMapper.insert(meProductInspectionItemsOrderEntity);
                if(i != 1){
                    throw new CommonException("新增保存失败！", CommonExceptionDefinition.BASIC_EXCEPTION);
                }
            }else{
                i = meProductInspectionItemsOrderMapper.updateById(meProductInspectionItemsOrderEntity);
                if(i != 1){
                    throw new CommonException("修改保存失败！", CommonExceptionDefinition.BASIC_EXCEPTION);
                }
            }

            // 不良代码集合
            Integer inspectionItemId = meProductInspectionItemsOrderEntity.getId();
            String itemType = meProductInspectionItemsOrderEntity.getItemType();
            if(inspectionItemId == null || "".equals(inspectionItemId)){
                throw new CommonException("不良代码无法获取inspectionItemId参数！", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            if(itemType == null || "".equals(itemType)){
                // throw new CommonException("不良代码无法获取itemType参数！", CommonExceptionDefinition.VERIFY_EXCEPTION);
                itemType = "0";
            }
            // ResponseData deleteResponseData = meProductInspectionItemsOrderNcCodeService.deleteByInspectionItemId(orderBo,inspectionItemId);
            // Assert.valid(!deleteResponseData.isSuccess(), deleteResponseData.getMsg());
            List<MeProductInspectionItemsOrderNcCode> meProductInspectionItemsOrderNcCodeList = meProductInspectionItemsOrderEntity.getMeProductInspectionItemsOrderNcCodeList();
            if(meProductInspectionItemsOrderNcCodeList != null && !"".equals(meProductInspectionItemsOrderNcCodeList)){
                for (MeProductInspectionItemsOrderNcCode meProductInspectionItemsOrderNcCode : meProductInspectionItemsOrderNcCodeList) {
                    meProductInspectionItemsOrderNcCode.setBo(null);
                    meProductInspectionItemsOrderNcCode.setOrderBo(orderBo);
                    meProductInspectionItemsOrderNcCode.setInspectionItemId(inspectionItemId);
                    meProductInspectionItemsOrderNcCode.setItemType(itemType);
                    meProductInspectionItemsOrderNcCode.setSite(site);
                    ResponseData saveResponseData = meProductInspectionItemsOrderNcCodeService.save(meProductInspectionItemsOrderNcCode);
                    Assert.valid(!saveResponseData.isSuccess(), saveResponseData.getMsg());
                }
            }
        }
        return true;
    }

    /**
     * 删除工单检验项目副本
     */
    @Override
    public int deleteOrderItems(String orderBo) throws CommonException {
        String site = UserUtils.getSite();
        if(orderBo == null || "".equals(orderBo)){
            throw new CommonException("删除工单检验项目时参数orderBo不能为空！", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        LambdaQueryWrapper<MeProductInspectionItemsOrderEntity> query = new QueryWrapper<MeProductInspectionItemsOrderEntity>().lambda()
                .eq(MeProductInspectionItemsOrderEntity::getOrderBo, orderBo);
        int i = meProductInspectionItemsOrderMapper.delete(query);
        return i;
    }

}

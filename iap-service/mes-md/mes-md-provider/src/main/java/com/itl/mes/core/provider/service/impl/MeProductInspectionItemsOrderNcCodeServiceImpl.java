package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.NcCodeHandleBO;
import com.itl.mes.core.api.entity.MeProductInspectionItemsOrderNcCode;
import com.itl.mes.core.api.entity.NcCode;
import com.itl.mes.core.api.service.MeProductInspectionItemsOrderNcCodeService;
import com.itl.mes.core.provider.mapper.MeProductInspectionItemsOrderNcCodeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service
public class MeProductInspectionItemsOrderNcCodeServiceImpl extends ServiceImpl<MeProductInspectionItemsOrderNcCodeMapper, MeProductInspectionItemsOrderNcCode> implements MeProductInspectionItemsOrderNcCodeService {


    @Resource
    private MeProductInspectionItemsOrderNcCodeMapper meProductInspectionItemsOrderNcCodeMapper;

    @Override
    public boolean checkItemsNcCode(MeProductInspectionItemsOrderNcCode meProductInspectionItemsOrderNcCode) throws CommonException {

        String site = UserUtils.getSite();
        String bo = meProductInspectionItemsOrderNcCode.getBo();
        String orderBo = meProductInspectionItemsOrderNcCode.getOrderBo();
        String ncBo = meProductInspectionItemsOrderNcCode.getNcBo();
        String ncCode = meProductInspectionItemsOrderNcCode.getNcCode();
        String ncName = meProductInspectionItemsOrderNcCode.getNcName();
        String ncDesc = meProductInspectionItemsOrderNcCode.getNcDesc();
        String ncType = meProductInspectionItemsOrderNcCode.getNcType();
        Integer serialNum = meProductInspectionItemsOrderNcCode.getSerialNum();
        Integer inspectionItemId = meProductInspectionItemsOrderNcCode.getInspectionItemId();
        String itemType = meProductInspectionItemsOrderNcCode.getItemType();

//        if(ncBo == null || "".equals(ncBo)){
//            throw new CommonException("????????????ncBo???????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
//        }
        if(orderBo == null || "".equals(orderBo)){
            throw new CommonException("??????orderBo???????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if(ncCode == null || "".equals(ncCode)){
            throw new CommonException("????????????ncCode???????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if(serialNum == null || "".equals(serialNum)){
            throw new CommonException("?????????????????????????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if(inspectionItemId == null || "".equals(inspectionItemId)){
            throw new CommonException("??????????????????(inspectionItemId)???????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        // @ApiModelProperty("???????????????????????????0?????????1????????????")
        if(itemType == null || "".equals(itemType)){
            itemType = "0";
            // throw new CommonException("????????????????????????(itemType)???????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }else if(!"1".equals(itemType) && !"0".equals(itemType)){
            throw new CommonException("????????????????????????(itemType)???????????????0???1", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        NcCode ncCodeObj = new NcCode();
        ncCodeObj.setSite(site);
        NcCodeHandleBO ncCodeHandleBO = new NcCodeHandleBO(site,ncCode);
        ncCodeObj.setBo(ncCodeHandleBO.getBo());
        ncCodeObj.setNcCode(ncCode);
        NcCode selectByNcCodeObj = meProductInspectionItemsOrderNcCodeMapper.selectByNcCode(ncCodeObj);
        if(selectByNcCodeObj == null || "".equals(selectByNcCodeObj)){
            throw new CommonException("???????????????????????????????????????", CommonExceptionDefinition.BASIC_EXCEPTION);
        }

        // ????????????????????????????????????????????????
        LambdaQueryWrapper<MeProductInspectionItemsOrderNcCode> query = new QueryWrapper<MeProductInspectionItemsOrderNcCode>().lambda()
                .eq(MeProductInspectionItemsOrderNcCode::getOrderBo, orderBo)
                .eq(MeProductInspectionItemsOrderNcCode::getInspectionItemId, inspectionItemId)
                .eq(MeProductInspectionItemsOrderNcCode::getNcCode, ncCode)
                .eq(MeProductInspectionItemsOrderNcCode::getItemType, itemType);
        int queryCount = meProductInspectionItemsOrderNcCodeMapper.selectCount(query);
        if(queryCount > 0){
            throw new CommonException("?????????????????????????????????"+ncCode+"???????????????", CommonExceptionDefinition.BASIC_EXCEPTION);
        }

        return true;
    }

    @Override
    @Transactional
    public boolean save(MeProductInspectionItemsOrderNcCode meProductInspectionItemsOrderNcCode) throws CommonException {
        // ??????????????????????????????
        /*String orderBo = meProductInspectionItemsOrderEntity.getOrderBo();
        if(orderBo != null || !"".equals(orderBo)){
            deleteOrderItems(orderBo);
        }*/
        Boolean checkBl = checkItemsNcCode(meProductInspectionItemsOrderNcCode);
        if(!checkBl){
            throw new CommonException("???????????????????????????", CommonExceptionDefinition.BASIC_EXCEPTION);
        }

        String site = UserUtils.getSite();
        String ncCode = meProductInspectionItemsOrderNcCode.getNcCode();
        NcCodeHandleBO ncCodeHandleBO = new NcCodeHandleBO(site,ncCode);
        meProductInspectionItemsOrderNcCode.setNcBo(ncCodeHandleBO.getBo());
        meProductInspectionItemsOrderNcCode.setSite(site);

        // ?????????????????????????????????
        String bo = meProductInspectionItemsOrderNcCode.getBo();
        int i = 0;
        if(bo == null || "".equals(bo)){
            i = meProductInspectionItemsOrderNcCodeMapper.insert(meProductInspectionItemsOrderNcCode);
            if(i != 1){
                throw new CommonException("????????????", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
        }else{
            i = meProductInspectionItemsOrderNcCodeMapper.updateById(meProductInspectionItemsOrderNcCode);
            if(i != 1){
                throw new CommonException("????????????", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
        }
        return true;
    }


    @Override
    @Transactional
    public boolean saveList(List<MeProductInspectionItemsOrderNcCode> meProductInspectionItemsOrderNcCodeList) throws CommonException {
        // ?????????????????????????????????????????????
        long count = meProductInspectionItemsOrderNcCodeList.stream().map(MeProductInspectionItemsOrderNcCode::getNcCode).distinct().count();
        if(count < meProductInspectionItemsOrderNcCodeList.size()){
            throw new CommonException("?????????????????????????????????????????????", CommonExceptionDefinition.BASIC_EXCEPTION);
        }

        String site = UserUtils.getSite();

        // ??????????????????????????????
        for(MeProductInspectionItemsOrderNcCode meProductInspectionItemsOrderNcCode : meProductInspectionItemsOrderNcCodeList){
            String orderBo = meProductInspectionItemsOrderNcCode.getOrderBo();
            Integer inspectionItemId = meProductInspectionItemsOrderNcCode.getInspectionItemId();
            if(orderBo != null && !"".equals(orderBo) && inspectionItemId != null && !"".equals(inspectionItemId)){
                LambdaQueryWrapper<MeProductInspectionItemsOrderNcCode> query = new QueryWrapper<MeProductInspectionItemsOrderNcCode>().lambda()
                        .eq(MeProductInspectionItemsOrderNcCode::getOrderBo, orderBo)
                        .eq(MeProductInspectionItemsOrderNcCode::getInspectionItemId, inspectionItemId)
                        .eq(MeProductInspectionItemsOrderNcCode::getSite, site);
                meProductInspectionItemsOrderNcCodeMapper.delete(query);
            }
        }
        // ??????????????????????????????
        for(MeProductInspectionItemsOrderNcCode meProductInspectionItemsOrderNcCode : meProductInspectionItemsOrderNcCodeList){
            Boolean checkBl = checkItemsNcCode(meProductInspectionItemsOrderNcCode);
            if(!checkBl){
                throw new CommonException("???????????????????????????", CommonExceptionDefinition.BASIC_EXCEPTION);
            }


            String ncCode = meProductInspectionItemsOrderNcCode.getNcCode();
            NcCodeHandleBO ncCodeHandleBO = new NcCodeHandleBO(site,ncCode);
            meProductInspectionItemsOrderNcCode.setNcBo(ncCodeHandleBO.getBo());
            meProductInspectionItemsOrderNcCode.setSite(site);

            // String bo = meProductInspectionItemsNcCode.getBo();
            meProductInspectionItemsOrderNcCode.setBo(null);
            int i = 0;
            i = meProductInspectionItemsOrderNcCodeMapper.insert(meProductInspectionItemsOrderNcCode);
            if(i != 1){
                throw new CommonException("????????????", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
        }
        return true;
    }

    @Override
    @Transactional
    public int deleteNcCodesByOrderBo(String orderBo) throws CommonException {
        if(orderBo == null || "".equals(orderBo)){
            throw new CommonException("???????????????orderBo???????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        LambdaQueryWrapper<MeProductInspectionItemsOrderNcCode> query = new QueryWrapper<MeProductInspectionItemsOrderNcCode>().lambda()
                .eq(MeProductInspectionItemsOrderNcCode::getOrderBo, orderBo)
                .eq(MeProductInspectionItemsOrderNcCode::getSite, UserUtils.getSite());
        int i = meProductInspectionItemsOrderNcCodeMapper.delete(query);
        return i;
    }

    @Override
    @Transactional
    public int deleteByInspectionItemId(String orderBo, Integer inspectionItemId) throws CommonException {
        if(orderBo == null || "".equals(orderBo)){
            throw new CommonException("???????????????orderBo???????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if(inspectionItemId == null || "".equals(inspectionItemId)){
            throw new CommonException("???????????????inspectionItemId???????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        LambdaQueryWrapper<MeProductInspectionItemsOrderNcCode> query = new QueryWrapper<MeProductInspectionItemsOrderNcCode>().lambda()
                .eq(MeProductInspectionItemsOrderNcCode::getOrderBo, orderBo)
                .eq(MeProductInspectionItemsOrderNcCode::getInspectionItemId, inspectionItemId)
                .eq(MeProductInspectionItemsOrderNcCode::getSite, UserUtils.getSite());
        int i = meProductInspectionItemsOrderNcCodeMapper.delete(query);
        return i;
    }

    @Override
    @Transactional
    public int deleteByInspectionItemIdItemType(String orderBo, Integer inspectionItemId, String itemType) throws CommonException {
        if(orderBo == null || "".equals(orderBo)){
            throw new CommonException("???????????????orderBo???????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if(inspectionItemId == null || "".equals(inspectionItemId)){
            throw new CommonException("???????????????inspectionItemId???????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if(itemType == null || "".equals(itemType)){
            throw new CommonException("???????????????itemType???????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        LambdaQueryWrapper<MeProductInspectionItemsOrderNcCode> query = new QueryWrapper<MeProductInspectionItemsOrderNcCode>().lambda()
                .eq(MeProductInspectionItemsOrderNcCode::getOrderBo, orderBo)
                .eq(MeProductInspectionItemsOrderNcCode::getInspectionItemId, inspectionItemId)
                .eq(MeProductInspectionItemsOrderNcCode::getItemType, itemType)
                .eq(MeProductInspectionItemsOrderNcCode::getSite, UserUtils.getSite());
        int i = meProductInspectionItemsOrderNcCodeMapper.delete(query);
        return i;
    }

    @Override
    public List<MeProductInspectionItemsOrderNcCode> listByOrderAndInspection(String orderBo, Integer inspectionItemId) {
        return lambdaQuery().eq(MeProductInspectionItemsOrderNcCode::getOrderBo, orderBo)
                .eq(MeProductInspectionItemsOrderNcCode::getInspectionItemId, inspectionItemId)
                .list();
    }
}

package com.itl.mes.me.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.NcCodeHandleBO;
import com.itl.mes.core.api.entity.NcCode;
import com.itl.mes.me.api.entity.MeProductInspectionItemsNcCode;
import com.itl.mes.me.api.service.MeProductInspectionItemsNcCodeService;
import com.itl.mes.me.api.vo.MeProductInspectionItemsNcCodeVo;
import com.itl.mes.me.provider.mapper.MeProductInspectionItemsNcCodeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service("meProductInspectionItemsNcCodeService")
public class MeProductInspectionItemsNcCodeServiceImpl extends ServiceImpl<MeProductInspectionItemsNcCodeMapper, MeProductInspectionItemsNcCode> implements MeProductInspectionItemsNcCodeService {


    @Resource
    private MeProductInspectionItemsNcCodeMapper meProductInspectionItemsNcCodeMapper;

    @Override
    public Boolean checkItemsNcCode(MeProductInspectionItemsNcCode meProductInspectionItemsNcCode) throws CommonException {
        String bo = meProductInspectionItemsNcCode.getBo();
        String ncBo = meProductInspectionItemsNcCode.getNcBo();
        String ncCode = meProductInspectionItemsNcCode.getNcCode();
        String ncName = meProductInspectionItemsNcCode.getNcName();
        String ncDesc = meProductInspectionItemsNcCode.getNcDesc();
        String ncType = meProductInspectionItemsNcCode.getNcType();
        Integer serialNum = meProductInspectionItemsNcCode.getSerialNum();
        Integer inspectionItemId = meProductInspectionItemsNcCode.getInspectionItemId();
        String itemType = meProductInspectionItemsNcCode.getItemType();

//        if(ncBo == null || "".equals(ncBo)){
//            throw new CommonException("????????????ncBo???????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
//        }
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
            throw new CommonException("????????????????????????(itemType)???????????????", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }else if(!"1".equals(itemType) && !"0".equals(itemType)){
            throw new CommonException("????????????????????????(itemType)???????????????0???1", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        NcCode ncCodeObj = new NcCode();
        ncCodeObj.setSite(UserUtils.getSite());
        ncCodeObj.setBo(ncBo);
        ncCodeObj.setNcCode(ncCode);
        NcCode selectByNcCodeObj = meProductInspectionItemsNcCodeMapper.selectByNcCode(ncCodeObj);
        if(selectByNcCodeObj == null || "".equals(selectByNcCodeObj)){
            throw new CommonException("???????????????????????????????????????", CommonExceptionDefinition.BASIC_EXCEPTION);
        }

        // ????????????????????????????????????????????????
        LambdaQueryWrapper<MeProductInspectionItemsNcCode> query = new QueryWrapper<MeProductInspectionItemsNcCode>().lambda()
                .eq(MeProductInspectionItemsNcCode::getInspectionItemId, inspectionItemId)
                .eq(MeProductInspectionItemsNcCode::getNcCode, ncCode)
                .eq(MeProductInspectionItemsNcCode::getItemType, itemType);
        int queryCount = meProductInspectionItemsNcCodeMapper.selectCount(query);
        if(queryCount > 0){
            throw new CommonException("???????????????????????????????????????"+ncCode+"???????????????", CommonExceptionDefinition.BASIC_EXCEPTION);
        }

        return true;
    }

    @Override
    @Transactional
    public Boolean saveList(List<MeProductInspectionItemsNcCode> meProductInspectionItemsNcCodeList) throws CommonException {
        // ?????????????????????????????????????????????
        long count = meProductInspectionItemsNcCodeList.stream().map(MeProductInspectionItemsNcCode::getNcCode).distinct().count();
        if(count < meProductInspectionItemsNcCodeList.size()){
            throw new CommonException("???????????????????????????????????????????????????", CommonExceptionDefinition.BASIC_EXCEPTION);
        }

        // ??????????????????????????????
        for(MeProductInspectionItemsNcCode meProductInspectionItemsNcCode : meProductInspectionItemsNcCodeList){
            Integer inspectionItemId = meProductInspectionItemsNcCode.getInspectionItemId();
            if(inspectionItemId != null && !"".equals(inspectionItemId)){
                LambdaQueryWrapper<MeProductInspectionItemsNcCode> query = new QueryWrapper<MeProductInspectionItemsNcCode>().lambda()
                        .eq(MeProductInspectionItemsNcCode::getInspectionItemId, inspectionItemId);
                meProductInspectionItemsNcCodeMapper.delete(query);
            }
        }
        // ??????????????????????????????
        for(MeProductInspectionItemsNcCode meProductInspectionItemsNcCode : meProductInspectionItemsNcCodeList){
            Boolean checkBl = checkItemsNcCode(meProductInspectionItemsNcCode);
            if(!checkBl){
                throw new CommonException("???????????????????????????", CommonExceptionDefinition.BASIC_EXCEPTION);
                //return ResponseData.error("?????????????????????????????????????????????");
            }

            // ??????ncCode??????ncBo??????
            String site = UserUtils.getSite();
            String ncCode = meProductInspectionItemsNcCode.getNcCode();
            NcCodeHandleBO ncCodeHandleBO = new NcCodeHandleBO(site,ncCode);
            meProductInspectionItemsNcCode.setNcBo(ncCodeHandleBO.getBo());
            meProductInspectionItemsNcCode.setSite(site);

            // String bo = meProductInspectionItemsNcCode.getBo();
            meProductInspectionItemsNcCode.setBo(null);
            int i = 0;
            i = meProductInspectionItemsNcCodeMapper.insert(meProductInspectionItemsNcCode);
            if(i != 1){
                throw new CommonException("????????????", CommonExceptionDefinition.BASIC_EXCEPTION);
                // return ResponseData.error("???????????????");
            }
        }
        return true;
    }

    @Override
    public List<MeProductInspectionItemsNcCodeVo> listItemNcCodesTwo(MeProductInspectionItemsNcCodeVo meProductInspectionItemsNcCodeVo) {
        return meProductInspectionItemsNcCodeMapper.listItemNcCodesTwo(meProductInspectionItemsNcCodeVo);
    }


}

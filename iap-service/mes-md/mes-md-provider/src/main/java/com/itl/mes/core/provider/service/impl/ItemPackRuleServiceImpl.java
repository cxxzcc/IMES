package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;

import com.itl.mes.core.api.bo.ItemHandleBO;
import com.itl.mes.core.api.dto.ItemPackRuleDetailDto;
import com.itl.mes.core.api.entity.ItemPackRuleDetail;

import com.itl.mes.core.api.service.ItemPackRuleService;

import com.itl.mes.core.api.service.PackingRuleService;
import com.itl.mes.core.provider.mapper.ItemPackRuleMapper;
import org.springframework.beans.BeanUtils;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author FengRR
 * @date 2021/5/28
 * @since JDK1.8
 */
@Service
public class ItemPackRuleServiceImpl extends ServiceImpl<ItemPackRuleMapper , ItemPackRuleDetail> implements ItemPackRuleService {


    @Resource
    private UserUtil userUtil;

    @Resource
    private PackingRuleService packingRuleService;

    @Override
    public void saveItemAndPackRule(String itemBo,List<ItemPackRuleDetailDto> itemPackRuleDetailDtoList) throws CommonException {

        //首先删除物料跟包装规则之间的关联
        QueryWrapper<ItemPackRuleDetail> wrapper = new QueryWrapper<>();
        wrapper.eq( "ITEM_BO" , itemBo);
        super.remove(wrapper);
        //把物料和对应的包装规则写入关系表

        for(int i = 0; i < itemPackRuleDetailDtoList.size(); i++){

                ItemPackRuleDetail itemPackRule = new ItemPackRuleDetail();
                BeanUtils.copyProperties(itemPackRuleDetailDtoList.get(i),itemPackRule);
                String relBo =  "IPR" + ":" + UserUtils.getSite() + "," + UUID.randomUUID().toString() ;
                itemPackRule.setBo(relBo);
                itemPackRule.setCreateDate(new Date());
                //itemPackRule.setMaxQty(itemPackRuleDetailDtoList.get(i).getMaxQty());
                //itemPackRule.setMinQty(itemPackRuleDetailDtoList.get(i).getMinQty());
                itemPackRule.setCreateUser(userUtil.getUser().getUserName());
                itemPackRule.setSite(UserUtils.getSite());
                itemPackRule.setItemBo(itemBo);
                super.save(itemPackRule);

        }
    }

    @Override
    public List<ItemPackRuleDetailDto> getItemAndPackRule(String item, String version) {
        ItemHandleBO itemHandleBO = new ItemHandleBO( UserUtils.getSite(), item, version);
        QueryWrapper<ItemPackRuleDetail> wrapper = new QueryWrapper<>();
        wrapper.eq( "ITEM_BO",itemHandleBO.getBo() );
        List<ItemPackRuleDetail> itemPackRules = super.list(wrapper);
        List<ItemPackRuleDetailDto> itemPackRuleDtoList = new ArrayList<>();


        for(int i = 0; i < itemPackRules.size(); i++){
            ItemPackRuleDetailDto itemPackRuleDetailDto = new ItemPackRuleDetailDto();
            BeanUtils.copyProperties(itemPackRules.get(i),itemPackRuleDetailDto);
            itemPackRuleDtoList.add(itemPackRuleDetailDto);
        }
        return itemPackRuleDtoList;
    }

    //根据规则BO获得规则名称
    private String getPackRule(String bo){

        return packingRuleService.getById(bo).getPackRule();
    }

}


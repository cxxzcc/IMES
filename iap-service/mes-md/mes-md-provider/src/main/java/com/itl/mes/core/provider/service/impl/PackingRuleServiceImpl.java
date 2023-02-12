package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.PackRuleDetailHandleBo;
import com.itl.mes.core.api.bo.PackingHandleBO;
import com.itl.mes.core.api.bo.PackingRuleHandleBo;
import com.itl.mes.core.api.dto.PackRuleDetailDto;
import com.itl.mes.core.api.dto.PackingRuleDto;
import com.itl.mes.core.api.entity.PackRuleDetail;
import com.itl.mes.core.api.entity.PackingRule;
import com.itl.mes.core.api.entity.WorkShop;
import com.itl.mes.core.api.service.PackingRuleService;
import com.itl.mes.core.provider.mapper.PackingRuleDetailMapper;
import com.itl.mes.core.provider.mapper.PackingRuleMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author FengRR
 * @date 2021/5/26
 * @since JDK1.8
 */
@Service
public class PackingRuleServiceImpl extends ServiceImpl<PackingRuleMapper , PackingRule> implements PackingRuleService {

    @Resource
    private PackingRuleMapper packingRuleMapper;

    @Resource
    private PackingRuleDetailMapper packingRuleDetailMapper;

    @Resource
    private  UserUtil userUtil;

    @Override
    public IPage<PackingRuleDto> findList(PackingRuleDto packingRuleDto) throws CommonException {

        if (ObjectUtil.isEmpty(packingRuleDto.getPage())) {
            packingRuleDto.setPage(new Page(0, 10));
        }
        QueryWrapper queryWrapper = new QueryWrapper<PackingRule>();
        queryWrapper.eq("SITE", UserUtils.getSite());

        if (StrUtil.isNotEmpty(packingRuleDto.getPackRule())) {
            queryWrapper.like("PACK_RULE", packingRuleDto.getPackRule());
        }
        if (StrUtil.isNotEmpty(packingRuleDto.getItemType())) {
            queryWrapper.like("ITEM_TYPE", packingRuleDto.getItemType());
        }
        queryWrapper.orderByDesc("CREATE_DATE");
        return packingRuleMapper.selectPage(packingRuleDto.getPage(), queryWrapper);
    }

    @Override
    public PackingRuleDto findPackRuleDelList(PackingRuleDto packingRuleDto) throws CommonException {

        //查询细节
        QueryWrapper queryWrapper = new QueryWrapper<PackRuleDetail>();
        queryWrapper.eq("SITE", UserUtils.getSite());
        queryWrapper.eq("RULE_BO", packingRuleDto.getBo());
        queryWrapper.orderByDesc("CREATE_DATE");

        //查主表
        PackingRule packingRule  = packingRuleMapper.selectById(packingRuleDto.getBo());
        PackingRuleDto dto = new PackingRuleDto();
        BeanUtils.copyProperties(packingRule, dto);
        List<PackRuleDetail> packRuleDetailList = packingRuleDetailMapper.selectList(queryWrapper);
        List<PackRuleDetailDto> list = new ArrayList<>();
        for(int i = 0; i < packRuleDetailList.size(); i++){
            PackRuleDetailDto packRuleDetailDto = new PackRuleDetailDto();
            BeanUtils.copyProperties(packRuleDetailList.get(i), packRuleDetailDto);
            packRuleDetailDto.setRuleMould(packRuleDetailList.get(i).getRuleMould());
            list.add(packRuleDetailDto);
        }
        dto.setPackRuleDetailDtoList(list);
        return dto;
    }

    @Override
    public PackingRuleDto findRule(String rule) throws CommonException {

        PackingRuleHandleBo packingRuleHandleBo = new PackingRuleHandleBo(UserUtils.getSite(),rule);
        PackingRule packingRuleEntity = packingRuleMapper.selectById(packingRuleHandleBo.getBo());
        PackingRuleDto packingRuleDto = new PackingRuleDto();

        //查询明细
        QueryWrapper queryWrapper = new QueryWrapper<PackRuleDetail>();
        queryWrapper.eq("SITE", UserUtils.getSite());
        queryWrapper.eq("RULE_BO", packingRuleHandleBo.getBo());
        List<PackRuleDetail> packRuleDetailList = packingRuleDetailMapper.selectList(queryWrapper);
        List<PackRuleDetailDto> list = new ArrayList<>();
        if(packingRuleEntity != null){
            for(int i = 0; i < packRuleDetailList.size(); i++){
                PackRuleDetailDto packRuleDetailDto = new PackRuleDetailDto();
                BeanUtils.copyProperties(packRuleDetailList.get(i), packRuleDetailDto);
                packRuleDetailDto.setSite(UserUtils.getSite());
                list.add(packRuleDetailDto);
            }
            BeanUtils.copyProperties(packingRuleEntity, packingRuleDto);
            packingRuleDto.setPackRuleDetailDtoList(list);
            return packingRuleDto;
        }else{
            return null;
        }
    }

    @Override
    public void updatePackingRule(PackingRuleDto packingRuleDto) {

        PackingRule packingRule = new PackingRule();
        BeanUtil.copyProperties(packingRuleDto, packingRule);
        packingRule.setModifyDate(new Date());
        packingRule.setModifyUser(userUtil.getUser().getUserName());
        packingRuleMapper.updateById(packingRule);

    }

    @Override
    public void deletePackingRule(List<String> bos) {
        for (int i = 0; i < list().size(); i++) {
            String handleBo = list().get(i).getBo();
            QueryWrapper<PackRuleDetail> wrapper = new QueryWrapper<>();
            wrapper.eq( "RULE_BO", handleBo );
            Integer integer = packingRuleDetailMapper.delete( wrapper );
        }
        packingRuleMapper.deleteBatchIds(bos);
    }

    @Override
    public void addPackingRule(PackingRuleDto packingRuleDto) throws CommonException {
        if(packingRuleDto.getPackRule().equals("")) {
            throw new CommonException("包装规则是必填项", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

        PackingRuleHandleBo packRuleHandleBo = new PackingRuleHandleBo(UserUtils.getSite(),packingRuleDto.getPackRule());
        PackingRule packingRuleEntity = getPackRuleByPackRuleHandleBO(packRuleHandleBo.getBo());
        PackingRule packingRule = new PackingRule();
        if(packingRuleEntity != null){
            throw new CommonException("包装名称已存在，请重新输入", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }else{
            BeanUtils.copyProperties(packingRuleDto,packingRule);
            packingRule.setBo(packRuleHandleBo.getBo());
            packingRule.setCreateDate(new Date());
            packingRule.setCreateUser(userUtil.getUser().getUserName());
            packingRule.setSite(UserUtils.getSite());
            packingRuleMapper.insert(packingRule);

            //明细的个数
            int num = packingRuleDto.getPackRuleDetailDtoList().size();
            for (int i = 0; i < num ; i++){
                if( packingRuleDto.getPackRuleDetailDtoList().get(i).getBo() == null || packingRuleDto.getPackRuleDetailDtoList().get(i).getBo().equals("")) {
                    PackRuleDetailHandleBo detailHandleBo = new PackRuleDetailHandleBo(UserUtils.getSite() ,
                            UUID.randomUUID().toString(),
                            packingRuleDto.getPackRuleDetailDtoList().get(i).getSeq());
                    PackRuleDetail packRuleDetail = new PackRuleDetail();
                    BeanUtils.copyProperties(packingRuleDto.getPackRuleDetailDtoList().get(i),packRuleDetail);
                    packRuleDetail.setBo(detailHandleBo.getBo());
                    if(packingRuleDto.getBo() == null || packingRuleDto.getBo().equals("")){
                        PackingRuleHandleBo packingRuleHandleBo = new PackingRuleHandleBo(UserUtils.getSite(),packingRuleDto.getPackRule());
                        packRuleDetail.setRuleBo(packingRuleHandleBo.getBo());
                    }else{
                        packRuleDetail.setRuleBo(packingRuleDto.getBo());
                    }
                    packRuleDetail.setSite(UserUtils.getSite());
                    packRuleDetail.setCreateDate(new Date());
                    packRuleDetail.setCreateUser(userUtil.getUser().getUserName());
                    packingRuleDetailMapper.insert(packRuleDetail);
                }else{
                    PackRuleDetail packRuleDetail = new PackRuleDetail();
                    BeanUtils.copyProperties(packingRuleDto.getPackRuleDetailDtoList().get(i),packRuleDetail);
                    packRuleDetail.setModifyDate(new Date());
                    packRuleDetail.setModifyUser(userUtil.getUser().getUserName());
                    packRuleDetail.setSite(UserUtils.getSite());
                    packingRuleDetailMapper.updateById(packRuleDetail);
                }
            }
        }




    }

    @Override
    public void savePackingRule(PackingRuleDto packingRuleDto) throws CommonException {
        if(packingRuleDto.getPackRule().equals("")) {
            throw new CommonException("包装规则是必填项", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        PackingRuleHandleBo packRuleHandleBo = new PackingRuleHandleBo(UserUtils.getSite(),packingRuleDto.getPackRule());
        PackingRule packingRuleEntity = getPackRuleByPackRuleHandleBO(packRuleHandleBo.getBo());
        PackingRule packingRule = new PackingRule();

        if(packingRuleEntity != null  && packingRuleEntity.getBo().equals(packingRuleDto.getBo())){
            BeanUtils.copyProperties(packingRuleDto,packingRule);
            packingRule.setBo(packingRuleDto.getBo());
            packingRule.setModifyDate(new Date());
            packingRule.setSite(UserUtils.getSite());
            packingRule.setModifyUser(userUtil.getUser().getUserName());
            packingRuleMapper.updateById(packingRule);

            //明细的个数
            int num = packingRuleDto.getPackRuleDetailDtoList().size();
            for (int i = 0; i < num ; i++){
                if( packingRuleDto.getPackRuleDetailDtoList().get(i).getBo() == null || packingRuleDto.getPackRuleDetailDtoList().get(i).getBo().equals("")) {
                    PackRuleDetailHandleBo detailHandleBo = new PackRuleDetailHandleBo(UserUtils.getSite() ,
                            UUID.randomUUID().toString(),
                            packingRuleDto.getPackRuleDetailDtoList().get(i).getSeq());
                    PackRuleDetail packRuleDetail = new PackRuleDetail();
                    BeanUtils.copyProperties(packingRuleDto.getPackRuleDetailDtoList().get(i),packRuleDetail);
                    packRuleDetail.setBo(detailHandleBo.getBo());
                    packRuleDetail.setCreateDate(new Date());
                    if(packingRuleDto.getBo() == null || packingRuleDto.getBo().equals("")){
                        PackingRuleHandleBo packingRuleHandleBo = new PackingRuleHandleBo(UserUtils.getSite(),packingRuleDto.getPackRule());
                        packRuleDetail.setRuleBo(packingRuleHandleBo.getBo());
                    }else{
                        packRuleDetail.setRuleBo(packingRuleDto.getBo());
                    }
                    packRuleDetail.setSite(UserUtils.getSite());
                    packRuleDetail.setCreateUser(userUtil.getUser().getUserName());
                    packingRuleDetailMapper.insert(packRuleDetail);
                }else{
                    PackRuleDetail packRuleDetail = new PackRuleDetail();
                    BeanUtils.copyProperties(packingRuleDto.getPackRuleDetailDtoList().get(i),packRuleDetail);
                    packRuleDetail.setModifyDate(new Date());
                    packRuleDetail.setModifyUser(userUtil.getUser().getUserName());
                    packRuleDetail.setSite(UserUtils.getSite());
                    packingRuleDetailMapper.updateById(packRuleDetail);
                }
            }
        }else{
            throw new CommonException("包装规则已存在，请重新输入", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
    }

    @Override
    public void deletePackingRuleDel(List<String> bos) {
        packingRuleDetailMapper.deleteBatchIds(bos);
    }

    @Override
    public String getPackRule(String bo) {

        return packingRuleMapper.selectById(bo).getPackRule();
    }

    @Override
    public PackingRule getPackRuleByPackRuleHandleBO(String packRuleHandleBO) {
        return packingRuleMapper.selectById(packRuleHandleBO);
    }
}


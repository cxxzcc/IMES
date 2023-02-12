package com.itl.iap.system.provider.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.constants.CacheConstants;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.util.CodeUtils;
import com.itl.iap.common.util.UUID;
import com.itl.iap.system.api.entity.IapDictItemT;
import com.itl.iap.system.api.entity.IapDictT;
import com.itl.iap.system.provider.mapper.IapDictItemMapper;
import com.itl.iap.system.api.dto.IapDictItemTDto;
import com.itl.iap.system.api.service.IapDictItemTService;
import com.itl.iap.system.provider.mapper.IapDictMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 字典详情表服务实现类
 *
 * @author 李骐光
 * @since 2020-06-16 10:07:17
 */
@Service
@Slf4j
public class IapDictItemTServiceImpl extends ServiceImpl<IapDictItemMapper, IapDictItemT> implements IapDictItemTService {
    @Autowired
    private IapDictItemMapper iapDictItemMapper;
    @Autowired
    private IapDictMapper iapDictMapper;

    /**
     * 新增字典
     *
     * @param iapDictItemTDto 字典详情
     * @return 新增数量
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    @CacheEvict(
            cacheNames = {CacheConstants.DICT_ITEM_CACHE_BASIC_PREFIX},
            key = "#iapDictItemTDto.parentCode",
            condition = "#result > 0"
    )
    public Integer insertIapDictItemT(IapDictItemTDto iapDictItemTDto) throws CommonException {
        LambdaQueryWrapper<IapDictItemT> query = new QueryWrapper<IapDictItemT>().lambda()
                .eq(IapDictItemT::getIapDictId, iapDictItemTDto.getIapDictId())
                .eq(IapDictItemT::getKeyValue, iapDictItemTDto.getKeyValue());
        if (CollUtil.isNotEmpty(iapDictItemMapper.selectList(query))) {
            throw new CommonException("字典key不能重复!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }else{
            LambdaQueryWrapper<IapDictItemT> queryCode = new QueryWrapper<IapDictItemT>().lambda()
                    .eq(IapDictItemT::getIapDictId, iapDictItemTDto.getIapDictId())
                    .eq(IapDictItemT::getCode, iapDictItemTDto.getCode());
            if (CollUtil.isNotEmpty(iapDictItemMapper.selectList(queryCode))) {
                throw new CommonException("字典编码不能重复!", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
        }
        try {
            IapDictItemT iapDictItemT = new IapDictItemT();
            iapDictItemTDto.setId(UUID.uuid32());
            if (iapDictItemTDto.getCode() == null && StringUtils.isEmpty(iapDictItemTDto.getCode())) {
                this.setCode(iapDictItemTDto, new Date(), 0);
            }
            BeanUtils.copyProperties(iapDictItemTDto, iapDictItemT);
            return iapDictItemMapper.insert(iapDictItemT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 修改字典
     *
     * @param iapDictItemDto 字典详情
     * @return 修改数量
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    @CacheEvict(
          cacheNames = {CacheConstants.DICT_ITEM_CACHE_BASIC_PREFIX},
          key = "#iapDictItemDto.parentCode",
          condition = "#result > 0"
    )
    public Integer updateIapDictItemT(IapDictItemTDto iapDictItemDto) throws CommonException {
        LambdaQueryWrapper<IapDictItemT> query = new QueryWrapper<IapDictItemT>().lambda()
                .ne(IapDictItemT::getId, iapDictItemDto.getId())
                .eq(IapDictItemT::getIapDictId, iapDictItemDto.getIapDictId())
                .eq(IapDictItemT::getKeyValue, iapDictItemDto.getKeyValue());
        if (CollUtil.isNotEmpty(iapDictItemMapper.selectList(query))) {
            throw new CommonException("字典key不能重复!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }else{
            LambdaQueryWrapper<IapDictItemT> queryCode = new QueryWrapper<IapDictItemT>().lambda()
                    .ne(IapDictItemT::getId, iapDictItemDto.getId())
                    .eq(IapDictItemT::getIapDictId, iapDictItemDto.getIapDictId())
                    .eq(IapDictItemT::getCode, iapDictItemDto.getCode());
            if (CollUtil.isNotEmpty(iapDictItemMapper.selectList(queryCode))) {
                throw new CommonException("字典编码不能重复!", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
        }
        IapDictItemT iapDictItemT = new IapDictItemT();
        BeanUtils.copyProperties(iapDictItemDto, iapDictItemT);
        return iapDictItemMapper.updateById(iapDictItemT);
    }

    /**
     * 通过id批量删除数据
     *
     * @param ids 主键集合
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    @CacheEvict(
            cacheNames = {CacheConstants.DICT_ITEM_CACHE_BASIC_PREFIX},
            key = "#parentCode"
    )
    public boolean deleteByIds(List<String> ids, String parentCode) {
        List<String> deleteList = new ArrayList<>();
        List<IapDictItemT> iapDictItemList = iapDictItemMapper.selectBatchIds(ids);
        if (iapDictItemList != null) {
            deleteList.addAll(ids);
            this.deleteByParentId(iapDictItemList, deleteList);
            iapDictItemMapper.deleteBatchIds(deleteList);
        }
        boolean b = this.removeByIds(ids);
        return b;
    }

    /**
     * 递归删除
     *
     * @param idList     传入的父idList
     * @param deleteList 需要删除的list，每次递归都会往里面加入要删除的数据
     */
    private void deleteByParentId(List<IapDictItemT> idList, List<String> deleteList) {
        List<IapDictItemT> parentList = null;
        if (idList != null && idList.size() > 0) {
            for (IapDictItemT iapDictItemT : idList) {
                parentList = this.list(new QueryWrapper<IapDictItemT>().eq("parent_id", iapDictItemT.getId()));
                if (parentList != null && parentList.size() > 0) {
                    deleteList.add(iapDictItemT.getId());
                    this.deleteByParentId(parentList, deleteList);
                }
            }
        }
    }

    /**
     * 通过字典编号和名称查询
     *
     * @param iapDictItemTDto 字典详情
     * @return 字典详情集合
     */
    @Override
    @Cacheable(
            cacheNames = {CacheConstants.DICT_ITEM_CACHE_BASIC_PREFIX},
            key = "#iapDictItemTDto.parentCode",
            condition = "#iapDictItemTDto.parentCode != null",
            unless = "#result.size() == 0"
    )
    public List<IapDictItemTDto> queryDictCodeOrName(IapDictItemTDto iapDictItemTDto) {
        return iapDictItemMapper.queryDictCodeOrName(iapDictItemTDto);
    }

    /**
     * 通过字典编号查询
     *
     * @param dictCode 字典编码
     * @return 字典详情集合
     */
    @Override
    @Cacheable(
        cacheNames = {CacheConstants.DICT_ITEM_CACHE_BASIC_PREFIX},
        key = "#dictCode",
        unless = "#result.size() == 0"
    )
    public List<IapDictItemTDto> queryDictCode(String dictCode) {
        return iapDictItemMapper.queryDictCode(dictCode);
    }

    /**
     * 通过IapDictId查询
     *
     * @param ids 字典主表id集合
     * @return 字典详情集合
     */
    @Override
    public List<IapDictItemTDto> selectByIapDictIdList(List<String> ids) {
        return iapDictItemMapper.selectByIapDictIdList(ids);
    }

    /**
     * 根据字典主表Code查字典详情取值
     *
     * @param code 字典主表code
     * @return 字典详情集合
     */
    @Override
    public List<IapDictItemT> getCode(String code) {
        QueryWrapper<IapDictT> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", code);
        IapDictT iapDictT = iapDictMapper.selectOne(queryWrapper);
        QueryWrapper<IapDictItemT> queryWrapper1 = new QueryWrapper<>();
        queryWrapper.eq("iap_dict_id", iapDictT.getId());
        return iapDictItemMapper.selectList(queryWrapper1);
    }

    @Override
    public Map<String, String> getDictItemMapByParentCode(String code) {
        if(StrUtil.isBlank(code)) {
            return Collections.emptyMap();
        }
        List<IapDictItemTDto> list = queryDictCode(code);
        if(CollUtil.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.toMap(IapDictItemTDto::getKeyValue, IapDictItemTDto::getName));
    }

    @Override
    public List<IapDictItemTDto> getItemDtoByIds(List<String> ids) {
        return baseMapper.getItemDtoByIds(ids);
    }

    /**
     * @param obj  需要设置code属性的实体类
     * @param date
     * @param num  默认0 大于 20跳出
     * @return void
     * @Description: 设置编码值
     */
    private void setCode(IapDictItemTDto obj, Date date, Integer num) throws CommonException {
        obj.setCode(CodeUtils.dateToCode("NX", date));
        if (num > CodeUtils.NUM) {
            log.error(CommonExceptionDefinition.getCurrentClassError() + "编码设置失败!");
            throw new CommonException(new CommonExceptionDefinition(500, "编码设置失败!"));
        }
        if (iapDictItemMapper.selectCount(new QueryWrapper<IapDictItemT>().ne("id", obj.getId()).eq("code", obj.getCode())) != 0) {
            num++;
            this.setCode(obj, date, num);
        }
    }
}

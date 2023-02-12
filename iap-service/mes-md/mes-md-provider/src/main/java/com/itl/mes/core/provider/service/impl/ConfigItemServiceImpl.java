package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.dto.ConfigItemDto;
import com.itl.mes.core.api.entity.ConfigItem;
import com.itl.mes.core.api.service.ConfigItemService;
import com.itl.mes.core.api.vo.ConfigItemVo;
import com.itl.mes.core.provider.mapper.ConfigItemMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 配置项 服务实现类
 * </p>
 *
 * @author chenjx1
 * @since 2021-11-9
 */
@Service
@Transactional
public class ConfigItemServiceImpl extends ServiceImpl<ConfigItemMapper, ConfigItem> implements ConfigItemService {


    @Autowired
    private ConfigItemMapper configItemMapper;


    @Override
    public List<ConfigItem> selectList(ConfigItem configItem) {
        QueryWrapper<ConfigItem> configItemWrapper = new QueryWrapper<>();
        configItem.setSite(UserUtils.getSite());
        configItemWrapper.setEntity(configItem);
        return configItemMapper.selectList(configItemWrapper);
    }

    @Override
    public IPage<ConfigItemDto> selectPageList(ConfigItemDto configItemDto) {
        if(ObjectUtil.isEmpty(configItemDto.getPage())){
            configItemDto.setPage(new Page(0, 10));
        }
        configItemDto.setSite(UserUtils.getSite());
        Page page = configItemDto.getPage();
        List<ConfigItemDto> configItemDtoList = configItemMapper.queryByItem(page, configItemDto);
        page.setRecords(configItemDtoList);
        return page;
    }

    @Override
    public void save(List<ConfigItemVo> configItemVoList) throws CommonException {
        for (ConfigItemVo configItemVo : configItemVoList) {
            ConfigItem configItem = new ConfigItem();
            String site = UserUtils.getSite();
            if(site == null || "".equals(site)){
                throw new CommonException("无法获取工厂编号！", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            if(configItemVo.getItemCode() == null || "".equals(configItemVo.getItemCode())){
                throw new CommonException("编号不能为空！", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            if(configItemVo.getConfigItemKey() == null || "".equals(configItemVo.getConfigItemKey())){
                throw new CommonException("配置项KEY不能为空！", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            if(configItemVo.getConfigItemValue() == null || "".equals(configItemVo.getConfigItemValue())){
                throw new CommonException("配置项VALUE不能为空！", CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            BeanUtils.copyProperties(configItemVo,configItem);
            configItem.setSite(site);
            if(configItem.getState() == null || "".equals(configItem.getState())){
                configItem.setState(1); // 默认为1，已启用
            }
            if(configItem.getBo() == null || "".equals(configItem.getBo())){
                ConfigItemDto configItemDto = new ConfigItemDto();
                configItemDto.setSite(configItem.getSite());
                configItemDto.setItemCode(configItem.getItemCode());
                configItemDto.setConfigItemKey(configItem.getConfigItemKey());
                List<ConfigItemDto> configItemDtoList = configItemMapper.queryByItemCode(configItemDto);
                if(configItemDtoList == null || configItemDtoList.size() >= 1){
                    throw new CommonException("该配置项KEY已存在！", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }

                configItem.setBo(IdUtil.simpleUUID());
                configItem.setCreateDate(new Date());
                configItem.setCreateUser(UserUtils.getUser());
                configItemMapper.insert(configItem);
            }else{
                ConfigItem configItem1 = configItemMapper.selectById(configItem.getBo());
                if(configItem.getItemCode() != null && !configItem.getItemCode().equals(configItem1.getItemCode())){
                    throw new CommonException("配置项ItemCode不允许修改！", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }else if(configItem.getConfigItemKey() != null && !configItem.getConfigItemKey().equals(configItem1.getConfigItemKey())){
                    throw new CommonException("配置项configItemKey不允许修改！", CommonExceptionDefinition.VERIFY_EXCEPTION);
                }
                configItem.setModifyDate(new Date());
                configItem.setModifyUser(UserUtils.getUser());
                configItemMapper.updateById(configItem);
            }
        }

    }

    @Override
    public ConfigItem selectById(String bo) throws CommonException {
        if(bo == null || "".equals(bo)){
            throw new CommonException("BO参数不能为空！", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        return configItemMapper.selectById(bo);
    }

    @Override
    public int delete(String bo) throws CommonException {
        if(bo == null || "".equals(bo)){
            throw new CommonException("BO参数不能为空！", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        return configItemMapper.deleteById(bo);
    }
}

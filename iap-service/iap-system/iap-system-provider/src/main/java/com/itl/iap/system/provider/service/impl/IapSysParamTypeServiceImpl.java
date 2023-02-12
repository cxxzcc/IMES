package com.itl.iap.system.provider.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.system.api.entity.IapSysParam;
import com.itl.iap.system.api.entity.IapSysParamType;
import com.itl.iap.system.provider.mapper.IapSysParamMapper;
import com.itl.iap.system.provider.mapper.IapSysParamTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class IapSysParamTypeServiceImpl {

    @Autowired
    private IapSysParamTypeMapper iapSysParamTypeMapper;
    @Autowired
    private IapSysParamMapper iapSysParamMapper;

    public Object findList(IapSysParamType iapSysParamType, Integer pageNum, Integer pageSize) {
        iapSysParamType.setSite(UserUtils.getSite());
        Page page = new Page(pageNum,pageSize);
        return iapSysParamTypeMapper.findList(page,iapSysParamType);
    }

    public Object findListByState(IapSysParamType iapSysParamType, Integer pageNum, Integer pageSize) {
        iapSysParamType.setSite(UserUtils.getSite());
        Page page = new Page(pageNum,pageSize);
        return iapSysParamTypeMapper.findListByState(page,iapSysParamType);
    }


    public void save(IapSysParamType iapSysParamType) {
        iapSysParamType.setSite(UserUtils.getSite());
        QueryWrapper<IapSysParamType> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq("type",iapSysParamType.getType()).eq("site",UserUtils.getSite());
        if(iapSysParamType.getId() != null) entityWrapper.ne("id",iapSysParamType.getId());
        List<IapSysParamType> iapSysParamTypes = iapSysParamTypeMapper.selectList(entityWrapper);
        if(!iapSysParamTypes.isEmpty()){
            throw new CommonException("类型不能重复！", 500);
        }
        if(iapSysParamType.getId() != null){
            iapSysParamType.setCreateDate(new Date());
            iapSysParamTypeMapper.updateById(iapSysParamType);
        }else{
            iapSysParamTypeMapper.insert(iapSysParamType);
        }
    }


    @Transactional
    public void delete(List<String> ids) throws CommonException {
        for (String id : ids) {
            final Integer integer = iapSysParamMapper.selectCount(new QueryWrapper<IapSysParam>().lambda().eq(IapSysParam::getIapSysParamTypeId, id));
            if (!integer.equals(0)) {
                throw new CommonException("数据已使用，不能执行删除操作", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
            iapSysParamTypeMapper.deleteById(id);
        }
    }


    public IapSysParamType findById(String id) {
        return iapSysParamTypeMapper.selectById(id);
    }

    public Object queryForLov(Map<String, Object> params) {
        Map<String,Object> map = (Map<String,Object>)params.get("page");
        params.remove("page");
        IapSysParamType iapSysParamType = JSONObject.parseObject(JSONObject.toJSONString(params), IapSysParamType.class);
        return findList(iapSysParamType, Integer.valueOf(map.get("current").toString()), Integer.valueOf(map.get("size").toString()));
    }
}

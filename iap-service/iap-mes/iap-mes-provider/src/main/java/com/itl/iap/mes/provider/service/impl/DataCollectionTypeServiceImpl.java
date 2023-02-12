package com.itl.iap.mes.provider.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.mes.api.entity.DataCollection;
import com.itl.iap.mes.api.entity.DataCollectionType;
import com.itl.iap.mes.provider.annotation.ParseState;
import com.itl.iap.mes.provider.common.CommonCode;
import com.itl.iap.mes.provider.config.Constant;
import com.itl.iap.mes.provider.exception.CustomException;
import com.itl.iap.mes.provider.mapper.DataCollectionMapper;
import com.itl.iap.mes.provider.mapper.DataCollectionTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class DataCollectionTypeServiceImpl{

    @Autowired
    private DataCollectionTypeMapper dataCollectionTypeMapper;
    @Autowired
    private DataCollectionMapper dataCollectionMapper;

    @ParseState(Constant.EnabledEnum.class)
    public Object findList(DataCollectionType dataCollectionType, Integer pageNum, Integer pageSize) {
        dataCollectionType.setSite(UserUtils.getSite());
        Page page = new Page(pageNum,pageSize);
        return dataCollectionTypeMapper.findList(page,dataCollectionType);
    }

    @ParseState(Constant.EnabledEnum.class)
    public Object findListByState(DataCollectionType dataCollectionType, Integer pageNum, Integer pageSize) {
        dataCollectionType.setSite(UserUtils.getSite());
        Page page = new Page(pageNum,pageSize);
        return dataCollectionTypeMapper.findListByState(page,dataCollectionType);
    }


    public void save(DataCollectionType dataCollection) {
        dataCollection.setSite(UserUtils.getSite());
        QueryWrapper<DataCollectionType> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq("type",dataCollection.getType()).eq("site",UserUtils.getSite());
        if(dataCollection.getId() != null) entityWrapper.ne("id",dataCollection.getId());
        List<DataCollectionType> dataCollectionTypes = dataCollectionTypeMapper.selectList(entityWrapper);
        if(!dataCollectionTypes.isEmpty()){
            throw new CustomException(CommonCode.TYPE_REPEAT);
        }
        if(dataCollection.getId() != null){
            dataCollection.setCreateDate(new Date());
            dataCollectionTypeMapper.updateById(dataCollection);
        }else{
            dataCollectionTypeMapper.insert(dataCollection);
        }
    }


    @Transactional
    public void delete(List<String> ids) throws CommonException {
        for (String id : ids) {
            final Integer integer = dataCollectionMapper.selectCount(new QueryWrapper<DataCollection>().lambda().eq(DataCollection::getDataCollectionTypeId, id));
            if (!integer.equals(0)) {
                throw new CommonException("数据已使用，不能执行删除操作", CommonExceptionDefinition.BASIC_EXCEPTION);
            }
            dataCollectionTypeMapper.deleteById(id);
        }
    }


    public DataCollectionType findById(String id) {
        return dataCollectionTypeMapper.selectById(id);
    }

    public Object queryForLov(Map<String, Object> params) {
        Map<String,Object> map = (Map<String,Object>)params.get("page");
        params.remove("page");
        DataCollectionType dataCollectionType = JSONObject.parseObject(JSONObject.toJSONString(params), DataCollectionType.class);
        return findList(dataCollectionType, Integer.valueOf(map.get("current").toString()), Integer.valueOf(map.get("size").toString()));
    }
}

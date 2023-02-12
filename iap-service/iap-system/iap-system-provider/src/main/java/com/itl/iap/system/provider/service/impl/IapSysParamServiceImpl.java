package com.itl.iap.system.provider.service.impl;


import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.mes.api.entity.DataCollection;
import com.itl.iap.system.api.dto.IapSysParamDto;
import com.itl.iap.system.api.entity.IapSysParam;
import com.itl.iap.system.provider.mapper.IapSysParamMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class IapSysParamServiceImpl {

    @Autowired
    private IapSysParamMapper iapSysParamMapper;

    public Object findList(String type, String name, Integer pageNum, Integer pageSize) {

        Page page = new Page(pageNum,pageSize);
        Map<String,Object> params = new HashMap<>();
        params.put("type",type);
        params.put("name",name);
        params.put("siteId", UserUtils.getSite());
        return iapSysParamMapper.findList(page,params);
    }

    public Object findListByState(String type, String name, Integer pageNum, Integer pageSize) {

        Page page = new Page(pageNum,pageSize);
        Map<String,Object> params = new HashMap<>();
        params.put("type",type);
        params.put("name",name);
        params.put("siteId", UserUtils.getSite());
        return iapSysParamMapper.findListByState(page,params);
    }


    public void save(IapSysParam iapSysParam) {
        iapSysParam.setSiteId(UserUtils.getSite());
        if(iapSysParam.getId() != null){
            iapSysParamMapper.updateById(iapSysParam);
        }else{
            iapSysParamMapper.insert(iapSysParam);
        }
    }


    public IapSysParamDto findById(String id) {
        return iapSysParamMapper.getById(id);
    }

    @Transactional
    public void delete(List<String> ids) {
        ids.forEach(id ->{
            iapSysParamMapper.deleteById(id);
        });
    }


    public Object queryForLov(Map<String, Object> params) {
        Map<String,Object> map = (Map<String,Object>)params.get("page");
        params.remove("page");
        DataCollection dataCollection = JSONObject.parseObject(JSONObject.toJSONString(params), DataCollection.class);
        return findList(dataCollection.getDataCollectionTypeId(),dataCollection.getName(), Integer.valueOf(map.get("current").toString()), Integer.valueOf(map.get("size").toString()));
    }

    public List<IapSysParam> findByIds(List<String> iapSysParamIds) {
        if(CollUtil.isEmpty(iapSysParamIds)) {
            return Collections.emptyList();
        }
        return iapSysParamMapper.selectBatchIds(iapSysParamIds);

    }
}

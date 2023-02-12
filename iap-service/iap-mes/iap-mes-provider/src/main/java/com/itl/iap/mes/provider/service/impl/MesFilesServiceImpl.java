package com.itl.iap.mes.provider.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.mes.api.entity.MesFiles;
import com.itl.iap.mes.provider.mapper.MesFilesMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yx
 * @date 2021/8/19
 * @since 1.8
 */
@Service
public class MesFilesServiceImpl extends ServiceImpl<MesFilesMapper, MesFiles> {


    /**
     * 根据关联id查询文件信息
     * @param groupId 关联id
     * @return mesFiles
     * */
    public List<MesFiles> listByGroupId(String groupId) {
        return lambdaQuery().eq(MesFiles::getGroupId, groupId).list();
    }

    /**
     * 根据关联id删除
     * @param groupId 关联id
     * @return 是否成功
     * */
    public Boolean removeByGroupId(String groupId) {
        List<MesFiles> mesFiles = listByGroupId(groupId);
        if(CollUtil.isEmpty(mesFiles)) {
            return true;
        }
        return removeByIds(mesFiles.stream().map(MesFiles::getId).collect(Collectors.toSet()));
    }

    /**
     * 根据关联id列表查询图集合
     * @param groupIds 关联id列表
     * @return mesFiles
     * */
    public List<MesFiles> listByGroupIds(List<String> groupIds) {
        return lambdaQuery().in(MesFiles::getGroupId, groupIds).list();
    }

}

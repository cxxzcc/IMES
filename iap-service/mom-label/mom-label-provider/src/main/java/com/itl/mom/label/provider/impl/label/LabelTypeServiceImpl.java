package com.itl.mom.label.provider.impl.label;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mom.label.api.dto.label.LabelTypeQueryDTO;
import com.itl.mom.label.api.entity.label.LabelEntity;
import com.itl.mom.label.api.entity.label.LabelTypeEntity;
import com.itl.mom.label.provider.common.CommonCode;
import com.itl.mom.label.provider.exception.CustomException;
import com.itl.mom.label.provider.mapper.label.LabelMapper;
import com.itl.mom.label.provider.mapper.label.LabelTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author liuchenghao
 * @date 2020/11/3 17:59
 */
@Service
public class LabelTypeServiceImpl {


    @Autowired
    private LabelTypeMapper labelTypeMapper;
    @Autowired
    private LabelMapper labelMapper;


    public IPage<LabelTypeEntity> findList(LabelTypeQueryDTO labelTypeQueryDTO) {

        if (ObjectUtil.isEmpty(labelTypeQueryDTO.getPage())) {
            labelTypeQueryDTO.setPage(new Page(0, 10));
        }

        QueryWrapper queryWrapper = new QueryWrapper<LabelTypeEntity>();
        queryWrapper.eq("site", UserUtils.getSite());

        if (StrUtil.isNotEmpty(labelTypeQueryDTO.getLabelType())) {
            queryWrapper.like("label_type", labelTypeQueryDTO.getLabelType());
        }
        labelTypeQueryDTO.getPage().setDesc("CREATION_DATE");
        return labelTypeMapper.selectPage(labelTypeQueryDTO.getPage(), queryWrapper);
    }

    public IPage<LabelTypeEntity> findListByState(LabelTypeQueryDTO labelTypeQueryDTO) {

        if (ObjectUtil.isEmpty(labelTypeQueryDTO.getPage())) {
            labelTypeQueryDTO.setPage(new Page(0, 10));
        }

        QueryWrapper queryWrapper = new QueryWrapper<LabelTypeEntity>();
        queryWrapper.eq("site", UserUtils.getSite());

        if (StrUtil.isNotEmpty(labelTypeQueryDTO.getLabelType())) {
            queryWrapper.like("label_type", labelTypeQueryDTO.getLabelType());
        }
        queryWrapper.eq("STATE", 1);
        labelTypeQueryDTO.getPage().setDesc("CREATION_DATE");
        return labelTypeMapper.selectPage(labelTypeQueryDTO.getPage(), queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(LabelTypeEntity labelTypeEntity) throws CommonException {
        LambdaQueryWrapper<LabelTypeEntity> query = new QueryWrapper<LabelTypeEntity>().lambda().eq(LabelTypeEntity::getLabelType, labelTypeEntity.getLabelType()).eq(LabelTypeEntity::getSite, UserUtils.getSite());
        if (ObjectUtil.isNotEmpty(labelTypeEntity.getId())) {
            query.ne(LabelTypeEntity::getId, labelTypeEntity.getId());
        }
        List<LabelTypeEntity> list = labelTypeMapper.selectList(query);
        if (list != null && list.size() > 0) {
            throw new CustomException(CommonCode.CODE_REPEAT);
        }

        labelTypeEntity.setSite(UserUtils.getSite());
        if (ObjectUtil.isNotEmpty(labelTypeEntity.getId())) {
            labelTypeEntity.setLastUpdateDate(new Date());
            labelTypeMapper.updateById(labelTypeEntity);
        } else {
            labelTypeEntity.setCreationDate(new Date());
            labelTypeMapper.insert(labelTypeEntity);
        }
    }


    @Transactional
    public ResponseData delete(List<String> ids) {
        List<LabelTypeEntity> labelTypeEntities = labelTypeMapper.selectBatchIds(ids);
        List<String> typeList = labelTypeEntities.stream().map(LabelTypeEntity::getLabelType).collect(Collectors.toList());
        //被引用无法删除
        QueryWrapper<LabelEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(LabelEntity::getLabelTypeId, typeList);
        List<LabelEntity> labelEntities = labelMapper.selectList(queryWrapper);
        Map<String, List<LabelEntity>> labelMaps = labelEntities.stream().collect(Collectors.groupingBy(LabelEntity::getLabelTypeId));
        List<String> noDelIds = new ArrayList<>();
        for (LabelTypeEntity labelTypeEntity : labelTypeEntities) {
            String labelType = labelTypeEntity.getLabelType();
            String id = labelTypeEntity.getId();
            if (labelMaps.containsKey(labelType)) {
                noDelIds.add(labelType);
                continue;
            }
            labelTypeMapper.deleteById(id);
        }
        if (CollectionUtil.isNotEmpty(noDelIds)) {
            String collect = labelTypeEntities.stream().filter(v -> noDelIds.contains(v.getLabelType())).map(LabelTypeEntity::getLabelType).collect(Collectors.joining(","));
            return ResponseData.error("类型（" + collect + "）已被引用，无法删除");
        }
        return ResponseData.success();
    }


    public LabelTypeEntity findById(String id) {

        return labelTypeMapper.selectById(id);
    }


    public List<LabelTypeEntity> queryToSelect() {
        return labelTypeMapper.selectList(
                new QueryWrapper<LabelTypeEntity>().lambda().eq(LabelTypeEntity::getState, 1));
    }

    public List<LabelTypeEntity> getByIdList(List<String> idList) {
        return labelTypeMapper.selectBatchIds(idList);
    }
}

package com.itl.mes.me.provider.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.me.api.entity.Action;
import com.itl.mes.me.api.entity.Operation;
import com.itl.mes.me.api.service.ActionService;
import com.itl.mes.me.provider.mapper.ActionMapper;
import com.itl.mes.me.provider.mapper.InstructorItemMapper;
import com.itl.mom.label.api.entity.label.LabelTypeEntity;
import com.itl.mom.label.client.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 过站动作
 *
 * @author cch
 * @date 2021-05-31
 */
@Service
public class ActionServiceImpl extends ServiceImpl<ActionMapper, Action> implements ActionService {
    @Autowired
    private InstructorItemMapper instructorItemMapper;
    @Autowired
    private ActionMapper actionMapper;
    @Autowired
    private LabelService labelService;

    @Override
    public List<HashMap<String, Object>> getDetails(String id) {
        List<HashMap<String, Object>> hashMaps = actionMapper.find(id);
        return hashMaps;
    }

    @Override
    public List<HashMap<String, Object>> getDetailsByOperationID(String id) {
        List<HashMap<String, Object>> hashMaps = actionMapper.infoByOperationID(id);
        List<String> labelList = hashMaps.stream().map(v -> ((String) v.get("scanSn")).split(","))
                .flatMap(Arrays::stream).distinct()
                .collect(Collectors.toList());
        ResponseData<List<LabelTypeEntity>> labelTypeByIdList = labelService.getLabelTypeByIdList(labelList);
        if (labelTypeByIdList != null && labelTypeByIdList.getData() != null) {
            List<LabelTypeEntity> data = labelTypeByIdList.getData();
            Map<String, LabelTypeEntity> collect = data.stream().collect(Collectors.toMap(LabelTypeEntity::getId, Function.identity()));
            for (HashMap<String, Object> record : hashMaps) {
                String scanSn = (String) record.get("scanSn");
                if (StrUtil.isNotBlank(scanSn)) {
                    String[] split = scanSn.split(",");
                    String scanSnName = "";
                    List<LabelTypeEntity> scanSnJsonList = new ArrayList<>();
                    for (int i = 0; i < split.length; i++) {
                        String s = split[i];
                        if (collect.containsKey(s)) {
                            LabelTypeEntity labelTypeEntity = collect.get(s);
                            scanSnJsonList.add(labelTypeEntity);
                            scanSnName += labelTypeEntity.getLabelType();
                        }
                        if (i < split.length - 1) {
                            scanSnName += ",";
                        }
                    }
                    record.put("scanSnName", scanSnName);
                    record.put("scanSnJson", JSONUtil.toJsonStr(scanSnJsonList));
                }
            }
        }
        return hashMaps;
    }
}

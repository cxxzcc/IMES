package com.itl.mes.me.provider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.mes.me.api.entity.LabelPrintLog;
import com.itl.mes.me.api.service.LabelPrintLogService;
import com.itl.mes.me.api.vo.LabelPrintLogVo;
import com.itl.mes.me.provider.mapper.LabelPrintLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @auth liuchenghao
 * @date 2021/4/6
 */
@Service
public class LabelPrintLogServiceImpl extends ServiceImpl<LabelPrintLogMapper, LabelPrintLog> implements LabelPrintLogService {


    @Autowired
    LabelPrintLogMapper labelPrintLogMapper;


    @Override
    public List<LabelPrintLogVo> findList(String labelPrintBo) {

        return labelPrintLogMapper.findList(labelPrintBo);
    }

    @Override
    public List<LabelPrintLogVo> findDetailList(String labelPrintDetailBo) {

        labelPrintDetailBo = labelPrintDetailBo.replace("=","");
        return labelPrintLogMapper.findDetailList(labelPrintDetailBo);
    }

}

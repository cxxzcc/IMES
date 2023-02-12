package com.itl.mom.label.provider.impl.label;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.mom.label.api.entity.label.LabelPrintLog;
import com.itl.mom.label.api.enums.LabelPrintLogStateEnum;
import com.itl.mom.label.api.service.label.LabelPrintLogService;
import com.itl.mom.label.api.vo.LabelPrintLogVo;
import com.itl.mom.label.provider.mapper.label.LabelPrintLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

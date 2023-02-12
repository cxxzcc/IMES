package com.itl.mes.me.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.me.api.entity.LabelPrintLog;
import com.itl.mes.me.api.vo.LabelPrintLogVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @auth liuchenghao
 * @date 2021/4/6
 */
@Repository
public interface LabelPrintLogMapper extends BaseMapper<LabelPrintLog> {


    List<LabelPrintLogVo> findList(String labelPrintBo);


    List<LabelPrintLogVo> findDetailList(String labelPrintDetailBo);

}

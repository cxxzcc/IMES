package com.itl.mom.label.api.service.label;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mom.label.api.entity.label.LabelPrintLog;
import com.itl.mom.label.api.vo.LabelPrintLogVo;

import java.util.List;

/**
 * @auth liuchenghao
 * @date 2021/4/6
 */
public interface LabelPrintLogService extends IService<LabelPrintLog> {


    /**
     * 查询标签打印的日志信息
     * @param labelPrintBo
     * @return
     */
    List<LabelPrintLogVo> findList(String labelPrintBo);


    /**
     * 查询标签详细打印的日志信息
     * @param labelPrintDetailBo
     * @return
     */
    List<LabelPrintLogVo> findDetailList(String labelPrintDetailBo);

}

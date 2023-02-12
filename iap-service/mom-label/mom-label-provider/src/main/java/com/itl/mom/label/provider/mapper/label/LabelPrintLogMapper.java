package com.itl.mom.label.provider.mapper.label;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mom.label.api.entity.label.LabelPrintLog;
import com.itl.mom.label.api.vo.LabelPrintLogVo;
import org.apache.ibatis.annotations.Param;


import java.util.List;
import java.util.Map;

/**
 * @auth liuchenghao
 * @date 2021/4/6
 */
public interface LabelPrintLogMapper extends BaseMapper<LabelPrintLog> {


    List<LabelPrintLogVo> findList(String labelPrintBo);


    List<LabelPrintLogVo> findDetailList(String labelPrintDetailBo);

    /**
     * 工位打印记录
     * @param page 分页参数
     * @param param 查询参数
     * @return 工位打印记录列表
     * */
    List<LabelPrintLogVo> getPrintLogByStation(@Param("page") Page<LabelPrintLogVo> page, @Param("param") Map<String, Object> param);

}

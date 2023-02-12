package com.itl.mom.label.provider.mapper.label;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mom.label.api.entity.label.LabelEntity;
import com.itl.mom.label.api.vo.LabelTransVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author liuchenghao
 * @date 2020/11/3 17:39
 */
public interface LabelMapper extends BaseMapper<LabelEntity> {


    /**
     * 条码转移-条码列表查询
     * @param queryPage 分页参数
     * @param params 查询参数
     * @return 条码列表
     * */
    List<LabelTransVo> labelTransList(Page<LabelTransVo> queryPage, @Param("params") Map<String, Object> params);

    /**
     * 拆单：条码转移-条码列表查询
     * @param params 查询参数
     * @return 条码列表
     * */
    List<LabelTransVo> labelTransListAsOrder(@Param("params") Map<String, Object> params);

    /**
     * 条码转移-条码列表查询
     * @param params 查询参数
     * @return 条码列表
     * */
    List<LabelTransVo> labelTransList(@Param("params") Map<String, Object> params);
}

package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.core.api.entity.ProductionOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 生产订单mapper
 * @author dengou
 * @date 2021/10/11
 */
@Mapper
public interface ProductionOrderMapper extends BaseMapper<ProductionOrder> {

    /**
     * 查询分页列表
     * @param params 查询参数
     * @param page 分页参数
     * */
    List<ProductionOrder> getPage(Page<ProductionOrder> page, @Param("params") Map<String, Object> params);
}

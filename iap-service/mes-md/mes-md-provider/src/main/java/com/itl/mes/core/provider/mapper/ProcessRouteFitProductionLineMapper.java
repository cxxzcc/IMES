package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.core.api.entity.ProcessRouteFitProductionLine;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * <p>
 * 产线工艺路线设置表 Mapper 接口
 * </p>
 *
 * @author lK
 * @since 2021-10-15
 */
@Repository
public interface ProcessRouteFitProductionLineMapper extends BaseMapper<ProcessRouteFitProductionLine> {

    IPage<Object> getProductLineRoute(Page<Object> page, @Param("map") Map<String, Object> map);

}

package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.core.api.entity.ProcessRouteFitProduct;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * <p>
 * 产品工艺路线设置表-中间表 Mapper 接口
 * </p>
 *
 * @author lK
 * @since 2021-10-15
 */
@Repository
public interface ProcessRouteFitProductMapper extends BaseMapper<ProcessRouteFitProduct> {

    IPage<Object> getProductRoute(Page<Object> page, @Param("map") Map<String, Object> map);

}

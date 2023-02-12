package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.mes.core.api.entity.Packing;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 包装定义表 Mapper 接口
 * </p>
 *
 * @author space
 * @since 2019-07-12
 */
@Repository
public interface PackingMapper extends BaseMapper<Packing> {

    /**
     * 获取标签打印时包装的lov
     * @return
     */
    List<Map<String, Object>> findLabelPrintPackingList(IPage<Map<String, Object>> page, @Param("params") Map<String, Object> params);
}
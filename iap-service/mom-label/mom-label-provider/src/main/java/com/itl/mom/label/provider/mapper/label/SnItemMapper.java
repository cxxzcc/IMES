package com.itl.mom.label.provider.mapper.label;

import com.itl.iap.common.base.mapper.MesBaseMapper;
import com.itl.mom.label.api.entity.label.SnItem;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface SnItemMapper extends MesBaseMapper<SnItem> {


    int updateSysl(@Param("id")String id, @Param("sysl")BigDecimal sysl);

    int updateSyslByBoList(@Param("list")List<String> boList);
}

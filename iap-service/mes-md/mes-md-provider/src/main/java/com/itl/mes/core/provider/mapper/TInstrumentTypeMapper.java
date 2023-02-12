package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.core.api.entity.TInstrumentType;
import org.apache.ibatis.annotations.Param;

public interface TInstrumentTypeMapper extends BaseMapper<TInstrumentType> {

    void updateStandard(@Param("instrumentTypeId") String instrumentTypeId);
}

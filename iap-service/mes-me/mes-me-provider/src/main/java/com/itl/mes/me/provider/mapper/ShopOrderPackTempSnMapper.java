package com.itl.mes.me.provider.mapper;

import com.itl.mes.me.api.entity.ShopOrderPackSnTemp;
import com.itl.iap.common.base.mapper.MesBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

@Mapper
public interface ShopOrderPackTempSnMapper extends MesBaseMapper<ShopOrderPackSnTemp> {

    Integer genSnObj(@Param("stationBo") String stationBo, @Param("shopOrderBo") String shopOrderBo, @Param("sn") String sn, @Param("now") Date date);
}

package com.itl.mes.me.provider.mapper;

import com.itl.mes.me.api.entity.ShopOrderPackTemp;
import com.itl.mes.me.api.vo.ShopOrderPackTempVO;
import com.itl.iap.common.base.mapper.MesBaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShopOrderPackTempMapper extends MesBaseMapper<ShopOrderPackTemp> {

    List<ShopOrderPackTempVO> getPackTempSn(String bo);
}

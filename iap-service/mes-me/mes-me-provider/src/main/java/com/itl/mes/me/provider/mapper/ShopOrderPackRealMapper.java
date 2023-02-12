package com.itl.mes.me.provider.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.itl.mes.me.api.entity.ShopOrderPackReal;
import com.itl.mes.me.api.vo.ShopOrderPackRealVO;
import com.itl.iap.common.base.mapper.MesBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShopOrderPackRealMapper extends MesBaseMapper<ShopOrderPackReal> {

    List<ShopOrderPackRealVO> getPackReal(@Param(Constants.WRAPPER) Wrapper wrapper);
}

package com.itl.iap.system.provider.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.system.api.entity.IapSysParamType;
import org.apache.ibatis.annotations.Param;

/**
 * @since JDK 1.8
 */
public interface IapSysParamTypeMapper extends BaseMapper<IapSysParamType> {

    IPage<IapSysParamType> findList(Page page, @Param("iapSysParamType") IapSysParamType iapSysParamType);

    IPage<IapSysParamType> findListByState(Page page, @Param("iapSysParamType") IapSysParamType iapSysParamType);
}

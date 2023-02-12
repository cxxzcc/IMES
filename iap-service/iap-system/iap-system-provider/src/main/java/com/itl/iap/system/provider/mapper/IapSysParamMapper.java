package com.itl.iap.system.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.system.api.dto.IapSysParamDto;
import com.itl.iap.system.api.entity.IapSysParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

;

/**
 * @since JDK 1.8
 */
@Mapper
public interface IapSysParamMapper extends BaseMapper<IapSysParam> {

    IPage<IapSysParamDto> findList(Page page, @Param("params") Map<String,Object> params);

    IPage<IapSysParamDto> findListByState(Page page, @Param("params") Map<String,Object> params);

    IapSysParamDto getById(String id);
}

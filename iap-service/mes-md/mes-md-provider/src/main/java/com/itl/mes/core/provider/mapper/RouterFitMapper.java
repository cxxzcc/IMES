package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.core.api.dto.RouterFitDto;
import com.itl.mes.core.api.entity.RouterFit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 *
 * @author xtz
 * @date 2021-05-25
 */
@Mapper
public interface RouterFitMapper extends BaseMapper<RouterFit> {

    IPage<RouterFitDto> queryByItem(Page page, @Param("routerFitDto") RouterFitDto routerFitDto);

    IPage<RouterFitDto> queryByItemGroup(Page page, @Param("routerFitDto") RouterFitDto routerFitDto);

    IPage<RouterFitDto> queryByProductLine(Page page, @Param("routerFitDto") RouterFitDto routerFitDto);

    Map<Object,String> getItemGroup(@Param("itemBo") String itemBo);
}

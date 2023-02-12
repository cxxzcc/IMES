package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.core.api.dto.ConfigItemDto;
import com.itl.mes.core.api.entity.ConfigItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 配置项 Mapper 接口
 * </p>
 *
 * @author chenjx1
 * @since 2021-11-9
 */

public interface ConfigItemMapper extends BaseMapper<ConfigItem> {

    List<ConfigItemDto> queryByItem(Page page, @Param("dto") ConfigItemDto configItemDto);

    List<ConfigItemDto> queryByItemCode(ConfigItemDto configItemDto);


}

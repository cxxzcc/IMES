package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.dto.ConfigItemDto;
import com.itl.mes.core.api.entity.ConfigItem;
import com.itl.mes.core.api.vo.ConfigItemVo;

import java.util.List;

/**
 * <p>
 * 配置项表 服务类
 * </p>
 *
 * @author chenjx1
 * @since 2021-11-9
 */
public interface ConfigItemService extends IService<ConfigItem> {

    IPage<ConfigItemDto> selectPageList(ConfigItemDto configItemDto);

    List<ConfigItem> selectList(ConfigItem configItem);

    void save(List<ConfigItemVo> configItemVoList)throws CommonException;

    ConfigItem selectById(String bo)throws CommonException;

    int delete(String bo)throws CommonException;


}

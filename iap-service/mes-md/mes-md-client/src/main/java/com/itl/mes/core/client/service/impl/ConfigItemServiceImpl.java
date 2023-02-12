package com.itl.mes.core.client.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.ConfigItemDto;
import com.itl.mes.core.api.entity.ConfigItem;
import com.itl.mes.core.api.vo.ConfigItemVo;
import com.itl.mes.core.client.service.ConfigItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author chenjx1
 * @date 2021/11/10
 * @since JDK1.8
 */
@Slf4j
@Component
public class ConfigItemServiceImpl implements ConfigItemService {


    @Override
    public ResponseData save(ConfigItemVo configItemVo) throws CommonException {
        return null;
    }

    @Override
    public ResponseData<IPage<ConfigItemDto>> queryPage(ConfigItemDto configItemDto) throws CommonException {
        return null;
    }

    @Override
    public ResponseData<List<ConfigItem>> query(ConfigItem configItem) throws CommonException {
        return null;
    }

}

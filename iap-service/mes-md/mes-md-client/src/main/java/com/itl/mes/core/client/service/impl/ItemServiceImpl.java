package com.itl.mes.core.client.service.impl;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.ItemQueryDto;
import com.itl.mes.core.api.dto.MboMitemDTO;
import com.itl.mes.core.api.entity.Item;
import com.itl.mes.core.api.vo.ItemAndCustomDataValVo;
import com.itl.mes.core.api.vo.ItemFullVo;
import com.itl.mes.core.client.service.ItemFeignService;
import com.itl.mes.core.client.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author chenjx1
 * @date 2021/10/20
 * @since JDK1.8
 */
@Slf4j
@Component
public class ItemServiceImpl implements ItemService {

    @Override
    public ResponseData<ItemFullVo> getItemByItemAndVersion(String item, String version) {
        return null;
    }
}

package com.itl.mes.core.client.service.impl;

import com.itl.mes.core.api.dto.ItemQueryDto;
import com.itl.mes.core.api.dto.MboMitemDTO;
import com.itl.mes.core.api.entity.Item;
import com.itl.mes.core.api.vo.ItemAndCustomDataValVo;
import com.itl.mes.core.client.service.ItemFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 崔翀赫
 * @date 2021/3/26$
 * @since JDK1.8
 */
@Slf4j
@Component
public class ItemFeignServiceImpl implements ItemFeignService {
    @Override
    public List<Item> getAll() {
        log.error("sorry ItemFeignServiceImpl getAll feign fallback " );
        return null;
    }

    @Override
    public Map<String,String> getByIds(Set<String> bos) {
        log.error("sorry ItemFeignServiceImpl getByIds feign fallback ");
        return null;
    }

    @Override
    public List<MboMitemDTO> getLov(MboMitemDTO mboMitemDTO) {
        log.error("sorry ItemFeignServiceImpl getLov feign fallback ");
        return null;
    }

    @Override
    public Map<String, String> getItemUnit(Set<String> bos) {
        log.error("sorry ItemFeignServiceImpl Method: getItemUnit() feign fallback — "+bos);
        return null;
    }

    /**
     * 根据BO集合查询Item 返回list集合
     *
     * @param bos
     */
    @Override
    public List<Item> getItemList(Set<String> bos) {
        log.error("sorry ItemFeignServiceImpl getItemList feign fallback bos：{}",bos);
        return null;
    }

    @Override
    public List<Item> getItemByTerm(ItemQueryDto itemQueryDto) {
        log.error("sorry ItemFeignServiceImpl getItemByTerm feign fallback ");
        return null;
    }

    @Override
    public List<ItemAndCustomDataValVo> getItemAndCustomDataVal(Set<String> bos) {
        log.error("sorry ItemFeignServiceImpl getItemAndCustomDataVal feign fallback ");
        return null;
    }
}

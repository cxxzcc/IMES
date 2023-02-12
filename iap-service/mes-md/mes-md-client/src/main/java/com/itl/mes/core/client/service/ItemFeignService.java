package com.itl.mes.core.client.service;

import com.itl.mes.core.api.dto.ItemQueryDto;
import com.itl.mes.core.api.dto.MboMitemDTO;
import com.itl.mes.core.api.entity.Item;
import com.itl.mes.core.api.vo.ItemAndCustomDataValVo;
import com.itl.mes.core.client.config.FallBackConfig;
import com.itl.mes.core.client.service.impl.ItemFeignServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 崔翀赫
 * @date 2021/3/26$
 * @since JDK1.8
 */
@FeignClient(value = "mes-md-provider", contextId = "item",
        fallback = ItemFeignServiceImpl.class,
        configuration = FallBackConfig.class)
public interface ItemFeignService {
    /**
     * 查询全部物料
     */
    @GetMapping("/items/getAll")
    List<Item> getAll();

    /**
     * 根据BO集合查询Item
     */
    @PostMapping("/items/getByIds")
    Map<String,String> getByIds(@RequestBody Set<String> bos);

    /**
     * 根据BO集合查询Item 返回list集合
     */
    @PostMapping("/items/getItemList")
    List<Item>  getItemList(@RequestBody Set<String> bos);



    @PostMapping("/commonBrowsers/queryAllItem")
    List<MboMitemDTO> getLov(@RequestBody MboMitemDTO mboMitemDTO);

    /**
     * liKun
     * 根据itemBO获取ITEM_UNIT(单位BO)
     */
    @PostMapping("/items/getItemUnit")
    Map<String, String> getItemUnit(@RequestBody Set<String> bos);



    @PostMapping("/items/getItemByTerm")
    List<Item> getItemByTerm(@RequestBody ItemQueryDto itemQueryDto);

    @PostMapping("/items/getItemAndCustomDataVal")
    List<ItemAndCustomDataValVo> getItemAndCustomDataVal(@RequestBody Set<String> bos);
}

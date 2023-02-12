package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.core.api.dto.ShopOrderBomComponnetQueryDto;
import com.itl.mes.core.api.entity.ShopOrderBomComponnet;
import com.itl.mes.core.api.vo.ShopOrderBomComponnetVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @auth liuchenghao
 * @date 2021/5/27
 */
@Repository
public interface ShopOrderBomComponnetMapper extends BaseMapper<ShopOrderBomComponnet> {

    /**
     * 根据bomBo查询BOM组件，用于新建工单未保存时页面显示
     * @param shopOrderBomComponnetQueryDto
     * @return
     */
    IPage<ShopOrderBomComponnetVo>  findListByBomBo(Page page, @Param("shopOrderBomComponnetQueryDto") ShopOrderBomComponnetQueryDto shopOrderBomComponnetQueryDto);

    /**
     * 根据工单BO查询工单BOM的组件，用于保存数据后回显数据
     * @param shopOrderBomComponnetQueryDto
     * @return
     */
    IPage<ShopOrderBomComponnetVo> findListByShopOrderBo(Page page,@Param("shopOrderBomComponnetQueryDto") ShopOrderBomComponnetQueryDto shopOrderBomComponnetQueryDto);

    /**
     * 根据工单Bo查询工单工序Bom
     * @param shopOrderBo
     * @param type
     * @return
     */
    List<ShopOrderBomComponnetVo> queryBomByShopOrderBo(@Param("shopOrderBo") String shopOrderBo, @Param("type") String type);
}

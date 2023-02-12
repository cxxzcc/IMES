package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.core.api.dto.MboMitemDTO;
import com.itl.mes.core.api.entity.ShopOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工单表 Mapper 接口
 * </p>
 *
 * @author space
 * @since 2019-06-17
 */
@Repository
public interface ShopOrderMapper extends BaseMapper<ShopOrder> {

    Integer updateShopOrder(@Param("shopOrder") ShopOrder shopOrder, @Param("changeDate") Date changeDate);

    Integer updateLimitQtyShopOrderReleaseQtyByBO( @Param( "bo" ) String bo,@Param( "qty" )BigDecimal qty,@Param( "overfullQty" )BigDecimal overfullQty );

    Integer updateOverfullQtyShopOrderReleaseQtyByBO( @Param( "bo" ) String bo,@Param( "qty" )BigDecimal qty );

    Integer updateShopOrderReleaseQtyByBO( @Param( "bo" ) String bo,@Param( "qty" )BigDecimal qty );

    Integer updateShopOrderCompleteQtyByBO( @Param( "bo" ) String bo,@Param( "completeQty" )BigDecimal completeQty );

    Integer updateShopOrderScrapQtyByBO( @Param( "bo" ) String bo,@Param( "scrapTty" )BigDecimal scrapTty );

    Integer updateShopOrderLabelQtyByBO( @Param( "bo" ) String bo,@Param( "labelQty" ) BigDecimal labelQty );

    Integer updateShopOrderScheduleQtyByBO( @Param( "bo" ) String bo,@Param( "scheduleQty" ) BigDecimal ScheduleQty );

    Integer updateShopOrderScheduleQtyAndOrderQtyByBO( @Param( "bo" ) String bo,@Param( "scheduleQty" ) BigDecimal ScheduleQty, @Param( "orderQty" ) BigDecimal orderQty );

    IPage<ShopOrder> getList(Page page, @Param("params") Map<String,Object> params);

    List<String> getIdsByVals(Map<String, Object> result);

    List<Map<String, Object>> getBindingBySite(String site);

    void updateEmergenc(Map<String, Object> params);

    List<String> getScheduleShopOrder();

    List<MboMitemDTO> getBomComponents(@Param("itemBos") List<String> itemBos);

    void updateProductState(@Param( "bo" ) String shopOrderBo,@Param( "s" ) String s);

    Integer updateShopOrderCompleteQtyAndState(@Param("shopOrderBo") String shopOrderBo, @Param("completeQty") Integer completeQty, @Param("stateBo") String bo);
}

package com.itl.mom.label.provider.mapper.label;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mom.label.api.dto.label.LabelPrintQueryDto;
import com.itl.mom.label.api.entity.label.LabelPrint;
import com.itl.mom.label.api.vo.ItemLabelListVo;
import com.itl.mom.label.api.vo.LabelPrintVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标签打印范围 Mapper 接口
 * </p>
 *
 * @author zhongfei
 * @since 2021-01-20
 */
public interface LabelPrintMapper extends BaseMapper<LabelPrint> {

    IPage<LabelPrintVo> findList(@Param("page") Page page, @Param("labelPrintQueryDto") LabelPrintQueryDto labelPrintRangeQueryDto);

    String getCodeRuleBo(String code, String site);

    IPage<LabelPrintVo> findPackingLabelPrint(@Param("page") Page page, @Param("labelPrintQueryDto") LabelPrintQueryDto labelPrintRangeQueryDto);


    Map<String, Object> getItem(@Param("bo") String bo);

    Map<String, Object> getItemName(@Param("bo") String bo);

    String getSn(@Param("bo") String bo);

    IPage<ItemLabelListVo> getItemLabelPageList(@Param("page") Page page, @Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * 通过item查询对应的条码
     * @param itemCode
     * @return
     */
    List<String> queryCodeByItem(@Param("itemCodes") List<String> itemCode);

    String queryItemByCode(@Param("sn") String sn);

    /**
     * 根据sn查询工单bo
     * */
    String queryShopOrderBoBySn(String sn);
}

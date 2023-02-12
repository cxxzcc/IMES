package com.itl.mes.me.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.me.api.dto.LabelPrintQueryDto;
import com.itl.mes.me.api.entity.LabelPrint;
import com.itl.mes.me.api.vo.LabelPrintVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 标签打印范围 Mapper 接口
 * </p>
 *
 * @author zhongfei
 * @since 2021-01-20
 */
@Repository
public interface LabelPrintMapper extends BaseMapper<LabelPrint> {

    IPage<LabelPrintVo> findList(@Param("page") Page page, @Param("labelPrintQueryDto") LabelPrintQueryDto labelPrintRangeQueryDto);

    String getCodeRuleBo(String code, String site);


    IPage<LabelPrintVo> findPackingLabelPrint(@Param("page") Page page, @Param("labelPrintQueryDto") LabelPrintQueryDto labelPrintRangeQueryDto);
}

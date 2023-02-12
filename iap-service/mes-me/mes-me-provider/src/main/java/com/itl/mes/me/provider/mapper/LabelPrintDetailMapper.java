package com.itl.mes.me.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.me.api.dto.LabelPrintDetailQueryDto;
import com.itl.mes.me.api.entity.LabelPrintDetail;
import com.itl.mes.me.api.vo.LabelPrintDetailVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 标签打印范围明细 Mapper 接口
 * </p>
 *
 * @author zhongfei
 * @since 2021-01-20
 */
@Repository
public interface LabelPrintDetailMapper extends BaseMapper<LabelPrintDetail> {


    IPage<LabelPrintDetailVo> findList(@Param("page") Page page, @Param("labelPrintDetailQueryDto") LabelPrintDetailQueryDto labelPrintDetailQueryDto);


    IPage<LabelPrintDetailVo> findPackingLabelPrintDetail(@Param("page") Page page, @Param("labelPrintDetailQueryDto") LabelPrintDetailQueryDto labelPrintDetailQueryDto);
}

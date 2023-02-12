package com.itl.mom.label.provider.mapper.label;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mom.label.api.dto.label.SnQueryDto;
import com.itl.mom.label.api.entity.label.Sn;
import com.itl.mom.label.api.vo.LabelPrintLogVo;
import com.itl.mom.label.api.vo.MeProductStatusVo;
import com.itl.mom.label.api.vo.SnVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @auth liuchenghao
 * @date 2021/4/23
 */
public interface SnMapper extends BaseMapper<Sn> {

    IPage<SnVo> findList(@Param("page") Page page, @Param("snQueryDto") SnQueryDto snQueryDto);
    IPage<LabelPrintLogVo> findLog(@Param("page") Page page, @Param("snQueryDto") SnQueryDto snQueryDto);
    IPage<MeProductStatusVo> findProductStatus(@Param("page") Page page, @Param("snQueryDto") SnQueryDto snQueryDto);

    IPage<SnVo> findPackingLabelPrintDetail(@Param("page") Page page, @Param("snQueryDto") SnQueryDto snQueryDto);

    /**
     * 根据条码查询物料信息
     * @param sn
     * @return
     */
    Map<String, String> getItemInfoAndSnStateBySn(@Param("sn") String sn);
    String getById(@Param("id") String id);

    /**
     * 拆单使用：查询拆当前工单产品SN，未上线状态的条码BO
     * @param orderBo
     * @param onLine // 0:未上线，1:已上线
     * @return
     */
    List<String> queryOrderBoList(String orderBo, int onLine);

}

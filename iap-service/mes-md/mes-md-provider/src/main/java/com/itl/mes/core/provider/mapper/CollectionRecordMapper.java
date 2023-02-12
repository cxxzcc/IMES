package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.core.api.dto.ProductCheckoutDTO;
import com.itl.mes.core.api.entity.CollectionRecord;
import com.itl.mes.core.api.vo.ProductCheckoutDetailVO;
import com.itl.mes.core.api.vo.ProductCheckoutVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author houfan
 * @since 2021-11-02
 */
public interface CollectionRecordMapper extends BaseMapper<CollectionRecord> {

    List<ProductCheckoutVO> getProductCheckoutList(@Param(Constants.WRAPPER) Wrapper wrapper);


    /**
     * 分页查询
     *
     * @param queryPage 分页参数
     * @param params    查询参数
     * @return 查询结果列表
     */
    List<CollectionRecord> getPage(Page<CollectionRecord> queryPage, @Param("params") Map<String, Object> params);

    /**
     * 通过sn跟工厂修改
     *
     * @param site site
     * @param sn   sn
     */
    void updateBySnAndSite(@Param("site") String site, @Param("sn") String sn);

    List<ProductCheckoutDetailVO> getProductCheckoutDetailList(@Param(Constants.WRAPPER) Wrapper wrapper);
}

package com.itl.iap.mes.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.mes.api.dto.sparepart.SparePartDTO;
import com.itl.iap.mes.api.entity.sparepart.SparePart;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author dengou
 * @date 2021/9/17
 */
public interface SparePartMapper extends BaseMapper<SparePart> {

    /**
     * 分页查询
     * @param params 分页参数，查询参数
     * @return 分页结果集
     * */
    List<SparePartDTO> getPage(Page<SparePartDTO> queryPage, @Param("params") Map<String, Object> params);
    /**
     * 分页查询库存列表
     * @param params 分页参数，查询参数
     * @return 分页结果集
     * */
    List<SparePartDTO> getPageByInventory(Page<SparePartDTO> queryPage, @Param("params") Map<String, Object> params);
}

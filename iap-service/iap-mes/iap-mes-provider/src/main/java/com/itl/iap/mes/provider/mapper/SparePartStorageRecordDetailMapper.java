package com.itl.iap.mes.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.mes.api.entity.sparepart.SparePartStorageRecordDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author dengou
 * @date 2021/9/17
 */
public interface SparePartStorageRecordDetailMapper extends BaseMapper<SparePartStorageRecordDetail> {
    /**
     * 出入库备件列表
     * @param params record= 出入库记录id
     * */
    List<SparePartStorageRecordDetail> listByRecordId(Page page, @Param("params") Map<String, Object> params);
}

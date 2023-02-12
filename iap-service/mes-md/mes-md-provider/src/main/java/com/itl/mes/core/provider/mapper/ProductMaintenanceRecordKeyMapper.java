package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.core.api.entity.ProductMaintenanceMethodRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 维修记录-维修措施关联信息
 * @author dengou
 * @date 2021/11/11
 */
@Mapper
public interface ProductMaintenanceRecordKeyMapper extends BaseMapper<ProductMaintenanceMethodRecord> {
}

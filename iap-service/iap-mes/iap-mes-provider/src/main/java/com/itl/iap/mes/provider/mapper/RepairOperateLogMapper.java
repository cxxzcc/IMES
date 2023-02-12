package com.itl.iap.mes.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.iap.mes.api.entity.RepairOperateLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 工单操作记录mapper
 * @author dengou
 * @date 2021/10/25
 */
@Mapper
public interface RepairOperateLogMapper extends BaseMapper<RepairOperateLog> {
}

package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.core.api.dto.TemporaryDataRetryLogDTO;
import com.itl.mes.core.api.entity.TemporaryDataRetryLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  过站暂存数据重传记录 Mapper 接口
 * </p>
 *
 * @author dengou
 * @since 2021-12-08
 */
public interface TemporaryDataRetryLogMapper extends BaseMapper<TemporaryDataRetryLog> {

    /**
     * 分页查询列表
     * @param params 查询参数
     * @param page 分页参数
     * @return 结果集
     * */
    List<TemporaryDataRetryLogDTO> getPage(Page<TemporaryDataRetryLogDTO> page, @Param("params") Map<String, Object> params);
}

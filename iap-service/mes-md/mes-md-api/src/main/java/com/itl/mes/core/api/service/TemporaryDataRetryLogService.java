package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.core.api.dto.TemporaryDataRetryLogDTO;
import com.itl.mes.core.api.entity.TemporaryDataRetryLog;

import java.util.Map;

/**
 * <p>
 *  过站暂存数据重传记录服务类
 * </p>
 *
 * @author dengou
 * @since 2021-12-08
 */
public interface TemporaryDataRetryLogService extends IService<TemporaryDataRetryLog> {


    /**
     * 重传记录
     * @param params 分页查询参数
     * @return 分页结果集
     * */
    Page<TemporaryDataRetryLogDTO> getPage(Map<String, Object> params);

    /**
     * 保存重传记录
     * @param temporaryDataRetryLog 重传记录参数
     * @return 是否成功
     * */
    Boolean temporaryDataRetryLog(TemporaryDataRetryLog temporaryDataRetryLog);

}

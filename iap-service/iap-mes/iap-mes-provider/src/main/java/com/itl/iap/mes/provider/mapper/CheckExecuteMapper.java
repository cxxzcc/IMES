package com.itl.iap.mes.provider.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.mes.api.dto.CheckExecuteAndPlanDTO;
import com.itl.iap.mes.api.dto.CheckExecuteQueryDto;
import com.itl.iap.mes.api.dto.CheckRecordDTO;
import com.itl.iap.mes.api.dto.RepairExecuteCountStatisticsDTO;
import com.itl.iap.mes.api.entity.CheckExecute;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 
 * @time 2018年11月14日
 * @since JDK 1.8
 */
@Repository
public interface CheckExecuteMapper  extends BaseMapper<CheckExecute> {



    IPage<CheckExecute> pageQuery(Page page, @Param(Constants.WRAPPER) Wrapper wrapper);


    List<CheckExecute> listQuery(@Param("checkExecute") CheckExecuteQueryDto checkExecute);


    /**
     * 点检保养记录
     * @param params id=设备id；分页参数
     * @return 点检记录列表
     * */
    List<CheckRecordDTO> checkRecord(Page<CheckRecordDTO> page, @Param("params") Map<String, Object> params);

    /**
     * 根据用户查询待点检工单数量
     * @param userId 用户id
     * @param site 工厂id
     * @return 待点检工单数量
     * */
    Integer getCheckCountByUser(@Param("userId") String userId, @Param("site") String site);

    /**
     * 点检工单统计
     * */
    List<RepairExecuteCountStatisticsDTO> checkExecuteStatistics(Map<String, Object> params);

    CheckExecute findById(@Param("id") String id);

    List<CheckExecuteAndPlanDTO> getCheckExecuteList(@Param(Constants.WRAPPER) Wrapper wrapper);
}

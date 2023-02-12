package com.itl.iap.mes.provider.mapper;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.mes.api.dto.RepairExecuteCountStatisticsDTO;
import com.itl.iap.mes.api.dto.UpkeepExecuteQueryDto;
import com.itl.iap.mes.api.dto.UpkeepRecordDTO;
import com.itl.iap.mes.api.entity.UpkeepExecute;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @time 2018年11月14日
 * @since JDK 1.8
 */
@Mapper
public interface UpkeepExecuteMapper extends BaseMapper<UpkeepExecute> {

    IPage<UpkeepExecute> findList(Page page,@Param(Constants.WRAPPER) Wrapper wrapper, @Param("sortName")String sortName,@Param("sortType") String sortType);


    List<UpkeepExecute> findList(@Param("upkeepExecute") UpkeepExecuteQueryDto upkeepExecute);

    /**
     * 设备保养记录
     *
     * @param params code=设备编号；分页参数
     * @return 保养记录列表
     */
    List<UpkeepRecordDTO> upkeepRecord(Page<UpkeepRecordDTO> queryPage, @Param("params") Map<String, Object> params);

    /**
     * 获取待保养的工单数
     *
     * @param site   工厂id
     * @param userId 用户id
     * @return
     */
    Integer getUpkeepCountByUser(@Param("userId") String userId, @Param("site") String site);
    /**
     * 点检工单统计
     * @param params 查询参数
     * */
    List<RepairExecuteCountStatisticsDTO> checkExecuteStatistics(Map<String, Object> params);

    UpkeepExecute findById(@Param("id") String id);
}

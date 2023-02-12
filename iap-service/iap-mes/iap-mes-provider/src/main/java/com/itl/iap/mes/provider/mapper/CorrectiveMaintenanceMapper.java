package com.itl.iap.mes.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.mes.api.dto.RepairRecordDTO;
import com.itl.iap.mes.api.entity.CorrectiveMaintenance;
import com.itl.mes.core.api.dto.AbnormalCountStatisticsDTO;
//import com.itl.mes.core.api.dto.DeviceCountStatisticsDTO;
import com.itl.mes.core.api.vo.ProductLineVo;
import com.itl.mes.core.api.vo.StationVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @time 2018年11月14日
 * @since JDK 1.8
 */
@Repository
public interface CorrectiveMaintenanceMapper extends BaseMapper<CorrectiveMaintenance> {
    IPage<CorrectiveMaintenance> findList(Page page, @Param("correctiveMaintenance") CorrectiveMaintenance correctiveMaintenance);

    List<Map<String, Object>> getDevice(String code);

    void deleteAndon(@Param("id") String id);

    /**
     * 维修执行 更新安灯触发记录
     */
    void updateAndonExecute(@Param("site") String site, @Param("repairNo") String repairNo, @Param("state") String state, @Param("user") String user, @Param("date") Date date, @Param("repairMethod") String repairMethod);

    /**
     * 受理 更新安灯触发记录
     */
    void updateAndonReceive(@Param("site") String site, @Param("repairNo") String repairNo, @Param("state") String state, @Param("user") String user, @Param("date") Date date);


    IPage<CorrectiveMaintenance> findListForPlanRepairUser(Page page, CorrectiveMaintenance correctiveMaintenance);

    /**
     * 设备维修记录
     *
     * @param queryPage 分页参数
     * @param params    查询参数
     * @return 维修记录列表
     */
    List<RepairRecordDTO> repairRecord(Page<RepairRecordDTO> queryPage, @Param("params") Map<String, Object> params);

    /**
     * 查询所有待处理的维修工单
     *
     * @param site 工厂id
     * @return 待处理维修工单数量
     */
    Integer getAllRepairCount(String site);

    /**
     * 根据用户id和工厂id查询待维修工单数
     *
     * @param userId 用户id
     * @param site   工厂id
     * @return 待处理维修工单数
     */
    Integer getRepairCountByUser(@Param("userId") String userId, @Param("site") String site);


    /**
     * 统计各个状态的维修工单的数量
     *
     * @param site 工厂id
     * @return
     */
    List<AbnormalCountStatisticsDTO> queryRepairCountStatisticsByState(String site);

    ProductLineVo getProductLineByBo(String productionLine);

    StationVo getStationByBo(String station);
}

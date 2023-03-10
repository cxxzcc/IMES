package com.itl.mes.pp.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.pp.api.dto.schedule.*;
import com.itl.mes.pp.api.entity.schedule.ScheduleEntity;

import java.util.List;
import java.util.Map;

/**
 * @author liuchenghao
 * @date 2020/11/12 10:33
 */

public interface ScheduleService {


    /**
     * 排程分页列表查询
     * @param scheduleQueryDTO
     * @return
     */
    IPage<ScheduleRespDTO> findList(ScheduleQueryDTO scheduleQueryDTO);

    /**
     * 排程分页列表查询Two
     * @param scheduleQueryTwoDTO
     * @return
     */
    IPage<ScheduleRespTwoDTO> findTwoList(ScheduleQueryTwoDTO scheduleQueryTwoDTO);

    IPage<ScheduleRespTwoDTO> findTwoPage(Map<String,Object> params);

    void save(ScheduleSaveDTO scheduleSaveDTO) throws CommonException;

    void saveTwo(List<ScheduleSaveTwoDTO> scheduleSaveTwoDTOList) throws CommonException;

    void delete(List<String> ids) throws CommonException;

    void deletePline(List<String> ids) throws CommonException;

    ScheduleDetailRespDTO findById(String id);

    ScheduleSaveTwoDTO findTwoById(String schedulePlineBo);

    ScheduleRespTwoDTO queryTwoById(String id);

    ScheduleDetailRespDTO findByIdWithCount(String id) throws CommonException;


    void updateState(String bo);

    void updateScheduleState(String bo) throws CommonException;

    void batchUpdateScheduleState(List<String> bos);

    void batchRelease(List<String> bos) throws CommonException;


    /**
     * 查询初始化排程产线（还未保存时显示）,根据车间bo查询产线数据
     * @param scheduleProductionLineQueryDTO
     * @return
     */
    IPage<ProductionLineResDTO> getInitScheduleProductionLineList(ScheduleProductionLineQueryDTO scheduleProductionLineQueryDTO);



    List<StationResDTO> getStationList(String productionLineBo);

    /**
     * 查询排程分配的产线（保存后显示）
     * @param scheduleProductionLineQueryDTO
     * @return
     */
    IPage<ScheduleProductionLineRespDTO> getScheduleProductionLine(ScheduleProductionLineQueryDTO scheduleProductionLineQueryDTO);

    /**
     * 根据产线粗略查询排程信息
     * @param scheduleShowDto
     * @return
     */
    IPage<Map<String, Object>> getByProductLine(ScheduleShowDto scheduleShowDto);

    /**
     * 根据工单号获取排程信息
     * @param shopOrderBO
     * @return
     */
    List<ScheduleEntity> getByScheduleShopOrder(String shopOrderBO);
}

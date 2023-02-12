package com.itl.iap.mes.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.mes.api.entity.RepairOperateLog;

import java.util.List;

/**
 * 工单操作记录
 * @author dengou
 * @date 2021/10/25
 */
public interface RepairOperateLogService extends IService<RepairOperateLog> {

    /**
     * 根据设备id查询列表， 操作时间倒序排列
     * @param deviceId 设备id
     * @return 返回操作时间倒序排列的工单操作记录
     * */
    RepairOperateLog getListByDeviceId(String deviceId);


    /**
     * 新增
     * @param repairOperateLog 操作记录信息
     * @return 是否成功
     * */
    Boolean add(RepairOperateLog repairOperateLog);

    /**
     * 根据工单id查询列表， 操作时间倒序排列
     * @param site 工厂id
     * @param orderId 工单id
     * @param orderType 工单类型
     * @return 返回操作时间倒序排列的工单操作记录
     * */
    List<RepairOperateLog> getListByOrderId(String orderId, String orderType, String site);

    /**
     * 根据工单编号查询列表， 操作时间倒序排列
     * @param site 工厂id
     * @param orderNo 工单编号
     * @param orderType 工单类型
     * @return 返回操作时间倒序排列的工单操作记录
     * */
    List<RepairOperateLog> getListByOrderNo(String orderNo, String orderType, String site);

    /**
     * 根据记录id查询记录详情
     * @param id 记录id
     * @return 记录详情
     * */
    RepairOperateLog getDetailById(String id);


}

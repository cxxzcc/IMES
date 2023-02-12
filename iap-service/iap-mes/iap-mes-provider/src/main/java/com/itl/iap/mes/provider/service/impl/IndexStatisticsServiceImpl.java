package com.itl.iap.mes.provider.service.impl;

import cn.hutool.core.thread.ThreadUtil;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.mes.api.dto.IndexStatisticsDTO;
import com.itl.iap.notice.client.NoticeService;
import com.itl.mes.core.client.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 首页统计service
 * @author dengou
 * @date 2021/9/30
 */
@Service
public class IndexStatisticsServiceImpl {

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private CorrectiveMaintenanceServiceImpl correctiveMaintenanceService;
    @Autowired
    private UpkeepExecuteServiceImpl upkeepExecuteService;
    @Autowired
    private CheckExecuteServiceImpl checkExecuteService;
    @Autowired
    private NoticeService noticeService;

    /**
     * 根据用户id和工厂查询首页统计信息
     * @param site 工厂id
     * @param userId 用户id
     * @return
     * */
    public IndexStatisticsDTO getStatisticsByUserAndSite(String userId, String userName,String site){
        IndexStatisticsDTO indexStatisticsDTO = new IndexStatisticsDTO();

        //查询设备总数
        Future<Integer> deviceCount = ThreadUtil.execAsync(() -> {
            ResponseData<Integer> result = deviceService.getDeviceCountBySite(site);
            if (result.isSuccess()) {
                return result.getData();
            }
            return 0;
        });
        //查询异常总数
        Future<Integer> abnormalCount = ThreadUtil.execAsync(() -> {
            return correctiveMaintenanceService.getAllRepairCount(site);
        });
        //查询待维修报修工单数
        Future<Integer> repairCount = ThreadUtil.execAsync(() -> {
            return correctiveMaintenanceService.getRepairCountByUser(userId, site);
        });
        //查询待保养保养工单数
        Future<Integer> upkeepCount = ThreadUtil.execAsync(() -> {
            return upkeepExecuteService.getUpkeepCountByUser(userName, site);
        });
        // 查询待点检点检工单数
        Future<Integer> checkCount = ThreadUtil.execAsync(() -> {
            return checkExecuteService.getCheckCountByUser(userName, site);
        });
        try {
            indexStatisticsDTO.setDeviceCount(deviceCount.get(10, TimeUnit.SECONDS));
            indexStatisticsDTO.setAbnormalCount(abnormalCount.get(10, TimeUnit.SECONDS));
            indexStatisticsDTO.setRepairCount(repairCount.get(10, TimeUnit.SECONDS));
            indexStatisticsDTO.setUpkeepCount(upkeepCount.get(10, TimeUnit.SECONDS));
            indexStatisticsDTO.setCheckCount(checkCount.get(10, TimeUnit.SECONDS));
        } catch (ExecutionException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }

        return indexStatisticsDTO;
    }


}

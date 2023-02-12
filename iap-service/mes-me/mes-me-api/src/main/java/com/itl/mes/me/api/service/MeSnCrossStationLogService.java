package com.itl.mes.me.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.me.api.entity.MeSnCrossStationLog;

/**
 * <p>
 * 条码过站记录 服务类
 * </p>
 *
 * @author dengou
 * @since 2021-12-07
 */
public interface MeSnCrossStationLogService extends IService<MeSnCrossStationLog> {


    /**
     * 新增过站记录
     * @param meSnCrossStationLog 新增实体参数
     * @return 是否成功
     * */
    Boolean addLog(MeSnCrossStationLog meSnCrossStationLog);


    /**
     * 查询过站次数
     * @param sn 条码
     * @param operationBo 工序bo
     * @param site 工厂id
     * @return 条码过站次数
     * */
    Integer getCountBySn(String sn, String site, String operationBo);

}

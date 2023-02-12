package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.BomItemSnByStation;
import com.itl.mes.core.api.dto.CheckBatchSnDto;
import com.itl.mes.core.api.dto.CheckBomItemAndStationSn;
import com.itl.mes.core.api.dto.CheckBomItemSn;
import com.itl.mes.core.api.entity.Sn;
import com.itl.mes.core.api.vo.CheckBatchSnVo;

/**
 * @author GKL
 * @date 2021/11/26 - 16:49
 * @since 2021/11/26 - 16:49 星期五 by GKL
 */
public interface CheckBatchSnService extends IService<Sn> {
    /**
     * 校验批次产品
     * @param checkBatchSnDto sn,工位,工厂
     * @return ResponseData.class
     */
     ResponseData<CheckBatchSnVo> checkBatchSn(CheckBatchSnDto checkBatchSnDto) ;



    /**
     * 校验对应的
     * @param checkBomItemSn checkBomItemSn
     * @return ResponseData.class
     */
    ResponseData<Boolean> checkBomItemSn(CheckBomItemSn checkBomItemSn);

    /**
     * 通过工位获取对应的所有数据
     * @param station station
     * @return ResponseData.class
     */
    ResponseData<BomItemSnByStation> getUsedListByStation(String station);

    /**
     * 校验
     * @param checkBomItemAndStationSn checkBomItemAndStationSn
     * @return ResponseData.class
     */
    ResponseData<Boolean> checkSnBomAndStation(CheckBomItemAndStationSn checkBomItemAndStationSn);
}

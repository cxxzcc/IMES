package com.itl.mes.me.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.ProductionDefectRecord;
import com.itl.mes.me.api.dto.RepairInputDto;
import com.itl.mes.me.api.dto.RepairTempDto;
import com.itl.mes.me.api.dto.ScrapOrRepairFinDto;
import com.itl.mes.me.api.entity.MeSfcRepair;
import com.itl.mes.me.api.vo.RepairObjVo;
import com.itl.mes.me.api.vo.RepairStationVo;

import java.util.List;


/**
 * Sfc维修
 *
 * @author renren
 * @date 2021-01-25 14:43:26
 */
public interface MeSfcRepairService extends IService<MeSfcRepair> {

    RepairStationVo frontDataVo(String snCode) throws CommonException;

    RepairObjVo saveInputRepair(List<RepairInputDto> repairInputList) throws CommonException;

    Integer scrapOrRepairFinish(ScrapOrRepairFinDto scrapOrRepairFinDto) throws CommonException;

    /**
     * 根据sn查询不合格代码列表
     * @param sn sn
     * @return 缺陷记录列表
     * */
    List<ProductionDefectRecord> getListBySn(String sn);

    /**
     * 条码报废
     * @param sn sn
     * @return 是否成功
     * */
    Boolean scrapped(String sn);


    /**
     * 暂存维修记录
     * @param sn sn
     * @param repairTempDtos 维修暂存数据
     * */
    Boolean saveTemp(String sn, List<RepairTempDto> repairTempDtos);

}


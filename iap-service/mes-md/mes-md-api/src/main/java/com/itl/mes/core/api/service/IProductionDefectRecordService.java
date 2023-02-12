package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.core.api.entity.ProductionDefectRecord;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author houfan
 * @since 2021-11-02
 */
public interface IProductionDefectRecordService extends IService<ProductionDefectRecord> {


    /**
     * 根据采集记录主键查询列表
     * @param collectionRecordId 采集记录主键
     * @return 缺陷记录列表
     * */
    List<ProductionDefectRecord> getListByCollectionRecordId(String collectionRecordId);

    /**
     * 根据sn查询列表
     * @param sn sn
     * @return 缺陷记录列表
     * */
    List<ProductionDefectRecord> getListBySn(String sn);

    /**
     * 根据id列表查询不合格代码列表
     * @param ids id列表
     * @return 缺陷记录列表
     * */
    List<ProductionDefectRecord> getListByIds(List<String> ids);

    /**
     * 更新不合格记录是否处理标识
     * @param defectRecordIds 不合格记录列表
     * @return 是否成功
     * */
    Boolean updateHandleFlag(List<String> defectRecordIds);

}

package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.core.api.entity.RecordOfProductTest;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author houfan
 * @since 2021-11-02
 */
public interface IRecordOfProductTestService extends IService<RecordOfProductTest> {


    /**
     * 产品检验记录
     * @param id 采集记录id
     * @return 产品检验记录列表
     * */
    List<RecordOfProductTest> getListByCollectionRecordId(String id);
}

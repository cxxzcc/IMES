package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.core.api.constant.TemporaryDataTypeEnum;
import com.itl.mes.core.api.entity.TemporaryData;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author houfan
 * @since 2021-11-02
 */
public interface ITemporaryDataService extends IService<TemporaryData> {


    /**
     * 保存临时数据， 根据sn和station判断是否存在， 存在则更新，不存在则新增
     * */
    Boolean addOrUpdate(TemporaryData temporaryData);

    /**
     * 根据sn列表和工位删除临时数据
     * @param sns sn列表
     * @param station 工位
     * @param type 类型 {@link TemporaryDataTypeEnum#getCode()}
     * */
    Boolean removeBySns(List<String> sns, String station, String type);

    /**
     * 根据sn列表和工位删除临时数据
     * @param sns sn列表
     * @param station 工位
     * @param type 类型 {@link TemporaryDataTypeEnum#getCode()}
     * */
    Boolean removeListBySns(List<String> sns, String station, String type);

    /**
     * 根据sn和station查询暂存数据
     * @param sn 条码
     * @param station 工位
     * @param type 类型
     * @return 暂存数据
     * */
    TemporaryData getBySnAndStation(String sn, String station, String type);
    /**
     * 根据sn和station查询暂存数据
     * @param sn 条码
     * @param station 工位
     * @param types 类型
     * @return 暂存数据
     * */
    List<TemporaryData> getBySnAndStation(String sn, String station, List<String> types);

}

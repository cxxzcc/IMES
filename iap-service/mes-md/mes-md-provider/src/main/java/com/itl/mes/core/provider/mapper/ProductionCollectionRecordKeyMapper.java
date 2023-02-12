package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.core.api.entity.ProductionCollectionRecordKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 生产采集记录-关键件 mapper
 * @author dengou
 * @date 2021/11/11
 */
@Mapper
public interface ProductionCollectionRecordKeyMapper extends BaseMapper<ProductionCollectionRecordKey> {

    /**
     * 根据生产采集记录id查询关键件列表
     * */
    List<ProductionCollectionRecordKey> getListByCollectionRecordId(String id);
}

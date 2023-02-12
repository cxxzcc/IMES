package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.core.api.entity.ProductionDefectRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author houfan
 * @since 2021-11-02
 */
public interface ProductionDefectRecordMapper extends BaseMapper<ProductionDefectRecord> {


    /**
     * 根据sn查询列表
     * @param sn sn
     * @return 缺陷记录列表
     * */
    List<ProductionDefectRecord> getListBySn(String sn);

    /**
     * 更新不合格记录是否处理标识
     * @param defectRecordIds 不合格记录列表
     * @return 是否成功
     * */
    Integer updateHandleFlag(@Param("ids") List<String> defectRecordIds);

}

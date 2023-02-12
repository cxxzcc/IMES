package com.itl.mes.core.provider.mapper;

import com.itl.mes.me.api.dto.MeProductInspectionItemsOrderDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QualitativeInspectionMapper {

    /**
     * 根据工单编码、工序和检验标识过滤出该工单的检验项目
     * @param operationBo 工序bo
     * @param sn sn
     * @param site 工厂id
     * @param checkMark 0=定性 1=定量
     * @return
     */
    List<MeProductInspectionItemsOrderDto> findProductInspectionItemsByOperationBo(
            @Param("operationBo")String operationBo, @Param("sn") String sn, @Param("site") String site, @Param("checkMark") String checkMark);



}

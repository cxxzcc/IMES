package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.MeProductInspectionItemsOrderNcCode;
import com.itl.mes.core.api.entity.NcCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 产品检验项目不合格记录表-工单副本
 *
 * @author chenjx1
 * @date 2021-12-10
 */
@Mapper
public interface MeProductInspectionItemsOrderNcCodeMapper extends BaseMapper<MeProductInspectionItemsOrderNcCode> {


//    IPage get(Page page, @Param("id")String id);
    NcCode selectByNcCode(@Param("ncCode") NcCode ncCode) throws CommonException;

}

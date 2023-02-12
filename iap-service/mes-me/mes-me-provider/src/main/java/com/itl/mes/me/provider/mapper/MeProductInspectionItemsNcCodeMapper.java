package com.itl.mes.me.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.NcCode;
import com.itl.mes.me.api.entity.MeProductInspectionItemsNcCode;
import com.itl.mes.me.api.vo.MeProductInspectionItemsNcCodeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品检验项目不合格记录表
 *
 * @author chenjx1
 * @date 2021-12-08
 */
@Mapper
public interface MeProductInspectionItemsNcCodeMapper extends BaseMapper<MeProductInspectionItemsNcCode> {


//    IPage get(Page page, @Param("id")String id);
    NcCode selectByNcCode(@Param("ncCode") NcCode ncCode) throws CommonException;

    List<MeProductInspectionItemsNcCodeVo> listItemNcCodesTwo(@Param("ncCode") MeProductInspectionItemsNcCodeVo meProductInspectionItemsNcCodeVo);

}

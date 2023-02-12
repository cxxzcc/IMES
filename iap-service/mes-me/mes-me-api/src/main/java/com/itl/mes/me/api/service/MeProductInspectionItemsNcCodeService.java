package com.itl.mes.me.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.me.api.entity.MeProductInspectionItemsNcCode;
import com.itl.mes.me.api.vo.MeProductInspectionItemsNcCodeVo;

import java.util.List;

/**
 * 产品检验项目-不良代码
 *
 * @author chenjx1
 * @date 2021-12-08
 */
public interface MeProductInspectionItemsNcCodeService extends IService<MeProductInspectionItemsNcCode> {

    /**
     * 数据校验
     */
    Boolean checkItemsNcCode(MeProductInspectionItemsNcCode meProductInspectionItemsNcCode) throws CommonException;

    /**
     * 保存集合
     */
    Boolean saveList(List<MeProductInspectionItemsNcCode> meProductInspectionItemsNcCodeList) throws CommonException;

    /**
     * 列表-产品检验项+关联不良代码信息
     */
    List<MeProductInspectionItemsNcCodeVo> listItemNcCodesTwo(MeProductInspectionItemsNcCodeVo meProductInspectionItemsNcCodeVo);

}


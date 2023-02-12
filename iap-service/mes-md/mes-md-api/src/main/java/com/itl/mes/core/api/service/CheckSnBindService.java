package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.CheckItemCodeDto;
import com.itl.mes.core.api.dto.CheckSnBindDto;
import com.itl.mes.core.api.entity.Sn;
import com.itl.mes.core.api.vo.CheckSnBindVo;

/**
 * 验证单体条码绑定Service
 *
 * @author GKL
 * @date 2021/11/4 - 14:30
 * @since 2021/11/4 - 14:30 星期四 by GKL
 */
public interface CheckSnBindService extends IService<Sn> {
    /**
     * 输入条码，校验条码
     * @param dto 条码,工位
     * @return ResponseData.class
     */
    ResponseData<CheckSnBindVo> checkSn(CheckSnBindDto dto);

    /**
     * 输入物料编码校验
     * @param dto bom相关参数，物料编码
     * @return ResponseData。class
     */
    ResponseData<Boolean> checkItemCode(CheckItemCodeDto dto);

    /**
     * 通过sn查询对应ItemBom
     * @param dto
     * @return
     */
    ResponseData<String> queryItemBomBySn(CheckSnBindDto dto);
}

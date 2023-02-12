package com.itl.mes.me.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.me.api.dto.LabelPrintDetailQueryDto;
import com.itl.mes.me.api.entity.LabelPrintDetail;
import com.itl.mes.me.api.vo.CheckBarCodeVo;
import com.itl.mes.me.api.vo.LabelPrintDetailVo;

import java.util.List;

/**
 * <p>
 * 标签打印范围明细 服务类
 * </p>
 *
 * @author liuchenghao
 * @since 2021-03-23
 */
public interface LabelPrintDetailService extends IService<LabelPrintDetail> {


    IPage<LabelPrintDetailVo> findList(LabelPrintDetailQueryDto labelPrintDetailQueryDto) throws CommonException;


    List<String> barCodeDetailPrint(String detailBo) throws CommonException;

    /**
     * 根据类型检查条码
     * @param barCode 条码
     * @param elementType 条码类型
     * @return
     */
    ResponseData<CheckBarCodeVo> checkBarCode(String barCode, String elementType);
}

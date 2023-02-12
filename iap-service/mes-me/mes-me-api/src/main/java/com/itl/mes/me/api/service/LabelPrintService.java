package com.itl.mes.me.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.me.api.dto.LabelPrintBarCodeDto;
import com.itl.mes.me.api.dto.LabelPrintQueryDto;
import com.itl.mes.me.api.dto.LabelPrintSaveDto;
import com.itl.mes.me.api.entity.LabelPrint;
import com.itl.mes.me.api.vo.LabelPrintVo;

import java.util.List;

/**
 * <p>
 * 标签打印 服务类
 * </p>
 *
 * @author liuchenghao
 * @since 2021-03-23
 */
public interface LabelPrintService extends IService<LabelPrint> {

    /**
     * 新建标签打印
     * @param labelPrintSaveDto
     */
    void addLabelPrint(LabelPrintSaveDto labelPrintSaveDto) throws CommonException;

    /**
     * 查询打印集合
     * @param labelPrintQueryDto
     * @return
     */
    IPage<LabelPrintVo> findList(LabelPrintQueryDto labelPrintQueryDto) throws CommonException;



    /**
     * 条码打印
     * @param barCodeDto
     * @return
     */
    List<String> barCodePrint(LabelPrintBarCodeDto barCodeDto) throws CommonException;

}

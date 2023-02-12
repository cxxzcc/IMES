package com.itl.mes.me.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.me.api.dto.ShopOrderPackDTO;
import com.itl.mes.me.api.vo.ShopOrderPackRealTreeVO;
import com.itl.mes.me.api.vo.ShopOrderPackTemTreeVO;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.me.api.entity.PackTemp;

import java.util.List;
import java.util.Map;

/**
 * 包装
 *
 * @author cch
 * @date 2021-06-16
 */
public interface PackTempService extends IService<PackTemp> {

    /**
     * 包装-扫描SN
     * @param sn
     * @param operationBo
     * @return
     * @throws CommonException
     */
    Map<String, Object> scanSn(String sn, String operationBo) throws CommonException;

    /**
     * 查询当前工序的待包装集合, 如果为空, 返回emptyList
     * @param operationBo 工序Bo
     * @return
     */
    List<PackTemp> listWaitToPack(String operationBo);

    /**
     * 执行装箱操作
     * @param list
     * @return
     */
    String executePack(List<PackTemp> list) throws CommonException;

    void scanSnPack(String sn);

    List<ShopOrderPackTemTreeVO> getPackTempSn();

    void saveScanPackSn(List<String> shopOrderList);

    List<ShopOrderPackRealTreeVO> getPackReal(ShopOrderPackDTO shopOrderPackDTO);
}


package com.itl.mom.label.api.service.label;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mom.label.api.dto.label.*;
import com.itl.mom.label.api.dto.ruleLabel.LabelPrintBarCodeDto;
import com.itl.mom.label.api.entity.label.LabelPrint;
import com.itl.mom.label.api.entity.label.Sn;
import com.itl.mom.label.api.vo.ItemLabelListVo;
import com.itl.mom.label.api.vo.LabelPrintVo;
import com.itl.mom.label.api.vo.ScanReturnVo;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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
     *
     * @param labelPrintSaveDto
     */
    List<Sn> addLabelPrint(LabelPrintSaveDto labelPrintSaveDto) throws CommonException, SQLException;

    /**
     * 查询打印集合
     *
     * @param labelPrintQueryDto
     * @return
     */
    IPage<LabelPrintVo> findList(LabelPrintQueryDto labelPrintQueryDto) throws CommonException;


    /**
     * 条码打印
     *
     * @param barCodeDto
     * @return
     */
    List<String> barCodePrint(LabelPrintBarCodeDto barCodeDto) throws CommonException, SQLException;

    /**
     * 扫描条码带出信息
     *
     * @param barCode     条码
     * @param elementType 条码类型
     * @return
     */
    ResponseData<ScanReturnVo> scanReturn(String barCode, String elementType);

    /**
     * 物料标签列表
     *
     * @param itemLabelQueryDTO
     * @return
     */
    IPage<ItemLabelListVo> getItemLabelPageList(ItemLabelQueryDTO itemLabelQueryDTO);

    /**
     * 挂起
     *
     */
    void hangup(ItemLabelQueryUpDTO itemLabelQueryUpDTO);



    ResponseData<LabelPrint> getLabelPrintBo(String labelPrintBo);

    /**
     * 物料编码查询对应的条码
     * @param itemCode itemCode
     * @return ResponseData.class
     */
    ResponseData<List<String>> queryCodeByItem(List<String > itemCode);

    ResponseData<String> queryItemBySn(String sn);

    /**
     * 根据sn查询工单编号
     * @param sn sn
     * */
    String queryShopOrderBySn(String sn);

    /**
     * 工单条码导入
     * */
    void saveByImport(List<ShopOrderSnImportDto> ts) throws CommonException;

    /**
     * 在线打印
     * */
    LabelPrintResponseDTO inProductionLinePrint(LabelInProductLinePrintDTO labelInProductLinePrintDTO);

    /**
     * 补打
     * */
    Map<String, Object> additionalPrint(LabelInProductLinePrintDTO labelInProductLinePrintDTO);
}

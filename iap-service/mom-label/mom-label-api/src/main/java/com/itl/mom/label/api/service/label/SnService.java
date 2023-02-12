package com.itl.mom.label.api.service.label;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mom.label.api.dto.label.LabelTransferRequestDTO;
import com.itl.mom.label.api.dto.label.SnQueryDto;
import com.itl.mom.label.api.entity.label.Sn;
import com.itl.mom.label.api.vo.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标签打印范围明细 服务类
 * </p>
 *
 * @author liuchenghao
 * @since 2021-03-23
 */
public interface SnService extends IService<Sn> {


    IPage<SnVo> findList(SnQueryDto snQueryDto) throws CommonException;
    IPage<LabelPrintLogVo> findLog(SnQueryDto snQueryDto) throws CommonException;
    IPage<MeProductStatusVo> findProductStatus(SnQueryDto snQueryDto) throws CommonException;


    List<String> barCodeDetailPrint(String detailBo, Integer printCount, String printer) throws CommonException;

    /**
     * 根据类型检查条码
     * @param barCode 条码
     * @param elementType 条码类型
     * @return
     */
    ResponseData<CheckBarCodeVo> checkBarCode(String barCode, String elementType);

    /**
     * 根据条码查询物料信息
     * @param sn
     * @return
     */
    Map<String, String> getItemInfoAndSnStateBySn(String sn);

    /**
     * 领用Sn
     * @param sn
     * @param workShopBo
     * @param productLineBo
     */
    void collar(String sn, String workShopBo, String productLineBo);

    /**
     * 根据map<sn,state>信息改变对应条码状态
     * @param map
     * @return
     */
    Boolean changeSnStateByMap(Map<String, String> map);

    /**
     * 根据map<sn,state>大批量生产条码
     * @param sns
     * @return
     */
    void saveBatchAuto(List<Sn> sns) throws SQLException;

    /**
     * 工单拆单-变更条码工单
     * @param snBoList
     * @param newOrderBo
     * @return
     */
    ResponseData updateOrderBo(List<String> snBoList, String newOrderBo);

    /**
     * 拆单使用：查询拆当前工单产品SN，未上线状态的条码BO
     * @param orderBo
     * @param onLine
     * @return
     */
    List<String> queryOrderBoList(String orderBo,int onLine);


    /**
     * 条码转移, 将旧工单的条码转移到新工单下。条码工单关联关系会发生改变，关闭原来的条码对应产品状态记录，新增另一条条码对应产品状态记录
     * 条件: 条码对应的产品状态不为报废和完工。新工单的可打印条码数量大于所转移的条码数量。
     * @param labelTransferRequestDTO 转移参数
     * @return 是否成功
     * */
    Boolean transferLabels(LabelTransferRequestDTO labelTransferRequestDTO);

    /**
     * 工单拆单使用
     * 条码转移, 将旧工单的条码转移到新工单下。条码工单关联关系会发生改变，关闭原来的条码对应产品状态记录，新增另一条条码对应产品状态记录
     * 条件: 条码对应的产品状态不为报废和完工。新工单的可打印条码数量大于所转移的条码数量。
     * @param labelTransferRequestDTO 转移参数
     * @return 是否成功
     * */
    Boolean transferLabelsAsOrder(LabelTransferRequestDTO labelTransferRequestDTO);

    /**
     * 条码转移-分页查询条码列表
     * @param params 分页查询参数
     * */
    Page<LabelTransVo> labelTransList(Map<String, Object> params);

}

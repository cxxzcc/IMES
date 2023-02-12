package com.itl.mom.label.api.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.ProductStateUpdateDto;
import com.itl.mes.core.api.dto.UpdateDoneDto;
import com.itl.mom.label.api.entity.MeProductStatus;
import com.itl.mom.label.api.vo.MeProductStatusQueryVo;

import java.util.List;

/**
 * product_status(MeProductStatus)表服务接口
 *
 * @author makejava
 * @since 2021-10-22 11:09:49
 */
public interface MeProductStatusService extends IService<MeProductStatus> {


    /**
     * 根据snboList关闭产品状态记录
     * @param snboList 条码id列表
     * @return 是否成功
     * */
    Boolean closeBySnboList(List<String> snboList);

    /**
     * 根据sn查询产品状态
     * @param site 工厂
     * @param sn 条码编号
     * @return 产品状态
     * */
    MeProductStatus getBySn(String sn, String site);
    /**
     * 根据sn查询产品状态
     * @param snBos snbo列表
     * @return 产品状态
     * */
    List<MeProductStatus> getBySnBos(List<String> snBos);

    /**
     * 根据sn跟状态查询对应的数据
     * @param sn sn
     * @param status 状态
     * @return ResponseData.class
     */
    ResponseData<List<MeProductStatusQueryVo>> findProductStatusBySnAndStatus(String sn, int status);

    /**
     *
     * @param productStateUpdateDto  productStateUpdateDto
     * @return ResponseData.class
     */
    ResponseData<Boolean> productStateUpdate(List<ProductStateUpdateDto> productStateUpdateDto);

    /**
     * 根据bo修改完成标识
     * @param updateDoneDtos productStateBo
     * @return ResponseData.class
     */
    ResponseData updateProductStatusDoneByBo(List<UpdateDoneDto> updateDoneDtos);

    /**
     * 更新下工序
     * */
    Boolean updateNextProcess(ProductStateUpdateDto productStateUpdateDtos);

    /**
     * 批量更新下工序
     * @param snBos snbo列表
     * @param nextProcessId 下工序id
     * @param nextProcessName 下工序名称
     * @return 是否更新成功
     * */
    Boolean updateNextProcessBatch(List<String> snBos, String nextProcessId, String nextProcessName);

    /**
     * 根据ids更新是否挂起
     * @param ids 产品状态id集合
     * @param isHold 是否挂起
     * @param checkSnClosed 是否校验条码关闭
     * @return 是否成功
     * */
    Boolean updateIsHoldByIds(List<String> ids, Integer isHold, Boolean checkSnClosed);
    /**
     * 根据ids更新是否挂起
     * @param snBoList 条码bo列表
     * @param isHold 是否挂起
     * @return 是否成功
     * */
    Boolean updateIsHoldBySnList(List<String> snBoList, Integer isHold);

    /**
     * 根据snbo列表查询  工单和sn
     * @param snBoList snbo列表
     * */
    List<MeProductStatus> getShopOrderBySnBoList(List<String> snBoList);
}

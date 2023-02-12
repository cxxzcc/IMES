package com.itl.mes.me.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.me.api.dto.UpdateBatchSnProcessDto;
import com.itl.mes.me.api.dto.UpdateSnRouteDto;
import com.itl.mes.me.api.entity.MeSnRouter;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 条码-工艺路线表 服务类
 * </p>
 *
 * @author dengou
 * @since 2021-11-26
 */
public interface MeSnRouterService extends IService<MeSnRouter> {


    /**
     * 根据sn查询工艺路线
     * @param snList 条码列表
     * @return 条码工艺路线
     * */
    MeSnRouter getSnRouteBySnList(List<String> snList);

    /**
     * 保存条码工艺路线, 保存完工艺路线后，默认值为开始之后的第一个节点。
     * @param updateSnRouteDto 条码工艺路线信息
     * @return 是否成功
     * */
    Boolean saveSnRoute(UpdateSnRouteDto updateSnRouteDto);

    /**
     * 更新工序-获取可选择的下工序列表
     * @param sn sn
     * @return 工序信息列表
     * */
    List<Map<String, Object>> getNexProcessListBySn(String sn);

    /**
     * 调整工序, 更新sn当前工序（对应采集记录的下一工序）
     * @param sn sn
     * @param processId me_sn_route.json.nodeList.id
     * @return 是否成功
     * */
    Boolean updateNexProcessBySn(String sn, String processId);

    /**
     * 批量调整工序
     * @param updateBatchSnProcessDto 更新参数
     * @return 是否成功
     * */
    Boolean updateNexProcessBySnBatch(UpdateBatchSnProcessDto updateBatchSnProcessDto);

    /**
     * 新增条码工艺路线 me_sn_route
     * */
    Boolean addSnRoute(UpdateSnRouteDto updateSnRouteDto);

    /**
     * 根据条码查询工艺路线 me_sn_route
     * */
    MeSnRouter getBySn(String sn, String site);
}

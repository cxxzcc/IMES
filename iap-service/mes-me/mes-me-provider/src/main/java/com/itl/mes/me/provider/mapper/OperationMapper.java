package com.itl.mes.me.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.mes.me.api.dto.OperationQueryDto;
import com.itl.mes.me.api.entity.Operation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 操作
 *
 * @author yx
 * @date 2021-05-31
 */
@Mapper
public interface OperationMapper extends BaseMapper<Operation> {

    /**
     * 查询SN与工艺路线的关联表,获取SN与工艺路线的绑定信息
     *
     * @param sn 条码
     * @return
     */
    List<Map<String, String>> getSnRouter(@Param("sn") String sn);

    /**
     * 根据SN查询获取工单Bo
     *
     * @param sn 条码
     * @return
     */
    String getShopOrderBoBySn(@Param("sn") String sn);

    /**
     * 保存SN-工艺步骤信息
     *
     * @param sn     条码
     * @param json   工艺步骤json
     * @param nodeId 当前节点
     */
    void saveSnRouter(@Param("sn") String sn, @Param("json") String json, @Param("nodeId") String nodeId, @Param("site") String site);

    /**
     * 更改sn_router表中的当前节点.
     *
     * @param sn         条码
     * @param nextNodeId 下一节点Id
     */
    void changeSnRouterCurrentNode(@Param("sn") String sn, @Param("nextNodeId") String nextNodeId);


    IPage<Operation> selectPageVo(IPage<OperationQueryDto> page, @Param("page") OperationQueryDto queryDto, @Param("siteId") String siteId);

    String findLableTypeBySn(@Param("sn") String sn);

    String getIdBySnAndState(@Param("sn") String sn);
}

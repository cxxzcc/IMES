package com.itl.mes.me.provider.mapper;

import com.itl.mes.me.api.bo.OrderAndCodeRuleBO;
import com.itl.mes.me.api.entity.PackRuleTemp;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工序-包装规则
 *
 * @author cch
 * @date 2021-06-16
 */
public interface PackRuleTempMapper extends BaseMapper<PackRuleTemp> {

    /**
     * 根据工序Bo查询在当前工序的包装规则, 如果为空,返回emptyList
     * @param operationBo 工序Bo
     * @return
     */
    List<PackRuleTemp> listByOperationBo(@Param("operationBo") String operationBo);

    List<OrderAndCodeRuleBO> findOrderAndPackCodeRuleInfo(String sn);
}

package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.core.api.entity.Operation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工序表 Mapper 接口
 * </p>
 *
 * @author space
 * @since 2019-05-28
 */

public interface OperationMapper extends BaseMapper<Operation> {

    /**
     * 获取当前版本工序，返回字段包含：OPERATION：工序，OPERATION_DESC：工序描述
     *
     * @param site 工厂
     * @return List<Map<String,Object>>
     */
    List<Map<String, Object>> selectCurrentVersionOperations(@Param("site") String site);

    Integer updateOperationVersionType(@Param("site") String site, @Param("operation") String operation);

    List<Operation> selectTop(@Param("site") String site);
}
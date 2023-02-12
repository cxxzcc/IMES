package com.itl.mes.me.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.me.api.entity.Action;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * 过站动作
 *
 * @author cch
 * @date 2021-05-31
 */
@Mapper
public interface ActionMapper extends BaseMapper<Action> {
    List<HashMap<String,Object>> find(@Param("id") String id);
    List<HashMap<String,Object>> infoByOperationID(@Param("id") String id);
}

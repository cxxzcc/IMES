package com.itl.mes.me.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.me.api.entity.ActionOperation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * ${comments}
 *
 * @author cch
 * @date 2021-05-31
 */
@Mapper
public interface ActionOperationMapper extends BaseMapper<ActionOperation> {


    List<HashMap<String,Object>> find(@Param("id") String id);

}

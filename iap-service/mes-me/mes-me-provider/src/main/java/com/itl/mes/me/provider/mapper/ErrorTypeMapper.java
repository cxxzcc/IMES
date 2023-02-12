package com.itl.mes.me.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.me.api.entity.ErrorType;
import org.apache.ibatis.annotations.Mapper;

/**
 * 异常类型维护mapper
 * @author dengou
 * @date 2021/11/3
 */
@Mapper
public interface ErrorTypeMapper extends BaseMapper<ErrorType> {
}

package com.itl.iap.mes.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.iap.mes.api.dto.ErrorTypeDto;
import com.itl.iap.mes.api.entity.ErrorType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yx
 * @date 2021/8/19
 * @since 1.8
 */
public interface ErrorTypeMapper extends BaseMapper<ErrorType> {

    /**
     * 查询最上层
     * @param errorTypeDto
     * @return
     */
    List<ErrorTypeDto> pageList(@Param("errorTypeDto") ErrorTypeDto errorTypeDto);
}

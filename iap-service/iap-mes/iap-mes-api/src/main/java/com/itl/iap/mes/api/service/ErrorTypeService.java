package com.itl.iap.mes.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.mes.api.dto.ErrorTypeDto;
import com.itl.iap.mes.api.entity.ErrorType;

import java.util.List;

/**
 * @author yx
 * @date 2021/8/19
 * @since 1.8
 */
public interface ErrorTypeService extends IService<ErrorType> {
    /**
     * 查询
     * @param errorTypeDto
     * @return
     */
    List<ErrorTypeDto> listTree(ErrorTypeDto errorTypeDto);

    /**
     * 删除
     * @param ids
     */
    void delete(List<String> ids);
}

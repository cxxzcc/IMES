package com.itl.mes.me.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.me.api.entity.ErrorType;

import java.util.List;

/**
 * 异常代码维护service
 * @author dengou
 * @date 2021/11/3
 */
public interface ErrorTypeService extends IService<ErrorType> {
    /**
     * 查询异常树
     * @return 异常树列表
     * */
    List<ErrorType> getTree();
    /**
     * 查询异常树
     * @return 异常树列表
     * */
    List<ErrorType> getLovTree();

    /**
     * 新增异常
     * @param errorType 异常类型参数
     * @return 是否成功
     * */
    Boolean add(ErrorType errorType);

    /**
     * 根据id查询异常类型详情
     * @param id 异常类型id
     * */
    ErrorType detail(String id);

    /**
     * 修改节点
     * @param errorType 异常类型
     * @return 是否成功
     * */
    Boolean updateErrorType(ErrorType errorType);

    /**
     * 删除节点、
     * @param  id 异常类型id
     * @return 是否成功
     * */
    Boolean delete(String id);
}

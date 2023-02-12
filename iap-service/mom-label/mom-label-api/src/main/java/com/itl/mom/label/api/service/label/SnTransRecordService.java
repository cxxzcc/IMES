package com.itl.mom.label.api.service.label;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mom.label.api.entity.label.SnTransRecord;

import java.util.Map;

/**
 * 条码转移记录service
 * @author dengou
 * @date 2021/11/1
 */
public interface SnTransRecordService extends IService<SnTransRecord> {

    /**
     * 新增转移记录
     * @param snTransRecord 条码转移详情
     * @return 是否成功
     * */
    Boolean add(SnTransRecord snTransRecord);


    /**
     * 根据条码id查询转移记录
     * @param params 分页查询参数
     * @return 条码转移记录
     * */
    Page<SnTransRecord> page(Map<String, Object> params);
}

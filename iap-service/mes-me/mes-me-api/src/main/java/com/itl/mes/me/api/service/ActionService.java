package com.itl.mes.me.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.me.api.entity.Action;

import java.util.HashMap;
import java.util.List;

/**
 * 过站动作
 *
 * @author cch
 * @date 2021-05-31
 */
public interface ActionService extends IService<Action> {
    List<HashMap<String,Object>> getDetails(String id);
    List<HashMap<String,Object>> getDetailsByOperationID(String id);
}


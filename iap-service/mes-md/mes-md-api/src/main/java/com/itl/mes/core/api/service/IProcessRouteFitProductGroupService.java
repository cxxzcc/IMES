package com.itl.mes.core.api.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.core.api.entity.ProcessRouteFitProductGroup;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 产品组工艺路线设置表 服务类
 * </p>
 *
 * @author lK
 * @since 2021-10-15
 */
public interface IProcessRouteFitProductGroupService extends IService<ProcessRouteFitProductGroup> {

    IPage<Object> getGroupRoute(Map<String, Object> map);

    List<ProcessRouteFitProductGroup> getProcessRouteFitProductGroup(ProcessRouteFitProductGroup processRouteFitProductGroup);

}

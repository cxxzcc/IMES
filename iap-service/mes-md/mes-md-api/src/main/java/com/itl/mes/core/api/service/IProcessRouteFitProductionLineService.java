package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.entity.ProcessRouteFitProductionLine;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 产线工艺路线设置表 服务类
 * </p>
 *
 * @author lK
 * @since 2021-10-15
 */
public interface IProcessRouteFitProductionLineService extends IService<ProcessRouteFitProductionLine> {

    IPage<Object> getProductLineRoute(Map<String, Object> map);

    List<ProcessRouteFitProductionLine> getProcessRouteFitProductionLine(ProcessRouteFitProductionLine processRouteFitProductionLine);

}

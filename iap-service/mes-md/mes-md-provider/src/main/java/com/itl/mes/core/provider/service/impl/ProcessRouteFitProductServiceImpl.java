package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.entity.ProcessRouteFitProduct;
import com.itl.mes.core.api.service.IProcessRouteFitProductService;
import com.itl.mes.core.provider.mapper.ProcessRouteFitProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 产品工艺路线设置表-中间表 服务实现类
 * </p>
 *
 * @author lK
 * @since 2021-10-15
 */
@Service
public class ProcessRouteFitProductServiceImpl extends ServiceImpl<ProcessRouteFitProductMapper, ProcessRouteFitProduct> implements IProcessRouteFitProductService {

    @Autowired
    private ProcessRouteFitProductMapper productMapper; // 产品工艺路线设置表

    @Override
    public IPage<Object> getProductRoute(Map<String, Object> map) {
        Integer current = (Integer) map.get("current");
        Integer size = (Integer) map.get("size");
        Page<Object> page = new Page<Object>(current.longValue(), size.longValue());

        map.put("site", UserUtils.getSite());
        return productMapper.getProductRoute(page, map);
    }

    @Override
    public List<ProcessRouteFitProduct> getProductRouteFitProduct(ProcessRouteFitProduct processRouteFitProduct) {
        processRouteFitProduct.setSite(UserUtils.getSite());
        QueryWrapper<ProcessRouteFitProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(processRouteFitProduct);
        return productMapper.selectList(queryWrapper);
    }

}

package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.RouterHandleBO;
import com.itl.mes.core.api.entity.NcDispRouter;
import com.itl.mes.core.api.service.NcDispRouterService;
import com.itl.mes.core.api.vo.NcDispRouterVo;
import com.itl.mes.core.provider.mapper.NcDispRouterMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yx
 * @date 2021/3/22
 * @since JDK1.8
 */
@Service
public class NcDispRouterServiceImpl extends ServiceImpl<NcDispRouterMapper, NcDispRouter> implements NcDispRouterService {
    @Override
    public List<NcDispRouter> selectList() {
        return list(null);
    }

    @Override
    public void saveByNcCodeBo(String ncCodeBO, List<NcDispRouterVo> assignedNcDispRouterVos) throws CommonException {
        String site = UserUtils.getSite();
        delete(ncCodeBO);
        if (CollUtil.isNotEmpty(assignedNcDispRouterVos)) {
            List<NcDispRouter> list = assignedNcDispRouterVos.stream()
                    .map(x -> new NcDispRouter().setNcCodeBo(ncCodeBO)
                            .setRouterBo(new RouterHandleBO(site, x.getRouter(), x.getVersion()).getBo()))
                    .collect(Collectors.toList());
            saveBatch(list);
        }
    }

    @Override
    public void delete(String ncCodeBO) throws CommonException {
        remove(new QueryWrapper<NcDispRouter>().lambda().eq(NcDispRouter::getNcCodeBo, ncCodeBO));
    }
}

package com.itl.mes.me.provider.service.impl;

import cn.hutool.core.lang.Snowflake;
import com.google.common.collect.Lists;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.vo.ShopOrderBomComponnetVo;
import com.itl.mes.me.api.util.GeneratorId;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.itl.mes.me.provider.mapper.AssyTempMapper;
import com.itl.mes.me.api.entity.AssyTemp;
import com.itl.mes.me.api.service.AssyTempService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 装配临时表
 *
 * @author cch
 * @date 2021-06-08
 */
@Service
public class AssyTempServiceImpl extends ServiceImpl<AssyTempMapper, AssyTemp> implements AssyTempService {

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void saveAssyTempInfo(String productSn, List<ShopOrderBomComponnetVo> list) {
        // 保存到临时表
        final String site = UserUtils.getSite();
        Date date = new Date();
        String userName = UserUtils.getCurrentUser().getUserName();
        Snowflake snowflake = new GeneratorId().getSnowflake();
        List<AssyTemp> ret = Lists.newArrayList();
        list.parallelStream().forEach(x -> {
            AssyTemp temp = new AssyTemp();
            temp.setId(snowflake.nextIdStr()).setSite(site).setSfc(productSn).setTraceMethod("L").setComponenetBo(x.getItemBo())
                    .setQty(x.getQty()).setAssyTime(date).setAssyUser(userName);
            String[] split = x.getItemSn().split(",");
            if (split.length != 1) {
                for (int i = 0; i < split.length; i++) {
                    ret.add(temp.setAssembledSn(split[i]));
                }
            } else {
                ret.add(temp.setAssembledSn(x.getItemSn()));
            }
        });
        this.saveBatch(ret);
    }
}

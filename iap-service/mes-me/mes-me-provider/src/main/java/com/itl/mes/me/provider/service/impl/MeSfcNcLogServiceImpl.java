package com.itl.mes.me.provider.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.util.UUID;
import com.itl.mes.me.api.entity.MeSfc;
import com.itl.mes.me.api.entity.MeSfcNcLog;
import com.itl.mes.me.api.entity.MeSfcWipLog;
import com.itl.mes.me.api.service.MeSfcNcLogService;
import com.itl.mes.me.api.service.MeSfcService;
import com.itl.mes.me.api.service.MeSfcWipLogService;
import com.itl.mes.me.provider.mapper.MeSfcNcLogMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service("meSfcNcLogService")
public class MeSfcNcLogServiceImpl extends ServiceImpl<MeSfcNcLogMapper, MeSfcNcLog> implements MeSfcNcLogService {


    @Resource
    private MeSfcWipLogService meSfcWipLogService;
    @Resource
    private MeSfcService meSfcService;

    BigDecimal one = new BigDecimal(1);
    BigDecimal zero = new BigDecimal(0);

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void saveNc(MeSfc meSfc, String ncBo, BigDecimal doneQty, BigDecimal scrapQty) {

        MeSfcNcLog meSfcNcLog = new MeSfcNcLog();

        MeSfc meSfc1 = meSfcService.updateLast(meSfc.setState("已检查"));
        final List<MeSfc> list = meSfcService.list(new QueryWrapper<MeSfc>().lambda().select(MeSfc::getScrapQty).eq(MeSfc::getScheduleBo, meSfc1.getScheduleBo()).orderByDesc(MeSfc::getScrapQty));

        meSfcService.update(
                Optional.ofNullable(list.get(0)).map(x -> {
                    x.getScrapQty().add(one);
                    return x;
                }).orElse(new MeSfc().setScrapQty(one)),
                new QueryWrapper<MeSfc>().lambda().eq(MeSfc::getScheduleBo, meSfc1.getScheduleBo()));
        MeSfcWipLog ng = meSfcWipLogService.recordLog(meSfc1, null, "NG", null);

        BeanUtil.copyProperties(meSfc1, meSfcNcLog);
        meSfcNcLog.setComponentBo(meSfc1.getItemBo());
        meSfcNcLog.setRecordTime(new Date());
        meSfcNcLog.setWipLogBo(ng.getBo());
        String[] split = ncBo.split(";");
        for (String s : split) {
            if (StrUtil.isNotEmpty(s)) {
                meSfcNcLog.setBo(UUID.uuid32());
                meSfcNcLog.setNcCodeBo(s);
                save(meSfcNcLog);
            }
        }
    }

    @Override
    public void saveOk(MeSfc meSfc, BigDecimal doneQty, BigDecimal scrapQty) {
        MeSfc meSfc1 = meSfcService.updateLast(meSfc.setState("已检查"));
        final List<MeSfc> list = meSfcService.list(
                new QueryWrapper<MeSfc>().lambda().select(MeSfc::getDoneQty).eq(MeSfc::getScheduleBo, meSfc1.getScheduleBo()).orderByDesc(MeSfc::getDoneQty));

        meSfcService.update(Optional.ofNullable(list.get(0)).map(x->x.setDoneQty(x.getDoneQty().add(one))).orElse(new MeSfc().setDoneQty(one)),
                new QueryWrapper<MeSfc>().lambda().eq(MeSfc::getScheduleBo, meSfc1.getScheduleBo()));


        meSfcWipLogService.recordLog(meSfc1, null, "OK", null);
    }
}

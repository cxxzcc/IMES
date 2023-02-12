package com.itl.mes.me.provider.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.itl.mes.me.provider.mapper.RuleLabelDetailMapper;
import com.itl.mes.me.api.entity.RuleLabelDetail;
import com.itl.mes.me.api.service.RuleLabelDetailService;

import javax.annotation.Resource;


@Service
public class RuleLabelDetailServiceImpl extends ServiceImpl<RuleLabelDetailMapper, RuleLabelDetail> implements RuleLabelDetailService {
    @Resource
    private RuleLabelDetailMapper ruleLabelDetailMapper;
}

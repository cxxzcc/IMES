package com.itl.mom.label.provider.impl.ruleLabe;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.mom.label.api.entity.ruleLabel.RuleLabelDetail;
import com.itl.mom.label.api.service.ruleLabel.RuleLabelDetailService;
import com.itl.mom.label.provider.mapper.ruleLabel.RuleLabelDetailMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class RuleLabelDetailServiceImpl extends ServiceImpl<RuleLabelDetailMapper, RuleLabelDetail> implements RuleLabelDetailService {
    @Resource
    private RuleLabelDetailMapper ruleLabelDetailMapper;
}

package com.itl.plugins.report.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.plugins.report.entity.ReportContent;
import com.itl.plugins.report.mapper.ReportContentMapper;
import com.itl.plugins.report.service.ReportContentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zero
 * @date 2019-04-25
 **/
@Slf4j
@Service
public class ReportContentServiceImpl extends ServiceImpl<ReportContentMapper, ReportContent> implements ReportContentService {

    @Override
    public ReportContent findByFileName(String fileName) {
        QueryWrapper<ReportContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ReportContent::getFileName, fileName);
        return this.getOne(queryWrapper);
    }

}

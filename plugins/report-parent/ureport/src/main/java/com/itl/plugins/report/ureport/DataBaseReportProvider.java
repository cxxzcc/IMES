package com.itl.plugins.report.ureport;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bstek.ureport.exception.ReportException;
import com.bstek.ureport.provider.report.ReportFile;
import com.bstek.ureport.provider.report.ReportProvider;
import com.itl.iap.common.base.utils.Assert;
import com.itl.plugins.report.common.ReportConfig;
import com.itl.plugins.report.entity.ReportContent;
import com.itl.plugins.report.service.ReportContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据库报表存储器
 * - 所有的数据内容都存储到数据库，包括文件名及文件内容等
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class DataBaseReportProvider implements ReportProvider {

    private final ReportContentService reportContentService;


    @Override
    public InputStream loadReport(String fileName) {
        fileName = substring(fileName);
        log.info("加载报表文件内容 文件名称: {}", fileName);
        Assert.valid(StrUtil.isBlank(fileName), "文件名不能为空");
        QueryWrapper<ReportContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ReportContent::getCode, fileName);
        ReportContent reportContent = reportContentService.getOne(queryWrapper);
        try {
            return IOUtils.toInputStream(reportContent.getContent(), "UTF-8");
        } catch (IOException e) {
            throw new ReportException(e);
        }
    }

    @Override
    public void deleteReport(String fileName) {
        fileName = substring(fileName);
        QueryWrapper<ReportContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ReportContent::getCode, substring(fileName));
        reportContentService.remove(queryWrapper);
    }

    @Override
    public List<ReportFile> getReportFiles() {
        List<ReportContent> contents = reportContentService.list();
        log.info("报表文件数量: {}", contents.size());
        return contents.stream()
                .map(this::toReportFile)
                .sorted((f1, f2) -> f2.getUpdateDate().compareTo(f1.getUpdateDate()))
                .collect(Collectors.toList());
    }

    @Override
    public void saveReport(String fileName, String content) {
        fileName = substring(fileName);
        if(fileName.contains(".")){
            fileName = fileName.substring(0, fileName.indexOf("."));
        }
        ReportContent reportContent = new ReportContent();
        reportContent.setContent(content);
        reportContent.setPreviewUrl(ReportConfig.PREVIEW_BASIC_URL + ReportConfig.PREFIX + fileName);
        reportContent.setDesignUrl(ReportConfig.DESIGN_BASIC_URL + ReportConfig.PREFIX + fileName);
        UpdateWrapper<ReportContent> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(ReportContent::getCode, fileName);
        reportContentService.update(reportContent, updateWrapper);
        log.info("报表文件保存成功 文件名: {}", fileName);
    }

    @Override
    public boolean disabled() {
        return false;
    }

    @Override
    public String getPrefix() {
        return ReportConfig.PREFIX;
    }

    @Override
    public String getName() {
        return "数据库存储";
    }

    /**
     * 去掉前缀
     */
    private String substring(String fileName) {
        String prefix = getPrefix();
        if (fileName.startsWith(prefix)) {
            return fileName.substring(prefix.length());
        }
        return fileName;
    }


    private ReportFile toReportFile(ReportContent content) {
        return new ReportFile(content.getCode(), content.getUpdateTime());
    }
}

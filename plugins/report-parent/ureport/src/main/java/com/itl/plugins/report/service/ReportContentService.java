package com.itl.plugins.report.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.plugins.report.entity.ReportContent;

/**
 * @author zero
 * @date 2019-04-25
 **/
public interface ReportContentService extends IService<ReportContent> {


    ReportContent findByFileName(String fileName);

}

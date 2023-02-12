package com.itl.iap.attachment.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.attachment.api.entity.ExcelExportGeneral;
import com.itl.iap.common.base.exception.CommonException;

import javax.servlet.http.HttpServletResponse;

/**
 * description:
 * author: lK
 * time: 2021/7/6 10:08
 */
public interface ExcelGeneralService extends IService<ExcelExportGeneral> {
    void exportExcelTemplate(String id, HttpServletResponse response) throws CommonException;

    void exportExcel(String id, HttpServletResponse response) throws CommonException;

}

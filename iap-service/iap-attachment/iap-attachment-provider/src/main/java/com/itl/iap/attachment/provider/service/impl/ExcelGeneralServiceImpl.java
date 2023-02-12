package com.itl.iap.attachment.provider.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.attachment.api.entity.ExcelExportGeneral;
import com.itl.iap.attachment.api.entity.IapUploadFile;
import com.itl.iap.attachment.api.service.ExcelGeneralService;
import com.itl.iap.attachment.provider.config.FastDFSClient;
import com.itl.iap.attachment.provider.mapper.ExcelExportGeneralMapper;
import com.itl.iap.attachment.provider.mapper.IapUploadFileMapper;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.utils.DBUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;


/**
 * description:
 * author: lK
 * time: 2021/7/6 10:10
 */
@Service
public class ExcelGeneralServiceImpl extends ServiceImpl<ExcelExportGeneralMapper, ExcelExportGeneral> implements ExcelGeneralService {

    private ExcelExportGeneralMapper excelGeneralMapper;

    @Autowired
    public void setExcelGeneralMapper(ExcelExportGeneralMapper excelGeneralMapper) {
        this.excelGeneralMapper = excelGeneralMapper;
    }

    private IapUploadFileMapper iapUploadFileMapper;

    @Autowired
    public void setIapUploadFileMapper(IapUploadFileMapper iapUploadFileMapper) {
        this.iapUploadFileMapper = iapUploadFileMapper;
    }

    private DBUtils dbUtils;

    @Autowired
    public void setDbUtils(DBUtils dbUtils) {
        this.dbUtils = dbUtils;
    }

    private FastDFSClient fastDfsClient;

    @Autowired
    public void setFastDfsClient(FastDFSClient fastDfsClient) {
        this.fastDfsClient = fastDfsClient;
    }

    /**
     * 下载excel模板 工艺路线设置专用
     *
     * @param id       id
     * @param response 物质
     * @throws CommonException 异常
     */
    @Override
    public void exportExcelTemplate(String id, HttpServletResponse response) throws CommonException {
        ExcelExportGeneral dbExportGeneral = excelGeneralMapper.selectById(id);
        if (null == dbExportGeneral) {
            throw new RuntimeException(id+" 并无上传记录");
        }
        IapUploadFile iapUploadFile = iapUploadFileMapper.selectById(dbExportGeneral.getUploadFileId());
        if (null == iapUploadFile) {
            throw new CommonException("模板不存在", 500);
        }

        String fileName = iapUploadFile.getFileName();
        String fileType = iapUploadFile.getFileType();
        if (StrUtil.isEmpty(fileName) || StrUtil.isEmpty(fileType)) {
            throw new CommonException("文件名或文件类型不能为空", 500);
        }

        BufferedOutputStream out = null;
        try {
            response.reset();
            response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", URLEncoder.encode(iapUploadFile.getFileName() + iapUploadFile.getFileType(), "UTF-8")));
            response.setContentType("application/octet-stream");
            out = new BufferedOutputStream(response.getOutputStream());
            byte[] bytes = fastDfsClient.downloadFile(iapUploadFile.getFilePath()); // fastDFS提供
            out.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    // 缓冲流强制输出
                    out.flush();
                    out.close();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }


//------------------------------------------------------ 通用 -----------------------------------------------------------

    /**
     * @param id       模板sql表的id
     * @param response 返回
     * @throws CommonException 异常
     */
    @Override
    public void exportExcel(String id, HttpServletResponse response) throws CommonException {
        ExcelExportGeneral dbExportGeneral = excelGeneralMapper.selectById(id);
        IapUploadFile iapUploadFile = iapUploadFileMapper.selectById(dbExportGeneral.getUploadFileId());
        if (null == iapUploadFile) {
            throw new CommonException("模板不存在", 500);
        }

        List<Map<String, Object>> maps = dbUtils.excuteQuerySql(dbExportGeneral.getSql());
        this.exportByTemplate(iapUploadFile, maps, response);
    }

    /**
     * 根据模板磁盘地址导出excel
     */
    public void exportByTemplate(IapUploadFile iapUploadFile, List<Map<String, Object>> maps, HttpServletResponse response) throws CommonException {
        String fileName = iapUploadFile.getFileName();
        String fileType = iapUploadFile.getFileType();
        if (StrUtil.isEmpty(fileName) || StrUtil.isEmpty(fileType)) {
            throw new CommonException("文件名或文件类型不能为空", 500);
        }

        BufferedOutputStream out = null;
        InputStream input = null;
        try {
            response.reset();
            response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", URLEncoder.encode(iapUploadFile.getFileName() + iapUploadFile.getFileType(), "UTF-8")));
            response.setContentType("application/octet-stream");
            out = new BufferedOutputStream(response.getOutputStream());

            byte[] bytes = fastDfsClient.downloadFile(iapUploadFile.getFilePath()); // fastDFS提供
            input = new ByteArrayInputStream(bytes);

            // 构建书写者
            ExcelWriter excelWriter = EasyExcel.write(out).withTemplate(input).build(); // todo
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            // 直接写入Excel数据
            excelWriter.fill(maps, writeSheet);
            // 关闭流
            excelWriter.finish();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    // 缓冲流强制输出
                    out.flush();
                    out.close();
                }
                if (input != null) {
                    // ByteArrayInputStream类型其实不用关
                    input.close();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }


}

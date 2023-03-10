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
     * ??????excel?????? ????????????????????????
     *
     * @param id       id
     * @param response ??????
     * @throws CommonException ??????
     */
    @Override
    public void exportExcelTemplate(String id, HttpServletResponse response) throws CommonException {
        ExcelExportGeneral dbExportGeneral = excelGeneralMapper.selectById(id);
        if (null == dbExportGeneral) {
            throw new RuntimeException(id+" ??????????????????");
        }
        IapUploadFile iapUploadFile = iapUploadFileMapper.selectById(dbExportGeneral.getUploadFileId());
        if (null == iapUploadFile) {
            throw new CommonException("???????????????", 500);
        }

        String fileName = iapUploadFile.getFileName();
        String fileType = iapUploadFile.getFileType();
        if (StrUtil.isEmpty(fileName) || StrUtil.isEmpty(fileType)) {
            throw new CommonException("????????????????????????????????????", 500);
        }

        BufferedOutputStream out = null;
        try {
            response.reset();
            response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", URLEncoder.encode(iapUploadFile.getFileName() + iapUploadFile.getFileType(), "UTF-8")));
            response.setContentType("application/octet-stream");
            out = new BufferedOutputStream(response.getOutputStream());
            byte[] bytes = fastDfsClient.downloadFile(iapUploadFile.getFilePath()); // fastDFS??????
            out.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    // ?????????????????????
                    out.flush();
                    out.close();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }


//------------------------------------------------------ ?????? -----------------------------------------------------------

    /**
     * @param id       ??????sql??????id
     * @param response ??????
     * @throws CommonException ??????
     */
    @Override
    public void exportExcel(String id, HttpServletResponse response) throws CommonException {
        ExcelExportGeneral dbExportGeneral = excelGeneralMapper.selectById(id);
        IapUploadFile iapUploadFile = iapUploadFileMapper.selectById(dbExportGeneral.getUploadFileId());
        if (null == iapUploadFile) {
            throw new CommonException("???????????????", 500);
        }

        List<Map<String, Object>> maps = dbUtils.excuteQuerySql(dbExportGeneral.getSql());
        this.exportByTemplate(iapUploadFile, maps, response);
    }

    /**
     * ??????????????????????????????excel
     */
    public void exportByTemplate(IapUploadFile iapUploadFile, List<Map<String, Object>> maps, HttpServletResponse response) throws CommonException {
        String fileName = iapUploadFile.getFileName();
        String fileType = iapUploadFile.getFileType();
        if (StrUtil.isEmpty(fileName) || StrUtil.isEmpty(fileType)) {
            throw new CommonException("????????????????????????????????????", 500);
        }

        BufferedOutputStream out = null;
        InputStream input = null;
        try {
            response.reset();
            response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", URLEncoder.encode(iapUploadFile.getFileName() + iapUploadFile.getFileType(), "UTF-8")));
            response.setContentType("application/octet-stream");
            out = new BufferedOutputStream(response.getOutputStream());

            byte[] bytes = fastDfsClient.downloadFile(iapUploadFile.getFilePath()); // fastDFS??????
            input = new ByteArrayInputStream(bytes);

            // ???????????????
            ExcelWriter excelWriter = EasyExcel.write(out).withTemplate(input).build(); // todo
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            // ????????????Excel??????
            excelWriter.fill(maps, writeSheet);
            // ?????????
            excelWriter.finish();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    // ?????????????????????
                    out.flush();
                    out.close();
                }
                if (input != null) {
                    // ByteArrayInputStream?????????????????????
                    input.close();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }


}

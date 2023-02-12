package com.itl.iap.common.base.utils;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.itl.iap.common.base.dto.MesFilesVO;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.model.FastDFSFile;
import com.itl.iap.common.base.response.ResponseData;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 导入导出工具类
 */
public class ExcelUtils {

    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass,
                                   String fileName, boolean isCreateHeader, HttpServletResponse response) throws CommonException {
        ExportParams exportParams = new ExportParams(title, sheetName);
        exportParams.setCreateHeadRows(isCreateHeader);
        defaultExport(list, pojoClass, fileName, response, exportParams);
    }

    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass,String fileName,
                                   HttpServletResponse response) throws CommonException {
        defaultExport(list, pojoClass, fileName, response, new ExportParams(title, sheetName));
    }

    public static void exportExcel(List<Map<String, Object>> list, String fileName, HttpServletResponse response) throws CommonException {
        defaultExport(list, fileName, response);
    }

    private static void defaultExport(List<?> list, Class<?> pojoClass, String fileName,
                                      HttpServletResponse response, ExportParams exportParams) throws CommonException {
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams,pojoClass,list);
        if (workbook != null) downLoadExcel(fileName, response, workbook);
    }

    private static void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook) throws CommonException {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            throw new CommonException( e.getMessage(), CommonExceptionDefinition.EXCEI_EXCEPTION);
        }
    }

    private static void defaultExport(List<Map<String, Object>> list, String fileName, HttpServletResponse response) throws CommonException {
        Workbook workbook = ExcelExportUtil.exportExcel(list, ExcelType.HSSF);
        if (workbook != null) downLoadExcel(fileName, response, workbook);
    }

    public static <T> List<T> importExcel(String filePath,Integer titleRows,Integer headerRows, Class<T> pojoClass) throws CommonException {
        if (StrUtil.isBlank(filePath)){
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(new File(filePath), pojoClass, params);
        }catch (NoSuchElementException e){
            throw new CommonException( "模板不能为空", CommonExceptionDefinition.EXCEI_EXCEPTION );
        } catch (Exception e) {
            throw new CommonException( e.getCause().getMessage(), CommonExceptionDefinition.EXCEI_EXCEPTION  );
        }
        return list;
    }

    public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass) throws CommonException {
        if (file == null){ return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(file.getInputStream(), pojoClass, params);
        }catch (NoSuchElementException e){
            throw new CommonException( "excel文件不能为空", CommonExceptionDefinition.EXCEI_EXCEPTION );
        } catch ( Exception e ) {
            throw new CommonException( e.getCause().getMessage(), CommonExceptionDefinition.EXCEI_EXCEPTION );
        }
        return list;
    }

    public static <T> List<T> importExcel(MultipartFile file, Integer sheet, Integer titleRows, Integer headerRows, Class<T> pojoClass) throws CommonException {
        if (file == null){
            return null;
        }
        if ( sheet ==null ){
            sheet = 0;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        params.setStartSheetIndex( sheet );
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(file.getInputStream(), pojoClass, params);
        }catch (NoSuchElementException e){
            throw new CommonException( "excel文件不能为空", CommonExceptionDefinition.VERIFY_EXCEPTION );
        } catch ( Exception e ) {
            throw new CommonException( e.getCause().getMessage(), CommonExceptionDefinition.VERIFY_EXCEPTION );
        }
        return list;
    }


    /**
     * 导出带图片的excel,可以指定列高度
     * @param height 列高度 {@link ExportParams#}
     * */
    public static void exportExcelWithImg(List<?> list, String title, String sheetName, Class<?> pojoClass,
                                   String fileName, boolean isCreateHeader, Short height, HttpServletResponse response) throws CommonException {
        ExportParams exportParams = new ExportParams(title, sheetName);
        exportParams.setCreateHeadRows(isCreateHeader);
        exportParams.setHeight(height);
        defaultExport(list, pojoClass, fileName, response, exportParams);
    }


//    public static ExcelImportResult importExcel(InputStream ips, Class<T> pojoClass) {
//        ImportParams params = new ImportParams();
//        params.setNeedVerify(true);
//        try {
//            ExcelImportResult<T> objectExcelImportResult = ExcelImportUtil.importExcelMore(ips, pojoClass, params);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 导入， 校验失败的行会生成错误文件保存在指定路径
     * @param inputStream excel输入流
     * @param pojoclass excel行对应的dto类
     * @param params 导入参数设置，自定义校验器在此设置 {@link ImportParams}
     * @param failFilePath 失败文件存储基础路径（文件夹）   默认会在后面创建 /yyyy/MM/dd 文件夹
     * @param consumer 执行导入数据持久化的方法，参数类型为List
     * @return 导入信息 failCount=导入失败条数；failFilePath=导入失败文件下载路径；successCount=导入成功条数
     * */
    public static <T> Map<String, Object> importExcel(InputStream inputStream, Class<T> pojoclass, ImportParams params, String failFilePath, Consumer<List<T>> consumer, Function<FastDFSFile, ResponseData<MesFilesVO>> func) {
        FileOutputStream fileOutputStream = null;
        ExcelImportResult<T> result = null;
        Map<String, Object> map = new HashMap<>(16);
        try {
            result = ExcelImportUtil.importExcelMore(inputStream, pojoclass, params);
            //验证是否有失败的数据
            if (result.isVerfiyFail()) {
                //保存错误文件
                Workbook failWorkbook = result.getFailWorkbook();
                String str = DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN);
                String fileDirPath = failFilePath + str.split("-")[0] + "/" + str.split("-")[1] + "/" + str.split("-")[2] + "/";
                FileUtil.mkdir(fileDirPath);
                String fileName = UUID.randomUUID().toString();
                String filePath =  fileDirPath + fileName + ".xls";
                fileOutputStream = new FileOutputStream(FileUtil.file(filePath));
                failWorkbook.write(fileOutputStream);
                map.put("failCount", result.getFailList().size());
                map.put("failFilePath", filePath);


                if(func != null && StrUtil.isNotBlank(filePath)) {
                    FastDFSFile fastDFSFile = new FastDFSFile(fileName, FileUtil.readBytes(filePath), ".xls");
                    //同步文件
                    ResponseData<MesFilesVO> apply = func.apply(fastDFSFile);
                    if(apply.isSuccess()) {
                        MesFilesVO data = apply.getData();
                        map.put("failFilePath", data.getFilePath());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(result != null && CollUtil.isNotEmpty(result.getList())) {
            //保存数据
            consumer.accept(result.getList());
            map.put("successCount", result.getList().size());
        }
        return map;
    }


}

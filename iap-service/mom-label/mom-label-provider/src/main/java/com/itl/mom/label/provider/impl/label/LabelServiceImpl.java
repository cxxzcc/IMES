package com.itl.mom.label.provider.impl.label;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.dto.CustomDataValRequest;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import com.itl.mes.core.client.service.CustomDataValService;
import com.itl.mom.label.api.dto.label.LabelQueryDTO;
import com.itl.mom.label.api.dto.label.LabelSaveDto;
import com.itl.mom.label.api.entity.file.MesFiles;
import com.itl.mom.label.api.entity.label.LabelEntity;
import com.itl.mom.label.api.entity.label.LabelEntityParams;
import com.itl.mom.label.api.entity.print.Printer;
import com.itl.mom.label.api.service.label.LabelService;
import com.itl.mom.label.api.vo.LabelVo;
import com.itl.mom.label.provider.common.CommonCode;
import com.itl.mom.label.provider.config.Constant;
import com.itl.mom.label.provider.exception.CustomException;
import com.itl.mom.label.provider.mapper.file.MesFilesMapper;
import com.itl.mom.label.provider.mapper.label.LabelEntityParamsMapper;
import com.itl.mom.label.provider.mapper.label.LabelMapper;
import com.itl.mom.label.provider.report.JasperReport;
import com.itl.mom.label.provider.utils.BarCodeUtil;
import com.itl.mom.label.provider.utils.CommonUtils;
import com.itl.mom.label.provider.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.jfree.util.Log;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Connection;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;


/**
 * @author liuchenghao
 * @date 2020/11/3 17:59
 */
@Service
@Slf4j
public class LabelServiceImpl extends JasperReport implements LabelService {

    @Autowired
    LabelMapper labelMapper;

    @Autowired
    CustomDataValService customDataValService;


    @Autowired
    LabelEntityParamsMapper labelEntityParamsMapper;

    @Autowired
    MesFilesMapper mesFilesMapper;
    @Autowired
    private CommonUtils commonUtils;


    @Value("${file.path}")
    private String filePath;


    @Value("${html.path}")
    private String htmlPath;


    @Override
    public IPage<LabelEntity> findList(LabelQueryDTO labelQueryDTO) {

        if (ObjectUtil.isEmpty(labelQueryDTO.getPage())) {
            labelQueryDTO.setPage(new Page(0, 10));
        }

        QueryWrapper queryWrapper = new QueryWrapper<LabelEntity>();

        queryWrapper.eq("site", UserUtils.getSite());

        if (StrUtil.isNotEmpty(labelQueryDTO.getLabelTypeId())) {
            queryWrapper.eq("label_type_id", labelQueryDTO.getLabelTypeId());
        }
        if (StrUtil.isNotEmpty(labelQueryDTO.getLabel())) {
            queryWrapper.like("label", labelQueryDTO.getLabel());
        }
        labelQueryDTO.getPage().setDesc("CREATION_DATE");
        queryWrapper.orderByDesc("CREATION_DATE");
        return labelMapper.selectPage(labelQueryDTO.getPage(), queryWrapper);
    }

    @Override
    public IPage<LabelEntity> findListByState(LabelQueryDTO labelQueryDTO) {

        if (ObjectUtil.isEmpty(labelQueryDTO.getPage())) {
            labelQueryDTO.setPage(new Page(0, 10));
        }

        QueryWrapper queryWrapper = new QueryWrapper<LabelEntity>();
        queryWrapper.eq("site", UserUtils.getSite());
        if (StrUtil.isNotEmpty(labelQueryDTO.getLabelTypeId())) {
            queryWrapper.eq("label_type_id", labelQueryDTO.getLabelTypeId());
        }
        if (StrUtil.isNotEmpty(labelQueryDTO.getLabel())) {
            queryWrapper.like("label", labelQueryDTO.getLabel());
        }
        labelQueryDTO.getPage().setDesc("CREATION_DATE");
        queryWrapper.orderByDesc("CREATION_DATE");
        queryWrapper.eq("STATE", "1");
        return labelMapper.selectPage(labelQueryDTO.getPage(), queryWrapper);
    }


    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void save(LabelSaveDto labelSaveDto) {
        LambdaQueryWrapper<LabelEntity> query = new QueryWrapper<LabelEntity>()
                .lambda()
                .eq(LabelEntity::getSite, UserUtils.getSite())
                .eq(LabelEntity::getLabelTypeId, labelSaveDto.getLabelTypeId())
                .eq(LabelEntity::getLabel, labelSaveDto.getLabel());
        if (ObjectUtil.isNotEmpty(labelSaveDto.getId())) {
            query.ne(LabelEntity::getId, labelSaveDto.getId());
        }
        List<LabelEntity> list = labelMapper.selectList(query);
        if (list != null && list.size() > 0) {
            throw new CustomException(CommonCode.LABEL_REPEAT);
        }
        LabelEntity labelEntity = new LabelEntity();
        BeanUtils.copyProperties(labelSaveDto, labelEntity);
        labelEntity.setSite(UserUtils.getSite());
        if (StrUtil.isNotEmpty(labelSaveDto.getId())) {
            labelEntity.setLastUpdateDate(new Date());
            labelMapper.updateById(labelEntity);
        } else {
            labelEntity.setCreationDate(new Date());
            labelEntity.setId(UUID.randomUUID().toString().replace("-", ""));
            labelMapper.insert(labelEntity);
        }
        //保存自定义数据
        if (labelSaveDto.getCustomDataValVoList() != null) {
            CustomDataValRequest customDataValRequest = new CustomDataValRequest();
            customDataValRequest.setBo(labelEntity.getId());
            customDataValRequest.setSite(UserUtils.getSite());
            customDataValRequest.setCustomDataType(CustomDataTypeEnum.LABEL.getDataType());
            customDataValRequest.setCustomDataValVoList(labelSaveDto.getCustomDataValVoList());
            customDataValService.saveCustomDataVal(customDataValRequest);
        }
        switch (labelSaveDto.getTemplateType()) {
            case Constant.JASPER:
                saveOrUpdateFile(labelEntity);
                break;
            default:
                break;
        }

        saveOrUpdateParams(labelEntity);
    }

    private void saveOrUpdateParams(LabelEntity labelEntity) {

        labelEntityParamsMapper.delete(new QueryWrapper<LabelEntityParams>().eq("label_id", labelEntity.getId()));

        if (labelEntity.getLabelEntityParamsList() != null && !labelEntity.getLabelEntityParamsList().isEmpty()) {

            labelEntity.getLabelEntityParamsList().forEach(labelEntityParams -> {
                labelEntityParams.setLabelId(labelEntity.getId());
                labelEntityParamsMapper.insert(labelEntityParams);
            });

        }
    }


    private void saveOrUpdateFile(LabelEntity labelEntity) {
        MesFiles file = labelEntity.getJasperFile();
        if (file != null) {
            file.setGroupId(labelEntity.getId());

            if (labelEntity.getId() != null) {
                QueryWrapper<MesFiles> query = new QueryWrapper<MesFiles>().eq("groupId", labelEntity.getId());
                if (StringUtils.isNotBlank(file.getId())) {
                    query.ne("id", file.getId());
                }
                List<MesFiles> mesFiles = mesFilesMapper.selectList(query);
                mesFiles.forEach(mes -> {
                    mesFilesMapper.deleteById(mes.getId());
                });
            }

            if (StringUtils.isNotBlank(file.getId())) {
                mesFilesMapper.updateById(file);
            } else {
                mesFilesMapper.insert(file);
            }
        }
    }


    @Override
    @Transactional
    public void delete(List<String> ids) {
        ids.forEach(id -> {
            labelMapper.deleteById(id);
        });
    }


    @Override
    public LabelVo findById(String id) {
        LabelEntity labelEntity = labelMapper.selectById(id);

        List<MesFiles> mesFiles = mesFilesMapper.selectList(new QueryWrapper<MesFiles>().eq("groupId", labelEntity.getId()));
        if (mesFiles != null && !mesFiles.isEmpty()) {
            MesFiles mesFile = mesFiles.get(0);
            mesFile.setFileUrl(mesFile.getFilePath().replace(filePath, commonUtils.getFtpUrl()));
            labelEntity.setJasperFile(mesFile);
        }

        List<LabelEntityParams> labelEntityParamsList = labelEntityParamsMapper.selectList(new QueryWrapper<LabelEntityParams>().eq("label_id", id));
        labelEntity.setLabelEntityParamsList(labelEntityParamsList);
        LabelVo labelVo = new LabelVo();
        BeanUtils.copyProperties(labelEntity, labelVo);
        List<CustomDataAndValVo> customDataAndValVoList = customDataValService
                .selectCustomDataAndValByBoAndDataType(UserUtils.getSite(), id, CustomDataTypeEnum.LABEL.getDataType());
        labelVo.setCustomDataAndValVoList(customDataAndValVoList);
        return labelVo;
    }

    public JasperPrint getJasper(Map<String, Object> params) {
        LabelEntity labelEntity = labelMapper.selectById(params.get("id").toString());
        createSqlParams(labelEntity, params);
        if (labelEntity != null) {
            MesFiles jasperFile = mesFilesMapper.selectOne(new QueryWrapper<MesFiles>().eq("groupId", labelEntity.getId()));
            try {
//                FileInputStream fis = new FileInputStream(new File("E:/home/sftp2021/57/03/d46e2b53-ea69-4c42-812b-3cf7ebbd1a3e/.jasper"));
                FileInputStream fis = new FileInputStream(jasperFile.getFilePath());
                log.info("预览标签路径信息:"+jasperFile.getFilePath());
                // FileInputStream fis = new FileInputStream(new File("D:mxy1.jasper"));
                JasperPrint jasperPrint = null;
                Integer useDataSource = labelEntity.getUseDataSource();
                if (useDataSource.equals(Constant.IsYesEnum.YES)) {
                    Connection connection = getConnection();
                    jasperPrint = JasperFillManager.fillReport(fis, params, connection);
                } else {
                    jasperPrint = JasperFillManager.fillReport(fis, params,
                            params.containsKey(Constant.LIST_DATA_SOURCE) ? new JRBeanCollectionDataSource((List<Object>) params.get(Constant.LIST_DATA_SOURCE)) : new JREmptyDataSource());
                }
                return jasperPrint;
            } catch (Exception e) {
                log.info("预览错误信息:"+e.getMessage());
                throw new RuntimeException(e.getMessage());
                //  throw new CustomException(CommonCode.JASPER_FAIL);
            }
        }
        return null;
    }

    private void convertImg(Map<String, Object> params) {
        for (String key : params.keySet()) {
            if (params.get(key) != null && (params.get(key).toString().contains("png") || params.get(key).toString().contains("jpg"))) {
                String imgPath = params.get(key).toString();
                try {
                    InputStream inputStream = new FileInputStream(new File(imgPath));
                    params.put(key, inputStream);
                } catch (FileNotFoundException e) {
                    throw new CustomException(CommonCode.IO_FAIL);
                }
            }
        }
    }

    private void createSqlParams(LabelEntity labelEntity, Map<String, Object> params) {
        List<LabelEntityParams> labelEntityParamsList = labelEntityParamsMapper.selectList(new QueryWrapper<LabelEntityParams>().eq("label_id", labelEntity.getId()));
        StringBuffer sb = new StringBuffer();
        labelEntityParamsList.forEach(labelEntityParams -> {
            if ((labelEntityParams.getIsFile().equals(Constant.BAR_CODE) || labelEntityParams.getIsFile().equals(Constant.QR_CODE)) && labelEntityParams.getCode() != null) {
                if (params.get(labelEntityParams.getCode().toString()) != null && StringUtils.isNotBlank(params.get(labelEntityParams.getCode().toString()).toString())) {
                    String uuid = UUID.randomUUID().toString();
                    if (labelEntityParams.getIsFile().equals(Constant.BAR_CODE))
                        BarCodeUtil.getBarCode(params.get(labelEntityParams.getCode().toString()).toString(), filePath + uuid + ".jpg");
                    if (labelEntityParams.getIsFile().equals(Constant.QR_CODE))
                        BarCodeUtil.generateQRCodeImage(params.get(labelEntityParams.getCode().toString()).toString(), 350, 350, filePath + uuid + ".jpg");
                    try {
                        params.put(labelEntityParams.getCode().toString(), new FileInputStream(new File(filePath + uuid + ".jpg")));
                    } catch (FileNotFoundException e) {
                        throw new CustomException(CommonCode.IO_FAIL);
                    }
                }
            }

            if (labelEntityParams.getIsFile().equals(Constant.QUERY_PARAM) && params.get(labelEntityParams.getCode()) != null && StringUtils.isNotBlank(params.get(labelEntityParams.getCode()).toString())) {
                sb.append(" and ").append(labelEntityParams.getCode()).append(" like '%").append(params.get(labelEntityParams.getCode()).toString()).append("%' ");
            }
        });
        //处理子数据源
        if (params.containsKey(Constant.SUB_LIST_DATA_SOURCE)) {
            params.put(Constant.SUB_LIST_DATA_SOURCE, new JRBeanCollectionDataSource((List<Object>) params.get(Constant.SUB_LIST_DATA_SOURCE)));
        }
        if (StringUtils.isNotBlank(sb.toString())) {
            params.put("params", sb.toString());
        }

    }

    private Map<String, Object> fieldToArrays(Map<String, Object> params) {

        List<LabelEntityParams> labelEntityParamsList = labelEntityParamsMapper.selectList(new QueryWrapper<LabelEntityParams>().eq("label_id", params.get("id").toString()));
        Map<String, Object> listDataMap = new HashMap<>(16);
        Map<String, Object> subListDataMap = new HashMap<>(16);
        labelEntityParamsList.stream().filter(labelEntityParams -> params.containsKey(labelEntityParams.getCode())).forEach(labelEntityParams -> {
            String value = BeanUtil.isEmpty(params.get(labelEntityParams.getCode())) ? "" : (String) params.get(labelEntityParams.getCode());
            if ((labelEntityParams.getIsFile().equals(Constant.ARRAY_CODE))
                    && labelEntityParams.getCode() != null) {
                listDataMap.put(labelEntityParams.getCode(), value);
                params.remove(labelEntityParams.getCode());
            } else if (labelEntityParams.getIsFile().equals(Constant.SUB_DATA_SOURCE_CODE)
                    && labelEntityParams.getCode() != null) {
                subListDataMap.put(labelEntityParams.getCode(), value);
                params.remove(labelEntityParams.getCode());
            }

        });
        if (CollectionUtil.isNotEmpty(listDataMap)) {
            List<Map<String, Object>> listSource = new ArrayList<>();
            listSource.add(listDataMap);
            params.put(Constant.LIST_DATA_SOURCE, listSource);
        }
        if (CollectionUtil.isNotEmpty(subListDataMap)) {
            List<Map<String, Object>> subListSource = new ArrayList<>();
            subListSource.add(subListDataMap);
            params.put(Constant.SUB_LIST_DATA_SOURCE, subListSource);
        }
        return params;

    }


    @Override
    public String preview(Map<String, Object> params, HttpServletResponse response) {
        try {
            String fileType = params.get("fileType").toString();
            String uuid = UUID.randomUUID().toString();
            ServletOutputStream os = response.getOutputStream();
            //处理json列表转换
            params = fieldToArrays(params);
            JasperPrint jasperPrint = getJasper(params);
            if (fileType.contains("pdf")) {
                JasperExportManager.exportReportToPdfStream(jasperPrint, os);
            } else if (fileType.contains("html")) {
                JasperExportManager.exportReportToHtmlFile(jasperPrint, htmlPath + uuid + ".html");
                Log.info("预览id:"+uuid + ".html");
                return uuid + ".html";
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }


    @Override
    public String exportFile(Map params, HttpServletResponse response) {
        String fileType = params.get("fileType").toString();
        JasperPrint jasperPrint = getJasper(params);
        String uuid = UUID.randomUUID().toString();
        try {
            if (fileType.contains("pdf")) {
                byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
                FileUtils.writeFile(filePath + uuid + ".pdf", bytes);
                downFile(response, filePath + uuid + ".pdf");
            } else if (fileType.contains("html")) {
                JasperExportManager.exportReportToHtmlFile(jasperPrint, filePath + uuid + ".html");
                return filePath + uuid + ".html";
            }
        } catch (Exception e) {
            throw new CustomException(CommonCode.JASPER_FAIL);
        }
        return null;
    }

    public void downFile(HttpServletResponse resp, String path) {
        resp.setContentType("text/html");
        try {
            File file = new File(path);
            FileInputStream in = new FileInputStream(file);
            resp.setContentType("application/octet-stream");
            resp.addHeader("Content-Disposition", "attachment;filename=" + file.getName());
            OutputStream out = resp.getOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = in.read(b)) != -1) {
                out.write(b, 0, n);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            throw new CustomException(CommonCode.FILE_DOWN_FAIL);
        }

    }


    @Override
    public void batchPrint(List<Map<String, Object>> list, String labelId) {

        List<String> addressList = batchCreatePdf(list, labelId);

        try {
            Printer instance = Printer.getInstance();
            addressList.forEach(address -> {
                instance.defaultPrintPDF(address, "");
            });

        } catch (Exception e) {
            throw new CustomException(CommonCode.PRINT_FAIL);
        }
    }

    @Override
    public List<String> batchCreatePdf(List<Map<String, Object>> list, String labelId) {
        LabelEntity labelEntity = labelMapper.selectById(labelId);

        MesFiles jasperFile = mesFilesMapper.selectOne(new QueryWrapper<MesFiles>().eq("groupId", labelEntity.getId()));

        labelEntity.setJasperFile(jasperFile);

        List<LabelEntityParams> labelEntityParamsList = labelEntityParamsMapper.selectList(new QueryWrapper<LabelEntityParams>().eq("label_id", labelId));

        int n = list.size();
        //解析list 将list数据拆分
        List<List<Map<String, Object>>> result = averageAssign(list, n);

        try {
            //将数据交给多个线程处理
            List<String> pdfFile=marginPdf(result, n, labelEntityParamsList, labelEntity);
            log.info("回返路径5："+pdfFile.get(0));
            return pdfFile;

        } catch (Exception e) {
            log.error("batchCreatePdf = {}", e);
            e.printStackTrace();
            throw new CustomException(CommonCode.PRINT_FAIL);
        }
    }


    public List<String> marginPdf(List<List<Map<String, Object>>> result, int nThreads, List<LabelEntityParams> labelEntityParamsList, LabelEntity labelEntity) throws Exception {
        if (result == null || result.isEmpty()) {
            return null;
        }
        int cole = Runtime.getRuntime().availableProcessors();
        if (nThreads == 1) {//代表单次打印
            cole = 1;
        }

        ExecutorService executorService = new ThreadPoolExecutor(cole + 1, cole * 2, 2, TimeUnit.MINUTES, new LinkedBlockingQueue<>(5000), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());


        List<Future<String>> futures = new ArrayList<>();


        for (int i = 0; i < nThreads; i++) {

            List<Map<String, Object>> subList = result.get(i);

            Callable<String> task = new Callable<String>() {

                @Override
                public String call() throws Exception {

                    String anyPdfAddress = createPdfByJapser(subList, labelEntityParamsList, labelEntity);
                    log.info("回返路径3："+anyPdfAddress);
                    //将多个pdf合并成一个pdf
//                    return marginAnyPdf(anyPdfAddress);
                    return anyPdfAddress;
                }
            };
            futures.add(executorService.submit(task));
        }

        List<String> pathAddressList = new ArrayList<>();

        for (Future<String> future : futures) {
            log.info("回返路径4："+future.get());
            pathAddressList.add(future.get());
        }
        executorService.shutdown();

        // 等待子线程结束，再继续执行下面的代码
        executorService.awaitTermination(180, TimeUnit.SECONDS);

        return pathAddressList;

    }


    private String marginAnyPdf(List<String> anyPdfAddress) {
        PDFMergerUtility mergePdf = new PDFMergerUtility();

        anyPdfAddress.forEach(address -> {
            try {
                mergePdf.addSource(address);
                mergePdf.setDestinationFileName(address);
            } catch (FileNotFoundException e) {
                throw new CustomException(CommonCode.PRINT_FAIL);
            }
        });
//        String uuid = UUID.randomUUID().toString();
//        mergePdf.setDestinationFileName(filePath+uuid+".pdf");
        try {
            mergePdf.mergeDocuments();
        } catch (IOException e) {
            throw new CustomException(CommonCode.PRINT_FAIL);
        }
        return "";
    }

    private String createPdfByJapser(List<Map<String, Object>> subList, List<LabelEntityParams> labelEntityParamsList, LabelEntity labelEntity) {

        List<String> collect = labelEntityParamsList.stream().map(LabelEntityParams::getCode).collect(Collectors.toList());
        collect.add(Constant.LIST_DATA_SOURCE);
        collect.add(Constant.SUB_LIST_DATA_SOURCE);
        String address = "";
        for (Map<String, Object> map : subList) {
            Map<String, Object> result = new HashMap<>();
            Set<String> keySet = map.keySet();
            keySet.forEach(key -> {
                if (collect.contains(key)) {
                    result.put(key, map.get(key));
                }
            });

            //进行生成pdf文件
            String singlePdfAddress = createSinglePdf(result, labelEntity,
                    Optional.ofNullable(map.get("SN")).map(Object::toString).orElseGet(IdWorker::getIdStr),
                    Optional.ofNullable(map.get("fileDate")).map(Object::toString).orElse(null));
            log.info("回返路径2："+singlePdfAddress);
            if (StringUtils.isNotBlank(singlePdfAddress)) {
                address = singlePdfAddress;
            }
        }

        return address;
    }


    private String createSinglePdf(Map<String, Object> params, LabelEntity labelEntity, String pdfName, String fileDate) {
        if (labelEntity != null) {
            MesFiles jasperFile = labelEntity.getJasperFile();
            try {
                String fileDir = filePath;
                //避免在linux目录pdf无法分类
                String fileSavePath = fileDate == null ? fileDir + File.separatorChar + pdfName + ".pdf" : fileDir + File.separatorChar + fileDate + File.separatorChar + pdfName + ".pdf";
                File dateFile = new File(fileDir + File.separatorChar + fileDate);//判断当前日期路径是否存在
                if (dateFile.exists() == false) {
                    dateFile.mkdirs();
                }
//                String fileSavePath =  fileDir + File.separatorChar+ pdfName+".pdf";
                log.info("pad文件路径；" + fileSavePath);
                // 如果已存在, 直接返回
                if (new File(fileSavePath).exists()) {
                    return fileSavePath;
                }
                FileInputStream fis = new FileInputStream(new File(jasperFile.getFilePath()));
                createSqlParams(labelEntity, params);
                // convertImg(params);
                JasperPrint jasperPrint = null;
                Integer useDataSource = labelEntity.getUseDataSource();
                if (useDataSource.equals(Constant.IsYesEnum.YES)) {
                    Connection connection = getConnection();
                    jasperPrint = JasperFillManager.fillReport(fis, params, connection);
                } else {
                    jasperPrint = JasperFillManager.fillReport(fis, params,
                            params.containsKey(Constant.LIST_DATA_SOURCE) ? new JRBeanCollectionDataSource((List<Object>) params.get(Constant.LIST_DATA_SOURCE)) : new JREmptyDataSource());
                }
                FileUtil.mkdir(fileDir);
                JasperExportManager.exportReportToPdfFile(jasperPrint, fileSavePath);
                log.info("回返路径1："+fileSavePath);
                return fileSavePath;
            } catch (Exception e) {
                e.printStackTrace();
                log.info("生成pdf文件的异常:{}" + e.getMessage());
                throw new CustomException(CommonCode.JASPER_FAIL);
            }
        }
        return null;
    }


    /**
     * 将一组数据平均分成n组
     */
    public <T> List<List<T>> averageAssign(List<T> source, int n) {
        List<List<T>> result = new ArrayList<List<T>>();
        int remainder = source.size() % n;  //(先计算出余数)
        int number = source.size() / n;  //然后是商
        int offset = 0;//偏移量
        for (int i = 0; i < n; i++) {
            List<T> value = null;
            if (remainder > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remainder--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }


}

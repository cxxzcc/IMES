package com.itl.iap.mes.client.service.impl;

import com.itl.iap.common.base.dto.MesFilesVO;
import com.itl.iap.common.base.model.FastDFSFile;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.mes.api.entity.MesFiles;
import com.itl.iap.mes.client.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yx
 * @date 2021/8/24
 * @since 1.8
 */
@Slf4j
@Component
public class FileUploadServiceImpl implements FileUploadService {

    @Override
    public ResponseData saveFilesForFeign(List<MesFilesVO> list, String groupId) {
        log.error("sorry FileUploadService saveFilesForFeign feign fallback list:{} groupId:{}", list, groupId);
        return ResponseData.error("500", "sorry FileUploadService saveFilesForFeign feign fallback");
    }

    @Override
    public ResponseData<List<MesFiles>> getFileInfo(String groupId) {
        log.error("sorry FileUploadService getFileInfo feign fallback groupId:{}", groupId);
        return ResponseData.error("500", "sorry FileUploadService getFileInfo feign fallback");
    }

    @Override
    public ResponseData<MesFilesVO> uploadSingle(FastDFSFile fastDFSFile) {
        log.error("sorry FileUploadService uploadSingle feign fallback groupId:{}");
        return ResponseData.error("500", "sorry FileUploadService uploadSingle feign fallback");
    }
}

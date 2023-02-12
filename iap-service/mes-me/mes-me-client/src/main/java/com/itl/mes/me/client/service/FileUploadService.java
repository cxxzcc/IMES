package com.itl.mes.me.client.service;

import com.itl.iap.common.base.dto.MesFilesVO;
import com.itl.iap.common.base.model.FastDFSFile;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.mes.api.entity.MesFiles;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author yx
 * @date 2021/8/24
 * @since 1.8
 */
@FeignClient(value = "iap-mes-provider",contextId = "fileUploadService")
public interface FileUploadService {

    @PostMapping("/m/file/saveFilesForFeign/{groupId}")
    ResponseData saveFilesForFeign(@RequestBody List<MesFilesVO> list, @PathVariable String groupId);

    @GetMapping("/m/file/getFileInfo/{groupId}")
    ResponseData<List<MesFiles>> getFileInfo(@PathVariable("groupId") String groupId);


    @PostMapping(value = "/m/file/singleUpload")
    ResponseData<MesFilesVO> uploadSingle(@RequestBody FastDFSFile fastDFSFile);
}

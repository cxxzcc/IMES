package com.itl.iap.mes.provider.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.common.base.controller.BaseController;
import com.itl.iap.common.base.dto.MesFilesVO;
import com.itl.iap.common.base.model.FastDFSFile;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.mes.api.entity.MesFiles;
import com.itl.iap.mes.provider.service.impl.FileUploadServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/m/file")
public class FileUploadController extends BaseController {

    @Autowired
    private FileUploadServiceImpl fileUploadService;

    @PostMapping("uploadToServer")
    @ApiOperation(value = "上传文件", notes = "上传文件")
    public ResponseData upload(@RequestParam("file") List<MultipartFile> file) {
        return ResponseData.success(fileUploadService.savefile(file));
    }

    @PostMapping("/singleUpload")
    @ApiOperation(value = "上传单个文件", notes = "上传单个文件")
    public ResponseData<MesFilesVO> uploadSingle(@RequestBody FastDFSFile fastDFSFile) {
        return ResponseData.success(fileUploadService.savefile(fastDFSFile));
    }

    @GetMapping("delFile/{id}")
    @ApiOperation(value = "删除文件", notes = "删除文件")
    public ResponseData delFile(@PathVariable("id") String id) {
        fileUploadService.delFile(id);
        return ResponseData.success(true);
    }

    @GetMapping("downloadFile/{id}")
    @ApiOperation(value = "下载文件", notes = "下载文件")
    public ResponseData download(HttpServletResponse response, @PathVariable("id") String id) {
        fileUploadService.downLoad(response, id);
        return ResponseData.success(true);
    }

    @GetMapping("/fileReview")
    public String fileReview(HttpServletResponse response,
                             @RequestParam("filePath") String filePath) {

        try {
            FileInputStream file = null;
            OutputStream out = null;
            filePath = URLDecoder.decode(filePath, "UTF-8");
            file = new FileInputStream(filePath);
            int sizi = file.available();
            byte[] data = new byte[sizi];
            file.read(data);
            file.close();
            file = null;
            String suffix = filePath.substring(filePath.lastIndexOf(".") + 1);
            response.setContentType("video/" + suffix);
            out = response.getOutputStream();
            out.write(data);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success";
    }

    @GetMapping("/getFileInfo/{groupId}")
    public ResponseData<List<MesFiles>> getFileInfo(@PathVariable("groupId") String groupId) {
        try {
            Objects.requireNonNull(groupId, "对象id为空!");
            return ResponseData.success(fileUploadService.lambdaQuery().eq(MesFiles::getGroupId, groupId).list());
        } catch (Exception e) {
            return ResponseData.error("500", e.getMessage());
        }
    }

    @PostMapping("/saveFilesForFeign/{groupId}")
    public ResponseData saveFilesForFeign(@RequestBody List<MesFilesVO> filesVOS, @PathVariable String groupId) {
        LambdaQueryWrapper<MesFiles> query = new QueryWrapper<MesFiles>().lambda().eq(MesFiles::getGroupId, groupId);
        List<MesFiles> list = new ArrayList<>();
        for (MesFilesVO item : filesVOS) {
            MesFiles mesFiles = new MesFiles();
            BeanUtils.copyProperties(item, mesFiles);
            list.add(mesFiles);
        }
        if (list != null && !list.isEmpty()) {
            list.forEach(file -> {
                query.ne(MesFiles::getId, file.getId());
                file.setGroupId(groupId);
            });
            fileUploadService.updateBatchById(list);
        }
        List<MesFiles> mesFiles = fileUploadService.list(query);
        if (mesFiles != null && mesFiles.size() > 0) {
            fileUploadService.removeByIds(mesFiles.stream().map(MesFiles::getId).collect(Collectors.toList()));
        }
        return ResponseData.success();
    }

    @PostMapping("/saveFilesForFeign1/{groupId}")
    public ResponseData saveFilesForFeign1(@RequestBody List<MesFilesVO> filesVOS, @PathVariable String groupId) {
       try{
           return fileUploadService.saveFilesForFeign1(filesVOS,groupId);
       }catch (Exception e){
           return ResponseData.error(e.getMessage());

       }
    }

    @GetMapping("/getFileIn1fo/{groupId}")
    public ResponseData<List<MesFiles>> getFileInfo1(@PathVariable("groupId") String groupId) {
        try {
            Objects.requireNonNull(groupId, "对象id为空!");
            return fileUploadService.getFileList(groupId);

        } catch (Exception e) {
            return ResponseData.error("500", e.getMessage());
        }
    }
}

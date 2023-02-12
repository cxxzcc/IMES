package com.itl.iap.common.base.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * filesvo {@link MesFiles}
 * @author dengou
 * @date 2021/9/30
 */
@Data
public class MesFilesVO implements Serializable {

    private String id;
    private String name;
    private Long fileSize;
    private String filePath;
    private String groupId;
    private String fileType;
}

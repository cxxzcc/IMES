package com.itl.iap.mes.api.dto;

import lombok.Data;

import java.util.List;

/**
 * @author yx
 * @date 2021/8/19
 * @since 1.8
 */
@Data
public class ErrorTypeDto {

    private String id;
    private String site;
    private String errorCode;
    private String errorName;
    private String errorDesc;
    private String parentId;

    private List<ErrorTypeDto> children;
}

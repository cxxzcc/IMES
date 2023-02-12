package com.itl.iap.system.api.dto;

import lombok.Data;

/**
 * @author yx
 * @date 2021/8/20
 * @since 1.8
 */
@Data
public class ChangePwdDto {
    private String id;
    private String oldPsw;
    private String userName;
    private String userPsw;
}

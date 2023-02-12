package com.itl.mes.pp.provider.exception;


import com.itl.iap.common.base.response.ResultCode;

public class ExceptionCast {

    public static void cast(ResultCode resultCode){
        throw new CustomException(resultCode);
    }
}

package com.itl.mes.pp.provider.exception;


import com.itl.iap.common.base.response.ResultCode;

public class CustomException extends RuntimeException {

    //错误代码
    ResultCode resultCode;

    public CustomException(ResultCode resultCode){
        this.resultCode = resultCode;
    }
    public ResultCode getResultCode(){
        return resultCode;
    }


}

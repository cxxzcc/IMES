package com.itl.mom.label.provider.common;

import com.itl.iap.common.base.response.ResultCode;

public class Result extends ResponseResult {
    public Result(ResultCode resultCode, Object obj) {
        super(resultCode);
        this.obj = obj;
    }
    Object obj;

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}

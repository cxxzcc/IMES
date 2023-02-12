package com.itl.iap.mes.provider.exception;

import com.google.common.collect.ImmutableMap;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.response.ResultCode;
import com.itl.iap.mes.provider.common.CommonCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Order(1)
@ControllerAdvice//控制器增强
public class ExceptionCatch {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionCatch.class);
    //定义map，配置异常类型所对应的错误代码
    private static ImmutableMap<Class<? extends Throwable>, ResultCode> EXCEPTIONS;
    //定义map的builder对象，去构建ImmutableMap
    protected static ImmutableMap.Builder<Class<? extends Throwable>, ResultCode> builder = ImmutableMap.builder();


    /**
     * 对验证约束异常进行拦截，返回约定的响应体
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseData bindExceptionHandler(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        StringBuilder stringBuilder = new StringBuilder();
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            if (errors != null) {
                errors.forEach(p -> {
                    FieldError fieldError = (FieldError) p;
                    stringBuilder.append(fieldError.getField() + ":" + fieldError.getDefaultMessage()).append(" ");
                });
            }
        }
        return ResponseData.error(CommonExceptionDefinition.VERIFY_EXCEPTION + "", stringBuilder.toString());
    }

    @ExceptionHandler(CommonException.class)
    @ResponseBody
    public ResponseData commonException(CommonException commonException) {
        //记录日志
        LOGGER.error("捕获 CommonException", commonException);
        return ResponseData.error(commonException.getCode() + "", commonException.getMessage());
    }

    //捕获CustomException此类异常
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseData customException(CustomException customException) {
        //记录日志
        LOGGER.error("捕获 CustomException", customException.getMessage());
        ResultCode resultCode = customException.getResultCode();
        return ResponseData.error(resultCode);
    }

    //捕获IllegalStateException此类异常
    @ExceptionHandler(IllegalStateException.class)
    @ResponseBody
    public ResponseData illegalStateException(IllegalStateException illegalStateException) {
        //记录日志
        LOGGER.error("捕获 IllegalStateException", illegalStateException);
        return ResponseData.error(illegalStateException.getMessage());
    }

    //捕获HttpRequestMethodNotSupportedException此类异常
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ResponseData httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        //记录日志
        LOGGER.error("捕获 HttpRequestMethodNotSupportedException", exception);
        return ResponseData.error("不支持该请求方法");
    }

    static {
        //定义异常类型所对应的错误代码
        builder.put(HttpMessageNotReadableException.class, CommonCode.INVALID_PARAM);
        //builder.put(MultipartException.class,CommonCode.FILE_FAIL);
    }
}

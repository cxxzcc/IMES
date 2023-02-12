package com.itl.mom.label.provider.exception;

import com.google.common.collect.ImmutableMap;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.response.ResultCode;
import com.itl.iap.common.base.response.ResultResponseEnum;
import com.itl.mom.label.provider.common.CommonCode;
import com.itl.mom.label.provider.common.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@ControllerAdvice//控制器增强
@Order(1)
public class ExceptionCatch {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionCatch.class);
    //定义map，配置异常类型所对应的错误代码
    private static ImmutableMap<Class<? extends Throwable>, ResultCode> EXCEPTIONS;
    //定义map的builder对象，去构建ImmutableMap
    protected static ImmutableMap.Builder<Class<? extends Throwable>,ResultCode> builder = ImmutableMap.builder();

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

    //捕获CustomException此类异常
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult customException(CustomException customException){
        //记录日志
        LOGGER.error("捕获 customException", customException);
        ResultCode resultCode = customException.getResultCode();
        return new ResponseResult(resultCode);
    }

    @ExceptionHandler(CommonException.class)
    @ResponseBody
    public ResponseResult commonException(CommonException commonException){
        //记录日志
        LOGGER.error("捕获 CommonException", commonException);
        return new ResponseResult(false, commonException.getCode(), commonException.getMessage());
    }

    /**
     * 默认异常处理方法，代码有很多直接抛runtimeException  需要返回message给前端
     *
     * @param ex
     * @return ResponseData
     */
    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public ResponseData defaultErrorHandler(RuntimeException ex) {
        LOGGER.error("捕获RuntimeException", ex);
        return ResponseData.error(ResultResponseEnum.DEFAULT_SERVICE_RUNTIME_EXCEPTION.getCode(), ex.getMessage());
    }

    //捕获Exception此类异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exception(Exception exception){
        //记录日志
        LOGGER.error("捕获 exception:{}", exception);
        exception.printStackTrace();
        if(EXCEPTIONS == null){
            EXCEPTIONS = builder.build();//EXCEPTIONS构建成功
        }
        //从EXCEPTIONS中找异常类型所对应的错误代码，如果找到了将错误代码响应给用户，如果找不到给用户响应99999异常
        ResultCode resultCode = EXCEPTIONS.get(exception.getClass());
        if(resultCode !=null){
            return new ResponseResult(resultCode);
        }else{
            //返回99999异常
            return new ResponseResult(CommonCode.SERVER_ERROR);
        }
    }

    static {
        //定义异常类型所对应的错误代码
        builder.put(HttpMessageNotReadableException.class, CommonCode.INVALID_PARAM);
        //builder.put(MultipartException.class,CommonCode.FILE_FAIL);
    }
}

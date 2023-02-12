package com.itl.iap.common.base.exception.handler;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.response.ResultResponseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * 全局异常处理类（ handler ）
 *
 * @author 胡广
 * @name GlobalExceptionHandler
 * @date 2019-07-1616:07
 * @since jdk1.8
 */
@Slf4j
@ControllerAdvice
@ResponseBody
@Order(999)
public class GlobalExceptionHandler {

    /**
     * 无参构造函数
     */
    public GlobalExceptionHandler() {
        log.info("Initialized GlobalExceptionHandler");
    }

    /**
     * 对验证约束异常进行拦截，返回约定的响应体
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseData bindExceptionHandler(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        StringBuilder stringBuilder = new StringBuilder();
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            if (errors != null) {
                errors.forEach(p -> {
                    FieldError fieldError = (FieldError) p;
                    log.warn("Bad Request Parameters: dto entity [{}],field [{}],message [{}]", fieldError.getObjectName(), fieldError.getField(), fieldError.getDefaultMessage());
                    stringBuilder.append(fieldError.getField() + ":" + fieldError.getDefaultMessage()).append(" ");
                });
            }
        }
        return ResponseData.error(CommonExceptionDefinition.VERIFY_EXCEPTION + "", stringBuilder.toString());
    }


    /**
     * 参数类型转换错误
     */
    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseData parameterTypeException(HttpMessageConversionException exception) {
        log.error("捕获HttpMessageConversionException", exception);
        return ResponseData.error(CommonExceptionDefinition.VERIFY_EXCEPTION + "", exception.getCause().getLocalizedMessage());
    }

    /**
     * 常见异常处理方法
     *
     * @param ex
     * @return ResponseData
     */
    @ExceptionHandler(CommonException.class)
    public ResponseData commonExceptionHandler(CommonException ex) {
        log.error("捕获CommonException", ex);
        return ResponseData.error(String.valueOf(ex.getCode()), ex.getMessage());
    }

    /**
     * 参数异常
     * */
    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseData<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("捕获IllegalArgumentException", ex);
        return ResponseData.error(CommonExceptionDefinition.BASIC_EXCEPTION+"", ex.getMessage());
    }

    /**
     * 默认异常处理方法
     *
     * @param ex
     * @return ResponseData
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseData defaultErrorHandler(Exception ex) {
        log.error("捕获Exception", ex);
        return ResponseData.error(ResultResponseEnum.DEFAULT_SERVICE_EXCEPTION.getCode(), ex.getMessage());
    }

    /**
     * 默认异常处理方法
     *
     * @param ex
     * @return ResponseData
     */
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseData defaultErrorHandler(RuntimeException ex) {
        log.error("捕获RuntimeException", ex);
        return ResponseData.error(ResultResponseEnum.DEFAULT_SERVICE_RUNTIME_EXCEPTION.getCode(), ex.getMessage());
    }

}

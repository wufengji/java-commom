package com.wfj.common.exception;

import com.wfj.common.response.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author wfj
 * @Description 全局异常处理
 * @data 2020/4/9
 */
@RestControllerAdvice
@ApiIgnore
public class ExceptionAdviceController {
    private static Logger logger = LoggerFactory.getLogger(ExceptionAdviceController.class);

    /**
     * 参数验证（对象验证）
     */
    @ExceptionHandler(BindException.class)
    public String bindException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        String msg = "参数校验失败:";
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            msg += fieldError.getDefaultMessage() + ", ";
        }
        return ResponseResult.failure("400", msg);
    }

    /**
     * 处理业务异常
     */
    @ExceptionHandler(value = ServiceException.class)
    public String exceptionHandler(ServiceException e) {
        logger.error(e.getMessage(), e);
        return ResponseResult.failure(e.getCode(), e.getMessage());
    }

    /**
     * 其他异常统一处理
     */
    @ExceptionHandler(value = Exception.class)
    public String exceptionHandler(Exception e) {
        logger.error(e.getMessage(), e);
        return ResponseResult.failure("500", "未知错误");
    }
}
package com.wfj.common.response;


import com.alibaba.fastjson.JSON;
import com.wfj.common.exception.ServiceException;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wfj
 * @data 2019/12/4
 */
@Data
public class ResponseResult<T> implements Serializable {
    private T data;
    private String code;
    private String message;
    private Boolean success;

    /**
     * 获取成功返回信息
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> String success(T data) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setSuccess(true);
        responseResult.setData(data);
        responseResult.setCode(CodeEnum.SUCCESS.getCode());
        responseResult.setMessage(CodeEnum.SUCCESS.getMsg());
        return JSON.toJSONString(responseResult);
    }

    public static String success() {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setSuccess(true);
        responseResult.setCode(CodeEnum.SUCCESS.getCode());
        responseResult.setMessage(CodeEnum.SUCCESS.getMsg());
        return JSON.toJSONString(responseResult);
    }

    /**
     * 获取失败返回信息
     *
     * @return
     */
    public static String failure() {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setSuccess(false);
        responseResult.setCode(CodeEnum.FAILURE.getCode());
        responseResult.setMessage(CodeEnum.FAILURE.getMsg());
        return JSON.toJSONString(responseResult);
    }

    public static String failure(ServiceException serviceException) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setSuccess(false);
        responseResult.setCode(serviceException.getCode());
        responseResult.setMessage(serviceException.getMessage());
        return JSON.toJSONString(responseResult);
    }

    public static String failure(CodeEnum codeEnum) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setSuccess(false);
        responseResult.setCode(codeEnum.getCode());
        responseResult.setMessage(codeEnum.getMsg());
        return JSON.toJSONString(responseResult);
    }

    public static String failure(String code, String msg) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setSuccess(false);
        responseResult.setCode(code);
        responseResult.setMessage(msg);
        return JSON.toJSONString(responseResult);
    }
}

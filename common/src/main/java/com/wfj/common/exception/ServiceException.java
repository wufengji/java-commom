package com.wfj.common.exception;


import lombok.Data;

/**
 * @author wfj
 * @data 2019/12/6
 */
@Data
public class ServiceException extends RuntimeException {
    private String code;

    public ServiceException(String code, String message) {
        super(message);
        this.code = code;
    }

    public ServiceException(String message) {
        super(message);
    }
}

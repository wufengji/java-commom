package com.wfj.common.response;

/**
 * @author wfj
 * @data 2019/12/4
 */
public enum CodeEnum {
    /*操作成功*/
    SUCCESS("200", "操作成功"),
    /*操作失败*/
    FAILURE("400", "操作失败"),

    /**
     * 业务异常
     */
    UNAUTHENTICATED("401", "用户未登录"),
    UNAUTHORIZED("403", "未授权，拒绝访问"),
    /**
     * 未指明的异常
     */
    UNSPECIFIED("500", "网络异常，请稍后再试"),
    NO_SERVICE("404", "网络异常, 服务器熔断");
    private String msg;
    private String code;

    private CodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public String getCode() {
        return code;
    }
}

package com.wfj.common.exception;

/**
 * @author wfj
 * @TODO: 断言处理类，用于抛出各种API异常
 * @since 2021/3/3
 */
public class Asserts {
    public static void fail(String message) {
        throw new ServiceException(message);
    }

    /**
     * 为空抛出异常
     *
     * @param object
     * @param message
     */
    public static void notNull(Object object, String message) {
        if (null == object) {
            fail(message);
        }
    }
}

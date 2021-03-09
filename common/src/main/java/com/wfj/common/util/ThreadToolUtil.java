package com.wfj.common.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author wfj
 * @since 2021/1/19
 */
public class ThreadToolUtil {

    public static ScheduledExecutorService getScheduledExecutorService(int corePoolSize, String namingPattern) {
        return new ScheduledThreadPoolExecutor(corePoolSize,
                new BasicThreadFactory.
                        Builder().namingPattern(StringUtils.isBlank(namingPattern) ? "scheduled-pool-%d" : namingPattern).daemon(true).build());
    }
}

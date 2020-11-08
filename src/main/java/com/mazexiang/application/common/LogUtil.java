/*
 * Alipay.com Inc.
 * Copyright (c) 2004-2020 All Rights Reserved.
 */
package com.mazexiang.application.common;


import org.slf4j.Logger;

/**
 * 日志工具类
 * @author mazexiang
 * @version $Id: LogUtil.java
 */
public class LogUtil {


    public static void info(final Logger logger, final Object... obj) {
        if (logger == null) {
            return;
        }
        if (logger.isInfoEnabled()) {
            logger.info(getLogString(obj));
        }
    }

    public static void warn(final Logger logger, final Object... obj) {
        if (logger != null) {
            logger.warn(getLogString(obj));
        }
    }

    public static void warn(final Logger logger, final Throwable throwable, final Object... obj) {
        if (logger != null) {
            logger.warn(getLogString(obj), throwable);
        }
    }
    public static void error(final Logger logger, final Throwable throwable, final Object... obj) {
        if (logger != null) {
            logger.error(getLogString(obj), throwable);
        }
    }


    public static void error(final Logger logger, final Object... obj) {
        if (logger != null) {
            logger.error(getLogString(obj));
        }
    }

    public static String getLogString(final Object... obj) {
        final StringBuilder log = new StringBuilder();
        log.append("[]-->");
        int fst = 1;
        for (final Object o : obj) {
            if (0 == fst) {
                log.append("|");
            } else {
                fst = 0;
            }
            log.append(o);
        }
        return log.toString();
    }

}
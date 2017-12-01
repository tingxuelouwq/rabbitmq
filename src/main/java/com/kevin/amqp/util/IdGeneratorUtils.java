package com.kevin.amqp.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @类名: IdGeneratorUtils
 * @包名：com.kevin.util
 * @作者：kevin[wangqi2017@xinhua.org]
 * @时间：2017/8/8 12:19
 * @版本：1.0
 * @描述：ID生成器工具类，格式为prefix+separator+time+separator+value
 */
public class IdGeneratorUtils {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * 前缀
     **/
    private static final String prefix = "";

    /**
     * 分隔符
     **/
    private static final String separator = "";

    /**
     * 初始值
     **/
    private static final Integer initialValue = 1;

    /**
     * 值
     **/
    private static final AtomicInteger value = new AtomicInteger(initialValue);

    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private IdGeneratorUtils() {
        // no constructor function
    }

    public static String next() {
        lock.readLock().lock();
        StringBuilder idBuilder = new StringBuilder();
        idBuilder.append(prefix)
                .append(separator)
                .append(LocalDateTime.now().format(FORMATTER))
                .append(separator)
                .append(value.getAndIncrement());
        lock.readLock().unlock();
        return idBuilder.toString();
    }
}
